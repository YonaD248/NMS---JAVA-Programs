import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RESTCONFMonitor {
    private static final String RESTCONF_URL = "http://127.0.0.1:8080/restconf/data/interfaces"; // Replace with actual URL
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";

    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
            .uri(new URI(RESTCONF_URL))
            .header("Accept", "application/yang-data+json")
            .header("Authorization", "Basic " + encodeCredentials(USERNAME, PASSWORD))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Interface Statistics: " + response.body());
    }

    private static String encodeCredentials(String username, String password) {
        return Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }
}
