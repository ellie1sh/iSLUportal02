import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

/**
 * Statement of Accounts Panel for iSLU Student Portal
 * Implements the visual design from the HTML template with Java Swing
 * Matches the layout and functionality shown in the provided images
 */
public class StatementOfAccountsPanel extends JPanel {
    
    // Color scheme matching the HTML design
    private static final Color HEADER_BLUE = new Color(14, 40, 79); // #0e284f
    private static final Color DARK_BLUE = new Color(25, 50, 100);
    private static final Color LIGHT_GRAY = new Color(240, 240, 240);
    private static final Color WHITE = Color.WHITE;
    private static final Color RED_AMOUNT = new Color(144, 24, 24); // #901818
    private static final Color GREEN_STATUS = new Color(0, 150, 0);
    private static final Color RED_STATUS = new Color(200, 0, 0);
    
    // Components
    private AccountStatement accountStatement;
    private String studentID;
    private JLabel amountDueValueLabel;
    private JLabel remainingBalanceLabel;
    private JLabel prelimStatusLabel;
    private JTable feeBreakdownTable;
    private DefaultTableModel feeTableModel;
    private JDialog paymentDialog;
    
    // Payment channel buttons
    private JButton upayButton, dragonpayButton, bpiButton, bdoButton, bdoBillsButton, bukasButton;
    
    public StatementOfAccountsPanel(String studentID) {
        this.studentID = studentID;
        this.accountStatement = AccountStatementManager.getStatement(studentID);
        
        initializeComponents();
        setupLayout();
        updateDisplay();
    }
    
    private void initializeComponents() {
        setBackground(LIGHT_GRAY);
        setLayout(new BorderLayout());
        
        // Initialize payment buttons
        initializePaymentButtons();
        
        // Initialize payment dialog
        initializePaymentDialog();
        
        // Setup payment button listeners
        setupPaymentButtonListeners();
    }
    
    private void setupLayout() {
        // Main container with two columns
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(LIGHT_GRAY);
        mainContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Left panel (70% width) - Statement of Accounts
        JPanel leftPanel = createLeftPanel();
        leftPanel.setPreferredSize(new Dimension(700, 0));
        
        // Right panel (30% width) - Online Payment Channels
        JPanel rightPanel = createRightPanel();
        rightPanel.setPreferredSize(new Dimension(300, 0));
        
        // Add panels to main container
        mainContainer.add(leftPanel, BorderLayout.CENTER);
        mainContainer.add(rightPanel, BorderLayout.EAST);
        
        add(mainContainer, BorderLayout.CENTER);
    }
    
    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(LIGHT_GRAY);
        
        // Statement of Accounts Panel
        JPanel statementPanel = createStatementPanel();
        leftPanel.add(statementPanel);
        leftPanel.add(Box.createVerticalStrut(10));
        
        // Fee Breakdown Panel
        JPanel breakdownPanel = createBreakdownPanel();
        leftPanel.add(breakdownPanel);
        
