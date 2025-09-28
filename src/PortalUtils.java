import java.util.List;

/**
 * Utility class that demonstrates integration of all classes in the Student Portal system
 * This class shows how MyDoublyLinkedList, MenuItem, DataManager, and other classes work together
 */
public class PortalUtils {

    /**
     * Creates a comprehensive menu system using MyDoublyLinkedList and MenuItem
     * @return A doubly linked list containing all menu items
     */
    public static MyDoublyLinkedList<MenuItem> createIntegratedMenuSystem() {
        MyDoublyLinkedList<MenuItem> menu = new MyDoublyLinkedList<>();

        // Create sub-items for each menu category
        MySinglyLinkedList<SubMenuItem> homeSubList = createHomeSublist();
        MySinglyLinkedList<SubMenuItem> attendanceSubList = createAttendanceSubList();
        MySinglyLinkedList<SubMenuItem> scheduleSubList = createScheduleSubList();
        MySinglyLinkedList<SubMenuItem> gradesSubList = createGradeSubList();
        MySinglyLinkedList<SubMenuItem> soaSubList = createSOASubList();
        MySinglyLinkedList<SubMenuItem> torSubList = createTORSubList();
        MySinglyLinkedList<SubMenuItem> personalDetailsSubList = createPersonalDetailsSubList();
        MySinglyLinkedList<SubMenuItem> curriculumChecklistSubList = createCurriculumChecklistSubList();
        MySinglyLinkedList<SubMenuItem> medicalSubList = createMedicalSubList();
        MySinglyLinkedList<SubMenuItem> journalSubList = createJournalSubList();
        MySinglyLinkedList<SubMenuItem> downloadableSubList = createDownloadableSubList();


        // Add menu items to the doubly linked list
        menu.add(new MenuItem("🏠 Home", homeSubList));
        menu.add(new MenuItem("📌 Attendance", attendanceSubList));
        menu.add(new MenuItem("📅 Schedule", scheduleSubList));
        menu.add(new MenuItem("🧮 Statement of Accounts", soaSubList));
        menu.add(new MenuItem("📊 Grades", gradesSubList));
        menu.add(new MenuItem("📋 Transcript of Records", torSubList));
        menu.add(new MenuItem("✅ Curriculum Checklist", curriculumChecklistSubList));
        menu.add(new MenuItem("🏥 Medical Record", medicalSubList));
        menu.add(new MenuItem("📚 Journal/Periodical", journalSubList));
        menu.add(new MenuItem("👤 Personal Details", personalDetailsSubList));
        menu.add(new MenuItem("ℹ️ Downloadable/ About iSLU", downloadableSubList));

        return menu;
    }

    /**
     * Demonstrates data integration by creating a student management system
     * @return A doubly linked list of student information
     */
    public static MyDoublyLinkedList<StudentInfo> createStudentManagementSystem() {
        MyDoublyLinkedList<StudentInfo> students = new MyDoublyLinkedList<>();

        // Get all students from DataManager
        List<StudentInfo> allStudents = DataManager.getAllStudents();

        // Add students to the doubly linked list
        for (StudentInfo student : allStudents) {
            students.add(student);
        }

        return students;
    }

    /**
     * Demonstrates menu navigation using the doubly linked list
     * @param menu The menu system
     * @param currentIndex Current menu index
     * @param direction Direction to navigate (1 for next, -1 for previous)
     * @return The menu item at the new position
     */
    public static MenuItem navigateMenu(MyDoublyLinkedList<MenuItem> menu, int currentIndex, int direction) {
        int newIndex = currentIndex + direction;

        if (newIndex < 0) {
            newIndex = menu.getSize() - 1; // Wrap to last item
        } else if (newIndex >= menu.getSize()) {
            newIndex = 0; // Wrap to first item
        }

        return menu.get(newIndex);
    }

    /**
     * Validates student data using integrated systems
     * @param studentID The student ID to validate
     * @param password The password to validate
     * @return true if valid, false otherwise
     */
    public static boolean validateStudentCredentials(String studentID, String password) {
        // Use DataManager for authentication
        boolean isValid = DataManager.authenticateUser(studentID, password);

        if (isValid) {
            // Get student info for additional validation
            StudentInfo studentInfo = DataManager.getStudentInfo(studentID);
            if (studentInfo != null) {
                System.out.println("Welcome, " + studentInfo.getFullName() + "!");
                return true;
            }
        }

        return false;
    }

    /**
     * Creates a comprehensive student portal session
     * @param studentID The student ID
     * @return A portal session object containing all integrated data
     */
    public static PortalSession createPortalSession(String studentID) {
        StudentInfo studentInfo = DataManager.getStudentInfo(studentID);
        MyDoublyLinkedList<MenuItem> menu = createIntegratedMenuSystem();
        List<PaymentTransaction> transactions = DataManager.loadPaymentTransactions(studentID);

        return new PortalSession(studentInfo, menu, transactions);
    }

    // Helper methods for creating sub-lists
    static MySinglyLinkedList<SubMenuItem> createHomeSublist() {
        MySinglyLinkedList<SubMenuItem> homeSubList = new MySinglyLinkedList<>();
        homeSubList.add(new SubMenuItem("📰 Events, News & Announcements"));
        homeSubList.add(new SubMenuItem("📌 Student Status"));
        return homeSubList;
    }

    private static MySinglyLinkedList<SubMenuItem> createAttendanceSubList() {
        MySinglyLinkedList<SubMenuItem> attendanceSubList = new MySinglyLinkedList<>();
        attendanceSubList.add(new SubMenuItem("Attendance Record"));
        return attendanceSubList;
    }

