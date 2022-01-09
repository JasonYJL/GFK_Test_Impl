package posmy.interview.boot.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import posmy.interview.boot.dto.BookBorrowerDto;
import posmy.interview.boot.dto.BookDto;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.entity.BookBorrower;
import posmy.interview.boot.entity.User;
import posmy.interview.boot.repository.BookBorrowerRepository;
import posmy.interview.boot.repository.BookRepository;
import posmy.interview.boot.repository.UserRepository;

import java.util.List;

@Service
public class BookManagementService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookBorrowerRepository bookBorrowerRepository;

    public void addNewBook(BookDto bookDto) {
        Book book = Book.builder()
                .status("AVAILABLE")
                .description(bookDto.getDescription())
                .title(bookDto.getTitle())
                .build();

        bookRepository.save(book);
    }

    @SneakyThrows
    public void updateBookInfo(BookDto bookDto) {
        Book book = getBookById(bookDto.getId());

        book.setDescription(bookDto.getDescription());
        book.setTitle(bookDto.getTitle());

        bookRepository.save(book);
    }

    @SneakyThrows
    public void insertBookBorrowerRecord(BookBorrowerDto bookBorrowerDto) {
        Book book = borrowOrReturnBook(bookBorrowerDto.getBookId(), true);
        User user = getUserById(bookBorrowerDto.getUserName());
        BookBorrower bookBorrower = BookBorrower.builder()
                .book(book)
                .user(user)
                .build();

        bookBorrowerRepository.save(bookBorrower);
    }

    @SneakyThrows
    public Book borrowOrReturnBook(int bookId, boolean isBorrow) {
        Book book = getBookById(bookId);

        if (isBorrow && !book.getStatus().equals("AVAILABLE")) {
            throw new Exception("The book is not available for borrow.");
        }

        book.setStatus(isBorrow ? "BORROWED" : "AVAILABLE");
        bookRepository.save(book);

        return book;
    }

    public void removeBook(int bookId) {
        bookRepository.deleteById(bookId);
    }

    public List<BookDto> getAllAvailableBook() {
        List<Book> bookList = bookRepository.findByStatus("AVAILABLE");
        return bookList.stream()
                .map(book -> BookDto.builder()
                        .id(book.getId())
                        .title(book.getTitle())
                        .description(book.getDescription())
                        .status(book.getStatus())
                        .build())
                .toList();
    }

    @SneakyThrows
    private Book getBookById(int bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new Exception("Book with batch no (" + bookId + ") record not found."));

    }

    @SneakyThrows
    private User getUserById(String userName) {
        return userRepository.findById(userName)
                .orElseThrow(() -> new Exception("User with user name (" + userName + ") not found."));
    }
}
