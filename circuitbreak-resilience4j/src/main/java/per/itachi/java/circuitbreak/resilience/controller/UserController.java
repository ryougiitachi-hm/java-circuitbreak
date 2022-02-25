package per.itachi.java.circuitbreak.resilience.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import per.itachi.java.circuitbreak.resilience.service.UserService;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/v1/users/show")
    @ResponseStatus(HttpStatus.OK)
    public void showUserDto(String username) {
        userService.showUserInfo(username);
    }
}
