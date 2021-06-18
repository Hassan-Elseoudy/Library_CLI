package io.hassan;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BookValidator {

    private final BookService bookService;
    private static final String INVALID_REQUEST = "Your request isn't valid";
    private static final String COMMA_DELIMITER = ",";
    private static final int NUMBER_OF_CHOICES = 6;
    private final List<Integer> choices;

    public static final int VIEW_BOOKS = 1;
    public static final int ADD_BOOK = 2;
    public static final int EDIT_BOOK = 3;
    public static final int SEARCH_BOOK = 4;
    public static final int SAVE = 5;
    public static final int GET_BOOK = 6;

    public BookValidator(BookService bookService) {
        this.bookService = bookService;
        this.choices = IntStream.rangeClosed(1, NUMBER_OF_CHOICES).boxed().collect(Collectors.toList());
    }

    public boolean isValidRequest(StringBuilder query, int operation) {
        if (choices.stream().noneMatch(choice -> choice == operation)) return false;
        switch (operation) {
            case GET_BOOK:
                if (!query.toString().matches("\\d+")) return false; //TODO: Check If value greater than Integer.
                break;
            case EDIT_BOOK:
                if (!query.toString().split(COMMA_DELIMITER)[0].matches("\\d+")) return false; //TODO: Check If value greater than Integer.

        }
        return true;
    }

    public String execute(StringBuilder query, int operation) {
        boolean isValidRequest = this.isValidRequest(query, operation);
        if (!isValidRequest) return INVALID_REQUEST;
        switch (operation) {
            case VIEW_BOOKS:
                return bookService.viewBooks();
            case ADD_BOOK:
                String[] params = query.toString().split(COMMA_DELIMITER);
                return bookService.addBook(new Book(params[0], params[1], params[2]));
            case EDIT_BOOK:
                String[] editParams = query.toString().split(COMMA_DELIMITER);
                return bookService.updateBook(new Book(Integer.parseInt(editParams[0]), editParams[1], editParams[2], editParams[3]), Integer.parseInt(editParams[0]));
            case GET_BOOK:
                return bookService.getBook(Integer.parseInt(query.toString()));
            case SEARCH_BOOK:
                return bookService.search(query.toString());
            case SAVE:
                return bookService.save();
        }
        return INVALID_REQUEST;
    }

}
