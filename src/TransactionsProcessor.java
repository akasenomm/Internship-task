import java.util.*;

public class TransactionsProcessor {
    private final Queue<Transaction> transactions;
    private final Casino casino;

    public TransactionsProcessor(Queue<Transaction> transactions) {
        this.transactions = transactions;
        this.casino = new Casino();
    }

    public void validatePlayers() {
        while (!transactions.isEmpty()){
            Transaction transaction = transactions.poll();
            transaction.performPlayerOperation();

            if (!transaction.getPlayer().isLegitimate()) {
                casino.addIllegitimatePlayerTransaction(transaction);
                continue;
            }

            casino.updateReserveBalance(transaction);

            if (casino.isFinalPlayerTransaction(transaction, transactions))
                casino.addLegitPlayer(transaction.getPlayer());

        }
    }

    public Casino getCasino() {
        return casino;
    }
}