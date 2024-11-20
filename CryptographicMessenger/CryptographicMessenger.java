import java.util.Scanner;

public class CryptographicMessenger {
    private static final int SHIFT = 3; 
    public static String encrypt(String message) {
        StringBuilder encrypted = new StringBuilder();
        for (char c : message.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                encrypted.append((char) ((c - base + SHIFT) % 26 + base));
            } else {
                encrypted.append(c); 
            }
        }
        return encrypted.toString();
    }
    public static String decrypt(String message) {
        StringBuilder decrypted = new StringBuilder();
        for (char c : message.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                decrypted.append((char) ((c - base - SHIFT + 26) % 26 + base));
            } else {
                decrypted.append(c); 
            }
        }
        return decrypted.toString();
    }

    public static void main(String[] args) {
        Scanner x = new Scanner(System.in);
        boolean continueMessaging = true; 

        while (continueMessaging) {
            System.out.println("Welcome to Cryptographic Messenger!");
            System.out.print("Enter your message (plain text or encrypted): ");
            String input = x.nextLine();

            System.out.print("Is the input (1) Plain Text or (2) Encrypted? Enter 1 or 2: ");
            int choice = x.nextInt();
            x.nextLine(); 

            String output;
            if (choice == 1) {
                output = encrypt(input);
                System.out.println("Encrypted Message: " + output);
            } else if (choice == 2) {
                output = decrypt(input);
                System.out.println("Decrypted Message: " + output);
            } else {
                System.out.println("Invalid choice. Exiting.");
                break; 
            }
            System.out.print("Do you want to send one more message? (yes/no): ");
            String response = x.nextLine();
            if (response.equalsIgnoreCase("no")) {
                continueMessaging = false; 
            }
        }

        System.out.println("Thank you for using Cryptographic Messenger!");
        x.close();
    }
}
