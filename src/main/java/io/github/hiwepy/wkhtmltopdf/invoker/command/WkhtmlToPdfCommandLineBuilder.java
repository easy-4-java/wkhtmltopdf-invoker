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
package io.github.hiwepy.wkhtmltopdf.invoker.command;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.cli.Commandline;

import io.github.hiwepy.calibre.invoker.exception.CommandLineConfigurationException;
import io.github.hiwepy.calibre.invoker.request.InvocationRequest;
import io.github.hiwepy.calibre.invoker.request.Web2diskInvocationRequest;

/**
 */
public class WkhtmlToPdfCommandLineBuilder extends AbstractCommandLineBuilder {

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

	protected void setDelay(WkhtmlToPdfInvocationRequest request, Commandline cli) {
		int delay = request.getDelay();
		cli.createArg().setValue("--delay");
		cli.createArg().setValue(String.valueOf(delay));
	}
 
	protected void setDontDownloadStylesheets(WkhtmlToPdfInvocationRequest request, Commandline cli) {
		if(request.isDontDownloadStylesheets()) {
			cli.createArg().setValue("--dont-download-stylesheets");
		}
	}
	
	protected void setEncoding(WkhtmlToPdfInvocationRequest request, Commandline cli) {
		if(StringUtils.isNotEmpty(request.getEncoding())) {
			cli.createArg().setValue("--encoding");
			cli.createArg().setValue(request.getEncoding());
		}
	}
	
	protected void setFilterRegexp(WkhtmlToPdfInvocationRequest request, Commandline cli) {
		if(StringUtils.isNotEmpty(request.getFilterRegexp())) {
			cli.createArg().setValue("--filter-regexp");
			cli.createArg().setValue(request.getFilterRegexp());
		}
	}
	
	protected void setMatchRegexp(WkhtmlToPdfInvocationRequest request, Commandline cli) {
		if(StringUtils.isNotEmpty(request.getMatchRegexp())) {
			cli.createArg().setValue("--match-regexp");
			cli.createArg().setValue(request.getMatchRegexp());
		}
	}
   
	protected void setMaxFiles(WkhtmlToPdfInvocationRequest request, Commandline cli) {
		long maxFiles = request.getMaxFiles();
		if (maxFiles > 0) {
			cli.createArg().setValue("-n");
			cli.createArg().setValue(String.valueOf(maxFiles));
		}
	}

	protected void setMaxRecursions(WkhtmlToPdfInvocationRequest request, Commandline cli) {
		long maxRecursions = request.getMaxRecursions();
		if (maxRecursions > 0) {
			cli.createArg().setValue("-r");
			cli.createArg().setValue(String.valueOf(maxRecursions));
		}
	}
	
	protected void setTimeout(WkhtmlToPdfInvocationRequest request, Commandline cli) {
		long timeout = request.getTimeout();
		if (timeout > 0) {
			cli.createArg().setValue("-t");
			cli.createArg().setValue(String.valueOf(timeout));
		}
	}

}
