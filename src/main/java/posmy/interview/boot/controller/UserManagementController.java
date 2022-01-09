package posmy.interview.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.dto.UserDto;
import posmy.interview.boot.service.UserManagementService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("user")
public class UserManagementController {

    @Autowired
    private UserManagementService userManagementService;

    @PreAuthorize("hasAuthority('LIBRARIAN')")
    @GetMapping("view")
    public ResponseEntity<List<UserDto>> getMemberUsers() {
        return ResponseEntity.ok(userManagementService.getMemberUsers());
    }

    @PreAuthorize("hasAuthority('LIBRARIAN')")
    @PostMapping("add")
    public void addNewUser(@Valid @RequestBody UserDto userDto) {
        userManagementService.addNewUser(userDto);
    }

    @PreAuthorize("hasAuthority('LIBRARIAN')")
    @PostMapping("update")
    public void updateUserInfo(@Valid @RequestBody UserDto userDto) {
        userManagementService.updateUserInfo(userDto);
    }

    @PreAuthorize("hasAnyAuthority('MEMBER', 'LIBRARIAN')")
    @DeleteMapping("remove")
    public void removeUser(String userName) {
        userManagementService.removeUser(userName);
    }

}
