import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * FeeDatabase - Manages fees in a database file
 * Provides real-time fee management with automatic updates
 */
public class FeeDatabase {
    
    private static final String FEES_DB_FILE = "feesDatabase.txt";
    private static final String STUDENT_FEES_DB_FILE = "studentFeesDatabase.txt";
    private static Map<String, FeeBreakdown> globalFees = new ConcurrentHashMap<>();
    private static Map<String, List<FeeBreakdown>> studentSpecificFees = new ConcurrentHashMap<>();
    private static List<FeeUpdateListener> listeners = new ArrayList<>();
    
    static {
        loadFees();
    }
    
    /**
     * Interface for listening to fee updates
     */
    public interface FeeUpdateListener {
        void onFeesUpdated(String studentID);
    }
    
    /**
     * Add a listener for fee updates
     */
    public static void addFeeUpdateListener(FeeUpdateListener listener) {
        listeners.add(listener);
    }
    
    /**
     * Remove a listener
     */
    public static void removeFeeUpdateListener(FeeUpdateListener listener) {
        listeners.remove(listener);
    }
    
    /**
     * Notify all listeners of fee updates
     */
    private static void notifyFeeUpdate(String studentID) {
        for (FeeUpdateListener listener : listeners) {
            try {
                listener.onFeesUpdated(studentID);
            } catch (Exception e) {
                System.err.println("Error notifying fee update listener: " + e.getMessage());
            }
        }
    }
    
    /**
     * Get all global fees (applied to all students)
     */
    public static List<FeeBreakdown> getGlobalFees() {
        return new ArrayList<>(globalFees.values());
    }
    
    /**
     * Get student-specific fees
     */
    public static List<FeeBreakdown> getStudentSpecificFees(String studentID) {
        return studentSpecificFees.getOrDefault(studentID, new ArrayList<>());
    }
    
    /**
     * Get all fees for a specific student (global + student-specific)
     */
    public static List<FeeBreakdown> getAllFeesForStudent(String studentID) {
        List<FeeBreakdown> allFees = new ArrayList<>();
        
        // Add global fees
        allFees.addAll(globalFees.values());
        
        // Add student-specific fees
        allFees.addAll(getStudentSpecificFees(studentID));
        
        return allFees;
    }
    
    /**
     * Add a global fee (applies to all students)
     */
    public static void addGlobalFee(FeeBreakdown fee) {
        globalFees.put(fee.getCode(), fee);
        saveFees();
        notifyFeeUpdate("ALL"); // Notify all students
    }
    
    /**
     * Add a student-specific fee
     */
    public static void addStudentFee(String studentID, FeeBreakdown fee) {
        studentSpecificFees.computeIfAbsent(studentID, k -> new ArrayList<>()).add(fee);
        saveFees();
        notifyFeeUpdate(studentID);
    }
    
    /**
     * Update a global fee
     */
    public static void updateGlobalFee(String feeCode, FeeBreakdown updatedFee) {
        if (globalFees.containsKey(feeCode)) {
            globalFees.put(feeCode, updatedFee);
            saveFees();
            notifyFeeUpdate("ALL");
        }
    }
    
    /**
     * Update a student-specific fee
     */
    public static void updateStudentFee(String studentID, String feeCode, FeeBreakdown updatedFee) {
        List<FeeBreakdown> studentFees = studentSpecificFees.get(studentID);
        if (studentFees != null) {
            for (int i = 0; i < studentFees.size(); i++) {
                if (studentFees.get(i).getCode().equals(feeCode)) {
                    studentFees.set(i, updatedFee);
                    saveFees();
                    notifyFeeUpdate(studentID);
                    return;
                }
            }
        }
    }
    
    /**
     * Remove a global fee
     */
    public static void removeGlobalFee(String feeCode) {
        if (globalFees.remove(feeCode) != null) {
            saveFees();
            notifyFeeUpdate("ALL");
        }
    }
    
    /**
     * Remove a student-specific fee
     */
    public static void removeStudentFee(String studentID, String feeCode) {
        List<FeeBreakdown> studentFees = studentSpecificFees.get(studentID);
        if (studentFees != null) {
            studentFees.removeIf(fee -> fee.getCode().equals(feeCode));
            saveFees();
            notifyFeeUpdate(studentID);
        }
    }
    
    /**
     * Get a specific fee by code
     */
    public static FeeBreakdown getFeeByCode(String feeCode) {
        return globalFees.get(feeCode);
    }
    
    /**
     * Get a student-specific fee by code
     */
    public static FeeBreakdown getStudentFeeByCode(String studentID, String feeCode) {
        List<FeeBreakdown> studentFees = studentSpecificFees.get(studentID);
        if (studentFees != null) {
            for (FeeBreakdown fee : studentFees) {
                if (fee.getCode().equals(feeCode)) {
                    return fee;
                }
            }
        }
        return null;
    }
    
    /**
     * Load fees from database files
     */
    private static void loadFees() {
        loadGlobalFees();
        loadStudentSpecificFees();
        
        // If no global fees exist, create default ones
        if (globalFees.isEmpty()) {
            createDefaultGlobalFees();
        }
    }
    
    /**
     * Load global fees from file
     */
    private static void loadGlobalFees() {
        File file = new File(FEES_DB_FILE);
        if (!file.exists()) {
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) continue;
                
                FeeBreakdown fee = FeeBreakdown.fromCSV(line);
                if (fee != null) {
                    globalFees.put(fee.getCode(), fee);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading global fees: " + e.getMessage());
        }
    }
    
