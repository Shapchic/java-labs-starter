package edu.course.lab05;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тестирование загрузчика SampleFileLoader")
class SampleFileLoaderTest {

    @TempDir
    Path tempDir;

    private SampleFileLoader loader;

    @BeforeEach
    void setUp() {
        loader = new SampleFileLoader();
    }

    @Test
    @DisplayName("Обязательный тест 1: Чтение полностью корректного файла")
    void shouldLoadValidFileSuccessfully() throws IOException {
        Path file = tempDir.resolve("valid_samples.csv");
        List<String> lines = List.of(
                "id;label;source;featureCount;status",
                "S-101;cat;camera-1;128;READY",
                "S-102;dog;camera-2;64;PROCESSED",
                "S-103;bird;sensor-alpha;32;RAW"
        );
        Files.write(file, lines, StandardCharsets.UTF_8);

        LoadResult result = loader.load(file);

        assertTrue(result.isSuccessful(), "Загрузка корректного файла должна пройти без ошибок");
        assertEquals(3, result.getSamples().size(), "Должно быть прочитано ровно 3 записи");
        assertTrue(result.getErrors().isEmpty(), "Список ошибок должен быть пуст");

        DataSample first = result.getSamples().get(0);
        assertEquals("S-101", first.getId());
        assertEquals("cat", first.getLabel());
        assertEquals("camera-1", first.getSource());
        assertEquals(128, first.getFeatureCount());
        assertEquals(SampleStatus.READY, first.getStatus());
    }

    @Test
    @DisplayName("Обязательный тест 2: Корректная обработка кириллицы и Unicode-символов")
    void shouldPreserveCyrillicAndUnicodeCharacters() throws IOException {
        Path file = tempDir.resolve("unicode_samples.csv");
        List<String> lines = List.of(
                "id;label;source;featureCount;status",
                "S-201;Кот домашний;камера-основная;256;READY",
                "S-202;Собака породы Хаски;датчик №1 (вход);512;PROCESSED",
                "S-203;Робот 🤖;лидар-λ;1024;RAW"
        );
        Files.write(file, lines, StandardCharsets.UTF_8);

        LoadResult result = loader.load(file);

        assertTrue(result.isSuccessful());
        assertEquals(3, result.getSamples().size());

        DataSample sample1 = result.getSamples().get(0);
        assertEquals("Кот домашний", sample1.getLabel());
        assertEquals("камера-основная", sample1.getSource());

        DataSample sample3 = result.getSamples().get(2);
        assertEquals("Робот 🤖", sample3.getLabel());
        assertEquals("лидар-λ", sample3.getSource());
    }

    @Test
    @DisplayName("Обязательный тест 3: Обнаружение пустых обязательных полей")
    void shouldRecordValidationErrorWhenFieldIsEmpty() throws IOException {
        Path file = tempDir.resolve("empty_fields.csv");
        List<String> lines = List.of(
                "id;label;source;featureCount;status",
                ";cat;camera-1;128;READY",          // Пустой id (строка 2)
                "S-102;;camera-2;64;READY",          // Пустая label (строка 3)
                "S-103;dog;;64;READY",               // Пустой source (строка 4)
                "S-104;dog;camera-1;;READY",         // Пустой featureCount (строка 5)
                "S-105;dog;camera-1;128;"            // Пустой status (строка 6)
        );
        Files.write(file, lines, StandardCharsets.UTF_8);

        LoadResult result = loader.load(file);

        assertFalse(result.isSuccessful());
        assertEquals(0, result.getSamples().size());
        assertEquals(5, result.getErrors().size());

        assertEquals(2, result.getErrors().get(0).getLineNumber());
        assertEquals("id", result.getErrors().get(0).getField());

        assertEquals(3, result.getErrors().get(1).getLineNumber());
        assertEquals("label", result.getErrors().get(1).getField());

        assertEquals(4, result.getErrors().get(2).getLineNumber());
        assertEquals("source", result.getErrors().get(2).getField());

        assertEquals(5, result.getErrors().get(3).getLineNumber());
        assertEquals("featureCount", result.getErrors().get(3).getField());

        assertEquals(6, result.getErrors().get(4).getLineNumber());
        assertEquals("status", result.getErrors().get(4).getField());
    }

    @Test
    @DisplayName("Обязательный тест 4: Обнаружение неправильного числового значения")
    void shouldRecordValidationErrorWhenFeatureCountIsNotANumber() throws IOException {
        Path file = tempDir.resolve("invalid_number.csv");
        List<String> lines = List.of(
                "id;label;source;featureCount;status",
                "S-101;cat;camera-1;NaN;READY",         // Строка 2: не число
                "S-102;dog;camera-2;12.5;READY",        // Строка 3: вещественное вместо целого
                "S-103;bird;camera-3;-10;READY"         // Строка 4: отрицательное число
        );
        Files.write(file, lines, StandardCharsets.UTF_8);

        LoadResult result = loader.load(file);

        assertFalse(result.isSuccessful());
        assertEquals(0, result.getSamples().size());
        assertEquals(3, result.getErrors().size());

        assertEquals(2, result.getErrors().get(0).getLineNumber());
        assertEquals("featureCount", result.getErrors().get(0).getField());

        assertEquals(3, result.getErrors().get(1).getLineNumber());
        assertEquals("featureCount", result.getErrors().get(1).getField());

        assertEquals(4, result.getErrors().get(2).getLineNumber());
        assertEquals("featureCount", result.getErrors().get(2).getField());
        assertTrue(result.getErrors().get(2).getMessage().contains("отрицательным"));
    }

