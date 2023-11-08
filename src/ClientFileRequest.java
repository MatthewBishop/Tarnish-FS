import java.io.*;
import java.net.*;

public class ClientFileRequest {
	
	/*
Sent request to the server... 32
Received medium value from the server: 32
Received byte array size from the server: 22987
	 */
    public static void main(String[] args) {
        String serverHost = "51.81.49.39";
        int serverPort = 43595;

        try (Socket socket = new Socket(serverHost, serverPort);
             DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
             DataInputStream inputStream = new DataInputStream(socket.getInputStream())) {

            //for (int i = 0; i < 5; i++) 
            {

            	//opcode 1 for file request
            	outputStream.writeByte(1);
            	
            	int file = create(0,1);
            	//request file 1 from index 0.
                writeMedium(outputStream, file);
                
                System.out.println("Sent request to the server... " +file);

                // Receive a response from the server
                //byte receivedByte = inputStream.readByte();
                boolean dataAvailable = false;
                while (!dataAvailable) {
                // Check if 7 bytes are available
                if (inputStream.available() >= 7) {
                    dataAvailable = true;

                    
                    // Read the first 3 bytes as an unsigned medium
                    int mediumValue = ((inputStream.readByte() & 0xFF) << 16) |
                                     ((inputStream.readByte() & 0xFF) << 8) |
                                     (inputStream.readByte() & 0xFF);
                    System.out.println("Received medium value from the server: " + mediumValue);

                    // Read the next 4 bytes as the array size
                    int byteArraySize = inputStream.readInt();
                    System.out.println("Received byte array size from the server: " + byteArraySize);

                    // Receive the byte array
                    byte[] receivedBytes = new byte[byteArraySize];
                    int totalBytesRead = 0;
                    while (totalBytesRead < byteArraySize) {
                        int bytesRead = inputStream.read(receivedBytes, totalBytesRead, byteArraySize - totalBytesRead);
                        if (bytesRead == -1) {
                            // Handle the case when the server disconnects prematurely
                            System.out.println("Server disconnected prematurely.");
                            break;
                        }
                        totalBytesRead += bytesRead;
                    }

                    if (totalBytesRead == byteArraySize) {
                        System.out.println("Received byte array from the server: " + new String(receivedBytes));
                    }
                }
                }

//                // Receive the size of the byte array from the server
//                int byteArraySize = inputStream.readInt();
//                System.out.println("Received byte array size from the server: " + byteArraySize);
//
//                // Receive the byte array
//                byte[] receivedBytes = new byte[byteArraySize];
//                int bytesRead = inputStream.read(receivedBytes, 0, byteArraySize);
//                if (bytesRead == byteArraySize) {
//                    System.out.println("Received byte array from the server: " + new String(receivedBytes));
//                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static int create(int index, int file) {
        if ((index & 0xFFFFFFE0) != 0)
          throw new IllegalArgumentException("invalid index " + index + ":" + file); 
        if ((file & 0xFFF80000) != 0)
          throw new IllegalArgumentException("invalid file " + index + ":" + file); 
        return index & 0x1F | (file & 0x7FFFF) << 5;
      }
    
    public static void writeMedium(DataOutputStream dat, int i) throws IOException {
        dat.write((byte) (i >> 16));
        dat.write((byte) (i >> 8));
        dat.write((byte) (i));
    }
}