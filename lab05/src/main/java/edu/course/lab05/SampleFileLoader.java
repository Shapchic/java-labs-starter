package edu.course.lab05;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SampleFileLoader {

    public static final String EXPECTED_HEADER = "id;label;source;featureCount;status";
    public static final int EXPECTED_COLUMN_COUNT = 5;

    //Загружает данные из файла по указанному пути.
    public LoadResult load(Path path) {
        if (path == null) {
            throw new IllegalArgumentException("Путь к файлу (path) не может быть null");
        }

        List<DataSample> samples = new ArrayList<>();
        List<ValidationError> errors = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String headerLine = reader.readLine();
            if (headerLine == null) {
                throw new DataLoadingException("Файл пуст: отсутствует обязательный заголовок (" + path + ")");
            }

            //Удаляем возможный BOM-маркер UTF-8 в начале файла.
            if (headerLine.startsWith("\uFEFF")) {
                headerLine = headerLine.substring(1);
            }

            if (!isValidHeader(headerLine)) {
                throw new DataLoadingException("Неверный заголовок файла. Ожидалось: '" +
                        EXPECTED_HEADER + "', получено: '" + headerLine + "'");
            }

            String line;
            int lineNumber = 1;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                //Пустые строки пропускаются.
                if (line.trim().isEmpty()) {
                    continue;
                }

                parseAndValidateLine(line, lineNumber, samples, errors);
            }

        } catch (IOException | SecurityException e) {
            throw new DataLoadingException("Cannot read " + path, e);
        }

        return new LoadResult(samples, errors);
    }

    //Проверяет соответствие заголовка ожидаемому формату.
    public boolean isValidHeader(String header) {
        if (header == null) {
            return false;
        }
        String clean = header.trim();
        if (clean.startsWith("\uFEFF")) {
            clean = clean.substring(1).trim();
        }
        return EXPECTED_HEADER.equalsIgnoreCase(clean);
    }

    //Разбирает строку на список полей с поддержкой кавычек и разделителей внутри них.
    public List<String> parseRow(String line) {
        if (line == null) {
            return null;
        }

        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        int length = line.length();

        for (int i = 0; i < length; i++) {
            char c = line.charAt(i);

            if (inQuotes) {
                if (c == '"') {
                    if (i + 1 < length && line.charAt(i + 1) == '"') {
                        current.append('"');
                        i++; //Пропускаем экранированную кавычку.
                    } else {
                        inQuotes = false;
                    }
                } else {
                    current.append(c);
                }
            } else {
                if (c == '"') {
                    inQuotes = true;
                } else if (c == ';') {
                    fields.add(current.toString().trim());
                    current.setLength(0);
                } else {
                    current.append(c);
                }
            }
        }

        if (inQuotes) {
            return null; //Незакрытые кавычки в строке.
        }

        fields.add(current.toString().trim());
        return fields;
    }

    //Выполняет синтаксический разбор и валидацию полей одной строки.
    private void parseAndValidateLine(String line, int lineNumber, List<DataSample> samples, List<ValidationError> errors) {
        List<String> fields = parseRow(line);
        if (fields == null) {
            errors.add(new ValidationError(lineNumber, "row", "Некорректный синтаксис строки: незакрытые двойные кавычки"));
            return;
        }

        if (fields.size() != EXPECTED_COLUMN_COUNT) {
            errors.add(new ValidationError(lineNumber, "columns",
                    "Неверное число столбцов: ожидалось " + EXPECTED_COLUMN_COUNT + ", получено " + fields.size()));
            return;
        }

        String id = fields.get(0);
        String label = fields.get(1);
        String source = fields.get(2);
        String featureCountStr = fields.get(3);
        String statusStr = fields.get(4);

        boolean hasError = false;

        if (id.isEmpty()) {
            errors.add(new ValidationError(lineNumber, "id", "Поле id не должно быть пустым"));
            hasError = true;
        }

        if (label.isEmpty()) {
            errors.add(new ValidationError(lineNumber, "label", "Поле label не должно быть пустым"));
            hasError = true;
        }

        if (source.isEmpty()) {
            errors.add(new ValidationError(lineNumber, "source", "Поле source не должно быть пустым"));
            hasError = true;
        }

        int featureCount = -1;
        if (featureCountStr.isEmpty()) {
            errors.add(new ValidationError(lineNumber, "featureCount", "Поле featureCount не должно быть пустым"));
            hasError = true;
        } else {
            try {
                featureCount = Integer.parseInt(featureCountStr);
                if (featureCount < 0) {
                    errors.add(new ValidationError(lineNumber, "featureCount",
                            "Количество признаков не может быть отрицательным: " + featureCount));
                    hasError = true;
                }
            } catch (NumberFormatException e) {
                errors.add(new ValidationError(lineNumber, "featureCount",
                        "Неверный формат числа в featureCount: '" + featureCountStr + "'"));
                hasError = true;
            }
        }

        SampleStatus status = null;
        if (statusStr.isEmpty()) {
            errors.add(new ValidationError(lineNumber, "status", "Поле status не должно быть пустым"));
            hasError = true;
        } else {
            try {
                status = SampleStatus.valueOf(statusStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                errors.add(new ValidationError(lineNumber, "status",
                        "Неизвестный статус: '" + statusStr + "'"));
                hasError = true;
            }
        }

        if (!hasError) {
            try {
                DataSample sample = new DataSample(id, label, source, featureCount, status);
                samples.add(sample);
            } catch (IllegalArgumentException e) {
                errors.add(new ValidationError(lineNumber, "domain", e.getMessage()));
            }
        }
    }
}
