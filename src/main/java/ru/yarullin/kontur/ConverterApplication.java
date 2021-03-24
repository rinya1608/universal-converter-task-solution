package ru.yarullin.kontur;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.yarullin.kontur.csv.CsvReader;
import ru.yarullin.kontur.unit.Unit;

import java.io.IOException;
import java.util.Map;

@SpringBootApplication
public class ConverterApplication {
    public static Map<String, Unit> unitByNameMap;
    public static void main(String[] args) {
        try {
            unitByNameMap = CsvReader.readAll(args[0]);
        } catch (IOException e) {
            System.out.println("файл не найден");
        }
        SpringApplication.run(ConverterApplication.class);
    }
}
