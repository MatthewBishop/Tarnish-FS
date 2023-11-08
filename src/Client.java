import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        String serverHost = "localhost";
        int serverPort = 12345;

        try (Socket socket = new Socket(serverHost, serverPort);
             DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
             DataInputStream inputStream = new DataInputStream(socket.getInputStream())) {

            for (int i = 0; i < 5; i++) {
                // Send a byte to the server
                byte dataToSend = (byte) (i * 10);
                outputStream.writeByte(dataToSend);
                System.out.println("Sent byte to the server: " + dataToSend);

                // Receive the size of the byte array from the server
                int byteArraySize = inputStream.readInt();
                System.out.println("Received byte array size from the server: " + byteArraySize);

                // Receive the byte array
                byte[] receivedBytes = new byte[byteArraySize];
                int bytesRead = inputStream.read(receivedBytes, 0, byteArraySize);
                if (bytesRead == byteArraySize) {
                    System.out.println("Received byte array from the server: " + new String(receivedBytes));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}