package io.github.thediamondog;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.ClientConnection;

public class ServerTrackerClient implements ClientModInitializer {
	// Logs the last joined server so your pc does not explode and so you don't get banned
	private String lastServerAddress = "";

	// Same purpose of the last one
	private boolean sentStartupMessage = false;

	// Runs at the start of the client stuff
	@Override
	public void onInitializeClient() {
		// Register a client tick callback to check the server information
		ClientTickCallback.EVENT.register(client -> {
			if (!sentStartupMessage) {
				String playerName = MinecraftClient.getInstance().getSession().getUsername();
				// Send startup message when Minecraft starts
				DiscordNotifier.sendStatus(playerName, "Online");
				sentStartupMessage = true;
			}

			// Define the connection

			ClientConnection connection = MinecraftClient.getInstance().getNetworkHandler() != null
					? MinecraftClient.getInstance().getNetworkHandler().getConnection()
					: null;

			// Checks if someone is in the server
			if (connection != null) {
				// Access server information
				String fullServerAddress = connection.getAddress().toString();
				String serverURL = parseServerURL(fullServerAddress);
				String playerName = MinecraftClient.getInstance().getSession().getUsername();
				// Spam protection
				if (!serverURL.equals(lastServerAddress)) {
					// What gets sent
					// Check if the server URL is different from the last one
					System.out.println("Joined server: " + serverURL);

					// Send server information to webhook with JSON embed
					if (serverURL.startsWith("local")) { // Adding this check made it look better
						DiscordNotifier.sendStatus(playerName, "Playing Singleplayer");
					} else {
						DiscordNotifier.sendServerAddress(playerName, serverURL); // Normal server sending
					}

					// Update the last server address
					lastServerAddress = serverURL;
				}
			}
		});
	}

	// Makes it look good
	// But also makes it not look like an ip address
	private String parseServerURL(String fullServerAddress) {
		// Remove everything after the '/'
		return fullServerAddress.split("/")[0];
	}
}
