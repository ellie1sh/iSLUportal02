/**
 * SubMenuItem class for third-level menu nesting
 * This class represents sub-items within menu items, creating a three-level hierarchy:
 * Level 1: Main Menu Items (MyDoublyLinkedList<MenuItem>)
 * Level 2: Sub Items (MySinglyLinkedList<SubMenuItem>)
 * Level 3: Sub-Sub Items (MySinglyLinkedList<String>)
 * 
 * This follows the IntelliJ IDEA menu structure example from the requirements.
 */
public class SubMenuItem {
    private String name;
    private MySinglyLinkedList<String> subSubItems;
    private String description;
    private boolean isEnabled;
    
    public SubMenuItem(String name) {
        this.name = name;
        this.subSubItems = new MySinglyLinkedList<>();
        this.description = "";
        this.isEnabled = true;
    }
    
    public SubMenuItem(String name, MySinglyLinkedList<String> subSubItems) {
        this.name = name;
        this.subSubItems = subSubItems != null ? subSubItems : new MySinglyLinkedList<>();
        this.description = "";
        this.isEnabled = true;
    }
    
    public SubMenuItem(String name, String description) {
        this.name = name;
        this.subSubItems = new MySinglyLinkedList<>();
        this.description = description;
        this.isEnabled = true;
    }
    
    public SubMenuItem(String name, MySinglyLinkedList<String> subSubItems, String description) {
        this.name = name;
        this.subSubItems = subSubItems != null ? subSubItems : new MySinglyLinkedList<>();
        this.description = description;
        this.isEnabled = true;
    }
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public MySinglyLinkedList<String> getSubSubItems() {
        return subSubItems;
    }
    
    public void setSubSubItems(MySinglyLinkedList<String> subSubItems) {
        this.subSubItems = subSubItems;
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
    
    public boolean hasSubSubItems() {
        return subSubItems != null && subSubItems.size() > 0;
    }
    
    /**
     * Add a sub-sub-item to this sub-item
     * @param subSubItem The sub-sub-item to add
     */
    public void addSubSubItem(String subSubItem) {
        if (subSubItems == null) {
            subSubItems = new MySinglyLinkedList<>();
        }
        subSubItems.add(subSubItem);
    }
    
    /**
     * Remove a sub-sub-item from this sub-item
     * @param subSubItem The sub-sub-item to remove
     * @return true if removed, false if not found
     */
    public boolean removeSubSubItem(String subSubItem) {
        if (subSubItems == null) return false;
        
        int index = subSubItems.indexOf(subSubItem);
        if (index != -1) {
            subSubItems.remove(index);
            return true;
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
        SubMenuItem that = (SubMenuItem) obj;
        return name != null ? name.equals(that.name) : that.name == null;
    }
    
    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
