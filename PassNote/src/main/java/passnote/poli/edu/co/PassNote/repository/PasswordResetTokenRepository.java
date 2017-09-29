package passnote.poli.edu.co.PassNote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import passnote.poli.edu.co.PassNote.entities.PasswordResetTokenEntity;
import passnote.poli.edu.co.PassNote.entities.UsersEntity;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity, Long> {
    PasswordResetTokenEntity findByUser(UsersEntity user);

    PasswordResetTokenEntity findByToken(String token);
}
