package emanuelrichieri.mutantdetector.util.suffixtree;

import java.util.Arrays;
import java.util.Collection;

/**
 * A kind of Map that uses native char types and sorted
 * arrays to keep minimize the memory footprint.
 */
class EdgeBag {
    private byte[] chars;
    private Edge[] values;
    private static final int BSEARCH_THRESHOLD = 6; // Minimum length to do binary search

    public Edge put(Character character, Edge e) {
        char c = character.charValue();
        if (c != (char) (byte) c) {
            throw new IllegalArgumentException("Illegal input character " + c + ".");
        }
        
        if (chars == null) {
            chars = new byte[0];
            values = new Edge[0];
        }
        int idx = search(c);
        Edge previous = null;

        if (idx < 0) {
            int currentSize = chars.length;
            byte[] copy = new byte[currentSize + 1];
            System.arraycopy(chars, 0, copy, 0, currentSize);
            chars = copy;
            Edge[] copy1 = new Edge[currentSize + 1];
            System.arraycopy(values, 0, copy1, 0, currentSize);
            values = copy1;
            chars[currentSize] = (byte) c;
            values[currentSize] = e;
            currentSize++;
            if (currentSize > BSEARCH_THRESHOLD) {
                sortArrays();
            }
        } else {
            previous = values[idx];
            values[idx] = e;
        }
        return previous;
    }

    public Edge get(char c) {
        if (c != (char) (byte) c) {
            throw new IllegalArgumentException("Illegal input character " + c + ".");
        }
        
        int idx = search(c);
        if (idx < 0) {
            return null;
        }
        return values[idx];
    }

    private int search(char c) {
        if (chars == null) {
        	return -1;
        }
        if (chars.length > BSEARCH_THRESHOLD) {
            return Arrays.binarySearch(chars, (byte) c);
        }        
        for (int i = 0; i < chars.length; i++) {
            if (c == chars[i]) {
                return i;
            }
        }
        return -1;
    }

    public Collection<Edge> values() {
        return Arrays.asList(values == null ? new Edge[0] : values);
    }
    
    /**
     * A trivial implementation of sort, used to sort chars[] and values[] according to the data in chars.
     * It was preferred to faster sorts (like qsort) because of the small sizes (<=36) of the collections involved.
     */
    private void sortArrays() {
        for (int i = 0; i < chars.length; i++) {
         for (int j = i; j > 0; j--) {
            if (chars[j-1] > chars[j]) {
               byte swap = chars[j];
               chars[j] = chars[j-1];
               chars[j-1] = swap;

               Edge swapEdge = values[j];
               values[j] = values[j-1];
               values[j-1] = swapEdge;
            }
         }
      }
    }
    
    public boolean isEmpty() {
        return chars == null || chars.length == 0;
    }
    
    public int size() {
        return chars == null ? 0 : chars.length;
    }
}
