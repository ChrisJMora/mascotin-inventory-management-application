package com.mascotin.inventorymanagementapplication.action;

import com.mascotin.inventorymanagementapplication.domain.RestockInventoryPlanGenerator;
import com.mascotin.inventorymanagementapplication.model.ProductRestockDetail;
import com.mascotin.inventorymanagementapplication.pojo.RestockPlanResult;
import org.openxava.actions.ViewBaseAction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GenerateRestockPlanAction extends ViewBaseAction {

    @Override
    public void execute() throws Exception {
        BigDecimal budget = (BigDecimal) getView().getValue("presupuesto");
        RestockPlanResult generatedRestockPlan = RestockInventoryPlanGenerator.generatePlan(budget);

        Collection<Map<String, Object>> planAsMaps = getMaps(generatedRestockPlan);
        getView().setValue("productsToRestock", planAsMaps);

        StringBuilder message = new StringBuilder();
        message.append("Presupuesto utilizado: ")
                .append(generatedRestockPlan.getPredictedBudget()).append("\n\n");

        if (!generatedRestockPlan.getProductsToRestock().isEmpty()) {
            message.append("Productos sugeridos:\n");
            for (ProductRestockDetail item : generatedRestockPlan.getProductsToRestock()) {
                message.append("- ")
                        .append(item.getProductName())
                        .append(" (").append(item.getRestockAmount()).append(" unidades) → $")
                        .append(item.getEstimatedCost()).append("\n");
            }
        }

        if (!generatedRestockPlan.getCriticAlerts().isEmpty()) {
            message.append("\n⚠️ Productos críticos NO reponibles:\n");
            for (String alert : generatedRestockPlan.getCriticAlerts()) {
                message.append("- ").append(alert).append("\n");
            }
        }

        if (!generatedRestockPlan.getExcessAlerts().isEmpty()) {
            message.append("\n📦 Excesos de inventario detectados:\n");
            for (String excess : generatedRestockPlan.getExcessAlerts()) {
                message.append("- ").append(excess).append("\n");
            }
        }

        addMessage(message.toString());
    }

    private static Collection<Map<String, Object>> getMaps(RestockPlanResult generatedRestockPlan) {
        Collection<Map<String, Object>> planAsMaps = new ArrayList<>();
        for (ProductRestockDetail detail : generatedRestockPlan.getProductsToRestock()) {
            Map<String, Object> map = new HashMap<>();
            map.put("productSku", detail.getProductSku());
            map.put("productName", detail.getProductName());
            map.put("restockAmount", detail.getRestockAmount());
            map.put("estimatedCost", detail.getEstimatedCost());
            planAsMaps.add(map);
        }
        return planAsMaps;
    }
}