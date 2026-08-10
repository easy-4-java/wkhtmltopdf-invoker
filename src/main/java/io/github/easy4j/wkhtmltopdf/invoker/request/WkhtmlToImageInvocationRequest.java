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
 * {@code wkhtmltoimage} tool, such as LRS mode, LRS file path and
 * output directory.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see InvocationRequest
 * @see DefaultWkhtmlToImageInvocationRequest
 */
public interface WkhtmlToImageInvocationRequest extends InvocationRequest {

	/**
	 * Indicates whether LRS-to-LRS conversion mode is enabled (useful for
	 * debugging).
	 *
	 * @return {@code true} if LRS mode is enabled.
	 */
	public boolean isLrs();

	/**
	 * Returns the LRS file to process.
	 *
	 * @return the LRS file, or {@code null} if not set.
	 */
	public File getLrsFile();

	/**
	 * Returns the output directory for the generated image.
	 *
	 * @return the output directory, or {@code null} if not set.
	 */
	public File getOutputDirectory();

	/**
	 * Set the value of the {@code lrs} {@code true} if the
	 * argument {@code --lrs} was specified, otherwise
	 * {@code false}
	 */
	InvocationRequest setLrs(boolean lrs);
	
	/**
	 * Set the value of the {@code output} {@code true} if the argument
	 * {@code --output} was specified, otherwise {@code false}
	 */
	InvocationRequest setOutputDirectory(File output);

	/**
	 * LRS file path. file.lrs
	 */
	InvocationRequest setLrsFile(File lrsFile);
	
}
