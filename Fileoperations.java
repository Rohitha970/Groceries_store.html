import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * FileOperations.java
 * 
 * This program demonstrates basic file operations in Java including:
 * - Reading text files
 * - Writing to text files
 * - Modifying text files
 * - Creating new files
 * - Displaying file information
 * 
 * The program provides a simple menu-driven interface for users to perform these operations.
 */
public class FileOperations {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Java File Operations Program ===");
        System.out.println("This program demonstrates reading, writing, and modifying text files.");

        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getUserChoice();
            
            switch (choice) {
                case 1:
                    readFile();
                    break;
                case 2:
                    writeToFile();
                    break;
                case 3:
                    appendToFile();
                    break;
                case 4:
                    modifyFile();
                    break;
                case 5:
                    createNewFile();
                    break;
                case 6:
                    displayFileInfo();
                    break;
                case 7:
                    running = false;
                    System.out.println("Exiting program. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        
        scanner.close();
    }

    /**
     * Displays the main menu options to the user.
     */
    private static void displayMenu() {
        System.out.println("\n=== Main Menu ===");
        System.out.println("1. Read a text file");
        System.out.println("2. Write to a text file (overwrite)");
        System.out.println("3. Append to a text file");
        System.out.println("4. Modify content in a text file");
        System.out.println("5. Create a new text file");
        System.out.println("6. Display file information");
        System.out.println("7. Exit");
        System.out.print("Enter your choice (1-7): ");
    }

    /**
     * Gets and validates user input for menu selection.
     * @return The validated user choice (1-7)
     */
    private static int getUserChoice() {
        while (true) {
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= 7) {
                    return choice;
                }
                System.out.print("Please enter a number between 1 and 7: ");
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }

    /**
     * Prompts user for a file path and validates its existence.
     * @param mustExist If true, requires the file to exist
     * @return The validated file path
     */
    private static String getFilePath(boolean mustExist) {
        while (true) {
            System.out.print("Enter the file path: ");
            String filePath = scanner.nextLine().trim();
            
            if (filePath.isEmpty()) {
                System.out.println("File path cannot be empty. Please try again.");
                continue;
            }
            
            if (mustExist && !Files.exists(Paths.get(filePath))) {
                System.out.println("File does not exist. Please try again.");
            } else {
                return filePath;
            }
        }
    }

    /**
     * Reads and displays the content of a text file.
     */
    private static void readFile() {
        System.out.println("\n=== Read a Text File ===");
        String filePath = getFilePath(true);
        
        try {
            System.out.println("\nFile content:");
            System.out.println("-------------");
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (String line : lines) {
                System.out.println(line);
            }
            System.out.println("-------------");
            System.out.println("File read successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        }
    }

    /**
     * Writes content to a text file (overwrites existing content).
     */
    private static void writeToFile() {
        System.out.println("\n=== Write to a Text File (Overwrite) ===");
        String filePath = getFilePath(false);
        
        System.out.println("Enter the content to write (press Enter then Ctrl+D (Unix) or Ctrl+Z (Windows) to finish):");
        StringBuilder content = new StringBuilder();
        while (scanner.hasNextLine()) {
            content.append(scanner.nextLine()).append("\n");
        }
        scanner.nextLine(); // Clear the buffer
        
        try {
            Files.write(Paths.get(filePath), content.toString().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Content written to file successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    /**
     * Appends content to an existing text file.
     */
    private static void appendToFile() {
        System.out.println("\n=== Append to a Text File ===");
        String filePath = getFilePath(true);
        
        System.out.println("Enter the content to append (press Enter then Ctrl+D (Unix) or Ctrl+Z (Windows) to finish):");
        StringBuilder content = new StringBuilder();
        while (scanner.hasNextLine()) {
            content.append(scanner.nextLine()).append("\n");
        }
        scanner.nextLine(); // Clear the buffer
        
        try {
            Files.write(Paths.get(filePath), content.toString().getBytes(), StandardOpenOption.APPEND);
            System.out.println("Content appended to file successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while appending to the file: " + e.getMessage());
        }
    }

    /**
     * Modifies specific content in a text file by replacing text.
     */
    private static void modifyFile() {
        System.out.println("\n=== Modify a Text File ===");
        String filePath = getFilePath(true);
        
        try {
            // Read current content
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            System.out.println("\nCurrent file content:");
            System.out.println("---------------------");
            System.out.println(content);
            System.out.println("---------------------");
            
            // Get search and replace strings
            System.out.print("Enter the text to replace: ");
            String searchText = scanner.nextLine();
            System.out.print("Enter the replacement text: ");
            String replaceText = scanner.nextLine();
            
            // Perform replacement
            String modifiedContent = content.replace(searchText, replaceText);
            
            if (modifiedContent.equals(content)) {
                System.out.println("No changes made. The search text was not found.");
                return;
            }
            
            // Write modified content back to file
            Files.write(Paths.get(filePath), modifiedContent.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("File modified successfully.");
            
        } catch (IOException e) {
            System.out.println("An error occurred while modifying the file: " + e.getMessage());
        }
    }

    /**
     * Creates a new empty text file.
     */
    private static void createNewFile() {
        System.out.println("\n=== Create a New Text File ===");
        String filePath = getFilePath(false);
        
        try {
            Files.createFile(Paths.get(filePath));
            System.out.println("New file created successfully at: " + filePath);
        } catch (IOException e) {
            System.out.println("An error occurred while creating the file: " + e.getMessage());
        }
    }

    /**
     * Displays information about a file.
     */
    private static void displayFileInfo() {
        System.out.println("\n=== Display File Information ===");
        String filePath = getFilePath(true);
        
        try {
            Path path = Paths.get(filePath);
            BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
            
            System.out.println("\nFile Information:");
            System.out.println("-----------------");
            System.out.println("Path: " + path.toAbsolutePath());
            System.out.println("Size: " + attrs.size() + " bytes");
            System.out.println("Created: " + attrs.creationTime());
            System.out.println("Last Modified: " + attrs.lastModifiedTime());
            System.out.println("Is Directory: " + attrs.isDirectory());
            System.out.println("Is Regular File: " + attrs.isRegularFile());
            System.out.println("-----------------");
        } catch (IOException e) {
            System.out.println("An error occurred while reading file information: " + e.getMessage());
        }
    }
}