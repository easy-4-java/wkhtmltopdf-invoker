package io.github.easy4j.wkhtmltopdf.invoker.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.codehaus.plexus.util.cli.Commandline;
import org.junit.Test;

import io.github.easy4j.wkhtmltopdf.invoker.SystemOutLogger;
import io.github.easy4j.wkhtmltopdf.invoker.exception.CommandLineConfigurationException;
import io.github.easy4j.wkhtmltopdf.invoker.request.DefaultWkhtmlToImageInvocationRequest;

/**
 * Tests for {@link WkhtmlToImageCommandLineBuilder}.
 */
public class WkhtmlToImageCommandLineBuilderTest {

    @Test
    public void shouldReturnWkhtmltoimageExecutable() throws CommandLineConfigurationException, IOException {
        WkhtmlToImageCommandLineBuilder builder = new WkhtmlToImageCommandLineBuilder();
        File exe = builder.findWkhtmltopdfExecutable();
        assertNotNull(exe);
        assertEquals("wkhtmltoimage", exe.getName());
    }

    @Test(expected = CommandLineConfigurationException.class)
    public void shouldThrowWhenCalibreExecutableNotFound() throws CommandLineConfigurationException, IOException {
        WkhtmlToImageCommandLineBuilder builder = new WkhtmlToImageCommandLineBuilder();
        builder.findCalibreExecutable();
    }

    @Test
    public void shouldHaveDefaultLogger() {
        WkhtmlToImageCommandLineBuilder builder = new WkhtmlToImageCommandLineBuilder();
        assertNotNull(builder.getLogger());
        assertTrue(builder.getLogger() instanceof SystemOutLogger);
    }

    @Test
    public void shouldSetAndGetLogger() {
        WkhtmlToImageCommandLineBuilder builder = new WkhtmlToImageCommandLineBuilder();
        SystemOutLogger logger = new SystemOutLogger();
        builder.setLogger(logger);
        assertEquals(logger, builder.getLogger());
    }

    @Test
    public void shouldSetAndGetWkhtmltopdfHome() {
        WkhtmlToImageCommandLineBuilder builder = new WkhtmlToImageCommandLineBuilder();
        File home = new File("/usr/local/wkhtmltopdf");
        builder.setWkhtmltopdfHome(home);
        assertEquals(home, builder.getWkhtmltopdfHome());
    }

    @Test
    public void shouldSetAndGetWorkingDirectory() {
        WkhtmlToImageCommandLineBuilder builder = new WkhtmlToImageCommandLineBuilder();
        File dir = new File("/tmp/work");
        builder.setWorkingDirectory(dir);
        assertEquals(dir, builder.getWorkingDirectory());
    }

    @Test
    public void shouldBuildCommandLineWithLrsFile() throws CommandLineConfigurationException {
        WkhtmlToImageCommandLineBuilder builder = new WkhtmlToImageCommandLineBuilder();
        DefaultWkhtmlToImageInvocationRequest request = new DefaultWkhtmlToImageInvocationRequest();
        request.setLrsFile(new File("/tmp/test.lrs"));
        request.setShellEnvironmentInherited(false);

        Commandline cli = builder.build(request);
        assertNotNull(cli);
        String[] args = cli.getArguments();
        assertTrue(args.length > 0);
        assertEquals(new File("/tmp/test.lrs").getAbsolutePath(), args[args.length - 1]);
    }

    @Test
    public void shouldBuildCommandLineWithLrsFlag() throws CommandLineConfigurationException {
        WkhtmlToImageCommandLineBuilder builder = new WkhtmlToImageCommandLineBuilder();
        DefaultWkhtmlToImageInvocationRequest request = new DefaultWkhtmlToImageInvocationRequest();
        request.setLrs(true);
        request.setLrsFile(new File("/tmp/test.lrs"));
        request.setShellEnvironmentInherited(false);

        Commandline cli = builder.build(request);
        String cliStr = cli.toString();
        assertTrue(cliStr.contains("--lrs"));
    }

    @Test
    public void shouldBuildCommandLineWithOutputDirectory() throws CommandLineConfigurationException {
        WkhtmlToImageCommandLineBuilder builder = new WkhtmlToImageCommandLineBuilder();
        DefaultWkhtmlToImageInvocationRequest request = new DefaultWkhtmlToImageInvocationRequest();
        request.setLrsFile(new File("/tmp/test.lrs"));
        request.setOutputDirectory(new File("/tmp/output"));
        request.setShellEnvironmentInherited(false);

        Commandline cli = builder.build(request);
        String cliStr = cli.toString();
        assertTrue(cliStr.contains("-o"));
    }

    @Test
    public void shouldBuildCommandLineWithVerbose() throws CommandLineConfigurationException {
        WkhtmlToImageCommandLineBuilder builder = new WkhtmlToImageCommandLineBuilder();
        DefaultWkhtmlToImageInvocationRequest request = new DefaultWkhtmlToImageInvocationRequest();
        request.setLrsFile(new File("/tmp/test.lrs"));
        request.setVerbose(true);
        request.setShellEnvironmentInherited(false);

        Commandline cli = builder.build(request);
        String cliStr = cli.toString();
        assertTrue(cliStr.contains("--verbose"));
    }

    @Test
    public void shouldBuildCommandLineWithProperties() throws CommandLineConfigurationException {
        WkhtmlToImageCommandLineBuilder builder = new WkhtmlToImageCommandLineBuilder();
        DefaultWkhtmlToImageInvocationRequest request = new DefaultWkhtmlToImageInvocationRequest();
        request.setLrsFile(new File("/tmp/test.lrs"));
        request.setShellEnvironmentInherited(false);
        java.util.Properties props = new java.util.Properties();
        props.setProperty("myKey", "myValue");
        request.setProperties(props);

        Commandline cli = builder.build(request);
        String cliStr = cli.toString();
        assertTrue(cliStr.contains("-D"));
        assertTrue(cliStr.contains("myKey=myValue"));
    }
}
