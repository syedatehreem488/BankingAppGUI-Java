import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Client implements Serializable {
    private String id;
    private Person personDetails;
    private List<Account> acList = new ArrayList<>(); //Composition  CLient has-a Account
    private static int count = 0; // for client id generation

    //Compile-time Polymorphism (Overloading)
    public Client(Person personDetails) {
        this.personDetails = personDetails;
        this.id = generateClientId();
    }

    //Compile-time Polymorphism (Overloading)
    // for loading from file
    public Client(String id, Person personDetails) { //Composition Client has-a Person
        this.id = id;
        this.personDetails = personDetails;
    }

    private static String generateClientId() {
        count++;
        return "CL" + count;
    }

    public static void setCount(int c) { count = c; }
    public static int getCount() { return count; }

    //getters
    public String getId() { return id; }
    public Person getPersonDetails() { return personDetails; }
    public List<Account> getAcList() { return acList; }

    //setters
    public void setId(String id) { this.id = id; }
    public void setPersonDetails(Person personDetails) { this.personDetails = personDetails; }
    public void setAcList(List<Account> acList) { this.acList = acList; }

    public float totalAmount() {
        float total = 0f;
        for (Account a : acList) total += a.getAmount();
        return total;
    }

    public void withdraw(float amount, String accNo) {
        Account a = findAccount(accNo);
        if (a == null) throw new IllegalArgumentException("Account not found: " + accNo);
        a.withdraw(amount);
    }

    public void deposit(float amount, String accNo) {
        Account a = findAccount(accNo);
        if (a == null) throw new IllegalArgumentException("Account not found: " + accNo);
        a.deposit(amount);
    }

    public void addAccount(Account a) {
        if (a == null) return;
        if (!acList.contains(a)) acList.add(a);
    }

    private Account findAccount(String accNo) {
        for (Account a : acList) {
            if (a.getNumber().equals(accNo)) return a;
        }
        return null;
    }

    //Runtime polymorphism (method overriding)
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Client{id='" + id + "', " + personDetails + "}\n");
        for (Account a : acList) {
            sb.append(" ").append(a.getIdAmountString()).append("\n");
        }
        sb.append(" Total client amount: ").append(totalAmount());
        return sb.toString(); } // Preferred version

    public List<Account> getAccounts() {
        return acList;
    }
}
