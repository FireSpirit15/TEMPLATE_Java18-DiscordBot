package de.firespirit.music;

import java.awt.Color;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class PlayerManager {
	
	private static PlayerManager INSTANCE;
	private final Map<Long, GuildMusicManager> musicManagers;
	private final AudioPlayerManager audioPlayerManager;
	int counter = 0;
	
	public PlayerManager() {
		this.musicManagers = new HashMap<>();
		this.audioPlayerManager = new DefaultAudioPlayerManager();
		
		AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
		AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
	}
	
	public GuildMusicManager getMusicManager(Guild guild) {
		return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
			final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);
			guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
			return guildMusicManager;
		});
	}
	
	public void loadAndPlay(TextChannel tc, String trackURL, Member m, Guild g, SlashCommandInteractionEvent e, DateTimeFormatter date) {
		final GuildMusicManager musicManager = this.getMusicManager(tc.getGuild());
		
		this.audioPlayerManager.loadItemOrdered(musicManager, trackURL, new AudioLoadResultHandler() {
			
			@Override
			public void trackLoaded(AudioTrack track) {
				DateTimeFormatter date = DateTimeFormatter.ofPattern("dd.MM.YYYY - HH:mm:ss");
				LocalDateTime now = LocalDateTime.now();
				musicManager.scheduler.queue(track);
				
				EmbedBuilder embed = new EmbedBuilder();
				embed.setColor(Color.MAGENTA);
				embed.setTitle(m.getEffectiveName() + " queued a new track!");
				embed.setDescription("Name » " + musicManager.audioPlayer.getPlayingTrack().getInfo().title
						+ "\nAuthor » " + musicManager.audioPlayer.getPlayingTrack().getInfo().author);
				embed.setFooter(date.format(now));
				
				tc.sendMessageEmbeds(embed.build()).queue();
				embed.clear();
			}
			
			@Override
			public void playlistLoaded(AudioPlaylist playlist) {
				DateTimeFormatter date = DateTimeFormatter.ofPattern("dd.MM.YYYY - HH:mm:ss");
				LocalDateTime now = LocalDateTime.now();
				final List<AudioTrack> tracks = playlist.getTracks();
				if (!tracks.isEmpty()) {
					musicManager.scheduler.queue(tracks.get(counter));
					
					EmbedBuilder embed = new EmbedBuilder();
					embed.setColor(Color.MAGENTA);
					embed.setTitle(m.getEffectiveName() + " queued a new track!");
					embed.setDescription("Name » " + tracks.get(counter).getInfo().title
							+ "\nAuthor » " + tracks.get(counter).getInfo().author);
					embed.setFooter(date.format(now));
					
					tc.sendMessageEmbeds(embed.build()).queue();
					embed.clear();
					counter++;
				}
				
			}
			
			@Override
			public void noMatches() {
				DateTimeFormatter date = DateTimeFormatter.ofPattern("dd.MM.YYYY - HH:mm:ss");
				LocalDateTime now = LocalDateTime.now();
				EmbedBuilder embed = new EmbedBuilder();
				embed.setColor(Color.RED);
				embed.setTitle("No matches found!");
				embed.setFooter(date.format(now));
				
				tc.sendMessageEmbeds(embed.build()).queue();
				embed.clear();
			}
			
			@Override
			public void loadFailed(FriendlyException exception) {
				DateTimeFormatter date = DateTimeFormatter.ofPattern("dd.MM.YYYY - HH:mm:ss");
				LocalDateTime now = LocalDateTime.now();
				EmbedBuilder embed = new EmbedBuilder();
				embed.setColor(Color.RED);
				embed.setTitle("Error loading specified track!");
				embed.setFooter(date.format(now));
				
				tc.sendMessageEmbeds(embed.build()).queue();
				embed.clear();
			}
		});
	}
	
	public static PlayerManager getINSTANCE() {
		if (INSTANCE == null) {
			INSTANCE = new PlayerManager();
		}
		return INSTANCE;
	}

}
