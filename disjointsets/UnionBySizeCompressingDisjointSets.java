package disjointsets;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * A quick-union-by-size data structure with path compression.
 *
 * @see DisjointSets for more documentation.
 */
public class UnionBySizeCompressingDisjointSets<T> implements DisjointSets<T> {
    //pointers holds the parent node of each set as -1, and anything other than -1 is a pointer to a different
    //node indicating that the node at the index is the parent of the current node
    List<Integer> pointers;

    //holds the size of each set to make a more efficient runtime
    List<Integer> setSize;

    //data holds each item as the key and the Integer is used as the value and points to the location in pointers
    Map<T, Integer> data;
    int index = 0;


    public UnionBySizeCompressingDisjointSets() {
        pointers = new ArrayList<>();
        setSize = new ArrayList<>();
        data = new HashMap<>();
    }

    //creates a set with a single item
    @Override
    public void makeSet(T item) {
        pointers.add(-1);
        data.put(item, index);
        setSize.add(1);
        index++;
    }

    //findSet finds the parent node of any given item using pointers
    @Override
    public int findSet(T item) throws IllegalArgumentException {
        Integer i = data.get(item);
        if (i == null) {
            throw new IllegalArgumentException();
        }
        List<Integer> indicesToVisit = new ArrayList<>();
        while (pointers.get(i) != -1) {
            indicesToVisit.add(i);
            i = pointers.get(i);
        }

        for (Integer edge : indicesToVisit) {
            pointers.set(edge, i);
        }
        return i;
    }

    //union combines two sets, putting the smaller one under the larger one
    @Override
    public boolean union(T item1, T item2) throws IllegalArgumentException {
        int item1P = this.findSet(item1);
        int item2P = this.findSet(item2);

        if (item1P == item2P) {
            return false;
        }

        int size1 = setSize.get(item1P);
        int size2 = setSize.get(item2P);

        if (size1 >= size2) {
            pointers.set(item2P, item1P);
            setSize.set(item1P, size1 + size2);
        } else {
            pointers.set(item1P, item2P);
            setSize.set(item2P, size1 + size2);
        }
        return true;
    }
}
