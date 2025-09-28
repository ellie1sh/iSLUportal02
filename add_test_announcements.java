import java.util.*;

public class add_test_announcements {
    public static void main(String[] args) {
        System.out.println("Adding test announcements for Teachers and All...");
        
        // Initialize SharedDataManager
        SharedDataManager.initialize();
        
        // Add announcement for "All"
        boolean success1 = SharedDataManager.addAnnouncement(
            "Important Update for Everyone", 
            "This announcement is visible to all users - teachers and students.", 
            "Dr. Test Admin", 
            "All", 
            "High"
        );
        System.out.println("Added 'All' announcement: " + success1);
        
        // Add announcement for "Teachers"
        boolean success2 = SharedDataManager.addAnnouncement(
            "Faculty Meeting Reminder", 
            "Monthly faculty meeting scheduled for next week. Please prepare your reports.", 
            "Dean's Office", 
            "Teachers", 
            "Medium"
        );
        System.out.println("Added 'Teachers' announcement: " + success2);
        
        // Add another announcement for "All"
        boolean success3 = SharedDataManager.addAnnouncement(
            "System Maintenance Notice", 
            "The system will be under maintenance this weekend. Please save your work.", 
            "IT Department", 
            "All", 
            "High"
        );
        System.out.println("Added second 'All' announcement: " + success3);
        
        System.out.println("\nTest announcements added successfully!");
    }
}
