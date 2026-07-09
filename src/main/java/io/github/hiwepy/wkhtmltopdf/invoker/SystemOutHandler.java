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
package io.github.hiwepy.wkhtmltopdf.invoker;

/**
 * Offers an output handler that writes to {@link System#out}.
 */
public class SystemOutHandler extends PrintStreamHandler {

	/**
	 * Creates a new output handler.
	 */
	public SystemOutHandler() {
		this(false);
	}

	/**
	 * Creates a new output handler.
	 * 
	 * @param alwaysFlush
	 *            A flag whether the print stream should be flushed after each line.
	 */
	public SystemOutHandler(boolean alwaysFlush) {
		super(System.out, alwaysFlush);
	}

}
