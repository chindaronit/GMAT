import express from "express";
import {
    getTransactionByTxnId,
    getAllTransactionsForMonth,
    addTransaction
} from "../controllers/transaction.controller.js";

const transactionRouter = express.Router();
transactionRouter.route("/").get(getTransactionByTxnId).post(addTransaction);
transactionRouter
  .route("/all")
  .get(getAllTransactionsForMonth)
  .get(getAllTransactionsForUser);

export default transactionRouter;
