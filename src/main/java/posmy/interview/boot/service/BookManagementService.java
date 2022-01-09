package posmy.interview.boot.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import posmy.interview.boot.dto.BookDto;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.repository.BookBorrowerRepository;
import posmy.interview.boot.repository.BookRepository;

@Service
public class BookManagementService {
    @Autowired
    private BookRepository bookRepository;

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
        Book book = bookRepository.findById(bookDto.getId()).orElseThrow(() -> new Exception("Book with batch no (" + bookDto.getId() + ") record not found."));

        book.setDescription(bookDto.getDescription());
        book.setTitle(bookDto.getTitle());

        bookRepository.save(book);
    }

    public void removeBook(int bookId) {
        bookRepository.deleteById(bookId);
    }
}
