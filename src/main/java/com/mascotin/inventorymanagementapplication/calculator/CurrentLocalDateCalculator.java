package com.mascotin.inventorymanagementapplication.calculator;

import org.openxava.calculators.*;
import java.time.LocalDate;

@SuppressWarnings({"PMD.AtLeastOneConstructor"})
public class CurrentLocalDateCalculator implements ICalculator {

    private static final long serialVersionUID = 1L;

    @Override
    public Object calculate() throws Exception {
        return LocalDate.now();
    }
}