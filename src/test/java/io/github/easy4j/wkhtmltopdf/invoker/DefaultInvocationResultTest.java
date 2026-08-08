package io.github.easy4j.wkhtmltopdf.invoker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.codehaus.plexus.util.cli.CommandLineException;
import org.junit.Test;

/**
 * Tests for {@link DefaultInvocationResult}.
 */
public class DefaultInvocationResultTest {

    @Test
    public void shouldReturnMinValueExitCodeByDefault() {
        DefaultInvocationResult result = new DefaultInvocationResult();
        assertEquals(Integer.MIN_VALUE, result.getExitCode());
    }

    @Test
    public void shouldReturnNullExecutionExceptionByDefault() {
        DefaultInvocationResult result = new DefaultInvocationResult();
        assertNull(result.getExecutionException());
    }

    @Test
    public void shouldSetAndGetExitCode() {
        DefaultInvocationResult result = new DefaultInvocationResult();
        result.setExitCode(0);
        assertEquals(0, result.getExitCode());
        result.setExitCode(1);
        assertEquals(1, result.getExitCode());
        result.setExitCode(-1);
        assertEquals(-1, result.getExitCode());
    }

    @Test
    public void shouldSetAndGetExecutionException() {
        DefaultInvocationResult result = new DefaultInvocationResult();
        CommandLineException ex = new CommandLineException("test error");
        result.setExecutionException(ex);
        assertEquals(ex, result.getExecutionException());
        assertEquals("test error", result.getExecutionException().getMessage());
    }

    @Test
    public void shouldClearExecutionException() {
        DefaultInvocationResult result = new DefaultInvocationResult();
        result.setExecutionException(new CommandLineException("error"));
        result.setExecutionException(null);
        assertNull(result.getExecutionException());
    }
}
