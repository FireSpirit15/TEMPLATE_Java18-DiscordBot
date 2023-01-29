package de.firespirit.music;

import java.awt.Color;
import java.time.format.DateTimeFormatter;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed.Thumbnail;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandStop extends ListenerAdapter {
	
	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent e) {
		
		
		if (e.getName().equals("stop")) {
			DateTimeFormatter date = DateTimeFormatter.ofPattern("dd.MM.YY - HH:mm");
            PlayerManager.getINSTANCE().getMusicManager(e.getGuild()).scheduler.audioPlayer.stopTrack();
            PlayerManager.getINSTANCE().getMusicManager(e.getGuild()).scheduler.queue.clear();
            e.getGuild().getAudioManager().closeAudioConnection();
			
			TextChannel logs = e.getGuild().getTextChannelById("INSERT CHANNEL ID HERE");
			Member m = e.getMember();

			EmbedBuilder embed = new EmbedBuilder();
			embed.setColor(Color.MAGENTA);
			embed.setTitle(m.getEffectiveName() + " stopped the player!");
			embed.setFooter(e.getTimeCreated().format(date));
			
			logs.sendMessageEmbeds(embed.build()).queue();
			embed.clear();
			
			//Required reply to original message for fix "application not responding" error
			Thumbnail thumb = new Thumbnail("https://cdn.pixabay.com/photo/2016/05/30/17/26/hook-1425312__340.png", "https://cdn.pixabay.com/photo/2016/05/30/17/26/hook-1425312__340.png", 356, 340);
			MessageEmbed me = new MessageEmbed("", "Success!", "The command has been executed!", EmbedType.AUTO_MODERATION, null, Color.GREEN.getRGB(), thumb, null, null, null, null, null, null);
			e.replyEmbeds(me).setEphemeral(true).queue();
		}
		
	}

}
