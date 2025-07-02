package com.mascotin.inventorymanagementapplication.model;

import com.mascotin.inventorymanagementapplication.model.catalogue.FoodType;
import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.Required;
import javax.persistence.*;
import javax.validation.ValidationException;
import java.math.BigDecimal;

@Entity
@Getter @Setter
public class Food extends Product {
    @Required
    @Enumerated(EnumType.STRING)
    private FoodType Tipo;  // Básica, Premium

    @Required
    private BigDecimal Contenido; // contenido neto en kg

    @PrePersist @PreUpdate
    private void validateFood() {
        if (Contenido == null || Contenido.compareTo(BigDecimal.ZERO) <= 0)
            throw new ValidationException("Contenido neto debe ser positivo");
    }
}
