import db from "../models/db.js";
import {
  collection,
  doc,
  getDoc,
  getDocs,
  setDoc,
  Timestamp,
  where,
  query,
} from "firebase/firestore";

const TRANSACTION_COLLECTION = "transactions";
const USER_COLLECTION = "users";

// Function to add a new transaction, storing it under a monthly nested structure
export const addTransaction = async (req, res) => {
  const { userId } = req.query;
  const { payerId, payeeId, type, amount, gstin, name } = req.body;

  if (!payerId || !payeeId || !amount || !name) {
    return res.status(400).send({
      message: "Bad Request: Missing or invalid fields in the request",
    });
  }

  let data = {
    payerId: payerId,
    payeeId: payeeId,
    type: type,
    amount: amount,
    gstin: gstin,
    status: "1",
    name: name,
    timestamp: Timestamp.now(),
  };

  try {
    const currentMonth = new Date().getMonth() + 1;
    const currentYear = new Date().getFullYear();
    const monthYearKey = `${currentYear}-${String(currentMonth).padStart(2, "0")}`;
    const transactionDocRef = doc(db, TRANSACTION_COLLECTION, userId);
    const transactionDoc = await getDoc(transactionDocRef);
    let monthlyTransactions = {};
    if (transactionDoc.exists()) {
      monthlyTransactions = transactionDoc.data().monthlyTransactions || {};
    }
    const txnId = `${userId}_${Timestamp.now().seconds}`;
    const newTransaction = { txnId, ...data };
    if (!monthlyTransactions[monthYearKey]) {
      monthlyTransactions[monthYearKey] = [];
    }
    monthlyTransactions[monthYearKey].push(newTransaction);
    await setDoc(transactionDocRef, { monthlyTransactions }, { merge: true });
    res.status(200).send({ msg: "Transaction added successfully", txnId: txnId });
  } catch (error) {
    console.error("Error adding transaction:", error);
    res.status(500).send({ message: "Internal server error" });
  }
};

// Function to get all transactions for a user in a particular month (format: YYYY-MM)
export const getAllTransactionsForMonth = async (req, res) => {
  const { userId, month, year } = req.query;

  if (!userId || !month || !year) {
    return res.status(400).send({
      message: "Bad Request: Missing or invalid fields in the request",
    });
  }

  try {
    const transactionDocRef = doc(db, TRANSACTION_COLLECTION, userId);
    const monthYear = `${year}-${month}`;
    const transactionDoc = await getDoc(transactionDocRef);
    // if (!transactionDoc.exists()) {
    //   return res.status(404).send({ message: "No transactions found for the user" });
    // }
    const monthlyTransactions = transactionDoc.data().monthlyTransactions || {};
    const transactionsForMonth = monthlyTransactions[monthYear];
    const sortedTransactions = transactionsForMonth.sort((a, b) => {
      return b.timestamp.seconds - a.timestamp.seconds;
    });

    res.status(200).send({
      message: "Transactions retrieved successfully",
      transactions: sortedTransactions,
    });
  } catch (error) {
    console.error("Error retrieving transactions:", error);
    res.status(500).send({ message: "Internal server error" });
  }
};

