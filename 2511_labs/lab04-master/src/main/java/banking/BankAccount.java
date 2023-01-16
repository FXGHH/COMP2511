package banking;

public class BankAccount {
    protected int balance;
    /**
     * @invariant balance >= 0
     * @param balance
     */
    public BankAccount(int balance) {
        this.balance = balance;
    }

    /**
     * @precondition amount >= 0
     * @param amount of money wanted to be deposited
     * postconditions balacne amount >= 0
     */
    public void deposit(int amount) {
        balance += amount;
    }

    /**
     * @precondition amount > 0 and balacne > 0
     * @param amount of money withdrawn from bank
     * postconditions balacne amount >= 0
     */
    public void withdraw(int amount) {
        if (balance > 0 && balance >= amount) {
            balance -= amount;
        }
    }
}