package ch.zli.m223.punchclock.controller;

import ch.zli.m223.punchclock.domain.PunchClockUser;
import ch.zli.m223.punchclock.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    private BCryptPasswordEncoder passwordEncoder;

    public UserController(UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;

        PunchClockUser admin = new PunchClockUser();
        admin.setUsername("Zaratustra");
        admin.setPassword(passwordEncoder.encode("test"));
        userService.registerUser(admin);
    }

    @RequestMapping(value = "/sign-up", method = RequestMethod.POST)
    public PunchClockUser signUp(@RequestBody PunchClockUser punchClockUser) {
        punchClockUser.setPassword(passwordEncoder.encode(punchClockUser.getPassword()));
        if (userService.registerUser(punchClockUser)) {
            return punchClockUser;
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT);
    }

}
