package de.kleinert.edna.reader;

import de.kleinert.edna.Edna;
import de.kleinert.edna.data.Symbol;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Assertions;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

class EdnaReaderDispatchTaggedUserDefTest {
    @Test
    void parseDecoderNameErrorTest() {
        // invalid name
        {
            var options = Edna.defaultOptions().copy((builder ->
                    builder.taggedElementDecoders(Map.of("pair", (e) -> e))
            ));
            Assertions.assertThrows(EdnaReaderException.class, () -> Edna.read("1", options));
        }

        // Valid name with option
        {
            var options = Edna.defaultOptions().copy(b ->
                    b.allowTaggedElementsWithoutNS(true)
                            .taggedElementDecoders(Map.of("pair", (e) -> e))
            );
            Edna.read("1", options);
        }

        // Valid name with option
        {
            Map<String, Function<Object, Object>> decoders = Map.of("pair", (e) -> e);
            var options = Edna.defaultOptions().copy(b ->
                    b.allowTaggedElementsWithoutNS(true)
                            .taggedElementDecoders(decoders)
            );
            Edna.read("1", options);
        }
    }

    @Test
    void parseDecoderTest() {
        Function<Object, Object> mapOrListToPair = (o) -> switch (o) {
            case Map<?, ?> m -> List.of(m.get("tag"), m.get("element"));
            case List<?> l -> List.of(l.get(0), l.get(1));
            default -> throw new IllegalArgumentException();
        };

        var decoders = Map.of("my/pair", mapOrListToPair);

        Assertions.assertEquals(
                List.of(4L, 5L),
                Edna.read(
                        "#my/pair {\"tag\" 4 \"element\" 5}",
                        Edna.defaultOptions().copy(b -> b.taggedElementDecoders(decoders))
                )
        );
        Assertions.assertEquals(
                List.of(4L, 5L),
                Edna.read(
                        "#my/pair [4 5]",
                        Edna.defaultOptions().copy(b -> b.taggedElementDecoders(decoders))
                )
        );
    }

    @Test
    void parseDecoderReferencingExternal() {
        var uniqueObj = new Object();
        var refs = Map.<Object, Object>of(Symbol.symbol("abc"), uniqueObj);
        var decoders = Map.of("edna/ref", (Function<Object, Object>) symbol -> refs.getOrDefault(symbol, 1));
        var options = Edna.defaultOptions().copy(b -> b.taggedElementDecoders(decoders));
        Assertions.assertSame(uniqueObj, Edna.read("#edna/ref abc", options));
    }
    @Test void parseDecoderListRepeater(){}
}