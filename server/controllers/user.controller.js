import db from "../models/db.js";
import {
  collection,
  doc,
  addDoc,
  getDoc,
  getDocs,
  updateDoc,
  query,
  where,
} from "firebase/firestore";

const USER_COLLECTION = "users";

// Function to get a user by phone number
export const getUserByPhone = async (req, res) => {
  const phNo = req.query.phNo;
  if (!phNo) {
    return res.status(400).send({
      message: "Bad Request: Missing or invalid phNo in the request",
    });
  }

  try {
    const usersCollection = collection(db, USER_COLLECTION);

    const userQuery = query(usersCollection, where("phNo", "==", phNo));
    const querySnapshot = await getDocs(userQuery);
    if (querySnapshot.empty) {
      return res.status(404).send({ message: "User not found" });
    }
    const user = querySnapshot.docs[0].data();
    res.status(200).send({ userId: querySnapshot.docs[0].id, ...user });
  } catch (error) {
    console.error(error);
    res.status(500).send({ message: "Internal server error" });
  }
};

// Function to get a user by VPA
export const getUserByVPA = async (req, res) => {
  const vpa = req.query.vpa;

  if (!vpa) {
    return res.status(400).send({
      message: "Bad Request: Missing or invalid vpa in the request",
    });
  }

  try {
    const usersCollection = collection(db, USER_COLLECTION);

    const userQuery = query(usersCollection, where("vpa", "==", vpa));
    const querySnapshot = await getDocs(userQuery);
    if (querySnapshot.empty) {
      return res.status(404).send({ message: "User not found" });
    }
    const user = querySnapshot.docs[0].data();
    res.status(200).send({ userId: querySnapshot.docs[0].id, ...user });
  } catch (error) {
    console.error(error);
    res.status(500).send({ message: "Internal server error" });
  }
};

// Function to add a new user
export const addUser = async (req, res) => {
  const { name, vpa, profile, qr, isMerchant, phNo } = req.body;

  if (!phNo || isMerchant === undefined || !name) {
    return res.status(400).send({
      message: "Bad Request: Missing or invalid fields in the request",
    });
  }

  const data = {
    name: name,
    vpa: vpa,
    phNo: phNo,
    qr: qr,
    profile: profile,
    isMerchant: isMerchant,
  };

  try {
    const userRef = await addDoc(collection(db, USER_COLLECTION), data);
    const userId = userRef.id;
    res.status(200).send({ msg: "User added successfully", userId: userId });
  } catch (error) {
    console.error(error);
    res.status(500).send({ message: "Internal server error" });
  }
};

// Function to update an existing user by ID
export const updateUser = async (req, res) => {
  const userId = req.body.userId;
  delete req.body.userId;
  const { name, vpa, profile, qr, isMerchant, phNo } = req.body;

  if (!phNo || !name || !userId) {
    return res.status(400).send({
      message: "Bad Request: Missing or invalid fields in the request",
    });
  }

  const data = {
    name: name,
    vpa: vpa,
    phNo: phNo,
    qr: qr,
    profile: profile,
    isMerchant: isMerchant,
  };

  try {
    const docRef = doc(db, USER_COLLECTION, userId);
    await updateDoc(docRef, data);

    res.status(200).send({ msg: "User updated successfully" });
  } catch (error) {
    console.error(error);
    res.status(500).send({ message: "Internal server error" });
  }
};

// Function to get all user details by user ID
export const getUserById = async (req, res) => {
  const userId = req.query.userId;
  if (!userId) {
    return res.status(400).send({
      message: "Bad Request: Missing or invalid user ID in the request",
    });
  }
  try {
    const userDoc = doc(db, USER_COLLECTION, userId);
    const userSnapshot = await getDoc(userDoc);
    if (!userSnapshot.exists()) {
      return res.status(404).send({ message: "User not found" });
    }
    const userData = userSnapshot.data();
    res.status(200).send({ userId: userSnapshot.id, ...userData });
  } catch (error) {
    console.error(error);
    res.status(500).send({ message: "Internal server error" });
  }
};
