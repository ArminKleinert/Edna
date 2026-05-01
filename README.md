# Edna
An EDN library for Java.

## Standard things implemented

[x] Can read from strings, files, inputstreams and readers.  
[x] Can write to strings, files and any Appendable.

[x] Uses UTF-8 for inputs.  
[x] Can parse any type mandated by the EDN standard.

## Examples

```java
void example() {
    IO.println(Edna.read("symbol")); // Symbol without namespace
    IO.println(Edna.read("namespace/symbol")); // Symbol
    IO.println(Edna.read(":keyword")); // Keyword without namespace
    IO.println(Edna.read(":namespace/keyword")); // Keyword
    IO.println(Edna.read("\"string\"")); // String
    IO.println(Edna.read("\\c")); // Character

    IO.println(Edna.read("(list elements)")); // List
    IO.println(Edna.read("[vector elements]")); // Vector
    IO.println(Edna.read("#{set elements}")); // Set
    IO.println(Edna.read("{map-key map-value}")); // Map

    IO.println(Edna.read("12648430")); // Long
    IO.println(Edna.read("0xC0FFEE", Edna.extendedOptions())); // Long, hex prefix requires additional setting
    IO.println(Edna.read("12648430N")); // The same as BigInt
    IO.println(Edna.read("5.0")); // Double
    IO.println(Edna.read("5.0M")); // BigDecimal

    IO.println(Edna.read("nil")); // null
    IO.println(Edna.read("false")); // false
    IO.println(Edna.read("true")); // true

    IO.println(Edna.read("#uuid \"f81d4fae-7dec-11d0-a765-00a0c91e6bf6\"")); // UUID
    IO.println(Edna.read("#inst \"1985-04-12T23:20:50.52Z\"")); // Instant

    IO.println(Edna.read("^:a abc", Edna.extendedOptions())); // Meta
}
```

☑ 