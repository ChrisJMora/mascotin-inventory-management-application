package com.mascotin.inventorymanagementapplication.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openxava.annotations.Money;
import org.openxava.annotations.ReadOnly;
import org.openxava.annotations.Required;

import javax.persistence.Embeddable;
import javax.persistence.OneToOne;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Embeddable
@Getter @Setter
@NoArgsConstructor
@SuppressWarnings({"PMD.LawOfDemeter"})
public class OrderItem {

    @Required
    @OneToOne
    private Product product;

    @Required
    @DecimalMin("1")
    @DecimalMax("10")
    private int amount;

    @Money
    @ReadOnly
    public BigDecimal getSubtotal() {
        BigDecimal subtotal = BigDecimal.ZERO;
        if (product != null) {
            final BigDecimal price = product.getSellPrice()
                    .multiply(BigDecimal.ONE.subtract(product.getSellDiscount()));
            subtotal = price.multiply(new BigDecimal(amount)).setScale(2, RoundingMode.HALF_UP);
        }
        return subtotal;
    }
}
