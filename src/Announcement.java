import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Announcement - Represents an announcement that can be shared between Teacher and Student portals
 * This class is used to synchronize announcements between portals
 * 
 * AUTHOR: Group Project Implementation
 * SUBJECT: DATA STRUCTURE IT212 9458
 */
public class Announcement {
    private String title;
    private String content;
    private String author;
    private String targetAudience; // "Students", "Teachers", or "All"
    private String dateCreated;
    private String priority; // "High", "Medium", "Low"
    
    public Announcement(String title, String content, String author, String targetAudience) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.targetAudience = targetAudience;
        this.dateCreated = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.priority = "Medium";
    }
    
    public Announcement(String title, String content, String author, String targetAudience, String priority) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.targetAudience = targetAudience;
        this.priority = priority;
        this.dateCreated = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
    
    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    
    public String getTargetAudience() { return targetAudience; }
    public void setTargetAudience(String targetAudience) { this.targetAudience = targetAudience; }
    
    public String getDateCreated() { return dateCreated; }
    public void setDateCreated(String dateCreated) { this.dateCreated = dateCreated; }
    
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    
    @Override
    public String toString() {
        return String.format("[%s] %s - %s (%s)", priority, title, author, dateCreated);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Announcement that = (Announcement) obj;
        return title.equals(that.title) && 
               author.equals(that.author) && 
               dateCreated.equals(that.dateCreated);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(title, author, dateCreated);
    }
}
