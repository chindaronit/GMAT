
import db from "../models/db.js";
import {
  collection,
  doc,
  addDoc,
  getDoc,
  setDoc,
  getDocs,
  query,
  where,
  updateDoc,
} from "firebase/firestore";

const TRANSACTION_COLLECTION = "transactions";

// Function to add a new transaction, storing it under a monthly nested structure
export const addTransaction = async (req, res) => {
  const data = req.body;
  const { userId, txnId, transactionDate, ...transactionDetails } = data;

  try {
    // Parse the month and year from the transaction date in "YYYY-MM" format
    const transactionDateObj = new Date(transactionDate);
    const monthYearKey = `${transactionDateObj.getFullYear()}-${String(
      transactionDateObj.getMonth() + 1
    ).padStart(2, "0")}`; // Format month to always have 2 digits (e.g., "2024-09")

    // Create a reference to the user-specific document in the "transactions" collection
    const transactionDocRef = doc(db, TRANSACTION_COLLECTION, userId);

    // Get the existing transactions for the user (if any)
    const transactionDoc = await getDoc(transactionDocRef);

    let monthlyTransactions = {};
    if (transactionDoc.exists()) {
      // Retrieve existing transactions data
      monthlyTransactions = transactionDoc.data().monthlyTransactions || {};
    }

    // Create a new transaction object
    const newTransaction = {
      txnId,
      transactionDate,
      ...transactionDetails,
    };

    // Update the transactions for the specified month
    if (!monthlyTransactions[monthYearKey]) {
      monthlyTransactions[monthYearKey] = [];
    }
    monthlyTransactions[monthYearKey].push(newTransaction);

    // Update the document with the new transaction data
    await setDoc(transactionDocRef, { monthlyTransactions }, { merge: true });

    res.status(200).send({ msg: "Transaction added successfully" });
  } catch (error) {
    console.error("Error adding transaction:", error);
    res.status(500).send({ message: "Internal server error" });
  }
};

// Function to get all transactions for a user in a particular month (format: YYYY-MM)
export const getAllTransactionsForMonth = async (req, res) => {
  const { userId, month,year } = req.body; // Expect monthYear in "YYYY-MM" format

  try {
    // Create a reference to the user-specific document in the "transactions" collection
    const transactionDocRef = doc(db, TRANSACTION_COLLECTION, userId);
    const monthYear = `${year}-${month}`;

    // Get the user's transaction document
    const transactionDoc = await getDoc(transactionDocRef);

    if (!transactionDoc.exists()) {
      return res
        .status(404)
        .send({ message: "No transactions found for the user" });
    }

    // Retrieve the monthly transactions for the specified month
    const monthlyTransactions = transactionDoc.data().monthlyTransactions || {};
    const transactionsForMonth = monthlyTransactions[monthYear];

    if (!transactionsForMonth) {
      return res
        .status(404)
        .send({ message: `No transactions found for the month: ${monthYear}` });
    }

    // Send the transactions for the specified month
    res
      .status(200)
      .send({
        message: "Transactions retrieved successfully",
        transactions: transactionsForMonth,
      });
  } catch (error) {
    console.error("Error retrieving transactions:", error);
    res.status(500).send({ message: "Internal server error" });
  }
};

// Function to get a specific transaction by transactionId and month (optional)
export const getTransactionByTxnId = async (req, res) => {
  const { userId, txnId, monthYear } = req.body; // Optionally provide monthYear to narrow down search

  try {
    const transactionDocRef = doc(db, TRANSACTION_COLLECTION, userId);
    const transactionDoc = await getDoc(transactionDocRef);

    if (!transactionDoc.exists()) {
      return res
        .status(404)
        .send({ message: "User transaction data not found" });
    }

    // If monthYear is provided, narrow down to that month; otherwise, search all months
    const monthlyTransactions = transactionDoc.data().monthlyTransactions || {};
    const targetMonths = monthYear
      ? [monthYear]
      : Object.keys(monthlyTransactions);

    let foundTransaction = null;
    for (const month of targetMonths) {
      const transactions = monthlyTransactions[month] || [];
      foundTransaction = transactions.find((txn) => txn.txnId === txnId);
      if (foundTransaction) break; // Stop searching once found
    }

    if (!foundTransaction) {
      return res.status(404).send({ message: "Transaction not found" });
    }

    res
      .status(200)
      .send({
        message: "Transaction retrieved successfully",
        transaction: foundTransaction,
      });
  } catch (error) {
    console.error("Error retrieving transaction:", error);
    res.status(500).send({ message: "Internal server error" });
  }
};

