![App Screenshot](https://i.ibb.co/pj46t55/Screenshot-2023-01-29-140504.png)

# TEMPLATE_Java18-DiscordBot
Template for Java 18 Discord Bot

## Features

- Restart command
- Shutdown command
- Music player
- Example for main class/method
- Example for event/command registration
## Usage

- Step 1: Download/clone repository via command or direct download

```bash
gh repo clone FireSpirit15/TEMPLATE_Java18-DiscordBot
````

- Step 2: Open project in your working environment

- Step 3: Replace in Main.java\
  `JDA jda = JDABuilder.createLight("INSERT BOT TOKEN HERE", Collections.emptyList())`\
  `Guild g = jda.getGuildById("INSERT GUILD ID HERE");`\
  `TextChannel tc = g.getTextChannelById("INSERT CHANNEL ID HERE");`\
  with own values

- Step 4: Replace in all command classes\
  `TextChannel logs = e.getGuild().getTextChannelById("INSERT CHANNEL ID HERE");`\
  with own values

- Step 5: Replace in Play.java\
  `final VoiceChannel vc = (VoiceChannel) e.getGuild().getVoiceChannelById("INSERT VOICE CHANNEL ID HERE");`\
  with own values

- Step 6: Run application and enjoy :D
## Documentation

[JDA (Java Discord API)](https://github.com/DV8FromTheWorld/JDA)  
[LavaPlayer - Audio player library for Discord
](https://github.com/sedmelluq/lavaplayer)

## Authors

- [@FireSpirit](https://github.com/FireSpirit15)

