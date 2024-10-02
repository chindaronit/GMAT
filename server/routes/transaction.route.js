import express from "express";
import {
  getTransactionByTxnId,
  getAllTransactionsForMonth,
  addTransaction,
  getAllTransactionsForUser,
} from "../controllers/transaction.controller.js";

const transactionRouter = express.Router();
transactionRouter.route("/").get(getTransactionByTxnId).post(addTransaction);
transactionRouter
  .route("/all/user")
    .get(getAllTransactionsForUser);
  transactionRouter.route("/all/month").get(getAllTransactionsForMonth);

export default transactionRouter;
