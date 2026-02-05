import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Bank implements Serializable {
    private String name;
    private List<Client> clList = new ArrayList<>(); //Composition  Bank has-a Client
    private List<Account> acList = new ArrayList<>(); //Composition  Bank has-a Account

    private final String ADMIN_USER = "admin";
    private final String ADMIN_PASS = "1234";

    public Bank(String name) {
        this.name = name;
    }
    public Bank() {}

    public String getName() { return name; }
    public List<Client> getClients() { return clList; }
    public List<Account> getAccounts() { return acList; }

    public void setName(String name) { this.name = name; }
    public void setClients(List<Client> clList) { this.clList = clList; }
    public void setAccounts(List<Account> acList) { this.acList = acList; }

    // Admin Login
    public boolean validateAdmin(String username, String password) {
        return ADMIN_USER.equals(username) && ADMIN_PASS.equals(password);
    }

    public Client addClient(Person p) {
        // Ensure CNIC uniqueness within this bank
        for (Client c : clList) {
            if (c.getPersonDetails().getCnic().equals(p.getCnic())) {
                return c;
            }
        }
        Client c = new Client(p);
        clList.add(c);
        return c;
    }

    public Account addAccount(String clientId, float amount, Client cRef) {
        Client c = cRef != null ? cRef : searchClientById(clientId);
        if (c == null) throw new IllegalArgumentException("Client not found: " + clientId);
        Account a = new Account(amount, c);
        acList.add(a);
        c.addAccount(a);
        return a;
    }

    public Account searchAccount(String accNumber) {
        for (Account a : acList) {
            if (a.getNumber().equals(accNumber)) return a;
        }
        return null;
    }

    public boolean removeClient(String id) {
        Client target = searchClientById(id);
        if (target == null) return false;

        // Remove all accounts of this client from bank
        Iterator<Account> it = acList.iterator();
        while (it.hasNext()) {
            Account a = it.next();
            if (a.getAcHolder() == target) it.remove();
        }
        // Remove client from bank
        clList.remove(target);
        return true;
    }

    public float totalAmount() {
        float total = 0f;
        for (Account a : acList) total += a.getAmount();
        return total;
    }

    public Client searchCustomerDetail(String cnic) {
        for (Client c : clList) {
            if (c.getPersonDetails().getCnic().equals(cnic)) return c;
        }
        return null;
    }

    private Client searchClientById(String id) {
        for (Client c : clList) {
            if (c.getId().equals(id)) return c;
        }
        return null;
    }

    @Override
    public String toString() {
        return "Bank{name='" + name + "', clients=" + clList.size() + ", accounts=" + acList.size() + "}";
    }
}
