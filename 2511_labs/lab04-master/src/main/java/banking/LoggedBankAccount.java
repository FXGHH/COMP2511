package banking;

public class LoggedBankAccount extends BankAccount {
    private String account;
    private String password;
    public LoggedBankAccount(int balance, String account, String password) {
        super(balance);
        this.account = account;
        this.password = password;
    }

    /**
     * @precondition account number and password are correct
     * @param amount of money wanted to be deposited
     * @pospostconditions 
     */
    public boolean login(String account, String password) {
        if (account == this.account && password == this.password) {
            return true;
        }
        return false;
    }

    /**
     * @precondition balance amount > 0
     * @param amount of money need to be deposited
     * postconditions balacne amount >= 0
     */
    public void deposit(String account, String password, int amount) {
        if (login(account, password)) {
            balance += amount;
        }
    }
    /**
     * @precondition balacne amount > 0
     * @param amount of money need to be withdrawn
     * postconditions balacne amount >= 0
     */
    public void withdraw(String account, String password, int amount) {
        if (login(account, password)) {
            super.withdraw(amount);
        }
    }
}