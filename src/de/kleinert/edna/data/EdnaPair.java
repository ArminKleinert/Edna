package de.kleinert.edna.data;

import java.util.Map;

public record EdnaPair<T1,T2>(T1 first, T2 second) {
    public Map.Entry<T1,T2> toMapEntry() { return Map.entry(first, second); }
}
