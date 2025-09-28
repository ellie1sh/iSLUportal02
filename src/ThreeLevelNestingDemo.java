/**
 * ThreeLevelNestingDemo - Demonstrates the three-level nested list structure
 * This class shows how the menu system implements the IntelliJ IDEA menu structure
 * as required by the project specifications.
 * 
 * Structure:
 * Level 1: Main Menu Items (MyDoublyLinkedList<MenuItem>)
 * Level 2: Sub Items (MySinglyLinkedList<SubMenuItem>)
 * Level 3: Sub-Sub Items (MySinglyLinkedList<String> within SubMenuItem)
 * 
 * AUTHOR: Group Project Implementation
 * SUBJECT: DATA STRUCTURE IT212 9458
 */
public class ThreeLevelNestingDemo {
    
    public static void main(String[] args) {
        System.out.println("=== THREE-LEVEL NESTED LIST DEMONSTRATION ===");
        System.out.println("Following the IntelliJ IDEA menu structure example\n");
        
        // Create the three-level menu system
        MyDoublyLinkedList<MenuItem> menu = createThreeLevelMenuSystem();
        
        // Display the complete menu structure
        displayMenuStructure(menu);
        
        // Demonstrate CRUD operations
        demonstrateCRUDOperations(menu);
        
        // Demonstrate search operations
        demonstrateSearchOperations(menu);
    }
    
    /**
     * Creates a three-level menu system demonstrating nested lists
     */
    private static MyDoublyLinkedList<MenuItem> createThreeLevelMenuSystem() {
        MyDoublyLinkedList<MenuItem> menu = new MyDoublyLinkedList<>();
        
        // Level 1: Main Menu Items
        // Create "File" menu with sub-items and sub-sub-items
        MySinglyLinkedList<SubMenuItem> fileSubItems = new MySinglyLinkedList<>();
        
        // Level 2: Sub Items for "File"
        SubMenuItem newItem = new SubMenuItem("New");
        // Level 3: Sub-Sub Items for "New"
        newItem.addSubSubItem("Project");
        newItem.addSubSubItem("File");
        newItem.addSubSubItem("Module");
        fileSubItems.add(newItem);
        
        SubMenuItem openItem = new SubMenuItem("Open");
        openItem.addSubSubItem("File or Project");
        openItem.addSubSubItem("Recent Files");
        openItem.addSubSubItem("Recent Projects");
        fileSubItems.add(openItem);
        
        SubMenuItem exportItem = new SubMenuItem("Export");
        exportItem.addSubSubItem("Files or Selection to HTML");
        exportItem.addSubSubItem("Project to Eclipse");
        exportItem.addSubSubItem("Project to Zip File");
        fileSubItems.add(exportItem);
        
        menu.add(new MenuItem("File", fileSubItems));
        
        // Create "Edit" menu with sub-items and sub-sub-items
        MySinglyLinkedList<SubMenuItem> editSubItems = new MySinglyLinkedList<>();
        
        SubMenuItem undoItem = new SubMenuItem("Undo");
        undoItem.addSubSubItem("Undo Last Action");
        undoItem.addSubSubItem("Redo Last Action");
        editSubItems.add(undoItem);
        
        SubMenuItem findItem = new SubMenuItem("Find");
        findItem.addSubSubItem("Find in File");
        findItem.addSubSubItem("Find in Project");
        findItem.addSubSubItem("Replace in File");
        findItem.addSubSubItem("Replace in Project");
        editSubItems.add(findItem);
        
        menu.add(new MenuItem("Edit", editSubItems));
        
        // Create "View" menu with sub-items and sub-sub-items
        MySinglyLinkedList<SubMenuItem> viewSubItems = new MySinglyLinkedList<>();
        
        SubMenuItem appearanceItem = new SubMenuItem("Appearance");
        appearanceItem.addSubSubItem("Tool Windows");
        appearanceItem.addSubSubItem("Status Bar");
        appearanceItem.addSubSubItem("Navigation Bar");
        viewSubItems.add(appearanceItem);
        
        SubMenuItem toolWindowsItem = new SubMenuItem("Tool Windows");
        toolWindowsItem.addSubSubItem("Project");
        toolWindowsItem.addSubSubItem("Structure");
        toolWindowsItem.addSubSubItem("Terminal");
        toolWindowsItem.addSubSubItem("Version Control");
        viewSubItems.add(toolWindowsItem);
        
        menu.add(new MenuItem("View", viewSubItems));
        
        return menu;
    }
    
