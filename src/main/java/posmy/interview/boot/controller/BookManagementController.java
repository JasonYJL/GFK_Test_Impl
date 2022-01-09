package posmy.interview.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.dto.BookBorrowerDto;
import posmy.interview.boot.dto.BookDto;
import posmy.interview.boot.service.BookManagementService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("book")
public class BookManagementController {
    @Autowired
    private BookManagementService bookManagementService;

    @PreAuthorize("hasAuthority('LIBRARIAN')")
    @PostMapping("add")
    public void addNewBook(@Valid @RequestBody BookDto bookDto) {
        bookManagementService.addNewBook(bookDto);
    }

    @PreAuthorize("hasAuthority('LIBRARIAN')")
    @PostMapping("update")
    public void updateBookInfo(@Valid @RequestBody BookDto bookDto) {
        bookManagementService.updateBookInfo(bookDto);
    }

    @PreAuthorize("hasAuthority('LIBRARIAN')")
    @DeleteMapping("remove")
    public void removeBook(Integer bookId) {
        bookManagementService.removeBook(bookId);
    }

    @PreAuthorize("hasAuthority('MEMBER')")
    @PostMapping("borrow")
    public void addBookBorrowerRecord(@RequestBody BookBorrowerDto bookBorrowerDto) {
        bookManagementService.insertBookBorrowerRecord(bookBorrowerDto);
    }

    @PreAuthorize("hasAuthority('MEMBER')")
    @PostMapping("return")
    public void returnBorrowedBook(Integer bookId) {
        bookManagementService.borrowOrReturnBook(bookId,false);
    }

    @PreAuthorize("hasAuthority('MEMBER')")
    @GetMapping("view")
    public ResponseEntity<List<BookDto>> getAllAvailableBook() {
        return ResponseEntity.ok(bookManagementService.getAllAvailableBook());
    }
}
