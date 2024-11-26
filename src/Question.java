import org.json.JSONArray;
import org.json.JSONObject;

public class Question {
    private final String type;
    private final String difficulty;
    private final String category;
    private final String question;
    private final String correctAnswer;
    private final JSONArray incorrectAnswers;
    private final JSONArray options;

    public Question(JSONObject jsonObject) {
        this.type = jsonObject.getString("type");
        this.difficulty = jsonObject.getString("difficulty");
        this.category = jsonObject.getString("category");
        this.question = jsonObject.getString("question");
        this.correctAnswer = jsonObject.getString("correct_answer");
        this.incorrectAnswers = jsonObject.getJSONArray("incorrect_answers");
        this.options = jsonObject.getJSONArray("options");
    }

    public String getType() {
        return type;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getCategory() {
        return category;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public JSONArray getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public JSONArray getOptions() {
        return options;
    }

    @Override
    public String toString() {
        return "Server.Question{" +
                "type='" + type + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", category='" + category + '\'' +
                ", question='" + question + '\'' +
                ", options='" + options + '\'' +
                ", correct_answer='" + correctAnswer + '\'' +
                ", incorrect_answers=" + incorrectAnswers +
                '}';
    }
}
