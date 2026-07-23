/**
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
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

public abstract class AbstractCommandLineBuilder {
	
	private static final InvokerLogger DEFAULT_LOGGER = new SystemOutLogger();

	protected InvokerLogger logger = DEFAULT_LOGGER;

	protected File workingDirectory;
	
	protected File localRepositoryDirectory;

	protected File wkhtmltopdfHome;

	protected File wkhtmltopdfExecutable;

	protected Properties systemEnvVars;

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
	
	protected abstract void doCommandInternal(InvocationRequest request,Commandline cli) throws CommandLineConfigurationException;
	
	protected abstract File findWkhtmltopdfExecutable() throws CommandLineConfigurationException, IOException;
	
	
	protected void checkRequiredState() throws IOException {
		if (logger == null) {
			throw new IllegalStateException("A logger instance is required.");
		}
	}
 
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

	protected void setGoals(InvocationRequest request, Commandline cli) {
		List<String> goals = request.getGoals();
		if ((goals != null) && !goals.isEmpty()) {
			cli.createArg().setLine(StringUtils.join(goals.iterator(), " "));
		}
	}

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

	public InvokerLogger getLogger() {
		return logger;
	}

	public void setLogger(InvokerLogger logger) {
		this.logger = logger;
	}

	public File getWkhtmltopdfHome() {
		return wkhtmltopdfHome;
	}

	public void setWkhtmltopdfHome(File wkhtmltopdfHome) {
		this.wkhtmltopdfHome = wkhtmltopdfHome;
	}

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

	public File getWorkingDirectory() {
		return workingDirectory;
	}

	public void setWorkingDirectory(File workingDirectory) {
		this.workingDirectory = workingDirectory;
	}
 

}
