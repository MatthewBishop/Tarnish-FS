import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.*;

public class NioServer {

    public static void main(String[] args) {
    	// Change this to your desired port
        int port = 43595; 
        
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.bind(new InetSocketAddress(port));
            System.out.println("Server is listening on port " + port);

            while (true) {
                SocketChannel clientChannel = serverSocketChannel.accept();
                handleClient(clientChannel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(SocketChannel clientChannel) {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1);
            int bytesRead = clientChannel.read(buffer);

            if (bytesRead == 1) {
                buffer.flip();
                int requestType = buffer.get();

                if (requestType == 0) {
                    // Handle Type 0 request
                    // Read data from the client (checksum/crc)
                    // Generate and send response
                    sendType0Response(clientChannel);
                } else if (requestType == 1) {
                    // Handle Type 1 request
                    // Read index/file from the client
                    buffer.clear();
                    int bytesReadIndex = clientChannel.read(buffer);
                    if (bytesReadIndex == 3) {
                        int index = readBigEndianMedium(buffer);
                        // Retrieve file data based on the index (handle your file logic here)
                        byte[] fileData = getFileData(index);
                        sendType1Response(clientChannel, index, fileData);
                    }
                }
            }

            clientChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendType0Response(SocketChannel clientChannel) throws IOException {
        // Prepare your Type 0 response
        // 1. Calculate checksum/crc values
        // 2. Create the response data in the specified format
        // 3. Gzip the response data

        // Example:
        byte[] responseBytes = generateType0Response();
        ByteBuffer buffer = ByteBuffer.wrap(responseBytes);
        clientChannel.write(buffer);
    }

    private static void sendType1Response(SocketChannel clientChannel, int index, byte[] fileData) throws IOException {
        // Prepare your Type 1 response
        // 1. Determine the size of the data
        // 2. Create the response data in the specified format
        // 3. Send the response to the client

        // Example:
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putShort((short) index);
        clientChannel.write(buffer);
        buffer.clear();

        buffer.putInt(fileData.length);
        clientChannel.write(buffer);

        buffer.clear();
        buffer.put(fileData);
        buffer.flip();
        clientChannel.write(buffer);
    }

    private static int readBigEndianMedium(ByteBuffer buffer) {
        return ((buffer.getShort() & 0xFFFF) << 8) | (buffer.get() & 0xFF);
    }

    // Replace this with your logic to fetch the file data
    private static byte[] getFileData(int index) {
        // You should read the file data associated with the given index
        // and return it as a byte array.
        // Example:
        String fileContent = "This is the content of file with index " + index;
        return fileContent.getBytes(StandardCharsets.UTF_8);
    }

    // Replace this with your logic to generate Type 0 response
    private static byte[] generateType0Response() {
        // Generate the Type 0 response data as specified
        // For example, you can create a byte array with checksum and gzipped data
        // and return it here.
        // Example:
        String response = "Type 0 Response Data";
        byte[] gzippedData = gzipCompress(response.getBytes(StandardCharsets.UTF_8));
        ByteBuffer buffer = ByteBuffer.allocate(4 + gzippedData.length);
        buffer.putInt(6); // Number of indexes
        buffer.put(gzippedData);
        return buffer.array();
    }

    private static byte[] gzipCompress(byte[] data) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(bos);
            gzip.write(data);
            gzip.close();
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
}
