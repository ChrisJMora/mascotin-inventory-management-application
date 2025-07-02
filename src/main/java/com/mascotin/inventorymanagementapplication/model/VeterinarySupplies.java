package com.mascotin.inventorymanagementapplication.model;

import com.mascotin.inventorymanagementapplication.model.catalogue.SupplyType;
import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.Required;
import javax.persistence.*;
import javax.validation.ValidationException;
import java.math.BigDecimal;

@Entity
@Getter @Setter
public class VeterinarySupplies extends Product {
    @Required
    @Enumerated(EnumType.STRING)
    private SupplyType Tipo; // Antiparasitario, Vitaminas, Probióticos

    @Required
    private BigDecimal Contenido; // contenido neto ml o mg

    @PrePersist @PreUpdate
    private void validateSupply() {
        if (Contenido == null || Contenido.compareTo(BigDecimal.ZERO) <= 0)
            throw new ValidationException("Contenido neto debe ser positivo");
    }
}
