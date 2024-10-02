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

// Function to update rewards for a user based on a transaction
export const updateUserTransactionRewards = async (req, res) => {
  const { phNo, transactionAmount } = req.body;

  try {
    // Step 1: Get the user ID by phone number
    const usersCollection = collection(db, "users");
    const userQuery = query(usersCollection, where("phNo", "==", phNo));
    const querySnapshot = await getDocs(userQuery);

    if (querySnapshot.empty) {
      return res.status(404).send({ message: "User not found" });
    }

    // Assuming there is only one user with the specified phone number
    const userDoc = querySnapshot.docs[0];
    const userId = userDoc.id;

    // Step 2: Calculate reward points based on transaction amount
    const rewardPoints = Math.floor(transactionAmount / 10); // Example: 1 reward point for every 10 units spent

    // Step 3: Get the current month and year
    const currentMonth = new Date().getMonth() + 1; // Adding 1 to convert to 1-based index (1-12)
    const currentYear = new Date().getFullYear();

    // Step 4: Reference to the "rewards" collection
    const rewardsCollectionRef = collection(db, "rewards");
    const userRewardsDocRef = doc(rewardsCollectionRef, userId);

    // Step 5: Check if the user's rewards document exists in the "rewards" collection
    const userRewardsDoc = await getDoc(userRewardsDocRef);

    let monthlyRewards = {};
    if (userRewardsDoc.exists()) {
      // If the document exists, get the existing monthly rewards data
      monthlyRewards = userRewardsDoc.data().monthlyRewards || {};
    }

    // Step 6: Update the rewards for the current month and year
    const monthKey = `${currentYear}-${currentMonth}`; // e.g., "2024-10"
    if (!monthlyRewards[monthKey]) {
      // If rewards for the current month do not exist, initialize them
      monthlyRewards[monthKey] = {
        points: 0,
        lastUpdated: null,
      };
    }

    // Step 7: Update the reward points for the current month
    monthlyRewards[monthKey].points += rewardPoints;
    monthlyRewards[monthKey].lastUpdated = Timestamp.fromDate(new Date());

    // Step 8: Save/update the rewards document in Firestore
    await setDoc(userRewardsDocRef, { monthlyRewards }, { merge: true });

    res.status(200).send({
      message: `Transaction successful. Added ${rewardPoints} points for user ${phNo}.`,
    });
  } catch (error) {
    console.error(error);
    res.status(500).send({ message: "Internal server error" });
  }
};

// Function to get reward points for a specific user by phone number
export const getUserRewardsPoints = async (req, res) => {
  const { phNo } = req.body; // Get the phone number from the request body

  try {
    // Step 1: Retrieve user ID using phone number from the 'users' collection
    const usersCollection = collection(db, "users");
    const userQuery = query(usersCollection, where("phNo", "==", phNo));
    const querySnapshot = await getDocs(userQuery);

    if (querySnapshot.empty) {
      return res.status(404).send({ message: "User not found" });
    }

    // Assuming there's only one user with the specified phone number
    const userDoc = querySnapshot.docs[0];
    const userId = userDoc.id;

    // Step 2: Retrieve the rewards document from the 'rewards' collection using the user ID
    const rewardsCollectionRef = collection(db, "rewards");
    const userRewardsDocRef = doc(rewardsCollectionRef, userId);
    const userRewardsDoc = await getDoc(userRewardsDocRef);

    if (!userRewardsDoc.exists()) {
      return res
        .status(404)
        .send({ message: "Rewards data not found for the user" });
    }

    // Step 3: Get the rewards data and send it in the response
    const rewardsData = userRewardsDoc.data().monthlyRewards || {};
    res.status(200).send({
      message: "Rewards data retrieved successfully",
      rewards: rewardsData,
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
    const rewardsCollectionRef = collection(db, "rewards");
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