/**
 * AnnouncementUIDemo - Demonstrates the new announcement management UI
 * This class shows the announcement management interface that matches the provided image
 * 
 * AUTHOR: Group Project Implementation
 * SUBJECT: DATA STRUCTURE IT212 9458
 */
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AnnouncementUIDemo extends JFrame {
    
    public AnnouncementUIDemo() {
        setTitle("Announcement Management UI Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // Create the main panel
        JPanel mainPanel = createAnnouncementManagementPanel();
        add(mainPanel);
    }
    
    private JPanel createAnnouncementManagementPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240)); // Light gray background
        
        // Dark blue header with megaphone icon
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(13, 37, 73)); // Dark blue
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel headerLabel = new JLabel("📢 Manage Announcements");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel, BorderLayout.WEST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Content area
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(240, 240, 240));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Post New Announcement button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(new Color(240, 240, 240));
        
        JButton postButton = new JButton("➕ Post New Announcement");
        postButton.setFont(new Font("Arial", Font.PLAIN, 14));
        postButton.setForeground(new Color(50, 50, 50));
        postButton.setBackground(Color.WHITE);
        postButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        postButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        postButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPostAnnouncementDialog();
            }
        });
        
        buttonPanel.add(postButton);
        contentPanel.add(buttonPanel, BorderLayout.NORTH);
        
        // Yellow alert bar for no announcements
        JPanel alertPanel = new JPanel(new BorderLayout());
        alertPanel.setBackground(new Color(255, 255, 0)); // Yellow background
        alertPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel alertLabel = new JLabel("⚠️ There are no announcement/s to display!");
        alertLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        alertLabel.setForeground(new Color(50, 50, 50));
        alertPanel.add(alertLabel, BorderLayout.WEST);
        
        contentPanel.add(alertPanel, BorderLayout.CENTER);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        return mainPanel;
    }
    
    private void showPostAnnouncementDialog() {
        JDialog dialog = new JDialog(this, "Post New Announcement", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(13, 37, 73));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel headerLabel = new JLabel("📢 Post New Announcement");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel, BorderLayout.WEST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Form content
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Title field
        JLabel titleLabel = new JLabel("Title:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(titleLabel);
        formPanel.add(Box.createVerticalStrut(5));

        JTextField titleField = new JTextField();
        titleField.setFont(new Font("Arial", Font.PLAIN, 12));
        titleField.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleField.setMaximumSize(new Dimension(400, 30));
        formPanel.add(titleField);
        formPanel.add(Box.createVerticalStrut(15));

        // Content field
        JLabel contentLabel = new JLabel("Content:");
        contentLabel.setFont(new Font("Arial", Font.BOLD, 12));
        contentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(contentLabel);
        formPanel.add(Box.createVerticalStrut(5));

        JTextArea contentArea = new JTextArea(6, 30);
        contentArea.setFont(new Font("Arial", Font.PLAIN, 12));
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        JScrollPane contentScroll = new JScrollPane(contentArea);
        contentScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentScroll.setMaximumSize(new Dimension(400, 120));
        formPanel.add(contentScroll);
        formPanel.add(Box.createVerticalStrut(15));

        // Priority selection
        JLabel priorityLabel = new JLabel("Priority:");
        priorityLabel.setFont(new Font("Arial", Font.BOLD, 12));
        priorityLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(priorityLabel);
        formPanel.add(Box.createVerticalStrut(5));

        JComboBox<String> priorityCombo = new JComboBox<>(new String[]{"High", "Medium", "Low"});
        priorityCombo.setFont(new Font("Arial", Font.PLAIN, 12));
        priorityCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        priorityCombo.setMaximumSize(new Dimension(400, 30));
        priorityCombo.setSelectedItem("Medium");
        formPanel.add(priorityCombo);
        formPanel.add(Box.createVerticalStrut(15));

        // Target audience
        JLabel audienceLabel = new JLabel("Target Audience:");
        audienceLabel.setFont(new Font("Arial", Font.BOLD, 12));
        audienceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(audienceLabel);
        formPanel.add(Box.createVerticalStrut(5));

        JComboBox<String> audienceCombo = new JComboBox<>(new String[]{"Students", "Teachers", "All"});
        audienceCombo.setFont(new Font("Arial", Font.PLAIN, 12));
        audienceCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        audienceCombo.setMaximumSize(new Dimension(400, 30));
        audienceCombo.setSelectedItem("Students");
        formPanel.add(audienceCombo);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 12));
        cancelButton.setBackground(new Color(108, 117, 125));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        cancelButton.addActionListener(e -> dialog.dispose());

        JButton postButton = new JButton("Post Announcement");
        postButton.setFont(new Font("Arial", Font.BOLD, 12));
        postButton.setBackground(new Color(40, 167, 69));
        postButton.setForeground(Color.WHITE);
        postButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        postButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String content = contentArea.getText().trim();
            String priority = (String) priorityCombo.getSelectedItem();
            String audience = (String) audienceCombo.getSelectedItem();

            if (title.isEmpty() || content.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(dialog, 
                "Announcement posted successfully!\n\n" +
                "Title: " + title + "\n" +
                "Priority: " + priority + "\n" +
                "Audience: " + audience, 
                "Success", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });

        buttonPanel.add(cancelButton);
        buttonPanel.add(postButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AnnouncementUIDemo().setVisible(true);
        });
    }
}
