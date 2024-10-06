import express from "express";
import {
  updateUserTransactionRewards,
  getUserRewardsPointsForMonth,
  getUsersByRewardsForMonth,
} from "../controllers/leaderboard.controller.js";

const leaderboardRouter = express.Router();
leaderboardRouter
  .route("/")
  .post(updateUserTransactionRewards)
  .get(getUserRewardsPointsForMonth);
leaderboardRouter.route("/all").get(getUsersByRewardsForMonth);

export default leaderboardRouter;
