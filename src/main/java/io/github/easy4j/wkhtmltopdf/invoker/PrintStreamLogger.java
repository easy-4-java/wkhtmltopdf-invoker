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

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Threshold-aware {@link InvokerLogger} that writes diagnostic messages to a
 * caller-supplied {@link PrintStream}, defaulting to {@link System#out}.
 *
 * <p>Each log call is prefixed with a severity tag such as {@code [INFO]} or
 * {@code [ERROR]}, and any accompanying {@link Throwable} is appended as a
 * formatted stack trace. Calls made for a level above the configured threshold
 * are silently discarded, allowing callers to mute noisy output without the
 * logger having to be re-instantiated.</p>
 *
 * <p>The class is intentionally simple so it can serve as a fallback when no
 * external logging framework (such as SLF4J) is available on the classpath.</p>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 2.0.9
 * @see InvokerLogger
 * @see SystemOutLogger
 */
public class PrintStreamLogger implements InvokerLogger {

	/**
	 * The print stream to write to, never {@code null}.
	 */
	private PrintStream out;

	/**
	 * The threshold used to filter messages.
	 */
	private int threshold;

	/**
	 * Creates a new logger that writes to {@link System#out} and has a threshold
	 * of {@link #INFO}.
	 */
	public PrintStreamLogger() {
		this(System.out, INFO);
	}

	/**
	 * Creates a new logger that writes to the specified print stream.
	 *
	 * @param out
	 *            the print stream to write to, must not be {@code null}.
	 * @param threshold
	 *            the threshold for the logger, must be one of
	 *            {@link #DEBUG}, {@link #INFO}, {@link #WARN},
	 *            {@link #ERROR} or {@link #FATAL}.
	 * @throws NullPointerException if {@code out} is {@code null}.
	 */
	public PrintStreamLogger(PrintStream out, int threshold) {
		if (out == null) {
			throw new NullPointerException("missing output stream");
		}
		this.out = out;
		setThreshold(threshold);
	}

	/**
	 * Writes the specified message and exception to the underlying print stream
	 * if {@code level} is not above the configured threshold.
	 *
	 * @param level    the priority level of the message.
	 * @param message  the message to log, may be {@code null}.
	 * @param error    the exception to log, may be {@code null}.
	 */
	private void log(int level, String message, Throwable error) {
		if (level > threshold) {
			// don't log when it doesn't match your threshold.
			return;
		}

		if (message == null && error == null) {
			// don't log when there's nothing to log.
			return;
		}

		StringBuffer buffer = new StringBuffer();

		switch (level) {
		case (DEBUG):
			buffer.append("[DEBUG]");
			break;

		case (INFO):
			buffer.append("[INFO]");
			break;

		case (WARN):
			buffer.append("[WARN]");
			break;

		case (ERROR):
			buffer.append("[ERROR]");
			break;

		case (FATAL):
			buffer.append("[FATAL]");
			break;

		default:
		}

		buffer.append(' ');

		if (message != null) {
			buffer.append(message);
		}

		if (error != null) {
			StringWriter writer = new StringWriter();
			PrintWriter pWriter = new PrintWriter(writer);

			error.printStackTrace(pWriter);

			if (message != null) {
				buffer.append('\n');
			}

			buffer.append("Error:\n");
			buffer.append(writer.toString());
		}

		out.println(buffer.toString());
	}

	/**
	 * Logs the specified message at {@link #DEBUG} level.
	 *
	 * @param message the message to log, may be {@code null}.
	 */
	@Override
	public void debug(String message) {
		log(DEBUG, message, null);
	}

	/**
	 * Logs the specified message and accompanying exception at {@link #DEBUG}
	 * level.
	 *
	 * @param message   the message to log, may be {@code null}.
	 * @param throwable the exception to log, may be {@code null}.
	 */
	@Override
	public void debug(String message, Throwable throwable) {
		log(DEBUG, message, throwable);
	}

	/**
	 * Logs the specified message at {@link #INFO} level.
	 *
	 * @param message the message to log, may be {@code null}.
	 */
	@Override
	public void info(String message) {
		log(INFO, message, null);
	}

	/**
	 * Logs the specified message and accompanying exception at {@link #INFO}
	 * level.
	 *
	 * @param message   the message to log, may be {@code null}.
	 * @param throwable the exception to log, may be {@code null}.
	 */
	@Override
	public void info(String message, Throwable throwable) {
		log(INFO, message, throwable);
	}

	/**
	 * Logs the specified message at {@link #WARN} level.
	 *
	 * @param message the message to log, may be {@code null}.
	 */
	@Override
	public void warn(String message) {
		log(WARN, message, null);
	}

	/**
	 * Logs the specified message and accompanying exception at {@link #WARN}
	 * level.
	 *
	 * @param message   the message to log, may be {@code null}.
	 * @param throwable the exception to log, may be {@code null}.
	 */
	@Override
	public void warn(String message, Throwable throwable) {
		log(WARN, message, throwable);
	}

	/**
	 * Logs the specified message at {@link #ERROR} level.
	 *
	 * @param message the message to log, may be {@code null}.
	 */
	@Override
	public void error(String message) {
		log(ERROR, message, null);
	}

	/**
	 * Logs the specified message and accompanying exception at {@link #ERROR}
	 * level.
	 *
	 * @param message   the message to log, may be {@code null}.
	 * @param throwable the exception to log, may be {@code null}.
	 */
	@Override
	public void error(String message, Throwable throwable) {
		log(ERROR, message, throwable);
	}

	/**
	 * Logs the specified message at {@link #FATAL} level.
	 *
	 * @param message the message to log, may be {@code null}.
	 */
	@Override
	public void fatalError(String message) {
		log(FATAL, message, null);
	}

	/**
	 * Logs the specified message and accompanying exception at {@link #FATAL}
	 * level.
	 *
	 * @param message   the message to log, may be {@code null}.
	 * @param throwable the exception to log, may be {@code null}.
	 */
	@Override
	public void fatalError(String message, Throwable throwable) {
		log(FATAL, message, throwable);
	}

	/**
	 * @return {@code true} when messages with priority {@link #DEBUG} or higher
	 *         are emitted.
	 */
	@Override
	public boolean isDebugEnabled() {
		return threshold >= DEBUG;
	}

	/**
	 * @return {@code true} when messages with priority {@link #ERROR} or higher
	 *         are emitted.
	 */
	@Override
	public boolean isErrorEnabled() {
		return threshold >= ERROR;
	}

	/**
	 * @return {@code true} when messages with priority {@link #FATAL} or higher
	 *         are emitted.
	 */
	@Override
	public boolean isFatalErrorEnabled() {
		return threshold >= FATAL;
	}

	/**
	 * @return {@code true} when messages with priority {@link #INFO} or higher
	 *         are emitted.
	 */
	@Override
	public boolean isInfoEnabled() {
		return threshold >= INFO;
	}

	/**
	 * @return {@code true} when messages with priority {@link #WARN} or higher
	 *         are emitted.
	 */
	@Override
	public boolean isWarnEnabled() {
		return threshold >= WARN;
	}

	/**
	 * @return the current threshold, one of {@link #DEBUG}, {@link #INFO},
	 *         {@link #WARN}, {@link #ERROR} or {@link #FATAL}.
	 */
	@Override
	public int getThreshold() {
		return threshold;
	}

	/**
	 * Updates the threshold used to filter log messages.
	 *
	 * @param threshold the new threshold, must be one of {@link #DEBUG},
	 *                  {@link #INFO}, {@link #WARN}, {@link #ERROR} or
	 *                  {@link #FATAL}.
	 */
	@Override
	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

}
