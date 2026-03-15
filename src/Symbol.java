import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.OptionalInt;

public class Symbol implements Comparable<Symbol> {
    private final @Nullable String namespace;
    private final @NotNull String name;

    private Symbol(@Nullable String namespace, @NotNull String name) {
        this.namespace = namespace;
        this.name = name;
    }

    public static Symbol symbol(@Nullable String namespace, @NotNull String name) {
        return new Symbol(namespace, name);
    }

    public static Symbol symbol(@NotNull String name) {
        return new Symbol(null, name);
    }

    public @Nullable String getNamespace() {
        return namespace;
    }

    public @NotNull String getName() {
        return name;
    }

    public boolean isFullyQualified() {
        return namespace != null;
    }

    @Override
    public String toString() {
        return namespace == null || namespace.isEmpty()
                ? name
                : namespace + '/' + name;
    }

    @Override
    public int compareTo(@NotNull Symbol symbol) {
        if (this == symbol)
            return 0;

        final int result;
        if (namespace == null)
            result = symbol.namespace == null ? 0 : -1;
        else if (symbol.namespace == null)
            result = 1;
        else
            result = namespace.compareTo(symbol.namespace);

        if (result != 0) return result;

        return name.compareTo(symbol.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Symbol symbol)) return false;
        return Objects.equals(namespace, symbol.namespace)
                && Objects.equals(name, symbol.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, name);
    }

    private static @NotNull OptionalInt dividerIndexIfValid(final @NotNull String s, final boolean allowUTF) {
        if (s.isEmpty()) return OptionalInt.empty();
        if (s.length() == 1 && s.charAt(0) == '/') return OptionalInt.of(-1);

        int dividerIndex = -1;
        var codepoints = s.codePoints().toArray();

        int index = -1;
        for (int chr : codepoints) {
            index++;

            if ((chr >= 'a' && chr <= 'z')|| (chr >= 'A' && chr <= 'Z')|| (chr >= '0' && chr <= '9')) {
                continue;
            }

            switch (chr) {
                case '*', '!', '_', '?', '$', '%', '&', '=', '<', '>':
                    break;
                case '.', '+', '-':
                    if ((index == 0 || index - 1 == dividerIndex)
                            && index < codepoints.length - 1
                            && codepoints[index + 1] >= '0'
                            && codepoints[index + 1] <= '9')
                        return OptionalInt.empty();
                    break;
                case '/':
                    if (index == 0) return OptionalInt.empty();
                    if (codepoints.length <= index + 1) return OptionalInt.empty();
                    if (dividerIndex == -1) dividerIndex = index;
                    break;
                default:
                    if (Character.isWhitespace(chr))
                        return OptionalInt.empty();
                    if (!allowUTF)
                        return OptionalInt.empty();
                    break;
            }
        }
        return OptionalInt.of(dividerIndex);
    }

    public static boolean isValidSymbol(final @NotNull String s) {
        return isValidSymbol(s, false);
    }

    public static boolean isValidSymbol(final @NotNull String s, final boolean allowUTF) {
        return dividerIndexIfValid(s, allowUTF).isPresent();
    }

    public static @Nullable Symbol parse(final @NotNull String s) {
        return parse(s, false);
    }

    public static @Nullable Symbol parse(final @NotNull String s, final boolean allowUTF) {
        if (s.equals("/"))
            return symbol("/");

        var dividerIndexOpt = dividerIndexIfValid(s, allowUTF);
        if (dividerIndexOpt.isEmpty()) return null;
        var dividerIndex = dividerIndexOpt.getAsInt();

        if (dividerIndex != -1) {
            var parts = s.split("/", 2);
            return symbol(parts[0], parts[1]);
        }

        return symbol(null, s);
    }

    public static @NotNull Symbol parseChecked(final @NotNull String s, final boolean allowUTF) {
        var temp = parse(s, allowUTF);
        if (temp == null)
            throw new IllegalArgumentException("Illegal symbol format: " + s);
        return temp;
    }

    public static @NotNull Symbol get(final @NotNull String s) {
        var temp = parse(s, false);
        if (temp == null)
            throw new IllegalArgumentException("Illegal symbol format: " + s);
        return temp;
    }
}
