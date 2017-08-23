package passnote.poli.edu.co.PassNote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import passnote.poli.edu.co.PassNote.entities.UserEntity;

@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UserRepository extends JpaRepository<UserEntity,Long> {

	UserEntity findByUsername(@Param("username")String username);
	
	UserEntity findByEmail(@Param("email")String email);
	
	@Query("select count(u) > 0 from UsuarioEntity u where u.username = :username")
	boolean isUserCreated(@Param("username") String username);
	
}
