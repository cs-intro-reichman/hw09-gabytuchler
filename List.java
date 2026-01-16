/** A linked list of character data objects. */
public class List {

    // Points to the first node in this list
    private Node first;

    // The number of elements in this list
    private int size;
    
    /** Constructs an empty list. */
    public List() {
        first = null;
        size = 0;
    }
    
    /** Returns the number of elements in this list. */
    public int getSize() {
          return size;
    }

    /** Returns the CharData of the first element in this list. */
    public CharData getFirst() {
        if (this.first == null) return null;
        return this.first.cp;
    }

    /** Adds a CharData object with the given character to the beginning of this list. */
    public void addFirst(char chr) {
        CharData newData = new CharData(chr);
        Node newNode = new Node(newData, first);
        first = newNode;
        size++;
    }
    
    /** Textual representation of this list. */
    public String toString() {
        if (size == 0) return "()";
        StringBuilder str = new StringBuilder("(");
        Node current = first;
        while (current != null) {
            str.append(current.cp.toString());
            if (current.next != null) {
                str.append(" ");
            }
            current = current.next;
        }
        str.append(")");
        return str.toString();
    }

    /** Returns the index of the first CharData object in this list. */
    public int indexOf(char chr) {
        Node current = first;
        int index = 0;
        while (current != null) {
            if (current.cp.chr == chr) {
                return index;
            }
            current = current.next; 
            index++;
        }
        return -1; 
    }

    /** Increments counter or adds new CharData. */
    public void update(char chr) {
        Node current = first;
        while (current != null) {
            if (current.cp.chr == chr) {
                current.cp.count++;
                return; 
            }
            current = current.next; 
        }
        addFirst(chr);
    }
    
    /** Removes a CharData object from the list. */
    public boolean remove(char chr) {
        if (first == null) return false;
        if (first.cp.chr == chr) {
            first = first.next;
            size--;
            return true;
        }
        Node pre = first;
        Node current = first.next;
        while (current != null) {
            if (current.cp.chr == chr) {
                pre.next = current.next;
                size--;
                return true;
            }
            pre = current;
            current = current.next;
        }
        return false;
    }

    /** Returns the CharData object at the specified index. */
    public CharData get(int index) {
        if (index < 0 || index >= size) { // שימי לב לתיקון הגבולות כאן
            throw new IndexOutOfBoundsException();
        }
        Node current = first;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.cp;
    } // <--- כאן היה סוגר מיותר קודם!

    /** Returns an array of CharData objects. */
    public CharData[] toArray() {
        CharData[] arr = new CharData[size];
        Node current = first;
        int i = 0;
        while (current != null) {
            arr[i++] = current.cp;
            current = current.next;
        }
        return arr;
    }

    /** Returns an iterator starting at index. */
    public ListIterator listIterator(int index) {
        if (size == 0) return null;
        Node current = first;
        int i = 0;
        while (i < index && current != null) {
            current = current.next;
            i++;
        }
        return new ListIterator(current);
    }
} 