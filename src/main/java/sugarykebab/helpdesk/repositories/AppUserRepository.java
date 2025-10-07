package sugarykebab.helpdesk.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import sugarykebab.helpdesk.entities.AppUser;

import java.util.Optional;

public interface AppUserRepository extends CrudRepository<AppUser, String> {

    Optional<AppUser> findByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO app_user (email, password, firstname, lastname, organization_id) " +
            "VALUES (:email, :password, :firstname, :lastname, :orgId)", nativeQuery = true)
    void insertUser(String email, String password, String firstname, String lastname, Long orgId);
}
