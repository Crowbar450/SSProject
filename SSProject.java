package ssproject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.System.exit;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class SSProject {

    public static void main(String[] args) throws IOException {

        try {

            File file1 = new File("users.txt");
            File file2 = new File("activty.txt");
            FileWriter fw1 = new FileWriter(file1, true);
            FileWriter fw2 = new FileWriter(file2, true);
            Scanner scan1=new Scanner(file1);
            Scanner scan2;
            String username;
            Scanner scanner = new Scanner(System.in);

            String choice1 = "";
            String choice2 = "";
            String choice3 = "";
            
            if(!scan1.hasNext()){
                setPassword("Setup admin password:",fw1);
                fw1.close();
            }
            do {
                System.out.println("1) log in\n2) sign up\n3) exit");
                choice1 = scanner.nextLine();
                switch (choice1) {
                    case "1":
                        scan1 = new Scanner(file1);
                        username = Login(scan1);

                        if (username.equals("m")) {
                            choice1 = "3";
                            break;
                        } else if (username.equals("admin")) {
                            System.out.println("you accessed admin user.");
                            do {

                                scan2 = new Scanner(file2);
                                System.out.println("1) show user activty\n2) exit");
                                choice2 = scanner.nextLine();

                                switch (choice2) {
                                    case "1":
                                        while (scan2.hasNext()) {
                                            System.out.println(scan2.nextLine());
                                        }
                                        choice2 = "2";
                                    case "2":
                                        choice1 = "3";
                                        break;
                                    default:
                                        System.out.println("Invalid choice.");

                                }
                            } while (!choice2.equals("2"));
                        } else {

                            do {
                                System.out.println("1) get a recommendation\n2) exit");
                                choice3 = scanner.nextLine();
                                switch (choice3) {

                                    case "1":
                                        fw2=new FileWriter(file2,true);
                                        int minSalary = getValidNumberInput("Enter minimum acceptable industry salary (SAR): ");
                                        double previousGPA = getValidDoubleInput("Enter previous GPA: ");
                                        String programmingInterest = getValidStringInput("Enter computer programming interest (Low, Medium, High, VeryHigh): ");

                                        int studyHours = (int) ((previousGPA - 3.0) * 7);
                                        String recommendation = getRecommendation(minSalary, previousGPA, programmingInterest, studyHours);
                                        fw2.append(username + "\n" + recommendation + "\n\n");
                                        fw2.close();

                                        System.out.println(recommendation);
                                        break;
                                    case "2":
                                        choice1="3";
                                        break;
                                    default:
                                        System.out.println("Invalid choice.");
                                }
                            } while (!choice3.equals("2"));
                        }
                        break;
                    case "2":
                        scan1 = new Scanner(file1);
                        fw1=new FileWriter(file1,true);
                        username = getValidUsername(scan1, fw1);
                        setPassword("Enter a password:", fw1);
                        fw1.close();
                        break;
                    case "3":
                        break;
                    default:
                }
            } while (!choice1.equals("3"));

        } catch (Exception e) {
            System.out.println("An error has occured while processing your data.");
            exit(0);
        }

    }

    static String getValidUsername(Scanner scan, FileWriter fw) throws IOException {
        String username;
        Scanner scanner = new Scanner(System.in);
        Scanner tempScan = scan;
        File file = new File("users.txt");
        do {
            scan = new Scanner(file);
            scan.nextLine();
            System.out.print("Enter your username:");
            username = scanner.nextLine();
            while (scan.hasNext()) {

                String temp = scan.nextLine();
                scan.nextLine();

                if (temp.equalsIgnoreCase(username)) {
                    username = "m";
                    System.out.println("Entered username already exists.");
                    break;
                }

            }
        } while (username.equals("m"));
        fw.append(username + "\n");
        return username;
    }

    static void setPassword(String message, FileWriter fw) throws NoSuchAlgorithmException, IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print(message);

        String password = scanner.nextLine();
        while (password.length() < 8) {
            System.out.println("Password length should be 8 or more");
            System.out.print(message);
            password = scanner.nextLine();
        }
        String hash = createMD5Hash(password);
        fw.append(hash + "\n");
    }

    static String Login(Scanner scan) throws NoSuchAlgorithmException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your username:");
        String username = scanner.nextLine();
        System.out.print("Enter your password:");
        String password = scanner.nextLine();
        String hash = createMD5Hash(password);

        if (username.equalsIgnoreCase("admin")) {
            if(hash.equals(scan.nextLine()))
            return "admin";
        }else{

        scan.nextLine();
        while (scan.hasNext()) {
            String tempUsername = scan.nextLine();

            String tempPassword = scan.nextLine();

            if (username.equalsIgnoreCase(tempUsername) && hash.equals(tempPassword)) {
                return username;
            }
        }
        
    }System.out.println("The username or password is incorrect.");
        return "m";
        }

    static int getValidNumberInput(String message) {
        Scanner scanner = new Scanner(System.in);
        int number;
        do {
            System.out.print(message);
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid positive number.");
                System.out.print(message);
                scanner.next();
            }
            number = scanner.nextInt();
        } while (number <= 0);
        return number;
    }

    static double getValidDoubleInput(String message) {
        Scanner scanner = new Scanner(System.in);
        double number;
        do {
            System.out.print(message);
            while (!scanner.hasNextDouble()) {
                System.out.println("Invalid input. Please enter a valid number.");
                System.out.print(message);
                scanner.next();
            }
            number = scanner.nextDouble();
            if (number < 0 || number > 5) {
                System.out.println("GPA is out of range (0-5)");
            }
        } while (number < 0 || number > 5);
        return number;
    }

    static String getValidStringInput(String message) {
        Scanner scanner = new Scanner(System.in);
        String input;
        System.out.print(message);
        do {
            input = scanner.next();
            if (!input.matches("[a-zA-Z]+")) {
                System.out.print("Invalid input. Please enter a valid string:");
                continue;
            }
            if (!input.equalsIgnoreCase("low") && !input.equalsIgnoreCase("medium") && !input.equalsIgnoreCase("high") && !input.equalsIgnoreCase("veryhigh")) {
                System.out.print("Please enter one of the given choices (Low, Medium, High, VeryHigh):");

            }
        } while (!input.equalsIgnoreCase("low") && !input.equalsIgnoreCase("medium") && !input.equalsIgnoreCase("high") && !input.equalsIgnoreCase("veryhigh"));
        return input;
    }

    static void displayProgram(String program, int studyHours, double requiredGPA) {
        System.out.println("Recommended Program: " + program);
        System.out.println("Daily suggested study hours: " + studyHours);
        System.out.println("Minimum acceptable GPA after degree completion: " + requiredGPA);
    }

    static String getRecommendation(int minSalary, double previousGPA, String programmingInterest, int studyHours) {
        if (minSalary >= 7000 && previousGPA >= 3.5 && programmingInterest.equalsIgnoreCase("VeryHigh")) {
            return "Recommended Program: Cybersecurity (CY)\nCategory: CY\nDaily suggested study hours: " + studyHours
                    + "\nMinimum acceptable GPA after degree completion: 4.0";
        } else if (minSalary >= 6000 && previousGPA >= 3.5 && programmingInterest.equalsIgnoreCase("Medium")) {
            return "Recommended Program: Data Science (DS)\nCategory: DS\nDaily suggested study hours: " + studyHours
                    + "\nMinimum acceptable GPA after degree completion: 3.5";
        } else if (minSalary >= 5000 && previousGPA >= 3.0) {
            if (programmingInterest.equalsIgnoreCase("Low")) {
                return "Recommended Program: Software Engineering (SE)\nCategory: SE\nDaily suggested study hours: " + studyHours
                        + "\nMinimum acceptable GPA after degree completion: 3.5";
            } else if (programmingInterest.equalsIgnoreCase("High")) {
                return "Recommended Program: Software Science and AI (CSAI)\nCategory: CSAI\nDaily suggested study hours: " + studyHours
                        + "\nMinimum acceptable GPA after degree completion: 3.5";
            } else if (programmingInterest.equalsIgnoreCase("Medium")) {
                return "Recommended Program: Computer and Network Engineering (CNE)\nCategory: CNE\nDaily suggested study hours: " + studyHours
                        + "\nMinimum acceptable GPA after degree completion: 3.5";
            }
        }
        return "No suitable program found based on the given criteria.";
    }

    public static String createMD5Hash(final String input)
            throws NoSuchAlgorithmException {

        String hashtext = null;
        MessageDigest md = MessageDigest.getInstance("MD5");

        // Compute message digest of the input
        byte[] messageDigest = md.digest(input.getBytes());

        hashtext = convertToHex(messageDigest);

        return hashtext;
    }

    static String convertToHex(final byte[] messageDigest) {
        BigInteger bigint = new BigInteger(1, messageDigest);
        String hexText = bigint.toString(16);
        while (hexText.length() < 32) {
            hexText = "0".concat(hexText);
        }
        return hexText;
    }
}
