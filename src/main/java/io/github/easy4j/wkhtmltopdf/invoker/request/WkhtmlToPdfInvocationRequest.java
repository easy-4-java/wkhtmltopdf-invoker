/**
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
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

public interface WkhtmlToPdfInvocationRequest extends InvocationRequest {

	public boolean isDontDownloadStylesheets();

	public File getBaseDirectory();

	public int getDelay();

	public String getEncoding();

	public String getFilterRegexp();

	public String getMatchRegexp();

	public long getMaxFiles();

	public int getMaxRecursions();

	public long getTimeout();

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
