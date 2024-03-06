package uj.wmii.pwj.map2d;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Map2DImpl<R, C, V> implements Map2D<R, C, V> {
    private final Map<R, Map<C, V>> map = new HashMap<>();

    @Override
    public V put(R rowKey, C columnKey, V value) {
        if (rowKey == null || columnKey == null)
            throw new NullPointerException();
        if (map.get(rowKey) == null) {
            map.put(rowKey, new HashMap<C, V>());
            map.get(rowKey).put(columnKey, value);
            return null;
        } else {
            V currentValue = get(rowKey, columnKey);
            map.get(rowKey).put(columnKey, value);
            return currentValue;
        }
    }

    @Override
    public V get(R rowKey, C columnKey) {
        if (map.get(rowKey) == null)
            return null;
        return map.get(rowKey).get(columnKey);
    }

    @Override
    public V getOrDefault(R rowKey, C columnKey, V defaultValue) {
        V currentValue = get(rowKey, columnKey);
        return currentValue == null ? defaultValue : currentValue;
    }

    @Override
    public V remove(R rowKey, C columnKey) {
        if (map.get(rowKey) == null)
            return null;
        return map.get(rowKey).remove(columnKey);
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean nonEmpty() {
        return !map.isEmpty();
    }

    @Override
    public int size() {
        int size = 0;
        for (var column : map.values()) {
            size += column.values().size();
        }
        return size;
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Map<C, V> rowView(R rowKey) {
        Map<C, V> resultMap = new HashMap<>();
        if (map.get(rowKey) != null)
            resultMap.putAll(map.get(rowKey));
        return resultMap;
    }

    @Override
    public Map<R, V> columnView(C columnKey) {
        Map<R, V> resultMap = new HashMap<>();
        for (var row : map.keySet()) {
            if (this.get(row, columnKey) != null)
                resultMap.put(row, this.get(row, columnKey));
        }
        return resultMap;
    }

    @Override
    public boolean hasValue(V value) {
        for (var column : map.values()) {
            if (column.containsValue(value))
                return true;
        }
        return false;
    }

    @Override
    public boolean hasKey(R rowKey, C columnKey) {
        return get(rowKey, columnKey) != null;
    }

    @Override
    public boolean hasRow(R rowKey) {
        if (map.get(rowKey) == null)
            return false;
        return !map.get(rowKey).values().isEmpty();
    }

    @Override
    public boolean hasColumn(C columnKey) {
        return !this.columnView(columnKey).isEmpty();
    }

    @Override
    public Map<R, Map<C, V>> rowMapView() {
        Map<R, Map<C, V>> resultMap = new HashMap<>();
        for (var entry : map.entrySet()) {
            for (var column : entry.getValue().keySet()) {
                if (resultMap.get(entry.getKey()) == null)
                    resultMap.put(entry.getKey(), new HashMap<>());
                resultMap.get(entry.getKey()).put(column, entry.getValue().get(column));
            }
        }
        return resultMap;
    }

    @Override
    public Map<C, Map<R, V>> columnMapView() {//check
        Map<C, Map<R, V>> resultMap = new HashMap<>();
        for (var entry : map.entrySet()) {
            for (var column : entry.getValue().keySet()) {
                if (resultMap.get(column) == null)
                    resultMap.put(column, new HashMap<>());
                resultMap.get(column).put(entry.getKey(), entry.getValue().get(column));
            }
        }
        return resultMap;
    }

    @Override
    public Map2D<R, C, V> fillMapFromRow(Map<? super C, ? super V> target, R rowKey) {
        target.putAll(rowView(rowKey));
        return this;
    }

    @Override
    public Map2D<R, C, V> fillMapFromColumn(Map<? super R, ? super V> target, C columnKey) {
        target.putAll(columnView(columnKey));
        return this;
    }

    @Override
    public Map2D<R, C, V> putAll(Map2D<? extends R, ? extends C, ? extends V> source) {
        var tempMap = source.rowMapView();
        for (var tempMapEntry : tempMap.entrySet()) {
            for (var tempMapColumn : tempMapEntry.getValue().keySet()) {
                V value = tempMapEntry.getValue().get(tempMapColumn);
                this.put(tempMapEntry.getKey(), tempMapColumn, value);
            }
        }
        return this;
    }

    @Override
    public Map2D<R, C, V> putAllToRow(Map<? extends C, ? extends V> source, R rowKey) {
        for (var column : source.keySet())
            this.put(rowKey, column, source.get(column));
        return this;
    }

    @Override
    public Map2D<R, C, V> putAllToColumn(Map<? extends R, ? extends V> source, C columnKey) {
        for (var row : source.keySet()) {
            this.put(row, columnKey, source.get(row));
        }
        return this;
    }

    @Override
    public <R2, C2, V2> Map2D<R2, C2, V2> copyWithConversion(
            Function<? super R, ? extends R2> rowFunction,
            Function<? super C, ? extends C2> columnFunction,
            Function<? super V, ? extends V2> valueFunction) {
        Map2D resultMap2D = new Map2DImpl();
        for (var entry : map.entrySet()) {
            for (var column : entry.getValue().keySet()) {
                resultMap2D.put(rowFunction.apply(entry.getKey()), columnFunction.apply(column), valueFunction.apply(entry.getValue().get(column)));
            }
        }
        return resultMap2D;
    }
}
