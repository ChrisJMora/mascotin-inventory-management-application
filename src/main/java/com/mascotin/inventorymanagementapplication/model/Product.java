package com.mascotin.inventorymanagementapplication.model;
import com.mascotin.inventorymanagementapplication.model.catalogue.*;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.DisplayName;
import org.openxava.annotations.*;
import javax.persistence.*;
import javax.validation.ValidationException;
import java.math.BigDecimal;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter @Setter
public abstract class Product {

    @Id @Hidden
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, unique = true)
    @Required
    private String SKU;  // Código de producto único

    @Column(length = 50)
    @Required
    private String Nombre;

    @Stereotype("MONEY")
    @Required
    private BigDecimal Precio;  // Precio de venta

    @Stereotype("MONEY")
    @Required
    private BigDecimal Costo;  // Costo de compra

    @Required
    private BigDecimal Descuento = BigDecimal.ZERO;  // Entre 0.00 y 1.00

    @Required
    @Enumerated(EnumType.STRING)
    private Manufacturer Fabricante;

    @Enumerated(EnumType.STRING)
    @Required
    private Brand Marca;

    @Required
    @Enumerated(EnumType.STRING)
    private Specie Especie;

    @Required
    @Enumerated(EnumType.STRING)
    private Age Edad;

    @Required
    @Enumerated(EnumType.STRING)
    private Size Raza;

    private int Stock;

    @Column(name = "CATEGORY", insertable = false, updatable = false)
    private String category;

    public String getCategory() {
        return this.getClass().getSimpleName();
    }

    @PrePersist @PreUpdate
    private void validate() {
        if (Precio == null || Precio.compareTo(BigDecimal.ZERO) <= 0)
            throw new ValidationException("El precio debe ser mayor a 0");

        if (Costo == null || Costo.compareTo(BigDecimal.ZERO) <= 0)
            throw new ValidationException("El costo de compra debe ser mayor a 0");

        if (Descuento == null || Descuento.compareTo(BigDecimal.ZERO) < 0 || Descuento.compareTo(BigDecimal.ONE) > 0)
            throw new ValidationException("El descuento debe estar entre 0.00 y 1.00");
    }
}
