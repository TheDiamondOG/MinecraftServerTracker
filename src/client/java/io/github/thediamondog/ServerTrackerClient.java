package io.github.thediamondog;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.ClientConnection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ServerTrackerClient implements ClientModInitializer {
	private String lastServerAddress = "";
	private boolean sentStartupMessage = false;

	public static String webhookUrl;
	public static int embedColorDecimal;

	@Override
	public void onInitializeClient() {
		loadConfig();

		ClientTickCallback.EVENT.register(client -> {
			if (!sentStartupMessage) {
				String playerName = MinecraftClient.getInstance().getSession().getUsername();
				DiscordNotifier.sendStatus(playerName, "Online");
				sentStartupMessage = true;
			}

			ClientConnection connection = MinecraftClient.getInstance().getNetworkHandler() != null
					? MinecraftClient.getInstance().getNetworkHandler().getConnection()
					: null;

			if (connection != null) {
				String fullServerAddress = connection.getAddress().toString();
				String serverURL = parseServerURL(fullServerAddress);
				String playerName = MinecraftClient.getInstance().getSession().getUsername();

				if (!serverURL.equals(lastServerAddress)) {
					System.out.println("Joined server: " + serverURL);

					if (serverURL.startsWith("local")) {
						DiscordNotifier.sendStatus(playerName, "Playing Singleplayer");
					} else {
						DiscordNotifier.sendServerAddress(playerName, serverURL);
					}

					lastServerAddress = serverURL;
				}
			}
		});
	}

	private void loadConfig() {
		try {
			File configFile = new File("config.properties");

			if (!configFile.exists()) {
				configFile.createNewFile();
				setDefaultConfig(configFile);
			}

			Properties prop = new Properties();
			FileInputStream input = new FileInputStream(configFile);
			prop.load(input);

			webhookUrl = prop.getProperty("webhookUrl", "");
			embedColorDecimal = Integer.parseInt(prop.getProperty("embedColorDecimal", "7722093"));
			input.close();

			saveConfig(configFile);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void setDefaultConfig(File configFile) {
		Properties prop = new Properties();
		prop.setProperty("webhookUrl", "");
		prop.setProperty("embedColorDecimal", "7722093");

		try {
			FileOutputStream output = new FileOutputStream(configFile);
			prop.store(output, "Minecraft Server Tracker Config");
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void saveConfig(File configFile) {
		try {
			Properties prop = new Properties();
			prop.setProperty("webhookUrl", webhookUrl);
			prop.setProperty("embedColorDecimal", Integer.toString(embedColorDecimal));

			FileOutputStream output = new FileOutputStream(configFile);
			prop.store(output, "Minecraft Server Tracker Config");
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String parseServerURL(String fullServerAddress) {
		return fullServerAddress.split("/")[0];
	}
}
