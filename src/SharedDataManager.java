import java.io.*;
import java.util.*;

/**
 * SharedDataManager - Manages data synchronization between Teacher and Student Portals
 * This class ensures that when teachers make changes, students can see them in real-time
 * 
 * AUTHOR: Group Project Implementation
 * SUBJECT: DATA STRUCTURE IT212 9458
 */
public class SharedDataManager {
    
    // File paths for shared data
    private static final String GRADES_FILE = "shared_grades.txt";
    private static final String ATTENDANCE_FILE = "shared_attendance.txt";
    private static final String CLASS_CHECKLIST_FILE = "shared_class_checklist.txt";
    private static final String ANNOUNCEMENTS_FILE = "shared_announcements.txt";
    
    // Data structures using our custom LinkedList implementations
    private static MyDoublyLinkedList<GradeRecord> gradeRecords = new MyDoublyLinkedList<>();
    private static MyDoublyLinkedList<AttendanceRecord> attendanceRecords = new MyDoublyLinkedList<>();
    private static MyDoublyLinkedList<ClassChecklistItem> classChecklistItems = new MyDoublyLinkedList<>();
    private static MyDoublyLinkedList<Announcement> announcements = new MyDoublyLinkedList<>();
    
    // Cache for fast lookups
    private static Map<String, MySinglyLinkedList<GradeRecord>> studentGradesCache = new HashMap<>();
    private static Map<String, MySinglyLinkedList<AttendanceRecord>> studentAttendanceCache = new HashMap<>();
    
    /**
     * Initialize the shared data manager and load existing data
     */
    public static void initialize() {
        loadAllData();
        System.out.println("SharedDataManager initialized with " + gradeRecords.size() + " grade records, " + 
                          attendanceRecords.size() + " attendance records, " + 
                          classChecklistItems.size() + " checklist items, " + 
                          announcements.size() + " announcements");
    }
    
    // ========== GRADE MANAGEMENT ==========
    
