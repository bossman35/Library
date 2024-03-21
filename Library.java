// Amaan Seraj
// 03/16/2024
// CS & 145
// Assignment 3: Creative
// This program generates a library catalogue

import java.util.*;
import java.text.*;

class Book {
    private String name;
    private boolean available;

    public Book(String name) {
        this.name = name;
        this.available = true;
    }

    public String getName() {
        return name;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}

public class Library {
    private Map<String, Date> borrowedBooks;
    private Map<String, Book> catalog;
    private Map<String, List<String>> reservations;
    private Map<String, Double> fines;

    public Library() {
        borrowedBooks = new HashMap<>();
        catalog = new HashMap<>();
        reservations = new HashMap<>();
        fines = new HashMap<>();
    }

    // Adds a book to the catalogue
    public void addBook(String bookName) {
        catalog.put(bookName, new Book(bookName));
        System.out.println("Book '" + bookName + "' added to the library catalog.");
    }

    // Allows the student to borrow a book
    public void borrowBook(String studentName, String bookName, Date dueDate) {
        if (!catalog.containsKey(bookName)) {
            System.out.println("Book '" + bookName + "' is not available in the library.");
            return;
        }

        Book book = catalog.get(bookName);
        if (!book.isAvailable()) {
            System.out.println("Book '" + bookName + "' is currently unavailable.");
            System.out.println("Do you want to reserve it? (Y/N): ");
            Scanner scanner = new Scanner(System.in);
            String reserveChoice = scanner.nextLine().toUpperCase();
            if (reserveChoice.equals("Y")) {
                reserveBook(studentName, bookName);
            }
            return;
        }

        borrowedBooks.put(bookName, dueDate);
        book.setAvailable(false);
        System.out.println(studentName + " has borrowed the book '" + bookName + "'.");
        System.out.println("Due date: " + dueDate);
    }

    // Does return of a book
    public void returnBook(String bookName) {
        if (!borrowedBooks.containsKey(bookName)) {
            System.out.println("Book '" + bookName + "' was not borrowed.");
            return;
        }
        Date dueDate = borrowedBooks.get(bookName);
        Date currentDate = new Date();
        if (currentDate.after(dueDate)) {
            double daysLate = (currentDate.getTime() - dueDate.getTime()) / (1000 * 60 * 60 * 24);
            double fineAmount = daysLate * 0.50; // Assuming fine rate of $0.50 per day
            fines.put(bookName, fineAmount);
            System.out.println("Book '" + bookName + "' returned late. Fine amount: $" + fineAmount);
        }

        borrowedBooks.remove(bookName);
        catalog.get(bookName).setAvailable(true);
        System.out.println("Book '" + bookName + "' has been returned.");
    }

    // Allows a student to put a book on hold
    public void reserveBook(String studentName, String bookName) {
        if (!reservations.containsKey(bookName)) {
            reservations.put(bookName, new ArrayList<>());
        }
        reservations.get(bookName).add(studentName);
        System.out.println(studentName + " has reserved the book '" + bookName + "'.");
    }

    // Shows available books
    public void displayBookAvailability() {
        System.out.println("Book Availability Status:");
        for (Map.Entry<String, Book> entry : catalog.entrySet()) {
            String bookName = entry.getKey();
            boolean available = entry.getValue().isAvailable();
            String status = available ? "Available" : "Not Available";
            System.out.println(bookName + ": " + status);
        }
    }

    // Tells you the fine amount
    public void displayFines() {
        System.out.println("Fines:");
        for (Map.Entry<String, Double> entry : fines.entrySet()) {
            String bookName = entry.getKey();
            double fineAmount = entry.getValue();
            System.out.println(bookName + ": $" + fineAmount);
        }
    }

    // Looks for a book in the catalogue
    public void searchBook(String bookName) {
        if (recursiveSearchBook(bookName, catalog.keySet().toArray(new String[0]), 0)) {
            System.out.println("Book '" + bookName + "' found in the library.");
        } else {
            System.out.println("Book '" + bookName + "' not found in the library.");
        }
    }

    // Helper method for search book method
    private boolean recursiveSearchBook(String bookName, String[] bookArray, int index) {
        if (index >= bookArray.length) {
            return false;
        }

        if (bookArray[index].equals(bookName)) {
            return true;
        }

        return recursiveSearchBook(bookName, bookArray, index + 1);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Library library = new Library();

        while (true) {
            System.out.println("\n1. Add Book.");
            System.out.println("2. Borrow Book.");
            System.out.println("3. Return Book.");
            System.out.println("4. Reserve Book.");
            System.out.println("5. Check Book Availability.");
            System.out.println("6. Display Fines.");
            System.out.println("7. Search Book.");
            System.out.println("8. Exit.");            
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    scanner.nextLine(); 
                    System.out.print("Enter book name to add: ");
                    String bookName = scanner.nextLine();
                    library.addBook(bookName);
                    break;

                case 2:
                    scanner.nextLine(); 
                    System.out.print("Enter student's name: ");
                    String studentName = scanner.nextLine();
                    System.out.print("Enter book name to borrow: ");
                    String bookToBorrow = scanner.nextLine();
                    System.out.print("Enter due date (YYYY-MM-DD): ");
                    String dueDateString = scanner.nextLine();
                    Date dueDate = null;
                    try {
                        dueDate = new SimpleDateFormat("yyyy-MM-dd").parse(dueDateString);
                    } catch (ParseException e) {
                        System.out.println("Invalid date format. Please use YYYY-MM-DD.");
                        continue;
                    }
                    library.borrowBook(studentName, bookToBorrow, dueDate);
                    break;

                case 3:
                    scanner.nextLine();
                    System.out.print("Enter book name to return: ");
                    String bookToReturn = scanner.nextLine();
                    library.returnBook(bookToReturn);
                    break;

                case 4:
                    scanner.nextLine(); 
                    System.out.print("Enter student's name: ");
                    String resStudentName = scanner.nextLine();
                    System.out.print("Enter book name to reserve: ");
                    String resBookName = scanner.nextLine();
                    library.reserveBook(resStudentName, resBookName);
                    break;

                case 5:
                    library.displayBookAvailability();
                    break;

                case 6:
                    library.displayFines();
                    break;

                case 7:
                    scanner.nextLine(); 
                    System.out.print("Enter book name to search: ");
                    String bookToSearch = scanner.nextLine();
                    library.searchBook(bookToSearch);
                    break;

                case 8:
                    System.out.println("Exiting program.");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }
}
