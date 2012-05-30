package net.intelie.lognit.cli.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;

public class AggregatedItem extends LinkedHashMap<String, Object> {

    @Override
    public Object put(String s, Object o) {
        if (o instanceof Double)
            o = new BigDecimal(new DecimalFormat("#.######").format(o));
        return super.put(s, o);
    }
}
