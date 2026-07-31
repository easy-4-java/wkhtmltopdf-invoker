package io.github.easy4j.wkhtmltox4j.source;

import java.io.File;
import java.nio.file.Path;

/**
 * Local file or remote url source.
 */
public class UrlSource implements Source<String> {

	private String url;
	
	public UrlSource(String url) {
		this.url = url;
	}
	
	public String getSource() {
		return url;
	}
	
	public static UrlSource fromUrl(String url) {
		return new UrlSource(url);
	}
	
	public static UrlSource from(File file) {
		return new UrlSource(file.getAbsolutePath());
	}
	
	public static UrlSource from(Path path) {
		return new UrlSource(path.toAbsolutePath().toString());
	}
	
}
