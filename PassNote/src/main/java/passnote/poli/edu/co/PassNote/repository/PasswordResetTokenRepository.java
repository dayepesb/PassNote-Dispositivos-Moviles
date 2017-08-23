package passnote.poli.edu.co.PassNote.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import passnote.poli.edu.co.PassNote.entities.PasswordResetTokenEntity;
import passnote.poli.edu.co.PassNote.entities.UserEntity;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity, Long> {
	PasswordResetTokenEntity findByUser(UserEntity user);
	PasswordResetTokenEntity findByToken(String token);
}
