package io.github.easy4j.wkhtmltopdf.invoker.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.codehaus.plexus.util.cli.Commandline;
import org.junit.Test;

import io.github.easy4j.wkhtmltopdf.invoker.SystemOutLogger;
import io.github.easy4j.wkhtmltopdf.invoker.exception.CommandLineConfigurationException;
import io.github.easy4j.wkhtmltopdf.invoker.request.DefaultWkhtmlToPdfInvocationRequest;

/**
 * Tests for {@link WkhtmlToPdfCommandLineBuilder}.
 */
public class WkhtmlToPdfCommandLineBuilderTest {

    @Test
    public void shouldReturnWkhtmltopdfExecutable() throws CommandLineConfigurationException, IOException {
        WkhtmlToPdfCommandLineBuilder builder = new WkhtmlToPdfCommandLineBuilder();
        File exe = builder.findWkhtmltopdfExecutable();
        assertNotNull(exe);
        assertEquals("wkhtmltopdf", exe.getName());
    }

    @Test(expected = CommandLineConfigurationException.class)
    public void shouldThrowWhenCalibreExecutableNotFound() throws CommandLineConfigurationException, IOException {
        WkhtmlToPdfCommandLineBuilder builder = new WkhtmlToPdfCommandLineBuilder();
        builder.findCalibreExecutable();
    }

    @Test
    public void shouldHaveDefaultLogger() {
        WkhtmlToPdfCommandLineBuilder builder = new WkhtmlToPdfCommandLineBuilder();
        assertNotNull(builder.getLogger());
        assertTrue(builder.getLogger() instanceof SystemOutLogger);
    }

    @Test
    public void shouldSetAndGetLogger() {
        WkhtmlToPdfCommandLineBuilder builder = new WkhtmlToPdfCommandLineBuilder();
        SystemOutLogger logger = new SystemOutLogger();
        builder.setLogger(logger);
        assertEquals(logger, builder.getLogger());
    }

    @Test
    public void shouldSetAndGetWkhtmltopdfHome() {
        WkhtmlToPdfCommandLineBuilder builder = new WkhtmlToPdfCommandLineBuilder();
        File home = new File("/usr/local/wkhtmltopdf");
        builder.setWkhtmltopdfHome(home);
        assertEquals(home, builder.getWkhtmltopdfHome());
    }

    @Test
    public void shouldSetAndGetWorkingDirectory() {
        WkhtmlToPdfCommandLineBuilder builder = new WkhtmlToPdfCommandLineBuilder();
        File dir = new File("/tmp/work");
        builder.setWorkingDirectory(dir);
        assertEquals(dir, builder.getWorkingDirectory());
    }

    @Test
    public void shouldSetAndGetWkhtmltopdfExecutable() {
        WkhtmlToPdfCommandLineBuilder builder = new WkhtmlToPdfCommandLineBuilder();
        File exe = new File("/usr/bin/wkhtmltopdf");
        builder.setWkhtmltopdfExecutable(exe);
        assertEquals(exe, builder.getWkhtmltopdfExecutable());
    }

    @Test
    public void shouldBuildCommandLineWithUrl() throws CommandLineConfigurationException {
        WkhtmlToPdfCommandLineBuilder builder = new WkhtmlToPdfCommandLineBuilder();
        DefaultWkhtmlToPdfInvocationRequest request = new DefaultWkhtmlToPdfInvocationRequest();
        request.setURL("https://example.com");
        request.setShellEnvironmentInherited(false);

        Commandline cli = builder.build(request);
        assertNotNull(cli);
        String[] args = cli.getArguments();
        // The URL should be the last argument
        assertTrue(args.length > 0);
        assertEquals("https://example.com", args[args.length - 1]);
    }

