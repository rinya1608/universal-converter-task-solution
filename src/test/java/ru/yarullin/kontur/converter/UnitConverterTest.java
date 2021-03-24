package ru.yarullin.kontur.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yarullin.kontur.exceptions.UnknownUnitException;
import ru.yarullin.kontur.unit.Unit;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UnitConverterTest {
    Unit unitSec;
    Unit unitHour;
    Unit unitMin;

    UnitConverter unitConverter;
    @BeforeEach
    void setUp() {
        unitSec = new Unit("с");
        unitHour = new Unit("час");
        unitMin = new Unit("мин");
        unitSec.getCoefficientsByUnit().put(unitMin,new BigDecimal("0.01666666666666667"));
        unitMin.getCoefficientsByUnit().put(unitSec,new BigDecimal("60"));
        unitMin.getCoefficientsByUnit().put(unitHour,new BigDecimal("0.01666666666666667"));
        unitHour.getCoefficientsByUnit().put(unitMin,new BigDecimal("60"));
        Map<String,Unit> unitByName = Map.ofEntries(Map.entry("с",unitSec),Map.entry("час",unitHour)
                ,Map.entry("мин",unitMin));
        unitConverter = new UnitConverter(unitByName);
    }

    @Test
    void convertWhenUnitFromEqualsUnitTo() throws UnknownUnitException {
        assertEquals(unitConverter.convert(unitSec, unitSec).compareTo(new BigDecimal("1")), 0);
    }
    @Test
    void convertWhenUnitFromOrUnitToEqualsNullThrowUnknownUnitException(){
        Throwable thrown = assertThrows(UnknownUnitException.class, () -> {
            unitConverter.convert(unitSec,null);
        });
        assertNotNull(thrown.toString());
    }

    @Test
    void convertWhenUnitFromNotEqualsUnitTo() throws UnknownUnitException {
        assertEquals(unitConverter.convert(unitHour, unitSec).compareTo(new BigDecimal("3600")), 0);
    }
}