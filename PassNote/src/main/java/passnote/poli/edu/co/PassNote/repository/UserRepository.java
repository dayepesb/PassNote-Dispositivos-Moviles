package passnote.poli.edu.co.PassNote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import passnote.poli.edu.co.PassNote.entities.UsersEntity;

public interface UserRepository extends JpaRepository<UsersEntity, Long> {

    UsersEntity findByUser(String user);

    UsersEntity findByMail(String email);

    @Query("select count(u) > 0 from UsersEntity u where u.user = :username")
    boolean isUserCreated(@Param("username") String username);
}