// Function to get all transactions for a payee in a particular month (format: YYYY-MM)
export const getTransactionsByPayeeForMonth = async (req, res) => {
  const { vpa, month, year } = req.query;

  if (!vpa || !month || !year) {
    return res.status(400).send({
      message: "Bad Request: Missing or invalid fields in the request",
    });
  }

  try {
    const transactionCollectionRef = collection(db, TRANSACTION_COLLECTION);
    const transactionQuerySnapshot = await getDocs(transactionCollectionRef);

    // if (transactionQuerySnapshot.empty) {
    //   return res.status(404).send({ message: "No transactions found" });
    // }

    const allTransactions = [];
    transactionQuerySnapshot.forEach((doc) => {
      const monthlyTransactions = doc.data().monthlyTransactions || {};
      const transactions = Object.values(monthlyTransactions).flat();
      allTransactions.push(...transactions);
    });

    const monthYearKey = `${year}-${month}`;;
    const filteredTransactions = allTransactions.filter((txn) => {
      const txnMonthYear = `${new Date(txn.timestamp.seconds * 1000).getFullYear()}-${String(new Date(txn.timestamp.seconds * 1000).getMonth() + 1).padStart(2, '0')}`;
      return txn.payeeId.trim() === vpa.trim() && txnMonthYear === monthYearKey;
    });
    const sortedTransactions = filteredTransactions.sort((a, b) => {
      return b.timestamp.seconds - a.timestamp.seconds;
    });

    res.status(200).send({
      message: "Transactions retrieved successfully",
      transactions: sortedTransactions,
    });
  } catch (error) {
    console.error("Error retrieving transactions:", error);
    res.status(500).send({ message: "Internal server error" });
  }
};


