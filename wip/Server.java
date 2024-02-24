import java.io.*;
import java.net.*;
import java.util.zip.*;
import java.nio.ByteBuffer;

public class Server {

    public static void main(String[] args) {
    	// Change this to your desired port
        int port = 43595; 

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                handleClient(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try {
            InputStream in = clientSocket.getInputStream();
            OutputStream out = clientSocket.getOutputStream();

            int requestType = in.read();

            if (requestType == 0) {
                // Handle Type 0 request
                // Read data from the client (checksum/crc)
                // Generate and send response
                sendType0Response(out);
            } else if (requestType == 1) {
                // Handle Type 1 request
                // Read index/file from the client
                int index = readBigEndianMedium(in);
                // Retrieve file data based on the index (handle your file logic here)
                byte[] fileData = getFileData(index);
                sendType1Response(out, index, fileData);
            }

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendType0Response(OutputStream out) throws IOException {
        // Prepare your Type 0 response
        // 1. Calculate checksum/crc values
        // 2. Create the response data in the specified format
        // 3. Gzip the response data

        // Example:
        byte[] responseBytes = generateType0Response();
        out.write(responseBytes);
        out.flush();
    }

    private static void sendType1Response(OutputStream out, int index, byte[] fileData) throws IOException {
        // Prepare your Type 1 response
        // 1. Determine the size of the data
        // 2. Create the response data in the specified format
        // 3. Send the response to the client

        // Example:
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putShort((short) index);
        out.write(buffer.array());
        buffer.clear();

        buffer.putInt(fileData.length);
        out.write(buffer.array());

        out.write(fileData);
        out.flush();
    }

    private static int readBigEndianMedium(InputStream in) throws IOException {
        byte[] buffer = new byte[3];
        in.read(buffer);
        ByteBuffer wrapped = ByteBuffer.wrap(buffer);
        return wrapped.getShort() & 0xFFFF; // Ensure it's an unsigned 16-bit value
    }

    // Replace this with your logic to fetch the file data
    private static byte[] getFileData(int index) {
        // You should read the file data associated with the given index
        // and return it as a byte array.
        // Example:
        String fileContent = "This is the content of file with index " + index;
        return fileContent.getBytes();
    }

    // Replace this with your logic to generate Type 0 response
    private static byte[] generateType0Response() {
        // Generate the Type 0 response data as specified
        // For example, you can create a byte array with checksum and gzipped data
        // and return it here.
        // Example:
        String response = "Type 0 Response Data";
        byte[] gzippedData = gzipCompress(response.getBytes());
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
