import db from "../models/db.js";
import {
  collection,
  doc,
  addDoc,
  getDoc,
  getDocs,
  setDoc,
  Timestamp,
  deleteDoc,
} from "firebase/firestore";

const TRANSACTION_COLLECTION = "transactions";

// Function to add a new transaction, storing it under a monthly nested structure
export const addTransaction = async (req, res) => {
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
    Timestamp: Timestamp.now(),
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
      data
    );
    const txnId = newTransactionRef.id;
    const newTransaction = { txnId, ...data };
    if (!monthlyTransactions[monthYearKey]) {
      monthlyTransactions[monthYearKey] = [];
    }
    monthlyTransactions[monthYearKey].push(newTransaction);
    await setDoc(transactionDocRef, { monthlyTransactions }, { merge: true });
    // Delete the transaction from the collection (if you need to delete the entire transaction)
    await deleteDoc(doc(db, TRANSACTION_COLLECTION, txnId));
    res.status(200).send({ msg: "Transaction added successfully", txnId });
  } catch (error) {
    console.error("Error adding transaction:", error);
    res.status(500).send({ message: "Internal server error" });
  }
};

// Function to get all transactions for a user in a particular month (format: YYYY-MM)
export const getAllTransactionsForMonth = async (req, res) => {
  const { userId, month, year } = req.body;
  if (!userId || !month || !year) {
    return res.status(400).send({
      message: "Bad Request: Missing or invalid fields in the request",
    });
  }

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
  if (!userId || !txnId) {
    return res.status(400).send({
      message: "Bad Request: Missing or invalid fields in the request",
    });
  }

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
  if (!userId) {
    return res.status(400).send({
      message: "Bad Request: Missing or invalid fields in the request",
    });
  }
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
  if (!userId || !payeeId) {
    return res.status(400).send({
      message: "Bad Request: Missing or invalid fields in the request",
    });
  }

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
      return transaction.payeeId === payeeId;
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

// Function to get all transactions for a particular GSTIN across all users
export const getAllTransactionsForGstin = async (req, res) => {
  const { gstin } = req.body;

  // Validate input GSTIN
  if (!gstin) {
    return res.status(400).send({
      message: "Bad Request: Missing or invalid GSTIN in the request",
    });
  }

  try {
    const transactionsCollectionRef = collection(db, TRANSACTION_COLLECTION);
    const transactionDocsSnapshot = await getDocs(transactionsCollectionRef);

    let matchingTransactions = [];
    transactionDocsSnapshot.forEach((docSnapshot) => {
      const userTransactions = docSnapshot.data().monthlyTransactions || {};
      let allUserTransactions = [];
      for (const monthKey in userTransactions) {
        if (userTransactions.hasOwnProperty(monthKey)) {
          allUserTransactions = allUserTransactions.concat(
            userTransactions[monthKey]
          );
        }
      }
      const gstinFilteredTransactions = allUserTransactions.filter(
        (transaction) => transaction.data.gstin === gstin
      );
      matchingTransactions = matchingTransactions.concat(
        gstinFilteredTransactions
      );
    });

    if (matchingTransactions.length === 0) {
      return res.status(404).send({
        message: `No transactions found for the GSTIN: ${gstin}`,
      });
    }
    res.status(200).send({
      message: "Transactions retrieved successfully",
      transactions: matchingTransactions,
    });
  } catch (error) {
    console.error("Error retrieving transactions for GSTIN:", error);
    res.status(500).send({ message: "Internal server error" });
  }
};

// Function to get all transactions for a particular payerId and payeeId
export const getAllTransactionsForPayerIdAndPayeeId = async (req, res) => {
  const { payerId, payeeId } = req.body;

  if (!payerId || !payeeId) {
    return res.status(400).send({
      message:
        "Bad Request: Missing or invalid payerId or payeeId in the request",
    });
  }

  try {
    const transactionsCollectionRef = collection(db, TRANSACTION_COLLECTION);
    const transactionDocsSnapshot = await getDocs(transactionsCollectionRef);

    let matchingTransactions = [];

    // Iterate over each user transaction document
    transactionDocsSnapshot.forEach((docSnapshot) => {
      const monthlyTransactions = docSnapshot.data().monthlyTransactions || {};
      
      // Iterate over each month to find transactions
      for (const month in monthlyTransactions) {
        const transactions = monthlyTransactions[month] || [];

        // Filter transactions by payerId and payeeId
        const filteredTransactions = transactions.filter((transaction) => {
          return (
            transaction.payerId === payerId && transaction.payeeId === payeeId
          );
        });

        matchingTransactions = matchingTransactions.concat(filteredTransactions);
      }
    });

    // Sort the transactions by timestamp in descending order
    matchingTransactions.sort(
      (a, b) => b.Timestamp.toDate() - a.Timestamp.toDate()
    );

    // Check if any transactions were found
    if (matchingTransactions.length === 0) {
      return res.status(404).send({
        message: `No transactions found for the payerId: ${payerId} and payeeId: ${payeeId}`,
      });
    }

    res.status(200).send({
      message: "Transactions retrieved successfully",
      transactions: matchingTransactions,
    });
  } catch (error) {
    console.error(
      "Error retrieving transactions for the specified payerId and payeeId:",
      error
    );
    res.status(500).send({ message: "Internal server error" });
  }
};


// Function to get all transactions for a particular GSTIN across all users for a specified month
export const getAllTransactionsForGstinInMonth = async (req, res) => {
  const { gstin, month, year } = req.body;
  const monthYear = `${year}-${month}`;

  // Validate input GSTIN and monthYear (format: YYYY-MM)
  if (!gstin || !monthYear) {
    return res.status(400).send({
      message:
        "Bad Request: Missing or invalid GSTIN or monthYear in the request",
    });
  }

  try {
    const transactionsCollectionRef = collection(db, TRANSACTION_COLLECTION);
    const transactionDocsSnapshot = await getDocs(transactionsCollectionRef);
    let matchingTransactions = [];
    transactionDocsSnapshot.forEach((docSnapshot) => {
      const userTransactions = docSnapshot.data().monthlyTransactions || {};
      const transactionsForMonth = userTransactions[monthYear] || [];
      const gstinFilteredTransactions = transactionsForMonth.filter(
        (transaction) => transaction.data.gstin === gstin
      );
      matchingTransactions = matchingTransactions.concat(
        gstinFilteredTransactions
      );
    });

    if (matchingTransactions.length === 0) {
      return res.status(404).send({
        message: `No transactions found for the GSTIN: ${gstin} in the month: ${monthYear}`,
      });
    }
    res.status(200).send({
      message: "Transactions retrieved successfully",
      transactions: matchingTransactions,
    });
  } catch (error) {
    console.error(
      "Error retrieving transactions for GSTIN in the specified month:",
      error
    );
    res.status(500).send({ message: "Internal server error" });
  }
};

// Function to get all transactions for a particular GSTIN across all users for a specific year
export const getAllTransactionsForGstinInYear = async (req, res) => {
  const { gstin, year } = req.body;
  if (!gstin || !year) {
    return res.status(400).send({
      message: "Bad Request: Missing or invalid GSTIN or year in the request",
    });
  }
  try {
    const transactionsCollectionRef = collection(db, TRANSACTION_COLLECTION);
    const transactionDocsSnapshot = await getDocs(transactionsCollectionRef);
    let matchingTransactions = [];
    transactionDocsSnapshot.forEach((docSnapshot) => {
      const userTransactions = docSnapshot.data().monthlyTransactions || {};
      for (const monthKey in userTransactions) {
        if (monthKey.startsWith(year)) {
          const transactionsForMonth = userTransactions[monthKey] || [];
          const gstinFilteredTransactions = transactionsForMonth.filter(
            (transaction) => {
              return transaction.gstin === gstin;
            }
          );
          matchingTransactions = matchingTransactions.concat(
            gstinFilteredTransactions
          );
        }
      }
    });

    if (matchingTransactions.length === 0) {
      return res.status(404).send({
        message: `No transactions found for the GSTIN: ${gstin} in the year: ${year}`,
      });
    }
    res.status(200).send({
      message: "Transactions retrieved successfully",
      transactions: matchingTransactions,
    });
  } catch (error) {
    console.error(
      "Error retrieving transactions for GSTIN in the specified year:",
      error
    );
    res.status(500).send({ message: "Internal server error" });
  }
};
