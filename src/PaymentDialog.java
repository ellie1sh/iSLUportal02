import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Payment Dialog for different payment channels
 * Implements the payment interface shown in the HTML images
 */
public class PaymentDialog extends JDialog {
    
    private static final Color HEADER_BLUE = new Color(14, 40, 79);
    private static final Color WHITE = Color.WHITE;
    private static final Color RED_AMOUNT = new Color(144, 24, 24);
    
    private String paymentMethod;
    private String studentID;
    private double amountToPay;
    private JTextField amountField;
    private JComboBox<String> paymentOptionCombo;
    private JLabel totalAmountLabel;
    private JLabel serviceChargeLabel;
    private JButton proceedButton;
    private JButton cancelButton;
    
    public PaymentDialog(Frame parent, String paymentMethod, String studentID, double amountToPay) {
        super(parent, "Payment through " + paymentMethod, true);
        this.paymentMethod = paymentMethod;
        this.studentID = studentID;
        this.amountToPay = amountToPay;
        
        initializeComponents();
        setupLayout();
        setupEventListeners();
        
        setSize(500, 400);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    private void initializeComponents() {
        // Amount to pay field
        amountField = new JTextField(String.format("%.0f", amountToPay));
        amountField.setFont(new Font("Arial", Font.PLAIN, 24));
        amountField.setHorizontalAlignment(SwingConstants.CENTER);
        amountField.setPreferredSize(new Dimension(300, 50));
        
        // Payment option combo box
        paymentOptionCombo = new JComboBox<>();
        setupPaymentOptions();
        paymentOptionCombo.setFont(new Font("Arial", Font.PLAIN, 12));
        paymentOptionCombo.setPreferredSize(new Dimension(300, 30));
        
        // Service charge label
        serviceChargeLabel = new JLabel();
        serviceChargeLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        serviceChargeLabel.setForeground(RED_AMOUNT);
        
        // Total amount label
        totalAmountLabel = new JLabel();
        totalAmountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalAmountLabel.setForeground(HEADER_BLUE);
        
        // Buttons
        proceedButton = new JButton("Proceed");
        proceedButton.setBackground(HEADER_BLUE);
        proceedButton.setForeground(WHITE);
        proceedButton.setFont(new Font("Arial", Font.BOLD, 12));
        proceedButton.setPreferredSize(new Dimension(100, 35));
        
        cancelButton = new JButton("Cancel");
        cancelButton.setBackground(Color.GRAY);
        cancelButton.setForeground(WHITE);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 12));
        cancelButton.setPreferredSize(new Dimension(100, 35));
    }
    
    private void setupPaymentOptions() {
        switch (paymentMethod.toLowerCase()) {
            case "dragon pay":
                setupDragonpayOptions();
                break;
            case "upay by unionbank":
                setupUPayOptions();
                break;
            case "bpi":
                setupBPIOptions();
                break;
            case "bdo online":
                setupBDOOnlineOptions();
                break;
            case "bdo bills payment":
                setupBDOBillsOptions();
                break;
            default:
                setupDefaultOptions();
        }
    }
    
    private void setupDragonpayOptions() {
        paymentOptionCombo.addItem("GCash");
        paymentOptionCombo.addItem("PayMaya");
        paymentOptionCombo.addItem("Coins.ph Wallet");
        paymentOptionCombo.addItem("GrabPay");
        paymentOptionCombo.addItem("Shopeepay");
        paymentOptionCombo.addItem("BPI Online");
        paymentOptionCombo.addItem("BDO Online");
        paymentOptionCombo.addItem("Metrobank Online");
        paymentOptionCombo.addItem("RCBC Online");
        paymentOptionCombo.addItem("UnionBank Online");
        paymentOptionCombo.addItem("Banco de Oro ATM");
        paymentOptionCombo.addItem("BPI ATM");
        paymentOptionCombo.addItem("Metrobank ATM");
        paymentOptionCombo.addItem("RCBC ATM");
        paymentOptionCombo.addItem("UnionBank ATM");
        
        serviceChargeLabel.setText("Note: There will be a twenty five pesos (P 25.00) service charge for using dragon pay. " +
                                 "An additional fee will be charged depending on the payment channel.");
    }
    
    private void setupUPayOptions() {
        paymentOptionCombo.addItem("UnionBank Online Banking");
        paymentOptionCombo.addItem("UnionBank Mobile App");
        paymentOptionCombo.addItem("UnionBank ATM");
        
        serviceChargeLabel.setText("Note: No additional service charges for UnionBank UPay transactions.");
    }
    
    private void setupBPIOptions() {
        paymentOptionCombo.addItem("BPI Online Banking");
        paymentOptionCombo.addItem("BPI Mobile App");
        paymentOptionCombo.addItem("BPI ATM");
        
        serviceChargeLabel.setText("Note: Standard BPI online banking fees may apply.");
    }
    
    private void setupBDOOnlineOptions() {
        paymentOptionCombo.addItem("BDO Online Banking");
        paymentOptionCombo.addItem("BDO Mobile App");
        paymentOptionCombo.addItem("BDO ATM");
        
        serviceChargeLabel.setText("Note: Standard BDO online banking fees may apply.");
    }
    
    private void setupBDOBillsOptions() {
        paymentOptionCombo.addItem("BDO Bills Payment - Online");
        paymentOptionCombo.addItem("BDO Bills Payment - Mobile");
        paymentOptionCombo.addItem("BDO Bills Payment - ATM");
        
        serviceChargeLabel.setText("Note: P 15.00 service charge for BDO Bills Payment transactions.");
    }
    
    private void setupDefaultOptions() {
        paymentOptionCombo.addItem("Online Banking");
        paymentOptionCombo.addItem("Mobile App");
        paymentOptionCombo.addItem("ATM");
        
        serviceChargeLabel.setText("Note: Standard banking fees may apply.");
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(WHITE);
        
        // Title section
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(WHITE);
        
        JLabel titleLabel = new JLabel("AMOUNT TO PAY");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(HEADER_BLUE);
        titlePanel.add(titleLabel);
        
        mainPanel.add(titlePanel);
        mainPanel.add(Box.createVerticalStrut(10));
        
        // Amount field
        JPanel amountPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        amountPanel.setBackground(WHITE);
        amountPanel.add(amountField);
        mainPanel.add(amountPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Payment option section
        JPanel optionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        optionPanel.setBackground(WHITE);
        
        JLabel optionLabel = new JLabel("SELECT A PAYMENT OPTION");
        optionLabel.setFont(new Font("Arial", Font.BOLD, 12));
        optionLabel.setForeground(HEADER_BLUE);
        optionPanel.add(optionLabel);
        
        mainPanel.add(optionPanel);
        mainPanel.add(Box.createVerticalStrut(5));
        
        // Payment option combo
        JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        comboPanel.setBackground(WHITE);
        comboPanel.add(paymentOptionCombo);
        mainPanel.add(comboPanel);
        mainPanel.add(Box.createVerticalStrut(15));
        
        // Service charge note
        JPanel notePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        notePanel.setBackground(WHITE);
        notePanel.add(serviceChargeLabel);
        mainPanel.add(notePanel);
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Total amount section
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        totalPanel.setBackground(WHITE);
        
        JLabel totalLabel = new JLabel("AMOUNT TO PAY + CHARGES:");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 12));
        totalLabel.setForeground(HEADER_BLUE);
        totalPanel.add(totalLabel);
        
        mainPanel.add(totalPanel);
        mainPanel.add(Box.createVerticalStrut(5));
        
        // Total amount value
        JPanel totalValuePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        totalValuePanel.setBackground(WHITE);
        totalValuePanel.add(totalAmountLabel);
        mainPanel.add(totalValuePanel);
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(WHITE);
        buttonPanel.add(cancelButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(proceedButton);
        mainPanel.add(buttonPanel);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Update total amount initially
        updateTotalAmount();
    }
    
    private void setupEventListeners() {
        // Amount field listener
        amountField.addActionListener(e -> updateTotalAmount());
        amountField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateTotalAmount(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateTotalAmount(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateTotalAmount(); }
        });
        
        // Payment option combo listener
        paymentOptionCombo.addActionListener(e -> updateTotalAmount());
        
        // Proceed button
        proceedButton.addActionListener(e -> processPayment());
        
        // Cancel button
        cancelButton.addActionListener(e -> dispose());
    }
    
    private void updateTotalAmount() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            double serviceCharge = calculateServiceCharge();
            double total = amount + serviceCharge;
            
            totalAmountLabel.setText(String.format("P %,.2f", total));
        } catch (NumberFormatException e) {
            totalAmountLabel.setText("P 0.00");
        }
    }
    
    private double calculateServiceCharge() {
        String selectedOption = (String) paymentOptionCombo.getSelectedItem();
        
        switch (paymentMethod.toLowerCase()) {
            case "dragon pay":
                return 25.00; // Base Dragonpay charge
            case "bdo bills payment":
                return 15.00; // BDO Bills Payment charge
            case "upay by unionbank":
                return 0.00; // No charge for UPay
            default:
                return 0.00; // No additional charge for other methods
        }
    }
    
    private void processPayment() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            String paymentChannel = (String) paymentOptionCombo.getSelectedItem();
            
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Please enter a valid amount.", "Invalid Amount", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Generate reference number
            String reference = generateReferenceNumber();
            
            // Process payment through AccountStatementManager
            AccountStatement.PaymentResult result = AccountStatementManager.processPayment(
                studentID, amount, paymentChannel, reference
            );
            
            if (result.success) {
                JOptionPane.showMessageDialog(this, 
                    "Payment processed successfully!\n" + result.message, 
                    "Payment Successful", 
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Payment failed!\n" + result.message, 
                    "Payment Failed", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount.", "Invalid Amount", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String generateReferenceNumber() {
        // Generate a reference number similar to the ones in the images
        Random random = new Random();
        int number = random.nextInt(90000000) + 10000000; // 8-digit number
        return String.format("%08d", number);
    }
}