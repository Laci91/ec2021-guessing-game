package tippjatek.gamesheet;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import tippjatek.model.Game;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.List;

public class GameSheetManager {

    public Game assembleGameObject() {
        List<List<String>> rawGuesses = new ArrayList<>();
        List<String> players = new ArrayList<>();
        List<String> gameNames = new ArrayList<>();
        Map<Integer, String> matchScores = new HashMap<>();
        List<String> favourites = new ArrayList<>();
        try (FileInputStream stream = new FileInputStream(getExcelFile()); Workbook book = new HSSFWorkbook(stream)) {
            Sheet sheet = book.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();
            Map<String, Integer> playerNameCount = new HashMap<>();

            while (rowIterator.hasNext()) {
                processRow(rowIterator, rawGuesses, players, gameNames, matchScores, playerNameCount, favourites);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return new Game(players, gameNames, matchScores, rawGuesses, favourites);
    }

    public void registerResult(int index, String value) {
        Workbook book;
        try (FileInputStream stream = new FileInputStream(getExcelFile())) {
            book = new HSSFWorkbook(stream);
            Sheet sheet = book.getSheetAt(0);
            Row activeRow = sheet.getRow(index + 2);
            Cell activeCell = activeRow.getCell(1);
            activeCell.setCellValue(value);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        try (FileOutputStream fos = new FileOutputStream(getExcelFile())) {
            book.write(fos);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void changeFavouriteStatus(String playerName) {
        Workbook book;
        try (FileInputStream stream = new FileInputStream(getExcelFile())) {
            book = new HSSFWorkbook(stream);
            Sheet sheet = book.getSheetAt(0);
            Row activeRow = sheet.getRow(0);
            Iterator<Cell> cellIterator = activeRow.cellIterator();
            while(cellIterator.hasNext()) {
                Cell nextCell = cellIterator.next();
                if (playerName.equals(nextCell.getStringCellValue())) {
                    int colIndex = nextCell.getColumnIndex();
                    Cell favCell = sheet.getRow(1).getCell(colIndex);
                    if (StringUtils.isBlank(favCell.getStringCellValue())) {
                        favCell.setCellValue("X");
                    } else {
                        favCell.setCellValue("");
                    }
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        try (FileOutputStream fos = new FileOutputStream(getExcelFile())) {
            book.write(fos);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private File getExcelFile() {
        File excelDataFile = new File(System.getProperty("java.io.tmpdir") + "/excelDataFile.xls");
        if (!excelDataFile.exists()) {
            try {
                Files.copy(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("guesses.xls")), excelDataFile.toPath());
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        return excelDataFile;
    }

    private void processRow(Iterator<Row> rowIterator, List<List<String>> rawGuesses, List<String> players, List<String> gameNames, Map<Integer, String> scores, Map<String, Integer> playerNameCount, List<String> favourites) {
        Row nextRow = rowIterator.next();
        Iterator<Cell> cellIterator = nextRow.cellIterator();

        while(cellIterator.hasNext()) {
            Cell nextCell = cellIterator.next();
            if (isEmptyRow(nextCell)) {
                break;
            }

            if (isPlayerNameRow(nextRow.getRowNum())) {
                if (isInCellWithPlayerName(nextCell) && !isCellEmpty(nextCell)) {
                    initializePlayerData(rawGuesses, players, playerNameCount, nextCell);
                }

                continue;
            }

            if (isFavouriteRow(nextRow.getRowNum())) {
                if (!isCellEmpty(nextCell) && nextCell.getColumnIndex() > 0) {
                    favourites.add(players.get(nextCell.getColumnIndex() - 2));
                }
                continue;
            }

            if (isGameNameColumn(nextCell.getColumnIndex())) {
                gameNames.add(nextCell.getStringCellValue());
                continue;
            }

            if (isMatchResultColumn(nextCell.getColumnIndex()) && !isCellEmpty(nextCell)) {
                scores.put(nextRow.getRowNum() - 2, nextCell.getStringCellValue());
            }

            if (reachedLastColumn(nextCell)) {
                break;
            }

            if (isInCellWithGuess(nextCell)) {
                rawGuesses.get(nextCell.getColumnIndex() - 2).add(nextCell.getStringCellValue());
            }
        }
    }

    private boolean isFavouriteRow(int rowNum) {
        return rowNum == 1;
    }

    private boolean isMatchResultColumn(int columnIndex) {
        return columnIndex == 1;
    }

    private boolean isInCellWithGuess(Cell nextCell) {
        return nextCell.getColumnIndex() > 1;
    }

    private boolean reachedLastColumn(Cell nextCell) {
        return isCellEmpty(nextCell) && nextCell.getColumnIndex() > 1;
    }

    private void initializePlayerData(List<List<String>> rawGuesses, List<String> players, Map<String, Integer> playerNameCount, Cell nextCell) {
        String playerName = nextCell.getStringCellValue();
        players.add(playerName + "#" + playerNameCount.getOrDefault(playerName, 0));
        rawGuesses.add(new ArrayList<>());
        if (!playerNameCount.containsKey(playerName)) {
            playerNameCount.put(playerName, 0);
        }
        playerNameCount.put(playerName, playerNameCount.get(playerName) + 1);
    }

    private boolean isInCellWithPlayerName(Cell nextCell) {
        return nextCell.getColumnIndex() > 1;
    }

    private boolean isCellEmpty(Cell nextCell) {
        return "".equals(nextCell.getStringCellValue());
    }

    private boolean isPlayerNameRow(int rowNum) {
        return rowNum == 0;
    }

    private boolean isGameNameColumn(int colIndex) {
        return colIndex == 0;
    }

    private boolean isEmptyRow(Cell nextCell) {
        return isPlayerNameRow(nextCell.getColumnIndex()) && "".equals(nextCell.getStringCellValue());
    }

    public void openExcelFile() {
        try {
            Desktop.getDesktop().open(getExcelFile());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
