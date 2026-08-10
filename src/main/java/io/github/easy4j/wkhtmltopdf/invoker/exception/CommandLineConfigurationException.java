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
package io.github.easy4j.wkhtmltopdf.invoker.exception;

/**
 * Signals an error encountered while assembling the command line that will be used to
 * invoke the {@code wkhtmltopdf} or {@code wkhtmltoimage} native binary.
 *
 * <p>Typical causes include a missing or unreadable wkhtmltopdf home directory,
 * an {@link java.io.IOException} raised when reading the host environment, or the
 * absence of the configured executable on disk. This exception describes a
 * <em>build-time</em> failure of the invoker itself &mdash; a non-zero exit code
 * returned by the forked wkhtmltopdf process should be reported through
 * {@link InvocationResult#getExitCode()} instead of through this exception.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see InvocationResult
 * @see io.github.easy4j.wkhtmltopdf.invoker.command.AbstractCommandLineBuilder
 */
public class CommandLineConfigurationException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new exception using the specified detail message and cause.
	 *
	 * @param message
	 *            The detail message for this exception, may be {@code null}.
	 * @param cause
	 *            The nested exception, may be {@code null}.
	 */
	public CommandLineConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Creates a new exception using the specified detail message.
	 *
	 * @param message
	 *            The detail message for this exception, may be {@code null}.
	 */
	public CommandLineConfigurationException(String message) {
		super(message);
	}

}
