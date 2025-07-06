package com.mascotin.inventorymanagementapplication.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openxava.annotations.Stereotype;

import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRestockDetail {

    private String productSku;
    private String productName;
    private int restockAmount;

    @Stereotype("MONEY")
    private BigDecimal estimatedCost;
}