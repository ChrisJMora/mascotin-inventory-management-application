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

@SuppressWarnings({"PMD.AtLeastOneConstructor", "PMD.LawOfDemeter",
        "PMD.AvoidInstantiatingObjectsInLoops", "PMD.ConsecutiveLiteralAppends"})
public class GenerateRestockPlanAction extends ViewBaseAction {

    @Override
    public void execute() throws Exception {
        final BigDecimal budget = (BigDecimal) getView().getValue("presupuesto");
        final RestockPlanResult generatedPlan = RestockInventoryPlanGenerator.generatePlan(budget);

        final Collection<Map<String, Object>> planAsMaps = getMaps(generatedPlan);
        getView().setValue("productsToRestock", planAsMaps);

        final StringBuilder message = new StringBuilder(40);
        message.append("Presupuesto utilizado: ")
                .append(generatedPlan.getPredictedBudget()).append("\n\n");

        if (!generatedPlan.getProductsToRestock().isEmpty()) {
            message.append("Productos sugeridos:\n");
            for (final ProductRestockDetail item : generatedPlan.getProductsToRestock()) {
                message.append("- ")
                        .append(item.getProductName())
                        .append(' ').append('(').append(item.getRestockAmount()).append(" unidades").append(')')
                        .append(" → $")
                        .append(item.getEstimatedCost())
                        .append('\n');
            }
        }

        if (!generatedPlan.getCriticAlerts().isEmpty()) {
            message.append('\n')
                    .append(" Productos críticos NO reponibles:")
                    .append('\n');
            for (final String alert : generatedPlan.getCriticAlerts()) {
                message.append("- ").append(alert).append('\n');
            }
        }

        if (!generatedPlan.getExcessAlerts().isEmpty()) {
            message.append('\n')
                    .append(" Excesos de inventario detectados:")
                    .append('\n');
            for (final String excess : generatedPlan.getExcessAlerts()) {
                message.append("- ").append(excess).append('\n');
            }
        }

        addMessage(message.toString());
    }

    private static Collection<Map<String, Object>> getMaps(final RestockPlanResult generatedPlan) {
        final Collection<Map<String, Object>> planAsMaps = new ArrayList<>();
        for (final ProductRestockDetail detail : generatedPlan.getProductsToRestock()) {
            final Map<String, Object> map = new HashMap<>();
            map.put("productSku", detail.getProductSku());
            map.put("productName", detail.getProductName());
            map.put("restockAmount", detail.getRestockAmount());
            map.put("estimatedCost", detail.getEstimatedCost());
            planAsMaps.add(map);
        }
        return planAsMaps;
    }
}