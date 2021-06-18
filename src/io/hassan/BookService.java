package io.hassan;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Main executor class.
 */
public class BookService {

    private final BookDatabase bookDatabase;
    private static final String BOOK_SAVED_SUCCESSFULLY = "Your book [%s] has been saved.";
    private static final String FAILURE = "Failure!";
    private static final String INDEX_NOT_FOUND = "Index not found";
    private static final String LIBRARY_SAVED = "Library saved successfully.";
    private static final String LIBRARY_NOT_SAVED = "Error during save.";

    public BookService(BookDatabase bookDatabase) {
        this.bookDatabase = bookDatabase;
    }

    /**
     * Add specific book into library.
     *
     * @param book book details
     * @return book id
     */
    public String addBook(Book book) {
        return bookDatabase.addBook(book) > 0 ? String.format(BOOK_SAVED_SUCCESSFULLY, book.getId()) : FAILURE;
    }

    /**
     * View all books we have in library.
     *
     * @return List of books we have in the library, summarized.
     */
    public String viewBooks() {
        return bookDatabase
                .viewBooks().stream()
                .map(Book::toStringSummary)
                .collect(Collectors.joining(System.lineSeparator()));

    }

    /**
     * Updated book details
     *
     * @param newBook book new details
     * @param id      book id.
     * @return updated book details.
     */
    public String updateBook(Book newBook, int id) {
        return bookDatabase.getBook(id) instanceof BookNull ? INDEX_NOT_FOUND :
                bookDatabase.updateBook(newBook, id).toString();
    }

    /**
     * @param id Book index.
     * @return Book details.
     */
    public String getBook(int id) {
        return this.bookDatabase.getBook(id).toString();
    }

    /**
     * @param query Query search.
     * @return Matching books details.
     */
    public String search(String query) {
        return Arrays.stream(query.split(" "))
                .map(this::getMatchedKeyword)
                .flatMap(Collection::stream)
                .distinct()
                .sorted(Comparator.comparing(Book::getId))
                .collect(Collectors.toList())
                .stream().map(Book::toStringSummary)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private List<Book> getMatchedKeyword(String keyword) {
        return bookDatabase
                .viewBooks().stream()
                .filter(book -> book.getId().toString().contains(keyword) ||
                        book.getTitle().contains(keyword) ||
                        book.getDescription().contains(keyword) ||
                        book.getAuthor().contains(keyword))
                .collect(Collectors.toList());
    }

    //  Flatten a stream of two arrays of the same type in Java
    public static<T> Stream<T> flatten(List<T> ... books) {
        return Stream.of(books).flatMap(List::stream);
    }


    /**
     * Save the books into .CSV file.
     *
     * @return true if successfully saved and false if not.
     */
    public String save() {
        File csvOutputFile = new File(bookDatabase.CSV_FILE_NAME);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            bookDatabase.viewBooks().stream()
                    .map(Book::toStringCSV)
                    .forEach(pw::println);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return LIBRARY_NOT_SAVED;
        }
        return LIBRARY_SAVED;
    }

}
