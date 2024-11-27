package com.inteligencia.ciclo_comercial.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_ciclo_comercial_estrutura", schema = "dbo", catalog = "db_inteligenciacomercial")
public class Territorio {

    @Id
    @Column(name = "Name")
    private String codigoTerritorio;

    @Column(name = "Assigned_Rep__r_Email")
    private String emailRTV;

    @Column(name = "Assigned_Rep__r_Name")
    private String nomeRTV;

    @Column(name = "Sales_Area_District_Hierarchy__r_Name")
    private String codigoRegional;

    @Column(name = "Sales_Region_Hierarchy__r_Name")
    private String codigoFilial;

    @Column(name = "Commercial_Unit_Hierarchy__r_Name")
    private String nomeUnidade;

    @Column(name = "isActive__c")
    private Boolean ativo = true; // Define o campo ativo como true por padrão
}