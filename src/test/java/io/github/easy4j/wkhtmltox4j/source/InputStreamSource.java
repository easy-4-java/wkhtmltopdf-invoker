package io.github.easy4j.wkhtmltox4j.source;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class InputStreamSource implements Source<InputStream> {

	private byte[] bytes;
	
	public InputStreamSource(byte[] bytes) {
		this.bytes = bytes;
	}
	
	public InputStream getSource() {
		return new ByteArrayInputStream(this.bytes);
	}
	
	public static InputStreamSource from(byte[] bytes) {
		return new InputStreamSource(bytes);
	}
	
	public static InputStreamSource from(InputStream inputStream) throws IOException {
		return new InputStreamSource(IOUtils.toByteArray(inputStream));
	}
	
	public static InputStreamSource from(String content, String charsetName) throws IOException {
		return new InputStreamSource(content.getBytes(charsetName));
	}
	
}
