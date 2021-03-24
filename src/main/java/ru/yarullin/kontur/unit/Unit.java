package ru.yarullin.kontur.unit;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Unit {
    private String name;
    private Map<Unit, BigDecimal> coefficientsByUnit;

    public Unit(String s) {
        this.name = s;
        coefficientsByUnit = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public Map<Unit, BigDecimal> getCoefficientsByUnit() {
        return coefficientsByUnit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unit that = (Unit) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
