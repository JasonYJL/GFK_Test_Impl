package posmy.interview.boot.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import posmy.interview.boot.dto.UserDto;
import posmy.interview.boot.entity.User;
import posmy.interview.boot.repository.UserRepository;

import java.util.List;

@Service
public class UserManagementService {
    @Autowired
    private UserRepository userRepository;

    public void addNewUser(UserDto userDto) {
        User user = User.builder()
                .name(userDto.getName())
                .role(userDto.getRole())
                .build();

        userRepository.save(user);
    }

    @SneakyThrows
    public void updateUserInfo(UserDto userDto) {
        User user = getUserById(userDto.getUserName());

        user.setName(user.getName());
        user.setRole(userDto.getRole());

        userRepository.save(user);
    }

    @SneakyThrows
    public void removeUser(String userName) {
        User user = getUserById(userName);

        if (!"MEMBER".equals(user.getRole())) {
            throw new Exception("Only user which is MEMBER can be removed");
        }

        userRepository.deleteById(userName);
    }

    public List<UserDto> getMemberUsers() {
        List<User>  users = userRepository.findByRole("MEMBER");
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
                .orElseThrow(() -> new Exception("User with user name (" + userName + ") not found."));
    }
}
