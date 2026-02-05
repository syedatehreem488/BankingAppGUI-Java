import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

public class BankingAppGUI4 extends JFrame {
    private Bank bank;
    private JTextArea displayArea;
    private boolean isAdmin = false;
    private Client loggedClient = null;

    public BankingAppGUI4() {
        try {
            bank = BankFileHandler.loadBankData("bankdata.txt");
        } catch (IOException e) {
            bank = new Bank("National Bank");
        }
        showHomePage();
    }

    private void showHomePage() {
        setTitle("Welcome | National Bank System");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel homePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(40, 40, 80),
                        getWidth(), getHeight(), new Color(10, 10, 25));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        homePanel.setLayout(new BorderLayout());
        homePanel.setBorder(new EmptyBorder(60, 60, 60, 60));

        // --- Title at the top ---
        JLabel title = new JLabel("National Bank System", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI Semibold", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        homePanel.add(title, BorderLayout.NORTH);

        // --- Centered Button Section ---
        JButton adminBtn = new JButton("Admin Login");
        JButton clientBtn = new JButton("Client Login");
        styleMainButton(adminBtn, new Color(52, 152, 219));
        styleMainButton(clientBtn, new Color(46, 204, 113));
        adminBtn.setPreferredSize(new Dimension(180, 55));
        clientBtn.setPreferredSize(new Dimension(180, 55));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.add(adminBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(40, 0)));
        buttonPanel.add(clientBtn);

        // Wrap in a centering container
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(buttonPanel);

        homePanel.add(centerWrapper, BorderLayout.CENTER);

        // --- Footer at the bottom ---
        JLabel footer = new JLabel("2030 National Bank. All rights reserved.", SwingConstants.CENTER);
        footer.setForeground(new Color(180, 180, 180));
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        homePanel.add(footer, BorderLayout.SOUTH);

        adminBtn.addActionListener(e -> {
            getContentPane().removeAll();
            revalidate();
            repaint();
            adminLogin();
        });

        clientBtn.addActionListener(e -> {
            getContentPane().removeAll();
            revalidate();
            repaint();
            clientLogin();
        });

        buttonPanel.add(adminBtn);
        buttonPanel.add(clientBtn);

