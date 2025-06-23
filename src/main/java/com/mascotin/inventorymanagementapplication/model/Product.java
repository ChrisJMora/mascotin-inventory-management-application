package com.mascotin.inventorymanagementapplication.model;

import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.Hidden;
import org.openxava.annotations.Required;
import org.openxava.annotations.Stereotype;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter @Setter
public abstract class Product {

    @Id @Hidden
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    @Required
    private String name;

    @Stereotype("MONEY")
    @Required
    private BigDecimal price;

    private BigDecimal discount = BigDecimal.ZERO;

    private int stock;

    @Column(name = "CATEGORY", insertable = false, updatable = false)
    private String category;

    public String getCategory() {
        return this.getClass().getSimpleName();
    }
}