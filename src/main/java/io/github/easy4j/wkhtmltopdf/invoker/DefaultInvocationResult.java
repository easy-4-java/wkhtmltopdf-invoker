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
 * Default implementation of {@link InvocationResult} that records the exit code
 * and any {@link CommandLineException} raised during a {@code wkhtmltopdf} (or
 * {@code wkhtmltoimage}) invocation.
 *
 * <p>Instances are created internally by {@link DefaultInvoker} and returned to
 * the caller after the forked process has completed (or failed to start).</p>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 * @see InvocationResult
 * @see DefaultInvoker#execute(io.github.easy4j.wkhtmltopdf.invoker.request.InvocationRequest)
 */
public final class DefaultInvocationResult
    implements InvocationResult
{

    /**
     * The exception that prevented to execute the command line, will be <code>null</code> if Calibre could be
     * successfully started.
     */
    private CommandLineException executionException;

    /**
     * The exit code reported by the Calibre invocation.
     */
    private int exitCode = Integer.MIN_VALUE;

    /**
     * Creates a new invocation result with default values. Package-private by
     * design; instances are produced exclusively by {@link DefaultInvoker}.
     */
    DefaultInvocationResult()
    {
        // hide constructor
    }

    /**
     * {@inheritDoc}
     *
     * @return the exit code from the wkhtmltopdf invocation; defaults to
     *         {@link Integer#MIN_VALUE} when the executable was never started.
     */
    public int getExitCode()
    {
        return exitCode;
    }

    /**
     * {@inheritDoc}
     *
     * @return the {@link CommandLineException} raised while preparing or starting
     *         the native process, or {@code null} when the process actually ran.
     */
    public CommandLineException getExecutionException()
    {
        return executionException;
    }

    /**
     * Sets the exit code reported by the Calibre invocation.
     * 
     * @param exitCode The exit code reported by the Calibre invocation.
     */
    void setExitCode( int exitCode )
    {
        this.exitCode = exitCode;
    }

    /**
     * Sets the exception that prevented to execute the command line.
     * 
     * @param executionException The exception that prevented to execute the command line, may be <code>null</code>.
     */
    void setExecutionException( CommandLineException executionException )
    {
        this.executionException = executionException;
    }

}
