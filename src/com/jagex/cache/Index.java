package com.jagex.cache;

import java.io.IOException;
import java.io.RandomAccessFile;

public final class Index {

	private static byte[] buffer = new byte[520];
	private RandomAccessFile data;
	private int file;
	private RandomAccessFile index;

	public Index(RandomAccessFile index, RandomAccessFile data, int file, int maximumFileSize) {
		this.file = file;
		this.data = data;
		this.index = index;
	}

	public synchronized byte[] decompress(int indexFile) {
		try {
			seek(index, indexFile * 6);
			for (int in = 0, read = 0; read < 6; read += in) {
				in = index.read(buffer, read, 6 - read);
				if (in == -1) {
					return null;
				}
			}

			int size = ((buffer[0] & 0xff) << 16) + ((buffer[1] & 0xff) << 8) + (buffer[2] & 0xff);
			int sector = ((buffer[3] & 0xff) << 16) + ((buffer[4] & 0xff) << 8) + (buffer[5] & 0xff);

//			if (size < 0 || size > maximumSize) {
//				return null;
//			} else 
			if (sector <= 0 || sector > data.length() / 520L) {
				return null;
			}

			byte[] decompressed = new byte[size];
			int totalRead = 0;

			for (int part = 0; totalRead < size; part++) {
				if (sector == 0) {
					return null;
				}

				seek(data, sector * 520);
				int unread = Math.min(size - totalRead, 512);

				for (int in = 0, read = 0; read < unread + 8; read += in) {
					in = data.read(buffer, read, unread + 8 - read);
					if (in == -1) {
						return null;
					}
				}

				int currentIndex = ((buffer[0] & 0xff) << 8) + (buffer[1] & 0xff);
				int currentPart = ((buffer[2] & 0xff) << 8) + (buffer[3] & 0xff);
				int nextSector = ((buffer[4] & 0xff) << 16) + ((buffer[5] & 0xff) << 8) + (buffer[6] & 0xff);
				int currentFile = buffer[7] & 0xff;

				if (currentIndex != indexFile || currentPart != part || currentFile != file) {
					return null;
				} else if (nextSector < 0 || nextSector > data.length() / 520L) {
					return null;
				}

				for (int i = 0; i < unread; i++) {
					decompressed[totalRead++] = buffer[i + 8];
				}

				sector = nextSector;
			}

			return decompressed;
		} catch (IOException ex) {
			return null;
		}
	}

	public synchronized void seek(RandomAccessFile file, int position) throws IOException {
		file.seek(position);
	}
}