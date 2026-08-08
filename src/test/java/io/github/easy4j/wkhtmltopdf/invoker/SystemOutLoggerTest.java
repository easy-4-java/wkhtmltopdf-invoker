package io.github.easy4j.wkhtmltopdf.invoker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for {@link SystemOutLogger}.
 */
public class SystemOutLoggerTest {

    @Test
    public void shouldCreateLogger() {
        SystemOutLogger logger = new SystemOutLogger();
        assertNotNull(logger);
    }

    @Test
    public void shouldHaveInfoThresholdByDefault() {
        SystemOutLogger logger = new SystemOutLogger();
        assertEquals(InvokerLogger.INFO, logger.getThreshold());
    }

    @Test
    public void shouldBeInstanceOfPrintStreamLogger() {
        SystemOutLogger logger = new SystemOutLogger();
        assertTrue(logger instanceof PrintStreamLogger);
    }

    @Test
    public void shouldBeInstanceOfInvokerLogger() {
        SystemOutLogger logger = new SystemOutLogger();
        assertTrue(logger instanceof InvokerLogger);
    }

    @Test
    public void shouldHaveDebugEnabledFalseByDefault() {
        SystemOutLogger logger = new SystemOutLogger();
        assertFalse(logger.isDebugEnabled());
    }

    @Test
    public void shouldHaveInfoEnabledTrueByDefault() {
        SystemOutLogger logger = new SystemOutLogger();
        assertTrue(logger.isInfoEnabled());
    }

    @Test
    public void shouldAllowThresholdChange() {
        SystemOutLogger logger = new SystemOutLogger();
        logger.setThreshold(InvokerLogger.DEBUG);
        assertTrue(logger.isDebugEnabled());
        assertEquals(InvokerLogger.DEBUG, logger.getThreshold());
    }
}
