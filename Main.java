public class Main {
    public static void main(String[] args) {
        // Part A: test core functionality
        Bank bank = new Bank("Test Bank");

        Person p1 = new Person("Ali", "35202-1234567-1", "0300-1234567");
        Client c1 = bank.addClient(p1);
        bank.addAccount(c1.getId(), 1000f, c1);
        bank.addAccount(c1.getId(), 2500f, c1);

        Person p2 = new Person("Sara", "35202-7654321-0", "0301-7654321");
        Client c2 = bank.addClient(p2);
        bank.addAccount(c2.getId(), 500f, c2);

        System.out.println(bank);
        System.out.println("Total Bank Amount: " + bank.totalAmount());
        System.out.println(c1);

        // Withdraw and deposit
        Account first = bank.getAccounts().get(0);
        first.deposit(300f);
        first.withdraw(200f);
        System.out.println("After ops, acc: " + first);

        // Part B: save and load
        try {
            BankFileHandler.saveBankData(bank, "bankdata.txt");
            Bank loaded = BankFileHandler.loadBankData("bankdata.txt");
            System.out.println("Loaded: " + loaded);
            System.out.println("Loaded total: " + loaded.totalAmount());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Launch GUI
        BankingAppGUI4.main(null);
    }
}
