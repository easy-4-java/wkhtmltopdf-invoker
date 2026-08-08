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

/**
 * Extends {@link InvocationRequest} with options specific to the
 * {@code wkhtmltopdf} (web-to-disk) tool, such as base directory, delay,
 * encoding, filter/match regular expressions, maximum files, maximum
 * recursion depth, timeout and the target URL.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 * @see InvocationRequest
 * @see DefaultWkhtmlToPdfInvocationRequest
 */
public interface WkhtmlToPdfInvocationRequest extends InvocationRequest {

	/**
	 * Indicates whether CSS stylesheets should not be downloaded.
	 *
	 * @return {@code true} if stylesheet download is disabled.
	 */
	public boolean isDontDownloadStylesheets();

	/**
	 * Returns the base directory into which the URL content is saved.
	 *
	 * @return the base directory, or {@code null} if not set.
	 */
	public File getBaseDirectory();

	/**
	 * Returns the minimum interval in seconds between consecutive fetches.
	 *
	 * @return the delay in seconds, default is {@code 0}.
	 */
	public int getDelay();

	/**
	 * Returns the character encoding for the websites being downloaded.
	 *
	 * @return the encoding name, or {@code null} to auto-detect.
	 */
	public String getEncoding();

	/**
	 * Returns the regular expression used to filter out links.
	 *
	 * @return the filter regexp, or {@code null} if not set.
	 */
	public String getFilterRegexp();

	/**
	 * Returns the regular expression used to match links that should be followed.
	 *
	 * @return the match regexp, or {@code null} if not set.
	 */
	public String getMatchRegexp();

	/**
	 * Returns the maximum number of files to download.
	 *
	 * @return the max files count, default is {@link Long#MAX_VALUE}.
	 */
	public long getMaxFiles();

	/**
	 * Returns the maximum number of recursion levels (link depth).
	 *
	 * @return the max recursion depth, default is {@code 1}.
	 */
	public int getMaxRecursions();

	/**
	 * Returns the timeout in seconds to wait for a server response.
	 *
	 * @return the timeout in seconds, default is {@code 10}.
	 */
	public long getTimeout();

	/**
	 * Returns the target URL to download (e.g. {@code https://google.com}).
	 *
	 * @return the URL string, or {@code null} if not set.
	 */
	public String getURL();

	/**
	 * Set the value of the {@code base-dir} {@code true} if the argument
	 * {@code --base-dir} was specified, otherwise {@code false}
	 */
	InvocationRequest setBaseDirectory(File baseDir);

	/**
	 * Set the value of the {@code delay} {@code true} if the argument
	 * {@code --delay} was specified, otherwise {@code false}
	 */
	InvocationRequest setDelay(int delay);

	/**
	 * Set the value of the {@code dont-download-stylesheets} {@code true} if the
	 * argument {@code --dont-download-stylesheets} was specified, otherwise
	 * {@code false}
	 */
	InvocationRequest setDontDownloadStylesheets(boolean dontDownloadStylesheets);

	/**
	 * Set the value of the {@code encoding} {@code true} if the argument
	 * {@code  --encoding} was specified, otherwise {@code false}
	 */
	InvocationRequest setEncoding(String encoding);

	/**
	 * Set the value of the {@code filter-regexp} {@code true} if the argument
	 * {@code --filter-regexp} was specified, otherwise {@code false}
	 */
	InvocationRequest setFilterRegexp(String filterRegexp);

	/**
	 * Set the value of the {@code match-regexp} {@code true} if the argument
	 * {@code --match-regexp} was specified, otherwise {@code false}
	 */
	InvocationRequest setMatchRegexp(String matchRegexp);

	/**
	 * Set the value of the {@code max-files} {@code true} if the argument
	 * {@code --max-files} was specified, otherwise {@code false}
	 */
	InvocationRequest setMaxFiles(long maxFiles);

	/**
	 * Set the value of the {@code max-recursions} {@code true} if the argument
	 * {@code --max-recursions} was specified, otherwise {@code false}
	 */
	InvocationRequest setMaxRecursions(int maxRecursions);

	/**
	 * Set the value of the {@code timeout} {@code true} if the argument
	 * {@code --timeout} was specified, otherwise {@code false}
	 */
	InvocationRequest setTimeout(long timeout);
	/**
	 * Where URL is for example https://google.com
	 */
	InvocationRequest setURL(String url);

}
