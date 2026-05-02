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

- [x] [Metadata](https://clojure.org/guides/weird_characters#_and_metadata) (optional because they are not mandated by the specification)
  - Most parsers would implement this, but it's actually not mandated by the specification.
  - For simplicity, I decided to _not_ make 
- [x] [Symbolic values](https://clojure.org/guides/weird_characters#_symbolic_values) (optional because they are not mandated by the specification)
  - The user can define their own symbolic values, but `NaN`, `Inf` and `-Inf` are available when the `allowSymbolicValues` option is true.
  - Since EDN technically does not mandate symbolic values, but the linked guide says that they are available, I eventually decided to make them optional.
- [x] Casting values read to get objects of concrete classes.
- [x] Parse inputs that contain multiple values via ```java Edna.readMulti(...)```. 
  - I was inspired by [edn-ruby](https://github.com/relevance/edn-ruby#ednreader), except that Edna parses values eagerly.

## Options

| Name                            | Meaning                                                                   | Type                                                            | Default                               |
|---------------------------------|---------------------------------------------------------------------------|-----------------------------------------------------------------|---------------------------------------|
| `allowSchemeUTF32Codes`         | Allow `\xXXXXXXXX` Char32 literals.                                       | `boolean`                                                       | `false`                               |
| `allowDispatchChars`            | Allow Char32 literals.                                                    | `boolean`                                                       | `false`                               |
| `taggedElementDecoders`         | Converters for tagged elements.                                           | `Map<String, Function<Object, Object>>`                         | Empty map                             |
| `taggedElementEncoders`         |                                                                           | `SequencedMap<Class<?>, Function<Object, EdnaPair<String, ?>>>` | Empty map                             |
| `moreNumberPrefixes`            | `0x` (hex), `0o` (octal), `0b` (binary) and `XXr` prefixes for integrals. | `boolean`                                                       | `false`                               |
| `allowMoreEncoderDecoderNames`  | Allows tagged elements without namespaces.                                | `boolean`                                                       | `false`                               |
| `encodingSequenceSeparator`     |                                                                           | `String`                                                        | `", "`                                |
| `listToEdnListConverter`        | Converter from `(...)` sequences to lists.                                | `Function<List<?>, List<?>>`                                    | `EdnaList::new`                       |
| `listToEdnVectorConverter`      | Converter from `[...]` vectors to lists.                                  | `Function<List<?>, List<?>>`                                    | `EdnaVector::new`                     |
| `listToEdnSetConverter`         | Converter from element list in `#{...}` into sets.                        | `Function<List<?>, Set<?>>`                                     | `EdnaSet::new`                        |
| `listToEdnMapConverter`         | Converter from pairs in `{...}` into maps.                                | `Function<List<Map.Entry<Object, Object>>, Map<?, ?>>`          | `EdnaMap::new`                        |
| `allowUTFSymbols`               | Allow UTF-8 codepoints in `Symbol` and `Keyword` names and namespaces.    | `boolean`                                                       | `false`                               |
| `encoderSequenceElementLimit`   |                                                                           | `int`                                                           | May vary                              |
| `encoderCollectionElementLimit` |                                                                           | `int`                                                           | May vary                              |
| `encoderMaxColumn`              |                                                                           | `int`                                                           | May vary                              |
| `encoderLineIndent`             |                                                                           | `String`                                                        | May vary                              |
| `encoderPrettyPrint`            | Use pretty printing when writing.                                         | `boolean`                                                       | `true`                                |
| `allowMetaData`                 | Allow metadata `^{...} e` (map), `^:... e` (keyword), `^... e` (symbol)   | `boolean`                                                       | `false`                               |
| `allowZeroPrefix`               | Allow numbers other than `0` to start with `0`.                           | `boolean`                                                       | `false`                               |
| `allowSymbolicValues`           | Use the `symbolicValues` map for the reader.                              | `boolean`                                                       | `false`                               |
| `symbolicValues`                | Symbolic value map with the keys being symbols without the `##` prefix.   | `Map<Symbol, Object>`                                           | Map with keys `NaN`, `Inf` and `-Inf` |

To copy options with some changed, you can use the following:

```java
import de.kleinert.edna.Edna;

var opts = Edna.defaultOptions().copy(builder -> builder
        .allowZeroPrefix(true)
        .allowMetaData(true)
        //.<OptionName>(...)
);
```

## Examples

```java
void example() {
    println(Edna.read("symbol")); // Symbol without namespace
    println(Edna.read("namespace/symbol")); // Symbol
    println(Edna.read(":keyword")); // Keyword without namespace
    println(Edna.read(":namespace/keyword")); // Keyword
    println(Edna.read("\"string\"")); // String
    println(Edna.read("\\c")); // Character

    println(Edna.read("(list elements)")); // List
    println(Edna.read("[vector elements]")); // Vector
    println(Edna.read("#{set elements}")); // Set
    println(Edna.read("{map-key map-value}")); // Map

    println(Edna.read("12648430")); // Long
    println(Edna.read("12648430N")); // The same as BigInt
    println(Edna.read("5.0")); // Double
    println(Edna.read("5.0M")); // BigDecimal

    println(Edna.read("nil")); // null
    println(Edna.read("false")); // false
    println(Edna.read("true")); // true

    println(Edna.read("#uuid \"f81d4fae-7dec-11d0-a765-00a0c91e6bf6\"")); // UUID
    println(Edna.read("#inst \"1985-04-12T23:20:50.52Z\"")); // Instant
  
    println(Edna.read("0xC0FFEE", Edna.extendedOptions())); // Long, hex prefix requires additional setting
    println(Edna.read("^:a abc", Edna.extendedOptions())); // Meta
}
```

☑ 