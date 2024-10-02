import express from "express";
import {
    getTransaction,
    getAllTransactions,
    addTransaction
} from "../controllers/transaction.controller.js";

const transactionRouter = express.Router();
transactionRouter.route("/").get(getTransaction).post(addTransaction);
transactionRouter.route("/all").get(getAllTransactions);

export default transactionRouter;
