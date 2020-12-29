package com.janaldous.minanamanila;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

public class ImageToByteData {

	public static byte[] extractBytes(String imageName) throws IOException {
		System.out.println(imageName);
		// open image
		File imgPath = new File(imageName);
		BufferedImage bufferedImage = ImageIO.read(imgPath);

		// get DataBufferBytes from Raster
		WritableRaster raster = bufferedImage.getRaster();
		DataBufferByte data = (DataBufferByte) raster.getDataBuffer();

		return (data.getData());
	}

	@Test
	public void test() throws IOException {
		String home = System.getProperty("user.home");
		
		File file = new File(home + "/Documents/code/" + "macbook-air-space-gray-select-201810.jfif");
		byte[] result = FileUtils.readFileToByteArray(file);
		
		System.out.println(result.length);
		assertTrue(result.length > 0);
		System.out.println(Arrays.toString(Arrays.copyOfRange(result, 111100, 111110)));
	}

}
