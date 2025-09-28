import java.util.*;

public class test_announcements_debug {
    public static void main(String[] args) {
        System.out.println("Testing announcement loading...");
        
        // Initialize SharedDataManager
        SharedDataManager.initialize();
        
        // Get teacher announcements
        MySinglyLinkedList<Announcement> teacherAnnouncements = SharedDataManager.getTeacherAnnouncements();
        System.out.println("Teacher announcements count: " + teacherAnnouncements.size());
        
        for (int i = 0; i < teacherAnnouncements.size(); i++) {
            Announcement announcement = teacherAnnouncements.get(i);
            System.out.println("Announcement " + (i+1) + ":");
            System.out.println("  Title: " + announcement.getTitle());
            System.out.println("  Target Audience: " + announcement.getTargetAudience());
            System.out.println("  Priority: " + announcement.getPriority());
            System.out.println("  Author: " + announcement.getAuthor());
            System.out.println("  Date: " + announcement.getDateCreated());
            System.out.println();
        }
        
        // Get all announcements
        MySinglyLinkedList<Announcement> allAnnouncements = SharedDataManager.getAllAnnouncements();
        System.out.println("All announcements count: " + allAnnouncements.size());
        
        for (int i = 0; i < allAnnouncements.size(); i++) {
            Announcement announcement = allAnnouncements.get(i);
            System.out.println("All Announcement " + (i+1) + ":");
            System.out.println("  Title: " + announcement.getTitle());
            System.out.println("  Target Audience: " + announcement.getTargetAudience());
            System.out.println("  Priority: " + announcement.getPriority());
            System.out.println("  Author: " + announcement.getAuthor());
            System.out.println("  Date: " + announcement.getDateCreated());
            System.out.println();
        }
    }
}
