# Edna

An EDN library for Java with options for some additional features. The file format specification can be found in the [edn-format  github repository](https://github.com/edn-format/edn). Additional inspiration was taken directly from [Clojure's source code for the LispReader class](https://github.com/clojure/clojure/blob/1fa5b038a434da34f787e3aec56f9cb48ed4dd99/src/jvm/clojure/lang/LispReader.java) and other implementations of the EDN file format, primarily [edn-ruby](https://github.com/relevance/edn-ruby).

I made this library as a replacement for my [Kotlin EDN library](https://github.com/ArminKleinert/ExtensibleDataSoup) for *personal use*. 

## Standard things implemented

- [x] Can read from strings, files, inputstreams and readers.
- [x] Uses UTF-8 for inputs.
- [x] Can write to strings, files and any `Appendable`.


- [x] Can parse any type mandated by the EDN specification.
- [x] [Tagged elements](https://github.com/edn-format/edn#tagged-elements)
    - Note that Edna handles tagged elements as function that take an object, not a Map, as input. This allows doing
      much more than just instantiate class instances.
    - Edna can not infer any type and create any object from a tag.

## Quasi-standard functions

These features are not in the specification, but most people implement them anyway.

- [x] [Metadata](https://clojure.org/guides/weird_characters#_and_metadata) (optional because they are not mandated by
  the specification)
    - Most parsers would implement this, but it's actually not mandated by the specification.
    - If metadata different metadata maps are applied to the same object, they are merged. This idea comes
      from [edn-ruby](https://github.com/relevance/edn-ruby#metadata).
- [x] [Symbolic values](https://clojure.org/guides/weird_characters#_symbolic_values) (optional because they are not
  mandated by the specification)
    - The user can define their own symbolic values, but `NaN`, `Inf` and `-Inf` are available when the
      `allowSymbolicValues` option is true.
    - Since EDN technically does not mandate symbolic values, but the linked guide says that they are available, I
      eventually decided to make them optional.

## Extended features

- [x] Casting values read to get objects of concrete classes.
- [x] Parse inputs that contain multiple values via ```java Edna.readMulti(...)```.
    - I was inspired by [edn-ruby](https://github.com/relevance/edn-ruby#ednreader), except that Edna parses values
      eagerly.
- [x] Symbols and keywords with Unicode characters. This requires the `allowUTFSymbols` option.
- [x] Tagged elements without a namespace. Available through the `allowTaggedElementsWithoutNS` option.
- [x] Supports octal chars like Clojure with the `allowBase8Chars` option.
- [x] Supports more character literals through a `Char32` type which represents a Unicode codepoint. If the
  `allowDispatchChars` option is active, the user can prefix a char literal with a dispatch ("#") character to get a
  Char32.
    - Variant 1: `#\\uXXXXXXXX` (backslash followed by 'x' followed by 8 hexadecimal symbols)
    - Variant 2: `#\\xXXXXXXXX` (backslash followed by 'x' followed by 8 hexadecimal symbols). This requires the
      additional `allowSchemeUTF32Codes` option.
    - Variant 3: Prefix any normal char literal with a dispatch character.
- [x] Numeric prefixes
    - `0x` (hexadecimal), `Oo` (octal), `0b` (binary)
    - `XXr` for anything. E.g. `10r255` is 255 base 10, `16rFF` is 255 base 16, etc.
    - Optionally allows
- [x] User-defined symbolic values. If the `allowSymbolicValues` option is true and `symbolicValues` is not empty, the user can insert their own symbolic values. I advise against it, but it's possible.

## Options

| Name                            | Meaning                                                                   | Type                                                            | Default                               |
|---------------------------------|---------------------------------------------------------------------------|-----------------------------------------------------------------|---------------------------------------|
| `allowSchemeUTF32Codes`         | Allow `\xXXXXXXXX` Char32 literals.                                       | `boolean`                                                       | `false`                               |
| `allowDispatchChars`            | Allow Char32 literals.                                                    | `boolean`                                                       | `false`                               |
| `allowBase8Chars`               | Allow octal chars.                                                        | `boolean`                                                       | `false`                               |
| `taggedElementDecoders`         | Converters for tagged elements.                                           | `Map<String, Function<Object, Object>>`                         | Empty map                             |
| `taggedElementEncoders`         |                                                                           | `SequencedMap<Class<?>, Function<Object, EdnaPair<String, ?>>>` | Empty map                             |
| `moreNumberPrefixes`            | `0x` (hex), `0o` (octal), `0b` (binary) and `XXr` prefixes for integrals. | `boolean`                                                       | `false`                               |
| `allowTaggedElementsWithoutNS`  | Allow tagged elements without namespaces.                                 | `boolean`                                                       | `false`                               |
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

To copy options with some changed, you can use the `EdnaOptions.copy` method. It takes a function which instantiates a
builder object. Options can be applied to it by writing the name and the new value. Example:

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