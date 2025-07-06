package com.mascotin.inventorymanagementapplication.pojo;

import com.mascotin.inventorymanagementapplication.model.ProductRestockDetail;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class RestockPlanResult {

    private BigDecimal predictedBudget = BigDecimal.ZERO;

    private List<String> excessAlerts = new ArrayList<>();
    private List<String> criticAlerts = new ArrayList<>();

    private List<ProductRestockDetail> productsToRestock = new ArrayList<>();
}

