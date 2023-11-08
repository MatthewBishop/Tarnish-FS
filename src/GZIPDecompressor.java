

import java.io.*;
import java.io.IOException;
import java.util.function.Supplier;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;

public final class GZIPDecompressor {
  private final Inflater inflater = new Inflater(true);
  
  private final byte[] buffer;
  
  public GZIPDecompressor(byte[] buffer) {
    this.buffer = buffer;
  }
  
  public GZIPDecompressor() {
    this(new byte[999999]);
  }
  
  public byte[] decompress(byte[] input) {
    int outputLength = decompress(input, this.buffer);
    byte[] output = new byte[outputLength];
    System.arraycopy(this.buffer, 0, output, 0, outputLength);
    return output;
  }
  
  public int decompress(byte[] input, byte[] output) {
    int offset = calcOffset(input);
    int uncompressedBytes = -1;
    try {
      this.inflater.setInput(input, offset, input.length - offset - 8);
      uncompressedBytes = this.inflater.inflate(output);
    } catch (Exception exception) {
      this.inflater.reset();
      throw new RuntimeException("Invalid GZIP compressed data!");
    } 
    this.inflater.reset();
    return uncompressedBytes;
  }
  
  public int calcOffset(byte[] input) {
    if (input[0] != 31 || input[1] != -117)
      throw new RuntimeException("invalid gzip header"); 
    int flags = input[3];
    int offset = 10;
    if ((flags & 0x2) != 0)
      offset += 2; 
    if ((flags & 0x4) != 0)
      offset += 2; 
    if ((flags & 0x8) != 0)
      while (input[offset++] != 0); 
    if ((flags & 0x10) != 0)
      while (input[offset++] != 0); 
    if ((flags & 0x20) != 0)
      offset += 12; 
    return offset;
  }
  
  private static final ThreadLocal<GZIPDecompressor> threadLocal = ThreadLocal.withInitial(GZIPDecompressor::new);
  
  public static GZIPDecompressor getInstance() {
    return threadLocal.get();
  }
  
	/**
	 * Uncompresses a GZIP file.
	 * 
	 * @param bytes
	 *            The compressed bytes.
	 * @return The uncompressed bytes.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public static byte[] gunzip(byte[] bytes) throws IOException {
		/* create the streams */
		InputStream is = new GZIPInputStream(new ByteArrayInputStream(bytes));
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			try {
				/* copy data between the streams */
				byte[] buf = new byte[4096];
				int len = 0;
				while ((len = is.read(buf, 0, buf.length)) != -1) {
					os.write(buf, 0, len);
				}
			} finally {
				os.close();
			}

			/* return the uncompressed bytes */
			return os.toByteArray();
		} finally {
			is.close();
		}
	}
}
