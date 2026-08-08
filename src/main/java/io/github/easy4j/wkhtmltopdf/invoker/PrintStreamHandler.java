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

/**
 * Default {@link InvocationOutputHandler} that re-emits every captured line on
 * a caller-supplied {@link PrintStream} (typically {@link System#out}).
 *
 * <p>The handler flushes the wrapped stream on demand when {@code alwaysFlush} is
 * {@code true}, which is convenient for long-running invocations where the
 * caller wants to see partial output before the process has terminated. When
 * set to {@code false} (the default for the no-argument constructor) the JVM
 * is free to buffer output and only flush on process termination.</p>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 * @see SystemOutHandler
 * @see InvocationOutputHandler
 */
public class PrintStreamHandler implements InvocationOutputHandler {

	/**
	 * The print stream to write to, never {@code null}.
	 */
	private PrintStream out;

	/**
	 * A flag whether the print stream should be flushed after each line.
	 */
	private boolean alwaysFlush;

	/**
	 * Creates a new output handler that writes to {@link System#out} without
	 * forcing a flush after every consumed line.
	 */
	public PrintStreamHandler() {
		this(System.out, false);
	}

	/**
	 * Creates a new output handler that writes to the specified print stream.
	 *
	 * @param out          the print stream to write to, must not be {@code null}.
	 * @param alwaysFlush  whether the print stream should be flushed after each
	 *                     consumed line.
	 * @throws NullPointerException if {@code out} is {@code null}.
	 */
	public PrintStreamHandler(PrintStream out, boolean alwaysFlush) {
		if (out == null) {
			throw new NullPointerException("missing output stream");
		}
		this.out = out;
		this.alwaysFlush = alwaysFlush;
	}

	/**
	 * Writes {@code line} to the wrapped print stream, terminated with a line
	 * separator. A {@code null} {@code line} produces a single empty line. If
	 * this handler was constructed with {@code alwaysFlush == true} the stream
	 * is flushed immediately.
	 *
	 * @param line the line of text to consume, may be {@code null}.
	 */
	@Override
	public void consumeLine(String line) {
		if (line == null) {
			out.println();
		} else {
			out.println(line);
		}

		if (alwaysFlush) {
			out.flush();
		}
	}

}
