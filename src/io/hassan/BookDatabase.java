package io.hassan;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Wrapper class for our database, The only class who
 * should directly communicate to the database (Our HashMap).
 * I'm using the linked hash map because mainly all of our operations
 * are indexed to the id, that minimizes the runtime If we
 * have a huge list of books.
 */
public class BookDatabase {

    private static int ID = 1;
    private final Map<Integer, Book> library;
    public final String COMMA_DELIMITER = ",";
    public final String CSV_FILE_NAME = "src/io/hassan/books.csv";

    public BookDatabase() throws IOException {
        this.library = new HashMap<>();
        ID = loadBooks(library);
    }

    /**
     * Load books into library by reading CSV file.
     *
     * @param library database instance.
     * @return number of books in library.
     * @throws IOException throws exception if not found.
     */
    public int loadBooks(Map<Integer, Book> library) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                library.put(ID++, new Book(Integer.parseInt(values[0]), values[1], values[2], values[3]));
            }
        }

        return ID;

    }

    /**
     * Add specific book into library.
     *
     * @param book book details
     * @return book id
     */
    public Integer addBook(Book book) {
        book.setId(ID);
        this.library.put(ID, book);
        return ID++;
    }

    /**
     * View all books we have in library.
     *
     * @return List of books we have in the library, summarized.
     */
    public List<Book> viewBooks() {
        return this.library.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    /**
     * Updated book details
     *
     * @param newBook book new details
     * @param id      book id.
     * @return updated book details.
     */
    public Book updateBook(Book newBook, int id) {
        return this.library.put(id, newBook);
    }

    /**
     * get a specific book by id, If the book not in database we return a Null book.
     *
     * @param id book id.
     * @return book id
     */
    public Book getBook(int id) {
        return this.library.getOrDefault(id, new BookNull(0, "", "", ""));
    }


}
