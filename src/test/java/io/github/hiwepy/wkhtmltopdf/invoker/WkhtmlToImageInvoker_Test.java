package io.github.hiwepy.wkhtmltopdf.invoker;

import java.io.File;
import java.util.Arrays;

import org.junit.Test;

import io.github.hiwepy.calibre.invoker.exception.CalibreInvocationException;
import io.github.hiwepy.calibre.invoker.request.DefaultWeb2diskInvocationRequest;
import io.github.hiwepy.calibre.invoker.request.Web2diskInvocationRequest;

public class WkhtmlToImageInvoker_Test {

	@Test
	public void testRequest() throws CalibreInvocationException {

		Web2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();

		request.setBaseDirectory(new File("D:\\tmp"));
		request.setDelay(0);
		request.setDontDownloadStylesheets(true);
		request.setEncoding("UTF-8");
		request.setFilterRegexp(null);
		request.setMatchRegexp(null);
		request.setURL("https://www.baidu.com");
		
		Invoker invoker = new DefaultInvoker();
		
		invoker.setCalibreHome(new File("C:\\Program Files (x86)\\Calibre2"));
		
		InvocationResult result = invoker.execute(request);

		System.out.println("ExitCode:" + result.getExitCode());
		System.out.println("Exception:" + result.getExecutionException().getMessage());

	}

	// @Test
	public void testGoals() throws CalibreInvocationException {

		Web2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();

		request.setGoals(Arrays.asList("--base-dir=D:\\", "--delay=0", "--dont-download-stylesheets",
				" --encoding=UTF-8", "--filter-regexp=", "--match-regexp=", "--max-files=20184", "--max-recursions=2",
				"--timeout=20"));
		
		Invoker invoker = new DefaultInvoker();
		invoker.setCalibreHome(new File("C:\\Program Files (x86)\\Calibre2"));
		InvocationResult result = invoker.execute(request);

		System.out.println("ExitCode:" + result.getExitCode());
		System.out.println("Exception:" + result.getExecutionException().getMessage());

	}

}
