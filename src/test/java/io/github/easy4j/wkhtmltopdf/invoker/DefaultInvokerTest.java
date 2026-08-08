package io.github.easy4j.wkhtmltopdf.invoker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import io.github.easy4j.wkhtmltopdf.invoker.command.WkhtmlToImageCommandLineBuilder;
import io.github.easy4j.wkhtmltopdf.invoker.command.WkhtmlToPdfCommandLineBuilder;
import io.github.easy4j.wkhtmltopdf.invoker.request.DefaultWkhtmlToImageInvocationRequest;
import io.github.easy4j.wkhtmltopdf.invoker.request.DefaultWkhtmlToPdfInvocationRequest;
import io.github.easy4j.wkhtmltopdf.invoker.request.InvocationRequest;

/**
 * Tests for {@link DefaultInvoker}.
 */
public class DefaultInvokerTest {

    @Test
    public void shouldReturnDefaultLoggerWhenNoneSet() {
        DefaultInvoker invoker = new DefaultInvoker();
        assertNotNull(invoker.getLogger());
        assertTrue(invoker.getLogger() instanceof SystemOutLogger);
    }

    @Test
    public void shouldSetAndGetLogger() {
        DefaultInvoker invoker = new DefaultInvoker();
        InvokerLogger customLogger = new PrintStreamLogger();
        Invoker returned = invoker.setLogger(customLogger);
        assertSame(customLogger, invoker.getLogger());
        assertSame(invoker, returned);
    }

    @Test
    public void shouldRevertToDefaultLoggerWhenNullPassed() {
        DefaultInvoker invoker = new DefaultInvoker();
        invoker.setLogger(new PrintStreamLogger());
        invoker.setLogger(null);
        assertNotNull(invoker.getLogger());
        assertTrue(invoker.getLogger() instanceof SystemOutLogger);
    }

    @Test
    public void shouldSetAndGetWorkingDirectory() {
        DefaultInvoker invoker = new DefaultInvoker();
        assertNull(invoker.getWorkingDirectory());
        File dir = new File("/tmp/work");
        Invoker returned = invoker.setWorkingDirectory(dir);
        assertEquals(dir, invoker.getWorkingDirectory());
        assertSame(invoker, returned);
    }

    @Test
    public void shouldSetAndGetWkhtmltopdfHome() {
        DefaultInvoker invoker = new DefaultInvoker();
        assertNull(invoker.getWkhtmltopdfHome());
        File home = new File("/usr/local/wkhtmltopdf");
        Invoker returned = invoker.setWkhtmltopdfHome(home);
        assertEquals(home, invoker.getWkhtmltopdfHome());
        assertSame(invoker, returned);
    }

    @Test
    public void shouldReturnWkhtmlToPdfBuilderForPdfRequest() {
        DefaultInvoker invoker = new DefaultInvoker();
        InvocationRequest request = new DefaultWkhtmlToPdfInvocationRequest();
        assertTrue(invoker.getCommandLineBuilder(request) instanceof WkhtmlToPdfCommandLineBuilder);
    }

    @Test
    public void shouldReturnWkhtmlToImageBuilderForImageRequest() {
        DefaultInvoker invoker = new DefaultInvoker();
        InvocationRequest request = new DefaultWkhtmlToImageInvocationRequest();
        assertTrue(invoker.getCommandLineBuilder(request) instanceof WkhtmlToImageCommandLineBuilder);
    }

    @Test
    public void shouldReturnNullBuilderForUnknownRequestType() {
        DefaultInvoker invoker = new DefaultInvoker();
        InvocationRequest request = new InvocationRequest() {
            public boolean isShellEnvironmentInherited() { return false; }
            public boolean isVerbose() { return false; }
            public InvocationOutputHandler getOutputHandler(InvocationOutputHandler h) { return h; }
            public InvocationOutputHandler getErrorHandler(InvocationOutputHandler h) { return h; }
            public File getWkhtmltopdfHome() { return null; }
            public java.util.Properties getProperties() { return null; }
            public java.util.List<String> getGoals() { return null; }
            public java.util.Map<String, String> getShellEnvironments() { return null; }
            public boolean isCollate() { return false; }
            public File getCookieJar() { return null; }
            public int getCopies() { return 1; }
            public int getDpi() { return 96; }
            public boolean isGrayscale() { return false; }
            public int getImageDpi() { return 600; }
            public InvocationRequest setOutputHandler(InvocationOutputHandler h) { return this; }
            public InvocationRequest setErrorHandler(InvocationOutputHandler h) { return this; }
            public InvocationRequest setWkhtmltopdfHome(File f) { return this; }
            public InvocationRequest setProperties(java.util.Properties p) { return this; }
            public InvocationRequest setGoals(java.util.List<String> g) { return this; }
            public InvocationRequest setShellEnvironmentInherited(boolean b) { return this; }
            public InvocationRequest addShellEnvironment(String n, String v) { return this; }
            public InvocationRequest setCollate(boolean b) { return this; }
            public InvocationRequest setCookieJar(File f) { return this; }
            public InvocationRequest setCopies(int c) { return this; }
            public InvocationRequest setDpi(int d) { return this; }
            public InvocationRequest setGrayscale(boolean g) { return this; }
            public InvocationRequest setImageDpi(int d) { return this; }
            public InvocationRequest setOutputFile(File f) { return this; }
        };
        assertNull(invoker.getCommandLineBuilder(request));
    }

    @Test
    public void shouldReturnRoleHintConstant() {
        assertEquals("default", DefaultInvoker.ROLE_HINT);
    }

    @Test
    public void shouldSetErrorHandler() {
        DefaultInvoker invoker = new DefaultInvoker();
        InvocationOutputHandler handler = new SystemOutHandler();
        Invoker returned = invoker.setErrorHandler(handler);
        assertSame(invoker, returned);
    }

    @Test
    public void shouldSetOutputHandler() {
        DefaultInvoker invoker = new DefaultInvoker();
        InvocationOutputHandler handler = new SystemOutHandler();
        Invoker returned = invoker.setOutputHandler(handler);
        assertSame(invoker, returned);
    }
}
