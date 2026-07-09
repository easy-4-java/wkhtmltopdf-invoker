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
package io.github.hiwepy.wkhtmltopdf.invoker.request;

import java.io.File;

public interface WkhtmlToImageInvocationRequest extends InvocationRequest {

	public boolean isLrs();

	public File getLrsFile();
	
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
