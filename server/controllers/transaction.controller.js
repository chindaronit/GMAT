import db from "../models/db.js";
import {
  collection,
  doc,
  addDoc,
  getDoc,
  setDoc,
  Timestamp,
} from "firebase/firestore";

const TRANSACTION_COLLECTION = "transactions";

// Function to add a new transaction, storing it under a monthly nested structure
export const addTransaction = async (req, res) => {
  // const data = req.body;
  const { userId, payerId, payeeId, type, amount, gstin } = req.body;
  if (!payerId || !payeeId || !type || !amount || !gstin) {
    return res.status(400).send({
      message: "Bad Request: Missing or invalid fields in the request",
    });
  }

  const data = {
    payerId: payerId,
    payeeId: payeeId,
    type: type,
    amount: amount,
    gstin: gstin,
    status: "1",
    Timestamp:Timestamp.now(),
  };

  try {
    const currentMonth = new Date().getMonth() + 1;
    const currentYear = new Date().getFullYear();
    const monthYearKey = `${currentYear}-${String(currentMonth).padStart(
      2,
      "0"
    )}`;
    const transactionDocRef = doc(db, TRANSACTION_COLLECTION, userId);
    const transactionDoc = await getDoc(transactionDocRef);
    let monthlyTransactions = {};
    if (transactionDoc.exists()) {
      monthlyTransactions = transactionDoc.data().monthlyTransactions || {};
    }
    const newTransactionRef = await addDoc(
      collection(db, TRANSACTION_COLLECTION),
      {
        data,
        userId,
      }
    );
    const txnId = newTransactionRef.id;
    const newTransaction = {
      txnId,
      data,
    };
    if (!monthlyTransactions[monthYearKey]) {
      monthlyTransactions[monthYearKey] = [];
    }
    monthlyTransactions[monthYearKey].push(newTransaction);
    await setDoc(transactionDocRef, { monthlyTransactions }, { merge: true });
    res.status(200).send({ msg: "Transaction added successfully", txnId });
  } catch (error) {
    console.error("Error adding transaction:", error);
    res.status(500).send({ message: "Internal server error" });
  }
};

// Function to get all transactions for a user in a particular month (format: YYYY-MM)
export const getAllTransactionsForMonth = async (req, res) => {
  const { userId, month, year } = req.body;

  try {
    const transactionDocRef = doc(db, TRANSACTION_COLLECTION, userId);
    const monthYear = `${year}-${month}`;
    const transactionDoc = await getDoc(transactionDocRef);
    if (!transactionDoc.exists()) {
      return res
        .status(404)
        .send({ message: "No transactions found for the user" });
    }
    const monthlyTransactions = transactionDoc.data().monthlyTransactions || {};
    const transactionsForMonth = monthlyTransactions[monthYear];
    if (!transactionsForMonth) {
      return res
        .status(404)
        .send({ message: `No transactions found for the month: ${monthYear}` });
    }
    res.status(200).send({
      message: "Transactions retrieved successfully",
      transactions: transactionsForMonth,
    });
  } catch (error) {
    console.error("Error retrieving transactions:", error);
    res.status(500).send({ message: "Internal server error" });
  }
};

// Function to get a specific transaction by transactionId
export const getTransactionByTxnId = async (req, res) => {
  const { userId, txnId } = req.body;
  try {
    const transactionDocRef = doc(db, TRANSACTION_COLLECTION, userId);
    const transactionDoc = await getDoc(transactionDocRef);

    if (!transactionDoc.exists()) {
      return res
        .status(404)
        .send({ message: "User transaction data not found" });
    }
    const monthlyTransactions = transactionDoc.data().monthlyTransactions || {};
    let foundTransaction = null;
    for (const month in monthlyTransactions) {
      const transactions = monthlyTransactions[month] || [];
      foundTransaction = transactions.find((txn) => txn.txnId === txnId);
      if (foundTransaction) break;
    }
    if (!foundTransaction) {
      return res.status(404).send({ message: "Transaction not found" });
    }
    res.status(200).send({
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
  const { userId } = req.body;
  try {
    const transactionDocRef = doc(db, TRANSACTION_COLLECTION, userId);
    const transactionDoc = await getDoc(transactionDocRef);
    if (!transactionDoc.exists()) {
      return res
        .status(404)
        .send({ message: "No transactions found for the user" });
    }
    const monthlyTransactions = transactionDoc.data().monthlyTransactions || {};
    const allTransactions = Object.values(monthlyTransactions).flat();
    res.status(200).send({
      message: "All transactions retrieved successfully",
      transactions: allTransactions,
    });
  } catch (error) {
    console.error("Error retrieving all transactions for user:", error);
    res.status(500).send({ message: "Internal server error" });
  }
};

// Function to get all transactions for a particular user using userId and payeeId (present in transaction details)
export const getUserTransactionsByUserIdAndPayeeId = async (req, res) => {
  const { userId, payeeId } = req.body;
  try {
    const transactionDocRef = doc(db, TRANSACTION_COLLECTION, userId);
    const transactionDoc = await getDoc(transactionDocRef);
    if (!transactionDoc.exists()) {
      return res
        .status(404)
        .send({ message: "No transaction data found for the user" });
    }
    const monthlyTransactions = transactionDoc.data().monthlyTransactions || {};
    let allTransactions = [];
    for (const monthKey in monthlyTransactions) {
      if (monthlyTransactions.hasOwnProperty(monthKey)) {
        allTransactions = allTransactions.concat(monthlyTransactions[monthKey]);
      }
    }
    const filteredTransactions = allTransactions.filter((transaction) => {
      return transaction.payee === payeeId;
    });

    if (filteredTransactions.length === 0) {
      return res
        .status(404)
        .send({ message: "No transactions found for the specified payee" });
    }
    res.status(200).send({
      message: "Transactions retrieved successfully",
      transactions: filteredTransactions,
    });
  } catch (error) {
    console.error("Error retrieving transactions:", error);
    res.status(500).send({ message: "Internal server error" });
  }
};
