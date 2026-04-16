package de.kleinert.edna.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Keyword implements Comparable<Keyword> {

    private final @NotNull Symbol sym;

    private Keyword(final @NotNull Symbol sym) {
        this.sym = sym;
    }

    private static final @NotNull Map<Symbol, WeakReference<Keyword>> table =
            new ConcurrentHashMap<>();

    private static final ReferenceQueue<Keyword> rq = new ReferenceQueue<>();

    public static void clearCache() {
        if (rq.poll() == null)
            return;

        @Nullable var rqPoll = rq.poll();
        while (rqPoll != null) {
            rqPoll = rq.poll();
        }

        for (var e : table.entrySet()) {
            final @NotNull var ref = e.getValue();
            if (ref.get() == null) {
                table.remove(e.getKey(), ref);
            }
        }
    }

    public static @NotNull Keyword intern(final @NotNull Symbol sym) {
        @Nullable Keyword k = null;
        @Nullable WeakReference<Keyword> existingRef = table.get(sym);
        if (existingRef == null) {
            clearCache();

            k = new Keyword(sym);
            existingRef = table.putIfAbsent(sym, new WeakReference<>(k, rq));
        }

        if (existingRef == null) {
            return k;
        }

        final @Nullable Keyword existingKeyword = existingRef.get();
        if (existingKeyword != null) {
            return existingKeyword;
        }
        table.remove(sym, existingRef);
        return intern(sym); // Intentional recursion
    }

    public static @Nullable Keyword parse(final @NotNull String s,
                                          final boolean allowUTF) {
        if (s.length() <= 1) return null; // Keywords must be at least 2 chars long
        if (s.charAt(0) != ':') return null; // Keywords must start with a colon
        if (s.length() == 2 && s.charAt(1) == '/') return null; // Keywords can not be `:/`

        var substring = s.substring(1);

        var sym = Symbol.parse(substring, allowUTF);
        if (sym == null) return null;
        return intern(sym);
    }

    public static @NotNull Keyword parseChecked(final @NotNull String s,
                                                final boolean allowUTF) {
        var name = s.startsWith(":") ? s : ":" + s;
        var temp = parse(name, allowUTF);
        if (temp == null)
            throw new IllegalArgumentException("Illegal keyword format: " + s);
        return temp;
    }

    public static @NotNull Keyword parseChecked(final @NotNull String s) {
        return parseChecked(s, false);
    }

    public static @Nullable Keyword parse(final @NotNull String s) {
        return parse(s, false);
    }

    public static @NotNull Keyword keyword(final @NotNull Symbol s) {
        return intern(s);
    }

    public static @NotNull Keyword keyword(final @NotNull String s) {
        return keyword(null, s);
    }

    public static @NotNull Keyword keyword(final @Nullable String prefix,
                                           final @NotNull String name) {
        return intern(Symbol.symbol(prefix, name));
    }

    public static @NotNull Keyword get(final @NotNull String s) {
        var name = s.startsWith(":") ? s : ":" + s;
        var temp = parse(name);
        if (temp == null)
            throw new IllegalArgumentException("Illegal keyword format: " + s);
        return temp;
    }

    public static @NotNull Keyword get(final @NotNull Symbol s) {
        return intern(s);
    }


    public static @Nullable Keyword find(final @NotNull Symbol sym) {
        final @Nullable var ref = table.get(sym);
        return ref == null ? null : ref.get();
    }

    public static @Nullable Keyword find(final @NotNull String nsAndName) {
        return find(Symbol.parseChecked(nsAndName));
    }

    @Override
    public String toString() {
        return ":" + sym;
    }

    @Override
    public boolean equals(final Object o) {
        return this == o;
    }

    @Override
    public int hashCode() {
        return sym.hashCode() - 1640531527;
    }

    @Override
    public int compareTo(final @NotNull Keyword keyword) {
        return sym.compareTo(keyword.sym);
    }

    public @NotNull Symbol getSym() {
        return sym;
    }

    public boolean isFullyQualified() {
        return sym.isFullyQualified();
    }

    @NotNull
    public String getName() {
        return sym.getName();
    }

    @Nullable
    public String getNamespace() {
        return sym.getNamespace();
    }
}