    private static MySinglyLinkedList<SubMenuItem> createScheduleSubList() {
        MySinglyLinkedList<SubMenuItem> scheduleSubList = new MySinglyLinkedList<>();
        scheduleSubList.add(new SubMenuItem("Class Schedule"));
        return scheduleSubList;
    }

    private static MySinglyLinkedList<SubMenuItem> createGradeSubList() {
        MySinglyLinkedList<SubMenuItem> gradesSublist = new MySinglyLinkedList<>();
        gradesSublist.add(new SubMenuItem("Grades"));
        return gradesSublist;
    }

    private static MySinglyLinkedList<SubMenuItem> createSOASubList() {
        MySinglyLinkedList<SubMenuItem> sOASubList = new MySinglyLinkedList<>();
        
        // Create SubMenuItem for "Statement of Accounts (SHORT TERM, 2025)" with sub-sub-items
        SubMenuItem statement = new SubMenuItem("Statement of Accounts (SHORT TERM, 2025)");
        statement.addSubSubItem("View Current Balance");
        statement.addSubSubItem("Payment History");
        statement.addSubSubItem("Fee Breakdown");
        statement.addSubSubItem("Download Statement");
        sOASubList.add(statement);
        
        // Create SubMenuItem for "Online Payment Channels" with sub-sub-items
        SubMenuItem paymentChannels = new SubMenuItem("Online Payment Channels");
        paymentChannels.addSubSubItem("Credit Card Payment");
        paymentChannels.addSubSubItem("Bank Transfer");
        paymentChannels.addSubSubItem("GCash Payment");
        paymentChannels.addSubSubItem("PayMaya Payment");
        paymentChannels.addSubSubItem("Over-the-Counter");
        sOASubList.add(paymentChannels);
        
        return sOASubList;
    }

    private static MySinglyLinkedList<SubMenuItem> createTORSubList() {
        MySinglyLinkedList<SubMenuItem> TORSubList = new MySinglyLinkedList<>();
        TORSubList.add(new SubMenuItem("📋 Transcript of Records"));
        return TORSubList;
    }

    private static MySinglyLinkedList<SubMenuItem> createPersonalDetailsSubList() {
        MySinglyLinkedList<SubMenuItem> personalDetailsSubList = new MySinglyLinkedList<>();
        personalDetailsSubList.add(new SubMenuItem("User Profile"));
        return personalDetailsSubList;
    }

    private static MySinglyLinkedList<SubMenuItem> createCurriculumChecklistSubList(){
        MySinglyLinkedList<SubMenuItem> CurriculumChecklistSubList = new MySinglyLinkedList<>();
        CurriculumChecklistSubList.add(new SubMenuItem("✅ BACHELOR OF SCIENCE IN INFORMATION TECHNOLOGY FIRST SEMESTER, 2018-2019"));
        return CurriculumChecklistSubList;
    }

    private static MySinglyLinkedList<SubMenuItem> createMedicalSubList(){
        MySinglyLinkedList<SubMenuItem> medicalSubList = new MySinglyLinkedList<>();
        return medicalSubList;
    }



    private static MySinglyLinkedList<SubMenuItem> createJournalSubList(){
        MySinglyLinkedList<SubMenuItem> journalSubList = new MySinglyLinkedList<>();
        
        // Create SubMenuItem for "WHAT ARE JOURNAL INDEXES?" with sub-sub-items
        SubMenuItem journalIndexes = new SubMenuItem("WHAT ARE JOURNAL INDEXES?");
        journalIndexes.addSubSubItem("Definition of Journal Indexes");
        journalIndexes.addSubSubItem("Types of Indexes");
        journalIndexes.addSubSubItem("Benefits of Using Indexes");
        journalSubList.add(journalIndexes);
        
        // Create SubMenuItem for "THE SLU LIBRARIES' PERIODICAL ARTICLE INDEXES" with sub-sub-items
        SubMenuItem sluIndexes = new SubMenuItem("THE SLU LIBRARIES' PERIODICAL ARTICLE INDEXES");
        sluIndexes.addSubSubItem("Academic Search Complete");
        sluIndexes.addSubSubItem("JSTOR");
        sluIndexes.addSubSubItem("ScienceDirect");
        sluIndexes.addSubSubItem("ProQuest");
        journalSubList.add(sluIndexes);
        
        // Create SubMenuItem for "STEPS IN ACCESSING THE PERIODICAL ARTICLE INDEXES" with sub-sub-items
        SubMenuItem accessSteps = new SubMenuItem("STEPS IN ACCESSING THE PERIODICAL ARTICLE INDEXES");
        accessSteps.addSubSubItem("Step 1: Login to SLU Portal");
        accessSteps.addSubSubItem("Step 2: Navigate to Library Resources");
        accessSteps.addSubSubItem("Step 3: Select Database");
        accessSteps.addSubSubItem("Step 4: Perform Search");
        accessSteps.addSubSubItem("Step 5: Access Full Text");
        journalSubList.add(accessSteps);
        
        return journalSubList;
    }

    private static MySinglyLinkedList<SubMenuItem> createDownloadableSubList(){
        MySinglyLinkedList<SubMenuItem> downloadableSubList = new MySinglyLinkedList<>();
        downloadableSubList.add(new SubMenuItem("Downloadables"));
        downloadableSubList.add(new SubMenuItem("About iSLU"));
        return downloadableSubList;
    }
}

