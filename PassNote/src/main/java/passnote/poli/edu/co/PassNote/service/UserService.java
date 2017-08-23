package passnote.poli.edu.co.PassNote.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import passnote.poli.edu.co.PassNote.exceptions.InvalidPasswordResetTokenException;
import passnote.poli.edu.co.PassNote.exceptions.UserNotFoundException;
import passnote.poli.edu.co.PassNote.exceptions.UsernameIsNotUniqueException;
import passnote.poli.edu.co.PassNote.service.dto.PasswordResetTokenDTO;
import passnote.poli.edu.co.PassNote.service.dto.UserDTO;

public interface UserService {
	UserDTO findByUsername(String username) throws  UserNotFoundException;

	UserDTO findByEmail(String email) throws UsernameNotFoundException;

	UserDTO createUser(UserDTO user) throws UsernameIsNotUniqueException;

	boolean isUserUnique(Long idUser, String username);

	boolean userExist(String username);

	PasswordResetTokenDTO createTokenResetPassword(UserDTO user);

	void removeTokenResetPassword(UserDTO user);

	void validateTokenResetPassword(long userId, String token) throws InvalidPasswordResetTokenException;

	void autorizateChangePassword(long userId, String token) throws InvalidPasswordResetTokenException;

	void changePassword(String newPassword);

}
