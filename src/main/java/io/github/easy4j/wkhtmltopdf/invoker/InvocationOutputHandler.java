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

import org.codehaus.plexus.util.cli.StreamConsumer;

/**
 * Receives lines of standard output or standard error produced by a
 * {@code wkhtmltopdf} (or {@code wkhtmltoimage}) invocation.
 *
 * <p>This interface extends Plexus {@link StreamConsumer} so that it can be
 * plugged directly into {@link org.codehaus.plexus.util.cli.CommandLineUtils}.
 * Implementations should be thread-safe because the forked process may write
 * to stdout and stderr concurrently.</p>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 * @see PrintStreamHandler
 * @see SystemOutHandler
 */
public interface InvocationOutputHandler extends StreamConsumer {
	// empty by design
}
