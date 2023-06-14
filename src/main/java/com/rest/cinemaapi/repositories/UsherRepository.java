package com.rest.cinemaapi.repositories;

import com.rest.cinemaapi.models.Usher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UsherRepository extends JpaRepository<Usher, Long> {
    Optional<Usher> findUsherByEmail(String email);
}
