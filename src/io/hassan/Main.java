package io.hassan;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    private static BookValidator validator = null;
    private static final String COMMA_DELIMITER = ",";
    private static final String SEARCH_BOOK = String.format("==== Search ====%s Type in one or more keywords to search for %s %sSearch: ", System.lineSeparator(), System.lineSeparator(), "\t");
    private static final String EDIT_BOOK = String.format("==== Edit A Book ====%s", System.lineSeparator());
    private static final String ADD_BOOK = String.format("==== ADD A Book ====%s", System.lineSeparator());
    private static final String VIEW_BOOKS = String.format("==== View Books ====%s", System.lineSeparator());
    private static final String BOOK_MANAGER = "==== Book Manager ====\n\t1) View all books \n\t2) Add a book \n\t3) Edit a book \n\t4) Search for a book \n\t5) Save and exit\n";
    private static final String ENTER_INFO = String.format("Please enter the following information:%s", System.lineSeparator());
    private static final String ENTER_BOOK_ID = "Please enter the book id you want to edit: ";
    private static final String VIEW_BOOK_DETAILS = "To view details enter the book ID, to return press <Enter>: ";
    private static final String TITLE = "Title: ";
    private static final String AUTHOR = " Author: ";
    private static final String DESCRIPTION = " Description: ";
    private static final String EDIT_INFO = "Input the following information. To leave a field unchanged, hit <Enter>";

    static {
        try {
            validator = new BookValidator(new BookService(new BookDatabase()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        while (true) {
            System.out.print(BOOK_MANAGER);
            System.out.print("Choose [1 - 5]: ");
            int operation = Integer.parseInt(s.nextLine().trim());
            StringBuilder query = new StringBuilder();
            switch (operation) {
                case BookValidator.ADD_BOOK -> {
                    System.out.print(ADD_BOOK);
                    System.out.println(ENTER_INFO);
                    System.out.print(TITLE);
                    query.append(s.nextLine().replaceAll(COMMA_DELIMITER, "")).append(COMMA_DELIMITER);
                    System.out.print(AUTHOR);
                    query.append(s.nextLine().replaceAll(COMMA_DELIMITER, "")).append(COMMA_DELIMITER);
                    System.out.print(DESCRIPTION);
                    query.append(s.nextLine().replaceAll(COMMA_DELIMITER, ""));
                    System.out.println(validator.execute(query, BookValidator.ADD_BOOK));
                }
                case BookValidator.EDIT_BOOK -> {
                    System.out.print(EDIT_BOOK);
                    System.out.println(validator.execute(null, BookValidator.VIEW_BOOKS));
                    System.out.print(ENTER_BOOK_ID);
                    query.append(s.nextLine());
                    String bookDetails = validator.execute(query, BookValidator.GET_BOOK);
                    List<String> bookDetailsSeparated = Arrays.stream(bookDetails.split(COMMA_DELIMITER)).map(field -> field.split(": ")[1]).collect(Collectors.toList());
                    System.out.println(bookDetails);
                    System.out.println(EDIT_INFO);
                    System.out.printf(TITLE + "[%s] ", bookDetailsSeparated.get(1));
                    String newTitle = s.nextLine();
                    query.append(COMMA_DELIMITER).append(newTitle.isBlank() ? bookDetailsSeparated.get(1) : newTitle).append(COMMA_DELIMITER);
                    System.out.printf(AUTHOR + "[%s] ", bookDetailsSeparated.get(2));
                    String newAuthor = s.nextLine();
                    query.append(newAuthor.isBlank() ? bookDetailsSeparated.get(2) : newAuthor).append(COMMA_DELIMITER);
                    System.out.printf(DESCRIPTION + "[%s] ", bookDetailsSeparated.get(3));
                    String newDescription = s.nextLine();
                    query.append(newDescription.isBlank() ? bookDetailsSeparated.get(3) : newDescription);
                    System.out.println(validator.execute(query, BookValidator.EDIT_BOOK));
                }
                case BookValidator.VIEW_BOOKS -> {
                    System.out.print(VIEW_BOOKS);
                    System.out.println(validator.execute(query, BookValidator.VIEW_BOOKS));
                    System.out.print(VIEW_BOOK_DETAILS);
                    query.append(s.nextLine());
                    System.out.println(validator.execute(query, BookValidator.GET_BOOK));
                }
                case BookValidator.SAVE -> {
                    System.out.println(validator.execute(query, BookValidator.SAVE));
                    return;
                }
                case BookValidator.SEARCH_BOOK -> {
                    System.out.print(SEARCH_BOOK);
                    query.append(s.nextLine());
                    System.out.println(validator.execute(query, BookValidator.SEARCH_BOOK));
                    query.setLength(0);
                    System.out.print(VIEW_BOOK_DETAILS);
                    query.append(s.nextLine());
                    System.out.println(validator.execute(query, BookValidator.GET_BOOK));
                }
            }
        }
    }
}
