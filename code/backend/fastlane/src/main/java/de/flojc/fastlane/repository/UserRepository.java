package de.flojc.fastlane.repository;

import de.flojc.fastlane.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// The code snippet is defining a repository interface called `UserRepository` for accessing and manipulating data
// related to the `User` entity.
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
