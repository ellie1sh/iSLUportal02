import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Dialog for adding and editing fees
 */
public class FeeEditDialog extends JDialog {
    
    private static final Color HEADER_BLUE = new Color(14, 40, 79);
    private static final Color WHITE = Color.WHITE;
    
    private JTextField codeField;
    private JTextField descriptionField;
    private JTextField amountField;
    private JComboBox<FeeBreakdown.FeeType> typeCombo;
    private JTextField dateField;
    private JTextField remarksField;
    private JButton saveButton;
    private JButton cancelButton;
    
    private FeeBreakdown existingFee;
    private String studentID;
    private boolean isGlobal;
    
    public FeeEditDialog(Frame parent, String title, FeeBreakdown existingFee, String studentID) {
        super(parent, title, true);
        this.existingFee = existingFee;
        this.studentID = studentID;
        this.isGlobal = studentID == null;
        
        initializeComponents();
        setupLayout();
        setupEventListeners();
        
        if (existingFee != null) {
            populateFields();
        } else {
            setDefaultValues();
        }
        
        setSize(500, 400);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    private void initializeComponents() {
        // Text fields
        codeField = new JTextField(20);
        codeField.setFont(new Font("Arial", Font.PLAIN, 12));
        
        descriptionField = new JTextField(30);
        descriptionField.setFont(new Font("Arial", Font.PLAIN, 12));
        
        amountField = new JTextField(15);
        amountField.setFont(new Font("Arial", Font.PLAIN, 12));
        
        typeCombo = new JComboBox<>(FeeBreakdown.FeeType.values());
        typeCombo.setFont(new Font("Arial", Font.PLAIN, 12));
        
        dateField = new JTextField(15);
        dateField.setFont(new Font("Arial", Font.PLAIN, 12));
        dateField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        
        remarksField = new JTextField(30);
        remarksField.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // Buttons
        saveButton = new JButton("Save");
        saveButton.setBackground(HEADER_BLUE);
        saveButton.setForeground(WHITE);
        saveButton.setFont(new Font("Arial", Font.BOLD, 12));
        saveButton.setPreferredSize(new Dimension(100, 35));
        
        cancelButton = new JButton("Cancel");
        cancelButton.setBackground(Color.GRAY);
        cancelButton.setForeground(WHITE);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 12));
        cancelButton.setPreferredSize(new Dimension(100, 35));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(HEADER_BLUE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel(isGlobal ? "🌍 Global Fee" : "👤 Student Fee");
        titleLabel.setForeground(WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Form fields
        mainPanel.add(createFormField("Fee Code:", codeField));
        mainPanel.add(Box.createVerticalStrut(10));
        
        mainPanel.add(createFormField("Description:", descriptionField));
        mainPanel.add(Box.createVerticalStrut(10));
        
        mainPanel.add(createFormField("Amount (P):", amountField));
        mainPanel.add(Box.createVerticalStrut(10));
        
        mainPanel.add(createFormField("Type:", typeCombo));
        mainPanel.add(Box.createVerticalStrut(10));
        
        mainPanel.add(createFormField("Date Posted:", dateField));
        mainPanel.add(Box.createVerticalStrut(10));
        
        mainPanel.add(createFormField("Remarks:", remarksField));
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Student ID field (for student-specific fees)
        if (!isGlobal) {
            JPanel studentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            studentPanel.setBackground(WHITE);
            
            JLabel studentLabel = new JLabel("Student ID:");
            studentLabel.setFont(new Font("Arial", Font.BOLD, 12));
            studentLabel.setPreferredSize(new Dimension(100, 25));
            
            JTextField studentIDField = new JTextField(studentID);
            studentIDField.setEditable(false);
            studentIDField.setBackground(Color.LIGHT_GRAY);
            studentIDField.setFont(new Font("Arial", Font.PLAIN, 12));
            studentIDField.setPreferredSize(new Dimension(150, 25));
            
            studentPanel.add(studentLabel);
            studentPanel.add(studentIDField);
            mainPanel.add(studentPanel);
            mainPanel.add(Box.createVerticalStrut(10));
        }
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(WHITE);
        buttonPanel.add(cancelButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(saveButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createFormField(String labelText, JComponent component) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(WHITE);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setPreferredSize(new Dimension(120, 25));
        
        panel.add(label);
        panel.add(component);
        
        return panel;
    }
    
    private void setupEventListeners() {
        saveButton.addActionListener(e -> saveFee());
        cancelButton.addActionListener(e -> dispose());
        
        // Enter key to save
        KeyAdapter enterKeyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    saveFee();
                }
            }
        };
        
        codeField.addKeyListener(enterKeyAdapter);
        descriptionField.addKeyListener(enterKeyAdapter);
        amountField.addKeyListener(enterKeyAdapter);
        dateField.addKeyListener(enterKeyAdapter);
        remarksField.addKeyListener(enterKeyAdapter);
    }
    
