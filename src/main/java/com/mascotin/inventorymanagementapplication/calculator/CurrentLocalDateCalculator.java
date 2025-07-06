package com.mascotin.inventorymanagementapplication.calculator;

import org.openxava.calculators.*;
import java.time.LocalDate;

public class CurrentLocalDateCalculator implements ICalculator {
    @Override
    public Object calculate() throws Exception {
        return LocalDate.now();
    }
}