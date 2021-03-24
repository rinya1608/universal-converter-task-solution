package ru.yarullin.kontur.mathapi;

import java.math.BigDecimal;
import java.util.Locale;

public class RoundApi {
    public static BigDecimal toSignificantFiguresString(BigDecimal bd, int significantFigures ){
        String test = String.format(Locale.ENGLISH,"%."+significantFigures+"G", bd);
        return new BigDecimal(test);
    }
}
