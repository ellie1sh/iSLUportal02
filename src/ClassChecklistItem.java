import java.util.Objects;

/**
 * ClassChecklistItem - Represents a class checklist item for tracking student requirements
 * This class is used to synchronize data between Teacher and Student portals
 * 
 * AUTHOR: Group Project Implementation
 * SUBJECT: DATA STRUCTURE IT212 9458
 */
public class ClassChecklistItem {
    private String studentID;
    private String subjectCode;
    private String requirement;
    private String status;
    private String remarks;
    private String dateUpdated;
    
    public ClassChecklistItem(String studentID, String subjectCode, String requirement, String status, String remarks) {
        this.studentID = studentID;
        this.subjectCode = subjectCode;
        this.requirement = requirement;
        this.status = status;
        this.remarks = remarks;
        this.dateUpdated = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
    
    // Getters and Setters
    public String getStudentID() { return studentID; }
    public void setStudentID(String studentID) { this.studentID = studentID; }
    
    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }
    
    public String getRequirement() { return requirement; }
    public void setRequirement(String requirement) { this.requirement = requirement; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { 
        this.status = status; 
        this.dateUpdated = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
    
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    
    public String getDateUpdated() { return dateUpdated; }
    
    @Override
    public String toString() {
        return String.format("%s - %s: %s (%s)", studentID, subjectCode, requirement, status);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ClassChecklistItem that = (ClassChecklistItem) obj;
        return studentID.equals(that.studentID) && 
               subjectCode.equals(that.subjectCode) && 
               requirement.equals(that.requirement);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(studentID, subjectCode, requirement);
    }
}
