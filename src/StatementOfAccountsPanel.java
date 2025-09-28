import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Statement of Accounts Panel - Java translation of the HTML/CSS structure
 * Matches the visual design from the provided images with database integration
 */
public class StatementOfAccountsPanel extends JPanel {
    private AccountStatement accountStatement;
    private String studentID;
    private String studentName;
    private String studentProgram;
    
    // UI Components for dynamic updates
    private JLabel amountDueLabel;
    private JLabel remainingBalanceLabel;
    private JLabel prelimStatusLabel;
    private JTable feeBreakdownTable;
    private DefaultTableModel feeTableModel;
    
    // Payment dialog references
    private JDialog paymentDialog;
    private JTextField amountField;
    private JComboBox<String> paymentMethodCombo;
    
    public StatementOfAccountsPanel(String studentID, String studentName, String studentProgram) {
        this.studentID = studentID;
        this.studentName = studentName;
        this.studentProgram = studentProgram;
        this.accountStatement = AccountStatementManager.getStatement(studentID);
        
        initializeComponents();
        setupLayout();
        updateDisplay();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240)); // Light gray background
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }
    
    private void setupLayout() {
        // Main container with table layout (70% left, 30% right)
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setOpaque(false);
        
        // Left panel (70% width) - Statement of Accounts
        JPanel leftPanel = createStatementLeftPanel();
        leftPanel.setPreferredSize(new Dimension(700, 0));
        
        // Right panel (30% width) - Online Payment Channels
        JPanel rightPanel = createPaymentChannelsPanel();
        rightPanel.setPreferredSize(new Dimension(300, 0));
        
        // Add panels to main container
        mainContainer.add(leftPanel, BorderLayout.WEST);
        mainContainer.add(rightPanel, BorderLayout.EAST);
        
        add(mainContainer, BorderLayout.CENTER);
        
        // Footer
        add(createFooter(), BorderLayout.SOUTH);
    }
    
    /**
     * Creates the left panel with Statement of Accounts and Breakdown of Fees
     */
    private JPanel createStatementLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        
        // Statement of Accounts Panel
        leftPanel.add(createStatementOfAccountsPanel());
        leftPanel.add(Box.createVerticalStrut(10));
        
        // Breakdown of Fees Panel
        leftPanel.add(createBreakdownOfFeesPanel());
        
        return leftPanel;
    }
    
    /**
     * Creates the Statement of Accounts panel matching HTML structure
     */
    private JPanel createStatementOfAccountsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Header with icon
        JPanel headerPanel = createPanelHeader("Statement of Accounts (FIRST SEMESTER, 2025-2026)", "📊");
        panel.add(headerPanel, BorderLayout.NORTH);
        
        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        
        // Student Information Section
        contentPanel.add(createStudentInfoSection());
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Amount Due Section
        contentPanel.add(createAmountDueSection());
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Remaining Balance Section
        contentPanel.add(createRemainingBalanceSection());
        contentPanel.add(Box.createVerticalStrut(15));
        
        // PRELIM Status Section
        contentPanel.add(createPrelimStatusSection());
        
        panel.add(contentPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Creates the Breakdown of Fees panel
     */
    private JPanel createBreakdownOfFeesPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Header
        JPanel headerPanel = createPanelHeader("Breakdown of fees as of " + getCurrentDate(), "📋");
        panel.add(headerPanel, BorderLayout.NORTH);
        
        // Table
        feeTableModel = new DefaultTableModel(new String[]{"Date", "Description", "Amount"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        feeBreakdownTable = new JTable(feeTableModel);
        feeBreakdownTable.setRowHeight(25);
        feeBreakdownTable.setFont(new Font("Arial", Font.PLAIN, 12));
        feeBreakdownTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        feeBreakdownTable.getTableHeader().setBackground(new Color(70, 130, 180)); // Steel blue
        feeBreakdownTable.getTableHeader().setForeground(Color.WHITE);
        
        // Set column widths
        feeBreakdownTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        feeBreakdownTable.getColumnModel().getColumn(1).setPreferredWidth(300);
        feeBreakdownTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        
        JScrollPane scrollPane = new JScrollPane(feeBreakdownTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Creates the Online Payment Channels panel
     */
    private JPanel createPaymentChannelsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Header
        JPanel headerPanel = createPanelHeader("Online Payment Channels", "🛒");
        panel.add(headerPanel, BorderLayout.NORTH);
        
        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        
        // Instructional text
        JLabel instructionLabel = new JLabel("<html><center><b style='font-size: 18px; color: #0e284f;'>" +
            "Tuition fees can be paid via the available online payment channels.</b></center></html>");
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(instructionLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        
        // Separator line
        JSeparator separator = new JSeparator();
        separator.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(separator);
        contentPanel.add(Box.createVerticalStrut(15));
        
        // Payment channel buttons
        contentPanel.add(createPaymentButton("UnionBank UPay Online", "UB", new Color(255, 140, 0), "upay"));
        contentPanel.add(Box.createVerticalStrut(8));
        contentPanel.add(createPaymentButton("Dragonpay Payment Gateway", "🐉", new Color(220, 20, 60), "dragonpay"));
        contentPanel.add(Box.createVerticalStrut(8));
        contentPanel.add(createPaymentButton("BPI BPI Online", "BPI", new Color(139, 0, 0), "bpi"));
        contentPanel.add(Box.createVerticalStrut(8));
        contentPanel.add(createPaymentButton("BDO BDO Online", "BDO", new Color(0, 0, 139), "bdo"));
        contentPanel.add(Box.createVerticalStrut(8));
        contentPanel.add(createPaymentButton("BDO Bills Payment", "BDO", new Color(0, 0, 139), "bdobills"));
        contentPanel.add(Box.createVerticalStrut(8));
        contentPanel.add(createPaymentButton("Bukas Tuition Installment Plans", "Bukas", new Color(135, 206, 235), "bukas"));
        
        panel.add(contentPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Creates a payment channel button
     */
    private JButton createPaymentButton(String text, String icon, Color color, String channel) {
        JButton button = new JButton("<html><center><b>" + icon + "</b><br>" + text + "</center></html>");
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(250, 50));
        button.setMaximumSize(new Dimension(250, 50));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addActionListener(e -> openPaymentDialog(channel, text));
        
        return button;
    }
    
    /**
     * Creates panel header with icon and title
     */
    private JPanel createPanelHeader(String title, String icon) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180)); // Steel blue
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel titleLabel = new JLabel(icon + " " + title);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        return headerPanel;
    }
    
    /**
     * Creates student information section
     */
    private JPanel createStudentInfoSection() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        
        // Student icon
        JLabel iconLabel = new JLabel("👤");
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));
        
        // Student details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setOpaque(false);
        
        JLabel idLabel = new JLabel(studentID + " | " + studentProgram);
        idLabel.setFont(new Font("Arial", Font.BOLD, 14));
        idLabel.setForeground(new Color(70, 130, 180));
        
        JLabel nameLabel = new JLabel(studentName);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setForeground(new Color(50, 50, 50));
        
        detailsPanel.add(idLabel);
        detailsPanel.add(Box.createVerticalStrut(5));
        detailsPanel.add(nameLabel);
        
        panel.add(iconLabel, BorderLayout.WEST);
        panel.add(detailsPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Creates amount due section
     */
    private JPanel createAmountDueSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Your amount due for PRELIM is:");
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel amountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        amountPanel.setOpaque(false);
        
        JLabel pesoLabel = new JLabel("P ");
        pesoLabel.setFont(new Font("Arial", Font.BOLD, 50));
        pesoLabel.setForeground(Color.BLACK);
        
        amountDueLabel = new JLabel("0.00");
        amountDueLabel.setFont(new Font("Arial", Font.BOLD, 50));
        amountDueLabel.setForeground(new Color(144, 24, 24)); // Dark red
        
        amountPanel.add(pesoLabel);
        amountPanel.add(amountDueLabel);
        
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(amountPanel);
        
        return panel;
    }
    
    /**
     * Creates remaining balance section
     */
    private JPanel createRemainingBalanceSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Your remaining balance as of " + getCurrentDate() + " is:");
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel amountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        amountPanel.setOpaque(false);
        
        JLabel pesoLabel = new JLabel("P ");
        pesoLabel.setFont(new Font("Arial", Font.BOLD, 50));
        pesoLabel.setForeground(Color.BLACK);
        
        remainingBalanceLabel = new JLabel("0.00");
        remainingBalanceLabel.setFont(new Font("Arial", Font.BOLD, 50));
        remainingBalanceLabel.setForeground(new Color(144, 24, 24)); // Dark red
        
        amountPanel.add(pesoLabel);
        amountPanel.add(remainingBalanceLabel);
        
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(amountPanel);
        
        return panel;
    }
    
    /**
     * Creates PRELIM status section
     */
    private JPanel createPrelimStatusSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        
        prelimStatusLabel = new JLabel();
        prelimStatusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        prelimStatusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(prelimStatusLabel);
        
        return panel;
    }
    
    /**
     * Creates footer
     */
    private JPanel createFooter() {
        JPanel footerPanel = new JPanel();
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JLabel footerLabel = new JLabel("Copyright © " + LocalDate.now().getYear() + 
            " Saint Louis University Inc. All rights reserved.");
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        footerLabel.setForeground(new Color(100, 100, 100));
        
        footerPanel.add(footerLabel);
        
        return footerPanel;
    }
    
    /**
     * Opens payment dialog for the specified channel
     */
    private void openPaymentDialog(String channel, String channelName) {
        if (channel.equals("bukas")) {
            // Open external link for Bukas
            JOptionPane.showMessageDialog(this, 
                "Redirecting to Bukas Tuition Installment Plans...\n" +
                "URL: https://bukas.ph/s/slu", 
                "External Payment", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Create payment dialog
        paymentDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
            "Payment through " + channelName, true);
        paymentDialog.setSize(500, 400);
        paymentDialog.setLocationRelativeTo(this);
        paymentDialog.setResizable(false);
        
        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BorderLayout());
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Amount input section
        JPanel amountPanel = new JPanel();
        amountPanel.setLayout(new BoxLayout(amountPanel, BoxLayout.Y_AXIS));
        
        JLabel amountLabel = new JLabel("AMOUNT TO PAY");
        amountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        amountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        amountField = new JTextField();
        amountField.setFont(new Font("Arial", Font.PLAIN, 30));
        amountField.setHorizontalAlignment(JTextField.CENTER);
        amountField.setPreferredSize(new Dimension(300, 50));
        amountField.setMaximumSize(new Dimension(300, 50));
        amountField.setAlignmentX(Component.CENTER_ALIGNMENT);
        amountField.setText(String.format("%.2f", accountStatement.getExamPeriodDue(AccountStatement.ExamPeriod.PRELIM)));
        
        // Payment method selection (for Dragonpay)
        JPanel methodPanel = new JPanel();
        methodPanel.setLayout(new BoxLayout(methodPanel, BoxLayout.Y_AXIS));
        methodPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        if (channel.equals("dragonpay")) {
            JLabel methodLabel = new JLabel("SELECT A PAYMENT OPTION");
            methodLabel.setFont(new Font("Arial", Font.BOLD, 12));
            methodLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            String[] methods = {"GCash", "PayMaya", "BPI Online", "BDO Online", "7-Eleven", "Bayad Center"};
            paymentMethodCombo = new JComboBox<>(methods);
            paymentMethodCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
            paymentMethodCombo.setPreferredSize(new Dimension(200, 30));
            
            methodPanel.add(methodLabel);
            methodPanel.add(Box.createVerticalStrut(5));
            methodPanel.add(paymentMethodCombo);
            
            // Service charge note
            JLabel noteLabel = new JLabel("<html><center>Note: There will be a twenty five pesos (P 25.00) service charge for using dragon pay.<br>" +
                "An additional fee will be charged depending on the payment channel.</center></html>");
            noteLabel.setFont(new Font("Arial", Font.PLAIN, 10));
            noteLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            methodPanel.add(Box.createVerticalStrut(10));
            methodPanel.add(noteLabel);
        }
        
        // Proceed button
        JButton proceedButton = new JButton("Proceed");
        proceedButton.setFont(new Font("Arial", Font.BOLD, 14));
        proceedButton.setPreferredSize(new Dimension(100, 35));
        proceedButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        proceedButton.addActionListener(e -> processPayment(channel, channelName));
        
        amountPanel.add(amountLabel);
        amountPanel.add(Box.createVerticalStrut(20));
        amountPanel.add(amountField);
        amountPanel.add(Box.createVerticalStrut(20));
        amountPanel.add(methodPanel);
        amountPanel.add(Box.createVerticalStrut(20));
        amountPanel.add(proceedButton);
        
        dialogPanel.add(amountPanel, BorderLayout.CENTER);
        paymentDialog.add(dialogPanel);
        paymentDialog.setVisible(true);
    }
    
    /**
     * Processes payment through the specified channel
     */
    private void processPayment(String channel, String channelName) {
        try {
            double amount = Double.parseDouble(amountField.getText());
            
            if (amount <= 0) {
                JOptionPane.showMessageDialog(paymentDialog, 
                    "Please enter a valid amount greater than zero.", 
                    "Invalid Amount", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Generate reference number
            String reference = generatePaymentReference(channel);
            
            // Process payment
            AccountStatement.PaymentResult result = AccountStatementManager.processPayment(
                studentID, amount, channelName, reference);
            
            if (result.success) {
                // Update account statement
                accountStatement = AccountStatementManager.getStatement(studentID);
                
                // Show success message
                StringBuilder message = new StringBuilder();
                message.append("Payment Successful!\n\n");
                message.append("Channel: ").append(channelName).append("\n");
                message.append("Amount: P ").append(String.format("%,.2f", amount)).append("\n");
                message.append("Reference: ").append(reference).append("\n");
                message.append("New Balance: P ").append(String.format("%,.2f", result.newBalance)).append("\n\n");
                
                if (result.newOverpayment > 0) {
                    message.append("Overpayment: P ").append(String.format("%,.2f", result.newOverpayment)).append("\n");
                }
                
                message.append("\nExam Eligibility Status:\n");
                message.append("Prelim: ").append(accountStatement.getExamPeriodDue(AccountStatement.ExamPeriod.PRELIM) <= 0 ? "✓ ELIGIBLE" : "✗ NOT ELIGIBLE").append("\n");
                message.append("Midterm: ").append(accountStatement.getExamPeriodDue(AccountStatement.ExamPeriod.MIDTERM) <= 0 ? "✓ ELIGIBLE" : "✗ NOT ELIGIBLE").append("\n");
                message.append("Finals: ").append(accountStatement.getExamPeriodDue(AccountStatement.ExamPeriod.FINALS) <= 0 ? "✓ ELIGIBLE" : "✗ NOT ELIGIBLE").append("\n");
                
                JOptionPane.showMessageDialog(paymentDialog, message.toString(), 
                    "Payment Successful", JOptionPane.INFORMATION_MESSAGE);
                
                // Close dialog and update display
                paymentDialog.dispose();
                updateDisplay();
                
            } else {
                JOptionPane.showMessageDialog(paymentDialog, result.message, 
                    "Payment Failed", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(paymentDialog, 
                "Please enter a valid numeric amount.", 
                "Invalid Input", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Generates a payment reference number
     */
    private String generatePaymentReference(String channel) {
        String prefix = channel.toUpperCase().substring(0, Math.min(3, channel.length()));
        long timestamp = System.currentTimeMillis() % 1000000;
        return prefix + String.format("%06d", timestamp);
    }
    
    /**
     * Updates the display with current account information
     */
    public void updateDisplay() {
        // Update amount due
        double prelimDue = accountStatement.getExamPeriodDue(AccountStatement.ExamPeriod.PRELIM);
        amountDueLabel.setText(String.format("%,.2f", prelimDue));
        
        // Update remaining balance
        remainingBalanceLabel.setText(String.format("%,.2f", accountStatement.getBalance()));
        
        // Update PRELIM status
        String statusText;
        Color statusColor;
        
        if (prelimDue <= 0) {
            statusText = "PRELIM STATUS: PAID. Permitted to take the exams.";
            statusColor = new Color(0, 150, 0); // Green
        } else {
            statusText = "PRELIM STATUS: NOT PAID. Please pay before prelim exams. " +
                "Ignore If you're SLU Dependent or Full TOF Scholar. " +
                "For verification on unposted payments after \"as of\" date, please email sass@slu.edu.ph";
            statusColor = new Color(200, 0, 0); // Red
        }
        
        prelimStatusLabel.setText(statusText);
        prelimStatusLabel.setForeground(statusColor);
        
        // Update fee breakdown table
        updateFeeBreakdownTable();
    }
    
    /**
     * Updates the fee breakdown table with current data
     */
    private void updateFeeBreakdownTable() {
        feeTableModel.setRowCount(0);
        
        // Add beginning balance
        feeTableModel.addRow(new Object[]{"", "BEGINNING BALANCE", "0.00"});
        
        // Add payment history
        for (PaymentTransaction payment : accountStatement.getPaymentHistory()) {
            String amount = payment.getAmount().replace("P ", "").replace(",", "");
            if (amount.startsWith("(") && amount.endsWith(")")) {
                // Payment (negative amount)
                feeTableModel.addRow(new Object[]{
                    payment.getDate().split(" ")[0], // Date only
                    "PAYMENT RECEIVED (" + payment.getReference() + ")",
                    amount
                });
            }
        }
        
        // Add fee breakdowns
        for (FeeBreakdown fee : accountStatement.getFeeBreakdowns()) {
            String date = fee.getDatePosted() != null ? 
                fee.getDatePosted().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")) : "";
            String amount = String.format("%,.2f", fee.getAmount());
            
            feeTableModel.addRow(new Object[]{date, fee.getDescription(), amount});
        }
    }
    
    /**
     * Gets current date in the required format
     */
    private String getCurrentDate() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
    }
    
    /**
     * Refreshes the account statement data
     */
    public void refreshAccountStatement() {
        accountStatement = AccountStatementManager.getStatement(studentID);
        updateDisplay();
    }
}