        return leftPanel;
    }
    
    private JPanel createStatementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Header
        JPanel headerPanel = createHeaderPanel("Statement of Accounts (FIRST SEMESTER, 2025-2026)", "📊");
        panel.add(headerPanel, BorderLayout.NORTH);
        
        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(WHITE);
        
        // Student Information
        JPanel studentInfoPanel = createStudentInfoPanel();
        contentPanel.add(studentInfoPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Amount Due for PRELIM
        JPanel amountDuePanel = createAmountDuePanel();
        contentPanel.add(amountDuePanel);
        contentPanel.add(Box.createVerticalStrut(15));
        
        // Remaining Balance
        JPanel balancePanel = createBalancePanel();
        contentPanel.add(balancePanel);
        contentPanel.add(Box.createVerticalStrut(15));
        
        // PRELIM Status
        JPanel statusPanel = createStatusPanel();
        contentPanel.add(statusPanel);
        
        panel.add(contentPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createHeaderPanel(String title, String icon) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(HEADER_BLUE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel titleLabel = new JLabel(icon + " " + title);
        titleLabel.setForeground(WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        return headerPanel;
    }
    
    private JPanel createStudentInfoPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(WHITE);
        
        // Student icon
        JLabel iconLabel = new JLabel("👤");
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        panel.add(iconLabel);
        
        // Student details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(WHITE);
        
        // Get student info from database
        StudentInfo studentInfo = StudentDataManager.getStudentById(studentID);
        String studentDetails = studentID + " | " + (studentInfo != null ? studentInfo.getProgram() : "BSIT 2");
        String studentName = studentInfo != null ? studentInfo.getFullName() : "Student Name";
        
        JLabel detailsLabel = new JLabel(studentDetails);
        detailsLabel.setFont(new Font("Arial", Font.BOLD, 12));
        detailsLabel.setForeground(HEADER_BLUE);
        
        JLabel nameLabel = new JLabel(studentName);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setForeground(HEADER_BLUE);
        
        detailsPanel.add(detailsLabel);
        detailsPanel.add(nameLabel);
        
        panel.add(detailsPanel);
        
        return panel;
    }
    
    private JPanel createAmountDuePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(WHITE);
        
        // Amount due text
        JLabel amountDueText = new JLabel("Your amount due for ");
        amountDueText.setFont(new Font("Arial", Font.PLAIN, 20));
        
        JLabel prelimText = new JLabel("PRELIM");
        prelimText.setFont(new Font("Arial", Font.BOLD, 20));
        prelimText.setForeground(HEADER_BLUE);
        
        JLabel isText = new JLabel(" is:");
        isText.setFont(new Font("Arial", Font.PLAIN, 20));
        
        panel.add(amountDueText);
        panel.add(prelimText);
        panel.add(isText);
        
        // Amount value
        double prelimDue = accountStatement.getExamPeriodDue(AccountStatement.ExamPeriod.PRELIM);
        amountDueValueLabel = new JLabel("P " + String.format("%,.2f", prelimDue));
        amountDueValueLabel.setFont(new Font("Arial", Font.BOLD, 36));
        amountDueValueLabel.setForeground(RED_AMOUNT);
        
        JPanel amountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        amountPanel.setBackground(WHITE);
        amountPanel.add(amountDueValueLabel);
        
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(WHITE);
        container.add(panel);
        container.add(amountPanel);
        
        return container;
    }
    
    private JPanel createBalancePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(WHITE);
        
        // Balance text
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
        JLabel balanceText = new JLabel("Your remaining balance as of " + currentDate + " is:");
        balanceText.setFont(new Font("Arial", Font.PLAIN, 16));
        
        panel.add(balanceText);
        
        // Balance value
        remainingBalanceLabel = new JLabel("P " + String.format("%,.2f", accountStatement.getBalance()));
        remainingBalanceLabel.setFont(new Font("Arial", Font.BOLD, 36));
        remainingBalanceLabel.setForeground(RED_AMOUNT);
        
        JPanel balanceValuePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        balanceValuePanel.setBackground(WHITE);
        balanceValuePanel.add(remainingBalanceLabel);
        
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(WHITE);
        container.add(panel);
        container.add(balanceValuePanel);
        
        return container;
    }
    
    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(WHITE);
        
        // Update payment statuses
        accountStatement.updatePaymentStatuses();
        
        // PRELIM Status
        String statusText;
        Color statusColor;
        
        if (accountStatement.isPrelimPaid()) {
            statusText = "PRELIM STATUS: PAID. Permitted to take the exams.";
            statusColor = GREEN_STATUS;
        } else {
            statusText = "PRELIM STATUS: NOT PAID. Please pay before prelim exams. " +
                        "Ignore if you're SLU Dependent or Full TOF Scholar. " +
                        "For verification on unposted payments after \"as of\" date, please email sass@slu.edu.ph";
            statusColor = RED_STATUS;
        }
        
        prelimStatusLabel = new JLabel(statusText);
        prelimStatusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        prelimStatusLabel.setForeground(statusColor);
        
        panel.add(prelimStatusLabel);
        
        return panel;
    }
    
    private JPanel createBreakdownPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Header
        JPanel headerPanel = createHeaderPanel("Breakdown of fees as of " + 
            LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")), "📋");
        panel.add(headerPanel, BorderLayout.NORTH);
        
        // Table
        JPanel tablePanel = createBreakdownTable();
        panel.add(tablePanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createBreakdownTable() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WHITE);
        
        // Column names
        String[] columnNames = {"Date", "Description", "Amount"};
        
        // Create table data
        List<Object[]> tableData = createTableData();
        
        // Create table model
        feeTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Add data to model
        for (Object[] row : tableData) {
            feeTableModel.addRow(row);
        }
        
        // Create table
        feeBreakdownTable = new JTable(feeTableModel);
        feeBreakdownTable.setBackground(WHITE);
        feeBreakdownTable.setGridColor(Color.LIGHT_GRAY);
        feeBreakdownTable.setShowGrid(true);
        feeBreakdownTable.setRowHeight(25);
        feeBreakdownTable.setFont(new Font("Arial", Font.PLAIN, 11));
        
        // Set column widths
        TableColumnModel columnModel = feeBreakdownTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(80);  // Date
        columnModel.getColumn(1).setPreferredWidth(300); // Description
        columnModel.getColumn(2).setPreferredWidth(100); // Amount
        
        // Center align amount column
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        feeBreakdownTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        
        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(feeBreakdownTable);
        scrollPane.setBorder(null);
        scrollPane.setBackground(WHITE);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private List<Object[]> createTableData() {
        List<Object[]> data = new ArrayList<>();
        
        // Beginning balance
        data.add(new Object[]{"", "BEGINNING BALANCE", "0.00"});
        
        // Payment history (negative amounts)
        for (PaymentTransaction payment : accountStatement.getPaymentHistory()) {
            String date = payment.getDate().split(" ")[0]; // Get date part only
            String description = "PAYMENT RECEIVED (" + payment.getReference() + ")";
            String amount = payment.getAmount().replace("P ", "").replace(",", "");
            data.add(new Object[]{date, description, "(" + amount + ")"});
        }
        
        // Fee breakdowns
        for (FeeBreakdown fee : accountStatement.getFeeBreakdowns()) {
            String date = fee.getDatePosted().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            String description = fee.getDescription();
            String amount = String.format("%.2f", fee.getAmount());
            data.add(new Object[]{date, description, amount});
        }
        
        return data;
    }
    
    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(LIGHT_GRAY);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        
        // Online Payment Channels Panel
        JPanel paymentPanel = createPaymentChannelsPanel();
        rightPanel.add(paymentPanel);
        
        return rightPanel;
    }
    
    private JPanel createPaymentChannelsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Header
        JPanel headerPanel = createHeaderPanel("Online Payment Channels", "🛒");
        panel.add(headerPanel, BorderLayout.NORTH);
        
        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(WHITE);
        
        // Instruction text
        JLabel instructionLabel = new JLabel("Tuition fees can be paid via the available online payment channels.");
        instructionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        instructionLabel.setForeground(HEADER_BLUE);
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(instructionLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        
        // Separator line
        JSeparator separator = new JSeparator();
        separator.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(separator);
        contentPanel.add(Box.createVerticalStrut(15));
        
        // Payment buttons
        JPanel buttonsPanel = createPaymentButtonsPanel();
        contentPanel.add(buttonsPanel);
        
        panel.add(contentPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void initializePaymentButtons() {
        // UnionBank UPay Online
        upayButton = createPaymentButton("UnionBank UPay Online", new Color(255, 140, 0), "UB");
        
        // Dragonpay Payment Gateway
        dragonpayButton = createPaymentButton("Dragonpay Payment Gateway", new Color(220, 20, 60), "@dragonpay");
        
        // BPI Online
        bpiButton = createPaymentButton("BPI Online", new Color(128, 0, 0), "BPI");
        
        // BDO Online
        bdoButton = createPaymentButton("BDO Online", new Color(0, 100, 200), "BDO");
        
        // BDO Bills Payment
        bdoBillsButton = createPaymentButton("BDO Bills Payment", new Color(0, 100, 200), "BDO Bills Payment");
        
        // Bukas Tuition Installment Plans
        bukasButton = createPaymentButton("Bukas Tuition Installment Plans", new Color(100, 150, 255), "Bukas");
    }
    
    private JButton createPaymentButton(String text, Color color, String logo) {
        JButton button = new JButton(logo + " " + text);
        button.setBackground(color);
        button.setForeground(WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(250, 40));
        button.setPreferredSize(new Dimension(250, 40));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    private JPanel createPaymentButtonsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(WHITE);
        
        panel.add(upayButton);
        panel.add(Box.createVerticalStrut(8));
        panel.add(dragonpayButton);
        panel.add(Box.createVerticalStrut(8));
        panel.add(bpiButton);
        panel.add(Box.createVerticalStrut(8));
        panel.add(bdoButton);
        panel.add(Box.createVerticalStrut(8));
        panel.add(bdoBillsButton);
        panel.add(Box.createVerticalStrut(8));
        panel.add(bukasButton);
        
        return panel;
    }
    
    private void initializePaymentDialog() {
        // Payment dialog will be created dynamically for each payment method
        // This method is kept for compatibility but the actual dialog creation
        // is handled in showPaymentDialog method
    }
    
    private void setupPaymentButtonListeners() {
        upayButton.addActionListener(e -> showPaymentDialog("UPay by UnionBank"));
        dragonpayButton.addActionListener(e -> showPaymentDialog("Dragon Pay"));
        bpiButton.addActionListener(e -> showPaymentDialog("BPI"));
        bdoButton.addActionListener(e -> showPaymentDialog("BDO Online"));
        bdoBillsButton.addActionListener(e -> showPaymentDialog("BDO Bills Payment"));
        
        bukasButton.addActionListener(e -> {
            // Open Bukas website
            try {
                Desktop.getDesktop().browse(java.net.URI.create("https://bukas.ph/s/slu"));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Unable to open Bukas website", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    
    private void showPaymentDialog(String paymentMethod) {
        double prelimDue = accountStatement.getExamPeriodDue(AccountStatement.ExamPeriod.PRELIM);
        PaymentDialog dialog = new PaymentDialog(
            (Frame) SwingUtilities.getWindowAncestor(this), 
            paymentMethod, 
            studentID, 
            prelimDue
        );
        dialog.setVisible(true);
        
        // Update display after payment dialog closes
        updateDisplay();
    }
    
    public void updateDisplay() {
        // Update payment statuses
        accountStatement.updatePaymentStatuses();
        
        // Update amount due
        double prelimDue = accountStatement.getExamPeriodDue(AccountStatement.ExamPeriod.PRELIM);
        amountDueValueLabel.setText("P " + String.format("%,.2f", prelimDue));
        
        // Update remaining balance
        remainingBalanceLabel.setText("P " + String.format("%,.2f", accountStatement.getBalance()));
        
        // Update status
        String statusText;
        Color statusColor;
        
        if (accountStatement.isPrelimPaid()) {
            statusText = "PRELIM STATUS: PAID. Permitted to take the exams.";
            statusColor = GREEN_STATUS;
        } else {
            statusText = "PRELIM STATUS: NOT PAID. Please pay before prelim exams. " +
                        "Ignore if you're SLU Dependent or Full TOF Scholar. " +
                        "For verification on unposted payments after \"as of\" date, please email sass@slu.edu.ph";
            statusColor = RED_STATUS;
        }
        
        prelimStatusLabel.setText(statusText);
        prelimStatusLabel.setForeground(statusColor);
        
        // Update table
        updateTableData();
    }
    
    private void updateTableData() {
        // Clear existing data
        feeTableModel.setRowCount(0);
        
        // Add new data
        List<Object[]> tableData = createTableData();
        for (Object[] row : tableData) {
            feeTableModel.addRow(row);
        }
    }
    
    // Public method to process payments
    public void processPayment(double amount, String channel, String reference) {
        AccountStatement.PaymentResult result = AccountStatementManager.processPayment(studentID, amount, channel, reference);
        
        if (result.success) {
            updateDisplay();
            JOptionPane.showMessageDialog(this, result.message, "Payment Successful", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, result.message, "Payment Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}