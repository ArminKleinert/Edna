package de.kleinert.edna.data;

import java.util.Map;

public record EdnaTagVal<T1,T2>(T1 tag, T2 element) {
    public Map.Entry<T1,T2> toMapEntry() { return Map.entry(tag, element); }
}
