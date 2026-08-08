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
package io.github.easy4j.wkhtmltopdf.invoker.command;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.cli.Commandline;

import io.github.easy4j.wkhtmltopdf.invoker.exception.CommandLineConfigurationException;
import io.github.easy4j.wkhtmltopdf.invoker.request.InvocationRequest;
import io.github.easy4j.wkhtmltopdf.invoker.request.WkhtmlToPdfInvocationRequest;


/**
 * Command-line builder for the {@code wkhtmltopdf} native binary. Translates
 * a {@link WkhtmlToPdfInvocationRequest} into the arguments expected by the
 * {@code wkhtmltopdf} CLI (e.g. {@code --delay}, {@code --encoding},
 * {@code --max-files}, etc.).
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 * @see AbstractCommandLineBuilder
 * @see WkhtmlToPdfInvocationRequest
 */
public class WkhtmlToPdfCommandLineBuilder extends AbstractCommandLineBuilder {

	/**
	 * {@inheritDoc}
	 *
	 * <p>Appends wkhtmltopdf-specific arguments such as base directory, delay,
	 * encoding, filter/match regexp, max files, max recursions, timeout and the
	 * target URL.</p>
	 */
	@Override
	protected void doCommandInternal(InvocationRequest request, Commandline cli)
			throws CommandLineConfigurationException {

		if(request instanceof WkhtmlToPdfInvocationRequest) {

			WkhtmlToPdfInvocationRequest web2diskRequest = ( WkhtmlToPdfInvocationRequest) request;
			
			setBaseDirectory(web2diskRequest, cli);
			setDelay(web2diskRequest, cli);
			setDontDownloadStylesheets(web2diskRequest, cli);
			setEncoding(web2diskRequest, cli);
			setFilterRegexp(web2diskRequest, cli);
			setMatchRegexp(web2diskRequest, cli);
			setMaxFiles(web2diskRequest, cli);
			setMaxRecursions(web2diskRequest, cli);
			setTimeout(web2diskRequest, cli);
			// Where URL is for example https://google.com
			cli.createArg().setValue(web2diskRequest.getURL());
			
		}
		
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>Locates the {@code web2disk} executable within the Calibre home
	 * directory.</p>
	 */
	@Override
	protected File findCalibreExecutable() throws CommandLineConfigurationException, IOException {
		
		if (calibreHome == null) {
			findCalibreHome();
		}

		logger.debug("Using ${calibre.home} of: \'" + calibreHome + "\'.");

		if (calibreExecutable == null || !calibreExecutable.isAbsolute()) {
			String executable;
			if (calibreExecutable != null) {
				executable = calibreExecutable.getPath();
			} else if (Os.isFamily("windows")) {
				executable = "web2disk.exe";
			} else {
				executable = "web2disk";
			}

			calibreExecutable = new File(calibreHome, executable);

			try {
				File canonicalMvn = calibreExecutable.getCanonicalFile();
				calibreExecutable = canonicalMvn;
			} catch (IOException e) {
				logger.debug("Failed to canonicalize maven executable: " + calibreExecutable + ". Using as-is.", e);
			}

			if (!calibreExecutable.isFile()) {
				throw new CommandLineConfigurationException("Calibre executable not found at: " + calibreExecutable);
			}
		}

		return calibreExecutable;
	}
	

	/**
	 * Appends the {@code -d} (base directory) argument to the command line.
	 *
	 * @param request the invocation request.
	 * @param cli     the command line to append to.
	 */
	protected void setBaseDirectory(WkhtmlToPdfInvocationRequest request, Commandline cli) {
		
		File baseDirectory = request.getBaseDirectory();
		if (baseDirectory != null) {
			try {
				File canSet = baseDirectory.getCanonicalFile();
				baseDirectory = canSet;
			} catch (IOException e) {
				logger.debug("Failed to canonicalize base directory path: " + baseDirectory.getAbsolutePath()
						+ ".", e);
			}

			cli.createArg().setValue("-d");
			cli.createArg().setValue(baseDirectory.getPath());
		}
 
	}

	/**
	 * Appends the {@code --delay} argument to the command line.
	 *
	 * @param request the invocation request.
	 * @param cli     the command line to append to.
	 */
	protected void setDelay(WkhtmlToPdfInvocationRequest request, Commandline cli) {
		int delay = request.getDelay();
		cli.createArg().setValue("--delay");
		cli.createArg().setValue(String.valueOf(delay));
	}
 
	/**
	 * Appends the {@code --dont-download-stylesheets} flag when enabled.
	 *
	 * @param request the invocation request.
	 * @param cli     the command line to append to.
	 */
	protected void setDontDownloadStylesheets(WkhtmlToPdfInvocationRequest request, Commandline cli) {
		if(request.isDontDownloadStylesheets()) {
			cli.createArg().setValue("--dont-download-stylesheets");
		}
	}
	
	/**
	 * Appends the {@code --encoding} argument when a non-empty encoding is set.
	 *
	 * @param request the invocation request.
	 * @param cli     the command line to append to.
	 */
	protected void setEncoding(WkhtmlToPdfInvocationRequest request, Commandline cli) {
		if(StringUtils.isNotEmpty(request.getEncoding())) {
			cli.createArg().setValue("--encoding");
			cli.createArg().setValue(request.getEncoding());
		}
	}
	
	/**
	 * Appends the {@code --filter-regexp} argument when a non-empty regexp is set.
	 *
	 * @param request the invocation request.
	 * @param cli     the command line to append to.
	 */
	protected void setFilterRegexp(WkhtmlToPdfInvocationRequest request, Commandline cli) {
		if(StringUtils.isNotEmpty(request.getFilterRegexp())) {
			cli.createArg().setValue("--filter-regexp");
			cli.createArg().setValue(request.getFilterRegexp());
		}
	}
	
	/**
	 * Appends the {@code --match-regexp} argument when a non-empty regexp is set.
	 *
	 * @param request the invocation request.
	 * @param cli     the command line to append to.
	 */
	protected void setMatchRegexp(WkhtmlToPdfInvocationRequest request, Commandline cli) {
		if(StringUtils.isNotEmpty(request.getMatchRegexp())) {
			cli.createArg().setValue("--match-regexp");
			cli.createArg().setValue(request.getMatchRegexp());
		}
	}
   
	/**
	 * Appends the {@code -n} (max files) argument when the value is positive.
	 *
	 * @param request the invocation request.
	 * @param cli     the command line to append to.
	 */
	protected void setMaxFiles(WkhtmlToPdfInvocationRequest request, Commandline cli) {
		long maxFiles = request.getMaxFiles();
		if (maxFiles > 0) {
			cli.createArg().setValue("-n");
			cli.createArg().setValue(String.valueOf(maxFiles));
		}
	}

	/**
	 * Appends the {@code -r} (max recursions) argument when the value is positive.
	 *
	 * @param request the invocation request.
	 * @param cli     the command line to append to.
	 */
	protected void setMaxRecursions(WkhtmlToPdfInvocationRequest request, Commandline cli) {
		long maxRecursions = request.getMaxRecursions();
		if (maxRecursions > 0) {
			cli.createArg().setValue("-r");
			cli.createArg().setValue(String.valueOf(maxRecursions));
		}
	}
	
	/**
	 * Appends the {@code -t} (timeout) argument when the value is positive.
	 *
	 * @param request the invocation request.
	 * @param cli     the command line to append to.
	 */
	protected void setTimeout(WkhtmlToPdfInvocationRequest request, Commandline cli) {
		long timeout = request.getTimeout();
		if (timeout > 0) {
			cli.createArg().setValue("-t");
			cli.createArg().setValue(String.valueOf(timeout));
		}
	}



	/**
	 * {@inheritDoc}
	 *
	 * @return a {@link File} representing the {@code wkhtmltopdf} executable name.
	 */
	@Override
	protected File findWkhtmltopdfExecutable() throws CommandLineConfigurationException, IOException {
		return new File("wkhtmltopdf");
	}
}
