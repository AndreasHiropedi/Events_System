package external;

import java.util.ArrayList;
import java.util.List;

public class MockPaymentSystem implements PaymentSystem
{
    private List<Transaction> transactions;

    public MockPaymentSystem()
    {
        transactions = new ArrayList<>();
    }

    @Override
    public boolean processPayment(String buyerAccountEmail, String sellerAccountEmail, double transactionAmount)
    {
        if (buyerAccountEmail != null && !buyerAccountEmail.equals("") &&
                sellerAccountEmail != null && !sellerAccountEmail.equals(""))
        {
            Transaction payment = new Transaction(buyerAccountEmail, sellerAccountEmail, transactionAmount);
            transactions.add(payment);
            return true;
        }
        return false;
    }

    @Override
    public boolean processRefund(String buyerAccountEmail, String sellerAccountEmail, double transactionAmount)
    {
        for (Transaction transaction : transactions) {
            String buyerEmail = transaction.getBuyerEmailAccount();
            String sellerEmail = transaction.getSellerEmailAccount();
            double amount = transaction.getAmount();
            boolean isRefunded = transaction.isRefunded();
            if (!isRefunded && buyerEmail.equals(buyerAccountEmail) &&
                    sellerEmail.equals(sellerAccountEmail) && amount == transactionAmount)
            {
                transaction.setRefunded(true);
                return true;
            }
        }
        return false;
    }

    public List<Transaction> findTransactionByBuyerEmail(String buyerEmailAccount)
    {
        List<Transaction> buyerTransactions = new ArrayList<>();
        for (Transaction transaction: transactions)
        {
            if (transaction.getBuyerEmailAccount().equals(buyerEmailAccount))
            {
                buyerTransactions.add(transaction);
            }
        }
        return buyerTransactions;
    }

    public static class Transaction
    {
        private String buyerEmailAccount;
        private String sellerEmailAccount;
        private double amount;
        private boolean refunded;

        public Transaction(String buyerEmailAccount, String sellerEmailAccount, double amount) {
            this.buyerEmailAccount = buyerEmailAccount;
            this.sellerEmailAccount = sellerEmailAccount;
            this.amount = amount;
            this.refunded = false;
        }

        public String getBuyerEmailAccount()
        {
            return buyerEmailAccount;
        }

        public String getSellerEmailAccount()
        {
            return sellerEmailAccount;
        }

        public double getAmount()
        {
            return amount;
        }

        public boolean isRefunded()
        {
            return refunded;
        }

        public void setRefunded(boolean refunded)
        {
            this.refunded = refunded;
        }
    }
}
