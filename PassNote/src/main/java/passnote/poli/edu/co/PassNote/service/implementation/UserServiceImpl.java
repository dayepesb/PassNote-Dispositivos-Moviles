package passnote.poli.edu.co.PassNote.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import passnote.poli.edu.co.PassNote.entities.PasswordResetTokenEntity;
import passnote.poli.edu.co.PassNote.entities.UsersEntity;
import passnote.poli.edu.co.PassNote.exceptions.InvalidPasswordResetTokenException;
import passnote.poli.edu.co.PassNote.exceptions.UserNotFoundException;
import passnote.poli.edu.co.PassNote.exceptions.UsernameIsNotUniqueException;
import passnote.poli.edu.co.PassNote.repository.PasswordResetTokenRepository;
import passnote.poli.edu.co.PassNote.repository.UserRepository;
import passnote.poli.edu.co.PassNote.service.UserService;
import passnote.poli.edu.co.PassNote.service.dto.PasswordResetTokenDTO;
import passnote.poli.edu.co.PassNote.service.dto.UsersDTO;

import java.util.*;

@Service
public class UserServiceImpl extends passnote.poli.edu.co.PassNote.service.BaseService implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    public UsersDTO findByUsername(String username) throws UserNotFoundException {
        UsersEntity user = userRepository.findByUser(username);

        if (user != null) {
            return mapper.map(user, UsersDTO.class);
        } else {
            throw new UserNotFoundException();
        }
    }

    @Override
    public UsersDTO createUser(UsersDTO usersDTO) throws UsernameIsNotUniqueException {
        processValidations(usersDTO);

        usersDTO.setPassword(usersDTO.getPassword());

        UsersEntity userEntity = mapper.map(usersDTO, UsersEntity.class);
        return mapper.map(userRepository.saveAndFlush(userEntity), UsersDTO.class);
    }

    private void processValidations(UsersDTO userDTO) throws UsernameIsNotUniqueException {
        if (!isUserUnique(userDTO.getId(), userDTO.getUser())) {
            throw new UsernameIsNotUniqueException();
        }
    }

    @Override
    public boolean isUserUnique(Long idUsuario, String username) {
        try {
            UsersDTO persistedUser = findByUsername(username);
            return persistedUser != null && persistedUser.getId().compareTo(idUsuario) != 0;
        } catch (UserNotFoundException e) {
            return true;
        }
    }

    @Override
    public boolean userExist(String username) {
        return userRepository.isUserCreated(username);
    }

    @Override
    public UsersDTO findByEmail(String email) throws UserNotFoundException {
        UsersEntity user = userRepository.findByMail(email);
        if (user != null) {
            return mapper.map(user, UsersDTO.class);
        } else {
            throw new UserNotFoundException();
        }
    }

    @Override
    public PasswordResetTokenDTO createTokenResetPassword(UsersDTO user) {
        PasswordResetTokenEntity tokenEntity = passwordResetTokenRepository
                .findByUser(mapper.map(user, UsersEntity.class));
        if (tokenEntity != null) {
            passwordResetTokenRepository.delete(tokenEntity.getId());
        }

        String token = UUID.randomUUID().toString();

        PasswordResetTokenDTO tokenDTO = new PasswordResetTokenDTO();
        tokenDTO.setUser(user);
        tokenDTO.setExpirationDate(calculateExpirationDateForToken());
        tokenDTO.setToken(token);

        tokenEntity = passwordResetTokenRepository.saveAndFlush(mapper.map(tokenDTO, PasswordResetTokenEntity.class));
        logger.info("generated token {} for user {}", tokenEntity.getToken(), user.getMail());

        return mapper.map(tokenEntity, PasswordResetTokenDTO.class);
    }

    private Date calculateExpirationDateForToken() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, PasswordResetTokenDTO.EXPIRATION_TIME_IN_MINUTES);

        return calendar.getTime();
    }

    @Override
    public void autorizateChangePassword(long id, String token) throws InvalidPasswordResetTokenException {
        PasswordResetTokenEntity passToken = passwordResetTokenRepository.findByToken(token);
        validateToken(id, passToken);

        UsersEntity user = passToken.getUser();
        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetailsService.getSpringSecurityUser(mapper.map(user, UsersDTO.class)), null,
                Arrays.asList(new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE")));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Override
    public void validateTokenResetPassword(long id, String token) throws InvalidPasswordResetTokenException {
        PasswordResetTokenEntity passToken = passwordResetTokenRepository.findByToken(token);
        validateToken(id, passToken);
    }

    private PasswordResetTokenEntity validateToken(long id, PasswordResetTokenEntity passToken)
            throws InvalidPasswordResetTokenException {
        if ((passToken == null) || (passToken.getUser().getIdUser() != id)) {
            throw new InvalidPasswordResetTokenException();
        }

        Calendar cal = Calendar.getInstance();
        if ((passToken.getExpirationDate().getTime() - cal.getTime().getTime()) <= 0) {
            throw new InvalidPasswordResetTokenException();
        }

        return passToken;
    }

    @Override
    public void changePassword(String newPassword) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UsersEntity userEntity = userRepository.findByUser(user.getUsername());

        userEntity.setPassword(passwordEncoder.encode(newPassword));
        userRepository.saveAndFlush(userEntity);

        removeTokenResetPassword(mapper.map(userEntity, UsersDTO.class));
        logger.info("password for user {} successfully changed", user.getUsername());
    }

    @Override
    public void removeTokenResetPassword(UsersDTO user) {
        PasswordResetTokenEntity tokenEntity = passwordResetTokenRepository
                .findByUser(mapper.map(user, UsersEntity.class));
        if (tokenEntity != null) {
            passwordResetTokenRepository.delete(tokenEntity.getId());
        }
    }
}
