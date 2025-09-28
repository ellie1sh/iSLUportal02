import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

/**
 * Demo class to test the Statement of Accounts Panel
 * Shows the complete functionality with sample data
 */
public class StatementOfAccountsDemo extends JFrame {
    
    public StatementOfAccountsDemo() {
        initializeFrame();
        setupDemo();
    }
    
    private void initializeFrame() {
        setTitle("iSLU Student Portal - Statement of Accounts Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Use default look and feel
    }
    
    private void setupDemo() {
        // Create demo student data
        String demoStudentID = "2255146";
        
        // Initialize student data manager with demo data
        setupDemoStudentData(demoStudentID);
        
        // Create and add the statement of accounts panel
        StatementOfAccountsPanel soaPanel = new StatementOfAccountsPanel(demoStudentID);
        add(soaPanel, BorderLayout.CENTER);
        
        // Add header
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Add footer
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(14, 40, 79));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Logo and title
        JLabel logoLabel = new JLabel("🎓 iSLU Student Portal");
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(logoLabel, BorderLayout.WEST);
        
        // User info
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setBackground(new Color(14, 40, 79));
        
        JLabel userLabel = new JLabel("👤 SHERLIE O. RIVERA");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Arial", Font.BOLD, 12));
        userPanel.add(userLabel);
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(Color.RED);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 10));
        logoutButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        userPanel.add(logoutButton);
        
        headerPanel.add(userPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(240, 240, 240));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel footerLabel = new JLabel("Copyright © 2025 Saint Louis University Inc. All rights reserved.");
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        footerLabel.setForeground(Color.GRAY);
        footerPanel.add(footerLabel);
        
        return footerPanel;
    }
    
    private void setupDemoStudentData(String studentID) {
        // Create demo student info
        StudentInfo demoStudent = new StudentInfo(
            studentID,
            "RIVERA, SHERLIE OBILLE",
            "BSIT 2"
        );
        
        // Note: StudentDataManager doesn't have addStudent method
        // Students are loaded from database file
        
        // Get or create account statement
        AccountStatement statement = AccountStatementManager.getStatement(studentID);
        
        // Add some demo payments to show in the breakdown
        if (statement.getPaymentHistory().isEmpty()) {
            // Add demo payments
            AccountStatementManager.processPayment(studentID, 21200.00, "Cashier Payment", "00161556C");
            AccountStatementManager.processPayment(studentID, 7500.00, "Online Banking", "00162355C");
        }
        
        // Update payment statuses to show some as completed
        statement.updatePaymentStatuses();
    }
    
    public static void main(String[] args) {
        // Initialize data managers
        SharedDataManager.initialize();
        
        SwingUtilities.invokeLater(() -> {
            try {
                new StatementOfAccountsDemo().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, 
                    "Error starting demo: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}