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
public class DefaultWkhtmlToImageInvocationRequest extends AbstractInvocationRequest implements WkhtmlToImageInvocationRequest {

	/**
	 * Convert LRS to LRS, useful for debugging.
	 */
	private boolean lrs;
	/**
	 * LRS file. file.lrs
	 */
	private File lrsFile;
	/**
	 * Path to output file
	 */
	private File outputDirectory;

	@Override
	public boolean isLrs() {
		return lrs;
	}

	@Override
	public File getLrsFile() {
		return lrsFile;
	}

	@Override
	public File getOutputDirectory() {
		return outputDirectory;
	}

	@Override
	public InvocationRequest setLrs(boolean lrs) {
		this.lrs = lrs;
		return this;
	}

	@Override
	public InvocationRequest setOutputDirectory(File outputDirectory) {
		this.outputDirectory = outputDirectory;
		return this;
	}

	@Override
	public InvocationRequest setLrsFile(File lrs) {
		this.lrsFile = lrs;
		return this;
	}

}
