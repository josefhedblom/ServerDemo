import org.json.JSONArray;


public class Protocol {
    private static final int GET_USERNAME = 0;
    private static final int GET_CATEGORY = 1;
    private static final int GET_DIFFICULTY = 2;
    private static final int CONNECT_DATABASE = 3;
    private static final int ASK_QUESTIONS = 4;
    private static final int CHECK_QUESTION = 5;
    private static final int GAME_OVER = 6;

    private final User user = new User();

    private int state = GET_USERNAME;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private JSONArray questionsList = new JSONArray();
    private Question question;

    public enum Messages {
        ENTER_USERNAME("Ange ett användarnamn: "),
        ENTER_CATEGORY("Ange en kategori: Film | Musik | TV | Brädspel: "),
        ENTER_DIFFICULTY("Ange en svårighetsgrad: Lätt | Medium | Svår: "),
        GREET_USER("Hej %s! Välj en kategori: Film | Musik | TV | Brädspel: "),
        CHOSEN_CATEGORY("Du valde kategorin %s. Ange svårighetsgrad: Lätt | Medium | Svår: "),
        CHOSEN_DIFFICULTY("Svårighetsgrad vald: %s. Startar frågor..."),
        QUESTION("Fråga: %d  %s %s : "),
        RIGHT_ANSWER("Rätt!"),
        WRONG_ANSWER("Fel! Rätt svar är: %s "),
        GAME_DONE("Alla frågor är klara!. Tack för att du spelade, %s! Du fick totalt %d poäng!"),
        ERROR("Ogiltigt tillstånd. Starta om spelet.");

        private final String message;

        Messages(String message) {
            this.message = message;
        }

        public String format(Object... args) {
            return String.format(message, args);
        }
    }


    public String processInput(String input) {
        switch (state) {
            case GET_USERNAME:
                return handelGetUsername(input);
            case GET_CATEGORY:
                return handelGetCategory(input);
            case GET_DIFFICULTY:
                return handelGetDifficulty(input);
            case CONNECT_DATABASE:
                handelConnectDatabase();
            case ASK_QUESTIONS:
                return handelAskQuestions(input);
            case CHECK_QUESTION:
                return handelCheckQuestions(input);
            case GAME_OVER:
                handelGameOver();
            default:
                return Messages.ERROR.format();
        }

    }

    private String handelGetUsername(String input) {
        if (isValid(input)) return Messages.ENTER_USERNAME.format();
        user.setUsername(input);
        state = GET_CATEGORY;
        return Messages.GREET_USER.format(user.getUsername());
    }

    private String handelGetCategory(String input) {
        if (isValid(input)) return Messages.ENTER_CATEGORY.format();
        user.setCategory(input.trim());
        state = GET_DIFFICULTY;
        return Messages.CHOSEN_CATEGORY.format(user.getCategory());
    }

    private String handelGetDifficulty(String input) {
        if (isValid(input)) return Messages.ENTER_DIFFICULTY.format();
        user.setDifficulty(input.trim());
        state = CONNECT_DATABASE;
        return Messages.CHOSEN_DIFFICULTY.format(user.getDifficulty());
    }

    private void handelConnectDatabase() {
        Database db = new Database(user.getCategory(), user.getDifficulty());
        this.questionsList = db.loadJSON().getJSONArray("results");
        state = ASK_QUESTIONS;
    }

    private String handelAskQuestions(String input) {
        if (currentQuestionIndex < questionsList.length()) {
            this.question = new Question(this.questionsList.getJSONObject(currentQuestionIndex));
            currentQuestionIndex++;
            state = CHECK_QUESTION;
            return Messages.QUESTION.format(currentQuestionIndex, question.getQuestion(), question.getOptions());
        } else {
            state = GAME_OVER;
            return Messages.GAME_DONE.format(user.getUsername(), this.score);
        }
    }

    private String handelCheckQuestions(String input) {
        if (input.equalsIgnoreCase(this.question.getCorrectAnswer())) {
            state = ASK_QUESTIONS;
            this.score++;
            this.user.setScore(this.score);
            return Messages.RIGHT_ANSWER.format();
        } else {
            state = ASK_QUESTIONS;
            return Messages.WRONG_ANSWER.format(this.question.getCorrectAnswer());
        }
    }

    private void handelGameOver() {
        System.exit(0);
    }

    private boolean isValid(String input) {
        return input == null || input.trim().isEmpty();
    }
}