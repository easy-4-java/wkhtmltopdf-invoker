package io.github.easy4j.wkhtmltopdf.invoker.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for {@link WkhtmlToPdfInvocationException}.
 */
public class WkhtmlToPdfInvocationExceptionTest {

    @Test
    public void shouldCreateExceptionWithMessage() {
        WkhtmlToPdfInvocationException ex = new WkhtmlToPdfInvocationException("invocation failed");
        assertEquals("invocation failed", ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    public void shouldCreateExceptionWithMessageAndCause() {
        RuntimeException cause = new RuntimeException("root cause");
        WkhtmlToPdfInvocationException ex = new WkhtmlToPdfInvocationException("invocation failed", cause);
        assertEquals("invocation failed", ex.getMessage());
        assertSame(cause, ex.getCause());
    }

    @Test
    public void shouldAcceptNullMessage() {
        WkhtmlToPdfInvocationException ex = new WkhtmlToPdfInvocationException(null);
        assertNull(ex.getMessage());
    }

    @Test
    public void shouldAcceptNullCause() {
        WkhtmlToPdfInvocationException ex = new WkhtmlToPdfInvocationException("msg", null);
        assertEquals("msg", ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    public void shouldBeInstanceOfException() {
        WkhtmlToPdfInvocationException ex = new WkhtmlToPdfInvocationException("msg");
        assertTrue(ex instanceof Exception);
    }
}
