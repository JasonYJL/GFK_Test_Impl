package posmy.interview.boot.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import posmy.interview.boot.dto.BookBorrowerDto;
import posmy.interview.boot.dto.BookDto;

import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookManagementControllerTests {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mvc = webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test()
    @Order(1)
    @WithMockUser(username="librarian", authorities = {"LIBRARIAN"})
    @DisplayName("Librarian can add new book")
    void test_addNewBooks_with_librarian_login() throws Exception {
        BookDto bookDto = BookDto.builder()
                .title("Integration Test v2")
                .description("Integration Test v2 Description")
                .build();

        mvc.perform(post("/book/add")
                .content(mapper.writeValueAsString(bookDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    @WithMockUser(username="librarian", authorities = {"LIBRARIAN"})
    @DisplayName("Librarian can update book info")
    void test_updateBookInfo_with_librarian_login() throws Exception {
        BookDto bookDto = BookDto.builder()
                .id(1)
                .title("SOLID Principle In Action v2")
                .description("SOLID in depth v2.")
                .build();

        mvc.perform(post("/book/update")
                .content(mapper.writeValueAsString(bookDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    @WithMockUser(username="librarian", authorities = {"LIBRARIAN"})
    @DisplayName("Librarian can remove book")
    void test_removeBook_with_librarian_login() throws Exception {
        mvc.perform(delete("/book/remove")
                .param("bookId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    @WithMockUser(username="member", authorities = {"MEMBER"})
    @DisplayName("Member can borrow book")
    void test_addBookBorrowerRecord_with_member_login() throws Exception {
        BookBorrowerDto bookBorrowerDto = BookBorrowerDto.builder()
                .bookId(2)
                .userName("member")
                .build();

        mvc.perform(post("/book/borrow")
                    .content(mapper.writeValueAsString(bookBorrowerDto))
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    @WithMockUser(username="member", authorities = {"MEMBER"})
    @DisplayName("Member can return borrowed book")
    void test_returnBook_with_member_login() throws Exception {
        mvc.perform(post("/book/return")
                .param("bookId", "2"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    @WithMockUser(username="member", authorities = {"MEMBER"})
    @DisplayName("Member can view available book")
    void test_viewAllAvailableBooks_with_member_login() throws Exception {
        MvcResult result = mvc.perform(get("/book/view"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        List<BookDto> bookDtoList = mapper.readValue(content,  new TypeReference<>() {});

        assertEquals(3, bookDtoList.size());
    }
}
