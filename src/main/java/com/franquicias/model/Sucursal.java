package com.franquicias.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("sucursales")
public class Sucursal {
    @Id
    private Long id;
    private String nombre;
    @Column("franquicia_id")
    private Long franquiciaId;
}
