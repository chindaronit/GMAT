// import { initializeApp } from "firebase/app";
// import { getAuth } from "firebase/auth";
// import { firebaseConfig } from "../config.js";

// const firebaseApp = initializeApp(firebaseConfig);
// const auth = getAuth(firebaseApp);

// const authenticateToken = async (req, res, next) => {
//     const idToken = req.headers["authorization"].split(" ")[1];
//     console.log(idToken);
    
//   if (!idToken) {
//     return res.status(400).send({ message: "ID token is required" });
//   }
//   try {
//     const decodedToken = await auth.verifyIdToken(idToken); // Use auth.verifyIdToken
//     const uid = decodedToken.uid;
//     console.log(decodedToken);
//     req.verificationID = uid;
//     next();
//   } catch (error) {
//     console.error("Error verifying ID token:", error);
//     return res.status(401).send({ message: "Unauthorized" }); // Respond with unauthorized if verification fails
//   }
// };

// export default authenticateToken;

import { initializeApp } from "firebase-admin/app";
import { getAuth as getAdminAuth } from "firebase-admin/auth";
import { firebaseConfig } from "../config.js";


initializeApp(firebaseConfig);
const adminAuth = getAdminAuth();

const authenticateToken = async (req, res, next) => {
  const idToken = req.headers["authorization"]?.split(" ")[1];
  if (!idToken) {
    return res.status(400).send({ message: "ID token is required" });
  }
    console.log(idToken);
  try {
    const decodedToken = await adminAuth.verifyIdToken(idToken);
      const uid = decodedToken.uid;
      console.log(uid);
    req.verificationID = uid;
    next();
  } catch (error) {
    console.error("Error verifying ID token:", error);
    return res.status(401).send({ message: "Unauthorized" });
  }
};

export default authenticateToken;
