import express from "express";
import {
  updateUserTransactionRewards,
  getUserRewardsPointsForMonth,
  getUsersByRewardsForMonth,
} from "../controllers/leaderboard.controller.js";

import authenticateToken from "../middleware/authenticateToken.js";

const leaderboardRouter = express.Router();
leaderboardRouter
  .route("/")
  .post(authenticateToken, updateUserTransactionRewards)
  .get(authenticateToken, getUserRewardsPointsForMonth);
leaderboardRouter.route("/all").get(authenticateToken, getUsersByRewardsForMonth);

export default leaderboardRouter;
