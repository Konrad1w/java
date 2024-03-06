package uj.wmii.pwj.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListMerger {
    public static List<Object> mergeLists(List<?> l1, List<?> l2) {
        if (l1 == null && l2 == null)
            return new ArrayList<>();
        List<Object> result = new ArrayList<>();
        int lenght = l1.size() > l2.size() ? l1.size() : l2.size();
        for (int i = 0; i < lenght; i++) {
            if (i < l1.size())
                result.add(l1.get(i));
            if (i < l2.size())
                result.add(l2.get(i));
        }
        return Collections.unmodifiableList(result);
    }

}
