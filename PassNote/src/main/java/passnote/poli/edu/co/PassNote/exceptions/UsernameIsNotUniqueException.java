package passnote.poli.edu.co.PassNote.exceptions;

public class UsernameIsNotUniqueException extends BaseException {

	private static final long serialVersionUID = 1L;
	
	public UsernameIsNotUniqueException() {
		super("El nombre de usuario no es único");
	}
}
