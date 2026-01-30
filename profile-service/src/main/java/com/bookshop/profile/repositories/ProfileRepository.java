package com.bookshop.profile.repositories;

import com.bookshop.profile.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, String> {

    Optional<Profile> findByUserId(String userId);

    Optional<Profile> findByUsername(String username);

    boolean existsByUserId(String userId);

    boolean existsByUsername(String username);
}
