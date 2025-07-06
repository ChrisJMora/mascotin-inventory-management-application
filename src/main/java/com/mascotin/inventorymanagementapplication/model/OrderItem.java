package com.mascotin.inventorymanagementapplication.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openxava.annotations.Required;

import javax.persistence.Embeddable;
import javax.persistence.OneToOne;

@Embeddable
@Getter @Setter
@NoArgsConstructor
public class OrderItem {

    @Required
    @OneToOne
    private Product product;

    @Required
    private int quantity;
}
