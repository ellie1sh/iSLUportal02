import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Fee Management Panel for administrators to manage fees
 * Allows editing of global fees and student-specific fees
 */
public class FeeManagementPanel extends JPanel implements FeeDatabase.FeeUpdateListener {
    
    private static final Color HEADER_BLUE = new Color(14, 40, 79);
    private static final Color WHITE = Color.WHITE;
    private static final Color LIGHT_GRAY = new Color(240, 240, 240);
    
    private JTable globalFeesTable;
    private JTable studentFeesTable;
    private DefaultTableModel globalFeesModel;
    private DefaultTableModel studentFeesModel;
    private JComboBox<String> studentCombo;
    private JTextField studentIDField;
    private JButton addGlobalFeeButton;
    private JButton addStudentFeeButton;
    private JButton editFeeButton;
    private JButton deleteFeeButton;
    private JButton refreshButton;
    
    public FeeManagementPanel() {
        // Register as fee update listener
        FeeDatabase.addFeeUpdateListener(this);
        
        initializeComponents();
        setupLayout();
        loadData();
    }
    
    private void initializeComponents() {
        // Global fees table
        String[] globalColumns = {"Code", "Description", "Amount", "Type", "Date Posted"};
        globalFeesModel = new DefaultTableModel(globalColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make read-only, editing through dialog
            }
        };
        globalFeesTable = new JTable(globalFeesModel);
        globalFeesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        globalFeesTable.setRowHeight(25);
        
