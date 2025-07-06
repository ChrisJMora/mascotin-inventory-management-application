package com.mascotin.inventorymanagementapplication.calculator;

import org.openxava.calculators.ICalculator;

import java.math.BigDecimal;

@SuppressWarnings({"PMD.AtLeastOneConstructor"})
public class ProductDefaultDiscountCalculator implements ICalculator {

    private static final long serialVersionUID = 1L;

    @Override
    public Object calculate() throws Exception {
        return BigDecimal.ZERO;
    }
}