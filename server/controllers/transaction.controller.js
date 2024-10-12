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
  where,
  query,
} from "firebase/firestore";

const TRANSACTION_COLLECTION = "transactions";
const USER_COLLECTION = "users";

// Function to add a new transaction, storing it under a monthly nested structure
export const addTransaction = async (req, res) => {
  const { userId, payerId, payeeId, type, amount, gstin } = req.body;
  if (!payerId || !payeeId || !type || !amount || !gstin) {
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
    timestamp: Timestamp.now(),
  };

  try {
    const payeeVpa = data.payeeId;
    const usersCollection = collection(db, USER_COLLECTION);
    const userQuery = query(usersCollection, where("vpa", "==", payeeVpa));
    const payeeQuerySnapshot = await getDocs(userQuery);
    let payeeName = null;
    if (!payeeQuerySnapshot.empty) {
      payeeQuerySnapshot.forEach((payeeDoc) => {
        const payeeData = payeeDoc.data();
        payeeName = payeeData.name || "Unknown";
      });
    } else {
      payeeName = "Unknown";
    }
    data = { ...data, name: payeeName };

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
    res
      .status(200)
      .send({ msg: "Transaction added successfully", txnId: txnId });
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
  const { userId, txnId } = req.query;
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
  const { userId } = req.query;
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
  const { userId, payeeId } = req.query;
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
  const { payerId, payeeId } = req.query;

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
    const type = "0";
    transactionDocsSnapshot.forEach((docSnapshot) => {
      const monthlyTransactions = docSnapshot.data().monthlyTransactions || {};
      for (const month in monthlyTransactions) {
        const transactions = monthlyTransactions[month] || [];
        const filteredTransactions = transactions.filter((transaction) => {
          const trimmedPayerId = transaction.payerId.trim();
          const trimmedPayeeId = transaction.payeeId.trim();
          const trimmedType = transaction.type.trim();
          const trimmedInputPayerId = payerId.trim();
          const trimmedInputPayeeId = payeeId.trim();
          const trimmedInputType = type.trim();
          return (
            trimmedPayerId === trimmedInputPayerId &&
            trimmedPayeeId === trimmedInputPayeeId &&
            trimmedType === trimmedInputType
          );
        });
        matchingTransactions =
          matchingTransactions.concat(filteredTransactions);
      }
    });

    // Sort the transactions by timestamp in descending order
    matchingTransactions.sort(
      (a, b) => b.timestamp.toDate() - a.timestamp.toDate()
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
  const { gstin, month, year } = req.query;
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

// export const getRecentTransactionsForUser = async (req, res) => {
//   const { userId } = req.query;

//   if (!userId) {
//     return res.status(400).send({
//       message: "Bad Request: Missing or invalid userId in the request",
//     });
//   }

//   try {
//     const transactionDocRef = doc(db, TRANSACTION_COLLECTION, userId);
//     const transactionDoc = await getDoc(transactionDocRef);
//     if (!transactionDoc.exists()) {
//       return res
//         .status(404)
//         .send({ message: "No transactions found for the user" });
//     }

//     const monthlyTransactions = transactionDoc.data().monthlyTransactions || {};
//     const allTransactions = Object.values(monthlyTransactions).flat();
//     const payerTransactions = allTransactions.filter((txn) => txn.type === "0");
//     const usersCollection = collection(db, USER_COLLECTION);
//     const transactionsByPayee = {};

//     for (const txn of payerTransactions) {
//       const vpa = txn.payeeId.trim();
//       const userQuery = query(usersCollection, where("vpa", "==", vpa));
//       const payeeQuerySnapshot = await getDocs(userQuery);

//       if (!payeeQuerySnapshot.empty) {
//         payeeQuerySnapshot.forEach((payeeDoc) => {
//           const payeeDetails = payeeDoc.data();
//           const payeeId = payeeDoc.id;

//           if (!transactionsByPayee[payeeId]) {
//             transactionsByPayee[payeeId] = {
//               payeeDetails: payeeDetails,
//               transactions: [],
//             };
//           }
//           transactionsByPayee[payeeId].transactions.push(txn);
//         });
//       } else {
//         console.log(`Payee with ID ${vpa} does not exist.`);
//       }
//     }

//     // Prepare the result array
//     const result = Object.keys(transactionsByPayee).map((payeeId) => {
//       const { payeeDetails, transactions } = transactionsByPayee[payeeId];

//       // Sort transactions based on the timestamp in descending order
//       const sortedTransactions = transactions.sort((a, b) => {
//         return b.timestamp.seconds - a.timestamp.seconds; // Adjust as necessary for sorting
//       });

//       return {
//         payeeDetails: payeeDetails,
//         transactions: sortedTransactions,
//       };
//     });

//     // Sort payees based on the latest transaction's timestamp in descending order
//     const sortedResult = result.sort((a, b) => {
//       const lastTxnA = a.transactions[0]?.timestamp.seconds || 0; // Get latest transaction timestamp for payee A
//       const lastTxnB = b.transactions[0]?.timestamp.seconds || 0; // Get latest transaction timestamp for payee B
//       return lastTxnB - lastTxnA; // Sort by descending order
//     });

//     res.status(200).send({
//       message: "Recent transactions retrieved successfully",
//       transactionsByPayees: sortedResult,
//     });
//   } catch (error) {
//     console.error("Error retrieving recent transactions for user:", error);
//     res.status(500).send({ message: "Internal server error" });
//   }
// };

// export const getRecentTransactionsForUser = async (req, res) => {
//   const { userId } = req.query;

//   if (!userId) {
//     return res.status(400).send({
//       message: "Bad Request: Missing or invalid userId in the request",
//     });
//   }

//   try {
//     const transactionDocRef = doc(db, TRANSACTION_COLLECTION, userId);
//     const transactionDoc = await getDoc(transactionDocRef);
//     if (!transactionDoc.exists()) {
//       return res
//         .status(404)
//         .send({ message: "No transactions found for the user" });
//     }

//     const monthlyTransactions = transactionDoc.data().monthlyTransactions || {};
//     const allTransactions = Object.values(monthlyTransactions)
//       .flat()
//       .sort((a, b) => {
//         return b.timestamp.seconds - a.timestamp.seconds;
//       });
//     const payerTransactions = allTransactions.filter((txn) => txn.type === "0");
//     const usersCollection = collection(db, USER_COLLECTION);
//     const transactionsByPayee = {};
//     for (const txn of payerTransactions) {
//       const vpa = txn.payeeId.trim();
//       const userQuery = query(usersCollection, where("vpa", "==", vpa));
//       const payeeQuerySnapshot = await getDocs(userQuery);

//       if (!payeeQuerySnapshot.empty) {
//         payeeQuerySnapshot.forEach((payeeDoc) => {
//           const payeeDetails = payeeDoc.data();
//           const payeeId = payeeDoc.id;
//           if (!transactionsByPayee[payeeId]) {
//             transactionsByPayee[payeeId] = {
//               payeeDetails: payeeDetails,
//               transactions: [],
//             };
//           }
//           transactionsByPayee[payeeId].transactions.push(txn);
//         });
//       } else {
//         console.log(`Payee with ID ${vpa} does not exist.`);
//       }
//     }
//     const result = Object.keys(transactionsByPayee).map((payeeId) => ({
//       payeeDetails: transactionsByPayee[payeeId].payeeDetails,
//       transactions: transactionsByPayee[payeeId].transactions,
//     }));

//     res.status(200).send({
//       message: "Recent transactions retrieved successfully",
//       data: result,
//     });
//   } catch (error) {
//     console.error("Error retrieving recent transactions for user:", error);
//     res.status(500).send({ message: "Internal server error" });
//   }
// };

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

    // Retrieve all monthly transactions and sort them by timestamp in descending order
    const monthlyTransactions = transactionDoc.data().monthlyTransactions || {};
    const allTransactions = Object.values(monthlyTransactions)
      .flat()
      .sort((a, b) => b.timestamp.seconds - a.timestamp.seconds);

    // Filter only payer transactions (where txn.type === "0")
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

          // If payee does not exist in the object, initialize their details
          if (!transactionsByPayee[payeeId]) {
            transactionsByPayee[payeeId] = {
              payeeDetails: payeeDetails,
              transactions: [],
            };
          }

          // Add transaction to payee's transaction list
          transactionsByPayee[payeeId].transactions.push(txn);
        });
      } else {
        console.log(`Payee with ID ${vpa} does not exist.`);
      }
    }

    // Sort payees by the most recent transaction's timestamp
    const sortedPayees = Object.values(transactionsByPayee)
      .sort((a, b) => {
        const latestTxnA = a.transactions[0].timestamp.seconds;
        const latestTxnB = b.transactions[0].timestamp.seconds;
        return latestTxnB - latestTxnA;
      })
      .slice(0, 12); // Limit to top 12 payees

    // Prepare the final result
    const result = sortedPayees.map((payee) => ({
      payeeDetails: payee.payeeDetails,
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
    if (transactionQuerySnapshot.empty) {
      return res.status(404).send({ message: "No transactions found" });
    }
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

    if (!merchantTransactions.length) {
      return res.status(404).send({
        message: "No transactions found for the given VPA",
      });
    }
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
              payerDetails: payerDetails,
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
      .slice(0, 12); // Limit to top 12 payers

    res.status(200).send({
      message: "Recent transactions retrieved successfully",
      data: result,
    });
  } catch (error) {
    console.error("Error retrieving recent transactions for merchant:", error);
    res.status(500).send({ message: "Internal server error" });
  }
};
