package passnote.poli.edu.co.PassNote.exceptions;

public class InvalidPasswordResetTokenException extends BaseException {

	private static final long serialVersionUID = 1L;

	public InvalidPasswordResetTokenException() {
		super("El token es inválido");
	}

}
