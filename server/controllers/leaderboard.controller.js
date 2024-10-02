import db from "../models/db.js";
import {
  collection,
  doc,
  setDoc,
  getDocs,
  updateDoc,
  getDoc,
  query,
  where,
  Timestamp,
} from "firebase/firestore";

const LEADERBOARD_COLLECTION="rewards"

// Function to update rewards for a user based on a transaction
export const updateUserTransactionRewards = async (req, res) => {
  const { userId, transactionAmount } = req.body;

  try {
    const rewardPoints = Math.floor(transactionAmount / 10); 
    const currentMonth = new Date().getMonth() + 1;
    const currentYear = new Date().getFullYear();
    const rewardsCollectionRef = collection(db, LEADERBOARD_COLLECTION);
    const userRewardsDocRef  = doc(rewardsCollectionRef, userId);
    const userRewardsDoc = await getDoc(userRewardsDocRef);

    let monthlyRewards = {};
    if (userRewardsDoc.exists()) {
      monthlyRewards = userRewardsDoc.data().monthlyRewards || {};
    }
    const monthKey = `${currentYear}-${currentMonth}`;
    if (!monthlyRewards[monthKey]) {
      monthlyRewards[monthKey] = {
        points: 0,
        lastUpdated: null,
      };
    }
    monthlyRewards[monthKey].points += rewardPoints;
    monthlyRewards[monthKey].lastUpdated = Timestamp.fromDate(new Date());
    await setDoc(userRewardsDocRef, { monthlyRewards }, { merge: true });
    res.status(200).send({
      message: `Transaction successful. Added ${rewardPoints} points for user ${phNo}.`,
    });
  } catch (error) {
    console.error(error);
    res.status(500).send({ message: "Internal server error" });
  }
};

// Function to get reward points for a specific user by phone number and a particular month (format: YYYY-MM)
export const getUserRewardsPointsForMonth = async (req, res) => {
  const { userId, month,year } = req.body; 

  try {
    const monthKey = `${year}-${month}`;
    const rewardsCollectionRef = collection(db, LEADERBOARD_COLLECTION);
    const userRewardsDocRef = doc(rewardsCollectionRef, userId);
    const userRewardsDoc = await getDoc(userRewardsDocRef);

    if (!userRewardsDoc.exists()) {
      return res  
        .status(404)
        .send({ message: "Rewards data not found for the user" });
    }
    const rewardsData = userRewardsDoc.data().monthlyRewards || {};
    const rewardsForMonth = rewardsData[monthKey];

    if (!rewardsForMonth) {
      return res.status(404).send({ message: `No rewards data found for the month: ${month}` });
    }
    res.status(200).send({
      message: `Rewards data retrieved successfully for the month: ${month}`,
      rewards: {
        monthKey,
        points: rewardsForMonth.points,
        lastUpdated: rewardsForMonth.lastUpdated,
      },
    });
  } catch (error) {
    console.error("Error retrieving user rewards points:", error);
    res.status(500).send({ message: "Internal server error" });
  }
};

// Function to get all users in descending order by rewards points for a specific month
export const getUsersByRewardsForMonth = async (req, res) => {
  const { month, year } = req.body; // Example: month = 10 (October), year = 2024

  try {
    // Step 1: Get all documents from the 'rewards' collection
    const rewardsCollectionRef = collection(db, LEADERBOARD_COLLECTION);
    const querySnapshot = await getDocs(rewardsCollectionRef);

    if (querySnapshot.empty) {
      return res.status(404).send({ message: "No rewards data found" });
    }

    const monthKey = `${year}-${month}`; // Format as "YYYY-MM" for example, "2024-10"
    const usersWithRewards = [];

    // Step 2: Iterate over each user document and collect their rewards points for the specified month
    for (const userDoc of querySnapshot.docs) {
      const userId = userDoc.id;
      const userRewardsData = userDoc.data().monthlyRewards || {};

      // Check if the user has rewards for the specified month
      if (userRewardsData[monthKey]) {
        const { points } = userRewardsData[monthKey];

        // Fetch the user's name or phone number from the 'users' collection
        const userDocRef = doc(db, "users", userId);
        const userDocSnapshot = await getDoc(userDocRef);

        if (userDocSnapshot.exists()) {
          const userData = userDocSnapshot.data();
          const userInfo = {
            userId,
            phNo: userData.phNo,
            name: userData.name || "Unknown",
            points,
          };
          usersWithRewards.push(userInfo);
        }
      }
    }

    // Step 3: Sort the users based on their rewards points in descending order
    usersWithRewards.sort((a, b) => b.points - a.points);

    // Step 4: Return the sorted users with rewards
    res.status(200).send({
      message: `Users sorted by rewards points for ${monthKey}`,
      users: usersWithRewards,
    });
  } catch (error) {
    console.error("Error retrieving users by rewards:", error);
    res.status(500).send({ message: "Internal server error" });
  }
};