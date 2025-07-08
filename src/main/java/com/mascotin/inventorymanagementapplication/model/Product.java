package com.mascotin.inventorymanagementapplication.model;
import com.mascotin.inventorymanagementapplication.calculator.DefaultZeroCalculator;
import com.mascotin.inventorymanagementapplication.model.catalogue.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openxava.annotations.*;
import javax.persistence.*;
import javax.validation.ValidationException;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "TIPO_ENTIDAD")
@Getter @Setter
@NoArgsConstructor
@SuppressWarnings({"PMD.CyclomaticComplexity"})
public class Product {

    @Id @Hidden
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(length = 20, unique = true)
    @Required
    private String sku;  // Código de producto único

    @Column(length = 50)
    @Required
    private String name;

    @Required
    @Stereotype("MONEY")
    @DecimalMin("0")
    @DefaultValueCalculator(DefaultZeroCalculator.class)
    private BigDecimal sellPrice;  // Precio de venta

    @Required
    @Stereotype("MONEY")
    @DecimalMin("0")
    @DefaultValueCalculator(DefaultZeroCalculator.class)
    private BigDecimal purchaseCost;  // Costo de compra

    @DecimalMin("0")
    @DecimalMax("1")
    @DefaultValueCalculator(DefaultZeroCalculator.class)
    private BigDecimal sellDiscount;  // Entre 0.00 y 1.00

    @Required
    @Enumerated(EnumType.STRING)
    private ProductManufacturer manufacturer;

    @Required
    @Enumerated(EnumType.STRING)
    private ProductBrand brand;

    @Required
    @Enumerated(EnumType.STRING)
    private PetSpecie petSpecie;

    @Required
    @Enumerated(EnumType.STRING)
    private PetAge petAge;

    @Required
    @Enumerated(EnumType.STRING)
    private PetSize petSize;

    @Required
    @DecimalMin("0")
    @DecimalMax("5000")
    @DefaultValueCalculator(DefaultZeroCalculator.class)
    private int stock;

    @Required
    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;

    @PrePersist @PreUpdate
    private void validate() {
        if (sellPrice == null || sellPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("El precio debe ser mayor a 0");
        }

        if (purchaseCost == null || purchaseCost.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("El costo de compra debe ser mayor a 0");
        }

        if (sellDiscount == null || sellDiscount.compareTo(BigDecimal.ZERO) < 0 || sellDiscount.compareTo(BigDecimal.ONE) > 0) {
            throw new ValidationException("El descuento debe estar entre 0.00 y 1.00");
        }
    }
}
