package com.inteligencia.ciclo_comercial.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_Sailpoint_People", schema = "dbo", catalog = "db_inteligenciacomercial")
public class UserAtivo {
    @Id
    @Column(name = "name")
    private String userId;

     @Column(name = "email")
    private String email;

     @Column(name = "status")
    private String status;
}