// Function to get a specific transaction by transactionId
export const getTransactionByTxnId = async (req, res) => {
  const { userId, txnId } = req.query;
  if (!userId || !txnId) {
    return res.status(400).send({
      message: "Bad Request: Missing or invalid fields in the request",
    });
  }

  try {
    const transactionDocRef = doc(db, TRANSACTION_COLLECTION, userId);
    const transactionDoc = await getDoc(transactionDocRef);

    // if (!transactionDoc.exists()) {
    //   return res
    //     .status(404)
    //     .send({ message: "User transaction data not found" });
    // }
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

export const getRecentTransactionsForUser = async (req, res) => {
  const { userId } = req.query;

  if (!userId) {
    return res.status(400).send({
      message: "Bad Request: Missing or invalid userId in the request",
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
    const allTransactions = Object.values(monthlyTransactions)
      .flat()
      .sort((a, b) => b.timestamp.seconds - a.timestamp.seconds);
    const payerTransactions = allTransactions.filter((txn) => txn.type === "0");
    const usersCollection = collection(db, USER_COLLECTION);
    const transactionsByPayee = {};

    for (const txn of payerTransactions) {
      const vpa = txn.payeeId.trim();
      const userQuery = query(usersCollection, where("vpa", "==", vpa));
      const payeeQuerySnapshot = await getDocs(userQuery);

      if (!payeeQuerySnapshot.empty) {
        payeeQuerySnapshot.forEach((payeeDoc) => {
          const payeeDetails = payeeDoc.data();
          const payeeId = payeeDoc.id;
          if (!transactionsByPayee[payeeId]) {
            transactionsByPayee[payeeId] = {
              payeeDetails: {
                ...payeeDetails,
                userId: payeeId,
              },
              transactions: [],
            };
          }
          transactionsByPayee[payeeId].transactions.push(txn);
        });
      } else {
        console.log(`Payee with ID ${vpa} does not exist.`);
      }
    }
    const sortedPayees = Object.values(transactionsByPayee)
      .sort((a, b) => {
        const latestTxnA = a.transactions[0].timestamp.seconds;
        const latestTxnB = b.transactions[0].timestamp.seconds;
        return latestTxnB - latestTxnA;
      })
      .slice(0, 12);
    const result = sortedPayees.map((payee) => ({
      userDetails: payee.payeeDetails,
      transactions: payee.transactions,
    }));

    res.status(200).send({
      message: "Recent transactions retrieved successfully",
      data: result,
    });
  } catch (error) {
    console.error("Error retrieving recent transactions for user:", error);
    res.status(500).send({ message: "Internal server error" });
  }
};

export const getRecentTransactionsForMerchant = async (req, res) => {
  const { vpa } = req.query;

  if (!vpa) {
    return res.status(400).send({
      message: "Bad Request: Missing or invalid vpa in the request",
    });
  }

  try {
    const transactionCollectionRef = collection(db, TRANSACTION_COLLECTION);
    const transactionQuerySnapshot = await getDocs(transactionCollectionRef);

    // if (transactionQuerySnapshot.empty) {
    //   return res.status(404).send({ message: "No transactions found" });
    // }
    const allTransactions = [];
    transactionQuerySnapshot.forEach((doc) => {
      const monthlyTransactions = doc.data().monthlyTransactions || {};
      const transactions = Object.values(monthlyTransactions).flat();
      allTransactions.push(...transactions);
    });
    
    const sortedTransactions = allTransactions.sort(
      (a, b) => b.timestamp.seconds - a.timestamp.seconds
    );

    const merchantTransactions = sortedTransactions.filter(
      (txn) => txn.payeeId.trim() === vpa.trim()
    );

    // if (!merchantTransactions.length) {
    //   return res.status(404).send({
    //     message: "No transactions found for the given VPA",
    //   });
    // }

    const usersCollection = collection(db, USER_COLLECTION);
    const transactionsGroupedByPayer = {};
    for (const txn of merchantTransactions) {
      const payerId = txn.payerId.trim();
      const payerQuery = query(usersCollection, where("vpa", "==", payerId));
      const payerQuerySnapshot = await getDocs(payerQuery);

      if (!payerQuerySnapshot.empty) {
        payerQuerySnapshot.forEach((payerDoc) => {
          const payerDetails = payerDoc.data();
          const payerKey = payerDoc.id;
          if (!transactionsGroupedByPayer[payerKey]) {
            transactionsGroupedByPayer[payerKey] = {
              userDetails: {
                ...payerDetails,
                userId: payerKey, 
              },
              transactions: [],
            };
          }
          transactionsGroupedByPayer[payerKey].transactions.push(txn);
        });
      } else {
        console.log(`Payer with ID ${payerId} does not exist.`);
      }
    }
    if (Object.keys(transactionsGroupedByPayer).length === 0) {
      return res.status(404).send({
        message: "No payer details found for the transactions",
      });
    }
    const result = Object.values(transactionsGroupedByPayer)
      .sort((a, b) => {
        const latestTxnA = a.transactions[0].timestamp.seconds;
        const latestTxnB = b.transactions[0].timestamp.seconds;
        return latestTxnB - latestTxnA;
      })
      .slice(0, 12); 
    res.status(200).send({
      message: "Recent transactions retrieved successfully",
      data: result,
    });
  } catch (error) {
    console.error("Error retrieving recent transactions for merchant:", error);
    res.status(500).send({ message: "Internal server error" });
  }
};

// For OpenAPI
// Function to get all transactions for a particular GSTIN across all users
export const getAllTransactionsForGstin = async (req, res) => {
  const { gstin } = req.query;
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
        (transaction) => {
          return transaction.gstin === gstin;
        }
      );
      matchingTransactions = matchingTransactions.concat(
        gstinFilteredTransactions
      );
    });

    // if (matchingTransactions.length === 0) {
    //   return res.status(404).send({
    //     message: `No transactions found for the GSTIN: ${gstin}`,
    //   });
    // }
    res.status(200).send({
      message: "Transactions retrieved successfully",
      transactions: matchingTransactions,
    });
  } catch (error) {
    console.error("Error retrieving transactions for GSTIN:", error);
    res.status(500).send({ message: "Internal server error" });
  }
};

// Function to get all transactions for a particular GSTIN across all users for a specified month
export const getAllTransactionsForGstinInMonth = async (req, res) => {
  const { gstin, month, year } = req.query;
  const monthYear = `${year}-${month}`;
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

    // if (matchingTransactions.length === 0) {
    //   return res.status(404).send({
    //     message: `No transactions found for the GSTIN: ${gstin} in the month: ${monthYear}`,
    //   });
    // }
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
  const { gstin, year } = req.query;
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

    // if (matchingTransactions.length === 0) {
    //   return res.status(404).send({
    //     message: `No transactions found for the GSTIN: ${gstin} in the year: ${year}`,
    //   });
    // }
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
