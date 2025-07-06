package com.mascotin.inventorymanagementapplication.model;

import com.mascotin.inventorymanagementapplication.calculator.CurrentLocalDateCalculator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openxava.annotations.DefaultValueCalculator;
import org.openxava.annotations.Hidden;
import org.openxava.annotations.ListProperties;
import org.openxava.annotations.Required;
import org.openxava.jpa.XPersistence;

import javax.persistence.*;
import javax.validation.ValidationException;
import javax.validation.constraints.AssertTrue;
import java.time.LocalDate;
import java.util.Collection;

@Entity
@Getter @Setter
@NoArgsConstructor
public class PurchaseOrder {

    @Id
    @Hidden
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Required
    @DefaultValueCalculator(value = CurrentLocalDateCalculator.class)
    private LocalDate orderDate;

    @ElementCollection
    @ListProperties("product.sku, product.name, quantity")
    private Collection<OrderItem> orderItems;

    @AssertTrue(message = "Debe haber al menos un ítem en la orden")
    private boolean isOrderItemsValid() {
        return orderItems != null && !orderItems.isEmpty();
    }

    @PrePersist
    @PreUpdate
    private void updateProductStock() {
        if (orderItems == null || orderItems.isEmpty()) {
            return; // No hay elementos para procesar
        }

        EntityManager em = XPersistence.getManager();
        for (OrderItem orderItem : orderItems) {
            Product product = orderItem.getProduct();
            int quantity = orderItem.getQuantity();

            if (product == null || quantity <= 0) {
                throw new ValidationException("Producto o cantidad inválida en la línea de pedido");
            }

            // Cargar la entidad Product desde la base de datos para evitar problemas de persistencia
            Product managedProduct = em.find(Product.class, product.getProductId());
            if (managedProduct == null) {
                throw new ValidationException("Producto no encontrado: " + product.getSku());
            }

            int newStock = managedProduct.getStock() - quantity;
            if (newStock < 0) {
                throw new ValidationException("Stock insuficiente para el producto: " + managedProduct.getName());
            }

            managedProduct.setStock(newStock);
            em.merge(managedProduct); // Actualizar el producto en la base de datos
        }
    }
}
