package io.hassan;


/**
 * Null object design patter.
 */
public class BookNull extends Book {

    private static final String BOOK_NOT_FOUND = "Book Not Found!";

    public BookNull(int id, String title, String author, String description) {
        super(id, title, author, description);
    }

    @Override
    public String toString() {
        return BOOK_NOT_FOUND;
    }

}
