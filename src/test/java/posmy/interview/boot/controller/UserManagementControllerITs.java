package posmy.interview.boot.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import posmy.interview.boot.constant.UserRoleEnum;
import posmy.interview.boot.dto.UserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
class UserManagementControllerITs {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mvc = webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    @WithMockUser(username="librarian", authorities = {"LIBRARIAN"})
    @DisplayName("Librarian can view all members")
    void test_viewAllMember_with_librarian_login() throws Exception {
        MvcResult result = mvc.perform(get("/user/view"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        List<UserDto> userDtoList = mapper.readValue(content,  new TypeReference<>() {});

        assertEquals(1, userDtoList.size());
        assertEquals("member", userDtoList.get(0).getUserName());
        assertEquals("MEMBER 1", userDtoList.get(0).getName());
        assertEquals(UserRoleEnum.MEMBER, userDtoList.get(0).getRole());
        assertNull(userDtoList.get(0).getPassword());
    }

    @Test
    @WithMockUser(username="librarian", authorities = {"LIBRARIAN"})
    @DisplayName("Librarian can add new member")
    void test_addNewMember_with_librarian_login() throws Exception {
        UserDto userDto = UserDto.builder()
                .userName("tester")
                .name("Tester")
                .role(UserRoleEnum.MEMBER)
                .password("password")
                .build();

        String content = mapper.writeValueAsString(userDto);

        mvc.perform(post("/user/add")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="librarian", authorities = {"LIBRARIAN"})
    @DisplayName("Librarian update existing member info")
    void test_updateMemberInfo_with_librarian_login() throws Exception {
        UserDto userDto = UserDto.builder()
                .userName("member")
                .name("Member V2")
                .role(UserRoleEnum.MEMBER)
                .build();

        String content = mapper.writeValueAsString(userDto);

        mvc.perform(post("/user/update")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="librarian", authorities = {"LIBRARIAN"})
    @DisplayName("Librarian remove member record")
    void test_removeMemberRecord_with_librarian_login() throws Exception {
        mvc.perform(delete("/user/remove")
                .param("userName", "member"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value="member", authorities = {"MEMBER"})
    @DisplayName("Member remove own record")
    void test_removeOwnMemberRecord_with_member_login() throws Exception {
        mvc.perform(delete("/user/remove")
                .param("userName", "member"))
                .andExpect(status().isOk());
    }
}
