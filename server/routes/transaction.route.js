import express from "express";
import {
  getTransactionByTxnId,
  getAllTransactionsForMonth,
  addTransaction,
  getAllTransactionsForUser,
  getUserTransactionsByUserIdAndPayeeId,
} from "../controllers/transaction.controller.js";

const transactionRouter = express.Router();
transactionRouter.route("/").get(getTransactionByTxnId).post(addTransaction);
transactionRouter
  .route("/all/user")
    .get(getAllTransactionsForUser);
transactionRouter.route("/all/month").get(getAllTransactionsForMonth);
transactionRouter.route("/all/payee").get(getUserTransactionsByUserIdAndPayeeId);

export default transactionRouter;
