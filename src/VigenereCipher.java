import java.io.*;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Paths;

public class VigenereCipher {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String operation = "";
        boolean programRunning = true; // Control the outer loop for the entire program

        while (programRunning) {

            // Ask the user for the operation (encrypt or decrypt) and check if their response is valid
            while (true) { // while loop is for continuous prompting
                System.out.print("\nDo you want to encrypt or decrypt? (enter 'E' or 'D'): ");
                operation = scanner.nextLine().toLowerCase();

                if ("e".equals(operation) || "d".equals(operation)) {
                    break;
                } else {
                    System.out.println("Invalid operation selected. Please enter 'E' for encryption or 'D' for decryption.\n");
                }
            }

            // Get file name and read file
            String fileContent = "";

            while (true) {
                System.out.print("Enter the file name: ");
                String fileName = scanner.nextLine();

                try {
                    fileContent = new String(Files.readAllBytes(Paths.get(fileName))).toLowerCase();
                    //System.out.println("(File content: " + fileContent +")"); // For testing design spec
                    break; // Exit the loop if the file is successfully read
                } catch (IOException e) {
                    System.err.println("File not found. Please enter an existing file name. Press 'enter' to continue.");
                    // Consume any extra newline to ensure proper prompting
                    if (scanner.hasNextLine()) {
                        scanner.nextLine();
                    }
                }
            }

            // Ask for the keyword
            String keyword;

            while (true) {
                System.out.print("Enter the keyword (single word): ");
                keyword = scanner.nextLine();

                if (keyword.matches("[a-zA-Z]+")) {
                    keyword = keyword.toLowerCase();
                    break;
                } else {
                    System.out.println("The keyword must be purely alphabetical. Please try again.\n");
                }
            }

            // Encrypt or decrypt the content in the appointed file
            String result = "";

            if (operation.startsWith("e")) {
                // Call the encrypt method
                result = encrypt(fileContent, keyword);
                // Print out the results
                System.out.println("\nWord to encrypt: " + fileContent);
                System.out.println("Keyword: " + keyword);
                System.out.println("Encrypted data: " + result);
            } else if (operation.startsWith("d")) {
                // Call the decrypt method
                result = decrypt(fileContent, keyword);
                // Print out the results
                System.out.println("\nWord to decrypt: " + fileContent);
                System.out.println("Keyword: " + keyword);
                System.out.println("Decrypted data: " + result);
            } else {
                System.out.println("Invalid operation selected. Please enter 'E' for encryption or 'D' for decryption.");
            }

            // Restart, quit or save data (1, 2, or 3)
            System.out.println("\nChoose an option:");
            System.out.println("1) Restart the program");
            System.out.println("2) Quit the program");
            System.out.println("3) Save the encrypted/decrypted data to a file");
            int userChoice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            if (userChoice == 1) {
                // continue - the loop will restart the program
            } else if (userChoice == 2) {
                programRunning = false; // set as false to terminate the program
                System.out.println("\nProgram terminated. Thank you for using the program, goodbye!");
            } else if (userChoice == 3) {
                System.out.print("\nEnter the name of the file to save the data: ");
                String fileName = scanner.nextLine();

                // Call the writeFile method!
                writeFile(fileName, keyword);
                System.out.println("Data saved to " + fileName);

                // Ask the reader if they want to continue the program (1 or 2)
                boolean continueAfterSave = true;
                while (continueAfterSave) {
                    System.out.println("\nWhat do you want to do next?");
                    System.out.println("1) Restart the program");
                    System.out.println("2) Quit the program");
                    int secondChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline

                    if (secondChoice == 1) {
                        continueAfterSave = false; // Exit this loop to restart the program
                    } else if (secondChoice == 2) {
                        programRunning = false; // Set as false to terminate the program
                        continueAfterSave = false; // Exit this loop to terminate the program
                        System.out.println("Program terminated. Thank you for using the program, goodbye!");
                    } else {
                        System.out.println("Invalid choice. Please enter 1 or 2.");
                    }
                }
            } else {
                System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
        scanner.close();
    }

    // writeFile method that saves the encrypted/decrypted data to a file
    private static void writeFile(String fileName, String result) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName));
        bufferedWriter.write(result);
        bufferedWriter.close();
    }

    // Encrypt method that encrypts data in the appointed file with the keyword
    private static String encrypt(String text, String keyword) {
        StringBuilder result = new StringBuilder();
        for (int i = 0, j = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isLetter(c)) {
                int encryptedCharIndex = (c - 'a' + keyword.charAt(j) - 'a') % 26;
                char encryptedChar = (char) (encryptedCharIndex + 'a');
                result.append(encryptedChar);
                j = ++j % keyword.length();
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    // Decrypt method that decrypts data in the appointed file with the keyword
    private static String decrypt(String text, String keyword) {
        StringBuilder result = new StringBuilder();
        for (int i = 0, j = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isLetter(c)) {
                int decryptedCharIndex = (c - keyword.charAt(j) + 26) % 26;
                char decryptedChar = (char) (decryptedCharIndex + 'a');
                result.append(decryptedChar);
                j = ++j % keyword.length();
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}