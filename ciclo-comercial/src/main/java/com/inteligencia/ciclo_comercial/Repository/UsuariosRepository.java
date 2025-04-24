package com.inteligencia.ciclo_comercial.Repository;

import com.inteligencia.ciclo_comercial.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuariosRepository extends JpaRepository<Usuario, Long> {

    @Query("SELECT p FROM Usuario p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Usuario> findByNome(@Param("nome") String nome);

    Optional<Usuario> findByEmailUsuario(String email);






}