// Function to get all transactions for a user across all months
export const getAllTransactionsForUser = async (req, res) => {
  const { userId } = req.body; // Extract the userId from the request body

  try {
    // Create a reference to the user-specific document in the "transactions" collection
    const transactionDocRef = doc(db, TRANSACTION_COLLECTION, userId);

    // Get the user's transaction document
    const transactionDoc = await getDoc(transactionDocRef);

    if (!transactionDoc.exists()) {
      return res
        .status(404)
        .send({ message: "No transactions found for the user" });
    }

    // Retrieve the monthly transactions for the user
    const monthlyTransactions = transactionDoc.data().monthlyTransactions || {};

    // Compile all transactions across all months
    const allTransactions = Object.values(monthlyTransactions).flat();

    // Send the compiled list of transactions in the response
    res.status(200).send({
      message: "All transactions retrieved successfully",
      transactions: allTransactions,
    });
  } catch (error) {
    console.error("Error retrieving all transactions for user:", error);
    res.status(500).send({ message: "Internal server error" });
  }
};


// import db from "../models/db.js";
// import {
//   collection,
//   doc,
//   addDoc,
//   getDocs,
//   query,
//   where,
// } from "firebase/firestore";

// const TRANSACTION_COLLECTION = "transactions";

// // Function to get a transaction by transactionId
// export const getTransaction = async (req, res) => {
//   const txnId = req.body.txnId;

//   try {
//     const transactionCollection = collection(db, TRANSACTION_COLLECTION);
//     const txnQuery = query(transactionCollection, where("txnId", "==", txnId));
//     const querySnapshot = await getDocs(txnQuery);

//     // Check if a transaction with the specified phone number exists
//     if (querySnapshot.empty) {
//       return res.status(404).send({ message: "Transaction does not exist" });
//     }

//     // Assuming there is only one transaction with the specified phone number
//     const transaction = querySnapshot.docs[0].data();

//     // Send the transaction data in the response
//     res.status(200).send({ id: querySnapshot.docs[0].id, ...transaction });
//   } catch (error) {
//     console.error(error);
//     res.status(500).send({ message: "Internal server error" });
//   }
// };

// // Function to get all transaction of userId
// export const getAllTransactions = async (req, res) => {
//   const userId = req.body.userId;
//   try {
//     const transactionCollection = collection(db, TRANSACTION_COLLECTION);

//     const transactionQuery = query(
//       transactionCollection,
//       where("userId", "==", userId)
//     );
//     const querySnapshot = await getDocs(transactionQuery);

//     let transactions;
//     querySnapshot.forEach((doc) => {
//       trasactions = { id: doc.id, ...doc.data() };
//     });

//     // Send the user data in the response
//     res.status(200).send(transactions);
//   } catch (error) {
//     console.error(error);
//     res.status(500).send({ message: "Internal server error" });
//   }
// };

// // Function to update an existing transaction by ID
// export const addTransaction = async (req, res) => {
//   const data = req.body;
//   try {

//     const transactionRef = await addDoc(collection(db, TRANSACTION_COLLECTION), data);
//     res.status(200).send({ msg: "Transaction added successfully" });
//   } catch (error) {
//     console.error(error);
//     res.status(500).send({ message: "Internal server error" });
//   }
// };
