package com.janaldous.minanamanila.readpdf;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PdfReaderTest {

	private PdfReader pdfReader = new PdfReader();

	@Test
	public void testParseApplePdfLine() {
		String fullLine = "APPLE	Macbook	Air	M1	8GB	256GB	-	Gold Gold 54,990														 1,000										 53,990																 7-	14	Days 1	Yr	Apple\r\n";

		ApplePdfProduct output = pdfReader.readLine(fullLine);

		assertEquals("APPLE Macbook Air M1 8GB 256GB - Gold", output.getName());
		assertEquals("Gold", output.getColor());
		assertEquals("54,990", output.getSrp());
		assertEquals("1,000", output.getDiscount());
		assertEquals("53,990", output.getLbd());
		assertEquals("7- 14 Days", output.getLeadTime());
		assertEquals("1 Yr Apple", output.getWarranty());
	}

	@Test
	public void test2() {
		String input = "APPLE	MacBook	Pro	(2020)	13\"	1.4GHZ	(	MXK62ZP/A)	256GB	with	Touchbar	 Silver 63,500														 1,000										 62,500																 7-	14	Days 1	Yr	Apple\r\n";
		
		pdfReader.readLine(input);
	}

}
