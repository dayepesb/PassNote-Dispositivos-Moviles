package passnote.poli.edu.co.PassNote.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import passnote.poli.edu.co.PassNote.entities.UserEntity;
import passnote.poli.edu.co.PassNote.exceptions.InvalidPasswordResetTokenException;
import passnote.poli.edu.co.PassNote.exceptions.UserNotFoundException;
import passnote.poli.edu.co.PassNote.exceptions.UsernameIsNotUniqueException;
import passnote.poli.edu.co.PassNote.repository.PasswordResetTokenRepository;
import passnote.poli.edu.co.PassNote.repository.UserRepository;
import passnote.poli.edu.co.PassNote.service.UserService;
import passnote.poli.edu.co.PassNote.service.dto.PasswordResetTokenDTO;
import passnote.poli.edu.co.PassNote.service.dto.UserDTO;

public class UserServiceImplementation extends BaseService implements UserService {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

//	@Autowired
//	private UserDetailsServiceImpl userDetailsService;


	@Override
	public UserDTO findByUsername(String username) throws UserNotFoundException  {
		  UserEntity user = userRepository.findByUsername(username);

			if (user != null) {
				return mapper.map(user, UserDTO.class);
			} else {
				throw new UserNotFoundException();
			}
	}

	@Override
	public UserDTO findByEmail(String email) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserDTO createUser(UserDTO user) throws UsernameIsNotUniqueException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isUserUnique(Long idUser, String username) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean userExist(String username) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public PasswordResetTokenDTO createTokenResetPassword(UserDTO user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeTokenResetPassword(UserDTO user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateTokenResetPassword(long userId, String token) throws InvalidPasswordResetTokenException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void autorizateChangePassword(long userId, String token) throws InvalidPasswordResetTokenException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changePassword(String newPassword) {
		// TODO Auto-generated method stub
		
	}

}
