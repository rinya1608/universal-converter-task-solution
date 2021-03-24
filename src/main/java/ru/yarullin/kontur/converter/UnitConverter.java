package ru.yarullin.kontur.converter;

import ru.yarullin.kontur.exceptions.UnknownUnitException;
import ru.yarullin.kontur.unit.Unit;

import java.math.BigDecimal;
import java.util.*;

public class UnitConverter {

    private Map<String, Unit> unitByName;

    public UnitConverter(Map<String, Unit> unitByName) {
        this.unitByName = unitByName;
    }

    public BigDecimal convert(Unit from, Unit to) throws UnknownUnitException {

        BigDecimal coefficient = new BigDecimal(1);

        if (from.equals(to)) return coefficient;
        List<Unit> path = getPath(from,to);
        if (path == null) return null;
        Unit prevUnit = null;
        for (Unit unit : path) {
            if (prevUnit != null){
                coefficient = coefficient.multiply(prevUnit.getCoefficientsByUnit().get(unit));
            }
            prevUnit = unit;
        }

        return coefficient;
    }

    public List<Unit> getPath(Unit from, Unit to) throws UnknownUnitException {

        if (from == null || to == null
                || unitByName.get(from.getName()) == null || unitByName.get(to.getName()) == null){
            throw new UnknownUnitException();
        }

        Unit current = from;
        ArrayDeque<Unit> queue = new ArrayDeque<>();
        ArrayList<Unit> visited = new ArrayList<>();
        HashMap<Unit,List<Unit>> paths = new HashMap<>();

        queue.addLast(current);
        paths.put(current,new ArrayList<>());
        while (!queue.isEmpty()){
            current = queue.pollFirst();
            visited.add(current);
            for (Map.Entry<Unit, BigDecimal> entry :
                    current.getCoefficientsByUnit().entrySet()) {

                if (!visited.contains(entry.getKey())){
                    List<Unit> path = new ArrayList<>(paths.get(current));
                    path.add(current);
                    paths.put(entry.getKey(),path);
                    queue.addLast(entry.getKey());
                    if (entry.getKey().equals(to)){
                        paths.get(to).add(to);
                        return paths.get(to);
                    }
                }

            }
        }
        return null;
    }

    public Map<String, Unit> getUnitByName() {
        return unitByName;
    }

    public void setUnitByName(Map<String, Unit> unitByName) {
        this.unitByName = unitByName;
    }
}
