/**
 * Copyright (c) 2018, Loong Wan (https://github.com/loong10k).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package io.github.easy4j.wkhtmltopdf.invoker.command;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

import io.github.easy4j.wkhtmltopdf.invoker.InvokerLogger;
import io.github.easy4j.wkhtmltopdf.invoker.SystemOutLogger;
import io.github.easy4j.wkhtmltopdf.invoker.exception.CommandLineConfigurationException;
import io.github.easy4j.wkhtmltopdf.invoker.request.InvocationRequest;

/**
 * Abstract base class for building a Plexus {@link Commandline} that will be
 * used to invoke a {@code wkhtmltopdf} or {@code wkhtmltoimage} native binary.
 *
 * <p>Subclasses must implement {@link #doCommandInternal(InvocationRequest, Commandline)}
 * to append tool-specific arguments, and {@link #findWkhtmltopdfExecutable()} to
 * locate the native executable on disk.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see WkhtmlToPdfCommandLineBuilder
 * @see WkhtmlToImageCommandLineBuilder
 */
public abstract class AbstractCommandLineBuilder {
	
	private static final InvokerLogger DEFAULT_LOGGER = new SystemOutLogger();

	protected InvokerLogger logger = DEFAULT_LOGGER;

	protected File workingDirectory;
	
	protected File localRepositoryDirectory;

	protected File wkhtmltopdfHome;

	protected File calibreHome;

	protected File calibreExecutable;

	protected File wkhtmltopdfExecutable;

	protected Properties systemEnvVars;

	/**
	 * Builds a fully-configured {@link Commandline} from the given invocation
	 * request. This method locates the executable, sets up the shell
	 * environment, delegates argument assembly to the subclass, and appends
	 * any system properties, goals and verbose flags.
	 *
	 * @param request the invocation request, must not be {@code null}.
	 * @return the assembled command line, never {@code null}.
	 * @throws CommandLineConfigurationException if the executable cannot be
	 *         found or the environment cannot be read.
	 */
	public Commandline build(InvocationRequest request) throws CommandLineConfigurationException {
		
		try {
			checkRequiredState();
		} catch (IOException e) {
			throw new CommandLineConfigurationException(e.getMessage(), e);
		}
		
		File executable = null;
		try {
			executable = findWkhtmltopdfExecutable();
		} catch (IOException e) {
			throw new CommandLineConfigurationException(e.getMessage(), e);
		}
		
		Commandline cli = new Commandline();
		
		cli.setExecutable(executable.getAbsolutePath());

		// handling for OS-level envars
		setShellEnvironment(request, cli);

		this.doCommandInternal(request, cli);
		
		setProperties(request, cli);

		setGoals(request, cli);
		
		setVerbose(request, cli);

		return cli;
	}
	
	/**
	 * Appends tool-specific arguments to the command line. Called by
	 * {@link #build(InvocationRequest)} after the executable and environment
	 * have been configured.
	 *
	 * @param request the invocation request, must not be {@code null}.
	 * @param cli     the command line being assembled, must not be {@code null}.
	 * @throws CommandLineConfigurationException if the request contains an
	 *         invalid argument combination.
	 */
	protected abstract void doCommandInternal(InvocationRequest request,Commandline cli) throws CommandLineConfigurationException;

	/**
	 * Locates the native wkhtmltopdf (or wkhtmltoimage) executable on disk.
	 *
	 * @return the executable file, never {@code null}.
	 * @throws CommandLineConfigurationException if the executable cannot be found.
	 * @throws IOException if an I/O error occurs while resolving the path.
	 */
	protected abstract File findWkhtmltopdfExecutable() throws CommandLineConfigurationException, IOException;

	/**
	 * Locates the Calibre home directory. Delegates to {@link #findWkhtmltopdfHome()}.
	 *
	 * @return the Calibre home directory, or {@code null} if not configured.
	 * @throws CommandLineConfigurationException if the directory cannot be resolved.
	 * @throws IOException if an I/O error occurs.
	 */
	protected File findCalibreHome() throws CommandLineConfigurationException, IOException {
		return findWkhtmltopdfHome();
	}

	/**
	 * Locates the Calibre executable. Returns {@code null} by default;
	 * subclasses may override.
	 *
	 * @return the Calibre executable, or {@code null}.
	 * @throws CommandLineConfigurationException if the executable cannot be found.
	 * @throws IOException if an I/O error occurs.
	 */
	protected File findCalibreExecutable() throws CommandLineConfigurationException, IOException {
		return null;
	}

	
	
	/**
	 * Validates that the builder is in a state suitable for building a command
	 * line. At minimum, a logger must be set.
	 *
	 * @throws IllegalStateException if a required field is missing.
	 * @throws IOException if an I/O error occurs during validation.
	 */
	protected void checkRequiredState() throws IOException {
		if (logger == null) {
			throw new IllegalStateException("A logger instance is required.");
		}
	}
 
	/**
	 * Propagates shell environment variables to the command line, including
	 * system environment variables (if inherited) and any custom variables
	 * registered on the request.
	 *
	 * @param request the invocation request.
	 * @param cli     the command line to configure.
	 * @throws CommandLineConfigurationException if the environment cannot be read.
	 */
	protected void setShellEnvironment(InvocationRequest request, Commandline cli)
			throws CommandLineConfigurationException {
		if (request.isShellEnvironmentInherited()) {
			try {
				cli.addSystemEnvironment();
			} catch (IOException e) {
				throw new CommandLineConfigurationException(
						"Error reading shell environment variables. Reason: " + e.getMessage(), e);
			} catch (Exception e) {
				if (e instanceof RuntimeException) {
					throw (RuntimeException) e;
				} else {
					IllegalStateException error = new IllegalStateException(
							"Unknown error retrieving shell environment variables. Reason: " + e.getMessage());
					error.initCause(e);

					throw error;
				}
			}
		}

		if (request.getWkhtmltopdfHome() != null) {
			cli.addEnvironment("CALIBRE_HOME", request.getWkhtmltopdfHome().getAbsolutePath());
		}

		for (Map.Entry<String, String> entry : request.getShellEnvironments().entrySet()) {
			cli.addEnvironment(entry.getKey(), entry.getValue());
		}
		
	}

