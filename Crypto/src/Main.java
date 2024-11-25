import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("Welcome to Cryptographic Messenger!");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    register();
                    break;
                case 2:
                    if (login()) {
                        loggedInMenu();
                    }
                    break;
                case 3:
                    System.out.println("Exiting... Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void register() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        if (UserService.registerUser(username, password)) {
            System.out.println("User registered successfully!");
        } else {
            System.out.println("Registration failed. Username may already exist.");
        }
    }

    private static boolean login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        if (UserService.loginUser(username, password)) {
            System.out.println("Login successful! Welcome " + username);
            return true;
        } else {
            System.out.println("Login failed. Check your credentials.");
            return false;
        }
    }

    private static void loggedInMenu() {
        while (true) {
            System.out.println("1. Send Message");
            System.out.println("2. View Sent Messages");
            System.out.println("3. View Received Messages");
            System.out.println("4. Delete Messages");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    MessageService.sendMessage();
                    break;
                case 2:
                    MessageService.viewSentMessages();
                    break;
                case 3:
                    MessageService.viewReceivedMessages();
                    break;
                case 4:
                    MessageService.deleteMessage();
                    break;
                case 5:
                    System.out.println("Logged out.");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
