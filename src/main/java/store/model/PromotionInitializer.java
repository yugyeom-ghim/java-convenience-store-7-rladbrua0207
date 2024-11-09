package store.model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

public class PromotionInitializer {

    private static final String ERROR_FILE_NOT_FOUND_MESSAGE = "[ERROR] 파일이 존재하지 않습니다.";

    private static final String PROMOTIONS_RESOURCES_FILE_NAME = "promotions.md";
    private static final String DELIMITER_COMMA = ",";
    private static final int NAME_INDEX = 0;
    private static final int BUY_COUNT_INDEX = 1;
    private static final int GET_COUNT_INDEX = 2;
    private static final int START_DATE_INDEX = 3;
    private static final int END_DATE_INDEX = 4;

    private final LinkedHashMap<String, Promotion> promotionMap = new LinkedHashMap<>();

    public void initPromotions() throws IOException {
        BufferedReader bufferedReader = initBufferedReader();
        ignoreFirstLine(bufferedReader);

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            initPromotion(line);
        }
    }

    private static BufferedReader initBufferedReader() throws IOException {
        BufferedReader bufferedReader;
        try {
            ClassLoader loader = Stock.class.getClassLoader();
            FileInputStream file = new FileInputStream(
                    Objects.requireNonNull(loader.getResource(PROMOTIONS_RESOURCES_FILE_NAME)).getFile());
            bufferedReader = new BufferedReader(new InputStreamReader(file));
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(ERROR_FILE_NOT_FOUND_MESSAGE);
        }
        return bufferedReader;
    }

    private void ignoreFirstLine(BufferedReader bufferedReader) throws IOException {
        bufferedReader.readLine();
    }

    private void initPromotion(String line) {
        List<String> promotionOptions = Arrays.stream(line.split(DELIMITER_COMMA)).toList();

        String name = promotionOptions.get(NAME_INDEX);
        int buyCount = Integer.parseInt(promotionOptions.get(BUY_COUNT_INDEX));
        int getCount = Integer.parseInt(promotionOptions.get(GET_COUNT_INDEX));
        LocalDate startDate = LocalDate.parse(promotionOptions.get(START_DATE_INDEX));
        LocalDate endDate = LocalDate.parse(promotionOptions.get(END_DATE_INDEX));

        promotionMap.put(name, new Promotion(name, buyCount, getCount, startDate, endDate));
    }

    public LinkedHashMap<String, Promotion> getPromotionMap() {
        return promotionMap;
    }
}
