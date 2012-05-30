package net.intelie.lognit.cli.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.LinkedHashMap;

public class AggregatedItem extends LinkedHashMap<String, Object> {
    private static final MathContext MATH = MathContext.DECIMAL64;

    @Override
    public Object put(String s, Object o) {
        if (o instanceof Double)
            o = new BigDecimal((Double)o, MATH).stripTrailingZeros();
        return super.put(s, o);
    }
}
