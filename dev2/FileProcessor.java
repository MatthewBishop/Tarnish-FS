import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class FileProcessor {

    public static void main(String[] args) {
        Set<Integer> modelIdsSet = new HashSet<>();

        // Replace "path/to/your/directory" with the actual path to your directory
        File directory = new File("K:\\dump\\types\\New folder\\");

        // Loop through all files in the directory
        for (File file : directory.listFiles()) {
            if (file.isFile()) {
                processFile(file, modelIdsSet);
            }
        }

        // Print the set of model ids
        System.out.println("Model IDs Set: " + modelIdsSet);
    }

    private static void processFile(File file, Set<Integer> modelIdsSet) {
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("male equip model id 1") ||
                    line.startsWith("male equip model id 2") ||
                    line.startsWith("male equip model id 3") ||
                    line.startsWith("female equip model id 1") ||
                    line.startsWith("female equip model id 2") ||
                    line.startsWith("female equip model id 3") ||
                    line.startsWith("inv model id")) {
                    // Extract the model id and add it to the set
                    String[] parts = line.split(" ");
                    int modelId = Integer.parseInt(parts[parts.length - 1]);
                    modelIdsSet.add(modelId);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}