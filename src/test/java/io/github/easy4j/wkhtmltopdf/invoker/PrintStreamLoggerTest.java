package io.github.easy4j.wkhtmltopdf.invoker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

/**
 * Tests for {@link PrintStreamLogger}.
 */
public class PrintStreamLoggerTest {

    @Test
    public void shouldCreateDefaultLogger() {
        PrintStreamLogger logger = new PrintStreamLogger();
        assertEquals(InvokerLogger.INFO, logger.getThreshold());
    }

    @Test
    public void shouldCreateLoggerWithCustomThreshold() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStreamLogger logger = new PrintStreamLogger(new PrintStream(baos), InvokerLogger.DEBUG);
        assertEquals(InvokerLogger.DEBUG, logger.getThreshold());
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeWhenStreamIsNull() {
        new PrintStreamLogger(null, InvokerLogger.INFO);
    }

    @Test
    public void shouldSetAndGetThreshold() {
        PrintStreamLogger logger = new PrintStreamLogger();
        logger.setThreshold(InvokerLogger.DEBUG);
        assertEquals(InvokerLogger.DEBUG, logger.getThreshold());
        logger.setThreshold(InvokerLogger.ERROR);
        assertEquals(InvokerLogger.ERROR, logger.getThreshold());
    }

    @Test
    public void shouldReportDebugEnabledWhenThresholdIsDebug() {
        PrintStreamLogger logger = new PrintStreamLogger();
        logger.setThreshold(InvokerLogger.DEBUG);
        assertTrue(logger.isDebugEnabled());
        assertTrue(logger.isInfoEnabled());
        assertTrue(logger.isWarnEnabled());
        assertTrue(logger.isErrorEnabled());
        assertTrue(logger.isFatalErrorEnabled());
    }

    @Test
    public void shouldReportDebugDisabledWhenThresholdIsInfo() {
        PrintStreamLogger logger = new PrintStreamLogger();
        logger.setThreshold(InvokerLogger.INFO);
        assertFalse(logger.isDebugEnabled());
        assertTrue(logger.isInfoEnabled());
        assertTrue(logger.isWarnEnabled());
        assertTrue(logger.isErrorEnabled());
        assertTrue(logger.isFatalErrorEnabled());
    }

    @Test
    public void shouldReportOnlyErrorAndFatalWhenThresholdIsError() {
        PrintStreamLogger logger = new PrintStreamLogger();
        logger.setThreshold(InvokerLogger.ERROR);
        assertFalse(logger.isDebugEnabled());
        assertFalse(logger.isInfoEnabled());
        assertFalse(logger.isWarnEnabled());
        assertTrue(logger.isErrorEnabled());
        assertTrue(logger.isFatalErrorEnabled());
    }

    @Test
    public void shouldReportOnlyFatalWhenThresholdIsFatal() {
        PrintStreamLogger logger = new PrintStreamLogger();
        logger.setThreshold(InvokerLogger.FATAL);
        assertFalse(logger.isDebugEnabled());
        assertFalse(logger.isInfoEnabled());
        assertFalse(logger.isWarnEnabled());
        assertFalse(logger.isErrorEnabled());
        assertTrue(logger.isFatalErrorEnabled());
    }

