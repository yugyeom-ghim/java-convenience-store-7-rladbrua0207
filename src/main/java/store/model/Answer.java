package store.model;

import java.util.Arrays;

public enum Answer {
    YES("Y"),
    NO("N");

    private static final String ERROR_INVALID_INPUT_MESSAGE = "[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.";

    private final String value;

    Answer(String value) {
        this.value = value;
    }

    public static Answer from(String input) {
        return Arrays.stream(values())
                .filter(answer -> answer.value.equals(input))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(ERROR_INVALID_INPUT_MESSAGE));
    }

    public boolean isYes() {
        return this == YES;
    }
}