    /**
     * Displays the complete three-level menu structure
     */
    private static void displayMenuStructure(MyDoublyLinkedList<MenuItem> menu) {
        System.out.println("THREE-LEVEL MENU STRUCTURE:");
        System.out.println("===========================");
        
        for (int i = 0; i < menu.size(); i++) {
            MenuItem mainItem = menu.get(i);
            System.out.println("Level 1: " + mainItem.getName());
            
            MySinglyLinkedList<SubMenuItem> subItems = mainItem.getSubItems();
            for (int j = 0; j < subItems.size(); j++) {
                SubMenuItem subItem = subItems.get(j);
                System.out.println("  Level 2: " + subItem.getName());
                
                MySinglyLinkedList<String> subSubItems = subItem.getSubSubItems();
                for (int k = 0; k < subSubItems.size(); k++) {
                    String subSubItem = subSubItems.get(k);
                    System.out.println("    Level 3: " + subSubItem);
                }
            }
            System.out.println();
        }
    }
    
    /**
     * Demonstrates CRUD operations on the three-level structure
     */
    private static void demonstrateCRUDOperations(MyDoublyLinkedList<MenuItem> menu) {
        System.out.println("CRUD OPERATIONS DEMONSTRATION:");
        System.out.println("==============================");
        
        // CREATE: Add a new main menu item
        System.out.println("CREATE: Adding new 'Build' menu item...");
        MySinglyLinkedList<SubMenuItem> buildSubItems = new MySinglyLinkedList<>();
        SubMenuItem compileItem = new SubMenuItem("Compile");
        compileItem.addSubSubItem("Compile Current File");
        compileItem.addSubSubItem("Compile Project");
        buildSubItems.add(compileItem);
        menu.add(new MenuItem("Build", buildSubItems));
        System.out.println("✓ Build menu added successfully\n");
        
        // READ: Display specific menu item
        System.out.println("READ: Reading 'File' menu item...");
        MenuItem fileItem = menu.get(0);
        System.out.println("✓ Found: " + fileItem.getName() + " with " + fileItem.getSubItems().size() + " sub-items\n");
        
        // UPDATE: Modify a sub-sub-item
        System.out.println("UPDATE: Updating 'New' sub-item in File menu...");
        SubMenuItem newSubItem = fileItem.getSubItems().get(0);
        newSubItem.addSubSubItem("New Class");
        System.out.println("✓ Added 'New Class' to New sub-item\n");
        
        // DELETE: Remove a sub-sub-item
        System.out.println("DELETE: Removing 'Module' from New sub-item...");
        boolean deleted = newSubItem.removeSubSubItem("Module");
        System.out.println("✓ Deletion " + (deleted ? "successful" : "failed") + "\n");
    }
    
    /**
     * Demonstrates search operations on the three-level structure
     */
    private static void demonstrateSearchOperations(MyDoublyLinkedList<MenuItem> menu) {
        System.out.println("SEARCH OPERATIONS DEMONSTRATION:");
        System.out.println("================================");
        
        // Search for menu items containing "File"
        System.out.println("Searching for items containing 'File'...");
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.get(i);
            if (item.getName().toLowerCase().contains("file")) {
                System.out.println("✓ Found main menu: " + item.getName());
            }
            
            // Search in sub-items
            MySinglyLinkedList<SubMenuItem> subItems = item.getSubItems();
            for (int j = 0; j < subItems.size(); j++) {
                SubMenuItem subItem = subItems.get(j);
                if (subItem.getName().toLowerCase().contains("file")) {
                    System.out.println("  ✓ Found sub-item: " + subItem.getName());
                }
                
                // Search in sub-sub-items
                MySinglyLinkedList<String> subSubItems = subItem.getSubSubItems();
                for (int k = 0; k < subSubItems.size(); k++) {
                    String subSubItem = subSubItems.get(k);
                    if (subSubItem.toLowerCase().contains("file")) {
                        System.out.println("    ✓ Found sub-sub-item: " + subSubItem);
                    }
                }
            }
        }
        
        System.out.println("\n=== DEMONSTRATION COMPLETE ===");
        System.out.println("This demonstrates the three-level nested list structure");
        System.out.println("as required by the project specifications, following");
        System.out.println("the IntelliJ IDEA menu structure example.");
    }
}
