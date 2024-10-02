import express from "express";
import {
  getUser,
  addUser,
  updateUser,
} from "../controllers/user.controller.js";

const userRouter = express.Router();
userRouter.route("/").get(getUser).post(addUser).put(updateUser);

export default userRouter
