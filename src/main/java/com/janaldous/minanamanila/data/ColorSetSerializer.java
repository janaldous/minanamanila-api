package com.janaldous.minanamanila.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ColorSetSerializer extends StdSerializer<Set<ProductColor>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ColorSetSerializer() {
		this(null);
	}

	public ColorSetSerializer(Class<Set<ProductColor>> t) {
		super(t);
	}

	@Override
	public void serialize(Set<ProductColor> productColors, JsonGenerator generator, SerializerProvider provider)
			throws IOException, JsonProcessingException {

		List<String> colorStrs = new ArrayList<>();
		for (ProductColor color : productColors) {
			colorStrs.add(color.getColor());
		}
		generator.writeObject(colorStrs);
	}
}