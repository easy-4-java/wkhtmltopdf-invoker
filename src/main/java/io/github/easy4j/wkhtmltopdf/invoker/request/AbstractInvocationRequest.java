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
package io.github.easy4j.wkhtmltopdf.invoker.request;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import io.github.easy4j.wkhtmltopdf.invoker.InvocationOutputHandler;

/**
 * Abstract base implementation of {@link InvocationRequest} that holds the
 * common fields shared by all wkhtmltopdf-family invocation requests.
 *
 * <p>Subclasses add tool-specific options (e.g. URL for wkhtmltopdf,
 * LRS file for wkhtmltoimage). All setters return {@code this} to allow
 * fluent method chaining.</p>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 * @see DefaultWkhtmlToPdfInvocationRequest
 * @see DefaultWkhtmlToImageInvocationRequest
 */
public abstract class AbstractInvocationRequest implements InvocationRequest {

	/** Default executable name for the wkhtmltopdf binary. */
	public static final String DEFAULT_EXECUTABLE = "wkhtmltopdf";

	/**
	 * Indicates whether Collate when printing multiple copies
	 */
	private boolean collate;

	private boolean debug;

	private File outputFile;

	private int imageDpi;

	private boolean grayscale;

	private int dpi;

	private int copies;

	private File cookieJar;

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

	/**
	 * {@inheritDoc}
	 *
	 * @param defaultHandler the fallback handler if none is set on this request.
	 * @return the error handler, or {@code defaultHandler} if not set.
	 */
	public InvocationOutputHandler getErrorHandler(InvocationOutputHandler defaultHandler) {
		return errorHandler == null ? defaultHandler : errorHandler;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return the goals list, or {@code null} if not set.
	 */
	public List<String> getGoals() {
		return goals;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param defaultHandler the fallback handler if none is set on this request.
	 * @return the output handler, or {@code defaultHandler} if not set.
	 */
	public InvocationOutputHandler getOutputHandler(InvocationOutputHandler defaultHandler) {
		return outputHandler == null ? defaultHandler : outputHandler;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return the system properties, or {@code null} if not set.
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return {@code true} if debug mode is enabled.
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param debug {@code true} to enable debug mode.
	 * @return this invocation request.
	 */
	public InvocationRequest setDebug(boolean debug) {
		this.debug = debug;
		return this;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param outputFile the output file.
	 * @return this invocation request.
	 */
	public InvocationRequest setOutputFile(File outputFile) {
		this.outputFile = outputFile;
		return this;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param dpi the image DPI.
	 * @return this invocation request.
	 */
	public InvocationRequest setImageDpi(int dpi) {
		this.imageDpi = dpi;
		return this;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param dpi the DPI value.
	 * @return this invocation request.
	 */
	public InvocationRequest setDpi(int dpi) {
		this.dpi = dpi;
		return this;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param grayscale {@code true} to enable grayscale mode.
	 * @return this invocation request.
	 */
	public InvocationRequest setGrayscale(boolean grayscale) {
		this.grayscale = grayscale;
		return this;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return {@code true} if verbose mode is enabled.
	 */
	public boolean isVerbose() {
		return verbose;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param errorHandler the error handler, may be {@code null}.
	 * @return this invocation request.
	 */
	public InvocationRequest setErrorHandler(InvocationOutputHandler errorHandler) {
		this.errorHandler = errorHandler;
		return this;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param goals the goals list, may be {@code null}.
	 * @return this invocation request.
	 */
	public InvocationRequest setGoals(List<String> goals) {
		this.goals = goals;
		return this;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param outputHandler the output handler, may be {@code null}.
	 * @return this invocation request.
	 */
	public InvocationRequest setOutputHandler(InvocationOutputHandler outputHandler) {
		this.outputHandler = outputHandler;
		return this;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param properties the system properties, may be {@code null}.
	 * @return this invocation request.
	 */
	public InvocationRequest setProperties(Properties properties) {
		this.properties = properties;
		return this;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return {@code true} if shell environment variables are inherited.
	 */
	public boolean isShellEnvironmentInherited() {
		return shellEnvironmentInherited;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param shellEnvironmentInherited {@code true} to inherit env vars.
	 * @return this invocation request.
	 */
	public InvocationRequest setShellEnvironmentInherited(boolean shellEnvironmentInherited) {
		this.shellEnvironmentInherited = shellEnvironmentInherited;
		return this;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return the wkhtmltopdf home directory, or {@code null} if not set.
	 */
	public File getWkhtmltopdfHome() {
		return wkhtmltopdfHome;
	}

	/**
	 * {@inheritDoc}
	 */
	/**
	 * {@inheritDoc}
	 *
	 * @param wkhtmltopdfHome the wkhtmltopdf home directory.
	 * @return this invocation request.
	 */
	public InvocationRequest setWkhtmltopdfHome(File wkhtmltopdfHome) {
		this.wkhtmltopdfHome = wkhtmltopdfHome;
		return this;
	}

	

	/**
	 * {@inheritDoc}
	 *
	 * @param verbose {@code true} to enable verbose mode.
	 * @return this invocation request.
	 */
	public InvocationRequest setVerbose(boolean verbose) {
		this.verbose = verbose;
		return this;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param name  the environment variable name, must not be {@code null}.
	 * @param value the environment variable value, must not be {@code null}.
	 * @return this invocation request.
	 */
	public InvocationRequest addShellEnvironment(String name, String value) {
		if (this.shellEnvironments == null) {
			this.shellEnvironments = new HashMap<String, String>();
		}
		this.shellEnvironments.put(name, value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return the environment variables map, never {@code null} (empty if none set).
	 */
	public Map<String, String> getShellEnvironments() {
		return shellEnvironments == null ? Collections.<String, String>emptyMap() : shellEnvironments;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return {@code true} if collation is enabled.
	 */
	public boolean isCollate() {
		return this.collate;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return the cookie jar file, or {@code null} if not set.
	 */
	public File getCookieJar() {
		return this.cookieJar;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return the number of copies.
	 */
	public int getCopies() {
		return this.copies;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return the DPI value.
	 */
	public int getDpi() {
		return this.dpi;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return {@code true} if grayscale mode is enabled.
	 */
	public boolean isGrayscale() {
		return this.grayscale;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return the image DPI.
	 */
	public int getImageDpi() {
		return this.imageDpi;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param collate {@code true} to enable collation.
	 * @return this invocation request.
	 */
	public InvocationRequest setCollate(boolean collate) {
		this.collate = collate;
		return this;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param cookieJar the cookie jar file, may be {@code null}.
	 * @return this invocation request.
	 */
	public InvocationRequest setCookieJar(File cookieJar) {
		this.cookieJar = cookieJar;
		return this;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param copies the number of copies.
	 * @return this invocation request.
	 */
	public InvocationRequest setCopies(int copies) {
		this.copies = copies;
		return this;
	}

}
