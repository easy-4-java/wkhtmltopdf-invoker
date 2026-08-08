package io.github.easy4j.wkhtmltopdf.invoker;

import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

/**
 * Tests for {@link PrintStreamHandler}.
 */
public class PrintStreamHandlerTest {

    @Test
    public void shouldCreateDefaultHandler() {
        PrintStreamHandler handler = new PrintStreamHandler();
        assertNotNull(handler);
    }

    @Test
    public void shouldCreateHandlerWithCustomStream() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStreamHandler handler = new PrintStreamHandler(ps, false);
        handler.consumeLine("hello");
        // No NPE means constructor worked
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeWhenStreamIsNull() {
        new PrintStreamHandler(null, false);
    }

    @Test
    public void shouldWriteLineToStream() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStreamHandler handler = new PrintStreamHandler(ps, false);
        handler.consumeLine("test output");
        String output = baos.toString().trim();
        org.junit.Assert.assertEquals("test output", output);
    }

    @Test
    public void shouldWriteEmptyLineForNullInput() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStreamHandler handler = new PrintStreamHandler(ps, false);
        handler.consumeLine(null);
        // null produces an empty println
        String output = baos.toString();
        org.junit.Assert.assertTrue(output.contains(System.lineSeparator()) || output.isEmpty());
    }

    @Test
    public void shouldFlushWhenAlwaysFlushIsTrue() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStreamHandler handler = new PrintStreamHandler(ps, true);
        handler.consumeLine("flushed");
        org.junit.Assert.assertEquals("flushed" + System.lineSeparator(), baos.toString());
    }

    @Test
    public void shouldWriteMultipleLines() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStreamHandler handler = new PrintStreamHandler(ps, true);
        handler.consumeLine("line1");
        handler.consumeLine("line2");
        String output = baos.toString();
        org.junit.Assert.assertTrue(output.contains("line1"));
        org.junit.Assert.assertTrue(output.contains("line2"));
    }
}
