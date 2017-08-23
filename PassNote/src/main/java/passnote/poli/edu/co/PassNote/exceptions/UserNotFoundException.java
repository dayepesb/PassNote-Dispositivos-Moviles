package passnote.poli.edu.co.PassNote.exceptions;


public class UserNotFoundException extends BaseException {

	private static final long serialVersionUID = 1L;
	
	public UserNotFoundException() {
		super("El usuario no existe");
	}
	
}
