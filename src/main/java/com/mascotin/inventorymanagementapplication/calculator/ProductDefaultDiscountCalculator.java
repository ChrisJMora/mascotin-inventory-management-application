package com.mascotin.inventorymanagementapplication.calculator;

import org.openxava.calculators.ICalculator;

import java.math.BigDecimal;

public class ProductDefaultDiscountCalculator implements ICalculator {

    @Override
    public Object calculate() throws Exception {
        return BigDecimal.ZERO;
    }
}