package com.mascotin.inventorymanagementapplication.model;

import com.mascotin.inventorymanagementapplication.calculator.DefaultZeroCalculator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openxava.annotations.*;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.util.Collection;

@Entity
@Getter @Setter
@NoArgsConstructor
public class RestockInventoryPlanner {
    @Id
    @Hidden
    private int restockPlanId = 1;

    @Required
    @Stereotype("MONEY")
    @DecimalMin("0")
    @DefaultValueCalculator(DefaultZeroCalculator.class)
    private BigDecimal presupuesto;

    @ElementCollection
    @ListProperties("productSku, productName, restockAmount, estimatedCost")
    private Collection<ProductRestockDetail> productsToRestock;

}
