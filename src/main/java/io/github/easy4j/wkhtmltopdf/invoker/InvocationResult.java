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


import org.codehaus.plexus.util.cli.CommandLineException;

/**
 * Describes the result of a {@code wkhtmltopdf} (or {@code wkhtmltoimage}) invocation.
 *
 * <p>An {@code InvocationResult} exposes the two pieces of information any caller
 * needs to evaluate a completed invocation: the process exit code returned by the
 * native binary, and the {@link CommandLineException} (if any) that prevented the
 * invoker from running the binary in the first place.</p>
 *
 * <p>Because the invoker never wraps a successful forked invocation in an
 * exception, callers should treat {@link #getExecutionException()} as the
 * authoritative "did the invoker manage to start the process?" signal.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see io.github.easy4j.wkhtmltopdf.invoker.Invoker#execute(InvocationRequest)
 */
public interface InvocationResult
{

    /**
     * Returns the exception that prevented the invoker from executing the command
     * line, or {@code null} if the command line was successfully handed to the
     * operating system.
     *
     * @return the {@link CommandLineException} raised while preparing or starting
     *         the native process, or {@code null} when the process actually ran.
     */
    CommandLineException getExecutionException();

    /**
     * Returns the exit code reported by the {@code wkhtmltopdf} invocation. A
     * non-zero value indicates a build failure.
     *
     * <p><strong>Note:</strong> this value is undefined when
     * {@link #getExecutionException()} returns a non-{@code null} exception, since
     * in that case no native process ever completed.</p>
     *
     * @return the exit code from the wkhtmltopdf invocation; defaults to
     *         {@link Integer#MIN_VALUE} when the executable was never started.
     */
    int getExitCode();

}