        add(homePanel);
        setVisible(true);
    }

    private void styleMainButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg);
            }
        });
    }

    private void styleMainTabs(JTabbedPane tabs) {
        tabs.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            private final Color[] tabColors = {
                    new Color(45, 127, 249),
                    new Color(46, 204, 113),
                    new Color(155, 89, 182)
            };
            @Override
            protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                Color c = tabColors[Math.min(tabIndex, tabColors.length - 1)];
                g.setColor(isSelected ? c.darker() : c);
                g.fillRect(x, y, w, h);
            }
            @Override
            protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics,
                                     int tabIndex, String title, Rectangle textRect, boolean isSelected) {
                g.setFont(font);
                g.setColor(Color.WHITE);
                g.drawString(title, textRect.x, textRect.y + metrics.getAscent());
            }
        });
        tabs.setBorder(null);
    }

    private void adminLogin() {
        String user = JOptionPane.showInputDialog(this, "Enter Admin Username:");
        String pass = JOptionPane.showInputDialog(this, "Enter Admin Password:");
        if (bank.validateAdmin(user, pass)) {
            isAdmin = true;
            initUI();
            attachActions();
            setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Admin credentials!");
            showHomePage();
        }
    }

    private void clientLogin() {
        String cnic = JOptionPane.showInputDialog(this, "Enter CNIC:");
        Client c = bank.searchCustomerDetail(cnic.trim());
        if (c != null) {
            loggedClient = c;
            initUI();
            attachActions();
            setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Client not found!");
            showHomePage();
        }
    }

    private void initUI() {
        setTitle("Banking Application");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Color bgDark = new Color(25, 25, 30);
        Color textFg = new Color(220, 220, 220);
        getContentPane().setBackground(bgDark);
        setLayout(new BorderLayout(10, 10));

        displayArea = new JTextArea();
        displayArea.setBackground(new Color(20, 20, 25));
        displayArea.setForeground(textFg);
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(displayArea);
        scroll.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(scroll, BorderLayout.CENTER);

        JTabbedPane tabs = new JTabbedPane();
        styleMainTabs(tabs);

        if (isAdmin) {
            JPanel clientsTab = new JPanel(new GridLayout(1, 3, 10, 10));
            JButton addClientBtn = new JButton("Add Client + Account");
            JButton searchBtn = new JButton("Search by CNIC");
            JButton removeBtn = new JButton("Remove Client");
            styleMainButton(addClientBtn, new Color(52, 152, 219));
            styleMainButton(searchBtn, new Color(192, 57, 43));
            styleMainButton(removeBtn, new Color(39, 174, 96));
            clientsTab.add(addClientBtn);
            clientsTab.add(searchBtn);
            clientsTab.add(removeBtn);

            JPanel accountsTab = new JPanel(new GridLayout(1, 2, 10, 10));
            JButton depositBtn = new JButton("Deposit");
            JButton withdrawBtn = new JButton("Withdraw");
            styleMainButton(depositBtn, new Color(230, 126, 34));
            styleMainButton(withdrawBtn, new Color(241, 196, 15));
            accountsTab.add(depositBtn);
            accountsTab.add(withdrawBtn);

            JPanel bankTab = new JPanel(new GridLayout(1, 4, 10, 10));
            JButton showBtn = new JButton("Show Bank Data");
            JButton saveBtn = new JButton("Save to File");
            JButton loadBtn = new JButton("Load from File");
            JButton exitBtn = new JButton("Exit");
            styleMainButton(showBtn, new Color(155, 89, 182));
            styleMainButton(saveBtn, new Color(233, 30, 99));
            styleMainButton(loadBtn, new Color(52, 152, 219));
            styleMainButton(exitBtn, new Color(192, 57, 43));
            bankTab.add(showBtn);
            bankTab.add(saveBtn);
            bankTab.add(loadBtn);
            bankTab.add(exitBtn);

            tabs.addTab("Clients", clientsTab);
            tabs.addTab("Accounts", accountsTab);
            tabs.addTab("Bank", bankTab);

            addClientBtn.putClientProperty("role", "addClient");
            searchBtn.putClientProperty("role", "searchClient");
            removeBtn.putClientProperty("role", "removeClient");
            depositBtn.putClientProperty("role", "deposit");
            withdrawBtn.putClientProperty("role", "withdraw");
            showBtn.putClientProperty("role", "show");
            saveBtn.putClientProperty("role", "save");
            loadBtn.putClientProperty("role", "load");
            exitBtn.putClientProperty("role", "exit");
        } else {
            JPanel clientTab = new JPanel(new GridLayout(1, 5, 10, 10));
            JButton addAccountBtn = new JButton("Add Account");
            JButton depositBtn = new JButton("Deposit");
            JButton withdrawBtn = new JButton("Withdraw");
            JButton showBtn = new JButton("Show My Account");
            JButton exitBtn = new JButton("Exit");
            styleMainButton(addAccountBtn, new Color(39, 174, 96));
            styleMainButton(depositBtn, new Color(230, 126, 34));
            styleMainButton(withdrawBtn, new Color(241, 196, 15));
            styleMainButton(showBtn, new Color(52, 152, 219));
            styleMainButton(exitBtn, new Color(192, 57, 43));
            clientTab.add(addAccountBtn);
            clientTab.add(depositBtn);
            clientTab.add(withdrawBtn);
            clientTab.add(showBtn);
            clientTab.add(exitBtn);
            tabs.addTab("My Account", clientTab);

            addAccountBtn.putClientProperty("role", "addAccount");
            depositBtn.putClientProperty("role", "deposit");
            withdrawBtn.putClientProperty("role", "withdraw");
            showBtn.putClientProperty("role", "showClient");
            exitBtn.putClientProperty("role", "exit");
        }

        add(tabs, BorderLayout.SOUTH);
    }

    private void attachActions() {
        JTabbedPane tabs = (JTabbedPane) getContentPane().getComponent(1);
        for (int i = 0; i < tabs.getTabCount(); i++) {
            JPanel panel = (JPanel) tabs.getComponentAt(i);
            for (Component comp : panel.getComponents()) {
                if (comp instanceof JButton) attachButton((JButton) comp);
            }
        }
    }

    private void attachButton(JButton btn) {
        String role = (String) btn.getClientProperty("role");
        btn.addActionListener(e -> {
            switch (role) {
                case "addClient": addClient(); break;
                case "searchClient": searchClient(); break;
                case "removeClient": removeClient(); break;
                case "deposit": deposit(); break;
                case "withdraw": withdraw(); break;
                case "show": showData(); break;
                case "save": saveData(); break;
                case "load": loadData(); break;
                case "showClient": showClientData(); break;
                case "addAccount": addAccountForClient(); break;
                case "exit":
                    this.dispose();
                    new BankingAppGUI4();
                    break;
            }
        });
    }

    private String prompt(String msg) { return JOptionPane.showInputDialog(this, msg); }
    private boolean isCancelled(String s) { return s == null || s.trim().isEmpty(); }
    private Float parseFloatSafe(String s) {
        try { return Float.parseFloat(s.trim()); } catch (Exception e) { return null; }
    }
    private void append(String msg) { displayArea.append(msg + "\n"); }

    private void addClient() {
        String name = prompt("Enter Name:");
        if (isCancelled(name)) { append("Client creation cancelled."); return; }
        String cnic = prompt("Enter CNIC:");
        if (isCancelled(cnic)) { append("Client creation cancelled."); return; }
        String phone = prompt("Enter Phone:");
        if (isCancelled(phone)) { append("Client creation cancelled."); return; }

        Person p = new Person(name.trim(), cnic.trim(), phone.trim());
        Client c = bank.addClient(p);

        String amtStr = prompt("Enter Initial Deposit:");
        if (isCancelled(amtStr)) { append("Account creation cancelled."); return; }
        Float amt = parseFloatSafe(amtStr);
        if (amt == null || amt < 0) { append("Invalid amount. Operation cancelled."); return; }

        Account a = bank.addAccount(c.getId(), amt, c);
        append("Client and Account created: " + a);
    }

    private void searchClient() {
        String cnic = prompt("Enter CNIC:");
        if (isCancelled(cnic)) { append("Search cancelled."); return; }
        Client c = bank.searchCustomerDetail(cnic.trim());
        if (c != null) append("Client found:\n" + c);
        else append("No client found with CNIC " + cnic);
    }

    private void removeClient() {
        String id = prompt("Enter Client ID (e.g., CL1):");
        if (isCancelled(id)) { append("Removal cancelled."); return; }
        boolean removed = bank.removeClient(id.trim());
        append(removed ? "Client removed and accounts destroyed." : "Client ID not found.");
    }

    private void deposit() {
        String accNo = prompt("Enter Account Number:");
        if (isCancelled(accNo)) { append("Deposit cancelled."); return; }
        String amtStr = prompt("Enter Deposit Amount:");
        if (isCancelled(amtStr)) { append("Deposit cancelled."); return; }
        Float amt = parseFloatSafe(amtStr);
        if (amt == null || amt < 0) { append("Invalid amount."); return; }

        Account acc = bank.searchAccount(accNo.trim());
        if (acc != null) {
            acc.deposit(amt);
            append("Deposit successful! New balance: " + acc.getAmount());
        } else append("Account not found!");
    }

    private void withdraw() {
        String accNo = prompt("Enter Account Number:");
        if (isCancelled(accNo)) { append("Withdrawal cancelled."); return; }
        String amtStr = prompt("Enter Withdraw Amount:");
        if (isCancelled(amtStr)) { append("Withdrawal cancelled."); return; }
        Float amt = parseFloatSafe(amtStr);
        if (amt == null || amt < 0) { append("Invalid amount."); return; }

        Account acc = bank.searchAccount(accNo.trim());
        if (acc != null) {
            try {
                acc.withdraw(amt);
                append("Withdrawal successful! Remaining balance: " + acc.getAmount());
            } catch (IllegalArgumentException ex) {
                append("Withdrawal failed: " + ex.getMessage());
            }
        } else append("Account not found!");
    }

    private void showData() {
        append(bank.toString());
        for (Client c : bank.getClients()) {
            append(c.toString());
            for (Account a : c.getAccounts()) append("  " + a.toString());
        }
        append("Total Bank Amount: " + bank.totalAmount());
    }

    private void addAccountForClient() {
        if (loggedClient == null) { append("No client logged in."); return; }
        String amtStr = prompt("Enter Initial Deposit for new account:");
        if (isCancelled(amtStr)) { append("Account creation cancelled."); return; }
        Float amt = parseFloatSafe(amtStr);
        if (amt == null || amt < 0) { append("Invalid amount. Operation cancelled."); return; }

        Account a = bank.addAccount(loggedClient.getId(), amt, loggedClient);
        append("New account created for client: " + a);
        try {
            BankFileHandler.saveBankData(bank, "bankdata.txt");
            append("Bank data updated in bankdata.txt");
        } catch (IOException ex) {
            append("Error saving data: " + ex.getMessage());
        }
    }

    private void saveData() {
        try {
            BankFileHandler.saveBankData(bank, "bankdata.txt");
            append("Data saved to bankdata.txt");
        } catch (IOException ex) {
            append("Error saving data: " + ex.getMessage());
        }
    }

    private void loadData() {
        try {
            bank = BankFileHandler.loadBankData("bankdata.txt");
            append("Data loaded from bankdata.txt");
        } catch (IOException ex) {
            append("Error loading data: " + ex.getMessage());
        }
    }

    private void showClientData() {
        if (loggedClient != null) {
            append("Your Data:\n" + loggedClient);
            for (Account a : loggedClient.getAccounts()) append(a.toString());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BankingAppGUI4::new);
    }
}