    /**
     * Load student-specific fees from file
     */
    private static void loadStudentSpecificFees() {
        File file = new File(STUDENT_FEES_DB_FILE);
        if (!file.exists()) {
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            String currentStudentID = null;
            
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                if (line.startsWith("STUDENT:")) {
                    currentStudentID = line.substring(8).trim();
                    studentSpecificFees.put(currentStudentID, new ArrayList<>());
                } else if (currentStudentID != null && line.startsWith("FEE:")) {
                    FeeBreakdown fee = FeeBreakdown.fromCSV(line.substring(4));
                    if (fee != null) {
                        studentSpecificFees.get(currentStudentID).add(fee);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading student-specific fees: " + e.getMessage());
        }
    }
    
    /**
     * Create default global fees if none exist
     */
    private static void createDefaultGlobalFees() {
        LocalDate now = LocalDate.now();
        
        // Base tuition and fees for IT students
        addGlobalFee(new FeeBreakdown("TF001", "Tuition Fee (21 units @ P1,500/unit)", 
                31500.00, FeeBreakdown.FeeType.TUITION, now));
        
        addGlobalFee(new FeeBreakdown("LF001", "Computer Laboratory Fee", 
                3500.00, FeeBreakdown.FeeType.LABORATORY, now));
        
        addGlobalFee(new FeeBreakdown("MF001", "Miscellaneous Fee", 
                2800.00, FeeBreakdown.FeeType.MISCELLANEOUS, now));
        
        addGlobalFee(new FeeBreakdown("RF001", "Registration Fee", 
                500.00, FeeBreakdown.FeeType.REGISTRATION, now));
        
        addGlobalFee(new FeeBreakdown("LB001", "Library Fee", 
                800.00, FeeBreakdown.FeeType.LIBRARY, now));
        
        addGlobalFee(new FeeBreakdown("AT001", "Athletic Fee", 
                500.00, FeeBreakdown.FeeType.ATHLETIC, now));
        
        addGlobalFee(new FeeBreakdown("MD001", "Medical/Dental Fee", 
                400.00, FeeBreakdown.FeeType.MEDICAL, now));
        
        addGlobalFee(new FeeBreakdown("GD001", "Guidance Fee", 
                300.00, FeeBreakdown.FeeType.GUIDANCE, now));
        
        addGlobalFee(new FeeBreakdown("PB001", "Student Publication Fee", 
                250.00, FeeBreakdown.FeeType.PUBLICATION, now));
        
        addGlobalFee(new FeeBreakdown("IN001", "Internet and Technology Fee", 
                1500.00, FeeBreakdown.FeeType.INTERNET, now));
        
        addGlobalFee(new FeeBreakdown("EN001", "Energy Fee", 
                1200.00, FeeBreakdown.FeeType.ENERGY, now));
        
        addGlobalFee(new FeeBreakdown("IS001", "Student Insurance", 
                350.00, FeeBreakdown.FeeType.INSURANCE, now));
        
        addGlobalFee(new FeeBreakdown("DV001", "Development Fund", 
                1000.00, FeeBreakdown.FeeType.DEVELOPMENT, now));
        
        addGlobalFee(new FeeBreakdown("CL001", "Cultural Activities Fee", 
                300.00, FeeBreakdown.FeeType.CULTURAL, now));
    }
    
    /**
     * Save fees to database files
     */
    private static void saveFees() {
        saveGlobalFees();
        saveStudentSpecificFees();
    }
    
    /**
     * Save global fees to file
     */
    private static void saveGlobalFees() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FEES_DB_FILE))) {
            writer.write("# Global Fees Database\n");
            writer.write("# Format: CODE,DESCRIPTION,AMOUNT,TYPE,DATE_POSTED,REMARKS\n");
            writer.write("# Last Updated: " + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "\n\n");
            
            for (FeeBreakdown fee : globalFees.values()) {
                writer.write(fee.toCSV() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error saving global fees: " + e.getMessage());
        }
    }
    
    /**
     * Save student-specific fees to file
     */
    private static void saveStudentSpecificFees() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STUDENT_FEES_DB_FILE))) {
            writer.write("# Student-Specific Fees Database\n");
            writer.write("# Format: STUDENT:ID followed by FEE:CODE,DESCRIPTION,AMOUNT,TYPE,DATE_POSTED,REMARKS\n");
            writer.write("# Last Updated: " + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "\n\n");
            
            for (Map.Entry<String, List<FeeBreakdown>> entry : studentSpecificFees.entrySet()) {
                String studentID = entry.getKey();
                List<FeeBreakdown> fees = entry.getValue();
                
                if (!fees.isEmpty()) {
                    writer.write("STUDENT:" + studentID + "\n");
                    for (FeeBreakdown fee : fees) {
                        writer.write("FEE:" + fee.toCSV() + "\n");
                    }
                    writer.write("\n");
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving student-specific fees: " + e.getMessage());
        }
    }
    
    /**
     * Get fee statistics
     */
    public static Map<String, Object> getFeeStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("globalFeesCount", globalFees.size());
        stats.put("studentsWithSpecificFees", studentSpecificFees.size());
        
        double totalGlobalAmount = globalFees.values().stream()
                .mapToDouble(FeeBreakdown::getAmount)
                .sum();
        stats.put("totalGlobalAmount", totalGlobalAmount);
        
        return stats;
    }
    
    /**
     * Clear all fees (for testing)
     */
    public static void clearAllFees() {
        globalFees.clear();
        studentSpecificFees.clear();
        saveFees();
        notifyFeeUpdate("ALL");
    }
    
    /**
     * Reload fees from database (for external changes)
     */
    public static void reloadFees() {
        globalFees.clear();
        studentSpecificFees.clear();
        loadFees();
        notifyFeeUpdate("ALL");
    }
}