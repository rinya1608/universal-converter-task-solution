package ru.yarullin.kontur.converter;

import ru.yarullin.kontur.exceptions.ConversionIsNotPossibleException;
import ru.yarullin.kontur.exceptions.UnknownUnitException;
import ru.yarullin.kontur.expression.Expression;
import ru.yarullin.kontur.expression.Expressions;
import ru.yarullin.kontur.mathapi.RoundApi;
import ru.yarullin.kontur.unit.Unit;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class ExpressionConverter {
    private UnitConverter unitConverter;

    public ExpressionConverter(UnitConverter unitConverter) {
        this.unitConverter = unitConverter;
    }

    public BigDecimal convert(Expression from, Expression to)
            throws UnknownUnitException, ConversionIsNotPossibleException {

        BigDecimal numeratorResult;
        BigDecimal denominatorResult;
        BigDecimal result;

        if (from == null) throw new UnknownUnitException();

        if (!Expressions.allUnitsInExpressionsExists(unitConverter.getUnitByName(),from,to)){
            throw new UnknownUnitException();
        }

        if (to == null) {
            return convertToDimensionlessValue(from);
        }


        if (from.compareNumeratorSizeTo(to) == -1 || from.compareDenominatorSizeTo(to) == -1) {
            Expressions.alignExpressions(from, to,unitConverter);
        }

        numeratorResult = convertUnitList(from.getNumerator(), to.getNumerator());
        if (from.getDenominator() == null && to.getDenominator() == null) {
            return numeratorResult.round(new MathContext(15));
        }

        denominatorResult = convertUnitList(from.getDenominator(), to.getDenominator());

        result = RoundApi.toSignificantFiguresString(numeratorResult
                .divide(denominatorResult,16 ,RoundingMode.HALF_UP),15);

        return new BigDecimal(result.stripTrailingZeros().toPlainString());
    }



    public BigDecimal convertToDimensionlessValue(Expression from)
            throws ConversionIsNotPossibleException, UnknownUnitException {

        if (from.getDenominator() == null || from.getDenominator().isEmpty()){
            if (from.getNumerator().size() == 1) return new BigDecimal(1);
        }
        return convertUnitList(from.getNumerator(), from.getDenominator());
    }


    public BigDecimal convertUnitList(List<Unit> from, List<Unit> to)
            throws UnknownUnitException, ConversionIsNotPossibleException {
        ArrayList<Integer> visitedIndex = new ArrayList<>();
        BigDecimal result = new BigDecimal("1");
        BigDecimal value;
        boolean isConverted;

        for (Unit unitFrom : from) {
            isConverted = false;
            for (int indexOfListTo = 0; indexOfListTo < to.size(); indexOfListTo++) {
                if (!visitedIndex.contains(indexOfListTo)) {
                    value = unitConverter.convert(unitFrom, to.get(indexOfListTo));
                    if (value != null) {
                        isConverted = true;
                        result = result.multiply(value);
                        visitedIndex.add(indexOfListTo);
                        break;
                    }
                }
            }
            if (!isConverted) throw new ConversionIsNotPossibleException();
        }
        return result;
    }

    public UnitConverter getUnitConverter() {
        return unitConverter;
    }

    public void setUnitConverter(UnitConverter unitConverter) {
        this.unitConverter = unitConverter;
    }
}
