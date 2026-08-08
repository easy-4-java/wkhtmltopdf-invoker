package io.github.easy4j.wkhtmltopdf.invoker.request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

/**
 * Tests for {@link DefaultWkhtmlToImageInvocationRequest}.
 */
public class DefaultWkhtmlToImageInvocationRequestTest {

    @Test
    public void shouldReturnDefaultValues() {
        DefaultWkhtmlToImageInvocationRequest request = new DefaultWkhtmlToImageInvocationRequest();
        assertFalse(request.isLrs());
        assertNull(request.getLrsFile());
        assertNull(request.getOutputDirectory());
    }

    @Test
    public void shouldSetAndGetLrs() {
        DefaultWkhtmlToImageInvocationRequest request = new DefaultWkhtmlToImageInvocationRequest();
        InvocationRequest returned = request.setLrs(true);
        assertTrue(request.isLrs());
        assertSame(request, returned);
    }

    @Test
    public void shouldSetAndGetLrsFile() {
        DefaultWkhtmlToImageInvocationRequest request = new DefaultWkhtmlToImageInvocationRequest();
        File file = new File("/tmp/test.lrs");
        InvocationRequest returned = request.setLrsFile(file);
        assertEquals(file, request.getLrsFile());
        assertSame(request, returned);
    }

    @Test
    public void shouldSetAndGetOutputDirectory() {
        DefaultWkhtmlToImageInvocationRequest request = new DefaultWkhtmlToImageInvocationRequest();
        File dir = new File("/tmp/output");
        InvocationRequest returned = request.setOutputDirectory(dir);
        assertEquals(dir, request.getOutputDirectory());
        assertSame(request, returned);
    }

    @Test
    public void shouldImplementWkhtmlToImageInvocationRequest() {
        DefaultWkhtmlToImageInvocationRequest request = new DefaultWkhtmlToImageInvocationRequest();
        assertTrue(request instanceof WkhtmlToImageInvocationRequest);
    }

    @Test
    public void shouldInheritAbstractInvocationRequestMethods() {
        DefaultWkhtmlToImageInvocationRequest request = new DefaultWkhtmlToImageInvocationRequest();

        // Test collate
        assertFalse(request.isCollate());
        request.setCollate(true);
        assertTrue(request.isCollate());

        // Test copies
        assertEquals(0, request.getCopies());
        request.setCopies(2);
        assertEquals(2, request.getCopies());

        // Test DPI
        request.setDpi(96);
        assertEquals(96, request.getDpi());

        // Test grayscale
        request.setGrayscale(true);
        assertTrue(request.isGrayscale());

        // Test image DPI
        request.setImageDpi(600);
        assertEquals(600, request.getImageDpi());

        // Test verbose
        assertFalse(request.isVerbose());
        request.setVerbose(true);
        assertTrue(request.isVerbose());

        // Test debug
        request.setDebug(true);
        assertTrue(request.isDebug());

        // Test shell environment
        assertTrue(request.isShellEnvironmentInherited());
        request.setShellEnvironmentInherited(false);
        assertFalse(request.isShellEnvironmentInherited());
    }

    @Test
    public void shouldSupportFluentChaining() {
        DefaultWkhtmlToImageInvocationRequest request = new DefaultWkhtmlToImageInvocationRequest();
        request.setLrs(true);
        request.setOutputDirectory(new File("/tmp/out"));
        request.setLrsFile(new File("/tmp/test.lrs"));
        assertTrue(request.isLrs());
        assertEquals(new File("/tmp/out"), request.getOutputDirectory());
        assertEquals(new File("/tmp/test.lrs"), request.getLrsFile());
    }
}