    @Test
    public void shouldLogDebugMessage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStreamLogger logger = new PrintStreamLogger(new PrintStream(baos), InvokerLogger.DEBUG);
        logger.debug("debug msg");
        assertTrue(baos.toString().contains("[DEBUG]"));
        assertTrue(baos.toString().contains("debug msg"));
    }

    @Test
    public void shouldNotLogDebugWhenThresholdIsInfo() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStreamLogger logger = new PrintStreamLogger(new PrintStream(baos), InvokerLogger.INFO);
        logger.debug("should not appear");
        assertEquals("", baos.toString());
    }

    @Test
    public void shouldLogInfoMessage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStreamLogger logger = new PrintStreamLogger(new PrintStream(baos), InvokerLogger.INFO);
        logger.info("info msg");
        assertTrue(baos.toString().contains("[INFO]"));
        assertTrue(baos.toString().contains("info msg"));
    }

    @Test
    public void shouldLogWarnMessage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStreamLogger logger = new PrintStreamLogger(new PrintStream(baos), InvokerLogger.WARN);
        logger.warn("warn msg");
        assertTrue(baos.toString().contains("[WARN]"));
        assertTrue(baos.toString().contains("warn msg"));
    }

    @Test
    public void shouldLogErrorMessage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStreamLogger logger = new PrintStreamLogger(new PrintStream(baos), InvokerLogger.ERROR);
        logger.error("error msg");
        assertTrue(baos.toString().contains("[ERROR]"));
        assertTrue(baos.toString().contains("error msg"));
    }

    @Test
    public void shouldLogFatalMessage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStreamLogger logger = new PrintStreamLogger(new PrintStream(baos), InvokerLogger.FATAL);
        logger.fatalError("fatal msg");
        assertTrue(baos.toString().contains("[FATAL]"));
        assertTrue(baos.toString().contains("fatal msg"));
    }

    @Test
    public void shouldLogMessageWithThrowable() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStreamLogger logger = new PrintStreamLogger(new PrintStream(baos), InvokerLogger.DEBUG);
        logger.debug("with exception", new RuntimeException("boom"));
        String output = baos.toString();
        assertTrue(output.contains("[DEBUG]"));
        assertTrue(output.contains("with exception"));
        assertTrue(output.contains("RuntimeException"));
        assertTrue(output.contains("boom"));
    }

    @Test
    public void shouldNotLogWhenBothMessageAndThrowableAreNull() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStreamLogger logger = new PrintStreamLogger(new PrintStream(baos), InvokerLogger.DEBUG);
        logger.debug(null, null);
        assertEquals("", baos.toString());
    }

    @Test
    public void shouldLogThrowableOnly() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStreamLogger logger = new PrintStreamLogger(new PrintStream(baos), InvokerLogger.DEBUG);
        logger.error(null, new RuntimeException("test"));
        String output = baos.toString();
        assertTrue(output.contains("[ERROR]"));
        assertTrue(output.contains("RuntimeException"));
    }

    @Test
    public void shouldLogInfoWithThrowable() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStreamLogger logger = new PrintStreamLogger(new PrintStream(baos), InvokerLogger.INFO);
        logger.info("info", new RuntimeException("err"));
        assertTrue(baos.toString().contains("[INFO]"));
        assertTrue(baos.toString().contains("Error:"));
    }

    @Test
    public void shouldLogWarnWithThrowable() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStreamLogger logger = new PrintStreamLogger(new PrintStream(baos), InvokerLogger.WARN);
        logger.warn("warn", new RuntimeException("err"));
        assertTrue(baos.toString().contains("[WARN]"));
    }

    @Test
    public void shouldLogErrorWithThrowable() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStreamLogger logger = new PrintStreamLogger(new PrintStream(baos), InvokerLogger.ERROR);
        logger.error("err", new RuntimeException("boom"));
        assertTrue(baos.toString().contains("[ERROR]"));
    }

    @Test
    public void shouldLogFatalWithThrowable() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStreamLogger logger = new PrintStreamLogger(new PrintStream(baos), InvokerLogger.FATAL);
        logger.fatalError("fatal", new RuntimeException("boom"));
        assertTrue(baos.toString().contains("[FATAL]"));
    }

    @Test
    public void shouldReportWarnEnabledWhenThresholdIsWarn() {
        PrintStreamLogger logger = new PrintStreamLogger();
        logger.setThreshold(InvokerLogger.WARN);
        assertFalse(logger.isDebugEnabled());
        assertFalse(logger.isInfoEnabled());
        assertTrue(logger.isWarnEnabled());
        assertTrue(logger.isErrorEnabled());
        assertTrue(logger.isFatalErrorEnabled());
    }

    @Test
    public void shouldDefineLogLevelConstants() {
        assertEquals(4, InvokerLogger.DEBUG);
        assertEquals(3, InvokerLogger.INFO);
        assertEquals(2, InvokerLogger.WARN);
        assertEquals(1, InvokerLogger.ERROR);
        assertEquals(0, InvokerLogger.FATAL);
    }
}
