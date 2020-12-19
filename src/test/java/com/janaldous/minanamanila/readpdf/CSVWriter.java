package com.janaldous.minanamanila.readpdf;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

public class CSVWriter {

	public void writeCSV(String outputFilename, List<ApplePdfProduct> products) throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, IOException {
		// Creating writer class to generate
		// csv file
		FileWriter writer = new FileWriter(outputFilename);

		// Create Mapping Strategy to arrange the
		// column name in order
		ColumnPositionMappingStrategy<ApplePdfProduct> mappingStrategy = new ColumnPositionMappingStrategy<>();
		mappingStrategy.setType(ApplePdfProduct.class);
		
		 // Arrange column name as provided in below array. 
        String[] columns = new String[]  
                { "name", "color", "srp", "discount", "lbd", "leadTime", "warranty"}; 
        mappingStrategy.setColumnMapping(columns);

		// Creating StatefulBeanToCsv object
		StatefulBeanToCsvBuilder<ApplePdfProduct> builder = new StatefulBeanToCsvBuilder<>(writer);
		StatefulBeanToCsv<ApplePdfProduct> beanWriter = builder.withMappingStrategy(mappingStrategy).build();

		// Write list to StatefulBeanToCsv object
		beanWriter.write(products);

		// closing the writer object
		writer.close();
	}

}
