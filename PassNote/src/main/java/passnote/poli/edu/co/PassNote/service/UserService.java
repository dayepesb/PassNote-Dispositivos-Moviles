package passnote.poli.edu.co.PassNote.service;

import passnote.poli.edu.co.PassNote.exceptions.InvalidPasswordResetTokenException;
import passnote.poli.edu.co.PassNote.exceptions.UserNotFoundException;
import passnote.poli.edu.co.PassNote.exceptions.UsernameIsNotUniqueException;
import passnote.poli.edu.co.PassNote.service.dto.PasswordResetTokenDTO;
import passnote.poli.edu.co.PassNote.service.dto.UsersDTO;

public interface UserService {
	UsersDTO findByUsername(String username) throws  UserNotFoundException;

	UsersDTO findByEmail(String email) throws UserNotFoundException;

	UsersDTO createUser(UsersDTO user) throws UsernameIsNotUniqueException;

	boolean isUserUnique(Long idUser, String username);

	boolean userExist(String username);

	PasswordResetTokenDTO createTokenResetPassword(UsersDTO user);

	void removeTokenResetPassword(UsersDTO user);

	void validateTokenResetPassword(long userId, String token) throws InvalidPasswordResetTokenException;

	void autorizateChangePassword(long userId, String token) throws InvalidPasswordResetTokenException;

	void changePassword(String newPassword);

}
