import java.io.*;
import java.util.*;

/**
 * TeacherDataManager - Manages teacher authentication and data
 * This class handles teacher login, data retrieval, and subject assignments
 * 
 * AUTHOR: Group Project Implementation
 * SUBJECT: DATA STRUCTURE IT212 9458
 */
public class TeacherDataManager {
    
    private static final String TEACHER_DB_FILE = "TeacherDatabase.txt";
    private static final String TEACHER_PASSWORD_FILE = "TeacherPasswordID.txt";
    
    // Cache for teacher data
    private static MyDoublyLinkedList<TeacherInfo> teacherCache = new MyDoublyLinkedList<>();
    private static Map<String, String> teacherPasswords = new HashMap<>();
    
    /**
     * Initialize teacher data manager and load data
     */
    public static void initialize() {
        loadTeacherData();
        loadTeacherPasswords();
        System.out.println("TeacherDataManager initialized with " + teacherCache.size() + " teachers");
    }
    
    /**
     * Authenticate teacher login
     */
    public static boolean authenticateTeacher(String teacherID, String password) {
        if (teacherID == null || password == null) {
            return false;
        }
        
        // Check if teacher ID format is correct (T followed by 6 digits)
        if (!teacherID.matches("T\\d{6}")) {
            return false;
        }
        
        String storedPassword = teacherPasswords.get(teacherID);
        return storedPassword != null && storedPassword.equals(password);
    }
    
    /**
     * Get teacher information by ID
     */
    public static TeacherInfo getTeacherInfo(String teacherID) {
        for (int i = 0; i < teacherCache.size(); i++) {
            TeacherInfo teacher = teacherCache.get(i);
            if (teacher.getTeacherID().equals(teacherID)) {
                return teacher;
            }
        }
        return null;
    }
    
    /**
     * Get teacher name by ID
     */
    public static String getTeacherName(String teacherID) {
        TeacherInfo teacher = getTeacherInfo(teacherID);
        return teacher != null ? teacher.getTeacherName() : "Unknown Teacher";
    }
    
    /**
     * Get teacher's assigned subject code
     */
    public static String getTeacherSubjectCode(String teacherID) {
        TeacherInfo teacher = getTeacherInfo(teacherID);
        return teacher != null ? teacher.getSubjectCode() : "";
    }
    
    /**
     * Get teacher's assigned subject name
     */
    public static String getTeacherSubjectName(String teacherID) {
        TeacherInfo teacher = getTeacherInfo(teacherID);
        return teacher != null ? teacher.getSubjectName() : "";
    }
    
    /**
     * Get all teachers assigned to a specific subject
     */
    public static MySinglyLinkedList<TeacherInfo> getTeachersBySubject(String subjectCode) {
        MySinglyLinkedList<TeacherInfo> teachers = new MySinglyLinkedList<>();
        for (int i = 0; i < teacherCache.size(); i++) {
            TeacherInfo teacher = teacherCache.get(i);
            if (teacher.getSubjectCode().equals(subjectCode)) {
                teachers.add(teacher);
            }
        }
        return teachers;
    }
    
    /**
     * Get all teachers
     */
    public static MyDoublyLinkedList<TeacherInfo> getAllTeachers() {
        return teacherCache;
    }
    
    /**
     * Check if a teacher ID exists
     */
    public static boolean teacherExists(String teacherID) {
        return getTeacherInfo(teacherID) != null;
    }
    
