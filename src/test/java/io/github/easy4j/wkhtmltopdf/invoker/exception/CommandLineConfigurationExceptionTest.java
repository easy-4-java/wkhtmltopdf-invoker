package io.github.easy4j.wkhtmltopdf.invoker.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for {@link CommandLineConfigurationException}.
 */
public class CommandLineConfigurationExceptionTest {

    @Test
    public void shouldCreateExceptionWithMessage() {
        CommandLineConfigurationException ex = new CommandLineConfigurationException("test error");
        assertEquals("test error", ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    public void shouldCreateExceptionWithMessageAndCause() {
        RuntimeException cause = new RuntimeException("root cause");
        CommandLineConfigurationException ex = new CommandLineConfigurationException("test error", cause);
        assertEquals("test error", ex.getMessage());
        assertSame(cause, ex.getCause());
    }

    @Test
    public void shouldAcceptNullMessage() {
        CommandLineConfigurationException ex = new CommandLineConfigurationException(null);
        assertNull(ex.getMessage());
    }

    @Test
    public void shouldAcceptNullCause() {
        CommandLineConfigurationException ex = new CommandLineConfigurationException("msg", null);
        assertEquals("msg", ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    public void shouldBeInstanceOfException() {
        CommandLineConfigurationException ex = new CommandLineConfigurationException("msg");
        assertTrue(ex instanceof Exception);
    }
}
