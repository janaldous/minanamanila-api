package com.janaldous.minanamanila.readpdf;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.junit.jupiter.api.Test;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

public class ReadPdfTest {

	@Test
	public void extractTextFromPdf() throws IOException {
		// Loading an existing document
		String absolutePath = getFilePath("LBD-PRICELIST-APPLE-_XIAOMI-12042020.pdf");
		File file = new File(absolutePath);
		PDDocument document = PDDocument.load(file);

		// Instantiate PDFTextStripper class
		PDFTextStripper pdfStripper = new PDFTextStripper();

		// Retrieving text from PDF document
		String text = pdfStripper.getText(document);
		System.out.println(text);

		// Closing the document
		document.close();
	}

	private String getFilePath(String filename) {
		Path resourceDirectory = Paths.get("src","test","resources", filename);
		String absolutePath = resourceDirectory.toFile().getAbsolutePath();
		return absolutePath;
	}
	
	@Test
	public void readPdf() throws IOException {
		int page = 3;
        int x = 83;
        int y = 791-683;
        int width = 591-x;
        int height = 680-54;// (791-628)-y;
        
//        int x = 365;
//        int y = 791-683;
//        int width = 500-x;//592+x;
//        int height = 680-54;// (791-628)-y;
        
        //680-54

        PDDocument document = PDDocument.load(new File(getFilePath("LBD-PRICELIST-APPLE-_XIAOMI-12042020.pdf")));
		
		PDFTextStripperByArea textStripper = new PDFTextStripperByArea();
        Rectangle2D rect = new java.awt.geom.Rectangle2D.Float(x, y, width, height);
        textStripper.addRegion("region", rect);

        PDPage docPage = document.getPage(page);

        textStripper.extractRegions(docPage);

        String textForRegion = textStripper.getTextForRegion("region");

        System.out.println(textForRegion);
	}
	
	@Test
	public void convertTextToApplePdfProductObjects() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
//		String filename = "extracted_text_apple_products.txt";
		String filename = "page4.txt";
		String absolutePath = getFilePath(filename);
		File file = new File(absolutePath);
		
		List<String> lines = FileUtils.readLines(file, "UTF-8");
		
		PdfReader pdfReader = new PdfReader();
		
		List<ApplePdfProduct> products = lines.stream()
			.filter(line -> !line.startsWith("#"))
			.map(line -> pdfReader.readLine(line))
			.collect(Collectors.toList());
		
		CSVWriter writer = new CSVWriter();
		writer.writeCSV(absolutePath.replace(".txt", ".csv"), products);
	}
	
}
