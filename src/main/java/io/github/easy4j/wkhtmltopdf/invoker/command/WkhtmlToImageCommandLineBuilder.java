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
package io.github.easy4j.wkhtmltopdf.invoker.command;

import java.io.File;
import java.io.IOException;

import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.cli.Commandline;

import io.github.easy4j.calibre.invoker.exception.CommandLineConfigurationException;
import io.github.easy4j.calibre.invoker.request.InvocationRequest;
import io.github.easy4j.calibre.invoker.request.Lrs2lrfInvocationRequest;

/**
 * Compile an LRS file into an LRF file.
 */
public class WkhtmlToImageCommandLineBuilder extends AbstractCommandLineBuilder {

	@Override
	protected void doCommandInternal(InvocationRequest request, Commandline cli)
			throws CommandLineConfigurationException {
		
		if(request instanceof WkhtmlToImageInvocationRequest) {

			WkhtmlToImageInvocationRequest lrs2lrfRequest = ( WkhtmlToImageInvocationRequest) request;
			
			setLrs(lrs2lrfRequest, cli);
			setOutputDirectory(lrs2lrfRequest, cli);
			// LRS file path. file.lrs
			cli.createArg().setValue(lrs2lrfRequest.getLrsFile().getAbsolutePath());
			
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
				executable = "lrs2lrf.exe";
			} else {
				executable = "lrs2lrf";
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
	
	protected void setLrs(WkhtmlToImageInvocationRequest request, Commandline cli) {
		if(request.isLrs()) {
			cli.createArg().setValue("--lrs");
		}
	}

	protected void setOutputDirectory(WkhtmlToImageInvocationRequest request, Commandline cli) {
		
		File outputDirectory = request.getOutputDirectory();
		if (outputDirectory != null) {
			try {
				File canSet = outputDirectory.getCanonicalFile();
				outputDirectory = canSet;
			} catch (IOException e) {
				logger.debug("Failed to canonicalize output path: " + outputDirectory.getAbsolutePath() + ".", e);
			}

			cli.createArg().setValue("-o");
			cli.createArg().setValue(outputDirectory.getPath());
		}
 
	}

	
	 
	

}
