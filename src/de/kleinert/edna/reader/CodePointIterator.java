package de.kleinert.edna.reader;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Arrays;
import java.util.PrimitiveIterator;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

public final class CodePointIterator implements PrimitiveIterator.OfInt, AutoCloseable {
    private final @NotNull PrimitiveIterator.OfInt iterator;
    private int @NotNull [] memory;
    private int memoryIndex = -1;

    private int textIndex = 0;
    private int lineIdx = 0;

    public CodePointIterator(final @NotNull IntStream codepointStream) {
        this.iterator = codepointStream.iterator();
        this.memory = new int[32];
    }

    public CodePointIterator(final @NotNull InputStream reader) {
        this.iterator = new IntermediateInputStream(reader);
        this.memory = new int[32];
    }

    public CodePointIterator(final @NotNull Reader reader) {
        this.iterator = new IntermediateReader(new BufferedReader(reader));
        this.memory = new int[32];
    }

    @Override
    public int nextInt() {
        final int code;
        if (memoryIndex >= 0) {
            code = memory[memoryIndex];
            memoryIndex--;
            textIndex++;
        } else if (iterator.hasNext()) {
            code = iterator.nextInt();
            textIndex++;
        } else {
            code = -1;
        }
        if (code == (int) '\n') {
            lineIdx++;
        }
        return code;
    }

    @Override
    public boolean hasNext() {
        return memoryIndex >= 0 || iterator.hasNext();
    }

    public int peek() {
        if (!hasNext())
            throw new IllegalStateException("No more codepoints left.");
        final int temp = nextInt();
        unread(temp);
        return temp;
    }

    public void unread(final int v) {
        memoryIndex++;
        if (memoryIndex == memory.length) {
            memory = Arrays.copyOf(memory, memory.length + Integer.min(memory.length / 2, 8));
        }
        memory[memoryIndex] = v;
        textIndex--;
        if (v == (int) '\n') {
            lineIdx--;
        }
    }

    public @NotNull StringBuilder takeCodePoints(final @NotNull StringBuilder dest, final @NotNull IntPredicate condition) {
        while (hasNext()) {
            final int codepoint = nextInt();
            if (!condition.test(codepoint)) {
                unread(codepoint);
                break;
            }
            dest.appendCodePoint(codepoint);
        }
        return dest;
    }

    public @NotNull StringBuilder takeCodePoints(final @NotNull StringBuilder dest, final int maxCount, final @NotNull IntPredicate condition) {
        int count = 0;
        while (count < maxCount && hasNext()) {
            int codepoint = nextInt();
            if (!condition.test(codepoint)) {
                unread(codepoint);
                break;
            }
            dest.appendCodePoint(codepoint);
            count++;
        }
        return dest;
    }

    public void skipLine() {
        while (hasNext()) {
            int ni = nextInt();
            if (ni == (int) '\n') break;
        }
    }

    public void skipWhile(final @NotNull IntPredicate condition) {
        while (hasNext()) {
            int ni = nextInt();
            if (!condition.test(ni)) {
                unread(ni);
                break;
            }
        }
    }

    @Override
    public void close() throws Exception {
        if (iterator instanceof AutoCloseable) ((AutoCloseable) iterator).close();
    }

    public int getTextIndex() {
        return textIndex;
    }

    public int getLineIdx() {
        return lineIdx;
    }

    private static class IntermediateReader implements PrimitiveIterator.OfInt {
        private final BufferedReader input;
        private int memory = -1;

        public IntermediateReader(final @NotNull BufferedReader input) {
            this.input = input;
        }

        public boolean hasNext() {
            if (memory != -1) return true;
            final int temp;
            try {
                temp = input.read();
            } catch (final @NotNull IOException e) {
                throw new CPIException(e);
            }
            if (temp == -1) return false;
            memory = temp;
            return true;
        }

        public int nextInt() {
            if (memory != -1) {
                final int temp = memory;
                memory = -1;
                return temp;
            }
            try {
                return input.read();
            } catch (final @NotNull IOException e) {
                throw new CPIException(e);
            }
        }
    }


    private static class IntermediateInputStream implements PrimitiveIterator.OfInt {
        private final @NotNull InputStream input;
        private int memory = -1;

        public IntermediateInputStream(final @NotNull InputStream input) {
            this.input = input;
        }

        public boolean hasNext() {
            if (memory != -1) return true;
            final int temp;
            try {
                temp = input.read();
            } catch (IOException e) {
                throw new CPIException(e);
            }
            if (temp == -1) return false;
            memory = temp;
            return true;
        }

        public int nextInt() {
            if (memory != -1) {
                final int temp = memory;
                memory = -1;
                return temp;
            }
            try {
                return input.read();
            } catch (IOException e) {
                throw new CPIException(e);
            }
        }
    }

    public static class CPIException extends RuntimeException {
        CPIException(IOException ioe) {
            super(ioe);
        }
    }
}
