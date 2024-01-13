# Minecraft Server Tracker

## Overview

Minecraft Server Tracker is a Minecraft client mod that tracks the player's server connections and sends it to a Discord webhooks.

## Features

- Tracks server connections and disconnections.
- Sends notifications with server information to Discord.

## Usage

1. Install the Minecraft Server Tracker mod.
2. Join Minecraft servers, and the mod will track your connections.
3. Notifications will be sent to the configured Discord webhook.

# Install

1. Clone the repository:
`git clone https://github.com/TheDiamondOG/MinecraftServerTracker.git`
2. Add your webhook url to the code (This is the step until I can find out how config files work).
src/client/java/io/github/thediamondog/DiscordNotifier.java line 11
3. Build the mod:
`./gradlew build`
4. Drag the mod into your mods folder.
The mod will be found in `build/libs/servertracker-1.0.3.jar`
5. Join a server without the risk of getting banned.
