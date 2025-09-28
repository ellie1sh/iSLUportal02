import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Demo application for Fee Management System
 * Shows how to manage fees and see real-time updates in Statement of Accounts
 */
public class FeeManagementDemo extends JFrame {
    
    private FeeManagementPanel feeManagementPanel;
    private StatementOfAccountsPanel statementPanel;
    private JTabbedPane tabbedPane;
    
    public FeeManagementDemo() {
        initializeFrame();
        setupDemo();
    }
    
    private void initializeFrame() {
        setTitle("iSLU Fee Management System Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }
    
    private void setupDemo() {
        // Create header
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Fee Management tab
        feeManagementPanel = new FeeManagementPanel();
        tabbedPane.addTab("💰 Fee Management", feeManagementPanel);
        
        // Statement of Accounts tab
        statementPanel = new StatementOfAccountsPanel("2255146");
        tabbedPane.addTab("📊 Statement of Accounts", statementPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Add footer
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
        
        // Add demo instructions
        showDemoInstructions();
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(14, 40, 79));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        // Title
        JLabel titleLabel = new JLabel("🎓 iSLU Fee Management System");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Demo info
        JLabel demoLabel = new JLabel("Database-Driven Fee Management with Real-time Updates");
        demoLabel.setForeground(Color.WHITE);
        demoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        headerPanel.add(demoLabel, BorderLayout.EAST);
        
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
    
    private void showDemoInstructions() {
        String instructions = """
            🎯 Fee Management System Demo Instructions:
            
            1. FEE MANAGEMENT TAB:
               • View all global fees (applied to all students)
               • Add new global fees using "Add Global Fee" button
               • Edit existing fees by selecting and clicking "Edit Fee"
               • Delete fees using "Delete Fee" button
               • Add student-specific fees using "Add Student Fee"
            
            2. STATEMENT OF ACCOUNTS TAB:
               • View how fees appear in student's account
               • See real-time updates when fees are modified
               • Test payment processing
               • Observe automatic balance calculations
            
            3. REAL-TIME UPDATES:
               • Make changes in Fee Management tab
               • Switch to Statement of Accounts tab
               • See immediate updates without refresh
            
            4. DATABASE PERSISTENCE:
               • All changes are saved to database files
               • Fees persist between application restarts
               • Global fees: feesDatabase.txt
               • Student fees: studentFeesDatabase.txt
            
            💡 Try adding a new fee and see it appear in the statement!
            """;
        
        JOptionPane.showMessageDialog(this, instructions, "Demo Instructions", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void main(String[] args) {
        // Initialize data managers
        SharedDataManager.initialize();
        
        SwingUtilities.invokeLater(() -> {
            try {
                new FeeManagementDemo().setVisible(true);
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