import java.io.Serializable;

public class Account implements Serializable {
    private String number;
    private float amount;
    private Client acHolder;
    private static int count = 0; // for account number generation

    //polymorphism
    public Account(float amount, Client holder) {
        this.number = generateAccountNumber();
        this.amount = amount;
        this.acHolder = holder;
    }
    //polymorphism
    public Account(String number, float amount, Client holder) { // for loading from file
        this.number = number;
        this.amount = amount;
        this.acHolder = holder;
    }

    private static String generateAccountNumber() {
        count++;
        return "AC" + count;
    }

    public static void setCount(int c) { count = c; }
    public static int getCount() { return count; }

    public String getNumber() { return number; }
    public float getAmount() { return amount; }
    public Client getAcHolder() { return acHolder; }

    public void setNumber(String number) { this.number = number; }
    public void setAmount(float amount) { this.amount = amount; }
    public void setAcHolder(Client acHolder) { this.acHolder = acHolder; }

    public float withdraw(float amt) {
        if (amt < 0) throw new IllegalArgumentException("Withdraw amount cannot be negative.");
        if (amt > amount) throw new IllegalArgumentException("Insufficient balance.");
        amount -= amt;
        return amount;
    }

    public float deposit(float amt) {
        if (amt < 0) throw new IllegalArgumentException("Deposit amount cannot be negative.");
        amount += amt;
        return amount;
    }

    public String getIdAmountString() {
        return "Account{number='" + number + "', amount=" + amount + "}";
    }

    @Override
    public String toString() {
        return "Account{number='" + number + "', amount=" + amount +
                ", holder='" + acHolder.getPersonDetails().getName() + "'}";
    }
}
