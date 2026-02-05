import java.io.*;
import java.util.*;

public class BankFileHandler {

    public static void saveBankData(Bank bank, String filePath) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            pw.println(bank.getName());
            pw.println(bank.getClients().size() + "," + bank.getAccounts().size());
            pw.println(Client.getCount() + "," + Account.getCount());

            for (Client c : bank.getClients()) {
                Person p = c.getPersonDetails();
                pw.println(String.join(",",
                        "CL", c.getId(), sanitize(p.getName()), sanitize(p.getCnic()), sanitize(p.getPhoneNo())));
            }

            for (Account a : bank.getAccounts()) {
                pw.println(String.join(",",
                        "AC", a.getNumber(), String.valueOf(a.getAmount()), a.getAcHolder().getId()));
            }
        }
    }

    public static Bank loadBankData(String filePath) throws IOException {
        File f = new File(filePath);
        if (!f.exists() || f.length() == 0) {
            return new Bank("National Bank"); // fallback if file missing/empty
        }

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String bankName = br.readLine();
            Bank bank = new Bank(bankName);

            String[] counts = br.readLine().split(",");
            int clientEntries = Integer.parseInt(counts[0]);
            int accountEntries = Integer.parseInt(counts[1]);

            String[] counters = br.readLine().split(",");
            Client.setCount(Integer.parseInt(counters[0]));
            Account.setCount(Integer.parseInt(counters[1]));

            Map<String, Client> clientIndex = new HashMap<>();

            // Load clients
            for (int i = 0; i < clientEntries; i++) {
                String line = br.readLine();
                if (line == null || line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                // CL,Id,Name,CNIC,Phone
                String id = parts[1];
                Person p = new Person(unsanitize(parts[2]), unsanitize(parts[3]), unsanitize(parts[4]));
                Client c = new Client(id, p);
                bank.getClients().add(c);
                clientIndex.put(id, c);
            }

            // Load accounts
            for (int i = 0; i < accountEntries; i++) {
                String line = br.readLine();
                if (line == null || line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                // AC,Number,Amount,ClientId
                String number = parts[1];
                float amount = Float.parseFloat(parts[2]);
                String clientId = parts[3];
                Client holder = clientIndex.get(clientId);
                if (holder == null) throw new IOException("Orphan account detected for clientId: " + clientId);
                Account a = new Account(number, amount, holder);
                bank.getAccounts().add(a);
                holder.addAccount(a);
            }

            return bank;
        }
    }

    private static String sanitize(String s) {
        return s == null ? "" : s.replace(",", ";"); // avoid commas in CSV
    }

    private static String unsanitize(String s) {
        return s == null ? "" : s.replace(";", ",");
    }
}
