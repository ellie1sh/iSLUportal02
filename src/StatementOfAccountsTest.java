import javax.swing.*;
import java.awt.*;

/**
 * Test class for StatementOfAccountsPanel
 * Demonstrates the new Statement of Accounts functionality
 */
public class StatementOfAccountsTest extends JFrame {
    
    public StatementOfAccountsTest() {
        setTitle("Statement of Accounts Test - iSLU Student Portal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        // Create test student data
        String testStudentID = "2255146";
        String testStudentName = "Sherlie O. Rivera";
        String testStudentProgram = "BSIT 2";
        
        // Create the Statement of Accounts panel
        StatementOfAccountsPanel soaPanel = new StatementOfAccountsPanel(
            testStudentID, testStudentName, testStudentProgram);
        
        // Add to frame
        add(soaPanel, BorderLayout.CENTER);
        
        // Add a refresh button for testing
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton refreshButton = new JButton("Refresh Account Statement");
        refreshButton.addActionListener(e -> soaPanel.refreshAccountStatement());
        buttonPanel.add(refreshButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    public static void main(String[] args) {
        // Set look and feel
        try {
            // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new StatementOfAccountsTest().setVisible(true);
        });
    }
}