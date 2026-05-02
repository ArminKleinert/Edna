# Edna

An EDN library for Java with options for some additional features. I made this as a personal replacement for my [Kotlin EDN library](https://github.com/ArminKleinert/ExtensibleDataSoup).

## Standard things implemented

- [x] Can read from strings, files, inputstreams and readers.  
- [x] Can write to strings, files and any `Appendable`.


- [x] [Tagged elements](https://github.com/edn-format/edn#tagged-elements)  
  - Note that Edna handles tagged elements as function that take an object, not a Map, as input. This allows doing much more than just instantiate class instances.
- [x] [Symbolic values](https://clojure.org/guides/weird_characters#_symbolic_values) (optional because they are not mandated by the specification)
  - The user can define their own symbolic values, but `NaN`, `Inf` and `-Inf` are available when the `allowSymbolicValues` option is true.
  - Since EDN technically does not mandate symbolic values, but the linked guide says that they are available, I eventually decided to make them optional.


- [x] Uses UTF-8 for inputs.
- [x] Can parse any type mandated by the EDN specification.


## Extended features

[x]

| Name                            | Meaning                                                                         | Type                                                             | Default                               |
|---------------------------------|---------------------------------------------------------------------------------|------------------------------------------------------------------|---------------------------------------|
| `allowSchemeUTF32Codes`         | Allow `\xXXXXXXXX` Char32 literals.                                             | `boolean`                                                        | `false`                               |
| `allowDispatchChars`            | Allow Char32 literals.                                                          | `boolean`                                                        | `false`                               |
| `taggedElementDecoders`         | Converters for tagged elements.                                                 | `Map<String, Function<Object, Object>>`                          | Empty map                             |
| `taggedElementEncoders`         |                                                                                 | `SequencedMap<Class<?>, Function<Object, Map.Entry<String, ?>>>` | Empty map                             |
| `moreNumberPrefixes`            | Allow `0x` (hex), `0o` (octal), `0b` (binary) and `XXr` prefixes for integrals. | `boolean`                                                        | `false`                               |
| `allowMoreEncoderDecoderNames`  | Allows tagged elements without namespaces.                                      | `boolean`                                                        | `false`                               |
| `encodingSequenceSeparator`     |                                                                                 | `String`                                                         | `", "`                                |
| `listToEdnListConverter`        | Converter from `(...)` sequences to lists.                                      | `Function<List<?>, List<?>>`                                     | `EdnaList::new`                       |
| `listToEdnVectorConverter`      | Converter from `[...]` vectors to lists.                                        | `Function<List<?>, List<?>>`                                     | `EdnaVector::new`                     |
| `listToEdnSetConverter`         | Converter from element list in `#{...}` into sets.                              | `Function<List<?>, Set<?>>`                                      | `EdnaSet::new`                        |
| `listToEdnMapConverter`         | Converter from pairs in `{...}` into maps.                                      | `Function<List<Map.Entry<Object, Object>>, Map<?, ?>>`           | `EdnaMap::new`                        |
| `allowUTFSymbols`               | Allow UTF-8 codepoints in `Symbol` and `Keyword` names and namespaces.          | `boolean`                                                        | `false`                               |
| `encoderSequenceElementLimit`   |                                                                                 | `int`                                                            | May vary                              |
| `encoderCollectionElementLimit` |                                                                                 | `int`                                                            | May vary                              |
| `encoderMaxColumn`              |                                                                                 | `int`                                                            | May vary                              |
| `encoderLineIndent`             |                                                                                 | `String`                                                         | May vary                              |
| `encoderPrettyPrint`            | Use pretty printing when writing.                                               | `boolean`                                                        | `true`                                |
| `allowMetaData`                 | Allow metadata `^{...} e` (map), `^:... e` (keyword), `^... e` (symbol)         | `boolean`                                                        | `false`                               |
| `allowZeroPrefix`               | Allow numbers other than `0` to start with `0`.                                 | `boolean`                                                        | `false`                               |
| `allowSymbolicValues`           | Use the `symbolicValues` map for the reader.                                    | `boolean`                                                        | `false`                               |
| `symbolicValues`                | Symbolic value map with the keys being symbols without the `##` prefix.         | `Map<Symbol, Object>`                                            | Map with keys `NaN`, `Inf` and `-Inf` |


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