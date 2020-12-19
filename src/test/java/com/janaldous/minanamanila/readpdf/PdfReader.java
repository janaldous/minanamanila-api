package com.janaldous.minanamanila.readpdf;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PdfReader {

	public ApplePdfProduct readLine(String line) {
		String[] arr = line.split(" ");

		System.out.println(arr.length);
//		System.out.println(Arrays.toString(arr));

		if (arr.length != 7)
			throw new IllegalArgumentException("Line is in the wrong format: " + line);

		List<String> list = Stream.of(arr).map(x -> x.replace("\t", " ").trim()).collect(Collectors.toList());

		ApplePdfProduct output = new ApplePdfProduct();
		output.setName(list.get(0));
		output.setColor(list.get(1));
		output.setSrp(toBigDecimal(list.get(2)));
		output.setDiscount(toBigDecimal(list.get(3)));
		output.setLbd(toBigDecimal(list.get(4)));
		output.setLeadTime(list.get(5));
		output.setWarranty(list.get(6));

		return output;
	}

	private BigDecimal toBigDecimal(String input) {
		return new BigDecimal(input.replace(",", ""));
	}

}
