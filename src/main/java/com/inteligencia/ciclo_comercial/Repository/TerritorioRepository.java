package com.inteligencia.ciclo_comercial.Repository;

import com.inteligencia.ciclo_comercial.Model.Territorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface TerritorioRepository extends JpaRepository<Territorio, Long> {

    @Query("SELECT p FROM Territorio p WHERE LOWER(p.codigoTerritorio) LIKE LOWER(CONCAT('%', :codigoTerritorio, '%'))")
    List<Territorio> findByNome(@Param("codigoTerritorio") String codigoTerritorio);

    @Query("SELECT t FROM Territorio t WHERE LOWER(t.codigoRegional) = LOWER(:codigoRegional)")
    List<Territorio> findByCodigoRegional(@Param("codigoRegional") String codigoRegional);

    @Query("SELECT t FROM Territorio t WHERE LOWER(t.codigoFilial) = LOWER(:codigoFilial)")
    List<Territorio> findByCodigoFilial(@Param("codigoFilial") String codigoFilial);

    @Query("SELECT t FROM Territorio t WHERE LOWER(t.nomeUnidade) = LOWER(:nomeUnidade)")
    List<Territorio> findByNomeUnidade(@Param("nomeUnidade") String nomeUnidade);

    @Query("SELECT t FROM Territorio t WHERE LOWER(t.codigoRegional) = LOWER(:codigoRegional)")
    Optional<Territorio> findFirstByCodigoRegional(@Param("codigoRegional") String codigoRegional);

    @Query("SELECT t FROM Territorio t WHERE LOWER(t.codigoFilial) = LOWER(:codigoFilial)")
    Optional<Territorio> findFirstByCodigoFilial(@Param("codigoFilial") String codigoFilial);

    @Query("SELECT t FROM Territorio t WHERE LOWER(t.nomeUnidade) = LOWER(:nomeUnidade)")
    Optional<Territorio> findFirstByNomeUnidade(@Param("nomeUnidade") String nomeUnidade);

    boolean existsByNomeUnidade(String nomeUnidade);

    Optional<Territorio> findByCodigoTerritorio(String codigoTerritorio);
}