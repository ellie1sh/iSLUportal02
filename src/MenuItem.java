
/**
 * MenuItem class for hierarchical menu structure
 * This class represents menu items with support for three levels of nesting:
 * Level 1: Main Menu Items (MyDoublyLinkedList<MenuItem>)
 * Level 2: Sub Items (MySinglyLinkedList<SubMenuItem>)
 * Level 3: Sub-Sub Items (MySinglyLinkedList<String> within SubMenuItem)
 * 
 * This follows the IntelliJ IDEA menu structure example from the requirements.
 */
public class MenuItem {
    private String name;
    private MySinglyLinkedList<SubMenuItem> subItems;
    private String description;
    private boolean isEnabled;

    public MenuItem(String name) {
        this.name = name;
        this.subItems = new MySinglyLinkedList<>();
        this.description = "";
        this.isEnabled = true;
    }

    public MenuItem(String name, MySinglyLinkedList<SubMenuItem> subItems) {
        this.name = name;
        this.subItems = subItems != null ? subItems : new MySinglyLinkedList<>();
        this.description = "";
        this.isEnabled = true;
    }
    
    // Legacy constructor for backward compatibility with String sub-items
    public static MenuItem createWithStringSubItems(String name, MySinglyLinkedList<String> legacySubItems) {
        MenuItem item = new MenuItem(name);
        
        // Convert legacy string sub-items to SubMenuItem objects
        if (legacySubItems != null) {
            for (int i = 0; i < legacySubItems.size(); i++) {
                String subItemName = legacySubItems.get(i);
                SubMenuItem subMenuItem = new SubMenuItem(subItemName);
                item.subItems.add(subMenuItem);
            }
        }
        return item;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public MySinglyLinkedList<SubMenuItem> getSubItems() {
        return subItems;
    }
    
    public void setSubItems(MySinglyLinkedList<SubMenuItem> subItems) {
        this.subItems = subItems;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public boolean isEnabled() {
        return isEnabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }

    public boolean hasSubItems() {
        return subItems != null && subItems.size() > 0;
    }
    
    /**
     * Add a sub-item to this menu item
     * @param subItem The sub-item to add
     */
    public void addSubItem(SubMenuItem subItem) {
        if (subItems == null) {
            subItems = new MySinglyLinkedList<>();
        }
        subItems.add(subItem);
    }
    
    /**
     * Add a sub-item by name (creates SubMenuItem internally)
     * @param subItemName The name of the sub-item to add
     */
    public void addSubItem(String subItemName) {
        addSubItem(new SubMenuItem(subItemName));
    }
    
    /**
     * Remove a sub-item from this menu item
     * @param subItem The sub-item to remove
     * @return true if removed, false if not found
     */
    public boolean removeSubItem(SubMenuItem subItem) {
        if (subItems == null) return false;
        
        int index = subItems.indexOf(subItem);
        if (index != -1) {
            subItems.remove(index);
            return true;
        }
        return false;
    }
    
    /**
     * Remove a sub-item by name
     * @param subItemName The name of the sub-item to remove
     * @return true if removed, false if not found
     */
    public boolean removeSubItem(String subItemName) {
        if (subItems == null) return false;
        
        for (int i = 0; i < subItems.size(); i++) {
            SubMenuItem subItem = subItems.get(i);
            if (subItem.getName().equals(subItemName)) {
                subItems.remove(i);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        return name + (description.isEmpty() ? "" : " - " + description);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MenuItem that = (MenuItem) obj;
        return name != null ? name.equals(that.name) : that.name == null;
    }
    
    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}