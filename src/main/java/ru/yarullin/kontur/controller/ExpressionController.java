package ru.yarullin.kontur.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yarullin.kontur.ConverterApplication;
import ru.yarullin.kontur.converter.ExpressionConverter;
import ru.yarullin.kontur.converter.UnitConverter;
import ru.yarullin.kontur.exceptions.ConversionIsNotPossibleException;
import ru.yarullin.kontur.exceptions.UnknownUnitException;
import ru.yarullin.kontur.expression.Expressions;
import ru.yarullin.kontur.expression.ExpressionsJsonBody;
import ru.yarullin.kontur.unit.Unit;

import java.math.BigDecimal;
import java.util.Map;

@RestController
public class ExpressionController {

    public final Map<String, Unit> unitByName = ConverterApplication.unitByNameMap;

    @PostMapping("/convert")
    public ResponseEntity<BigDecimal> getAnswer(@RequestBody ExpressionsJsonBody form){
        BigDecimal result;
        UnitConverter unitConverter = new UnitConverter(unitByName);
        ExpressionConverter expressionConverter = new ExpressionConverter(unitConverter);
         try {
             result = expressionConverter.convert(Expressions.parseExpression(form.getFrom(),unitByName)
                     ,Expressions.parseExpression(form.getTo(),unitByName));
             return new ResponseEntity<>(result,HttpStatus.OK);
         }
         catch (UnknownUnitException e){
             return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
         }
         catch (ConversionIsNotPossibleException e){
             return new ResponseEntity<>(HttpStatus.NOT_FOUND);
         }
    }

}
