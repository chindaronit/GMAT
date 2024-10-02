import db from "../models/db.js";
import {
  collection,
  doc,
  addDoc,
  getDocs,
  query,
  where,
} from "firebase/firestore";

const TRANSACTION_COLLECTION = "transactions";

// Function to get a transaction by transactionId
export const getTransaction = async (req, res) => {
  const txnId = req.body.txnId;

  try {
    const transactionCollection = collection(db, TRANSACTION_COLLECTION);
    const txnQuery = query(transactionCollection, where("txnId", "==", txnId));
    const querySnapshot = await getDocs(txnQuery);

    // Check if a transaction with the specified phone number exists
    if (querySnapshot.empty) {
      return res.status(404).send({ message: "Transaction does not exist" });
    }

    // Assuming there is only one transaction with the specified phone number
    const transaction = querySnapshot.docs[0].data();

    // Send the transaction data in the response
    res.status(200).send({ id: querySnapshot.docs[0].id, ...transaction });
  } catch (error) {
    console.error(error);
    res.status(500).send({ message: "Internal server error" });
  }
};

// Function to get all transaction of userId
export const getAllTransactions = async (req, res) => {
  const userId = req.body.userId;
  try {
    const transactionCollection = collection(db, TRANSACTION_COLLECTION);

    const transactionQuery = query(
      transactionCollection,
      where("userId", "==", userId)
    );
    const querySnapshot = await getDocs(transactionQuery);

    let transactions;
    querySnapshot.forEach((doc) => {
      trasactions = { id: doc.id, ...doc.data() };
    });

    // Send the user data in the response
    res.status(200).send(transactions);
  } catch (error) {
    console.error(error);
    res.status(500).send({ message: "Internal server error" });
  }
};

// Function to update an existing transaction by ID
export const addTransaction = async (req, res) => {
  const data = req.body;
  try {
    const transactionRef = await addDoc(collection(db, TRANSACTION_COLLECTION), data);
    res.status(200).send({ msg: "Transaction added successfully" });
  } catch (error) {
    console.error(error);
    res.status(500).send({ message: "Internal server error" });
  }
};
