/**
 * MyList Interface - Required interface for LinkedList implementations
 * This interface defines the contract that all list implementations must follow
 * for the Data Structures project requirements.
 * 
 * AUTHOR: Group Project Implementation
 * SUBJECT: DATA STRUCTURE IT212 9458
 */
public interface MyList<T> {
    
    /**
     * Add element to the end of the list
     * @param data The data to add
     */
    void add(T data);
    
    /**
     * Add element at specific index
     * @param index The index where to insert
     * @param data The data to add
     * @throws IndexOutOfBoundsException if index is invalid
     */
    void add(int index, T data);
    
    /**
     * Get element at specific index
     * @param index The index to retrieve
     * @return The element at the specified index
     * @throws IndexOutOfBoundsException if index is invalid
     */
    T get(int index);
    
    /**
     * Remove element at specific index
     * @param index The index to remove
     * @return The removed element
     * @throws IndexOutOfBoundsException if index is invalid
     */
    T remove(int index);
    
    /**
     * Get the size of the list
     * @return The number of elements in the list
     */
    int size();
    
    /**
     * Check if the list is empty
     * @return true if list is empty, false otherwise
     */
    boolean isEmpty();
    
    /**
     * Check if the list contains the specified element
     * @param data The element to search for
     * @return true if element is found, false otherwise
     */
    boolean contains(T data);
    
    /**
     * Find the index of the first occurrence of the specified element
     * @param data The element to search for
     * @return The index of the element, or -1 if not found
     */
    int indexOf(T data);
    
    /**
     * Clear all elements from the list
     */
    void clear();
    
    /**
     * Set element at specific index
     * @param index The index to set
     * @param data The new data
     * @return The previous element at that index
     * @throws IndexOutOfBoundsException if index is invalid
     */
    T set(int index, T data);
}
