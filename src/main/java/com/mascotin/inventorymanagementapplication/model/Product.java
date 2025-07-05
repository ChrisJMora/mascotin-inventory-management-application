package com.mascotin.inventorymanagementapplication.model;
import com.mascotin.inventorymanagementapplication.model.catalogue.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openxava.annotations.*;
import javax.persistence.*;
import javax.validation.ValidationException;
import java.math.BigDecimal;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter @Setter
@NoArgsConstructor
@SuppressWarnings({"PMD.CyclomaticComplexity"})
public abstract class Product {

    @Id @Hidden
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(length = 20, unique = true)
    @Required
    private String sku;  // Código de producto único

    @Column(length = 50)
    @Required
    private String nombre;

    @Stereotype("MONEY")
    @Required
    private BigDecimal precio;  // Precio de venta

    @Stereotype("MONEY")
    @Required
    private BigDecimal costo;  // Costo de compra

    @Required
    private BigDecimal descuento = BigDecimal.ZERO;  // Entre 0.00 y 1.00

    @Required
    @Enumerated(EnumType.STRING)
    private Manufacturer fabricante;

    @Enumerated(EnumType.STRING)
    @Required
    private Brand marca;

    @Required
    @Enumerated(EnumType.STRING)
    private Specie especie;

    @Required
    @Enumerated(EnumType.STRING)
    private Age edad;

    @Required
    @Enumerated(EnumType.STRING)
    private Size raza;

    private int stock;

    @Column(name = "CATEGORY", insertable = false, updatable = false)
    private String category;

    public String getCategory() {
        return this.getClass().getSimpleName();
    }

    @PrePersist @PreUpdate
    private void validate() {
        if (precio == null || precio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("El precio debe ser mayor a 0");
        }

        if (costo == null || costo.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("El costo de compra debe ser mayor a 0");
        }

        if (descuento == null || descuento.compareTo(BigDecimal.ZERO) < 0 || descuento.compareTo(BigDecimal.ONE) > 0) {
            throw new ValidationException("El descuento debe estar entre 0.00 y 1.00");
        }
    }
}