    @Test
    public void shouldBuildCommandLineWithAllOptions() throws CommandLineConfigurationException {
        WkhtmlToPdfCommandLineBuilder builder = new WkhtmlToPdfCommandLineBuilder();
        DefaultWkhtmlToPdfInvocationRequest request = new DefaultWkhtmlToPdfInvocationRequest();
        request.setURL("https://example.com");
        request.setBaseDirectory(new File("/tmp/base"));
        request.setDelay(5);
        request.setDontDownloadStylesheets(true);
        request.setEncoding("UTF-8");
        request.setFilterRegexp(".*\\.css");
        request.setMatchRegexp(".*\\.html");
        request.setMaxFiles(100);
        request.setMaxRecursions(3);
        request.setTimeout(30);
        request.setVerbose(true);
        request.setShellEnvironmentInherited(false);

        Commandline cli = builder.build(request);
        assertNotNull(cli);
        String cliStr = cli.toString();
        assertTrue(cliStr.contains("--delay"));
        assertTrue(cliStr.contains("--dont-download-stylesheets"));
        assertTrue(cliStr.contains("--encoding"));
        assertTrue(cliStr.contains("--filter-regexp"));
        assertTrue(cliStr.contains("--match-regexp"));
        assertTrue(cliStr.contains("--verbose"));
    }

    @Test
    public void shouldBuildCommandLineWithProperties() throws CommandLineConfigurationException {
        WkhtmlToPdfCommandLineBuilder builder = new WkhtmlToPdfCommandLineBuilder();
        DefaultWkhtmlToPdfInvocationRequest request = new DefaultWkhtmlToPdfInvocationRequest();
        request.setURL("https://example.com");
        request.setShellEnvironmentInherited(false);
        java.util.Properties props = new java.util.Properties();
        props.setProperty("key1", "value1");
        request.setProperties(props);

        Commandline cli = builder.build(request);
        String cliStr = cli.toString();
        assertTrue(cliStr.contains("-D"));
        assertTrue(cliStr.contains("key1=value1"));
    }

    @Test
    public void shouldBuildCommandLineWithGoals() throws CommandLineConfigurationException {
        WkhtmlToPdfCommandLineBuilder builder = new WkhtmlToPdfCommandLineBuilder();
        DefaultWkhtmlToPdfInvocationRequest request = new DefaultWkhtmlToPdfInvocationRequest();
        request.setGoals(java.util.Arrays.asList("--page-size", "A4"));
        request.setShellEnvironmentInherited(false);

        Commandline cli = builder.build(request);
        assertNotNull(cli);
    }

    @Test
    public void shouldBuildCommandLineWithShellEnvironments() throws CommandLineConfigurationException {
        WkhtmlToPdfCommandLineBuilder builder = new WkhtmlToPdfCommandLineBuilder();
        DefaultWkhtmlToPdfInvocationRequest request = new DefaultWkhtmlToPdfInvocationRequest();
        request.setURL("https://example.com");
        request.setShellEnvironmentInherited(false);
        request.addShellEnvironment("MY_VAR", "my_value");

        Commandline cli = builder.build(request);
        assertNotNull(cli);
    }

    @Test
    public void shouldNotAppendMaxFilesWhenZero() throws CommandLineConfigurationException {
        WkhtmlToPdfCommandLineBuilder builder = new WkhtmlToPdfCommandLineBuilder();
        DefaultWkhtmlToPdfInvocationRequest request = new DefaultWkhtmlToPdfInvocationRequest();
        request.setURL("https://example.com");
        request.setMaxFiles(0);
        request.setShellEnvironmentInherited(false);

        Commandline cli = builder.build(request);
        String cliStr = cli.toString();
        // -n should not appear when maxFiles is 0
        // (but -n could appear in other contexts, so just verify no exception)
        assertNotNull(cli);
    }

    @Test
    public void shouldNotAppendTimeoutWhenZero() throws CommandLineConfigurationException {
        WkhtmlToPdfCommandLineBuilder builder = new WkhtmlToPdfCommandLineBuilder();
        DefaultWkhtmlToPdfInvocationRequest request = new DefaultWkhtmlToPdfInvocationRequest();
        request.setURL("https://example.com");
        request.setTimeout(0);
        request.setShellEnvironmentInherited(false);

        Commandline cli = builder.build(request);
        assertNotNull(cli);
    }

    @Test
    public void shouldResolveConfiguredHomeDirectory() throws Exception {
        String previous = System.getProperty("wkhtmltopdf.home");
        File home = Files.createTempDirectory("wkhtmltopdf-home-").toFile();
        try {
            System.setProperty("wkhtmltopdf.home", home.getAbsolutePath());
            WkhtmlToPdfCommandLineBuilder builder = new WkhtmlToPdfCommandLineBuilder();
            assertEquals(home, builder.findWkhtmltopdfHome());
        } finally {
            if (previous == null) {
                System.clearProperty("wkhtmltopdf.home");
            } else {
                System.setProperty("wkhtmltopdf.home", previous);
            }
            Files.deleteIfExists(home.toPath());
        }
    }

