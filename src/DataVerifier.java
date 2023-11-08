import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class DataVerifier {

	public static void main(String[] args) {
		byte[] receivedBytes = FileOperations.ReadFile("./data2/data/0");
        System.out.println("Decompressing: " + receivedBytes.length);
        byte[] decompressedBytes = GZIPDecompressor.getInstance().decompress(receivedBytes);
//        for (byte b : decompressedBytes) {
//            System.out.print(b + " ");
//        }
        System.out.println("Decompressed size: " + decompressedBytes.length);
        ByteBuf data = Unpooled.wrappedBuffer(decompressedBytes);
        System.out.println("remaining: " + data.readableBytes());

       // Int2IntOpenHashMap int2IntOpenHashMap = new Int2IntOpenHashMap();
        int indexCount = data.readUnsignedByte();
        System.out.println("indexCount" + indexCount);
        for (int indexId = 0; indexId < indexCount; indexId++) {
          int archiveCount = data.readUnsignedMedium();
          System.out.println("archiveCount" + archiveCount);
          for (int archiveId = 0; archiveId < archiveCount; archiveId++) {
            int crc32 = data.readInt();

            if (crc32 != 0) {
                System.out.println(indexId + " - "+ archiveId + " - "+ crc32);
          //    int filePair = FilePair.create(indexId, archiveId);
          //    int2IntOpenHashMap.put(filePair, crc32);
            } else {
            //    System.out.println(indexId + " - "+ archiveId + " - "+ crc32);

            }
            
          } 
        } 
        System.out.println("remaining: " + data.readableBytes());
	}

}
