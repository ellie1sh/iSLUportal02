import java.util.*;

public class test_add_announcements {
    public static void main(String[] args) {
        System.out.println("Adding test announcements...");
        
        // Initialize SharedDataManager
        SharedDataManager.initialize();
        
        // Add announcement for "All"
        boolean success1 = SharedDataManager.addAnnouncement(
            "Important Update for Everyone", 
            "This is an announcement for all users.", 
            "Dr. Test", 
            "All", 
            "High"
        );
        System.out.println("Added 'All' announcement: " + success1);
        
        // Add announcement for "Teachers"
        boolean success2 = SharedDataManager.addAnnouncement(
            "Faculty Meeting Reminder", 
            "Monthly faculty meeting scheduled for next week.", 
            "Dean's Office", 
            "Teachers", 
            "Medium"
        );
        System.out.println("Added 'Teachers' announcement: " + success2);
        
        // Test teacher announcements
        MySinglyLinkedList<Announcement> teacherAnnouncements = SharedDataManager.getTeacherAnnouncements();
        System.out.println("\nTeacher announcements count: " + teacherAnnouncements.size());
        
        for (int i = 0; i < teacherAnnouncements.size(); i++) {
            Announcement announcement = teacherAnnouncements.get(i);
            System.out.println("Teacher Announcement " + (i+1) + ":");
            System.out.println("  Title: " + announcement.getTitle());
            System.out.println("  Target Audience: " + announcement.getTargetAudience());
            System.out.println("  Priority: " + announcement.getPriority());
            System.out.println("  Author: " + announcement.getAuthor());
            System.out.println();
        }
    }
}
