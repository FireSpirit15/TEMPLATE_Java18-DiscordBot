package de.firespirit.music;

import java.awt.Color;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.firespirit.main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed.Thumbnail;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandSkip extends ListenerAdapter {
	
	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent e) {
		
		
		if (e.getName().equals("skip")) {
			DateTimeFormatter date = DateTimeFormatter.ofPattern("dd.MM.YYYY - HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			final GuildMusicManager musicManager = PlayerManager.getINSTANCE().getMusicManager(e.getGuild());
	        final AudioPlayer audioPlayer = musicManager.audioPlayer;
	        TextChannel logs = e.getGuild().getTextChannelById(Main.channelID);

	        if(audioPlayer.getPlayingTrack() == null){
	        	EmbedBuilder embed = new EmbedBuilder();
				embed.setColor(Color.MAGENTA);
				embed.setTitle("There is no track queued!");
				embed.setFooter(date.format(now));
				
				logs.sendMessageEmbeds(embed.build()).queue();
				embed.clear();
	            return;
	        }

	        musicManager.scheduler.nextTrack();
			
			Member m = e.getMember();

			EmbedBuilder embed = new EmbedBuilder();
			embed.setColor(Color.MAGENTA);
			embed.setTitle(m.getEffectiveName() + " skipped current track!");
			embed.setDescription("Now playing"
					+ "\nName » " + musicManager.audioPlayer.getPlayingTrack().getInfo().title
					+ "\nAuthor » " + musicManager.audioPlayer.getPlayingTrack().getInfo().author);
			embed.setFooter(date.format(now));
			
			logs.sendMessageEmbeds(embed.build()).queue();
			embed.clear();
			
			//Required reply to original message for fix "application not responding" error
			Thumbnail thumb = new Thumbnail("https://cdn.pixabay.com/photo/2016/05/30/17/26/hook-1425312__340.png", "https://cdn.pixabay.com/photo/2016/05/30/17/26/hook-1425312__340.png", 356, 340);
			MessageEmbed me = new MessageEmbed("", "Success!", "The command has been executed!", EmbedType.AUTO_MODERATION, null, Color.GREEN.getRGB(), thumb, null, null, null, null, null, null);
			e.replyEmbeds(me).setEphemeral(true).queue();
		}
		
	}

}
