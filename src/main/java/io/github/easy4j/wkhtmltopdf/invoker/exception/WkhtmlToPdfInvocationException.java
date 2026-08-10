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

import io.github.easy4j.wkhtmltopdf.invoker.InvocationResult;

/**
 * Signals a build-time error while setting up the command line used to invoke
 * the wkhtmltopdf (or wkhtmltoimage) native binary, for example an illegal
 * combination of invocation arguments.
 *
 * <p>This exception describes a failure of the <em>invoker</em> itself and
 * should not be confused with a failure of the <em>invoked</em> wkhtmltopdf
 * process, which is reported through the non-zero exit code returned by
 * {@link InvocationResult#getExitCode()}.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see InvocationResult#getExitCode()
 * @see CommandLineConfigurationException
 */
public class WkhtmlToPdfInvocationException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new exception using the specified detail message and cause.
	 *
	 * @param message
	 *            The detail message for this exception, may be {@code null}.
	 * @param cause
	 *            The nested exception, may be {@code null}.
	 */
	public WkhtmlToPdfInvocationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Creates a new exception using the specified detail message.
	 *
	 * @param message
	 *            The detail message for this exception, may be {@code null}.
	 */
	public WkhtmlToPdfInvocationException(String message) {
		super(message);
	}

}