    @Test
    @DisplayName("Обязательный тест 5: Обнаружение неизвестного статуса (неверный enum)")
    void shouldRecordValidationErrorWhenStatusIsUnknown() throws IOException {
        Path file = tempDir.resolve("unknown_status.csv");
        List<String> lines = List.of(
                "id;label;source;featureCount;status",
                "S-101;cat;camera-1;128;UNKNOWN_STATE",
                "S-102;dog;camera-2;64;COMPLETED"
        );
        Files.write(file, lines, StandardCharsets.UTF_8);

        LoadResult result = loader.load(file);

        assertFalse(result.isSuccessful());
        assertEquals(0, result.getSamples().size());
        assertEquals(2, result.getErrors().size());

        ValidationError error1 = result.getErrors().get(0);
        assertEquals(2, error1.getLineNumber());
        assertEquals("status", error1.getField());
        assertTrue(error1.getMessage().contains("Неизвестный статус"));

        ValidationError error2 = result.getErrors().get(1);
        assertEquals(3, error2.getLineNumber());
        assertEquals("status", error2.getField());
    }

    @Test
    @DisplayName("Обязательный тест 6: Обнаружение неправильного числа столбцов")
    void shouldRecordValidationErrorWhenColumnCountIsIncorrect() throws IOException {
        Path file = tempDir.resolve("column_count.csv");
        List<String> lines = List.of(
                "id;label;source;featureCount;status",
                "S-101;cat;camera-1;128",                     // Строка 2: 4 столбца (меньше)
                "S-102;dog;camera-2;64;READY;extra_column",    // Строка 3: 6 столбцов (больше)
                "S-103;bird;camera-3;32;RAW"                  // Строка 4: валидная строка
        );
        Files.write(file, lines, StandardCharsets.UTF_8);

        LoadResult result = loader.load(file);

        assertEquals(1, result.getSamples().size());
        assertEquals("S-103", result.getSamples().get(0).getId());

        assertEquals(2, result.getErrors().size());
        assertEquals(2, result.getErrors().get(0).getLineNumber());
        assertEquals("columns", result.getErrors().get(0).getField());

        assertEquals(3, result.getErrors().get(1).getLineNumber());
        assertEquals("columns", result.getErrors().get(1).getField());
    }

    @Test
    @DisplayName("Обязательный тест 7: Совместная обработка корректных и ошибочных строк без прерывания")
    void shouldProcessMixedValidAndInvalidRowsPreservingValidEntries() throws IOException {
        Path file = tempDir.resolve("mixed_dataset.csv");
        List<String> lines = List.of(
                "id;label;source;featureCount;status",
                "S-101;cat;camera-1;128;READY",        // Строка 2: валидна
                "S-102;;camera-2;64;READY",            // Строка 3: ошибка (пустая метка)
                "S-103;dog;camera-3;256;PROCESSED",    // Строка 4: валидна
                "S-104;fox;camera-4;bad_num;RAW",      // Строка 5: ошибка (число)
                "S-105;wolf;camera-5;512;READY"        // Строка 6: валидна
        );
        Files.write(file, lines, StandardCharsets.UTF_8);

        LoadResult result = loader.load(file);

        assertEquals(3, result.getSamples().size(), "Все корректные строки должны быть загружены");
        assertEquals("S-101", result.getSamples().get(0).getId());
        assertEquals("S-103", result.getSamples().get(1).getId());
        assertEquals("S-105", result.getSamples().get(2).getId());

        assertEquals(2, result.getErrors().size(), "Должно быть ровно 2 ошибки");
        assertEquals(3, result.getErrors().get(0).getLineNumber());
        assertEquals("label", result.getErrors().get(0).getField());

        assertEquals(5, result.getErrors().get(1).getLineNumber());
        assertEquals("featureCount", result.getErrors().get(1).getField());
    }

    @Test
    @DisplayName("Обязательный тест 8: Отсутствующий файл выбрасывает DataLoadingException с сохранением исходной причины")
    void shouldThrowDataLoadingExceptionWithPreservedCauseForMissingFile() {
        Path missingPath = tempDir.resolve("non_existent_file.csv");

        DataLoadingException exception = assertThrows(DataLoadingException.class, () ->
                loader.load(missingPath));

        assertTrue(exception.getMessage().contains("Cannot read"), "Сообщение должно содержать 'Cannot read'");
        assertNotNull(exception.getCause(), "Исходная причина (cause) должна быть сохранена");
        assertTrue(exception.getCause() instanceof IOException, "Причина должна быть экземпляром IOException");
    }

