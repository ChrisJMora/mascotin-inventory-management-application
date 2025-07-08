package com.mascotin.inventorymanagementapplication.domain;

import com.mascotin.inventorymanagementapplication.model.Product;
import com.mascotin.inventorymanagementapplication.model.ProductRestockDetail;
import com.mascotin.inventorymanagementapplication.model.catalogue.ProductStatus;
import com.mascotin.inventorymanagementapplication.pojo.RestockPlanResult;
import org.openxava.jpa.XPersistence;

import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SuppressWarnings({"PMD.LawOfDemeter", "PMD.AvoidInstantiatingObjectsInLoops"})
public abstract class RestockInventoryPlanGenerator {

    private static final int OPTIMAL_DAYS = 15;
    private static final int SECURITY_DAYS = 5;
    private static final double HIGH_DEMAND = 10.0;

    public static RestockPlanResult generatePlan(final BigDecimal budget) {
        final RestockPlanResult restockPlan = new RestockPlanResult();
        BigDecimal totalCost = BigDecimal.ZERO;

        // Obtener todos los productos activos
        final TypedQuery<Product> query = XPersistence.getManager()
                .createQuery("from Product p where p.productStatus = :status", Product.class);

        query.setParameter("status", ProductStatus.AVAILABLE);

        final List<Product> products = query.getResultList();

        // Procesar cada producto
        for (final Product product : products) {

            final double averageDailySales = calculateAverageDailySales(product);
            final double optimalStock = averageDailySales * OPTIMAL_DAYS;
            final double securityStock = averageDailySales * SECURITY_DAYS;

            if (product.getStock() < securityStock) {
                final int baseAmount = (int) Math.ceil(optimalStock - product.getStock());
                final int adjustedAmount = adjustForDemand(baseAmount, averageDailySales);

                final BigDecimal estimatedCost = product.getPurchaseCost()
                        .multiply(new BigDecimal(adjustedAmount));

                if (totalCost.add(estimatedCost).compareTo(budget) <= 0) {
                    restockPlan.getProductsToRestock().add(
                            new ProductRestockDetail(
                                    product.getSku(),
                                    product.getName(),
                                    adjustedAmount,
                                    estimatedCost
                            )
                    );
                    totalCost = totalCost.add(estimatedCost);
                } else if (averageDailySales > HIGH_DEMAND) {
                    restockPlan.getCriticAlerts().add("Producto crítico no reponible: " +
                            product.getName() + " - Demanda: " + averageDailySales);
                }
            }
        }

        restockPlan.setPredictedBudget(totalCost);
        return restockPlan;
    }

    private static double calculateAverageDailySales(final Product product) {
        final TypedQuery<Long> query = XPersistence.getManager().createQuery(
                "select sum(oi.amount) from PurchaseOrder po " +
                        "join po.orderItems oi " +
                        "where oi.product = :product and po.orderDate >= :dateLimit", Long.class);

        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -30);
        final Date dateLimit = calendar.getTime();

        final LocalDate localDateLimit = dateLimit.toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();

        query.setParameter("product", product);
        query.setParameter("dateLimit", localDateLimit);

        Long totalSales = query.getSingleResult();
        if (totalSales == null) { totalSales = 0L; }
        return totalSales / 30.0;
    }

    private static int adjustForDemand(final int baseAmount, final double demand) {
        int result;
        if (demand > HIGH_DEMAND) {
            result = baseAmount + 10;
        } else {
            result = baseAmount;
        }
        return result;
    }
}
