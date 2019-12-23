package com.github.ennoxhd.aig;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

import javax.imageio.ImageIO;

final class FileUtils {
	
	static Optional<BufferedImage> getImageFromFile(File imageFile) {
		if(imageFile == null) Optional.empty();
		try {
			return Optional.ofNullable(ImageIO.read(imageFile));
		} catch (IOException e) {
			return Optional.empty();
		}
	}
	
	static Optional<File> toTxtFile(File file) {
		if(file == null) Optional.empty();
		if(!file.isFile()) Optional.empty();
		final String txtExtension = ".txt";
		String newFile = null;
		int cutIdx = -1;
		if((cutIdx = file.getName().lastIndexOf(".")) <= 0) {
			newFile = file.getPath() + txtExtension;
		} else {
			String parent = file.getParent();
			if(parent == null) parent = "";
			newFile = parent + File.separator
					+ file.getName().substring(0, cutIdx) + txtExtension;
		}
		return Optional.of(new File(newFile));
	}
	
	private static Optional<String> createFileName(File file, int n) {
		if(n < 0) return Optional.empty();
		if(file == null) {
			if(n == 0) return Optional.empty();
			return Optional.of(String.valueOf(n));
		}
		if(n == 0) return Optional.of(file.getPath());
		String newFile = null;
		int cutIdx = -1;
		if((cutIdx = file.getName().lastIndexOf(".")) <= 0) {
			newFile = file.getPath() + n;
		} else {
			String parent = file.getParent();
			if(parent == null) parent = "";
			newFile = parent + File.separator
					+ file.getName().substring(0, cutIdx) + n
					+ file.getName().substring(cutIdx, file.getName().length());
		}
		return Optional.of(newFile);
	}
	
	static Optional<File> nextFile(File file) {
		if(file == null) Optional.empty();
		if(!file.exists()) Optional.of(file);
		for(int existingFileCount = 0; existingFileCount >= 0; existingFileCount++) {
			Optional<String> optFileName = createFileName(file, existingFileCount);
			if(optFileName.isEmpty()) return Optional.empty();
			File nextFile = new File(optFileName.get());
			if(!nextFile.exists()) return Optional.of(nextFile);
		}
		return Optional.empty();
	}
	
	static boolean writeToFile(String[] lines, File file) {
		if(lines == null || file == null) return false;
		try {
			FileWriter fileWriter = new FileWriter(file);
			BufferedWriter writer = new BufferedWriter(fileWriter);
			for(String line : lines) {
				writer.append(line);
				writer.newLine();
			}
			writer.close();
			fileWriter.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}
}