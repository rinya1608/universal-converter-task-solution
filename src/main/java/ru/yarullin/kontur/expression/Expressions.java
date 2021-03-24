package ru.yarullin.kontur.expression;

import ru.yarullin.kontur.converter.UnitConverter;
import ru.yarullin.kontur.exceptions.ConversionIsNotPossibleException;
import ru.yarullin.kontur.exceptions.UnknownUnitException;
import ru.yarullin.kontur.unit.Unit;

import java.util.*;
import java.util.stream.Collectors;

public class Expressions {
    public static boolean allUnitsInExpressionsExists(Map<String, Unit> unitByName, Expression... expressions){

        List<Unit> allUnitInExpressions = new ArrayList<>();
        for (Expression expression :
                expressions) {
            if (expression != null) allUnitInExpressions.addAll(expression.getAllUnits());
        }

        for (Unit unit : allUnitInExpressions) {
            if (unit == null || unitByName.get(unit.getName()) == null) return false;
        }
        return true;
    }

    public static Expression parseExpression(String s, Map<String, Unit> source)
            throws ConversionIsNotPossibleException {
        List<Unit> numerator = new ArrayList<>();
        List<Unit> denominator = new ArrayList<>();
        if (s.isEmpty()) return null;
        String[] fractionMembers = s.split("/");



        if (fractionMembers.length > 2){
            throw new ConversionIsNotPossibleException();
        }
        else{
            if (!(fractionMembers[0].length() == 1
                    && Character.isDigit(fractionMembers[0].charAt(0))
                    && fractionMembers[0].equals("1"))){
                numerator = Arrays.stream(fractionMembers[0]
                        .split("\\*"))
                        .map(source::get)
                        .collect(Collectors.toList());
            }

            if (fractionMembers.length == 1) return new Expression(numerator,denominator);

            denominator = Arrays.stream(fractionMembers[1]
                    .split("\\*"))
                    .map(source::get)
                    .collect(Collectors.toList());

            return new Expression(numerator, denominator);
        }

    }

    public static void alignExpressions(Expression from, Expression to, UnitConverter unitConverter)
            throws UnknownUnitException, ConversionIsNotPossibleException {
        Iterator<Unit> numeratorIter = to.getNumerator().iterator();
        while (numeratorIter.hasNext()
                && (from.compareNumeratorSizeTo(to) != 0 && from.compareDenominatorSizeTo(to) != 0)){
            Unit numeratorUnit = numeratorIter.next();
            for (Unit unit :
                    to.getDenominator()) {
                if (unitConverter.convert(numeratorUnit,unit) != null){
                    System.out.println(numeratorUnit.getName());
                    from.getDenominator().add(numeratorUnit);
                    numeratorIter.remove();
                    break;
                }
            }
        }

        if (from.compareNumeratorSizeTo(to) == -1 || from.compareDenominatorSizeTo(to) == -1){
            throw new ConversionIsNotPossibleException();
        }
    }
}
