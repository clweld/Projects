package kata_weather;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.Scanner;

public class KataWeather {

    private static final String API_KEY = "cd8a81b7476dccce4551f6a0dd2f78f7"; 
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&appid=" + API_KEY;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter location: ");
        String location = scanner.nextLine();
        scanner.close();
        
        try {
            JsonObject weatherData = fetchWeatherData(location);
            displayWeatherData(location, weatherData);
        } catch (IOException | URISyntaxException e) {
            System.out.println("Error fetching weather data: " + e.getMessage());
        }
    }

    public static JsonObject fetchWeatherData(String location) throws IOException, URISyntaxException {
    	String urlString = String.format(API_URL, location);
    	
    	URI uri = new URI(urlString);
		URL url = uri.toURL();
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP error code: " + responseCode);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder content = new StringBuilder();
        String inputLine;
        
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        connection.disconnect();

        Gson gson = new Gson();
        return gson.fromJson(content.toString(), JsonObject.class);
    }

    public static void displayWeatherData(String location, JsonObject weatherData) {
        JsonObject mainData = weatherData.getAsJsonObject("main");
        JsonObject windData = weatherData.getAsJsonObject("wind");
        String description = weatherData.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString();

        System.out.println("\nWeather Data for " + location + ":");
        System.out.println("-------------------------------------------------");
        System.out.printf("%-20s %s°C%n", "Temperature:", mainData.get("temp").getAsString());
        System.out.printf("%-20s %s%% %n", "Humidity:", mainData.get("humidity").getAsString());
        System.out.printf("%-20s %s hPa%n", "Pressure:", mainData.get("pressure").getAsString());
        System.out.printf("%-20s %s m/s%n", "Wind Speed:", windData.get("speed").getAsString());
        System.out.printf("%-20s %s%n", "Description:", description);
    }
}