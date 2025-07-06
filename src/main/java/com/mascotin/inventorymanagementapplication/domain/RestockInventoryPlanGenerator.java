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

public class RestockInventoryPlanGenerator {

    private static final int OPTIMAL_STOCK_DAYS = 15;
    private static final int SECURITY_STOCK_dAYS = 5;
    private static final double HIGH_DEMAND = 10.0;

    public static RestockPlanResult generatePlan(BigDecimal budget) throws Exception {
        RestockPlanResult restockPlanResult = new RestockPlanResult();
        BigDecimal totalCost = BigDecimal.ZERO;

        TypedQuery<Product> query = XPersistence.getManager()
                .createQuery("from Product p where p.productStatus = :status", Product.class);

        query.setParameter("status", ProductStatus.AVAILABLE);

        List<Product> products = query.getResultList();

        for (Product product : products) {

            double averageDailySales = calculateAverageDailySales(product);
            double optimalStock = averageDailySales * OPTIMAL_STOCK_DAYS;
            double securityStock = averageDailySales * SECURITY_STOCK_dAYS;

            if (product.getStock() < securityStock) {
                int baseAmount = (int) Math.ceil(optimalStock - product.getStock());
                int adjustedAmount = adjustForDemand(baseAmount, averageDailySales);

                BigDecimal estimatedCost = product.getPurchaseCost()
                        .multiply(new BigDecimal(adjustedAmount));

                if (totalCost.add(estimatedCost).compareTo(budget) <= 0) {
                    restockPlanResult.getProductsToRestock().add(
                            new ProductRestockDetail(
                                    product.getSku(),
                                    product.getName(),
                                    adjustedAmount,
                                    estimatedCost
                            )
                    );
                    totalCost = totalCost.add(estimatedCost);
                } else if (averageDailySales > HIGH_DEMAND) {
                    restockPlanResult.getCriticAlerts().add("Producto crítico no reponible: " +
                            product.getName() + " - Demanda: " + averageDailySales);
                }
            }
        }

        restockPlanResult.setPredictedBudget(totalCost);
        return restockPlanResult;
    }

    private static double calculateAverageDailySales(Product product) {
        TypedQuery<Long> query = XPersistence.getManager().createQuery(
                "select sum(oi.quantity) from PurchaseOrder po " +
                        "join po.orderItems oi " +
                        "where oi.product = :product and po.orderDate >= :dateLimit", Long.class);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -30);
        Date dateLimit = cal.getTime();

        LocalDate localDateLimit = dateLimit.toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();

        query.setParameter("product", product);
        query.setParameter("dateLimit", localDateLimit);

        Long totalSales = query.getSingleResult();
        if (totalSales == null) { totalSales = 0L; }
        return totalSales / 30.0;
    }

    private static int adjustForDemand(int baseAmount, double demand) {
        if (demand > HIGH_DEMAND) return baseAmount + 10;
        return baseAmount;
    }
}
