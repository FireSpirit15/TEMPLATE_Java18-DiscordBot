package de.firespirit.main;

import de.firespirit.commands.Shutdown;

import java.awt.Color;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import javax.security.auth.login.LoginException;

import de.firespirit.commands.Restart;
import de.firespirit.music.CommandPause;
import de.firespirit.music.CommandPlay;
import de.firespirit.music.CommandSkip;
import de.firespirit.music.CommandStop;
import de.firespirit.music.CommandVolume;
import de.firespirit.music.GuildMusicManager;
import de.firespirit.music.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Main {
	
	public static int errorCount;
	public static String channelID = "INSERT LOG CHANNEL ID HERE";
	public static String memberID = "INSERT BOT MEMBER ID HERE";
	public static String voiceChannelID = "INSERT VOICE CHANNEL ID HERE";
	public static String botToken = "INSERT BOT TOKEN HERE";
	public static String guildID = "INSERT GUILD ID HERE";
	
	public static void main(String[] args) throws LoginException {
		
        
        JDA jda = JDABuilder.createLight(botToken, Collections.emptyList())
        	.setActivity(Activity.playing("Moderation"))
            .setStatus(OnlineStatus.ONLINE)
            .enableIntents(GatewayIntent.GUILD_MEMBERS)
            .enableIntents(GatewayIntent.GUILD_MESSAGES)
            .enableIntents(GatewayIntent.MESSAGE_CONTENT)
            .enableIntents(GatewayIntent.GUILD_VOICE_STATES)
            .enableCache(CacheFlag.VOICE_STATE)
            
            //Music
            .addEventListeners(new CommandPause())
            .addEventListeners(new CommandStop())
            .addEventListeners(new CommandSkip())
            .addEventListeners(new CommandVolume())
            .addEventListeners(new CommandPlay())
            
        	//Commands
            .addEventListeners(new Shutdown())
            .addEventListeners(new Restart())
            
            .build();
        
        //Music
        jda.upsertCommand("play", "Play track")
        .addOption(OptionType.STRING, "link", "Youtube-URL", true)
        .queue();
        jda.upsertCommand("volume", "Change volume")
        .addOption(OptionType.INTEGER, "volume", "Volume", true)
        .queue();
        jda.upsertCommand("pause", "Pause current track")
        .queue();
        jda.upsertCommand("stop", "Stop track and disconnect")
        .queue();
        jda.upsertCommand("skip", "Skip current track")
        .queue();

        //Command-Registration
        jda.upsertCommand("restart", "Restart bot").queue();
        jda.upsertCommand("shutdown", "Shutdown bot").queue();
        try {
			Thread.sleep(1000);
			DateTimeFormatter date = DateTimeFormatter.ofPattern("dd.MM.YYYY - HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			Guild g = jda.getGuildById(guildID);
			TextChannel tc = g.getTextChannelById(channelID);
			EmbedBuilder eb = new EmbedBuilder();
			eb.setTitle("Bot online");
			eb.setColor(Color.decode("#208013"));
			eb.setDescription("» » Bot startup successful!"
					+ "\n» Errors: " + errorCount);
			eb.setFooter("-----------------------------------------------------------------" + "\n" + "System • " + date.format(now));
			tc.sendMessageEmbeds(eb.build()).queue();
			eb.clear();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void announceTrack(net.dv8tion.jda.api.entities.Member m, Guild g, SlashCommandInteractionEvent e, DateTimeFormatter date, TextChannel tc) {
		final GuildMusicManager musicManager = PlayerManager.getINSTANCE().getMusicManager(g);
		EmbedBuilder embed = new EmbedBuilder();
		embed.setColor(Color.YELLOW);
		embed.setTitle(m.getEffectiveName() + " queued a new track!");
		embed.setDescription("Name » " + musicManager.audioPlayer.getPlayingTrack().getInfo().title
				+ "\nAuthor » " + musicManager.audioPlayer.getPlayingTrack().getInfo().author);
		embed.setFooter(e.getTimeCreated().format(date) + " Uhr");
		
		tc.sendMessageEmbeds(embed.build()).queue();
		embed.clear();
	}
}
