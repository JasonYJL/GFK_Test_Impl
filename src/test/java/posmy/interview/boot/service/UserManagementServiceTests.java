package posmy.interview.boot.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import posmy.interview.boot.constant.UserRoleEnum;
import posmy.interview.boot.dto.UserDto;
import posmy.interview.boot.entity.User;
import posmy.interview.boot.exception.NonMemberRemoveException;
import posmy.interview.boot.exception.RecordNotFoundException;
import posmy.interview.boot.repository.UserRepository;

import java.util.List;
import java.util.Optional;

class UserManagementServiceTests {
    private UserRepository userRepository;
    private UserManagementService userManagementService;

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        userManagementService = new UserManagementService(userRepository);
    }

    @Test
    void test_addNewUser() {
        UserDto userDto = UserDto.builder()
                .userName("tester")
                .name("Tester")
                .role(UserRoleEnum.MEMBER)
                .password("password")
                .build();

        userManagementService.addNewUser(userDto);

        verify(userRepository, times(1)).save(any());
    }

    @Test
    void test_updateUserInfo_with_normal_behavior() {
        String userName = "tester";

        UserDto userDto = UserDto.builder()
                .userName(userName)
                .name("Tester v2")
                .role(UserRoleEnum.LIBRARIAN)
                .password("password123")
                .build();

        User user = User.builder()
                .userName(userName)
                .name("Tester")
                .role(UserRoleEnum.MEMBER)
                .password("password")
                .build();

        when(userRepository.findById(userName)).thenReturn(Optional.of(user));

        userManagementService.updateUserInfo(userDto);

        assertEquals(userDto.getName(), user.getName());
        assertEquals(userDto.getRole(), user.getRole());
        assertEquals(user.getPassword(), user.getPassword());
        assertEquals(user.getUserName(), user.getUserName());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void test_updateUserInfo_with_non_exist_user() {
        String userName = "tester";
        UserDto userDto = UserDto.builder()
                .userName(userName)
                .name("Tester v2")
                .role(UserRoleEnum.LIBRARIAN)
                .password("password123")
                .build();

        when(userRepository.findById(userName)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RecordNotFoundException.class, () -> userManagementService.updateUserInfo(userDto));
        assertEquals("User with user name (" + userName + ") not found.", exception.getMessage());
    }

    @Test
    void test_removeUser_by_non_librarian_delete_other_member() {
        String userName = "tester";

        userManagementService = new UserManagementService(userRepository) {
            @Override
            String getCurrentAuthenticatedUser() {
                return "member";
            }

            @Override
            List<GrantedAuthority> getCurrentAuthenticatedUserAuthorities() {
                return List.of(new SimpleGrantedAuthority(UserRoleEnum.MEMBER.name()));
            }
        };

        User user = User.builder()
                .name(userName)
                .userName("Other Member")
                .role(UserRoleEnum.MEMBER)
                .build();

        when(userRepository.findById(userName)).thenReturn(Optional.of(user));

        Exception exception = assertThrows(NonMemberRemoveException.class, () -> userManagementService.removeUser(userName));
        assertEquals("Only librarian allowed to removed other member record", exception.getMessage());
    }

    @Test
    void test_removeUser_by_delete_non_member_record() {
        String userName = "tester";

        userManagementService = new UserManagementService(userRepository) {
            @Override
            String getCurrentAuthenticatedUser() {
                return "librarian";
            }

            @Override
            List<GrantedAuthority> getCurrentAuthenticatedUserAuthorities() {
                return List.of(new SimpleGrantedAuthority(UserRoleEnum.LIBRARIAN.name()));
            }
        };

        User user = User.builder()
                .name(userName)
                .userName("Other Librarian")
                .role(UserRoleEnum.LIBRARIAN)
                .build();

        when(userRepository.findById(userName)).thenReturn(Optional.of(user));

        Exception exception = assertThrows(NonMemberRemoveException.class, () -> userManagementService.removeUser(userName));
        assertEquals("Only member can be removed.", exception.getMessage());
    }

    @Test
    void test_removeUser_with_normal_behavior() {
        String userName = "tester";

        userManagementService = new UserManagementService(userRepository) {
            @Override
            String getCurrentAuthenticatedUser() {
                return userName;
            }

            @Override
            List<GrantedAuthority> getCurrentAuthenticatedUserAuthorities() {
                return List.of(new SimpleGrantedAuthority(UserRoleEnum.LIBRARIAN.name()));
            }
        };

        User user = User.builder()
                .name(userName)
                .userName("Other Librarian")
                .role(UserRoleEnum.MEMBER)
                .build();
        when(userRepository.findById(userName)).thenReturn(Optional.of(user));

        userManagementService.removeUser(userName);

        verify(userRepository, times(1)).deleteById(userName);
    }

    @Test
    void test_getMemberUsers() {
        List<User> userList = List.of(
                User.builder()
                    .userName("librarian")
                    .name("Librarian")
                    .role(UserRoleEnum.LIBRARIAN)
                    .password("password")
                    .build(),
                User.builder()
                    .userName("member")
                    .name("Member")
                    .role(UserRoleEnum.MEMBER)
                    .password("password")
                    .build()
        );

        when(userRepository.findByRole(UserRoleEnum.MEMBER)).thenReturn(userList);

        List<UserDto> userDtoList = userManagementService.getMemberUsers();

        assertEquals(userList.size(), userDtoList.size());

        for (int i = 0; i < userDtoList.size(); i++) {
            UserDto userDto = userDtoList.get(i);
            User user = userList.get(i);

            assertEquals(user.getUserName(), userDto.getUserName());
            assertEquals(user.getName(), userDto.getName());
            assertEquals(user.getRole(), userDto.getRole());
            assertNull(userDto.getPassword());
        }
    }
}
