import express from "express";
import userRouter from "./routes/user.routes.js";
import transactionRouter from "./routes/transaction.route.js";
import leaderboardRouter from "./routes/leaderboard.route.js";

const app = express();
const port = process.env.PORT || 80;


// Middleware
app.use(express.json());

// Routing to URL
app.use("/users", userRouter);
app.use("/transactions", transactionRouter);
app.use("/leaderboard",leaderboardRouter)

app.listen(port, () => {
  console.log(`Server is listening on port ${port}...`);
});
