package com.jagex.cache;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

/**
 * A cache represents a collection of files used by Jagex in Runescape.
 * <p>
 * 
 * @author Advocatus
 *
 */
public class Cache {
		
	private Index[] indices = new Index[6];
	
	/**
	 * Loads a cache from a target directory.
	 * @param directory The directory.
	 * @throws FileNotFoundException Unable to access the directory.
	 */
	public Cache(String directory) throws FileNotFoundException {
		RandomAccessFile cache = new RandomAccessFile(directory  + "main_file_cache.dat", "rw");
		RandomAccessFile[] indexes = new RandomAccessFile[6];
		for (int index = 0; index < 6; index++) {
			indexes[index] = new RandomAccessFile(directory + "main_file_cache.idx" + index, "rw");
		}

		for (int index = 0; index < 6; index++) {
			indices[index] = new Index(indexes[index], cache, index + 1, 0xffffff);
		}
	}
	
	public byte[] getFile(int index, int file) {
		return this.indices[index].decompress(file);
	}
}