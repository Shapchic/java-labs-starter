package edu.course.lab05;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Подготовительная часть: работа с @TempDir, файлами в UTF-8 и исключениями")
class PreparatoryTempDirTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("Создание UTF-8 файла во временном каталоге и чтение через Files.readAllLines")
    void shouldCreateAndReadNumbersFileWithExplicitUtf8() throws IOException {
        Path file = tempDir.resolve("numbers.txt");
        List<String> expectedLines = List.of("10", "bad", "20");

        Files.write(file, expectedLines, StandardCharsets.UTF_8);

        List<String> actualLines = Files.readAllLines(file, StandardCharsets.UTF_8);
        assertEquals(expectedLines, actualLines, "Содержимое прочитанного файла должно совпадать с записанным");
    }

    @Test
    @DisplayName("Преобразование строк обычным циклом: строка 'bad' приводит к NumberFormatException")
    void shouldThrowNumberFormatExceptionWhenParsingBadNumberWithRegularLoop() throws IOException {
        Path file = tempDir.resolve("numbers.txt");
        Files.write(file, List.of("10", "bad", "20"), StandardCharsets.UTF_8);

        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);

        assertThrows(NumberFormatException.class, () -> {
            for (String line : lines) {
                Integer.parseInt(line);
            }
        }, "Попытка разбора нечисловой строки 'bad' должна выбросить NumberFormatException");
    }

    @Test
    @DisplayName("Успешное преобразование валидных строк в целые числа обычным циклом без Stream API")
    void shouldParseValidNumbersWithRegularLoop() throws IOException {
        Path file = tempDir.resolve("valid_numbers.txt");
        Files.write(file, List.of("10", "20", "30"), StandardCharsets.UTF_8);

        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        List<Integer> numbers = new ArrayList<>();
        for (String line : lines) {
            numbers.add(Integer.parseInt(line));
        }

        assertEquals(List.of(10, 20, 30), numbers, "Все валидные строки должны быть успешно преобразованы в числа");
    }
}
