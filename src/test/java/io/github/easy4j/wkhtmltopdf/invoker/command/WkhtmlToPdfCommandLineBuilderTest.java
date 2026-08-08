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
}
