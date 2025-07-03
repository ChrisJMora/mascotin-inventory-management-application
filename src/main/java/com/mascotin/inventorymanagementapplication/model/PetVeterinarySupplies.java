package com.mascotin.inventorymanagementapplication.model;

import com.mascotin.inventorymanagementapplication.model.catalogue.SupplyType;
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
public class PetVeterinarySupplies extends Product {
    @Required
    @Enumerated(EnumType.STRING)
    private SupplyType tipo; // Antiparasitario, Vitaminas, Probióticos

    @Required
    private BigDecimal contenido; // contenido neto ml o mg

    @PrePersist @PreUpdate
    private void validateSupply() {
        if (contenido == null || contenido.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Contenido neto debe ser positivo");
        }
    }
}
