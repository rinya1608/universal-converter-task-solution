package ru.yarullin.kontur.csv;

import ru.yarullin.kontur.unit.Unit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;

public class CsvReader {

    public static Map<String, Unit> readAll(String path) throws IOException {
        Map<String, Unit> unitByName = new HashMap<>();
        unitByName.put("1",new Unit("1"));
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String row;
        String[] elements;
        Unit from;
        Unit to;
        while ((row = reader.readLine()) != null){
            row = row.replaceAll("\"","");
            elements = row.split(",");

            if (unitByName.get(elements[0]) == null){
                from = new Unit(elements[0]);
                unitByName.put(elements[0],from);
            }
            else from = unitByName.get(elements[0]);

            if (unitByName.get(elements[1]) == null){
                to = new Unit(elements[1]);
                unitByName.put(elements[1],to);
            }
            else to = unitByName.get(elements[1]);

            to.getCoefficientsByUnit()
                    .put(from,new BigDecimal("1")
                            .divide(new BigDecimal(elements[2]), new MathContext(16)));

            from.getCoefficientsByUnit().put(to, new BigDecimal(elements[2]));
        }
        reader.close();
        return unitByName;
    }

}
