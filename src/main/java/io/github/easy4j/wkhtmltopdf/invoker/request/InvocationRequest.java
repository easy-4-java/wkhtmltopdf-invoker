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
import java.util.List;
import java.util.Map;
import java.util.Properties;

import io.github.easy4j.wkhtmltopdf.invoker.InvocationOutputHandler;

/**
 * Specifies the parameters used to control a {@code wkhtmltopdf} (or
 * {@code wkhtmltoimage}) invocation.
 *
 * <p>This interface defines the common options shared by all wkhtmltopdf-family
 * tools: output/error handlers, environment variables, system properties,
 * collation, DPI, grayscale mode and more. Tool-specific sub-interfaces
 * (e.g. {@link WkhtmlToPdfInvocationRequest},
 * {@link WkhtmlToImageInvocationRequest}) add further options.</p>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 * @see WkhtmlToPdfInvocationRequest
 * @see WkhtmlToImageInvocationRequest
 */
public interface InvocationRequest {

	/**
	 * Indicates whether the environment variables of the current process should be
	 * propagated to the wkhtmltopdf invocation. By default, the current environment
	 * variables are inherited by the new wkhtmltopdf invocation.
	 * 
	 * @return <code>true</code> if the environment variables should be propagated,
	 *         <code>false</code> otherwise.
	 */
	boolean isShellEnvironmentInherited();

	/**
	 * Indicates whether detailed output information should be shown. Useful
	 * for debugging wkhtmltopdf invocations.
	 *
	 * @return {@code true} if verbose mode is enabled, {@code false} otherwise.
	 */
	boolean isVerbose();

	/**
	 * Gets the handler used to capture the standard output from the wkhtmltopdf build.
	 * 
	 * @return The output handler or <code>null</code> if not set.
	 */
	InvocationOutputHandler getOutputHandler(InvocationOutputHandler defaultHandler);

	/**
	 * Gets the handler used to capture the error output from the wkhtmltopdf build.
	 * 
	 * @return The error handler or <code>null</code> if not set.
	 */
	InvocationOutputHandler getErrorHandler(InvocationOutputHandler defaultHandler);

	/**
	 * Gets the path to the base directory of the wkhtmltopdf installation used to run
	 * wkhtmltopdf.
	 * 
	 * @return The path to the base directory of the wkhtmltopdf installation used to
	 *         run wkhtmltopdf or <code>null</code> to use the default wkhtmltopdf home.
	 */
	File getWkhtmltopdfHome();

	/**
	 * Gets the system properties for the wkhtmltopdf invocation.
	 * 
	 * @return The system properties for the wkhtmltopdf invocation or <code>null</code>
	 *         if not set.
	 */
	Properties getProperties();

	/**
	 * Gets the goals for the wkhtmltopdf invocation.
	 * 
	 * @return The goals for the wkhtmltopdf invocation or <code>null</code> if not set.
	 */
	List<String> getGoals();

	/**
	 * Gets the environment variables for the wkhtmltopdf invocation.
	 * @return The environment variables for the wkhtmltopdf invocation or <code>null</code> if not set.
	 */
	Map<String, String> getShellEnvironments();
	
	/**
	 * Indicates whether to collate when printing multiple copies. Default
	 * is {@code true}.
	 *
	 * @return {@code true} if collation is enabled, {@code false} otherwise.
	 */
	boolean isCollate();
	
	/**
	 * Returns the cookie jar file used to read and write cookies during the
	 * wkhtmltopdf invocation.
	 *
	 * @return the cookie jar file, or {@code null} if not set.
	 */
	File getCookieJar();
	
	/**
	 * Returns the number of copies to print into the PDF file.
	 *
	 * @return the number of copies, default is {@code 1}.
	 */
	int getCopies();
	
	/**
	 * Returns the DPI setting for the PDF output. This has no effect on X11-based
	 * systems.
	 *
	 * @return the DPI value, default is {@code 96}.
	 */
	int getDpi();
	
	/**
	 * Indicates whether the PDF will be generated in grayscale.
	 *
	 * @return {@code true} if grayscale mode is enabled, {@code false} otherwise.
	 */
	boolean isGrayscale();