	/**
	 * Appends the goals (positional arguments) from the request to the command line.
	 *
	 * @param request the invocation request.
	 * @param cli     the command line to append to.
	 */
	protected void setGoals(InvocationRequest request, Commandline cli) {
		List<String> goals = request.getGoals();
		if ((goals != null) && !goals.isEmpty()) {
			cli.createArg().setLine(StringUtils.join(goals.iterator(), " "));
		}
	}

	/**
	 * Appends {@code -Dkey=value} system properties from the request to the
	 * command line.
	 *
	 * @param request the invocation request.
	 * @param cli     the command line to append to.
	 */
	protected void setProperties(InvocationRequest request, Commandline cli) {
		Properties properties = request.getProperties();

		if (properties != null) {
			for (Iterator<Entry<Object, Object>> it = properties.entrySet().iterator(); it.hasNext();) {
				Entry<Object, Object> entry = it.next();

				String key = (String) entry.getKey();
				String value = (String) entry.getValue();

				cli.createArg().setValue("-D");
				cli.createArg().setValue(key + '=' + value);
			}
		}
	}

	/**
	 * Locates the wkhtmltopdf home directory by checking (in order): the
	 * field already set on this builder, the {@code wkhtmltopdf.home} system
	 * property, and the {@code WKHTMLTOPDF_HOME} environment variable.
	 *
	 * @return the wkhtmltopdf home directory, or {@code null} if not found.
	 * @throws CommandLineConfigurationException if the system property points
	 *         to a non-directory path.
	 * @throws IOException if the environment variables cannot be read.
	 */
	protected File findWkhtmltopdfHome() throws CommandLineConfigurationException, IOException {
		if (wkhtmltopdfHome == null) {
			String calibreHomeProperty = System.getProperty("wkhtmltopdf.home");
			if (calibreHomeProperty != null) {
				wkhtmltopdfHome = new File(calibreHomeProperty);
				if (!wkhtmltopdfHome.isDirectory()) {
					throw new IllegalStateException(
							"${wkhtmltopdf.home} is not specified as a directory: \'" + calibreHomeProperty + "\'.");
				}
			}
			
			if ((wkhtmltopdfHome == null) && (getSystemEnvVars().getProperty("WKHTMLTOPDF_HOME") != null)) {
				wkhtmltopdfHome = new File(getSystemEnvVars().getProperty("WKHTMLTOPDF_HOME"));
			}
		}
		return wkhtmltopdfHome;
	}
	 
	/**
	 * Appends the {@code --verbose} flag to the command line when the request
	 * has verbose mode enabled.
	 *
	 * @param request the invocation request.
	 * @param cli     the command line to append to.
	 */
	protected void setVerbose(InvocationRequest request, Commandline cli) {
		if(request.isVerbose()) {
			cli.createArg().setValue("--verbose");
		}
	}

	private Properties getSystemEnvVars() throws IOException {
		if (this.systemEnvVars == null) {
			// with 1.5 replace with System.getenv()
			this.systemEnvVars = CommandLineUtils.getSystemEnvVars();
		}
		return this.systemEnvVars;
	}

	/**
	 * Returns the logger used by this builder.
	 *
	 * @return the logger, never {@code null}.
	 */
	public InvokerLogger getLogger() {
		return logger;
	}

	/**
	 * Sets the logger used by this builder.
	 *
	 * @param logger the logger, must not be {@code null}.
	 */
	public void setLogger(InvokerLogger logger) {
		this.logger = logger;
	}

	/**
	 * Returns the wkhtmltopdf home directory configured on this builder.
	 *
	 * @return the home directory, or {@code null} if not set.
	 */
	public File getWkhtmltopdfHome() {
		return wkhtmltopdfHome;
	}

	/**
	 * Sets the wkhtmltopdf home directory.
	 *
	 * @param wkhtmltopdfHome the home directory, may be {@code null}.
	 */
	public void setWkhtmltopdfHome(File wkhtmltopdfHome) {
		this.wkhtmltopdfHome = wkhtmltopdfHome;
	}

	/**
	 * Returns the wkhtmltopdf executable file configured on this builder.
	 *
	 * @return the executable file, or {@code null} if not set.
	 */
	public File getWkhtmltopdfExecutable() {
		return wkhtmltopdfExecutable;
	}

	/**
	 * {@code wkhtmltopdfExecutable} can either be relative to ${wkhtmltopdf.home}/ or absolute
	 * @param wkhtmltopdfExecutable the executable
	 */
	public void setWkhtmltopdfExecutable(File wkhtmltopdfExecutable) {
		this.wkhtmltopdfExecutable = wkhtmltopdfExecutable;
	}

	/**
	 * Returns the working directory for the wkhtmltopdf process.
	 *
	 * @return the working directory, or {@code null} if not set.
	 */
	public File getWorkingDirectory() {
		return workingDirectory;
	}

	/**
	 * Sets the working directory for the wkhtmltopdf process.
	 *
	 * @param workingDirectory the working directory, may be {@code null}.
	 */
	public void setWorkingDirectory(File workingDirectory) {
		this.workingDirectory = workingDirectory;
	}
 

}
