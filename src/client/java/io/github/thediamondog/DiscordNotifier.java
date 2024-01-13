package io.github.thediamondog;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import io.github.thediamondog.ServerTrackerClient;

public class DiscordNotifier {
    // This will send the server url
    public static void sendServerAddress(String playerName, String serverAddress) {
        try {
            String content = buildEmbedServer(playerName, serverAddress);
            sendRequest(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // When the status changes for a user then it will get sent using this
    public static void sendStatus(String playerName, String status) {
        try {
            String content = buildEmbedStatus(playerName, status);
            sendRequest(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Build an embed for sending the server url
    private static String buildEmbedServer(String playerName, String serverAddress) {
        // The embed code
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

    // Build an embed for sending the player status changes
    private static String buildEmbedStatus(String playerName, String status) {
        // The embed code strikes again
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

    // Everything gets sent here
    public static void sendRequest(String content) throws Exception {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(ServerTrackerClient.webhookUrl);

        // Set headers
        httpPost.setHeader("Content-Type", "application/json");

        // Set content
        StringEntity jsonEntity = new StringEntity(content);
        httpPost.setEntity(jsonEntity);

        // Execute the request
        HttpResponse response = httpClient.execute(httpPost);

        // Log the response
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("Webhook response code: " + statusCode);
    }
}