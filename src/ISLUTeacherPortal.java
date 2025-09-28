import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Timer;

public class ISLUTeacherPortal extends JFrame {
    private JPanel mainPanel;
    private JPanel contentPanel;
    private JPanel sidebarPanel;
    private JPanel footbarPanel;
    private JLabel userNameLabel;
    private long lastDatabaseModified = 0; // Track database file modification time
    private Timer databaseCheckTimer; // Timer for checking database changes
    private JLabel semesterLabel;
    private JTextArea statusArea;
    private JPanel mainCardHolder;
    private CardLayout mainCardLayout;
    private MyDoublyLinkedList<MenuItem> menu;

    // Teacher data
    private String teacherID;
    private String teacherName;
    private String subjectCode;
    private String subjectName;
    private String semester = "FIRST SEMESTER, 2025-2026";
    private String status = "CURRENTLY TEACHING THIS FIRST SEMESTER, 2025-2026";
    
    // Grades table references for filtering
    private DefaultTableModel gradesTableModel;
    private JTable gradesTable;

    public ISLUTeacherPortal(String teacherID) {
        this.teacherID = teacherID;
        
        // Initialize teacher data manager
        TeacherDataManager.initialize();
        
        // Get teacher information
        TeacherInfo teacherInfo = TeacherDataManager.getTeacherInfo(teacherID);
        if (teacherInfo != null) {
            this.teacherName = teacherInfo.getTeacherName();
            this.subjectCode = teacherInfo.getSubjectCode();
            this.subjectName = teacherInfo.getSubjectName();
        } else {
            this.teacherName = "Unknown Teacher";
            this.subjectCode = "";
            this.subjectName = "";
        }

        // Initialize shared data manager
        SharedDataManager.initialize();
        
        // Initialize student data manager
        StudentDataManager.initialize();

        initializeComponents();
        buildCards();
        setupLayout();
        loadAnnouncements();

        // Start database monitoring
        startDatabaseMonitoring();

        // Add window listener to stop monitoring when window closes
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                stopDatabaseMonitoring();
                System.exit(0);
            }
        });
        loadTeacherStatus();
    }

    private void initializeComponents() {
        setTitle("iSLU Teacher Portal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());

        JPanel headerPanel = createHeader();
        sidebarPanel = createSidebar();
        footbarPanel = createFooter();

        // content container
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.WHITE);

        // card holder
        mainCardLayout = new CardLayout();
        mainCardHolder = new JPanel(mainCardLayout);

        contentPanel.add(mainCardHolder, BorderLayout.CENTER);
        mainContentPanel.add(contentPanel, BorderLayout.CENTER);
        mainContentPanel.add(footbarPanel, BorderLayout.SOUTH);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(sidebarPanel, BorderLayout.WEST);
        mainPanel.add(mainContentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(13, 37, 73));
        headerPanel.setPreferredSize(new Dimension(0, 55));
        headerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        JSeparator headerSeparator = new JSeparator();
        headerSeparator.setForeground(new Color(70, 130, 180));
        headerSeparator.setBackground(new Color(70, 130, 180));
        headerSeparator.setPreferredSize(new Dimension(0, 2));

        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoPanel.setBackground(new Color(52, 73, 94));
        JLabel logoLabel = new JLabel("iSLU (Teacher)");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(Color.WHITE);
        logoPanel.add(logoLabel);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setBackground(new Color(31, 47, 57));

        userNameLabel = new JLabel(teacherName);
        userNameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        userNameLabel.setForeground(Color.WHITE);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBorderPainted(false);
        logoutButton.addActionListener(e -> {
            dispose();
            new Login().setVisible(true);
        });

        userPanel.add(userNameLabel);
        userPanel.add(Box.createHorizontalStrut(20));
        userPanel.add(logoutButton);

        headerPanel.add(logoPanel, BorderLayout.WEST);
        headerPanel.add(userPanel, BorderLayout.EAST);

        JPanel headerWithSeparator = new JPanel(new BorderLayout());
        headerWithSeparator.add(headerSeparator, BorderLayout.SOUTH);
        headerWithSeparator.add(headerPanel, BorderLayout.CENTER);

        return headerWithSeparator;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(13, 37, 73));
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setBorder(new EmptyBorder(20, 0, 20, 0));

        semesterLabel = new JLabel(semester);
        semesterLabel.setForeground(Color.WHITE);
        semesterLabel.setFont(new Font("Arial", Font.BOLD, 12));
        semesterLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        semesterLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        sidebar.add(semesterLabel);

        JPanel buttonList = new JPanel();
        buttonList.setLayout(new BoxLayout(buttonList, BoxLayout.Y_AXIS));
        buttonList.setBackground(new Color(13, 37, 73));
        buttonList.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Home
        buttonList.add(buildSidebarButton("🏠 Home", () -> mainCardLayout.show(mainCardHolder, "HOME")));

        // Class Checklist
        buttonList.add(buildSidebarButton("✅ Class Checklist", () -> mainCardLayout.show(mainCardHolder, "CHECKLIST")));

        // Attendance
        buttonList.add(buildSidebarButton("📋 Attendance", () -> mainCardLayout.show(mainCardHolder, "ATTENDANCE")));

        // Encode Grades
        buttonList.add(buildSidebarButton("📝 Encode Grades", () -> mainCardLayout.show(mainCardHolder, "ENCODE_GRADES")));

        // Completion
        buttonList.add(buildSidebarButton("✅ Completion", () -> mainCardLayout.show(mainCardHolder, "COMPLETION")));

        // Current Grades
        buttonList.add(buildSidebarButton("📊 Current Grades", () -> mainCardLayout.show(mainCardHolder, "CURRENT_GRADES")));

        // Announcements
        buttonList.add(buildSidebarButton("📢 Class Announcements", () -> mainCardLayout.show(mainCardHolder, "ANNOUNCEMENTS")));

        // Personal Details
        buttonList.add(buildSidebarButton("👤 Personal Details", () -> mainCardLayout.show(mainCardHolder, "DETAILS")));

        // Journal
        buttonList.add(buildSidebarButton("📚 Journal", () -> mainCardLayout.show(mainCardHolder, "JOURNAL")));

        // About
        buttonList.add(buildSidebarButton("ℹ️ About", () -> mainCardLayout.show(mainCardHolder, "ABOUT")));

        sidebar.add(buttonList, BorderLayout.WEST);
        return sidebar;
    }


    private JPanel buildSidebarButton(String text, Runnable onClick) {
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(new Color(13, 37, 73));
        buttonPanel.setOpaque(true);
        Border topBottomBorder = BorderFactory.createMatteBorder(1, 0, 1, 0, Color.black);
        Border padding = new EmptyBorder(10, 14, 10, 10);
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(topBottomBorder, padding));

        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.WHITE);
        lbl.setHorizontalAlignment(SwingConstants.LEFT);
        buttonPanel.add(lbl, BorderLayout.WEST);

        buttonPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        buttonPanel.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { onClick.run(); }
        });
        return buttonPanel;
    }

    // ===== Build main content cards =====
    private void buildCards() {
        // Home: announcements + notes
        JPanel home = new JPanel(new GridLayout(1, 2, 10, 10));
        home.setBackground(Color.WHITE);
        home.add(createHomeAnnouncementsPanel());
        home.add(createNotesPanel());
        loadTeacherNotes();
        mainCardHolder.add(home, "HOME");

        // Add all the new panels - these methods create and add panels internally
        createClassChecklistPanel();
        createAttendancePanel();
        createEncodeGradesPanel();
        createCompletionPanel();
        createCurrentGradesPanel();
        
        // Other panels - these methods create and add panels internally
        mainCardHolder.add(createAnnouncementsPanel(), "ANNOUNCEMENTS");
        createPersonalDetailsPanel();
        createJournalPanel();
        createAboutPanel();
    }

    private void createHomePanel() {
        JPanel homePanel = new JPanel(new BorderLayout());
        homePanel.setBackground(Color.WHITE);
        homePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("🏠 Teacher Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        homePanel.add(headerPanel, BorderLayout.NORTH);

        // Content area with two columns
        JPanel contentArea = new JPanel(new GridLayout(1, 2, 20, 20));
        contentArea.setBackground(Color.WHITE);

        // Left column - Quick Stats
        JPanel statsPanel = createStatsPanel();
        contentArea.add(statsPanel);

        // Right column - Recent Activities
        JPanel activitiesPanel = createActivitiesPanel();
        contentArea.add(activitiesPanel);

        homePanel.add(contentArea, BorderLayout.CENTER);

        mainCardHolder.add(homePanel, "HOME");
    }

    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "📊 Quick Statistics",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14),
            new Color(52, 73, 94)
        ));

        // Add stat items
        statsPanel.add(createStatItem("Total Classes", "8"));
        statsPanel.add(createStatItem("Total Students", "240"));
        statsPanel.add(createStatItem("Pending Grades", "3"));
        statsPanel.add(createStatItem("Attendance Rate", "95.2%"));

        return statsPanel;
    }

    private JPanel createStatItem(String label, String value) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel labelLabel = new JLabel(label);
        labelLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        labelLabel.setForeground(new Color(100, 100, 100));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 16));
        valueLabel.setForeground(new Color(52, 73, 94));

        itemPanel.add(labelLabel, BorderLayout.WEST);
        itemPanel.add(valueLabel, BorderLayout.EAST);

        return itemPanel;
    }

    private JPanel createActivitiesPanel() {
        JPanel activitiesPanel = new JPanel();
        activitiesPanel.setLayout(new BoxLayout(activitiesPanel, BoxLayout.Y_AXIS));
        activitiesPanel.setBackground(Color.WHITE);
        activitiesPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "📋 Recent Activities",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14),
            new Color(52, 73, 94)
        ));

        // Add activity items
        activitiesPanel.add(createActivityItem("📝 Graded IT 111 assignments", "2 hours ago"));
        activitiesPanel.add(createActivityItem("📋 Updated attendance for CS 101", "4 hours ago"));
        activitiesPanel.add(createActivityItem("📢 Posted announcement", "1 day ago"));
        activitiesPanel.add(createActivityItem("✅ Completed grade encoding", "2 days ago"));

        return activitiesPanel;
    }

    private JPanel createActivityItem(String activity, String time) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel activityLabel = new JLabel(activity);
        activityLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        activityLabel.setForeground(new Color(50, 50, 50));

        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        timeLabel.setForeground(new Color(150, 150, 150));

        itemPanel.add(activityLabel, BorderLayout.WEST);
        itemPanel.add(timeLabel, BorderLayout.EAST);

        return itemPanel;
    }

    private void createClassChecklistPanel() {
        JPanel checklistPanel = new JPanel(new BorderLayout());
        checklistPanel.setBackground(Color.WHITE);
        checklistPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("🔗 Class Checklist");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        checklistPanel.add(headerPanel, BorderLayout.NORTH);

        // Content - Table matching the Current Load design
        String[] columnNames = {"Class Code", "Course Number", "Course Description", "Paid Units", "Schedule", "Days", "Room", "# of Students", "Students"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Read-only like the Current Load table
            }
        };

        // Load class checklist data with subject information
        loadClassChecklistData(model);

        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(240, 240, 240));

        // Set column widths to match the design
        table.getColumnModel().getColumn(0).setPreferredWidth(80);  // Class Code
        table.getColumnModel().getColumn(1).setPreferredWidth(100); // Course Number
        table.getColumnModel().getColumn(2).setPreferredWidth(300); // Course Description
        table.getColumnModel().getColumn(3).setPreferredWidth(80);  // Paid Units
        table.getColumnModel().getColumn(4).setPreferredWidth(120); // Schedule
        table.getColumnModel().getColumn(5).setPreferredWidth(60);  // Days
        table.getColumnModel().getColumn(6).setPreferredWidth(80);  // Room
        table.getColumnModel().getColumn(7).setPreferredWidth(80);  // # of Students
        table.getColumnModel().getColumn(8).setPreferredWidth(60);  // Students

        // Add click listener for the Students column
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                
                if (row >= 0 && col == 8) { // Students column (index 8)
                    showStudentsDialog();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Add total paid units at the bottom
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        totalPanel.setBackground(Color.WHITE);
        JLabel totalLabel = new JLabel("Total Paid Units: " + getTotalPaidUnits());
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalLabel.setForeground(new Color(52, 73, 94));
        totalPanel.add(totalLabel);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(totalPanel, BorderLayout.SOUTH);

        checklistPanel.add(contentPanel, BorderLayout.CENTER);

        mainCardHolder.add(checklistPanel, "CHECKLIST");
    }

    private JPanel createChecklistItem(String className, String studentCount, boolean completed) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JCheckBox checkBox = new JCheckBox(className);
        checkBox.setSelected(completed);
        checkBox.setFont(new Font("Arial", Font.PLAIN, 12));
        checkBox.setBackground(Color.WHITE);

        JLabel countLabel = new JLabel(studentCount);
        countLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        countLabel.setForeground(new Color(100, 100, 100));

        itemPanel.add(checkBox, BorderLayout.WEST);
        itemPanel.add(countLabel, BorderLayout.EAST);

        return itemPanel;
    }

    private void createAttendancePanel() {
        JPanel attendancePanel = new JPanel(new BorderLayout());
        attendancePanel.setBackground(Color.WHITE);
        attendancePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("📋 Attendance");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        attendancePanel.add(headerPanel, BorderLayout.NORTH);

        // Content - Classes table matching student portal format
        String[] columnNames = {"Class Code", "Course", "Schedule", "Days", "Room", "Check", "Export"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Table is read-only, buttons handle actions
            }
        };

        // Load class data
        loadAttendanceClassData(model);

        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(240, 240, 240));

        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(80);  // Class Code
        table.getColumnModel().getColumn(1).setPreferredWidth(300); // Course
        table.getColumnModel().getColumn(2).setPreferredWidth(120); // Schedule
        table.getColumnModel().getColumn(3).setPreferredWidth(60);  // Days
        table.getColumnModel().getColumn(4).setPreferredWidth(80);  // Room
        table.getColumnModel().getColumn(5).setPreferredWidth(60);  // Check
        table.getColumnModel().getColumn(6).setPreferredWidth(60);  // Export

        // Add mouse listener for button clicks
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                
                if (row >= 0 && col >= 0) {
                    if (col == 5) { // Check column
                        String classCode = (String) model.getValueAt(row, 0);
                        String course = (String) model.getValueAt(row, 1);
                        showAttendanceDialog(classCode, course);
                    } else if (col == 6) { // Export column
                        String classCode = (String) model.getValueAt(row, 0);
                        String course = (String) model.getValueAt(row, 1);
                        exportAttendance(classCode, course);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        attendancePanel.add(scrollPane, BorderLayout.CENTER);

        mainCardHolder.add(attendancePanel, "ATTENDANCE");
    }

    private void createEncodeGradesPanel() {
        JPanel gradesPanel = new JPanel(new BorderLayout());
        gradesPanel.setBackground(Color.WHITE);
        gradesPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("📝 Encode Grades");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        gradesPanel.add(headerPanel, BorderLayout.NORTH);

        // Filter Panel
        JPanel filterPanel = createGradesFilterPanel();
        gradesPanel.add(filterPanel, BorderLayout.NORTH);

        // Content - Grades table matching student portal format
        String[] columnNames = {"Student ID", "Name", "Prelim", "Midterm", "Tentative Final", "Average"};
        gradesTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 2 && column <= 4; // Only grade columns are editable
            }
        };

        // Load real data from SharedDataManager
        loadGradesData(gradesTableModel);

        gradesTable = new JTable(gradesTableModel);
        gradesTable.setFont(new Font("Arial", Font.PLAIN, 12));
        gradesTable.setRowHeight(25);
        gradesTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        gradesTable.getTableHeader().setBackground(new Color(240, 240, 240));

        // Set column widths to match student portal
        gradesTable.getColumnModel().getColumn(0).setPreferredWidth(100); // Student ID
        gradesTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Name
        gradesTable.getColumnModel().getColumn(2).setPreferredWidth(80);  // Prelim
        gradesTable.getColumnModel().getColumn(3).setPreferredWidth(80);  // Midterm
        gradesTable.getColumnModel().getColumn(4).setPreferredWidth(120); // Tentative Final
        gradesTable.getColumnModel().getColumn(5).setPreferredWidth(80);  // Average

        // Add table change listener to save changes
        gradesTable.getModel().addTableModelListener(e -> {
            if (e.getType() == javax.swing.event.TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int col = e.getColumn();
                if (col >= 2 && col <= 4) { // Grade columns
                    String studentID = (String) gradesTableModel.getValueAt(row, 0);
                    
                    // Handle grade values - convert to double, handle "Not Yet Submitted"
                    double prelim = parseGradeValue(gradesTableModel.getValueAt(row, 2));
                    double midterm = parseGradeValue(gradesTableModel.getValueAt(row, 3));
                    double finalGrade = parseGradeValue(gradesTableModel.getValueAt(row, 4));
                    
                    // Calculate average only if all grades are present
                    if (prelim > 0 && midterm > 0 && finalGrade > 0) {
                        double average = (prelim + midterm + finalGrade) / 3.0;
                        gradesTableModel.setValueAt(Math.round(average * 10.0) / 10.0, row, 5);
                    } else {
                        gradesTableModel.setValueAt("", row, 5); // Clear average if not all grades present
                    }
                    
                    // Save to shared data manager
                    SharedDataManager.saveGradeRecord(studentID, subjectCode, subjectName, prelim, midterm, finalGrade);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(gradesTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        gradesPanel.add(scrollPane, BorderLayout.CENTER);

        mainCardHolder.add(gradesPanel, "ENCODE_GRADES");
    }

    private JPanel createGradesFilterPanel() {
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Class Code Dropdown
        JLabel classCodeLabel = new JLabel("Class Code:");
        classCodeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        filterPanel.add(classCodeLabel);

        JComboBox<String> classCodeCombo = new JComboBox<>();
        classCodeCombo.setPreferredSize(new Dimension(120, 25));
        classCodeCombo.addItem("All Classes");
        classCodeCombo.addItem(subjectCode); // Add current teacher's subject
        filterPanel.add(classCodeCombo);

        // Add spacing
        filterPanel.add(Box.createHorizontalStrut(20));

        // Subject Dropdown
        JLabel subjectLabel = new JLabel("Subject:");
        subjectLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        filterPanel.add(subjectLabel);

        JComboBox<String> subjectCombo = new JComboBox<>();
        subjectCombo.setPreferredSize(new Dimension(150, 25));
        subjectCombo.addItem("Select subject...");
        subjectCombo.addItem(subjectName); // Add current teacher's subject
        filterPanel.add(subjectCombo);

        // Add spacing
        filterPanel.add(Box.createHorizontalStrut(20));

        // Student ID Search
        JLabel studentIDLabel = new JLabel("Student ID:");
        studentIDLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        filterPanel.add(studentIDLabel);

        JTextField studentIDField = new JTextField();
        studentIDField.setPreferredSize(new Dimension(120, 25));
        studentIDField.setToolTipText("Enter student ID to search");
        filterPanel.add(studentIDField);

        // Add search button
        JButton searchButton = new JButton("Search");
        searchButton.setPreferredSize(new Dimension(80, 25));
        searchButton.setFont(new Font("Arial", Font.PLAIN, 11));
        searchButton.setBackground(new Color(52, 73, 94));
        searchButton.setForeground(Color.WHITE);
        searchButton.setBorderPainted(false);

        // Add action listeners for search functionality
        searchButton.addActionListener(e -> {
            String selectedClassCode = (String) classCodeCombo.getSelectedItem();
            String selectedSubject = (String) subjectCombo.getSelectedItem();
            String studentID = studentIDField.getText().trim();
            
            // Filter and refresh the grades table
            filterGradesTable(selectedClassCode, selectedSubject, studentID);
        });

        // Add Enter key listener to student ID field
        studentIDField.addActionListener(e -> {
            String selectedClassCode = (String) classCodeCombo.getSelectedItem();
            String selectedSubject = (String) subjectCombo.getSelectedItem();
            String studentID = studentIDField.getText().trim();
            
            filterGradesTable(selectedClassCode, selectedSubject, studentID);
        });

        // Add change listeners to dropdowns for automatic filtering
        classCodeCombo.addActionListener(e -> {
            String selectedClassCode = (String) classCodeCombo.getSelectedItem();
            String selectedSubject = (String) subjectCombo.getSelectedItem();
            String studentID = studentIDField.getText().trim();
            
            filterGradesTable(selectedClassCode, selectedSubject, studentID);
        });

        subjectCombo.addActionListener(e -> {
            String selectedClassCode = (String) classCodeCombo.getSelectedItem();
            String selectedSubject = (String) subjectCombo.getSelectedItem();
            String studentID = studentIDField.getText().trim();
            
            filterGradesTable(selectedClassCode, selectedSubject, studentID);
        });

        filterPanel.add(searchButton);

        return filterPanel;
    }

    private void createCompletionPanel() {
        JPanel completionPanel = new JPanel(new BorderLayout());
        completionPanel.setBackground(Color.WHITE);
        completionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("✅ Completion Status");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        completionPanel.add(headerPanel, BorderLayout.NORTH);

        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);

        // Add completion items
        contentPanel.add(createCompletionItem("IT 111 - Introduction to Computing", "95%", "25/30 students completed"));
        contentPanel.add(createCompletionItem("IT 112 - Computer Programming 1", "100%", "30/30 students completed"));
        contentPanel.add(createCompletionItem("CS 101 - Data Structures", "88%", "22/25 students completed"));
        contentPanel.add(createCompletionItem("IT 113 - Discrete Mathematics", "100%", "30/30 students completed"));

        completionPanel.add(contentPanel, BorderLayout.CENTER);

        mainCardHolder.add(completionPanel, "COMPLETION");
    }

    private JPanel createCompletionItem(String className, String percentage, String details) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel classLabel = new JLabel(className);
        classLabel.setFont(new Font("Arial", Font.BOLD, 14));
        classLabel.setForeground(new Color(52, 73, 94));

        JLabel percentageLabel = new JLabel(percentage);
        percentageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        percentageLabel.setForeground(new Color(46, 204, 113));

        JLabel detailsLabel = new JLabel(details);
        detailsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        detailsLabel.setForeground(new Color(100, 100, 100));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.add(classLabel);
        leftPanel.add(detailsLabel);

        itemPanel.add(leftPanel, BorderLayout.WEST);
        itemPanel.add(percentageLabel, BorderLayout.EAST);

        return itemPanel;
    }

    private void createCurrentGradesPanel() {
        JPanel gradesPanel = new JPanel(new BorderLayout());
        gradesPanel.setBackground(Color.WHITE);
        gradesPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("📊 Current Grades Overview");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        gradesPanel.add(headerPanel, BorderLayout.NORTH);

        // Content - Grades summary table
        String[] columnNames = {"Class", "Students", "Average Grade", "Pass Rate", "Status"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Read-only
            }
        };

        // Add sample data
        model.addRow(new Object[]{"IT 111", "30", "87.5", "96.7%", "Good"});
        model.addRow(new Object[]{"IT 112", "30", "89.2", "100%", "Excellent"});
        model.addRow(new Object[]{"CS 101", "25", "82.1", "88%", "Fair"});
        model.addRow(new Object[]{"IT 113", "30", "91.8", "100%", "Excellent"});

        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(240, 240, 240));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        gradesPanel.add(scrollPane, BorderLayout.CENTER);

        mainCardHolder.add(gradesPanel, "CURRENT_GRADES");
    }

    private JPanel createHomeAnnouncementsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "📰 Department News & Announcements",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14)
        ));
        panel.setBackground(Color.WHITE);

        JTextArea announcementsArea = new JTextArea();
        announcementsArea.setEditable(false);
        announcementsArea.setFont(new Font("Arial", Font.PLAIN, 12));
        announcementsArea.setLineWrap(true);
        announcementsArea.setWrapStyleWord(true);
        announcementsArea.setBackground(Color.WHITE);

        StringBuilder announcements = new StringBuilder();
        announcements.append("[FACULTY MEETING] Department Meeting - September 30, 2025\n\n");
        announcements.append("Agenda: Midterm preparations, grading timelines, and research updates.\n\n");
        announcements.append("─────────────────────────────────────────────────────\n\n");
        announcements.append("Call for proposals for Teaching Innovation Grants AY 2025-2026.\n");
        announcementsArea.setText(announcements.toString());
        announcementsArea.setCaretPosition(0);

        JScrollPane scrollPane = new JScrollPane(announcementsArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(0, 400));

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createAnnouncementsPanel() {
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
        postButton.addActionListener(e -> showPostAnnouncementDialog());
        
        buttonPanel.add(postButton);
        contentPanel.add(buttonPanel, BorderLayout.NORTH);
        
        // Announcements display area
        JPanel announcementsDisplayPanel = new JPanel(new BorderLayout());
        announcementsDisplayPanel.setBackground(new Color(240, 240, 240));
        
        // Check if there are announcements - show only announcements for Teachers and All
        MySinglyLinkedList<Announcement> teacherAnnouncements = SharedDataManager.getTeacherAnnouncements();
        
        if (teacherAnnouncements.size() == 0) {
            // Show yellow alert bar for no announcements
            JPanel alertPanel = new JPanel(new BorderLayout());
            alertPanel.setBackground(new Color(255, 255, 0)); // Yellow background
            alertPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            
            JLabel alertLabel = new JLabel("⚠️ There are no announcement/s to display!");
            alertLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            alertLabel.setForeground(new Color(50, 50, 50));
            alertPanel.add(alertLabel, BorderLayout.WEST);
            
            announcementsDisplayPanel.add(alertPanel, BorderLayout.CENTER);
        } else {
            // Display existing announcements
            JPanel announcementsListPanel = new JPanel();
            announcementsListPanel.setLayout(new BoxLayout(announcementsListPanel, BoxLayout.Y_AXIS));
            announcementsListPanel.setBackground(Color.WHITE);
            announcementsListPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
            
            for (int i = 0; i < teacherAnnouncements.size(); i++) {
                Announcement announcement = teacherAnnouncements.get(i);
                JPanel announcementItem = createAnnouncementItem(
                    announcement.getTitle(),
                    announcement.getContent(),
                    announcement.getDateCreated(),
                    announcement.getAuthor(),
                    announcement.getPriority()
                );
                announcementsListPanel.add(announcementItem);
                if (i < teacherAnnouncements.size() - 1) {
                    announcementsListPanel.add(Box.createVerticalStrut(10));
                }
            }
            
            JScrollPane scrollPane = new JScrollPane(announcementsListPanel);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setPreferredSize(new Dimension(0, 400));
            
            announcementsDisplayPanel.add(scrollPane, BorderLayout.CENTER);
        }
        
        contentPanel.add(announcementsDisplayPanel, BorderLayout.CENTER);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        return mainPanel;
    }

    private JPanel createCleanAnnouncementItem(String title, String content, String date, String author, String priority) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Top row with priority and date
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        
        // Priority label with color
        JLabel priorityLabel = new JLabel("[" + priority + "]");
        priorityLabel.setFont(new Font("Arial", Font.BOLD, 12));
        if (priority.equals("High")) {
            priorityLabel.setForeground(new Color(220, 53, 69)); // Red
        } else if (priority.equals("Medium")) {
            priorityLabel.setForeground(new Color(255, 193, 7)); // Orange
        } else {
            priorityLabel.setForeground(new Color(40, 167, 69)); // Green
        }
        topPanel.add(priorityLabel, BorderLayout.WEST);
        
        // Date label
        JLabel dateLabel = new JLabel(date);
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        dateLabel.setForeground(new Color(150, 150, 150));
        topPanel.add(dateLabel, BorderLayout.EAST);
        
        itemPanel.add(topPanel, BorderLayout.NORTH);
        
        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(new Color(50, 50, 50));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        itemPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Content
        JLabel contentLabel = new JLabel("<html><div style='width: 500px;'>" + content + "</div></html>");
        contentLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        contentLabel.setForeground(new Color(50, 50, 50));
        contentLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        itemPanel.add(contentLabel, BorderLayout.SOUTH);
        
        // Author (small text at bottom)
        JLabel authorLabel = new JLabel("Posted by: " + author);
        authorLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        authorLabel.setForeground(new Color(100, 100, 100));
        authorLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(authorLabel, BorderLayout.WEST);
        itemPanel.add(bottomPanel, BorderLayout.SOUTH);

        return itemPanel;
    }

    private JPanel createAnnouncementItem(String title, String content, String date, String author, String priority) {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Priority indicator
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel priorityLabel = new JLabel("[" + priority + "]");
        priorityLabel.setFont(new Font("Arial", Font.BOLD, 12));
        if (priority.equals("High")) {
            priorityLabel.setForeground(new Color(220, 53, 69)); // Red
        } else if (priority.equals("Medium")) {
            priorityLabel.setForeground(new Color(255, 193, 7)); // Yellow
        } else {
            priorityLabel.setForeground(new Color(40, 167, 69)); // Green
        }
        headerPanel.add(priorityLabel, BorderLayout.WEST);
        
        JLabel dateLabel = new JLabel(date);
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        dateLabel.setForeground(new Color(150, 150, 150));
        headerPanel.add(dateLabel, BorderLayout.EAST);
        
        itemPanel.add(headerPanel);
        itemPanel.add(Box.createVerticalStrut(8));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(new Color(52, 73, 94));

        JLabel contentLabel = new JLabel("<html><div style='width: 500px;'>" + content + "</div></html>");
        contentLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        contentLabel.setForeground(new Color(50, 50, 50));

        JLabel authorLabel = new JLabel("Posted by: " + author);
        authorLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        authorLabel.setForeground(new Color(100, 100, 100));

        itemPanel.add(titleLabel);
        itemPanel.add(Box.createVerticalStrut(5));
        itemPanel.add(contentLabel);
        itemPanel.add(Box.createVerticalStrut(5));
        itemPanel.add(authorLabel);

        return itemPanel;
    }

    private void createPersonalDetailsPanel() {
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("👤 Personal Details");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        detailsPanel.add(headerPanel, BorderLayout.NORTH);

        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);

        // Add personal detail items
        contentPanel.add(createDetailItem("Teacher ID", teacherID));
        contentPanel.add(createDetailItem("Full Name", teacherName));
        contentPanel.add(createDetailItem("Department", "College of Information Technology"));
        contentPanel.add(createDetailItem("Position", "Assistant Professor"));
        contentPanel.add(createDetailItem("Email", teacherID.toLowerCase() + "@slu.edu.ph"));
        contentPanel.add(createDetailItem("Phone", "+63 917 123 4567"));
        contentPanel.add(createDetailItem("Office", "IT Building Room 205"));
        contentPanel.add(createDetailItem("Office Hours", "Monday-Friday, 8:00 AM - 5:00 PM"));

        detailsPanel.add(contentPanel, BorderLayout.CENTER);

        mainCardHolder.add(detailsPanel, "DETAILS");
    }

    private JPanel createDetailItem(String label, String value) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel labelLabel = new JLabel(label + ":");
        labelLabel.setFont(new Font("Arial", Font.BOLD, 12));
        labelLabel.setForeground(new Color(52, 73, 94));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        valueLabel.setForeground(new Color(50, 50, 50));

        itemPanel.add(labelLabel, BorderLayout.WEST);
        itemPanel.add(valueLabel, BorderLayout.EAST);

        return itemPanel;
    }

    private void createJournalPanel() {
        JPanel journalPanel = new JPanel(new BorderLayout());
        journalPanel.setBackground(Color.WHITE);
        journalPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("📚 Journal & Research");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        journalPanel.add(headerPanel, BorderLayout.NORTH);

        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);

        // Add journal items
        contentPanel.add(createJournalItem("📖 Research Publications", "3 published papers", "View Publications"));
        contentPanel.add(createJournalItem("🔬 Ongoing Research", "2 active projects", "View Projects"));
        contentPanel.add(createJournalItem("📝 Conference Presentations", "5 presentations", "View Presentations"));
        contentPanel.add(createJournalItem("📚 Teaching Materials", "12 course materials", "View Materials"));

        journalPanel.add(contentPanel, BorderLayout.CENTER);

        mainCardHolder.add(journalPanel, "JOURNAL");
    }

    private JPanel createJournalItem(String title, String description, String action) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(new Color(52, 73, 94));

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descLabel.setForeground(new Color(100, 100, 100));

        JButton actionButton = new JButton(action);
        actionButton.setFont(new Font("Arial", Font.PLAIN, 10));
        actionButton.setBackground(new Color(52, 73, 94));
        actionButton.setForeground(Color.WHITE);
        actionButton.setFocusPainted(false);
        actionButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.add(titleLabel);
        leftPanel.add(descLabel);

        itemPanel.add(leftPanel, BorderLayout.WEST);
        itemPanel.add(actionButton, BorderLayout.EAST);

        return itemPanel;
    }

    private void createAboutPanel() {
        JPanel aboutPanel = new JPanel(new BorderLayout());
        aboutPanel.setBackground(Color.WHITE);
        aboutPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("ℹ️ About iSLU Teacher Portal");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        aboutPanel.add(headerPanel, BorderLayout.NORTH);

        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);

        // Add about information
        JLabel welcomeLabel = new JLabel("Welcome to the iSLU Teacher Portal");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        welcomeLabel.setForeground(new Color(52, 73, 94));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descLabel = new JLabel("<html><div style='text-align: center; width: 500px;'>" +
            "The iSLU Teacher Portal provides comprehensive tools for managing classes, " +
            "tracking student progress, and maintaining academic records. " +
            "This system is designed to streamline teaching activities and enhance " +
            "the educational experience for both teachers and students.</div></html>");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descLabel.setForeground(new Color(50, 50, 50));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel versionLabel = new JLabel("Version 1.0 | © 2025 Saint Louis University");
        versionLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        versionLabel.setForeground(new Color(150, 150, 150));
        versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(welcomeLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(descLabel);
        contentPanel.add(Box.createVerticalStrut(30));
        contentPanel.add(versionLabel);
        contentPanel.add(Box.createVerticalGlue());

        aboutPanel.add(contentPanel, BorderLayout.CENTER);

        mainCardHolder.add(aboutPanel, "ABOUT");
    }

    private JPanel createFooter() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setPreferredSize(new Dimension(0, 50));
        footerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        JSeparator footerSeparator = new JSeparator();
        footerSeparator.setForeground(new Color(70, 130, 180));
        footerSeparator.setBackground(new Color(70, 130, 180));
        footerSeparator.setPreferredSize(new Dimension(0, 2));

        JLabel copyrightLabel = new JLabel("Copyright © 2025 Saint Louis University Inc. All rights reserved.");
        copyrightLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        copyrightLabel.setForeground(Color.BLACK);
        copyrightLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel footerWithSeparator = new JPanel(new BorderLayout());
        footerWithSeparator.add(footerSeparator, BorderLayout.NORTH);
        footerWithSeparator.add(footerPanel, BorderLayout.CENTER);

        footerPanel.add(copyrightLabel, BorderLayout.CENTER);
        return footerWithSeparator;
    }

    private void setupLayout() {
        // Show home panel by default
        mainCardLayout.show(mainCardHolder, "HOME");
    }

    private JPanel createNotesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "📌 Faculty Notes",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14)
        ));
        panel.setBackground(Color.WHITE);

        statusArea = new JTextArea();
        statusArea.setEditable(false);
        statusArea.setFont(new Font("Arial", Font.PLAIN, 12));
        statusArea.setLineWrap(true);
        statusArea.setWrapStyleWord(true);
        statusArea.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(statusArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(0, 400));

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void loadAnnouncements() {
        // This method is now handled by the new announcement panel structure
        // The announcements are loaded dynamically in createAnnouncementsPanel()
        // No need to set text on announcementsArea as it's no longer used
    }

    private void loadTeacherNotes() {
        String text = "Welcome, " + teacherName + "!\n\n"
                + "- Use the sidebar to navigate.\n"
                + "- Open 📊 Previous Grades to access Encode, Completion, and View.\n"
                + "- Demo: Changes are not persisted to Database.txt.\n";
        statusArea.setText(text);
        statusArea.setCaretPosition(0);
    }

    private void loadTeacherStatus() {
        // Load teacher status information
        // This is a placeholder implementation
    }

    private String getTeacherNameFromDatabase(String teacherID) {
        // This would normally query the database
        // For now, return a placeholder name
        return "Dr. " + teacherID.substring(3); // Extract last 4 digits as name
    }

    private void startDatabaseMonitoring() {
        // Start monitoring database changes
        databaseCheckTimer = new Timer(5000, e -> {
            // Check for database changes every 5 seconds
            // This is a placeholder implementation
        });
        databaseCheckTimer.start();
    }

    private void stopDatabaseMonitoring() {
        if (databaseCheckTimer != null) {
            databaseCheckTimer.stop();
        }
    }
    
    // ========== DATA LOADING METHODS ==========
    
    private void loadClassChecklistData(DefaultTableModel model) {
        // Load the teacher's assigned subject information
        TeacherInfo teacherInfo = TeacherDataManager.getTeacherInfo(teacherID);
        if (teacherInfo != null) {
            // Get subject details from the teacher database
            String classCode = teacherInfo.getSubjectCode();
            String courseNumber = getCourseNumberFromSubjectCode(classCode);
            String courseDescription = teacherInfo.getSubjectName();
            int paidUnits = getPaidUnitsFromSubjectCode(classCode);
            String schedule = getScheduleFromSubjectCode(classCode);
            String days = getDaysFromSubjectCode(classCode);
            String room = getRoomFromSubjectCode(classCode);
            
            // Get student count for this subject
            int studentCount = StudentDataManager.getAllStudents().size();
            
            // Add the subject as a row in the table
            model.addRow(new Object[]{
                classCode,
                courseNumber,
                courseDescription,
                paidUnits,
                schedule,
                days,
                room,
                studentCount,
                "👥" // Students icon
            });
        }
    }
    
    private String getCourseNumberFromSubjectCode(String subjectCode) {
        // Map subject codes to course numbers
        switch (subjectCode) {
            case "7024": return "NSTP 101";
            case "9454": return "GSTS 101";
            case "9455": return "GENVI 101";
            case "9456": return "CFE 103";
            case "9457": return "IT 211";
            case "9458A": return "IT 212";
            case "9458B": return "IT 212L";
            case "9459A": return "IT 213";
            case "9459B": return "IT 213L";
            case "9547": return "FIT OA";
            default: return "UNKNOWN";
        }
    }
    
    private int getPaidUnitsFromSubjectCode(String subjectCode) {
        // Map subject codes to paid units
        switch (subjectCode) {
            case "7024": return 3;
            case "9454": return 3;
            case "9455": return 3;
            case "9456": return 3;
            case "9457": return 3;
            case "9458A": return 2;
            case "9458B": return 1;
            case "9459A": return 2;
            case "9459B": return 1;
            case "9547": return 2;
            default: return 3;
        }
    }
    
    private String getScheduleFromSubjectCode(String subjectCode) {
        // Map subject codes to schedules
        switch (subjectCode) {
            case "7024": return "01:30 PM - 02:30 PM";
            case "9454": return "09:30 AM - 10:30 AM";
            case "9455": return "09:30 AM - 10:30 AM";
            case "9456": return "01:30 PM - 02:30 PM";
            case "9457": return "10:30 AM - 11:30 AM";
            case "9458A": return "02:30 PM - 03:30 PM";
            case "9458B": return "04:00 PM - 05:30 PM";
            case "9459A": return "08:30 AM - 09:30 AM";
            case "9459B": return "11:30 AM - 01:00 PM";
            case "9547": return "03:30 PM - 05:30 PM";
            default: return "TBA";
        }
    }
    
    private String getDaysFromSubjectCode(String subjectCode) {
        // Map subject codes to days
        switch (subjectCode) {
            case "7024": return "MWF";
            case "9454": return "TThS";
            case "9455": return "MWF";
            case "9456": return "TThS";
            case "9457": return "MWF";
            case "9458A": return "TF";
            case "9458B": return "TF";
            case "9459A": return "TF";
            case "9459B": return "TF";
            case "9547": return "TH";
            default: return "TBA";
        }
    }
    
    private String getRoomFromSubjectCode(String subjectCode) {
        // Map subject codes to rooms
        switch (subjectCode) {
            case "7024": return "D906";
            case "9454": return "D504";
            case "9455": return "D503";
            case "9456": return "D503";
            case "9457": return "D511";
            case "9458A": return "D513";
            case "9458B": return "D522";
            case "9459A": return "D513";
            case "9459B": return "D528";
            case "9547": return "D221";
            default: return "TBA";
        }
    }
    
    private int getTotalPaidUnits() {
        // Return the paid units for the teacher's assigned subject
        TeacherInfo teacherInfo = TeacherDataManager.getTeacherInfo(teacherID);
        if (teacherInfo != null) {
            return getPaidUnitsFromSubjectCode(teacherInfo.getSubjectCode());
        }
        return 0;
    }
    
    private void loadAttendanceData(DefaultTableModel model) {
        // Load all students from database
        MyDoublyLinkedList<StudentInfo> allStudents = StudentDataManager.getAllStudents();
        
        // Create sample dates for attendance tracking
        String[] sampleDates = {
            "2025-01-15",
            "2025-01-17", 
            "2025-01-20",
            "2025-01-22",
            "2025-01-24"
        };
        
        // For each student, create attendance records for each date
        for (int i = 0; i < allStudents.size(); i++) {
            StudentInfo student = allStudents.get(i);
            
            for (String date : sampleDates) {
                // Check if this student already has attendance data for this date
                MySinglyLinkedList<AttendanceRecord> existingRecords = SharedDataManager.getStudentAttendance(student.getStudentID());
                String status = "Present";
                String remarks = "";
                
                // Look for existing data
                for (int j = 0; j < existingRecords.size(); j++) {
                    AttendanceRecord existingRecord = existingRecords.get(j);
                    if (existingRecord.getSubjectCode().equals(subjectCode) && 
                        existingRecord.getDateString().equals(date)) {
                        status = existingRecord.getStatus();
                        remarks = existingRecord.getRemarks();
                        break;
                    }
                }
                
                model.addRow(new Object[]{
                    student.getStudentID(),
                    student.getStudentName(),
                    date,
                    status,
                    remarks
                });
            }
        }
    }

    private void loadAttendanceClassData(DefaultTableModel model) {
        // Add the teacher's assigned class/subject
        String classCode = subjectCode;
        String course = subjectName;
        String schedule = getScheduleFromSubjectCode(subjectCode);
        String days = getDaysFromSubjectCode(subjectCode);
        String room = getRoomFromSubjectCode(subjectCode);
        
        model.addRow(new Object[]{
            classCode,
            course,
            schedule,
            days,
            room,
            "👤", // Check button placeholder
            "📄"  // Export button placeholder
        });
    }

    private void showAttendanceDialog(String classCode, String course) {
        // Create a dialog to show attendance management for the class
        JDialog attendanceDialog = new JDialog(this, "Attendance - " + course, true);
        attendanceDialog.setSize(800, 600);
        attendanceDialog.setLocationRelativeTo(this);
        
        JPanel dialogPanel = new JPanel(new BorderLayout());
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel titleLabel = new JLabel("📋 Attendance Management - " + course);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        dialogPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Content - Student attendance table
        String[] columnNames = {"Student ID", "Name", "Date", "Status", "Remarks"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3 || column == 4; // Only Status and Remarks are editable
            }
        };
        
        // Load attendance data for this class
        loadClassAttendanceData(model, classCode);
        
        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        
        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(100); // Student ID
        table.getColumnModel().getColumn(1).setPreferredWidth(200); // Name
        table.getColumnModel().getColumn(2).setPreferredWidth(100); // Date
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // Status
        table.getColumnModel().getColumn(4).setPreferredWidth(150); // Remarks
        
        // Add combo box editor for Status column
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Present", "Absent", "Late"});
        table.getColumnModel().getColumn(3).setCellEditor(new javax.swing.DefaultCellEditor(statusCombo));
        
        // Add table change listener to save changes
        table.getModel().addTableModelListener(e -> {
            if (e.getType() == javax.swing.event.TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int col = e.getColumn();
                if (col == 3 || col == 4) { // Status or Remarks column
                    String studentID = (String) model.getValueAt(row, 0);
                    String date = (String) model.getValueAt(row, 2);
                    String status = (String) model.getValueAt(row, 3);
                    String remarks = (String) model.getValueAt(row, 4);
                    
                    // Save to shared data manager
                    SharedDataManager.recordAttendance(studentID, subjectCode, date, status, remarks);
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        dialogPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JButton closeButton = new JButton("Close");
        closeButton.setPreferredSize(new Dimension(80, 30));
        closeButton.setFont(new Font("Arial", Font.PLAIN, 12));
        closeButton.setBackground(new Color(52, 73, 94));
        closeButton.setForeground(Color.WHITE);
        closeButton.setBorderPainted(false);
        closeButton.addActionListener(e -> attendanceDialog.dispose());
        
        buttonPanel.add(closeButton);
        dialogPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        attendanceDialog.add(dialogPanel);
        attendanceDialog.setVisible(true);
    }

    private void loadClassAttendanceData(DefaultTableModel model, String classCode) {
        // Load all students from database
        MyDoublyLinkedList<StudentInfo> allStudents = StudentDataManager.getAllStudents();
        
        // Create sample dates for attendance tracking
        String[] sampleDates = {
            "2025-01-15",
            "2025-01-17", 
            "2025-01-20",
            "2025-01-22",
            "2025-01-24"
        };
        
        // For each student, create attendance records for each date
        for (int i = 0; i < allStudents.size(); i++) {
            StudentInfo student = allStudents.get(i);
            
            for (String date : sampleDates) {
                // Check if this student already has attendance data for this date
                MySinglyLinkedList<AttendanceRecord> existingRecords = SharedDataManager.getStudentAttendance(student.getStudentID());
                String status = "Present";
                String remarks = "";
                
                // Look for existing data
                for (int j = 0; j < existingRecords.size(); j++) {
                    AttendanceRecord existingRecord = existingRecords.get(j);
                    if (existingRecord.getSubjectCode().equals(subjectCode) && 
                        existingRecord.getDateString().equals(date)) {
                        status = existingRecord.getStatus();
                        remarks = existingRecord.getRemarks();
                        break;
                    }
                }
                
                model.addRow(new Object[]{
                    student.getStudentID(),
                    student.getStudentName(),
                    date,
                    status,
                    remarks
                });
            }
        }
    }

    private void exportAttendance(String classCode, String course) {
        // Create a simple export functionality
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Attendance - " + course);
        fileChooser.setSelectedFile(new java.io.File("attendance_" + classCode + "_" + 
            java.time.LocalDate.now().toString() + ".txt"));
        
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();
            
            try (java.io.PrintWriter writer = new java.io.PrintWriter(file)) {
                writer.println("ATTENDANCE REPORT");
                writer.println("=================");
                writer.println("Class Code: " + classCode);
                writer.println("Course: " + course);
                writer.println("Teacher: " + teacherName);
                writer.println("Date: " + java.time.LocalDate.now());
                writer.println();
                writer.println("Student ID\tName\t\t\tDate\t\tStatus\t\tRemarks");
                writer.println("------------------------------------------------------------------------");
                
                // Load all students and their attendance
                MyDoublyLinkedList<StudentInfo> allStudents = StudentDataManager.getAllStudents();
                String[] sampleDates = {
                    "2025-01-15", "2025-01-17", "2025-01-20", "2025-01-22", "2025-01-24"
                };
                
                for (int i = 0; i < allStudents.size(); i++) {
                    StudentInfo student = allStudents.get(i);
                    
                    for (String date : sampleDates) {
                        MySinglyLinkedList<AttendanceRecord> attendance = SharedDataManager.getStudentAttendance(student.getStudentID());
                        
                        String status = "Present";
                        String remarks = "";
                        
                        for (int j = 0; j < attendance.size(); j++) {
                            AttendanceRecord record = attendance.get(j);
                            if (record.getSubjectCode().equals(subjectCode) && 
                                record.getDateString().equals(date)) {
                                status = record.getStatus();
                                remarks = record.getRemarks();
                                break;
                            }
                        }
                        
                        writer.println(student.getStudentID() + "\t" + 
                            student.getStudentName() + "\t\t" + 
                            date + "\t" + 
                            status + "\t\t" + 
                            remarks);
                    }
                }
                
                writer.close();
                
                // Show success message
                javax.swing.JOptionPane.showMessageDialog(this, 
                    "Attendance exported successfully to:\n" + file.getAbsolutePath(),
                    "Export Successful", 
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (java.io.IOException e) {
                javax.swing.JOptionPane.showMessageDialog(this, 
                    "Error exporting attendance: " + e.getMessage(),
                    "Export Error", 
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void loadGradesData(DefaultTableModel model) {
        // Load all students from database
        MyDoublyLinkedList<StudentInfo> allStudents = StudentDataManager.getAllStudents();
        
        // For each student, create grade record for this teacher's subject
        for (int i = 0; i < allStudents.size(); i++) {
            StudentInfo student = allStudents.get(i);
            
            // Check if this student already has grades for this subject
            MySinglyLinkedList<GradeRecord> existingGrades = SharedDataManager.getStudentGrades(student.getStudentID());
            double prelimGrade = 0.0;
            double midtermGrade = 0.0;
            double finalGrade = 0.0;
            double average = 0.0;
            boolean hasGrades = false;
            
            // Look for existing grades for this subject
            for (int j = 0; j < existingGrades.size(); j++) {
                GradeRecord existingGrade = existingGrades.get(j);
                if (existingGrade.getSubjectCode().equals(subjectCode)) {
                    prelimGrade = existingGrade.getPrelimGrade();
                    midtermGrade = existingGrade.getMidtermGrade();
                    finalGrade = existingGrade.getFinalGrade();
                    average = (prelimGrade + midtermGrade + finalGrade) / 3.0;
                    hasGrades = true;
                    break;
                }
            }
            
            // Format grades to match student portal - show "Not Yet Submitted" for empty grades
            String prelimDisplay = hasGrades && prelimGrade > 0 ? String.valueOf(prelimGrade) : "";
            String midtermDisplay = hasGrades && midtermGrade > 0 ? String.valueOf(midtermGrade) : "";
            String finalDisplay = hasGrades && finalGrade > 0 ? String.valueOf(finalGrade) : "Not Yet Submitted";
            String averageDisplay = hasGrades && average > 0 ? String.valueOf(Math.round(average * 10.0) / 10.0) : "";
            
            model.addRow(new Object[]{
                student.getStudentID(),
                student.getStudentName(),
                prelimDisplay,
                midtermDisplay,
                finalDisplay,
                averageDisplay
            });
        }
    }
    
    private String getStudentName(String studentID) {
        return StudentDataManager.getStudentName(studentID);
    }

    private double parseGradeValue(Object value) {
        if (value == null) return 0.0;
        if (value instanceof Number) return ((Number) value).doubleValue();
        if (value instanceof String) {
            String str = (String) value;
            if (str.trim().isEmpty() || str.equals("Not Yet Submitted")) return 0.0;
            try {
                return Double.parseDouble(str);
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }
        return 0.0;
    }

    private void filterGradesTable(String selectedClassCode, String selectedSubject, String studentID) {
        if (gradesTableModel == null) return;
        
        // Clear current table data
        gradesTableModel.setRowCount(0);
        
        // Load all students from database
        MyDoublyLinkedList<StudentInfo> allStudents = StudentDataManager.getAllStudents();
        
        // Apply filters and load data
        for (int i = 0; i < allStudents.size(); i++) {
            StudentInfo student = allStudents.get(i);
            
            // Apply student ID filter
            if (!studentID.isEmpty() && !student.getStudentID().contains(studentID)) {
                continue;
            }
            
            // Apply class code filter (for now, all students are in the teacher's class)
            if (!selectedClassCode.equals("All Classes") && !selectedClassCode.equals(subjectCode)) {
                continue;
            }
            
            // Apply subject filter
            if (!selectedSubject.equals("Select subject...") && !selectedSubject.equals(subjectName)) {
                continue;
            }
            
            // Check if this student already has grades for this subject
            MySinglyLinkedList<GradeRecord> existingGrades = SharedDataManager.getStudentGrades(student.getStudentID());
            double prelimGrade = 0.0;
            double midtermGrade = 0.0;
            double finalGrade = 0.0;
            double average = 0.0;
            boolean hasGrades = false;
            
            // Look for existing grades for this subject
            for (int j = 0; j < existingGrades.size(); j++) {
                GradeRecord existingGrade = existingGrades.get(j);
                if (existingGrade.getSubjectCode().equals(subjectCode)) {
                    prelimGrade = existingGrade.getPrelimGrade();
                    midtermGrade = existingGrade.getMidtermGrade();
                    finalGrade = existingGrade.getFinalGrade();
                    average = (prelimGrade + midtermGrade + finalGrade) / 3.0;
                    hasGrades = true;
                    break;
                }
            }
            
            // Format grades to match student portal - show "Not Yet Submitted" for empty grades
            String prelimDisplay = hasGrades && prelimGrade > 0 ? String.valueOf(prelimGrade) : "";
            String midtermDisplay = hasGrades && midtermGrade > 0 ? String.valueOf(midtermGrade) : "";
            String finalDisplay = hasGrades && finalGrade > 0 ? String.valueOf(finalGrade) : "Not Yet Submitted";
            String averageDisplay = hasGrades && average > 0 ? String.valueOf(Math.round(average * 10.0) / 10.0) : "";
            
            gradesTableModel.addRow(new Object[]{
                student.getStudentID(),
                student.getStudentName(),
                prelimDisplay,
                midtermDisplay,
                finalDisplay,
                averageDisplay
            });
        }
    }

    private void showStudentsDialog() {
        // Create a dialog to show the students in this teacher's class
        JDialog studentsDialog = new JDialog(this, "Students - " + subjectName, true);
        studentsDialog.setSize(600, 500);
        studentsDialog.setLocationRelativeTo(this);

        JPanel dialogPanel = new JPanel(new BorderLayout());
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel headerLabel = new JLabel("Students in " + subjectName + " (" + subjectCode + ")");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel, BorderLayout.WEST);

        dialogPanel.add(headerPanel, BorderLayout.NORTH);

        // Content - Students list with person icons
        JPanel studentsPanel = new JPanel();
        studentsPanel.setLayout(new BoxLayout(studentsPanel, BoxLayout.Y_AXIS));
        studentsPanel.setBackground(new Color(240, 240, 240));
        studentsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Load all students and create clickable student icons
        MyDoublyLinkedList<StudentInfo> allStudents = StudentDataManager.getAllStudents();
        for (int i = 0; i < allStudents.size(); i++) {
            StudentInfo student = allStudents.get(i);
            JPanel studentIcon = createStudentIcon(student);
            studentsPanel.add(studentIcon);
            studentsPanel.add(Box.createVerticalStrut(5));
        }

        JScrollPane studentScrollPane = new JScrollPane(studentsPanel);
        studentScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        studentScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        studentScrollPane.setBorder(BorderFactory.createEmptyBorder());

        dialogPanel.add(studentScrollPane, BorderLayout.CENTER);

        // Close button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> studentsDialog.dispose());
        buttonPanel.add(closeButton);

        dialogPanel.add(buttonPanel, BorderLayout.SOUTH);

        studentsDialog.add(dialogPanel);
        studentsDialog.setVisible(true);
    }

    private JPanel createStudentIcon(StudentInfo student) {
        JPanel studentPanel = new JPanel(new BorderLayout());
        studentPanel.setBackground(new Color(220, 220, 220));
        studentPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        studentPanel.setPreferredSize(new Dimension(200, 50));
        studentPanel.setMaximumSize(new Dimension(200, 50));

        // Create student icon (using text representation)
        JLabel iconLabel = new JLabel("👤");
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Student info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(220, 220, 220));

        JLabel nameLabel = new JLabel(student.getStudentName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        nameLabel.setForeground(Color.BLACK);

        JLabel idLabel = new JLabel(student.getStudentID());
        idLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        idLabel.setForeground(Color.GRAY);

        infoPanel.add(nameLabel);
        infoPanel.add(idLabel);

        studentPanel.add(iconLabel, BorderLayout.WEST);
        studentPanel.add(infoPanel, BorderLayout.CENTER);

        // Add click functionality
        studentPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        studentPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showStudentChecklist(student);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                studentPanel.setBackground(new Color(200, 200, 200));
                infoPanel.setBackground(new Color(200, 200, 200));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                studentPanel.setBackground(new Color(220, 220, 220));
                infoPanel.setBackground(new Color(220, 220, 220));
            }
        });

        return studentPanel;
    }

    private void showStudentChecklist(StudentInfo student) {
        // Create a dialog to show the student's checklist
        JDialog studentDialog = new JDialog(this, "Student Checklist - " + student.getStudentName(), true);
        studentDialog.setSize(600, 400);
        studentDialog.setLocationRelativeTo(this);

        JPanel dialogPanel = new JPanel(new BorderLayout());
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel headerLabel = new JLabel("Student Checklist - " + student.getStudentName() + " (" + student.getStudentID() + ")");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel, BorderLayout.WEST);

        dialogPanel.add(headerPanel, BorderLayout.NORTH);

        // Content - Student's checklist items
        String[] columns = {"Requirement", "Status", "Remarks"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1 || column == 2; // Status and Remarks are editable
            }
        };

        // Load student's checklist data
        loadStudentChecklistData(model, student.getStudentID());

        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(240, 240, 240));

        // Status combo box
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Complete", "Pending", "Missing", "In Progress"});
        table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(statusCombo));

        // Add table change listener
        table.getModel().addTableModelListener(e -> {
            if (e.getType() == javax.swing.event.TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int col = e.getColumn();
                if (col == 1 || col == 2) { // Status or Remarks column
                    String requirement = (String) model.getValueAt(row, 0);
                    String status = (String) model.getValueAt(row, 1);
                    String remarks = (String) model.getValueAt(row, 2);
                    SharedDataManager.updateClassChecklist(student.getStudentID(), subjectCode, requirement, status, remarks);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        dialogPanel.add(scrollPane, BorderLayout.CENTER);

        // Close button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> studentDialog.dispose());
        buttonPanel.add(closeButton);

        dialogPanel.add(buttonPanel, BorderLayout.SOUTH);

        studentDialog.add(dialogPanel);
        studentDialog.setVisible(true);
    }

    private void loadStudentChecklistData(DefaultTableModel model, String studentID) {
        // Create default requirements for this subject
        String[] defaultRequirements = {
            "Project 1",
            "Project 2", 
            "Midterm Exam",
            "Final Exam",
            "Participation"
        };
        
        // For each requirement, check if student has data
        for (String requirement : defaultRequirements) {
            // Check if this student already has data for this requirement
            MySinglyLinkedList<ClassChecklistItem> existingItems = SharedDataManager.getStudentClassChecklist(studentID);
            String status = "Pending";
            String remarks = "";
            
            // Look for existing data
            for (int j = 0; j < existingItems.size(); j++) {
                ClassChecklistItem existingItem = existingItems.get(j);
                if (existingItem.getSubjectCode().equals(subjectCode) && 
                    existingItem.getRequirement().equals(requirement)) {
                    status = existingItem.getStatus();
                    remarks = existingItem.getRemarks();
                    break;
                }
            }
            
            model.addRow(new Object[]{
                requirement,
                status,
                remarks
            });
        }
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

            // Create announcement with priority
            Announcement announcement = new Announcement(title, content, teacherName, audience, priority);
            
            // Add to shared data manager
            boolean success = SharedDataManager.addAnnouncement(title, content, teacherName, audience, priority);
            
            if (success) {
                JOptionPane.showMessageDialog(dialog, "Announcement posted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                // Refresh the announcements panel
                refreshAnnouncementsPanel();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to post announcement. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(cancelButton);
        buttonPanel.add(postButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    private void refreshAnnouncementsPanel() {
        // This method will refresh the announcements panel
        // For now, we'll just rebuild the cards to show updated announcements
        buildCards();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // For testing purposes, use a sample teacher ID
            new ISLUTeacherPortal("TCH001").setVisible(true);
        });
    }
}
