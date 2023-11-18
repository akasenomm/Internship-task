
# Java Betting Program
Playtech Winternship 2024 task

This Java project processes betting data for multiple players and matches. It simulates a betting game where players can perform three types of operations: deposit, withdraw, and bet. The matches have two sides, A and B, and each match has a result and a rate value for each side. The players gain or lose coins based on their bets and the match outcomes. The program reads input data from two text files, player_data.txt and match_data.txt, and writes output data to a text file, result.txt. The output data consists of three groups: the list of legitimate players with their final balance and win rate, the list of illegitimate players with their first illegal operation, and the summary statistics of the game.

## Transaction Class

This class represents a transaction (player data) in the betting system. A transaction is an action performed by a player, such as depositing, withdrawing, or betting money on a match. A transaction has the following attributes:

    player: The player making the transaction.
    operation: The operation being performed (DEPOSIT, BET, WITHDRAW).
    match: The match being bet on, if applicable.
    amount: The amount of money involved in the transaction.
    betSide: The side the player is betting on, if applicable.

## Player Class

This class represents a player in the casino. A player has the following attributes:

    playerId: The unique ID of the player.
    totalBets: The total number of bets the player has placed.
    totalWins: The total number of bets the player has won.
    balance: The current amount of money the player has.
    legitimate: A boolean flag indicating whether the player is legitimate or not.

## Casino Class

This class represents a casino that manages the betting game. A casino has the following attributes:

    legitPlayers: A list of legitimate players who have not performed any illegal transactions.
    illegitimatePlayerTransactions: A list of transactions that are illegal, such as betting or withdrawing more than the balance.
    reserveBalance: The amount of money that the casino has reserved for paying the winnings of the players (staked until player is declared either legitimate or illegitimate).
    casinoHostBalance: The amount of money that the casino has earned from the game.

## TransactionsProcessor Class

This class processes transactions in the betting system. A transaction is an action performed by a player, such as depositing, withdrawing, or betting money on a match. A TransactionsProcessor has the following attributes:

    transactions: A queue of transactions to be processed.
    casino: A casino that manages the betting game and keeps track of the players and their balances.

The class also provides methods to process all transactions, detect illegitimate players and skip checking their other transactions.
