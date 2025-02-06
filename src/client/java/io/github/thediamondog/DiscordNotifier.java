package io.github.thediamondog;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import io.github.thediamondog.ServerTrackerClient;

public class DiscordNotifier {
    public static void sendServerAddress(String playerName, String serverAddress) {
        try {
            String content = buildEmbedServer(playerName, serverAddress);
            sendRequest(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendStatus(String playerName, String status) {
        try {
            String content = buildEmbedStatus(playerName, status);
            sendRequest(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static String buildEmbedServer(String playerName, String serverAddress) {
        return "{"
                + "\"embeds\": ["
                + "{"
                + "\"title\": \"" + playerName + " has joined the server\","
                + "\"color\": 7722093,"
                + "\"description\": \"" + serverAddress + "\""
                + "}"
                + "]"
                + "}";
    }
    
    private static String buildEmbedStatus(String playerName, String status) {
        return "{"
                + "\"embeds\": ["
                + "{"
                + "\"title\": \"" + playerName + " Status Update\","
                + "\"color\": \"" + ServerTrackerClient.embedColorDecimal + "\","
                + "\"description\": \"" + status + "\""
                + "}"
                + "]"
                + "}";
    }

    public static void sendRequest(String content) throws Exception {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(ServerTrackerClient.webhookUrl);
        
        httpPost.setHeader("Content-Type", "application/json");

        StringEntity jsonEntity = new StringEntity(content);
        httpPost.setEntity(jsonEntity);
        
        HttpResponse response = httpClient.execute(httpPost);

        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("Webhook response code: " + statusCode);
    }
}
