package ru.yarullin.kontur.expression;

import ru.yarullin.kontur.unit.Unit;

import java.util.ArrayList;
import java.util.List;

public class Expression{
    private List<Unit> numerator;
    private List<Unit> denominator;

    public Expression(List<Unit> numerator, List<Unit> denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public int compareNumeratorSizeTo(Expression expression){
        return Integer.compare(numerator.size(), expression.numerator.size());
    }
    public int compareDenominatorSizeTo(Expression expression){
        return Integer.compare(denominator.size(), expression.denominator.size());
    }


    public List<Unit> getAllUnits(){
        List<Unit> allUnits = new ArrayList<>(getNumerator());
        if (getDenominator() != null) allUnits.addAll(getDenominator());
        return allUnits;
    }
    public List<Unit> getNumerator() {
        return numerator;
    }

    public void setNumerator(List<Unit> numerator) {
        this.numerator = numerator;
    }

    public List<Unit> getDenominator() {
        return denominator;
    }

    public void setDenominator(List<Unit> denominator) {
        this.denominator = denominator;
    }

}
