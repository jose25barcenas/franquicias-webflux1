package com.franquicias.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table("franquicias")
public class Franquicia {
    @Id
    private Long id;
    private String nombre;

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
