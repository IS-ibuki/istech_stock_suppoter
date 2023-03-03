package com.app.istech.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Download {

	@Autowired
	protected ResourceLoader resourceLoader;

	public final static MediaType TEXT_CSV_TYPE = new MediaType("text", "csv");

	// 製品構成マスタ作成画面
	@RequestMapping(path = "/download/productsTable", method = RequestMethod.GET)
	public ResponseEntity<Resource> productsTable(HttpServletResponse response) throws IOException {
		
		String pathAndFile = "static/excel/productsTable.csv";
		Resource resource = resourceLoader.getResource("classpath:" + pathAndFile);
		
		return ResponseEntity.ok()
				.contentType(TEXT_CSV_TYPE)
				.contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	// 製品構成マスタ作成画面
	@RequestMapping(path = "/download/compositionsTable", method = RequestMethod.GET)
	public ResponseEntity<Resource> compositionsTable(HttpServletResponse response) throws IOException {
		
		String pathAndFile = "static/excel/compositionsTable.csv";
		Resource resource = resourceLoader.getResource("classpath:" + pathAndFile);
		
		return ResponseEntity.ok()
				.contentType(TEXT_CSV_TYPE)
				.contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	
	private MediaType getContentType(Path path) throws IOException {
	    try {
	      System.out.println(MediaType.parseMediaType(Files.probeContentType(path)));
	      return MediaType.parseMediaType(Files.probeContentType(path));
	    } catch (IOException e) {
	      return MediaType.APPLICATION_OCTET_STREAM;
	    }
	}
	
	
}
