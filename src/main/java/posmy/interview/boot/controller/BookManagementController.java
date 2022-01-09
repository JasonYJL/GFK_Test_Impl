package posmy.interview.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.dto.BookDto;
import posmy.interview.boot.service.BookManagementService;

import javax.validation.Valid;

@RestController
@RequestMapping("book")
public class BookManagementController {
    @Autowired
    private BookManagementService bookManagementService;

    @PostMapping("add")
    public void addNewBook(@Valid @RequestBody BookDto bookDto) {
        bookManagementService.addNewBook(bookDto);
    }

    @PostMapping("update")
    public void updateBookInfo(@Valid @RequestBody BookDto bookDto) {
        bookManagementService.updateBookInfo(bookDto);
    }

    @DeleteMapping("remove")
    public void removeBook(Integer bookId) {
        bookManagementService.removeBook(bookId);
    }
}
