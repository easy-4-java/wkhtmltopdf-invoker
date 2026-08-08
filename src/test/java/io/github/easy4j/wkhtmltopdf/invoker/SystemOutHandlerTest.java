package io.github.easy4j.wkhtmltopdf.invoker;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for {@link SystemOutHandler}.
 */
public class SystemOutHandlerTest {

    @Test
    public void shouldCreateDefaultHandler() {
        SystemOutHandler handler = new SystemOutHandler();
        assertNotNull(handler);
        assertTrue(handler instanceof InvocationOutputHandler);
    }

    @Test
    public void shouldCreateHandlerWithFlushEnabled() {
        SystemOutHandler handler = new SystemOutHandler(true);
        assertNotNull(handler);
    }

    @Test
    public void shouldCreateHandlerWithFlushDisabled() {
        SystemOutHandler handler = new SystemOutHandler(false);
        assertNotNull(handler);
    }

    @Test
    public void shouldConsumeLineWithoutError() {
        SystemOutHandler handler = new SystemOutHandler();
        handler.consumeLine("test line");
        handler.consumeLine(null);
        handler.consumeLine("");
    }

    @Test
    public void shouldBeInstanceOfPrintStreamHandler() {
        SystemOutHandler handler = new SystemOutHandler();
        assertTrue(handler instanceof PrintStreamHandler);
    }
}