	/**
	 * Returns the DPI to which embedded images are scaled down.
	 *
	 * @return the image DPI, default is {@code 600}.
	 */
	int getImageDpi();
	
	
	// ----------------------------------------------------------------------
	//
	// ----------------------------------------------------------------------

	/**
	 * Sets the handler used to capture the standard output from the wkhtmltopdf build.
	 * @param outputHandler The output handler, may be <code>null</code> if the output is not of interest.
	 * @return This invocation request.
	 */
	InvocationRequest setOutputHandler(InvocationOutputHandler outputHandler);

	/**
	 * Sets the handler used to capture the error output from the wkhtmltopdf build.
	 * 
	 * @param errorHandler The error handler, may be <code>null</code> if the output is not of interest.
	 * @return This invocation request.
	 */
	InvocationRequest setErrorHandler(InvocationOutputHandler errorHandler);

	/**
	 * Sets the path to the base directory of the wkhtmltopdf installation used to run wkhtmltopdf.
	 * 
	 * @param wkhtmltopdfHome The path to the base directory of the wkhtmltopdf installation used to
	 *            run wkhtmltopdf, may be <code>null</code> to use the default wkhtmltopdf home.
	 * @return This invocation request.
	 */
	InvocationRequest setWkhtmltopdfHome(File wkhtmltopdfHome);

	/**
	 * Sets the system properties for the wkhtmltopdf invocation.
	 * @param properties The system properties for the wkhtmltopdf invocation, may be <code>null</code> if not set.
	 * @return This invocation request.
	 */
	InvocationRequest setProperties(Properties properties);

	/**
	 * Sets the goals for the wkhtmltopdf invocation.
	 * @param goals The goals for the wkhtmltopdf invocation 
	 * @return This invocation request.
	 */
	InvocationRequest setGoals(List<String> goals);

	/**
	 * Specifies whether the environment variables of the current process should be
	 * propagated to the wkhtmltopdf invocation.
	 * 
	 * @param shellEnvironmentInherited
	 *            <code>true</code> if the environment variables should be
	 *            propagated, <code>false</code> otherwise.
	 * @return This invocation request.
	 */
	InvocationRequest setShellEnvironmentInherited(boolean shellEnvironmentInherited);

	/**
	 * Adds the specified environment variable to the wkhtmltopdf invocation.
	 * 
	 * @param name The name of the environment variable, must not be <code>null</code>.
	 * @param value The value of the environment variable, must not be <code>null</code>.
	 * @return This invocation request.
	 */
	InvocationRequest addShellEnvironment(String name, String value);
	
	
	/**
	 * Enables or disables collation when printing multiple copies.
	 *
	 * @param collate {@code true} to enable collation ({@code --collate}),
	 *                {@code false} to disable it ({@code --no-collate}).
	 * @return this invocation request.
	 */
	InvocationRequest setCollate(boolean collate);
	
	/**
	 * Sets the cookie jar file used to read and write cookies during the
	 * invocation.
	 *
	 * @param cookieJar the cookie jar file, may be {@code null}.
	 * @return this invocation request.
	 */
	InvocationRequest setCookieJar(File cookieJar);
	
	/**
	 * Sets the number of copies to print into the PDF file.
	 *
	 * @param copies the number of copies, default is {@code 1}.
	 * @return this invocation request.
	 */
	InvocationRequest setCopies(int copies);
	
	/**
	 * Sets the DPI explicitly. This has no effect on X11-based systems.
	 *
	 * @param dpi the DPI value, default is {@code 96}.
	 * @return this invocation request.
	 */
	InvocationRequest setDpi(int dpi);
	
	/**
	 * Enables or disables grayscale mode for the generated PDF.
	 *
	 * @param grayscale {@code true} to generate a grayscale PDF
	 *                  ({@code --grayscale}), {@code false} otherwise.
	 * @return this invocation request.
	 */
	InvocationRequest setGrayscale(boolean grayscale);
	
	/**
	 * Sets the DPI to which embedded images are scaled down.
	 *
	 * @param dpi the image DPI, default is {@code 600}.
	 * @return this invocation request.
	 */
	InvocationRequest setImageDpi(int dpi);
	
	/**
	 * Sets the output file for the generated PDF or image.
	 *
	 * @param outputFile the output file, may be {@code null}.
	 * @return this invocation request.
	 */
	InvocationRequest setOutputFile(File outputFile);

}
