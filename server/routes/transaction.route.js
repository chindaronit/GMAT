import express from "express";
import {
  getTransactionByTxnId,
  getAllTransactionsForMonth,
  addTransaction,
  getAllTransactionsForGstinInYear,
  getAllTransactionsForGstinInMonth,
  getAllTransactionsForGstin,
  getRecentTransactionsForUser,
  getRecentTransactionsForMerchant,
  getTransactionsByPayeeForMonth,
} from "../controllers/transaction.controller.js";

import authenticateToken from "../middleware/authenticateToken.js";

const transactionRouter = express.Router();
transactionRouter
  .route("/")
  .get(authenticateToken, getTransactionByTxnId)
  .post(authenticateToken, addTransaction);
transactionRouter
  .route("/all/month")
  .get(authenticateToken, getAllTransactionsForMonth);
transactionRouter
  .route("/recenttransaction")
  .get(authenticateToken, getRecentTransactionsForUser);
transactionRouter
  .route("/recentmerchanttransaction")
  .get(authenticateToken, getRecentTransactionsForMerchant);
transactionRouter
  .route("/all/merchant")
  .get(authenticateToken, getTransactionsByPayeeForMonth);
transactionRouter
  .route("/all/gstin")
  .get(authenticateToken, getAllTransactionsForGstin);
transactionRouter
  .route("/all/gstin/year")
  .get(authenticateToken, getAllTransactionsForGstinInYear);
transactionRouter
  .route("/all/gstin/month")
  .get(authenticateToken, getAllTransactionsForGstinInMonth);
  
export default transactionRouter;
