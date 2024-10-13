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

const transactionRouter = express.Router();
transactionRouter.route("/").get(getTransactionByTxnId).post(addTransaction);
transactionRouter.route("/all/month").get(getAllTransactionsForMonth);
transactionRouter.route("/recenttransaction").get(getRecentTransactionsForUser);
transactionRouter.route("/recentmerchanttransaction").get(getRecentTransactionsForMerchant);
transactionRouter.route("/all/payee").get(getTransactionsByPayeeForMonth);
transactionRouter.route("/all/merchant").get(getTransactionsByPayeeForMonth);
// for openAPI
transactionRouter.route("/all/gstin").get(getAllTransactionsForGstin)
transactionRouter.route("/all/gstin/year").get(getAllTransactionsForGstinInYear);
transactionRouter.route("/all/gstin/month").get(getAllTransactionsForGstinInMonth);
  
export default transactionRouter;
