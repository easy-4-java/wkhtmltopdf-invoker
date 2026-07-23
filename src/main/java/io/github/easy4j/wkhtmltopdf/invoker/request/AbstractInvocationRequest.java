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
package io.github.easy4j.wkhtmltopdf.invoker.request;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import io.github.easy4j.wkhtmltopdf.invoker.InvocationOutputHandler;

/**
 * Specifies the parameters used to control a wkhtmltopdf invocation.
 */
public abstract class AbstractInvocationRequest implements InvocationRequest {

	public static final String DEFAULT_EXECUTABLE = "wkhtmltopdf";

	/**
	 * Indicates whether Collate when printing multiple copies
	 */
	private boolean collate;

	private InvocationOutputHandler errorHandler;

	private List<String> goals;

	private InvocationOutputHandler outputHandler;

	private Properties properties;

	private boolean shellEnvironmentInherited = true;

	private File wkhtmltopdfHome;

	private Map<String, String> shellEnvironments;
	/**
	 * Show detailed output information. Useful for debugging
	 */
	private boolean verbose;

	public InvocationOutputHandler getErrorHandler(InvocationOutputHandler defaultHandler) {
		return errorHandler == null ? defaultHandler : errorHandler;
	}

	public List<String> getGoals() {
		return goals;
	}

	public InvocationOutputHandler getOutputHandler(InvocationOutputHandler defaultHandler) {
		return outputHandler == null ? defaultHandler : outputHandler;
	}

	public Properties getProperties() {
		return properties;
	}

	public boolean isDebug() {
		return debug;
	}

	public InvocationRequest setDebug(boolean debug) {
		this.debug = debug;
		return this;
	}

	public InvocationRequest setErrorHandler(InvocationOutputHandler errorHandler) {
		this.errorHandler = errorHandler;
		return this;
	}

	public InvocationRequest setGoals(List<String> goals) {
		this.goals = goals;
		return this;
	}

	public InvocationRequest setOutputHandler(InvocationOutputHandler outputHandler) {
		this.outputHandler = outputHandler;
		return this;
	}

	public InvocationRequest setProperties(Properties properties) {
		this.properties = properties;
		return this;
	}

	/**
	 * @see CalibreCommandLineBuilder#setShellEnvironment(InvocationRequest,
	 *      org.codehaus.plexus.util.cli.Commandline)
	 */
	public boolean isShellEnvironmentInherited() {
		return shellEnvironmentInherited;
	}

	public InvocationRequest setShellEnvironmentInherited(boolean shellEnvironmentInherited) {
		this.shellEnvironmentInherited = shellEnvironmentInherited;
		return this;
	}

	public File getWkhtmltopdfHome() {
		return wkhtmltopdfHome;
	}

	/**
	 * {@inheritDoc}
	 */
	public InvocationRequest setWkhtmltopdfHome(File wkhtmltopdfHome) {
		this.wkhtmltopdfHome = wkhtmltopdfHome;
		return this;
	}

	public boolean isVerbose() {
		return verbose;
	}

	public InvocationRequest setVerbose(boolean verbose) {
		this.verbose = verbose;
		return this;
	}

	public InvocationRequest addShellEnvironment(String name, String value) {
		if (this.shellEnvironments == null) {
			this.shellEnvironments = new HashMap<String, String>();
		}
		this.shellEnvironments.put(name, value);
		return this;
	}

	public Map<String, String> getShellEnvironments() {
		return shellEnvironments == null ? Collections.<String, String>emptyMap() : shellEnvironments;
	}

}
