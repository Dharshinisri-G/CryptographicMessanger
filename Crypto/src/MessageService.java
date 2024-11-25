import java.sql.*;
import java.util.Base64;
import java.util.Scanner;

public class MessageService {
    private static final Scanner scanner = new Scanner(System.in);

    // Send a message (either plain or encrypted)
    public static void sendMessage() {
        System.out.print("Enter recipient username: ");
        String recipient = scanner.nextLine();
        System.out.print("Enter message (type 'exit' to cancel): ");
        String message = scanner.nextLine();

        // Handle cancellation
        if ("exit".equalsIgnoreCase(message)) {
            System.out.println("Message sending canceled.");
            return;
        }

        System.out.print("Do you want to encrypt the message? (y/n): ");
        boolean encrypt = scanner.nextLine().equalsIgnoreCase("y");

        if (encrypt) {
            try {
                System.out.print("Enter encryption key (16 chars): ");
                String encryptionKey = scanner.nextLine();
                String encryptedMessage = CryptoUtil.encrypt(message, encryptionKey);
                saveMessage(recipient, message, encryptedMessage, true);
                System.out.println("Message encrypted and sent!");
            } catch (Exception e) {
                System.out.println("Error during encryption: " + e.getMessage());
            }
        } else {
            saveMessage(recipient, message, null, false);
            System.out.println("Message sent!");
        }
    }

    // Save the message in the database
    private static void saveMessage(String recipient, String plainMessage, String encryptedMessage, boolean encrypted) {
        String sql = "INSERT INTO messages (sender, recipient, message, encrypted) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, getCurrentUsername()); // Assume a function to get current logged-in username
            stmt.setString(2, recipient);
            stmt.setString(3, encrypted ? encryptedMessage : plainMessage);
            stmt.setBoolean(4, encrypted);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error saving message: " + e.getMessage());
        }
    }

    // View sent messages (both plain and encrypted)
    public static void viewSentMessages() {
        String sql = "SELECT * FROM messages WHERE sender = ? ORDER BY timestamp DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, getCurrentUsername()); // Get current logged-in username
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String recipient = rs.getString("recipient");
                String message = rs.getString("message");
                boolean encrypted = rs.getBoolean("encrypted");
                Timestamp timestamp = rs.getTimestamp("timestamp");

                System.out.println("Message ID: " + id);
                System.out.println("Recipient: " + recipient);
                System.out.println("Message: " + (encrypted ? "Encrypted" : message));
                System.out.println("Timestamp: " + timestamp);
                System.out.println("------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Error viewing sent messages: " + e.getMessage());
        }
    }

    // View received messages (decryption available for encrypted messages)
    public static void viewReceivedMessages() {
        String sql = "SELECT * FROM messages WHERE recipient = ? ORDER BY timestamp DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, getCurrentUsername()); // Get current logged-in username
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String sender = rs.getString("sender");
                String message = rs.getString("message");
                boolean encrypted = rs.getBoolean("encrypted");
                Timestamp timestamp = rs.getTimestamp("timestamp");

                System.out.println("Message ID: " + id);
                System.out.println("Sender: " + sender);
                System.out.println("Message: " + (encrypted ? "Encrypted" : message));
                if (encrypted) {
                    System.out.print("Do you want to decrypt this message? (y/n): ");
                    String decryptChoice = scanner.nextLine();
                    if (decryptChoice.equalsIgnoreCase("y")) {
                        System.out.print("Enter decryption key: ");
                        String decryptionKey = scanner.nextLine();
                        try {
                            String decryptedMessage = CryptoUtil.decrypt(message, decryptionKey);
                            System.out.println("Decrypted Message: " + decryptedMessage);
                        } catch (Exception e) {
                            System.out.println("Error during decryption: " + e.getMessage());
                        }
                    }
                }
                System.out.println("Timestamp: " + timestamp);
                System.out.println("------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Error viewing received messages: " + e.getMessage());
        }
    }

    // Delete a message by ID
    public static void deleteMessage() {
        System.out.print("Enter message ID to delete: ");
        int messageId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String sql = "DELETE FROM messages WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, messageId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Message deleted successfully.");
            } else {
                System.out.println("No message found with ID: " + messageId);
            }
        } catch (SQLException e) {
            System.out.println("Error deleting message: " + e.getMessage());
        }
    }

    // Helper method to get the current logged-in username (this can be adjusted based on your implementation)
    private static String getCurrentUsername() {
        // Assume this function returns the username of the currently logged-in user
        // You can store the logged-in user in a global variable or session
        return "current_user"; // Replace with the actual username
    }
}