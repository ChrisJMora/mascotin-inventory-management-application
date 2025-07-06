package com.mascotin.inventorymanagementapplication.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openxava.annotations.*;

import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Collection;

@Entity
@Getter @Setter
@NoArgsConstructor
public class RestockInventoryPlanner {
    @Id
    @Hidden
    private int id = 1;

    @Required
    @Stereotype("MONEY")
    private BigDecimal presupuesto;

    @Stereotype("BUTTON")
    @Action("RestockPlan.generate")
    private String generarPlan;

    @ElementCollection
    @ListProperties("productSku, productName, restockAmount, estimatedCost")
    private Collection<ProductRestockDetail> productsToRestock;

}
