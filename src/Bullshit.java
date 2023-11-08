import java.io.File;

public class Bullshit {

	static int start_id = 37005;
    public static void main(String[] args) {
        // Specify the directory path you want to list files from
        String directoryPath = "K:\\documents\\GitHub\\eternalscape-server4\\assets\\models\\eternalscape\\items\\";

        // Create a File object for the directory
        File directory = new File(directoryPath);

        // Check if the directory exists and is a directory
        if (directory.exists() && directory.isDirectory()) {
            // List all files in the directory
            File[] files = directory.listFiles();

            if (files != null) {
                // Loop through the files and print their names
                for (File file : files) {
                    if (file.isFile()) {
                 //       System.out.println("" + file.getName());
                        System.out.println("packModel("+ ++start_id+", java.nio.file.Files.readAllBytes(Paths.get(\"assets/models/eternalscape/items/" + file.getName() + "\")));");

                        //packModel(37000, java.nio.file.Files.readAllBytes(Paths.get("assets/models/eternalscape/Teleporter.dat")));

                    }
                }
            } else {
                System.out.println("No files found in the directory.");
            }
        } else {
            System.out.println("The specified directory does not exist or is not a directory.");
        }
    }
}


