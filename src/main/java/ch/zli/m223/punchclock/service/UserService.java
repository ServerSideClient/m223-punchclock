package ch.zli.m223.punchclock.service;

import ch.zli.m223.punchclock.domain.PunchClockUser;
import ch.zli.m223.punchclock.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        PunchClockUser punchClockUser = userRepository.findByUsername(username);
        if (punchClockUser == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(punchClockUser.getUsername(), punchClockUser.getPassword(), emptyList());
    }

    public Boolean registerUser(PunchClockUser punchClockUser) {
        if (userRepository.findByUsername(punchClockUser.getUsername()) != null) {
            return false;
        }
        userRepository.saveAndFlush(punchClockUser);
        return true;
    }

}
