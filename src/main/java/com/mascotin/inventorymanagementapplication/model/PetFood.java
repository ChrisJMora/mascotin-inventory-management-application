package com.mascotin.inventorymanagementapplication.model;

import com.mascotin.inventorymanagementapplication.model.catalogue.FoodType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openxava.annotations.Required;
import javax.persistence.*;
import javax.validation.ValidationException;
import java.math.BigDecimal;

@Entity
@Getter @Setter
@NoArgsConstructor
public class PetFood extends Product {
    @Required
    @Enumerated(EnumType.STRING)
    private FoodType tipo;  // Básica, Premium

    @Required
    private BigDecimal contenido; // contenido neto en kg

    @PrePersist @PreUpdate
    private void validateFood() {
        if (contenido == null || contenido.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Contenido neto debe ser positivo");
        }
    }
}
