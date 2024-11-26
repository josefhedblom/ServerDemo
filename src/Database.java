import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Database {
    private final String category;
    private final String difficulty;

    public Database(String category,String difficulty) {
        this.category = Category.getCategory(category);
        this.difficulty = Difficulty.getDifficulty(difficulty);
    }

    // public static loadAllJSON hämta alla filer

    public JSONObject loadJSON() {
        String filePath = buildFilePath(category, difficulty);
        try {
            return new JSONObject(new String(Files.readAllBytes(Paths.get(filePath))));
        } catch (IOException e) {
            throw new RuntimeException("Kunde inte läsa filen: " + filePath, e);
        }
    }

    public static String buildFilePath(String category, String difficulty) {
        return category + "_" + difficulty + ".json";
    }
}
