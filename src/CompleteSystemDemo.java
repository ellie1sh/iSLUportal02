/**
 * CompleteSystemDemo - Demonstrates the complete teacher-student system
 * This class shows how all students appear in each teacher's subject management
 * 
 * AUTHOR: Group Project Implementation
 * SUBJECT: DATA STRUCTURE IT212 9458
 */
public class CompleteSystemDemo {
    
    public static void main(String[] args) {
        System.out.println("=== COMPLETE TEACHER-STUDENT SYSTEM DEMONSTRATION ===");
        System.out.println("This demonstrates how all students appear in each teacher's subject management\n");
        
        try {
            // Initialize all data managers
            System.out.println("Step 1: Initializing All Data Managers...");
            TeacherDataManager.initialize();
            StudentDataManager.initialize();
            SharedDataManager.initialize();
            System.out.println("✓ All data managers initialized\n");
            
            // Show all students
            System.out.println("Step 2: Displaying All Students in Database...");
            displayAllStudents();
            
            // Show teacher assignments
            System.out.println("\nStep 3: Displaying Teacher Subject Assignments...");
            displayTeacherAssignments();
            
            // Demonstrate how each teacher sees all students
            System.out.println("\nStep 4: Demonstrating Teacher-Student Management...");
            demonstrateTeacherStudentManagement();
            
            // Launch portals for testing
            System.out.println("\nStep 5: Launching Portals for Complete Testing...");
            launchCompleteSystem();
            
            System.out.println("\n=== COMPLETE SYSTEM DEMONSTRATION COMPLETE ===");
            System.out.println("Key Features Demonstrated:");
            System.out.println("- All students appear in each teacher's subject management");
            System.out.println("- Each teacher can only edit their assigned subject");
            System.out.println("- Teacher actions immediately reflect in student portal");
            System.out.println("- No random data - everything is teacher-controlled");
            System.out.println("- Complete CRUD operations with real-time synchronization");
            
        } catch (Exception e) {
            System.err.println("❌ Demo failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Display all students in the database
     */
    private static void displayAllStudents() {
        System.out.println("All Students in Database:");
        System.out.println("========================");
        
        MyDoublyLinkedList<StudentInfo> allStudents = StudentDataManager.getAllStudents();
        for (int i = 0; i < allStudents.size(); i++) {
            StudentInfo student = allStudents.get(i);
            System.out.println(String.format("%s - %s (%s)", 
                student.getStudentID(), student.getStudentName(), student.getProgram()));
        }
        System.out.println("Total Students: " + allStudents.size());
    }
    
    /**
     * Display teacher subject assignments
     */
    private static void displayTeacherAssignments() {
        System.out.println("Teacher Subject Assignments:");
        System.out.println("============================");
        
        MyDoublyLinkedList<TeacherInfo> teachers = TeacherDataManager.getAllTeachers();
        for (int i = 0; i < teachers.size(); i++) {
            TeacherInfo teacher = teachers.get(i);
            System.out.println(String.format("%s - %s", teacher.getTeacherID(), teacher.getTeacherName()));
            System.out.println(String.format("  Subject: %s (%s)", teacher.getSubjectCode(), teacher.getSubjectName()));
            System.out.println(String.format("  Can manage: ALL %d students in database", StudentDataManager.getAllStudents().size()));
            System.out.println();
        }
    }
    
    /**
     * Demonstrate how each teacher manages all students for their subject
     */
    private static void demonstrateTeacherStudentManagement() {
        System.out.println("Teacher-Student Management Examples:");
        System.out.println("===================================");
        
        // Example 1: NSTP Teacher
        System.out.println("Example 1: NSTP Teacher (T001001 - Dr. John Smith)");
        System.out.println("Subject: 7024 - NSTP-CWTS 1");
        System.out.println("Students to manage:");
        
        MyDoublyLinkedList<StudentInfo> allStudents = StudentDataManager.getAllStudents();
        for (int i = 0; i < Math.min(5, allStudents.size()); i++) { // Show first 5 students
            StudentInfo student = allStudents.get(i);
            System.out.println(String.format("  - %s (%s)", student.getStudentID(), student.getStudentName()));
        }
        System.out.println("  ... and " + (allStudents.size() - 5) + " more students");
        System.out.println("Actions: Can encode grades, record attendance, update checklist for ALL students");
        System.out.println();
        
        // Example 2: IT 211 Teacher
        System.out.println("Example 2: IT 211 Teacher (T001005 - Dr. Michael Brown)");
        System.out.println("Subject: 9457 - IT 211 (Requirements Analysis and Modeling)");
        System.out.println("Students to manage:");
        for (int i = 0; i < Math.min(5, allStudents.size()); i++) { // Show first 5 students
            StudentInfo student = allStudents.get(i);
            System.out.println(String.format("  - %s (%s)", student.getStudentID(), student.getStudentName()));
        }
        System.out.println("  ... and " + (allStudents.size() - 5) + " more students");
        System.out.println("Actions: Can encode grades, record attendance, update checklist for ALL students");
        System.out.println();
        
        // Example 3: Data Structures Teacher
        System.out.println("Example 3: Data Structures Teacher (T001006 - Dr. Lisa Davis)");
        System.out.println("Subject: 9458A - IT 212 (Data Structures LEC)");
        System.out.println("Students to manage:");
        for (int i = 0; i < Math.min(5, allStudents.size()); i++) { // Show first 5 students
            StudentInfo student = allStudents.get(i);
            System.out.println(String.format("  - %s (%s)", student.getStudentID(), student.getStudentName()));
        }
        System.out.println("  ... and " + (allStudents.size() - 5) + " more students");
        System.out.println("Actions: Can encode grades, record attendance, update checklist for ALL students");
    }
    
    /**
     * Launch the complete system for testing
     */
    private static void launchCompleteSystem() {
        System.out.println("Launching Complete System for Testing:");
        System.out.println("=====================================");
        
        // Launch NSTP Teacher Portal
        System.out.println("1. Launching NSTP Teacher Portal (T001001 - Dr. John Smith)...");
        javax.swing.SwingUtilities.invokeLater(() -> {
            ISLUTeacherPortal nstpPortal = new ISLUTeacherPortal("T001001");
            nstpPortal.setVisible(true);
            System.out.println("   ✓ NSTP Teacher Portal launched");
        });
        
        // Wait a moment
        try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        
        // Launch IT 211 Teacher Portal
        System.out.println("2. Launching IT 211 Teacher Portal (T001005 - Dr. Michael Brown)...");
        javax.swing.SwingUtilities.invokeLater(() -> {
            ISLUTeacherPortal it211Portal = new ISLUTeacherPortal("T001005");
            it211Portal.setVisible(true);
            System.out.println("   ✓ IT 211 Teacher Portal launched");
        });
        
        // Wait a moment
        try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        
        // Launch Student Portal
        System.out.println("3. Launching Student Portal (2251234)...");
        javax.swing.SwingUtilities.invokeLater(() -> {
            ISLUStudentPortal studentPortal = new ISLUStudentPortal("2251234");
            studentPortal.setVisible(true);
            System.out.println("   ✓ Student Portal launched");
        });
        
        System.out.println("\nComplete Testing Instructions:");
        System.out.println("=============================");
        System.out.println("1. NSTP Teacher Portal (T001001):");
        System.out.println("   - Go to 'Encode Grades' → See ALL students from database");
        System.out.println("   - Go to 'Attendance' → See ALL students from database");
        System.out.println("   - Go to 'Class Checklist' → See ALL students from database");
        System.out.println("   - Edit grades/attendance/checklist for any student");
        System.out.println();
        System.out.println("2. IT 211 Teacher Portal (T001005):");
        System.out.println("   - Go to 'Encode Grades' → See ALL students from database");
        System.out.println("   - Go to 'Attendance' → See ALL students from database");
        System.out.println("   - Go to 'Class Checklist' → See ALL students from database");
        System.out.println("   - Edit grades/attendance/checklist for any student");
        System.out.println();
        System.out.println("3. Student Portal (2251234):");
        System.out.println("   - Go to 'Grades' → See grades only when encoded by teachers");
        System.out.println("   - Go to 'Attendance' → See attendance only when recorded by teachers");
        System.out.println("   - Go to 'Class Checklist' → See checklist only when updated by teachers");
        System.out.println();
        System.out.println("4. Test Real-Time Synchronization:");
        System.out.println("   - In NSTP Teacher Portal: Encode a grade for student 2251234");
        System.out.println("   - In Student Portal: Go to 'Grades' → See the grade appear");
        System.out.println("   - In IT 211 Teacher Portal: Record attendance for student 2251234");
        System.out.println("   - In Student Portal: Go to 'Attendance' → See the attendance record");
        System.out.println();
        System.out.println("5. Test Different Teachers:");
        System.out.println("   - T001002 (Dr. Maria Garcia - GSTS)");
        System.out.println("   - T001006 (Dr. Lisa Davis - IT 212)");
        System.out.println("   - T001008 (Dr. Jennifer Taylor - IT 213)");
        System.out.println("   - All passwords: teacher123");
        System.out.println();
        System.out.println("6. Key Points:");
        System.out.println("   - Each teacher sees ALL students in their subject management");
        System.out.println("   - Each teacher can only edit their assigned subject");
        System.out.println("   - Students see data only when teachers input it");
        System.out.println("   - Real-time synchronization between teacher and student portals");
    }
}
