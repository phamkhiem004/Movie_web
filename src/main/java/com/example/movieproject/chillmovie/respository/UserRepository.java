package com.example.movieproject.chillmovie.respository;


import com.example.movieproject.chillmovie.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying
    @Query("UPDATE User u SET u.status = 'ACTIVE' WHERE u.id = :id")
    void setActive(@Param("id") Long id);

    @Modifying
    @Query("UPDATE User u SET u.status = 'BLOCKED' WHERE u.id = :id")
    void setBlocked(@Param("id") Long id);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);




}
