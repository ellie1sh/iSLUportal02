/**
 * TeacherDatabaseDemo - Demonstrates the teacher database system
 * This class shows how teacher accounts work with the new Txxxxxx format
 * 
 * AUTHOR: Group Project Implementation
 * SUBJECT: DATA STRUCTURE IT212 9458
 */
public class TeacherDatabaseDemo {
    
    public static void main(String[] args) {
        System.out.println("=== TEACHER DATABASE SYSTEM DEMONSTRATION ===");
        System.out.println("This demonstrates the teacher database with Txxxxxx format IDs\n");
        
        try {
            // Initialize teacher data manager
            System.out.println("Step 1: Initializing Teacher Data Manager...");
            TeacherDataManager.initialize();
            System.out.println("✓ Teacher data manager initialized\n");
            
            // Show all teachers
            System.out.println("Step 2: Displaying All Teachers and Their Subjects...");
            displayAllTeachers();
            
            // Demonstrate teacher authentication
            System.out.println("\nStep 3: Demonstrating Teacher Authentication...");
            demonstrateTeacherAuthentication();
            
            // Show subject assignments
            System.out.println("\nStep 4: Showing Subject Assignments...");
            demonstrateSubjectAssignments();
            
            // Launch portals for testing
            System.out.println("\nStep 5: Launching Portals for Testing...");
            launchPortalsForTesting();
            
            System.out.println("\n=== TEACHER DATABASE DEMONSTRATION COMPLETE ===");
            System.out.println("Key Features Demonstrated:");
            System.out.println("- Teacher IDs in Txxxxxx format (T + 6 digits)");
            System.out.println("- One teacher per subject/class code");
            System.out.println("- Proper authentication system");
            System.out.println("- Subject-specific data management");
            System.out.println("- Grades only appear when encoded by teachers");
            
        } catch (Exception e) {
            System.err.println("❌ Demo failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Display all teachers and their assigned subjects
     */
    private static void displayAllTeachers() {
        System.out.println("All Teachers and Their Subjects:");
        System.out.println("===============================");
        
        MyDoublyLinkedList<TeacherInfo> teachers = TeacherDataManager.getAllTeachers();
        for (int i = 0; i < teachers.size(); i++) {
            TeacherInfo teacher = teachers.get(i);
            System.out.println(String.format("%s - %s", teacher.getTeacherID(), teacher.getTeacherName()));
            System.out.println(String.format("  Subject: %s (%s)", teacher.getSubjectCode(), teacher.getSubjectName()));
            System.out.println(String.format("  Department: %s", teacher.getDepartment()));
            System.out.println(String.format("  Position: %s", teacher.getPosition()));
            
            MySinglyLinkedList<String> classes = teacher.getAssignedClasses();
            System.out.print("  Assigned Classes: ");
            for (int j = 0; j < classes.size(); j++) {
                System.out.print(classes.get(j));
                if (j < classes.size() - 1) System.out.print(", ");
            }
            System.out.println("\n");
        }
    }
    
    /**
     * Demonstrate teacher authentication
     */
    private static void demonstrateTeacherAuthentication() {
        System.out.println("Teacher Authentication Test:");
        System.out.println("============================");
        
        // Test valid teacher login
        String validTeacherID = "T001001";
        String validPassword = "teacher123";
        
        System.out.println("Testing valid teacher login:");
        System.out.println("Teacher ID: " + validTeacherID);
        System.out.println("Password: " + validPassword);
        
        boolean isValid = TeacherDataManager.authenticateTeacher(validTeacherID, validPassword);
        System.out.println("Authentication Result: " + (isValid ? "✓ SUCCESS" : "✗ FAILED"));
        
        if (isValid) {
            TeacherInfo teacher = TeacherDataManager.getTeacherInfo(validTeacherID);
            System.out.println("Teacher Name: " + teacher.getTeacherName());
            System.out.println("Assigned Subject: " + teacher.getSubjectCode() + " - " + teacher.getSubjectName());
        }
        
        // Test invalid teacher login
        System.out.println("\nTesting invalid teacher login:");
        String invalidTeacherID = "T999999";
        String invalidPassword = "wrongpassword";
        
        System.out.println("Teacher ID: " + invalidTeacherID);
        System.out.println("Password: " + invalidPassword);
        
        boolean isInvalid = TeacherDataManager.authenticateTeacher(invalidTeacherID, invalidPassword);
        System.out.println("Authentication Result: " + (isInvalid ? "✗ UNEXPECTED SUCCESS" : "✓ CORRECTLY FAILED"));
        
        // Test student ID format (should fail)
        System.out.println("\nTesting student ID format (should fail):");
        String studentID = "2251234";
        String studentPassword = "student123";
        
        System.out.println("ID: " + studentID);
        System.out.println("Password: " + studentPassword);
        
        boolean isStudentFormat = TeacherDataManager.authenticateTeacher(studentID, studentPassword);
        System.out.println("Authentication Result: " + (isStudentFormat ? "✗ UNEXPECTED SUCCESS" : "✓ CORRECTLY FAILED"));
    }
    
    /**
     * Demonstrate subject assignments
     */
    private static void demonstrateSubjectAssignments() {
        System.out.println("Subject Assignments:");
        System.out.println("===================");
        
        // Get all subject codes
        MySinglyLinkedList<String> subjectCodes = TeacherDataManager.getAllSubjectCodes();
        
        for (int i = 0; i < subjectCodes.size(); i++) {
            String subjectCode = subjectCodes.get(i);
            String subjectName = TeacherDataManager.getSubjectName(subjectCode);
            
            System.out.println(String.format("Subject: %s - %s", subjectCode, subjectName));
            
            // Get teachers assigned to this subject
            MySinglyLinkedList<TeacherInfo> teachers = TeacherDataManager.getTeachersBySubject(subjectCode);
            for (int j = 0; j < teachers.size(); j++) {
                TeacherInfo teacher = teachers.get(j);
                System.out.println(String.format("  Assigned Teacher: %s (%s)", 
                    teacher.getTeacherName(), teacher.getTeacherID()));
            }
            System.out.println();
        }
    }
    
    /**
     * Launch portals for testing
     */
    private static void launchPortalsForTesting() {
        System.out.println("Launching Teacher Portal (T001001 - Dr. John Smith - NSTP-CWTS 1)...");
        javax.swing.SwingUtilities.invokeLater(() -> {
            ISLUTeacherPortal teacherPortal = new ISLUTeacherPortal("T001001");
            teacherPortal.setVisible(true);
            System.out.println("✓ Teacher Portal launched for Dr. John Smith (NSTP-CWTS 1)");
        });
        
        // Wait a moment before launching student portal
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("Launching Student Portal (2251234)...");
        javax.swing.SwingUtilities.invokeLater(() -> {
            ISLUStudentPortal studentPortal = new ISLUStudentPortal("2251234");
            studentPortal.setVisible(true);
            System.out.println("✓ Student Portal launched");
        });
        
        System.out.println("\nTesting Instructions:");
        System.out.println("====================");
        System.out.println("1. In Teacher Portal (Dr. John Smith - NSTP-CWTS 1):");
        System.out.println("   - Go to 'Encode Grades' and add grades for students");
        System.out.println("   - Go to 'Attendance' and record attendance");
        System.out.println("   - Go to 'Class Checklist' and update requirements");
        System.out.println("2. In Student Portal:");
        System.out.println("   - Go to 'Grades' to see grades (initially shows 'Not Yet Submitted')");
        System.out.println("   - Go to 'Attendance' to see attendance records");
        System.out.println("   - Go to 'Class Checklist' to see requirement status");
        System.out.println("3. Test with different teachers:");
        System.out.println("   - T001002 (Dr. Maria Garcia - GSTS)");
        System.out.println("   - T001005 (Dr. Michael Brown - IT 211)");
        System.out.println("   - T001006 (Dr. Lisa Davis - IT 212)");
        System.out.println("4. All teacher passwords are: teacher123");
        System.out.println("5. Grades will only appear when encoded by the assigned teacher!");
    }
}
