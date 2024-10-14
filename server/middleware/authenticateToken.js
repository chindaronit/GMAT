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
  try {
    const decodedToken = await adminAuth.verifyIdToken(idToken);
    const uid = decodedToken.uid;
    req.verificationID = uid;
    console.log(decodedToken);
    const currentTime = Math.floor(Date.now() / 1000);
    if (decodedToken.exp < currentTime) {
      return res.status(401).send({ message: "Token has expired" });
    }
    next();
  } catch (error) {
    console.error("Error verifying ID token:", error);
    return res.status(401).send({ message: "Unauthorized" });
  }
};

export default authenticateToken;