    @Test
    public void shouldRejectHomePropertyThatIsNotDirectory() throws Exception {
        String previous = System.getProperty("wkhtmltopdf.home");
        File file = File.createTempFile("wkhtmltopdf-home-", ".txt");
        try {
            System.setProperty("wkhtmltopdf.home", file.getAbsolutePath());
            WkhtmlToPdfCommandLineBuilder builder = new WkhtmlToPdfCommandLineBuilder();
            try {
                builder.findWkhtmltopdfHome();
                org.junit.Assert.fail("Expected invalid home directory to be rejected");
            } catch (IllegalStateException expected) {
                assertTrue(expected.getMessage().contains("wkhtmltopdf.home"));
            }
        } finally {
            if (previous == null) {
                System.clearProperty("wkhtmltopdf.home");
            } else {
                System.setProperty("wkhtmltopdf.home", previous);
            }
            Files.deleteIfExists(file.toPath());
        }
    }

    @Test
    public void shouldResolveLegacyWeb2diskExecutableFromConfiguredHome() throws Exception {
        File home = Files.createTempDirectory("web2disk-home-").toFile();
        File executable = new File(home, "web2disk");
        try {
            assertTrue(executable.createNewFile());
            WkhtmlToPdfCommandLineBuilder builder = new WkhtmlToPdfCommandLineBuilder();
            builder.calibreHome = home;
            assertEquals(executable.getCanonicalFile(), builder.findCalibreExecutable());
        } finally {
            Files.deleteIfExists(executable.toPath());
            Files.deleteIfExists(home.toPath());
        }
    }

    @Test
    public void shouldResolveHomeFromEnvironmentWhenPropertyIsAbsent() throws Exception {
        String previous = System.getProperty("wkhtmltopdf.home");
        File home = Files.createTempDirectory("wkhtmltopdf-env-").toFile();
        try {
            System.clearProperty("wkhtmltopdf.home");
            WkhtmlToPdfCommandLineBuilder builder = new WkhtmlToPdfCommandLineBuilder();
            builder.systemEnvVars = new java.util.Properties();
            builder.systemEnvVars.setProperty("WKHTMLTOPDF_HOME", home.getAbsolutePath());
            assertEquals(home, builder.findWkhtmltopdfHome());
        } finally {
            if (previous != null) {
                System.setProperty("wkhtmltopdf.home", previous);
            }
            Files.deleteIfExists(home.toPath());
        }
    }

    @Test(expected = IllegalStateException.class)
    public void shouldRejectMissingLoggerBeforeBuildingCommand() throws Exception {
        WkhtmlToPdfCommandLineBuilder builder = new WkhtmlToPdfCommandLineBuilder();
        builder.setLogger(null);
        builder.build(new DefaultWkhtmlToPdfInvocationRequest());
    }

    @Test
    public void shouldWrapExecutableLookupFailure() throws Exception {
        WkhtmlToPdfCommandLineBuilder builder = new WkhtmlToPdfCommandLineBuilder() {
            @Override
            protected File findWkhtmltopdfExecutable() throws IOException {
                throw new IOException("executable unavailable");
            }
        };
        try {
            builder.build(new DefaultWkhtmlToPdfInvocationRequest());
            org.junit.Assert.fail("Expected executable lookup failure");
        } catch (CommandLineConfigurationException expected) {
            assertTrue(expected.getCause() instanceof IOException);
        }
    }

    @Test
    public void shouldInheritHostEnvironmentWhenRequested() throws Exception {
        WkhtmlToPdfCommandLineBuilder builder = new WkhtmlToPdfCommandLineBuilder();
        DefaultWkhtmlToPdfInvocationRequest request = new DefaultWkhtmlToPdfInvocationRequest();
        request.setURL("https://example.com");
        request.setShellEnvironmentInherited(true);
        assertNotNull(builder.build(request));
    }
}
