package com.experiment.application;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.AbstractResource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class is not used. I was trying to stream the file and send it
 * in attachment rather creating in classpath.
 */
@Deprecated
public class StreamAttachmentDataSource extends AbstractResource {
	private final static Log logger = LogFactory.getLog(StreamAttachmentDataSource.class);
	private ByteArrayOutputStream outputStream;
	private String name;
	private String contentType;

	public StreamAttachmentDataSource(InputStream inputStream, String name,
			String contentType) {
		this.outputStream = new ByteArrayOutputStream();
		this.name = name;
		this.contentType = contentType;

		int read;
		byte[] buffer = new byte[256];
		try {
			while((read = inputStream.read(buffer)) != -1) {
				getOutputStream().write(buffer, 0, read);
			}
		} catch (IOException e) {
			// logger.error(“Cannot create inputstream for mail attachment”);
			e.printStackTrace();
		}
	}

	public String getDescription() {
		return "hello";
	}

	public InputStream getInputStream() throws IOException {
		return new ByteArrayInputStream(this.outputStream.toByteArray());
	}

	public String getContentType() {
		return contentType;
	}

	public String getName() {
		return name;
	}

	public ByteArrayOutputStream getOutputStream() {
		return outputStream;
	}

}


