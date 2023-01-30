package de.firespirit.music;

import java.awt.Color;
import java.net.URI;
import java.time.format.DateTimeFormatter;

import de.firespirit.main.Main;
import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed.Thumbnail;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.managers.AudioManager;

public class CommandPlay extends ListenerAdapter {
	
	public boolean isUrl(String url) {
		try {
			new URI(url);
			return true;
		} catch (Exception e2) {
			return false;
		}
	}
	
	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent e) {
		
		DateTimeFormatter date = DateTimeFormatter.ofPattern("dd.MM.YY - HH:mm");
		
		if (e.getName().equals("play")) {
			Guild g = e.getGuild();
			Member bot = g.getMemberById(Main.memberID);
			if (!bot.getVoiceState().inAudioChannel()) {
				final AudioManager audioManager = e.getGuild().getAudioManager();
				final VoiceChannel vc = (VoiceChannel) e.getGuild().getVoiceChannelById(Main.voiceChannelID);
				audioManager.openAudioConnection(vc);
				PlayerManager.getINSTANCE().getMusicManager(e.getGuild()).scheduler.audioPlayer.setVolume(10);
			}
				
				OptionMapping optionlink = e.getOption("link");
				Member m = e.getMember();
				String link = optionlink.getAsString();
				if (!isUrl(link)) {
					link = "ytsearch:" + link + " audio";
				}
				PlayerManager.getINSTANCE().loadAndPlay((TextChannel) e.getChannel(), link, m, g, e, date);
			
				//Required reply to original message for fix "application not responding" error
				Thumbnail thumb = new Thumbnail("https://cdn.pixabay.com/photo/2016/05/30/17/26/hook-1425312__340.png", "https://cdn.pixabay.com/photo/2016/05/30/17/26/hook-1425312__340.png", 356, 340);
				MessageEmbed me = new MessageEmbed("", "Success!", "The command has been executed!", EmbedType.AUTO_MODERATION, null, Color.GREEN.getRGB(), thumb, null, null, null, null, null, null);
				e.replyEmbeds(me).setEphemeral(true).queue();
		}
		
	}

}
