/**
 * Data class to hold student information
 */
public class StudentInfo {
    private String id;
    private String lastName;
    private String firstName;
    private String middleName;
    private String dateOfBirth;
    private String password;

    public StudentInfo(String id, String lastName, String firstName, String middleName, String dateOfBirth, String password) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.dateOfBirth = dateOfBirth;
        this.password = password;
    }
    
    // Constructor for StudentDataManager compatibility
    public StudentInfo(String studentID, String studentName, String program) {
        this.id = studentID;
        this.lastName = studentName; // Store full name in lastName field
        this.firstName = "";
        this.middleName = "";
        this.dateOfBirth = "";
        this.password = "";
    }

    // Getters
    public String getId() { return id; }
    public String getStudentID() { return id; } // Alias for compatibility
    public String getLastName() { return lastName; }
    public String getFirstName() { return firstName; }
    public String getMiddleName() { return middleName; }
    public String getDateOfBirth() { return dateOfBirth; }
    public String getPassword() { return password; }
    public String getStudentName() { return getFullName(); } // Return full name
    public String getProgram() { return "BSIT"; } // Default program

    public String getFullName() {
        StringBuilder fullName = new StringBuilder();
        
        if (firstName != null && !firstName.trim().isEmpty()) {
            fullName.append(firstName.trim().toUpperCase());
        }
        
        if (middleName != null && !middleName.trim().isEmpty()) {
            if (fullName.length() > 0) fullName.append(" ");
            fullName.append(middleName.trim().toUpperCase());
        }
        
        if (lastName != null && !lastName.trim().isEmpty()) {
            if (fullName.length() > 0) fullName.append(" ");
            fullName.append(lastName.trim().toUpperCase());
        }
        
        return fullName.toString();
    }

    public String toDatabaseFormat() {
        return id + "," + lastName + "," + firstName + "," + middleName + "," + dateOfBirth + "," + password;
    }
}