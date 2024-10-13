import db from "../models/db.js";
import {
  collection,
  doc,
  setDoc,
  getDocs,
  getDoc,
  Timestamp,
} from "firebase/firestore";

const LEADERBOARD_COLLECTION = "rewards";
const USER_COLLECTION = "users";

// Function to update rewards for a user based on a transaction
export const updateUserTransactionRewards = async (req, res) => {
  const { userId, transactionAmount } = req.body;
  console.log(req.body);
  if (!userId || !transactionAmount) {
    console.error("Error: Missing required fields in the request body");
    return res
      .status(400)
      .send({ message: "Bad Request: userId, transactionAmount are required" });
  }
  try {
    const rewardPoints = Math.floor(transactionAmount / 10);
    const currentMonth = new Date().getMonth() + 1;
    const currentYear = new Date().getFullYear();
    const rewardsCollectionRef = collection(db, LEADERBOARD_COLLECTION);
    const userRewardsDocRef = doc(rewardsCollectionRef, userId);
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
      message: `Transaction successful. Added ${rewardPoints} points for user ${userId}.`,
    });
  } catch (error) {
    console.error(error);
    res.status(500).send({ message: "Internal server error" });
  }
};

// Function to get reward points for a specific user by phone number and a particular month (format: YYYY-MM)
export const getUserRewardsPointsForMonth = async (req, res) => {
  const { userId, month, year } = req.query;

  if (!userId || !month || !year) {
    console.error("Error: Missing required fields in the request body");
    return res
      .status(400)
      .send({ message: "Bad Request: userId, month, and year are required" });
  }

  try {
    // Fetch user's name from the USER_COLLECTION
    const userDocRef = doc(collection(db, USER_COLLECTION), userId);
    const userDoc = await getDoc(userDocRef);

    if (!userDoc.exists()) {
      return res.status(404).send({ message: "User not found" });
    }

    const userName = userDoc.data().name;
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
      return res
        .status(404)
        .send({ message: `No rewards data found for the month: ${month}` });
    }
    res.status(200).send({
      message: `Rewards data retrieved successfully for the month: ${month}`,
      rewards: {
        name: userName,
        month: month,
        year: year,
        points: rewardsForMonth.points,
      },
    });
  } catch (error) {
    console.error("Error retrieving user rewards points:", error);
    res.status(500).send({ message: "Internal server error" });
  }
};

// Function to get all users in descending order by rewards points for a specific month
export const getUsersByRewardsForMonth = async (req, res) => {
  const { month, year } = req.query;
  if (!month || !year) {
    console.error("Error: Missing required fields in the request body");
    return res
      .status(400)
      .send({ message: "Bad Request: month and year are required" });
  }
  try {
    const rewardsCollectionRef = collection(db, LEADERBOARD_COLLECTION);
    const querySnapshot = await getDocs(rewardsCollectionRef);
    if (querySnapshot.empty) {
      return res.status(404).send({ message: "No rewards data found" });
    }
    const monthKey = `${year}-${month}`;
    const usersWithRewards = [];
    for (const userDoc of querySnapshot.docs) {
      const userId = userDoc.id;
      const userRewardsData = userDoc.data().monthlyRewards || {};
      if (userRewardsData[monthKey]) {
        const { points } = userRewardsData[monthKey];
        const userDocRef = doc(collection(db, USER_COLLECTION), userId);
        const userDocSnapshot = await getDoc(userDocRef);

        if (userDocSnapshot.exists()) {
          const userData = userDocSnapshot.data();
          const userInfo = {
            userId: userId,
            name: userData.name,
            points: points,
            month: month,
            year: year,
          };
          usersWithRewards.push(userInfo);
        }
      }
    }
    usersWithRewards.sort((a, b) => b.points - a.points);
    res.status(200).send({
      message: `Users sorted by rewards points for ${monthKey}`,
      data: usersWithRewards,
    });
  } catch (error) {
    console.error("Error retrieving users by rewards:", error);
    res.status(500).send({ message: "Internal server error" });
  }
};