    /**
     * Add or update a grade record (called by teacher)
     */
    public static boolean saveGradeRecord(String studentID, String subjectCode, String subjectName, 
                                        double prelimGrade, double midtermGrade, double finalGrade) {
        try {
            // Create or update grade record
            GradeRecord gradeRecord = new GradeRecord(studentID, subjectCode, subjectName, 
                                                    prelimGrade, midtermGrade, finalGrade);
            
            // Check if record already exists
            boolean updated = false;
            for (int i = 0; i < gradeRecords.size(); i++) {
                GradeRecord existing = gradeRecords.get(i);
                if (existing.getStudentID().equals(studentID) && 
                    existing.getSubjectCode().equals(subjectCode)) {
                    gradeRecords.set(i, gradeRecord);
                    updated = true;
                    break;
                }
            }
            
            if (!updated) {
                gradeRecords.add(gradeRecord);
            }
            
            // Update cache
            updateStudentGradesCache(studentID);
            
            // Save to file
            saveGradesToFile();
            
            System.out.println("Grade record saved for student " + studentID + " in " + subjectCode);
            return true;
            
        } catch (Exception e) {
            System.err.println("Error saving grade record: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get grade records for a specific student (called by student)
     */
    public static MySinglyLinkedList<GradeRecord> getStudentGrades(String studentID) {
        if (studentGradesCache.containsKey(studentID)) {
            return studentGradesCache.get(studentID);
        }
        
        MySinglyLinkedList<GradeRecord> studentGrades = new MySinglyLinkedList<>();
        for (int i = 0; i < gradeRecords.size(); i++) {
            GradeRecord record = gradeRecords.get(i);
            if (record.getStudentID().equals(studentID)) {
                studentGrades.add(record);
            }
        }
        
        studentGradesCache.put(studentID, studentGrades);
        return studentGrades;
    }
    
    /**
     * Get all grade records for a specific subject (called by teacher)
     */
    public static MySinglyLinkedList<GradeRecord> getSubjectGrades(String subjectCode) {
        MySinglyLinkedList<GradeRecord> subjectGrades = new MySinglyLinkedList<>();
        for (int i = 0; i < gradeRecords.size(); i++) {
            GradeRecord record = gradeRecords.get(i);
            if (record.getSubjectCode().equals(subjectCode)) {
                subjectGrades.add(record);
            }
        }
        return subjectGrades;
    }
    
    // ========== ATTENDANCE MANAGEMENT ==========
    
    /**
     * Record attendance (called by teacher)
     */
    public static boolean recordAttendance(String studentID, String subjectCode, String date, 
                                         String status, String remarks) {
        try {
            AttendanceRecord attendanceRecord = new AttendanceRecord(studentID, subjectCode, date, status, remarks);
            attendanceRecords.add(attendanceRecord);
            
            // Update cache
            updateStudentAttendanceCache(studentID);
            
            // Save to file
            saveAttendanceToFile();
            
            System.out.println("Attendance recorded for student " + studentID + " in " + subjectCode + " on " + date);
            return true;
            
        } catch (Exception e) {
            System.err.println("Error recording attendance: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get attendance records for a specific student (called by student)
     */
    public static MySinglyLinkedList<AttendanceRecord> getStudentAttendance(String studentID) {
        if (studentAttendanceCache.containsKey(studentID)) {
            return studentAttendanceCache.get(studentID);
        }
        
        MySinglyLinkedList<AttendanceRecord> studentAttendance = new MySinglyLinkedList<>();
        for (int i = 0; i < attendanceRecords.size(); i++) {
            AttendanceRecord record = attendanceRecords.get(i);
            if (record.getStudentID().equals(studentID)) {
                studentAttendance.add(record);
            }
        }
        
        studentAttendanceCache.put(studentID, studentAttendance);
        return studentAttendance;
    }
    
    /**
     * Get attendance records for a specific subject (called by teacher)
     */
    public static MySinglyLinkedList<AttendanceRecord> getSubjectAttendance(String subjectCode) {
        MySinglyLinkedList<AttendanceRecord> subjectAttendance = new MySinglyLinkedList<>();
        for (int i = 0; i < attendanceRecords.size(); i++) {
            AttendanceRecord record = attendanceRecords.get(i);
            if (record.getSubjectCode().equals(subjectCode)) {
                subjectAttendance.add(record);
            }
        }
        return subjectAttendance;
    }
    
    // ========== CLASS CHECKLIST MANAGEMENT ==========
    
    /**
     * Update class checklist item (called by teacher)
     */
    public static boolean updateClassChecklist(String studentID, String subjectCode, String requirement, 
                                             String status, String remarks) {
        try {
            ClassChecklistItem checklistItem = new ClassChecklistItem(studentID, subjectCode, requirement, status, remarks);
            
            // Check if item already exists
            boolean updated = false;
            for (int i = 0; i < classChecklistItems.size(); i++) {
                ClassChecklistItem existing = classChecklistItems.get(i);
                if (existing.getStudentID().equals(studentID) && 
                    existing.getSubjectCode().equals(subjectCode) && 
                    existing.getRequirement().equals(requirement)) {
                    classChecklistItems.set(i, checklistItem);
                    updated = true;
                    break;
                }
            }
            
            if (!updated) {
                classChecklistItems.add(checklistItem);
            }
            
            // Save to file
            saveClassChecklistToFile();
            
            System.out.println("Class checklist updated for student " + studentID + " in " + subjectCode);
            return true;
            
        } catch (Exception e) {
            System.err.println("Error updating class checklist: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get class checklist for a specific student (called by student)
     */
    public static MySinglyLinkedList<ClassChecklistItem> getStudentClassChecklist(String studentID) {
        MySinglyLinkedList<ClassChecklistItem> studentChecklist = new MySinglyLinkedList<>();
        for (int i = 0; i < classChecklistItems.size(); i++) {
            ClassChecklistItem item = classChecklistItems.get(i);
            if (item.getStudentID().equals(studentID)) {
                studentChecklist.add(item);
            }
        }
        return studentChecklist;
    }
    
    /**
     * Get class checklist for a specific subject (called by teacher)
     */
    public static MySinglyLinkedList<ClassChecklistItem> getSubjectClassChecklist(String subjectCode) {
        MySinglyLinkedList<ClassChecklistItem> subjectChecklist = new MySinglyLinkedList<>();
        for (int i = 0; i < classChecklistItems.size(); i++) {
            ClassChecklistItem item = classChecklistItems.get(i);
            if (item.getSubjectCode().equals(subjectCode)) {
                subjectChecklist.add(item);
            }
        }
        return subjectChecklist;
    }
    
    // ========== ANNOUNCEMENT MANAGEMENT ==========
    
    /**
     * Add announcement (called by teacher)
     */
    public static boolean addAnnouncement(String title, String content, String author, String targetAudience) {
        try {
            Announcement announcement = new Announcement(title, content, author, targetAudience, "Medium");
            announcements.add(announcement);
            
            // Save to file
            saveAnnouncementsToFile();
            
            System.out.println("Announcement added: " + title);
            return true;
            
        } catch (Exception e) {
            System.err.println("Error adding announcement: " + e.getMessage());
            return false;
        }
    }
    
    public static boolean addAnnouncement(String title, String content, String author, String targetAudience, String priority) {
        try {
            Announcement announcement = new Announcement(title, content, author, targetAudience, priority);
            announcements.add(announcement);
            
            // Save to file
            saveAnnouncementsToFile();
            
            System.out.println("Announcement added: " + title);
            return true;
            
        } catch (Exception e) {
            System.err.println("Error adding announcement: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get announcements for students
     */
    public static MySinglyLinkedList<Announcement> getStudentAnnouncements() {
        MySinglyLinkedList<Announcement> studentAnnouncements = new MySinglyLinkedList<>();
        for (int i = 0; i < announcements.size(); i++) {
            Announcement announcement = announcements.get(i);
            if (announcement.getTargetAudience().equals("Students") || 
                announcement.getTargetAudience().equals("All")) {
                studentAnnouncements.add(announcement);
            }
        }
        return studentAnnouncements;
    }
    
    /**
     * Get announcements for teachers
     */
    public static MySinglyLinkedList<Announcement> getTeacherAnnouncements() {
        MySinglyLinkedList<Announcement> teacherAnnouncements = new MySinglyLinkedList<>();
        for (int i = 0; i < announcements.size(); i++) {
            Announcement announcement = announcements.get(i);
            if (announcement.getTargetAudience().equals("Teachers") || 
                announcement.getTargetAudience().equals("All")) {
                teacherAnnouncements.add(announcement);
            }
        }
        return teacherAnnouncements;
    }
    
    /**
     * Get all announcements (for teacher portal management)
     */
    public static MySinglyLinkedList<Announcement> getAllAnnouncements() {
        MySinglyLinkedList<Announcement> allAnnouncements = new MySinglyLinkedList<>();
        for (int i = 0; i < announcements.size(); i++) {
            allAnnouncements.add(announcements.get(i));
        }
        return allAnnouncements;
    }
    
    // ========== FILE OPERATIONS ==========
    
    private static void loadAllData() {
        loadGradesFromFile();
        loadAttendanceFromFile();
        loadClassChecklistFromFile();
        loadAnnouncementsFromFile();
    }
    
    private static void loadGradesFromFile() {
        try {
            File file = new File(GRADES_FILE);
            if (!file.exists()) {
                // Create sample data
                createSampleGradeData();
                return;
            }
            
            gradeRecords.clear();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = line.split("\\|");
                    if (parts.length >= 6) {
                        GradeRecord record = new GradeRecord(parts[0], parts[1], parts[2], 
                                                           Double.parseDouble(parts[3]), 
                                                           Double.parseDouble(parts[4]), 
                                                           Double.parseDouble(parts[5]));
                        gradeRecords.add(record);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading grades: " + e.getMessage());
        }
    }
    
    private static void saveGradesToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(GRADES_FILE))) {
            for (int i = 0; i < gradeRecords.size(); i++) {
                GradeRecord record = gradeRecords.get(i);
                writer.write(record.getStudentID() + "|" + record.getSubjectCode() + "|" + 
                           record.getSubjectName() + "|" + record.getPrelimGrade() + "|" + 
                           record.getMidtermGrade() + "|" + record.getFinalGrade());
                writer.newLine();
            }
        } catch (Exception e) {
            System.err.println("Error saving grades: " + e.getMessage());
        }
    }
    
    private static void loadAttendanceFromFile() {
        try {
            File file = new File(ATTENDANCE_FILE);
            if (!file.exists()) {
                createSampleAttendanceData();
                return;
            }
            
            attendanceRecords.clear();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = line.split("\\|");
                    if (parts.length >= 5) {
                        AttendanceRecord record = new AttendanceRecord(parts[0], parts[1], parts[2], parts[3], parts[4]);
                        attendanceRecords.add(record);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading attendance: " + e.getMessage());
        }
    }
    
    private static void saveAttendanceToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ATTENDANCE_FILE))) {
            for (int i = 0; i < attendanceRecords.size(); i++) {
                AttendanceRecord record = attendanceRecords.get(i);
                writer.write(record.getStudentID() + "|" + record.getSubjectCode() + "|" + 
                           record.getDateString() + "|" + record.getStatus() + "|" + record.getRemarks());
                writer.newLine();
            }
        } catch (Exception e) {
            System.err.println("Error saving attendance: " + e.getMessage());
        }
    }
    
    private static void loadClassChecklistFromFile() {
        try {
            File file = new File(CLASS_CHECKLIST_FILE);
            if (!file.exists()) {
                createSampleClassChecklistData();
                return;
            }
            
            classChecklistItems.clear();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = line.split("\\|");
                    if (parts.length >= 5) {
                        ClassChecklistItem item = new ClassChecklistItem(parts[0], parts[1], parts[2], parts[3], parts[4]);
                        classChecklistItems.add(item);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading class checklist: " + e.getMessage());
        }
    }
    
    private static void saveClassChecklistToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CLASS_CHECKLIST_FILE))) {
            for (int i = 0; i < classChecklistItems.size(); i++) {
                ClassChecklistItem item = classChecklistItems.get(i);
                writer.write(item.getStudentID() + "|" + item.getSubjectCode() + "|" + 
                           item.getRequirement() + "|" + item.getStatus() + "|" + item.getRemarks());
                writer.newLine();
            }
        } catch (Exception e) {
            System.err.println("Error saving class checklist: " + e.getMessage());
        }
    }
    
    private static void loadAnnouncementsFromFile() {
        try {
            File file = new File(ANNOUNCEMENTS_FILE);
            if (!file.exists()) {
                createSampleAnnouncementData();
                return;
            }
            
            announcements.clear();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = line.split("\\|");
                    if (parts.length >= 4) {
                        try {
                            // Try to create announcement with all available fields
                            String title = parts[0];
                            String content = parts[1];
                            String author = parts[2];
                            String targetAudience = parts[3];
                            String priority = "Medium"; // Default priority
                            String dateCreated = null;
                            
                            // Check if we have priority field
                            if (parts.length > 4 && !parts[4].trim().isEmpty()) {
                                priority = parts[4];
                            }
                            
                            // Check if we have date field
                            if (parts.length > 5 && !parts[5].trim().isEmpty()) {
                                dateCreated = parts[5];
                            }
                            
                            // Create announcement with priority
                            Announcement announcement = new Announcement(title, content, author, targetAudience, priority);
                            
                            // Set date if available
                            if (dateCreated != null) {
                                announcement.setDateCreated(dateCreated);
                            }
                            
                            announcements.add(announcement);
                            System.out.println("Loaded announcement: " + title + " | " + targetAudience + " | " + priority);
                            
                        } catch (Exception e) {
                            System.err.println("Error parsing announcement line: " + line);
                            System.err.println("Error: " + e.getMessage());
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading announcements: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void saveAnnouncementsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ANNOUNCEMENTS_FILE))) {
            for (int i = 0; i < announcements.size(); i++) {
                Announcement announcement = announcements.get(i);
                writer.write(announcement.getTitle() + "|" + announcement.getContent() + "|" + 
                           announcement.getAuthor() + "|" + announcement.getTargetAudience() + "|" + 
                           announcement.getPriority() + "|" + announcement.getDateCreated());
                writer.newLine();
            }
        } catch (Exception e) {
            System.err.println("Error saving announcements: " + e.getMessage());
        }
    }
    
    // ========== SAMPLE DATA CREATION ==========
    
    private static void createSampleGradeData() {
        // No sample grade data - grades will only appear when encoded by teachers
        System.out.println("No sample grade data created - grades will only appear when encoded by teachers");
    }
    
    private static void createSampleAttendanceData() {
        // No sample attendance data - attendance will only appear when recorded by teachers
        System.out.println("No sample attendance data created - attendance will only appear when recorded by teachers");
    }
    
    private static void createSampleClassChecklistData() {
        // No sample class checklist data - checklist will only appear when updated by teachers
        System.out.println("No sample class checklist data created - checklist will only appear when updated by teachers");
    }
    
    private static void createSampleAnnouncementData() {
        // Sample announcements
        addAnnouncement("Midterm Exam Schedule", "Midterm exams will be held from March 15-20, 2025.", "Dr. Smith", "Students");
        addAnnouncement("Project Submission", "Final projects are due on March 25, 2025.", "Dr. Johnson", "Students");
        addAnnouncement("Faculty Meeting", "Monthly faculty meeting on March 30, 2025.", "Dean's Office", "Teachers");
    }
    
    // ========== CACHE MANAGEMENT ==========
    
    private static void updateStudentGradesCache(String studentID) {
        MySinglyLinkedList<GradeRecord> studentGrades = new MySinglyLinkedList<>();
        for (int i = 0; i < gradeRecords.size(); i++) {
            GradeRecord record = gradeRecords.get(i);
            if (record.getStudentID().equals(studentID)) {
                studentGrades.add(record);
            }
        }
        studentGradesCache.put(studentID, studentGrades);
    }
    
    private static void updateStudentAttendanceCache(String studentID) {
        MySinglyLinkedList<AttendanceRecord> studentAttendance = new MySinglyLinkedList<>();
        for (int i = 0; i < attendanceRecords.size(); i++) {
            AttendanceRecord record = attendanceRecords.get(i);
            if (record.getStudentID().equals(studentID)) {
                studentAttendance.add(record);
            }
        }
        studentAttendanceCache.put(studentID, studentAttendance);
    }
    
    // ========== UTILITY METHODS ==========
    
    /**
     * Get statistics for a teacher's class
     */
    public static Map<String, Object> getClassStatistics(String subjectCode) {
        Map<String, Object> stats = new HashMap<>();
        
        MySinglyLinkedList<GradeRecord> subjectGrades = getSubjectGrades(subjectCode);
        MySinglyLinkedList<AttendanceRecord> subjectAttendance = getSubjectAttendance(subjectCode);
        
        stats.put("totalStudents", subjectGrades.size());
        stats.put("averageGrade", calculateAverageGrade(subjectGrades));
        stats.put("attendanceRate", calculateAttendanceRate(subjectAttendance));
        
        return stats;
    }
    
    private static double calculateAverageGrade(MySinglyLinkedList<GradeRecord> grades) {
        if (grades.size() == 0) return 0.0;
        
        double total = 0.0;
        for (int i = 0; i < grades.size(); i++) {
            GradeRecord record = grades.get(i);
            total += record.getFinalGrade();
        }
        return total / grades.size();
    }
    
    private static double calculateAttendanceRate(MySinglyLinkedList<AttendanceRecord> attendance) {
        if (attendance.size() == 0) return 0.0;
        
        int present = 0;
        for (int i = 0; i < attendance.size(); i++) {
            AttendanceRecord record = attendance.get(i);
            if (record.getStatus().equals("Present")) {
                present++;
            }
        }
        return (double) present / attendance.size() * 100.0;
    }
}
