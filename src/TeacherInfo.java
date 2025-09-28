/**
 * TeacherInfo - Represents teacher information and their assigned subjects
 * This class manages teacher data and their class assignments
 * 
 * AUTHOR: Group Project Implementation
 * SUBJECT: DATA STRUCTURE IT212 9458
 */
public class TeacherInfo {
    private String teacherID;
    private String teacherName;
    private String department;
    private String position;
    private String subjectCode;
    private String subjectName;
    private MySinglyLinkedList<String> assignedClasses;
    
    public TeacherInfo(String teacherID, String teacherName, String department, 
                      String position, String subjectCode, String subjectName, 
                      MySinglyLinkedList<String> assignedClasses) {
        this.teacherID = teacherID;
        this.teacherName = teacherName;
        this.department = department;
        this.position = position;
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.assignedClasses = assignedClasses != null ? assignedClasses : new MySinglyLinkedList<>();
    }
    
    // Getters
    public String getTeacherID() { return teacherID; }
    public String getTeacherName() { return teacherName; }
    public String getDepartment() { return department; }
    public String getPosition() { return position; }
    public String getSubjectCode() { return subjectCode; }
    public String getSubjectName() { return subjectName; }
    public MySinglyLinkedList<String> getAssignedClasses() { return assignedClasses; }
    
    // Setters
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }
    public void setDepartment(String department) { this.department = department; }
    public void setPosition(String position) { this.position = position; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    public void setAssignedClasses(MySinglyLinkedList<String> assignedClasses) { 
        this.assignedClasses = assignedClasses; 
    }
    
    public void addAssignedClass(String className) {
        if (assignedClasses == null) {
            assignedClasses = new MySinglyLinkedList<>();
        }
        assignedClasses.add(className);
    }
    
    public boolean removeAssignedClass(String className) {
        if (assignedClasses == null) return false;
        int index = assignedClasses.indexOf(className);
        if (index != -1) {
            assignedClasses.remove(index);
            return true;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return String.format("%s - %s (%s)", teacherID, teacherName, subjectName);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TeacherInfo that = (TeacherInfo) obj;
        return teacherID.equals(that.teacherID);
    }
    
    @Override
    public int hashCode() {
        return teacherID.hashCode();
    }
}