        // Student fees table
        String[] studentColumns = {"Code", "Description", "Amount", "Type", "Date Posted"};
        studentFeesModel = new DefaultTableModel(studentColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make read-only, editing through dialog
            }
        };
        studentFeesTable = new JTable(studentFeesModel);
        studentFeesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentFeesTable.setRowHeight(25);
        
        // Student selection
        studentCombo = new JComboBox<>();
        studentCombo.setPreferredSize(new Dimension(200, 30));
        
        studentIDField = new JTextField(15);
        studentIDField.setPreferredSize(new Dimension(150, 30));
        
        // Buttons
        addGlobalFeeButton = createButton("Add Global Fee", Color.GREEN);
        addStudentFeeButton = createButton("Add Student Fee", Color.BLUE);
        editFeeButton = createButton("Edit Fee", Color.ORANGE);
        deleteFeeButton = createButton("Delete Fee", Color.RED);
        refreshButton = createButton("Refresh", Color.GRAY);
        
        setupEventListeners();
    }
    
    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        return button;
    }
    
    private void setupEventListeners() {
        addGlobalFeeButton.addActionListener(e -> showAddFeeDialog(true));
        addStudentFeeButton.addActionListener(e -> showAddFeeDialog(false));
        editFeeButton.addActionListener(e -> showEditFeeDialog());
        deleteFeeButton.addActionListener(e -> deleteSelectedFee());
        refreshButton.addActionListener(e -> loadData());
        
        studentCombo.addActionListener(e -> loadStudentFees());
        studentIDField.addActionListener(e -> loadStudentFees());
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        setBackground(LIGHT_GRAY);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(LIGHT_GRAY);
        
        // Global fees section
        JPanel globalSection = createGlobalFeesSection();
        mainPanel.add(globalSection, BorderLayout.NORTH);
        
        // Student fees section
        JPanel studentSection = createStudentFeesSection();
        mainPanel.add(studentSection, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(HEADER_BLUE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("💰 Fee Management System");
        titleLabel.setForeground(WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JLabel subtitleLabel = new JLabel("Manage global and student-specific fees");
        subtitleLabel.setForeground(WHITE);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        headerPanel.add(subtitleLabel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createGlobalFeesSection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setBackground(WHITE);
        section.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Section header
        JPanel sectionHeader = new JPanel(new BorderLayout());
        sectionHeader.setBackground(WHITE);
        
        JLabel sectionTitle = new JLabel("🌍 Global Fees (Applied to All Students)");
        sectionTitle.setFont(new Font("Arial", Font.BOLD, 14));
        sectionTitle.setForeground(HEADER_BLUE);
        sectionHeader.add(sectionTitle, BorderLayout.WEST);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(WHITE);
        buttonPanel.add(addGlobalFeeButton);
        buttonPanel.add(editFeeButton);
        buttonPanel.add(deleteFeeButton);
        buttonPanel.add(refreshButton);
        sectionHeader.add(buttonPanel, BorderLayout.EAST);
        
        section.add(sectionHeader, BorderLayout.NORTH);
        
        // Table
        JScrollPane scrollPane = new JScrollPane(globalFeesTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        section.add(scrollPane, BorderLayout.CENTER);
        
        return section;
    }
    
    private JPanel createStudentFeesSection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setBackground(WHITE);
        section.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Section header
        JPanel sectionHeader = new JPanel(new BorderLayout());
        sectionHeader.setBackground(WHITE);
        
        JLabel sectionTitle = new JLabel("👤 Student-Specific Fees");
        sectionTitle.setFont(new Font("Arial", Font.BOLD, 14));
        sectionTitle.setForeground(HEADER_BLUE);
        sectionHeader.add(sectionTitle, BorderLayout.WEST);
        
        // Student selection
        JPanel studentSelectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        studentSelectionPanel.setBackground(WHITE);
        
        JLabel studentLabel = new JLabel("Student ID:");
        studentLabel.setFont(new Font("Arial", Font.BOLD, 12));
        studentSelectionPanel.add(studentLabel);
        studentSelectionPanel.add(studentIDField);
        studentSelectionPanel.add(studentCombo);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(WHITE);
        buttonPanel.add(addStudentFeeButton);
        sectionHeader.add(studentSelectionPanel, BorderLayout.CENTER);
        sectionHeader.add(buttonPanel, BorderLayout.EAST);
        
        section.add(sectionHeader, BorderLayout.NORTH);
        
        // Table
        JScrollPane scrollPane = new JScrollPane(studentFeesTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        section.add(scrollPane, BorderLayout.CENTER);
        
        return section;
    }
    
    private void loadData() {
        loadGlobalFees();
        loadStudentList();
    }
    
    private void loadGlobalFees() {
        globalFeesModel.setRowCount(0);
        
        for (FeeBreakdown fee : FeeDatabase.getGlobalFees()) {
            Object[] row = {
                fee.getCode(),
                fee.getDescription(),
                String.format("P %,.2f", fee.getAmount()),
                fee.getType().getDisplayName(),
                fee.getDatePosted().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
            };
            globalFeesModel.addRow(row);
        }
    }
    
    private void loadStudentList() {
        studentCombo.removeAllItems();
        studentCombo.addItem("Select Student...");
        
        // Load students from StudentDataManager
        try {
            // This would normally load from StudentDataManager
            // For now, add some sample students
            studentCombo.addItem("2255146 - RIVERA, SHERLIE OBILLE");
            studentCombo.addItem("2250605 - BIMMUYAG, ASHEL JOHN D");
            studentCombo.addItem("2251234 - SAMPLE, STUDENT NAME");
        } catch (Exception e) {
            System.err.println("Error loading student list: " + e.getMessage());
        }
    }
    
    private void loadStudentFees() {
        studentFeesModel.setRowCount(0);
        
        String selectedStudent = (String) studentCombo.getSelectedItem();
        if (selectedStudent != null && !selectedStudent.equals("Select Student...")) {
            String studentID = selectedStudent.split(" - ")[0];
            studentIDField.setText(studentID);
            
            for (FeeBreakdown fee : FeeDatabase.getStudentSpecificFees(studentID)) {
                Object[] row = {
                    fee.getCode(),
                    fee.getDescription(),
                    String.format("P %,.2f", fee.getAmount()),
                    fee.getType().getDisplayName(),
                    fee.getDatePosted().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
                };
                studentFeesModel.addRow(row);
            }
        }
    }
    
    private void showAddFeeDialog(boolean isGlobal) {
        FeeEditDialog dialog = new FeeEditDialog(
            (Frame) SwingUtilities.getWindowAncestor(this),
            "Add Fee",
            null,
            isGlobal ? null : studentIDField.getText()
        );
        dialog.setVisible(true);
    }
    
    private void showEditFeeDialog() {
        JTable selectedTable = globalFeesTable.getSelectedRow() >= 0 ? globalFeesTable : studentFeesTable;
        int selectedRow = selectedTable.getSelectedRow();
        
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a fee to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String feeCode = (String) selectedTable.getValueAt(selectedRow, 0);
        boolean isGlobal = selectedTable == globalFeesTable;
        String studentID = isGlobal ? null : studentIDField.getText();
        
        FeeBreakdown fee = isGlobal ? 
            FeeDatabase.getFeeByCode(feeCode) : 
            FeeDatabase.getStudentFeeByCode(studentID, feeCode);
        
        if (fee != null) {
            FeeEditDialog dialog = new FeeEditDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                "Edit Fee",
                fee,
                studentID
            );
            dialog.setVisible(true);
        }
    }
    
    private void deleteSelectedFee() {
        JTable selectedTable = globalFeesTable.getSelectedRow() >= 0 ? globalFeesTable : studentFeesTable;
        int selectedRow = selectedTable.getSelectedRow();
        
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a fee to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String feeCode = (String) selectedTable.getValueAt(selectedRow, 0);
        boolean isGlobal = selectedTable == globalFeesTable;
        String studentID = isGlobal ? null : studentIDField.getText();
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this fee?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (isGlobal) {
                FeeDatabase.removeGlobalFee(feeCode);
            } else {
                FeeDatabase.removeStudentFee(studentID, feeCode);
            }
        }
    }
    
    @Override
    public void onFeesUpdated(String updatedStudentID) {
        SwingUtilities.invokeLater(() -> {
            loadData();
            if (updatedStudentID != null && !updatedStudentID.equals("ALL")) {
                loadStudentFees();
            }
        });
    }
    
    public void cleanup() {
        FeeDatabase.removeFeeUpdateListener(this);
    }
}