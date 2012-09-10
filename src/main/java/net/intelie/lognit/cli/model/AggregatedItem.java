package net.intelie.lognit.cli.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.LinkedHashMap;
import java.util.Locale;

public class AggregatedItem extends LinkedHashMap<String, Object> {
    public static final DecimalFormatSymbols SYMBOLS = new DecimalFormatSymbols(Locale.ENGLISH);

    @Override
    public Object put(String s, Object o) {
        if (o instanceof Double)
            o = new BigDecimal(new DecimalFormat("#.######", SYMBOLS).format(o));
        return super.put(s, o);
    }
}
