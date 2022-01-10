package posmy.interview.boot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import posmy.interview.boot.constant.BookStatusEnum;
import posmy.interview.boot.constant.UserRoleEnum;
import posmy.interview.boot.dto.BookBorrowerDto;
import posmy.interview.boot.dto.BookDto;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.entity.User;
import posmy.interview.boot.exception.BookNotAvailableException;
import posmy.interview.boot.exception.RecordNotFoundException;
import posmy.interview.boot.repository.BookBorrowerRepository;
import posmy.interview.boot.repository.BookRepository;
import posmy.interview.boot.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BookManagementServiceTests {

    private BookRepository bookRepository;
    private UserRepository userRepository;
    private BookBorrowerRepository bookBorrowerRepository;
    private BookManagementService bookManagementService;

    @BeforeEach
    void setup() {
        bookRepository = Mockito.mock(BookRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        bookBorrowerRepository = Mockito.mock(BookBorrowerRepository.class);
        bookManagementService = new BookManagementService(bookRepository, userRepository, bookBorrowerRepository);
    }

    @Test
    void test_addNewBook() {
        BookDto bookDto = BookDto.builder()
                .description("Test In Depth Description")
                .title("Testing in depth")
                .build();

        bookManagementService.addNewBook(bookDto);
        verify(bookRepository, times(1)).save(any());
    }

    @Test
    void test_updateBookInfo_with_non_exist_id() {
        int bookId = 1;
        BookDto bookDto = BookDto.builder()
                .id(bookId)
                .description("Test In Depth Description")
                .title("Testing in depth")
                .build();

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RecordNotFoundException.class, () -> bookManagementService.updateBookInfo(bookDto));
        assertEquals("Book with id (" + bookId + ") record not found.", exception.getMessage());
    }

    @Test
    void test_updateBookInfo_with_exist_id() {
        int bookId = 1;
        String newDescription = "Test In Depth Description v2";
        String newTitle = "Testing in depth v2";
        BookDto bookDto = BookDto.builder()
                .id(bookId)
                .description(newDescription)
                .title(newTitle)
                .build();

        Book book = Book.builder()
                .id(bookId)
                .title("Testing in depth")
                .description("Test In Depth Description")
                .status(BookStatusEnum.AVAILABLE)
                .build();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        bookManagementService.updateBookInfo(bookDto);

        assertEquals(newTitle, book.getTitle());
        assertEquals(newDescription, book.getDescription());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void test_removeBook() {
        int bookId = 1;
        bookManagementService.removeBook(bookId);
        verify(bookRepository, times(1)).deleteById(1);
    }

    @Test
    void test_getAllAvailableBook() {
        List<Book> bookList = List.of(
                Book.builder()
                        .id(1)
                        .title("Testing in depth")
                        .description("Test In Depth Description")
                        .status(BookStatusEnum.AVAILABLE)
                        .build(),
                Book.builder()
                        .id(2)
                        .title("Testing in depth v2")
                        .description("Test In Depth Description v2")
                        .status(BookStatusEnum.AVAILABLE)
                        .build()
        );
        when(bookRepository.findByStatus(BookStatusEnum.AVAILABLE)).thenReturn(bookList);

        List<BookDto> bookDtos = bookManagementService.getAllAvailableBook();
        assertEquals(bookList.size(), bookDtos.size());

        for (int i = 0; i < bookDtos.size(); i++) {
            BookDto bookDto = bookDtos.get(i);
            Book book = bookList.get(i);

            assertEquals(book.getId(), bookDto.getId());
            assertEquals(book.getDescription(), bookDto.getDescription());
            assertEquals(book.getStatus(), bookDto.getStatus());
            assertEquals(book.getTitle(), bookDto.getTitle());
        }
    }

    @Test
    void test_insertBookBorrowerRecord_with_borrowed_status_book() {
        int bookId = 1;

        BookBorrowerDto bookBorrowerDto = BookBorrowerDto.builder()
                .bookId(bookId)
                .userName("tester")
                .build();

        Book book = Book.builder()
                .status(BookStatusEnum.BORROWED)
                .description("Testing In Depth Description")
                .title("Testing In Depth")
                .id(bookId)
                .build();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        Exception exception = assertThrows(BookNotAvailableException.class, () -> bookManagementService.insertBookBorrowerRecord(bookBorrowerDto));
        assertEquals("The book (" + bookId + ") is not available for borrow.", exception.getMessage());
    }

    @Test
    void test_insertBookBorrowerRecord_with_non_exist_userName() {
        String userName = "tester";

        BookBorrowerDto bookBorrowerDto = BookBorrowerDto.builder()
                .bookId(1)
                .userName("tester")
                .build();

        Book book = Book.builder()
                .status(BookStatusEnum.AVAILABLE)
                .build();

        when(bookRepository.findById(any())).thenReturn(Optional.of(book));
        when(userRepository.findById(userName)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RecordNotFoundException.class, () -> bookManagementService.insertBookBorrowerRecord(bookBorrowerDto));
        assertEquals("User with user name (" + userName + ") not found.", exception.getMessage());
    }

    @Test
    void test_insertBookBorrowerRecord_with_normal_behavior() {
        String userName = "tester";
        int bookId = 1;

        BookBorrowerDto bookBorrowerDto = BookBorrowerDto.builder()
                .bookId(bookId)
                .userName(userName)
                .build();

        Book book = Book.builder()
                .status(BookStatusEnum.AVAILABLE)
                .description("Testing In Depth Description")
                .title("Testing In Depth")
                .id(bookId)
                .build();

        User user = User.builder()
                .name(userName)
                .userName("Tester")
                .role(UserRoleEnum.MEMBER)
                .build();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(userRepository.findById(userName)).thenReturn(Optional.of(user));

        bookManagementService.insertBookBorrowerRecord(bookBorrowerDto);

        assertEquals(BookStatusEnum.BORROWED, book.getStatus());
        verify(bookBorrowerRepository, times(1)).save(any());
    }

    @Test
    void test_borrowOrReturnBook_with_return_book() {
        int bookId = 1;

        Book book = Book.builder()
                .status(BookStatusEnum.BORROWED)
                .description("Testing In Depth Description")
                .title("Testing In Depth")
                .id(bookId)
                .build();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        bookManagementService.borrowOrReturnBook(bookId, false);

        assertEquals(BookStatusEnum.AVAILABLE, book.getStatus());
        verify(bookRepository, times(1)).save(book);
    }
}
