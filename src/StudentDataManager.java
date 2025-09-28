import java.io.*;

/**
 * StudentDataManager - Manages student data loading and retrieval
 * This class loads all students from the database for teacher management
 * 
 * AUTHOR: Group Project Implementation
 * SUBJECT: DATA STRUCTURE IT212 9458
 */
public class StudentDataManager {
    
    private static final String STUDENT_DB_FILE = "Database.txt";
    private static MyDoublyLinkedList<StudentInfo> allStudents = new MyDoublyLinkedList<>();
    
    /**
     * Initialize and load all students from database
     */
    public static void initialize() {
        loadAllStudents();
        System.out.println("StudentDataManager initialized with " + allStudents.size() + " students");
    }
    
    /**
     * Get all students
     */
    public static MyDoublyLinkedList<StudentInfo> getAllStudents() {
        return allStudents;
    }
    
    /**
     * Get student by ID
     */
    public static StudentInfo getStudentById(String studentID) {
        for (int i = 0; i < allStudents.size(); i++) {
            StudentInfo student = allStudents.get(i);
            if (student.getStudentID().equals(studentID)) {
                return student;
            }
        }
        return null;
    }
    
    /**
     * Get student name by ID
     */
    public static String getStudentName(String studentID) {
        StudentInfo student = getStudentById(studentID);
        return student != null ? student.getFullName() : "Unknown Student";
    }
    
    
    /**
     * Load all students from database file
     */
    private static void loadAllStudents() {
        allStudents.clear();
        
        try {
            File file = new File(STUDENT_DB_FILE);
            if (!file.exists()) {
                System.err.println("Student database file not found: " + STUDENT_DB_FILE);
                return;
            }
            
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    
                    String[] parts = line.split(",");
                    if (parts.length >= 3) {
                        String studentID = parts[0].trim();
                        String lastName = parts[1].trim();
                        String firstName = parts.length > 2 ? parts[2].trim() : "";
                        String middleName = parts.length > 3 ? parts[3].trim() : "";
                        String dateOfBirth = parts.length > 4 ? parts[4].trim() : "";
                        String password = parts.length > 5 ? parts[5].split("\\|")[0].trim() : "";
                        
                        StudentInfo student = new StudentInfo(studentID, lastName, firstName, middleName, dateOfBirth, password);
                        allStudents.add(student);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading student data: " + e.getMessage());
        }
    }
    
    /**
     * Print all students (for debugging)
     */
    public static void printAllStudents() {
        System.out.println("=== ALL STUDENTS ===");
        for (int i = 0; i < allStudents.size(); i++) {
            StudentInfo student = allStudents.get(i);
            System.out.println(student.toString());
        }
    }
}
