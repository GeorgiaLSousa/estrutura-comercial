package com.inteligencia.ciclo_comercial.Repository;

import com.inteligencia.ciclo_comercial.Model.UserAtivo;
import com.inteligencia.ciclo_comercial.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAtivoRepository extends JpaRepository<UserAtivo, String> {

    @Query("SELECT p FROM UserAtivo p WHERE LOWER(p.email) = LOWER(:email)")
    Optional<UserAtivo> findByEmail(@Param("email") String email);
}