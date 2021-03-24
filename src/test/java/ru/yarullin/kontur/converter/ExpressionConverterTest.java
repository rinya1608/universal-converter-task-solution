package ru.yarullin.kontur.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yarullin.kontur.exceptions.ConversionIsNotPossibleException;
import ru.yarullin.kontur.exceptions.UnknownUnitException;
import ru.yarullin.kontur.expression.Expression;
import ru.yarullin.kontur.unit.Unit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionConverterTest {
    Unit unitSec;
    Unit unitHour;
    Unit unitMin;
    Unit unitM;
    Unit unitMm;
    Unit unitSm;
    Unit unitKm;
    Expression from;
    Expression to;

    UnitConverter unitConverter;
    ExpressionConverter expressionConverter;
    @BeforeEach
    void setUp() {
        unitSec = new Unit("с");
        unitHour = new Unit("час");
        unitMin = new Unit("мин");
        unitM = new Unit("м");;
        unitMm = new Unit("мм");;
        unitSm = new Unit("см");;
        unitKm = new Unit("км");;
        unitSec.getCoefficientsByUnit().put(unitMin,new BigDecimal("0.01666666666666667"));
        unitMin.getCoefficientsByUnit().put(unitSec,new BigDecimal("60"));
        unitMin.getCoefficientsByUnit().put(unitHour,new BigDecimal("0.01666666666666667"));
        unitHour.getCoefficientsByUnit().put(unitMin,new BigDecimal("60"));
        unitMm.getCoefficientsByUnit().put(unitM,new BigDecimal("0.001"));
        unitKm.getCoefficientsByUnit().put(unitM,new BigDecimal("1000"));
        unitSm.getCoefficientsByUnit().put(unitM,new BigDecimal("0.01"));
        unitM.getCoefficientsByUnit().put(unitKm,new BigDecimal("0.001"));
        unitM.getCoefficientsByUnit().put(unitSm,new BigDecimal("100"));
        unitM.getCoefficientsByUnit().put(unitMm,new BigDecimal("1000"));
        Map<String,Unit> unitByName = Map.ofEntries(Map.entry("с",unitSec),Map.entry("час",unitHour)
                ,Map.entry("мин",unitMin),Map.entry("м",unitM),Map.entry("мм",unitMm)
                ,Map.entry("см",unitSm),Map.entry("км",unitKm));
        unitConverter = new UnitConverter(unitByName);
        expressionConverter = new ExpressionConverter(unitConverter);
    }

    @Test
    void convertExpressionWithSingleUnitToExpressionWithSingleUnit()
            throws ConversionIsNotPossibleException, UnknownUnitException {

        from = new Expression(new ArrayList<>(Arrays.asList(unitHour)),new ArrayList<>());
        to = new Expression(new ArrayList<>(Arrays.asList(unitSec)),new ArrayList<>());
        assertEquals(expressionConverter.convert(from, to).compareTo(new BigDecimal("3600")), 0);
    }
    @Test
    void convertSameSizedExpressions()
            throws ConversionIsNotPossibleException, UnknownUnitException {

        from = new Expression(new ArrayList<>(Arrays.asList(unitM)),new ArrayList<>(Arrays.asList(unitSec)));
        to = new Expression(new ArrayList<>(Arrays.asList(unitKm)),new ArrayList<>(Arrays.asList(unitHour)));
        System.out.println(expressionConverter.convert(from, to));
        assertEquals(expressionConverter.convert(from, to).compareTo(new BigDecimal("3.6")), 0);
    }
    @Test
    void convertExpressionsWhereSizeOfToIsLargerThenFrom()
            throws ConversionIsNotPossibleException, UnknownUnitException {

        from = new Expression(new ArrayList<>(Arrays.asList(unitM)),new ArrayList<>());
        to = new Expression(new ArrayList<>(Arrays.asList(unitKm,unitSec)),new ArrayList<>(Arrays.asList(unitHour)));
        assertEquals(expressionConverter.convert(from, to).compareTo(new BigDecimal("3.6")), 0);
    }
    @Test
    void convertExpressionToDimensionlessValue()
            throws ConversionIsNotPossibleException, UnknownUnitException {

        from = new Expression(new ArrayList<>(Arrays.asList(unitM)),new ArrayList<>());
        assertEquals(expressionConverter.convert(from, null).compareTo(new BigDecimal("1")), 0);
    }
    @Test
    void convertExpressionsMinusTheOneDegree()
            throws ConversionIsNotPossibleException, UnknownUnitException {

        from = new Expression(new ArrayList<>(),new ArrayList<>(Arrays.asList(unitSec)));
        to = new Expression(new ArrayList<>(),new ArrayList<>(Arrays.asList(unitHour)));
        assertEquals(expressionConverter.convert(from, to).compareTo(new BigDecimal("3600")), 0);
    }
    @Test
    void convertExpressionsThrowConversionIsNotPossibleException(){

        from = new Expression(new ArrayList<>(Arrays.asList(unitM)),new ArrayList<>());
        to = new Expression(new ArrayList<>(Arrays.asList(unitKm)),new ArrayList<>(Arrays.asList(unitHour)));
        Throwable thrown = assertThrows(ConversionIsNotPossibleException.class, () -> {
            expressionConverter.convert(from,to);
        });
        assertNotNull(thrown.toString());
    }
    @Test
    void convertExpressionsThrowUnknownUnitException(){

        from = new Expression(new ArrayList<>(Arrays.asList(unitM,new Unit("none"))),new ArrayList<>());
        to = new Expression(new ArrayList<>(Arrays.asList(unitKm)),new ArrayList<>(Arrays.asList(unitHour)));
        Throwable thrown = assertThrows(UnknownUnitException.class, () -> {
            expressionConverter.convert(from,to);
        });
        assertNotNull(thrown.toString());
    }
}