    @Test
    @DisplayName("Исключение DataLoadingException при неверном заголовке файла")
    void shouldThrowDataLoadingExceptionWhenHeaderIsInvalid() throws IOException {
        Path file = tempDir.resolve("wrong_header.csv");
        Files.write(file, List.of("id;title;wrong_field;status", "S-1;cat;cam;READY"), StandardCharsets.UTF_8);

        DataLoadingException ex = assertThrows(DataLoadingException.class, () -> loader.load(file));
        assertTrue(ex.getMessage().contains("Неверный заголовок"));
    }

    @Test
    @DisplayName("Исключение DataLoadingException для пустого файла (отсутствует заголовок)")
    void shouldThrowDataLoadingExceptionWhenFileIsEmpty() throws IOException {
        Path file = tempDir.resolve("empty.csv");
        Files.createFile(file);

        DataLoadingException ex = assertThrows(DataLoadingException.class, () -> loader.load(file));
        assertTrue(ex.getMessage().contains("Файл пуст"));
    }

    @Test
    @DisplayName("Исключение IllegalArgumentException при передаче null пути")
    void shouldThrowIllegalArgumentExceptionWhenPathIsNull() {
        assertThrows(IllegalArgumentException.class, () -> loader.load(null));
    }

    @Test
    @DisplayName("Пропуск пустых и пробельных строк между записями")
    void shouldIgnoreBlankAndEmptyLines() throws IOException {
        Path file = tempDir.resolve("with_blanks.csv");
        List<String> lines = List.of(
                "id;label;source;featureCount;status",
                "",
                "S-101;cat;camera-1;128;READY",
                "   ",
                "\t",
                "S-102;dog;camera-2;64;RAW",
                ""
        );
        Files.write(file, lines, StandardCharsets.UTF_8);

        LoadResult result = loader.load(file);

        assertTrue(result.isSuccessful());
        assertEquals(2, result.getSamples().size());
        assertEquals("S-101", result.getSamples().get(0).getId());
        assertEquals("S-102", result.getSamples().get(1).getId());
    }

    @Test
    @DisplayName("Дополнительная часть: поддержка полей в двойных кавычках с разделителем ';' внутри")
    void shouldSupportQuotedFieldsWithSemicolonsInside() throws IOException {
        Path file = tempDir.resolve("quoted_fields.csv");
        List<String> lines = List.of(
                "id;label;source;featureCount;status",
                "S-301;\"wild;cat\";\"camera;front;v2\";128;READY",
                "S-302;\"dog; large\";sensor-1;64;PROCESSED"
        );
        Files.write(file, lines, StandardCharsets.UTF_8);

        LoadResult result = loader.load(file);

        assertTrue(result.isSuccessful());
        assertEquals(2, result.getSamples().size());

        DataSample sample1 = result.getSamples().get(0);
        assertEquals("wild;cat", sample1.getLabel());
        assertEquals("camera;front;v2", sample1.getSource());

        DataSample sample2 = result.getSamples().get(1);
        assertEquals("dog; large", sample2.getLabel());
    }

    @Test
    @DisplayName("Дополнительная часть: поддержка экранированных двойных кавычек (\" \")")
    void shouldSupportEscapedQuotesInsideQuotedFields() throws IOException {
        Path file = tempDir.resolve("escaped_quotes.csv");
        List<String> lines = List.of(
                "id;label;source;featureCount;status",
                "S-303;\"cat \"\"persian\"\"\";camera-1;128;READY"
        );
        Files.write(file, lines, StandardCharsets.UTF_8);

        LoadResult result = loader.load(file);

        assertTrue(result.isSuccessful());
        assertEquals(1, result.getSamples().size());
        assertEquals("cat \"persian\"", result.getSamples().get(0).getLabel());
    }

    @Test
    @DisplayName("Дополнительная часть: строка с незакрытой кавычкой порождает ValidationError")
    void shouldHandleUnclosedQuotesAsRowValidationError() throws IOException {
        Path file = tempDir.resolve("unclosed_quote.csv");
        List<String> lines = List.of(
                "id;label;source;featureCount;status",
                "S-304;\"unclosed label;camera-1;128;READY"
        );
        Files.write(file, lines, StandardCharsets.UTF_8);

        LoadResult result = loader.load(file);

        assertFalse(result.isSuccessful());
        assertEquals(0, result.getSamples().size());
        assertEquals(1, result.getErrors().size());
        assertEquals(2, result.getErrors().get(0).getLineNumber());
        assertEquals("row", result.getErrors().get(0).getField());
        assertTrue(result.getErrors().get(0).getMessage().contains("незакрытые"));
    }
}
