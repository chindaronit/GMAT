import { initializeApp } from "firebase/app";
import { firebaseConfig } from "../config.js";
import { initializeFirestore } from "firebase/firestore";

const firebase = initializeApp(firebaseConfig);
const db = initializeFirestore(firebase, {
  experimentalForceLongPolling: true, // this line
  useFetchStreams: false, // and this line
});

export default db;
