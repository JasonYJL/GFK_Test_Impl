package posmy.interview.boot.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import posmy.interview.boot.constant.UserRoleEnum;
import posmy.interview.boot.dto.UserDto;
import posmy.interview.boot.entity.User;
import posmy.interview.boot.exception.NonMemberRemoveException;
import posmy.interview.boot.exception.RecordNotFoundException;
import posmy.interview.boot.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserManagementService {

    private UserRepository userRepository;

    @Autowired
    public UserManagementService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addNewUser(UserDto userDto) {
        User user = User.builder()
                .userName(userDto.getUserName())
                .name(userDto.getName())
                .role(userDto.getRole())
                .password(userDto.getPassword())
                .build();

        userRepository.save(user);
    }

    @SneakyThrows
    public void updateUserInfo(UserDto userDto) {
        User user = getUserById(userDto.getUserName());

        user.setName(userDto.getName());
        user.setRole(userDto.getRole());
        user.setPassword(userDto.getPassword());

        userRepository.save(user);
    }

    @SneakyThrows
    public void removeUser(String userName) {
        User user = getUserById(userName);
        checkIfUserAllowedRemoveUserRecord(user);
        userRepository.deleteById(userName);
    }

    public List<UserDto> getMemberUsers() {
        List<User>  users = userRepository.findByRole(UserRoleEnum.MEMBER);
        return users.stream()
                    .map(user -> UserDto.builder()
                        .userName(user.getUserName())
                        .name(user.getName())
                        .role(user.getRole())
                        .build())
                    .toList();
    }

    @SneakyThrows
    private User getUserById(String userName) {
        return userRepository.findById(userName)
                .orElseThrow(() -> new RecordNotFoundException("User with user name (" + userName + ") not found."));
    }

    @SneakyThrows
    private void checkIfUserAllowedRemoveUserRecord(User user) {
        Optional<GrantedAuthority> librarianAuthorityOptional = getCurrentAuthenticatedUserAuthorities().stream()
                .filter(grantedAuthority -> grantedAuthority.getAuthority().equals("LIBRARIAN"))
                .findFirst();

        if (!librarianAuthorityOptional.isPresent() && !user.getUserName().equals(getCurrentAuthenticatedUser())) {
            throw new NonMemberRemoveException("Only librarian allowed to removed other member record");
        }

        if (!UserRoleEnum.MEMBER.equals(user.getRole())) {
            throw new NonMemberRemoveException("Only member can be removed.");
        }
    }

    String getCurrentAuthenticatedUser() {
        org.springframework.security.core.userdetails.User authenticatedUser = (org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return authenticatedUser.getUsername();
    }

    List<GrantedAuthority> getCurrentAuthenticatedUserAuthorities() {
         return (List<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    }
}