    private void populateFields() {
        if (existingFee != null) {
            codeField.setText(existingFee.getCode());
            descriptionField.setText(existingFee.getDescription());
            amountField.setText(String.format("%.2f", existingFee.getAmount()));
            typeCombo.setSelectedItem(existingFee.getType());
            dateField.setText(existingFee.getDatePosted().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            remarksField.setText(existingFee.getRemarks());
        }
    }
    
    private void setDefaultValues() {
        // Generate a default code
        String defaultCode = isGlobal ? "GF" : "SF";
        defaultCode += String.format("%03d", FeeDatabase.getGlobalFees().size() + 1);
        codeField.setText(defaultCode);
        
        // Set current date
        dateField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        
        // Set default type
        typeCombo.setSelectedItem(FeeBreakdown.FeeType.OTHER);
    }
    
    private void saveFee() {
        // Validate inputs
        if (!validateInputs()) {
            return;
        }
        
        try {
            String code = codeField.getText().trim();
            String description = descriptionField.getText().trim();
            double amount = Double.parseDouble(amountField.getText().trim());
            FeeBreakdown.FeeType type = (FeeBreakdown.FeeType) typeCombo.getSelectedItem();
            LocalDate date = LocalDate.parse(dateField.getText().trim());
            String remarks = remarksField.getText().trim();
            
            FeeBreakdown fee = new FeeBreakdown(code, description, amount, type, date, remarks);
            
            if (existingFee != null) {
                // Update existing fee
                if (isGlobal) {
                    FeeDatabase.updateGlobalFee(code, fee);
                } else {
                    FeeDatabase.updateStudentFee(studentID, code, fee);
                }
                JOptionPane.showMessageDialog(this, "Fee updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Add new fee
                if (isGlobal) {
                    FeeDatabase.addGlobalFee(fee);
                } else {
                    FeeDatabase.addStudentFee(studentID, fee);
                }
                JOptionPane.showMessageDialog(this, "Fee added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            
            dispose();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount.", "Invalid Amount", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving fee: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean validateInputs() {
        if (codeField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a fee code.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            codeField.requestFocus();
            return false;
        }
        
        if (descriptionField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a description.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            descriptionField.requestFocus();
            return false;
        }
        
        if (amountField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an amount.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            amountField.requestFocus();
            return false;
        }
        
        try {
            double amount = Double.parseDouble(amountField.getText().trim());
            if (amount < 0) {
                JOptionPane.showMessageDialog(this, "Amount cannot be negative.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                amountField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            amountField.requestFocus();
            return false;
        }
        
        if (dateField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a date.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            dateField.requestFocus();
            return false;
        }
        
        try {
            LocalDate.parse(dateField.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid date (yyyy-mm-dd).", "Validation Error", JOptionPane.ERROR_MESSAGE);
            dateField.requestFocus();
            return false;
        }
        
        return true;
    }
}