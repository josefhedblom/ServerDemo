import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Scanner;

public class Demo {
    public Demo() {
        Scanner input = new Scanner(System.in);
        String choice = CapitalizeFirstLetter.capitalize(input.nextLine());
        System.out.println("Välj nivå på kategori: Lätt | Medium | Svår");
        String difficulty = CapitalizeFirstLetter.capitalize(input.nextLine());
        boolean running = true;
        while (running) {
            switch (choice) {
                case "Film":
                    getCategory("Film",difficulty);
                    running = false;
                    break;
                case "Musik":
                    getCategory("Musik",difficulty);
                    running = false;
                    break;
                case "Tv":
                    getCategory("TV",difficulty);
                    running = false;
                    break;
                case "Brädspel":
                    getCategory("Brädspel",difficulty);
                    running = false;
                    break;
                default:
                    System.out.println("Välj en kategori: Film | Musik | TV | Brädspel");
                    break;
            }
        }

    }

    public static void getCategory(String category, String difficulty){
        Database db = new Database(category, difficulty);
        JSONArray results = db.loadJSON().getJSONArray("results");

        Scanner input = new Scanner(System.in);

        for (int i = 0; i < results.length(); i++) {
            JSONObject result = results.getJSONObject(i);
            Question question = new Question(result);

            System.out.println("Fråga: " + question.getQuestion());
            System.out.println("Alternativ: " + question.getOptions());

            System.out.print("Ditt svar: ");
            String choice = input.nextLine();

            if (choice.equalsIgnoreCase(question.getCorrectAnswer())) {
                System.out.println("Rätt!");
            } else {
                System.out.println("Fel! Rätt svar är: " + question.getCorrectAnswer());
            }
        }
    }

    public class CapitalizeFirstLetter {
        public static String capitalize(String input) {
            if (input == null || input.isEmpty()) {
                return input;
            }
            return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
        }
    }

    public static void main(String[] args) {
        new Demo();
    }
}
