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

import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.cli.Commandline;

import io.github.easy4j.wkhtmltopdf.invoker.exception.CommandLineConfigurationException;
import io.github.easy4j.wkhtmltopdf.invoker.request.InvocationRequest;
import io.github.easy4j.wkhtmltopdf.invoker.request.WkhtmlToImageInvocationRequest;


/**
 * Command-line builder for the {@code wkhtmltoimage} native binary. Translates
 * a {@link WkhtmlToImageInvocationRequest} into the arguments expected by the
 * {@code wkhtmltoimage} CLI (e.g. {@code --lrs}, {@code -o}).
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 * @see AbstractCommandLineBuilder
 * @see WkhtmlToImageInvocationRequest
 */
public class WkhtmlToImageCommandLineBuilder extends AbstractCommandLineBuilder {

	/**
	 * {@inheritDoc}
	 *
	 * <p>Appends wkhtmltoimage-specific arguments: the {@code --lrs} flag,
	 * the output directory ({@code -o}), and the LRS file path.</p>
	 */
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

	/**
	 * {@inheritDoc}
	 *
	 * <p>Locates the {@code lrs2lrf} executable within the Calibre home
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
	
	/**
	 * Appends the {@code --lrs} flag when LRS mode is enabled.
	 *
	 * @param request the invocation request.
	 * @param cli     the command line to append to.
	 */
	protected void setLrs(WkhtmlToImageInvocationRequest request, Commandline cli) {
		if(request.isLrs()) {
			cli.createArg().setValue("--lrs");
		}
	}

	/**
	 * Appends the {@code -o} (output directory) argument when an output
	 * directory is specified.
	 *
	 * @param request the invocation request.
	 * @param cli     the command line to append to.
	 */
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

	
	 
	



	/**
	 * {@inheritDoc}
	 *
	 * @return a {@link File} representing the {@code wkhtmltoimage} executable name.
	 */
	@Override
	protected File findWkhtmltopdfExecutable() throws CommandLineConfigurationException, IOException {
		return new File("wkhtmltoimage");
	}
}
