import express from "express";
import {
  getUserByVPA,
  getUserByPhone,
  addUser,
  updateUser,
  getUserById,
} from "../controllers/user.controller.js";

import authenticateToken from "../middleware/authenticateToken.js";

const userRouter = express.Router();
userRouter
  .route("/")
  .post(authenticateToken,addUser)
  .get(authenticateToken, getUserById);
userRouter.route("/update").post(authenticateToken,updateUser);
userRouter
  .route("/get/vpa")
  .get(authenticateToken,authenticateToken, getUserByVPA);
userRouter.route("/get/ph").get(authenticateToken,getUserByPhone);

export default userRouter;
