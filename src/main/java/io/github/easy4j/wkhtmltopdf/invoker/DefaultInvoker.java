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
package io.github.easy4j.wkhtmltopdf.invoker;

import java.io.File;

import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

import io.github.easy4j.wkhtmltopdf.invoker.command.AbstractCommandLineBuilder;
import io.github.easy4j.wkhtmltopdf.invoker.command.WkhtmlToPdfCommandLineBuilder;
import io.github.easy4j.wkhtmltopdf.invoker.command.WkhtmlToImageCommandLineBuilder;
import io.github.easy4j.wkhtmltopdf.invoker.exception.CommandLineConfigurationException;
import io.github.easy4j.wkhtmltopdf.invoker.exception.WkhtmlToPdfInvocationException;
import io.github.easy4j.wkhtmltopdf.invoker.request.InvocationRequest;
import io.github.easy4j.wkhtmltopdf.invoker.request.WkhtmlToImageInvocationRequest;
import io.github.easy4j.wkhtmltopdf.invoker.request.WkhtmlToPdfInvocationRequest;

/**
 * Default implementation of {@link Invoker} that invokes a forked
 * {@code wkhtmltopdf} (or {@code wkhtmltoimage}) process from the host
 * application.
 *
 * <p>The invoker delegates command-line assembly to an
 * {@link AbstractCommandLineBuilder} selected by the type of the supplied
 * {@link InvocationRequest}, then uses Plexus {@link CommandLineUtils} to
 * execute the resulting {@link Commandline}.</p>
 *
 * <p>Typical usage:
 * <pre>{@code
 *   Invoker invoker = new DefaultInvoker();
 *   invoker.setWkhtmltopdfHome(new File("/usr/local/bin"));
 *   InvocationResult result = invoker.execute(request);
 *   if (result.getExitCode() != 0) { ... }
 * }</pre>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see Invoker
 * @see WkhtmlToPdfCommandLineBuilder
 * @see WkhtmlToImageCommandLineBuilder
 */
public class DefaultInvoker implements Invoker {

	/** Plexus role-hint for this invoker implementation. */
	public static final String ROLE_HINT = "default";

	private static final InvokerLogger DEFAULT_LOGGER = new SystemOutLogger();

	private static final InvocationOutputHandler DEFAULT_OUTPUT_HANDLER = new SystemOutHandler();

	private InvokerLogger logger = DEFAULT_LOGGER;

	private File workingDirectory;
	
	private File wkhtmltopdfHome;

	private InvocationOutputHandler outputHandler = DEFAULT_OUTPUT_HANDLER;

	private InvocationOutputHandler errorHandler = DEFAULT_OUTPUT_HANDLER;
	
	/**
	 * Selects the appropriate command-line builder for the given request type.
	 *
	 * @param request the invocation request, must not be {@code null}.
	 * @return the builder suitable for the request type, or {@code null} if the
	 *         request type is not recognized.
	 */
	protected AbstractCommandLineBuilder getCommandLineBuilder(InvocationRequest request) {
		if(request instanceof WkhtmlToPdfInvocationRequest) {
			return new WkhtmlToPdfCommandLineBuilder();
		}
		if(request instanceof WkhtmlToImageInvocationRequest) {
			return new WkhtmlToImageCommandLineBuilder();
		}
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>Builds a command line from the request, executes the forked wkhtmltopdf
	 * process, and returns the exit code (or the exception that prevented
	 * execution).</p>
	 *
	 * @param request the invocation request, must not be {@code null}.
	 * @return the result of the invocation, never {@code null}.
	 * @throws WkhtmlToPdfInvocationException if the command-line cannot be
	 *         configured.
	 */
	public InvocationResult execute(InvocationRequest request) throws WkhtmlToPdfInvocationException {
		
		AbstractCommandLineBuilder cliBuilder = getCommandLineBuilder(request);

		InvokerLogger logger = getLogger();
		if (logger != null) {
			cliBuilder.setLogger(getLogger());
		}
 
		File wkhtmltopdfHome = getWkhtmltopdfHome();
		if (wkhtmltopdfHome != null) {
			cliBuilder.setWkhtmltopdfHome(getWkhtmltopdfHome());
		}
		
		File workingDirectory = getWorkingDirectory();
		if (workingDirectory != null) {
			cliBuilder.setWorkingDirectory(getWorkingDirectory());
		}

		Commandline cli;
		try {
			cli = cliBuilder.build(request);
		} catch (CommandLineConfigurationException e) {
			throw new WkhtmlToPdfInvocationException("Error configuring command-line. Reason: " + e.getMessage(), e);
		}

		DefaultInvocationResult result = new DefaultInvocationResult();

		try {
			
			int exitCode = executeCommandLine(cli, request);

			result.setExitCode(exitCode);
		} catch (CommandLineException e) {
			result.setExecutionException(e);
		}

		return result;
	}

	/**
	 * Executes the assembled command line, routing stdout/stderr to the
	 * handlers configured on the request (or the defaults on this invoker).
	 *
	 * @param cli     the command line to execute, must not be {@code null}.
	 * @param request the original invocation request, used to resolve handlers.
	 * @return the process exit code.
	 * @throws CommandLineException if the process cannot be started.
	 */
	private int executeCommandLine(Commandline cli, InvocationRequest request) throws CommandLineException {
		int result = Integer.MIN_VALUE;

		InvocationOutputHandler outputHandler = request.getOutputHandler(this.outputHandler);
		InvocationOutputHandler errorHandler = request.getErrorHandler(this.errorHandler);

		if (getLogger().isDebugEnabled()) {
			getLogger().debug("Executing: " + cli);
		}
		result = CommandLineUtils.executeCommandLine(cli, outputHandler, errorHandler);
		return result;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return the logger used by this invoker, never {@code null}.
	 */
	public InvokerLogger getLogger() {
		return logger;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param logger the logger to use, may be {@code null} to revert to the
	 *               default {@link SystemOutLogger}.
	 * @return this invoker instance.
	 */
	public Invoker setLogger(InvokerLogger logger) {
		this.logger = (logger != null) ? logger : DEFAULT_LOGGER;
		return this;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return the working directory, or {@code null} if not set.
	 */
	public File getWorkingDirectory() {
		return workingDirectory;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param workingDirectory the working directory for the wkhtmltopdf process.
	 * @return this invoker instance.
	 */
	public Invoker setWorkingDirectory(File workingDirectory) {
		this.workingDirectory = workingDirectory;
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
	 *
	 * @param wkhtmltopdfHome the base directory of the wkhtmltopdf installation.
	 * @return this invoker instance.
	 */
	public Invoker setWkhtmltopdfHome(File wkhtmltopdfHome) {
		this.wkhtmltopdfHome = wkhtmltopdfHome;
		return this;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param errorHandler the error output handler, may be {@code null}.
	 * @return this invoker instance.
	 */
	public Invoker setErrorHandler(InvocationOutputHandler errorHandler) {
		this.errorHandler = errorHandler;
		return this;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param outputHandler the standard output handler, may be {@code null}.
	 * @return this invoker instance.
	 */
	public Invoker setOutputHandler(InvocationOutputHandler outputHandler) {
		this.outputHandler = outputHandler;
		return this;
	}
	
}
