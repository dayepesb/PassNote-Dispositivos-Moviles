package passnote.poli.edu.co.PassNote.service.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import passnote.poli.edu.co.PassNote.exceptions.UserNotFoundException;
import passnote.poli.edu.co.PassNote.service.UserService;
import passnote.poli.edu.co.PassNote.service.dto.UsersDTO;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("authenticating user: {}", username);

        try {
            UsersDTO user = userService.findByUsername(username);
            logger.info("user authenticated: {}", username);
            return getSpringSecurityUser(user);
        } catch (UserNotFoundException e) {
            logger.error("failed to authenticate user: {}", username);
            throw new UsernameNotFoundException(e.getMessage());
        }

    }

    private List<GrantedAuthority> getGrantedAuhtorities(UsersDTO usersDTO) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
//		usersDTO.getRoles().stream().map((role) -> {
//			return role;
//		}).forEach((role) -> {
//			grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role.getType()));
//		});
//		logger.info("roles: {}", grantedAuthorities);
        return grantedAuthorities;
    }

    public User getSpringSecurityUser(UsersDTO user) {
        return new User(user.getUser(), user.getPassword(), true, true, true, true, getGrantedAuhtorities(user));
    }

}
