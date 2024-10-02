import db from "../models/db.js";
import {
  collection,
  doc,
  addDoc,
  getDocs,
  updateDoc,
  query,
  where,
} from "firebase/firestore";

const USER_COLLECTION = "users";

// Function to get a user by phone number
export const getUser = async (req, res) => {
  console.log(req);
  const phNo = req.body.phNo;
  // const phNo = "7988224882"
  
  try {
    const usersCollection = collection(db, USER_COLLECTION);

    const userQuery = query(usersCollection, where("phNo", "==", phNo));
    const querySnapshot = await getDocs(userQuery);

    // Check if a user with the specified phone number exists
    if (querySnapshot.empty) {
      return res.status(404).send({ message: "User not found" });
    }

    // Assuming there is only one user with the specified phone number
    const user = querySnapshot.docs[0].data();

    // Send the user data in the response
    res.status(200).send({ id: querySnapshot.docs[0].id, ...user });
  } catch (error) {
    console.error(error);
    res.status(500).send({ message: "Internal server error" });
  }
};

// Function to add a new user
export const addUser = async (req, res) => {
  const data = req.body;
  try {
    const userRef = await addDoc(collection(db, USER_COLLECTION), data);
    res.status(200).send({ msg: "User added successfully" });
  } catch (error) {
    console.error(error);
    res.status(500).send({ message: "Internal server error" });
  }
};

// Function to update an existing user by ID
export const updateUser = async (req, res) => {
  const id = req.body.id;
  delete req.body.id;
  const data = req.body;
  try {
    const docRef = doc(db, USER_COLLECTION, id);
    await updateDoc(docRef, data);

    res.status(200).send({ msg: "User updated successfully" });
  } catch (error) {
    console.error(error);
    res.status(500).send({ message: "Internal server error" });
  }
};
