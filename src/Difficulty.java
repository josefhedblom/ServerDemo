import java.util.HashMap;
import java.util.Map;

public class Difficulty {
    private static final Map<String, String> DIFFICULTY_LEVEL = new HashMap<>();

    static {
        DIFFICULTY_LEVEL.put("Lätt", "easy");
        DIFFICULTY_LEVEL.put("Medium", "medium");
        DIFFICULTY_LEVEL.put("Svår", "hard");
    }

    // public static getDifficulties hämta all svårighetsnivåer


    public static String getDifficulty(String difficulty) {
        String path = DIFFICULTY_LEVEL.get(difficulty.trim());
        if (path.isEmpty()) {
            throw new IllegalArgumentException("Ogiltig nivå: " + difficulty);
        }
        return path;
    }
}
