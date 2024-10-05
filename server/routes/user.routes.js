import express from "express";
import {
  getUserByVPA,
  getUserByPhone,
  addUser,
  updateUser,
  getUserById,
} from "../controllers/user.controller.js";

const userRouter = express.Router();
userRouter.route("/").post(addUser).put(updateUser).get(getUserById);
userRouter.route("/get/vpa").get(getUserByVPA);
userRouter.route("/get/ph").get(getUserByPhone);

export default userRouter;