    /**
     * Load teacher data from database file
     */
    private static void loadTeacherData() {
        teacherCache.clear();
        
        try {
            File file = new File(TEACHER_DB_FILE);
            if (!file.exists()) {
                System.err.println("Teacher database file not found: " + TEACHER_DB_FILE);
                return;
            }
            
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    
                    String[] parts = line.split(",");
                    if (parts.length >= 7) {
                        String teacherID = parts[0].trim();
                        String teacherName = parts[1].trim();
                        String department = parts[2].trim();
                        String position = parts[3].trim();
                        String subjectCode = parts[4].trim();
                        String subjectName = parts[5].trim();
                        
                        // Parse assigned classes
                        MySinglyLinkedList<String> assignedClasses = new MySinglyLinkedList<>();
                        for (int i = 6; i < parts.length; i++) {
                            assignedClasses.add(parts[i].trim());
                        }
                        
                        TeacherInfo teacher = new TeacherInfo(teacherID, teacherName, department, 
                                                           position, subjectCode, subjectName, assignedClasses);
                        teacherCache.add(teacher);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading teacher data: " + e.getMessage());
        }
    }
    
    /**
     * Load teacher passwords from password file
     */
    private static void loadTeacherPasswords() {
        teacherPasswords.clear();
        
        try {
            File file = new File(TEACHER_PASSWORD_FILE);
            if (!file.exists()) {
                System.err.println("Teacher password file not found: " + TEACHER_PASSWORD_FILE);
                return;
            }
            
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    
                    // Parse format: "ID: T001001, Password: teacher123, Name: Dr. John Smith, Subject: NSTP-CWTS 1"
                    if (line.contains("ID:") && line.contains("Password:")) {
                        String[] parts = line.split(",");
                        String teacherID = null;
                        String password = null;
                        
                        for (String part : parts) {
                            part = part.trim();
                            if (part.startsWith("ID:")) {
                                teacherID = part.substring(3).trim();
                            } else if (part.startsWith("Password:")) {
                                password = part.substring(9).trim();
                            }
                        }
                        
                        if (teacherID != null && password != null) {
                            teacherPasswords.put(teacherID, password);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading teacher passwords: " + e.getMessage());
        }
    }
    
    /**
     * Add a new teacher
     */
    public static boolean addTeacher(TeacherInfo teacher) {
        if (teacher == null || teacherExists(teacher.getTeacherID())) {
            return false;
        }
        
        teacherCache.add(teacher);
        return saveTeacherData();
    }
    
    /**
     * Update teacher information
     */
    public static boolean updateTeacher(TeacherInfo teacher) {
        if (teacher == null) {
            return false;
        }
        
        for (int i = 0; i < teacherCache.size(); i++) {
            TeacherInfo existing = teacherCache.get(i);
            if (existing.getTeacherID().equals(teacher.getTeacherID())) {
                teacherCache.set(i, teacher);
                return saveTeacherData();
            }
        }
        
        return false;
    }
    
    /**
     * Delete a teacher
     */
    public static boolean deleteTeacher(String teacherID) {
        for (int i = 0; i < teacherCache.size(); i++) {
            TeacherInfo teacher = teacherCache.get(i);
            if (teacher.getTeacherID().equals(teacherID)) {
                teacherCache.remove(i);
                teacherPasswords.remove(teacherID);
                return saveTeacherData() && saveTeacherPasswords();
            }
        }
        
        return false;
    }
    
    /**
     * Save teacher data to database file
     */
    private static boolean saveTeacherData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEACHER_DB_FILE))) {
            for (int i = 0; i < teacherCache.size(); i++) {
                TeacherInfo teacher = teacherCache.get(i);
                StringBuilder line = new StringBuilder();
                line.append(teacher.getTeacherID()).append(",");
                line.append(teacher.getTeacherName()).append(",");
                line.append(teacher.getDepartment()).append(",");
                line.append(teacher.getPosition()).append(",");
                line.append(teacher.getSubjectCode()).append(",");
                line.append(teacher.getSubjectName());
                
                // Add assigned classes
                MySinglyLinkedList<String> classes = teacher.getAssignedClasses();
                for (int j = 0; j < classes.size(); j++) {
                    line.append(",").append(classes.get(j));
                }
                
                writer.write(line.toString());
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error saving teacher data: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Save teacher passwords to password file
     */
    private static boolean saveTeacherPasswords() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEACHER_PASSWORD_FILE))) {
            for (int i = 0; i < teacherCache.size(); i++) {
                TeacherInfo teacher = teacherCache.get(i);
                String password = teacherPasswords.get(teacher.getTeacherID());
                if (password != null) {
                    writer.write("ID: " + teacher.getTeacherID() + 
                               ", Password: " + password + 
                               ", Name: " + teacher.getTeacherName() + 
                               ", Subject: " + teacher.getSubjectName());
                    writer.newLine();
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error saving teacher passwords: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get all subject codes
     */
    public static MySinglyLinkedList<String> getAllSubjectCodes() {
        MySinglyLinkedList<String> subjectCodes = new MySinglyLinkedList<>();
        for (int i = 0; i < teacherCache.size(); i++) {
            TeacherInfo teacher = teacherCache.get(i);
            String subjectCode = teacher.getSubjectCode();
            if (!subjectCodes.contains(subjectCode)) {
                subjectCodes.add(subjectCode);
            }
        }
        return subjectCodes;
    }
    
    /**
     * Get subject name by code
     */
    public static String getSubjectName(String subjectCode) {
        for (int i = 0; i < teacherCache.size(); i++) {
            TeacherInfo teacher = teacherCache.get(i);
            if (teacher.getSubjectCode().equals(subjectCode)) {
                return teacher.getSubjectName();
            }
        }
        return "";
    }
    
    /**
     * Print all teacher information (for debugging)
     */
    public static void printAllTeachers() {
        System.out.println("=== ALL TEACHERS ===");
        for (int i = 0; i < teacherCache.size(); i++) {
            TeacherInfo teacher = teacherCache.get(i);
            System.out.println(teacher.toString() + " - " + teacher.getSubjectCode() + " " + teacher.getSubjectName());
        }
    }
}
