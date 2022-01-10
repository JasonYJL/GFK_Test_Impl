package posmy.interview.boot.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import posmy.interview.boot.entity.User;
import posmy.interview.boot.repository.UserRepository;

import java.util.List;
import java.util.Optional;

public class CustomAuthentication implements AuthenticationManager {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        Optional<User> userOptional = userRepository.findById(authentication.getName());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (user.getPassword().equals(authentication.getCredentials())) {
                String name = authentication.getName();

                return new UsernamePasswordAuthenticationToken(name, null, List.of(new SimpleGrantedAuthority(user.getRole().name())));
            }
        }

        return null;
    }
}
