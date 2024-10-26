package kata_quiz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * 
 */
public class KataQuiz {

	private static final String quizURL = "https://opentdb.com/api.php?amount=10&category=9&difficulty=easy&type=multiple";

	public static void main(String[] args) {
		// ask user what category

		// ask user what difficulty

		// count of correct answers

		// display score

		// play again?

		try {
			String jsonResponse = fetchTriviaQuestions();
			parseAndDisplayQuestions(jsonResponse);
		} catch (IOException | URISyntaxException e) {
			System.out.println("Error fetching trivia questions: " + e.getMessage());
		}

	}

	public static String fetchTriviaQuestions() throws IOException, URISyntaxException {
		String urlQuiz = "https://opentdb.com/api.php?amount=1&category=9&difficulty=easy&type=multiple";

		URI uri = new URI(urlQuiz);
		URL url = uri.toURL();
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");

		int responseCode = connection.getResponseCode();
		if (responseCode != HttpURLConnection.HTTP_OK) {
			throw new IOException("HTTP error code: " + responseCode);
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		StringBuilder response = new StringBuilder();
		String inputLine = in.readLine();

		while (inputLine != null) {
			response.append(inputLine);
			inputLine = in.readLine();
		}

		in.close();
		connection.disconnect();

		return response.toString();
	}

	public static void parseAndDisplayQuestions(String json) {
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
		JsonArray questionsArray = jsonObject.getAsJsonArray("results");

		for (int i = 0; i < questionsArray.size(); i++) {
			JsonObject questionObject = questionsArray.get(i).getAsJsonObject();
			String question = questionObject.get("question").getAsString();
			String correctAnswer = questionObject.get("correct_answer").getAsString();
			JsonArray incorrectAnswersArray = questionObject.getAsJsonArray("incorrect_answers");

			System.out.println("Question: " + question);

			System.out.println("Correct Answer: " + correctAnswer);
			System.out.print("Incorrect Answers: ");
			for (int j = 0; j < incorrectAnswersArray.size(); j++) {
				System.out.print(incorrectAnswersArray.get(j).getAsString() + " ");
			}
			System.out.println("\n");
		}

		// How to randomize answer choices?
		// display answers as A B C D
		// assign random number between 0-3 to answers
		// A = 0, B = 1, C = 2, D = 3
		
//		String answer = null;
//		Random random = new Random();
//		int randomNum = random.nextInt(4) + 1;
//		
//		String[] answersArray = new String[4];
//		answersArray[0] = "A: ";
//		answersArray[1] = "B: ";
//		answersArray[2] = "C: ";
//		answersArray[3] = "D: ";
//		
//		for (int i = 0; i < answersArray.length; i++) {
//			answersArray[i] += answer;
//		}
		
	}

}
