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

/**
 * @author ： <a href="https://github.com/hiwepy">hiwepy</a>
 */
public class DefaultWkhtmlToPdfInvocationRequest extends AbstractInvocationRequest implements WkhtmlToPdfInvocationRequest {

	/**
	 * Base directory into which URL is saved. Default is .
	 */
	private File baseDir;
	/**
	 * Minimum interval in seconds between consecutive fetches. Default is 0 s
	 */
	private int delay;
	/**
	 * Indicates whether Do not download CSS stylesheets.
	 */
	private boolean dontDownloadStylesheets;
	/**
	 * The character encoding for the websites you are trying to download. The
	 * default is to try and guess the encoding.
	 */
	private String encoding;
	/**
	 * Any link that matches this regular expression will be ignored. This option
	 * can be specified multiple times, in which case as long as any regexp matches
	 * a link, it will be ignored. By default, no links are ignored. If both filter
	 * regexp and match regexp are specified, then filter regexp is applied first.
	 */
	private String filterRegexp;
	/**
	 * Only links that match this regular expression will be followed. This option
	 * can be specified multiple times, in which case as long as a link matches any
	 * one regexp, it will be followed. By default all links are followed.
	 */
	private String matchRegexp;
	/**
	 * The maximum number of files to download. This only applies to files from <a
	 * href> tags. Default is 9223372036854775807
	 */
	private long maxFiles = 9223372036854775807L;
	/**
	 * Maximum number of levels to recurse i.e. depth of links to follow. Default 1
	 */
	private int maxRecursions = 1;
	/**
	 * Timeout in seconds to wait for a response from the server. Default: 10.0 s
	 */
	private long timeout = 10;
	/**
	 * Where URL is for example https://google.com
	 */
	private String url;

	@Override
	public boolean isDontDownloadStylesheets() {
		return dontDownloadStylesheets;
	}

	@Override
	public File getBaseDirectory() {
		return baseDir;
	}

	@Override
	public int getDelay() {
		return delay;
	}

	@Override
	public String getEncoding() {
		return encoding;
	}

	@Override
	public String getFilterRegexp() {
		return filterRegexp;
	}

	@Override
	public String getMatchRegexp() {
		return matchRegexp;
	}
	
	@Override
	public long getMaxFiles() {
		return maxFiles;
	}
	
	@Override
	public int getMaxRecursions() {
		return maxRecursions;
	}
	
	@Override
	public long getTimeout() {
		return timeout;
	}
	
	@Override
	public String getURL() {
		return url;
	}

	@Override
	public InvocationRequest setBaseDirectory(File baseDir) {
		this.baseDir = baseDir;
		return this;
	}

	@Override
	public InvocationRequest setDelay(int delay) {
		this.delay = delay;
		return null;
	}

	@Override
	public InvocationRequest setDontDownloadStylesheets(boolean dontDownloadStylesheets) {
		this.dontDownloadStylesheets = dontDownloadStylesheets;
		return this;
	}

	@Override
	public InvocationRequest setEncoding(String encoding) {
		this.encoding = encoding;
		return this;
	}

	@Override
	public InvocationRequest setFilterRegexp(String filterRegexp) {
		this.filterRegexp = filterRegexp;
		return this;
	}

	@Override
	public InvocationRequest setMatchRegexp(String matchRegexp) {
		this.matchRegexp = matchRegexp;
		return this;
	}

	@Override
	public InvocationRequest setMaxFiles(long maxFiles) {
		this.maxFiles = maxFiles;
		return this;
	}

	@Override
	public InvocationRequest setMaxRecursions(int maxRecursions) {
		this.maxRecursions = maxRecursions;
		return this;
	}

	@Override
	public InvocationRequest setTimeout(long timeout) {
		this.timeout = timeout;
		return this;
	}

	@Override
	public InvocationRequest setURL(String url) {
		this.url = url;
		return this;
	}

}
