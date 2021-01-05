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
		assertEquals("54990", output.getSrp().toString());
		assertEquals("1000", output.getDiscount().toString());
		assertEquals("53990", output.getLbd().toString());
		assertEquals("7- 14 Days", output.getLeadTime());
		assertEquals("1 Yr Apple", output.getWarranty());
	}

	@Test
	public void test2() {
		String input = "APPLE	MacBook	Pro	(2020)	13\"	1.4GHZ	(	MXK62ZP/A)	256GB	with	Touchbar	 Silver 63,500														 1,000										 62,500																 7-	14	Days 1	Yr	Apple\r\n";
		
		pdfReader.readLine(input);
	}

}
