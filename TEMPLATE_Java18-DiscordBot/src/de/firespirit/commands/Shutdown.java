package de.firespirit.commands;

import java.awt.Color;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import de.firespirit.main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed.Thumbnail;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Shutdown extends ListenerAdapter {
	
	
	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent e) {
		
		if (e.getName().equals("shutdown")) {
			
			TextChannel logs = e.getGuild().getTextChannelById(Main.channelID);
			DateTimeFormatter date = DateTimeFormatter.ofPattern("dd.MM.YYYY - HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			EmbedBuilder eb = new EmbedBuilder();
			eb.setTitle("Bot offline");
			eb.setDescription("» Bot shutdown successful!");
			eb.setColor(Color.decode("#911917"));
			eb.setFooter("-------------------------------------------------------" + "\n" + "Moderator • " + e.getMember().getEffectiveName() + " • " + date.format(now));
			logs.sendMessageEmbeds(eb.build()).queue();
			eb.clear();
			
			//Required reply to original message for fix "application not responding" error
			Thumbnail thumb = new Thumbnail("https://cdn.pixabay.com/photo/2016/05/30/17/26/hook-1425312__340.png", "https://cdn.pixabay.com/photo/2016/05/30/17/26/hook-1425312__340.png", 356, 340);
			MessageEmbed me = new MessageEmbed("", "Success!", "The command has been executed!", EmbedType.AUTO_MODERATION, null, Color.GREEN.getRGB(), thumb, null, null, null, null, null, null);
			e.replyEmbeds(me).setEphemeral(true).queue();
			
			try {
				Thread.sleep(1000);
				e.getGuild().getJDA().shutdownNow();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}

}
