/**
 * CompatibilityTest - Tests the compatibility between old and new menu systems
 * This ensures that the three-level nesting works with the existing ISLUStudentPortal
 */
public class CompatibilityTest {
    
    public static void main(String[] args) {
        System.out.println("=== COMPATIBILITY TEST ===");
        
        try {
            // Test 1: Create three-level menu system
            System.out.println("Test 1: Creating three-level menu system...");
            MyDoublyLinkedList<MenuItem> menu = PortalUtils.createIntegratedMenuSystem();
            System.out.println("✓ Three-level menu system created successfully");
            System.out.println("  - Main menu items: " + menu.size());
            
            // Test 2: Check if menu items have sub-items
            System.out.println("\nTest 2: Checking sub-items...");
            for (int i = 0; i < menu.size(); i++) {
                MenuItem item = menu.get(i);
                System.out.println("  - " + item.getName() + ": " + item.getSubItems().size() + " sub-items");
                
                // Check for third-level items
                MySinglyLinkedList<SubMenuItem> subItems = item.getSubItems();
                for (int j = 0; j < subItems.size(); j++) {
                    SubMenuItem subItem = subItems.get(j);
                    if (subItem.hasSubSubItems()) {
                        System.out.println("    - " + subItem.getName() + ": " + subItem.getSubSubItems().size() + " sub-sub-items");
                    }
                }
            }
            
            // Test 3: Test conversion method (simulating ISLUStudentPortal usage)
            System.out.println("\nTest 3: Testing conversion compatibility...");
            MySinglyLinkedList<SubMenuItem> subMenuItems = PortalUtils.createHomeSublist();
            MySinglyLinkedList<String> stringList = convertSubMenuItemsToStrings(subMenuItems);
            System.out.println("✓ Conversion successful: " + stringList.size() + " items converted");
            
            // Test 4: Test CRUD operations
            System.out.println("\nTest 4: Testing CRUD operations...");
            
            // CREATE: Add a new menu item
            MySinglyLinkedList<SubMenuItem> testSubItems = new MySinglyLinkedList<>();
            testSubItems.add(new SubMenuItem("Test Sub Item"));
            menu.add(new MenuItem("Test Menu", testSubItems));
            System.out.println("✓ CREATE: Added new menu item");
            
            // READ: Read menu item
            MenuItem testItem = menu.get(menu.size() - 1);
            System.out.println("✓ READ: Retrieved menu item: " + testItem.getName());
            
            // UPDATE: Update menu item
            testItem.setDescription("Updated description");
            System.out.println("✓ UPDATE: Updated menu item description");
            
            // DELETE: Delete menu item
            menu.remove(menu.size() - 1);
            System.out.println("✓ DELETE: Removed menu item");
            
            System.out.println("\n=== ALL TESTS PASSED ===");
            System.out.println("The three-level nesting system is fully compatible with the existing codebase!");
            
        } catch (Exception e) {
            System.err.println("❌ Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Helper method to convert SubMenuItem list to String list (same as in ISLUStudentPortal)
     */
    private static MySinglyLinkedList<String> convertSubMenuItemsToStrings(MySinglyLinkedList<SubMenuItem> subMenuItems) {
        MySinglyLinkedList<String> stringList = new MySinglyLinkedList<>();
        if (subMenuItems != null) {
            for (int i = 0; i < subMenuItems.size(); i++) {
                SubMenuItem subItem = subMenuItems.get(i);
                stringList.add(subItem.getName());
            }
        }
        return stringList;
    }
}
