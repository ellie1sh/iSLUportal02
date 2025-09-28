/**
 * InterconnectedPortalDemo - Demonstrates the interconnected Teacher-Student Portal system
 * This class shows how teacher actions automatically reflect in student portals
 * 
 * AUTHOR: Group Project Implementation
 * SUBJECT: DATA STRUCTURE IT212 9458
 */
public class InterconnectedPortalDemo {
    
    public static void main(String[] args) {
        System.out.println("=== INTERCONNECTED TEACHER-STUDENT PORTAL DEMONSTRATION ===");
        System.out.println("This demonstrates real-time data synchronization between portals\n");
        
        try {
            // Initialize the shared data system
            System.out.println("Step 1: Initializing Shared Data Manager...");
            SharedDataManager.initialize();
            System.out.println("✓ Shared data system initialized with sample data\n");
            
            // Demonstrate teacher actions
            System.out.println("Step 2: Simulating Teacher Actions...");
            demonstrateTeacherActions();
            
            // Demonstrate student data retrieval
            System.out.println("\nStep 3: Showing Student Data Retrieval...");
            demonstrateStudentDataRetrieval();
            
            // Demonstrate real-time synchronization
            System.out.println("\nStep 4: Demonstrating Real-Time Synchronization...");
            demonstrateRealTimeSync();
            
            // Launch portals for visual demonstration
            System.out.println("\nStep 5: Launching Portals for Visual Demonstration...");
            launchPortals();
            
            System.out.println("\n=== INTERCONNECTED PORTAL DEMONSTRATION COMPLETE ===");
            System.out.println("Key Features Demonstrated:");
            System.out.println("- Teacher actions automatically save to shared data");
            System.out.println("- Student portals read real-time data from shared system");
            System.out.println("- Three-level nested list structure maintained");
            System.out.println("- Complete CRUD operations with data persistence");
            System.out.println("- Professional GUI with interconnected functionality");
            
        } catch (Exception e) {
            System.err.println("❌ Demo failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Demonstrates teacher actions that affect student data
     */
    private static void demonstrateTeacherActions() {
        System.out.println("Teacher Actions:");
        System.out.println("===============");
        
        // Teacher records attendance
        System.out.println("📋 Teacher records attendance:");
        SharedDataManager.recordAttendance("2251234", "IT111", "2025-01-20", "Present", "");
        SharedDataManager.recordAttendance("2251235", "IT111", "2025-01-20", "Present", "");
        SharedDataManager.recordAttendance("2251236", "IT111", "2025-01-20", "Absent", "Sick");
        System.out.println("  ✓ Attendance recorded for 3 students");
        
        // Teacher encodes grades
        System.out.println("📝 Teacher encodes grades:");
        SharedDataManager.saveGradeRecord("2251234", "IT111", "Introduction to Computing", 85.0, 88.0, 90.0);
        SharedDataManager.saveGradeRecord("2251235", "IT111", "Introduction to Computing", 92.0, 95.0, 98.0);
        SharedDataManager.saveGradeRecord("2251236", "IT111", "Introduction to Computing", 78.0, 82.0, 85.0);
        System.out.println("  ✓ Grades encoded for 3 students");
        
        // Teacher updates class checklist
        System.out.println("✅ Teacher updates class checklist:");
        SharedDataManager.updateClassChecklist("2251234", "IT111", "Project 1", "Completed", "Excellent work");
        SharedDataManager.updateClassChecklist("2251235", "IT111", "Project 1", "Completed", "Good work");
        SharedDataManager.updateClassChecklist("2251236", "IT111", "Project 1", "Pending", "Not submitted");
        System.out.println("  ✓ Class checklist updated for 3 students");
        
        // Teacher posts announcement
        System.out.println("📢 Teacher posts announcement:");
        SharedDataManager.addAnnouncement("Midterm Exam Schedule", 
            "Midterm exams will be held from March 15-20, 2025. Please prepare accordingly.", 
            "Dr. Smith", "Students");
        System.out.println("  ✓ Announcement posted for students");
    }
    
    /**
     * Demonstrates how students can retrieve their data
     */
    private static void demonstrateStudentDataRetrieval() {
        System.out.println("Student Data Retrieval:");
        System.out.println("======================");
        
        String studentID = "2251234";
        System.out.println("Student ID: " + studentID);
        
        // Get student grades
        System.out.println("📊 Student grades:");
        MySinglyLinkedList<GradeRecord> grades = SharedDataManager.getStudentGrades(studentID);
        for (int i = 0; i < grades.size(); i++) {
            GradeRecord grade = grades.get(i);
            double average = (grade.getPrelimGrade() + grade.getMidtermGrade() + grade.getFinalGrade()) / 3.0;
            System.out.println("  " + grade.getSubjectCode() + " - " + grade.getSubjectName() + 
                             ": " + grade.getPrelimGrade() + ", " + grade.getMidtermGrade() + 
                             ", " + grade.getFinalGrade() + " (Avg: " + Math.round(average * 10.0) / 10.0 + ")");
        }
        
        // Get student attendance
        System.out.println("📋 Student attendance:");
        MySinglyLinkedList<AttendanceRecord> attendance = SharedDataManager.getStudentAttendance(studentID);
        for (int i = 0; i < attendance.size(); i++) {
            AttendanceRecord record = attendance.get(i);
            System.out.println("  " + record.getDate() + " - " + record.getSubjectCode() + 
                             ": " + record.getStatus() + " " + 
                             (record.getRemarks().isEmpty() ? "" : "(" + record.getRemarks() + ")"));
        }
        
        // Get student class checklist
        System.out.println("✅ Student class checklist:");
        MySinglyLinkedList<ClassChecklistItem> checklist = SharedDataManager.getStudentClassChecklist(studentID);
        for (int i = 0; i < checklist.size(); i++) {
            ClassChecklistItem item = checklist.get(i);
            System.out.println("  " + item.getSubjectCode() + " - " + item.getRequirement() + 
                             ": " + item.getStatus() + " " + 
                             (item.getRemarks().isEmpty() ? "" : "(" + item.getRemarks() + ")"));
        }
        
        // Get student announcements
        System.out.println("📢 Student announcements:");
        MySinglyLinkedList<Announcement> announcements = SharedDataManager.getStudentAnnouncements();
        for (int i = 0; i < announcements.size(); i++) {
            Announcement announcement = announcements.get(i);
            System.out.println("  " + announcement.getTitle() + " - " + announcement.getAuthor());
        }
    }
    
    /**
     * Demonstrates real-time synchronization
     */
    private static void demonstrateRealTimeSync() {
        System.out.println("Real-Time Synchronization:");
        System.out.println("=========================");
        
        String studentID = "2251234";
        
        // Show initial state
        System.out.println("Initial state - Student " + studentID + " grades:");
        MySinglyLinkedList<GradeRecord> initialGrades = SharedDataManager.getStudentGrades(studentID);
        for (int i = 0; i < initialGrades.size(); i++) {
            GradeRecord grade = initialGrades.get(i);
            System.out.println("  " + grade.getSubjectCode() + ": " + grade.getFinalGrade());
        }
        
        // Simulate teacher updating a grade
        System.out.println("\nTeacher updates grade for student " + studentID + "...");
        SharedDataManager.saveGradeRecord(studentID, "IT111", "Introduction to Computing", 85.0, 88.0, 95.0);
        
        // Show updated state
        System.out.println("Updated state - Student " + studentID + " grades:");
        MySinglyLinkedList<GradeRecord> updatedGrades = SharedDataManager.getStudentGrades(studentID);
        for (int i = 0; i < updatedGrades.size(); i++) {
            GradeRecord grade = updatedGrades.get(i);
            System.out.println("  " + grade.getSubjectCode() + ": " + grade.getFinalGrade());
        }
        
        System.out.println("✓ Real-time synchronization working - student data updated immediately!");
    }
    
    /**
     * Launches both portals for visual demonstration
     */
    private static void launchPortals() {
        System.out.println("Launching Teacher Portal...");
        javax.swing.SwingUtilities.invokeLater(() -> {
            ISLUTeacherPortal teacherPortal = new ISLUTeacherPortal("TCH001");
            teacherPortal.setVisible(true);
            System.out.println("✓ Teacher Portal launched");
        });
        
        // Wait a moment before launching student portal
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("Launching Student Portal...");
        javax.swing.SwingUtilities.invokeLater(() -> {
            ISLUStudentPortal studentPortal = new ISLUStudentPortal("2251234");
            studentPortal.setVisible(true);
            System.out.println("✓ Student Portal launched");
        });
        
        System.out.println("\nInstructions for Testing:");
        System.out.println("1. In Teacher Portal: Go to 'Encode Grades' and modify grades");
        System.out.println("2. In Teacher Portal: Go to 'Attendance' and update attendance");
        System.out.println("3. In Teacher Portal: Go to 'Class Checklist' and update requirements");
        System.out.println("4. In Student Portal: Go to 'Grades' to see updated grades");
        System.out.println("5. In Student Portal: Go to 'Attendance' to see updated attendance");
        System.out.println("6. In Student Portal: Go to 'Class Checklist' to see updated requirements");
        System.out.println("7. Changes in Teacher Portal will automatically reflect in Student Portal!");
    }
}
