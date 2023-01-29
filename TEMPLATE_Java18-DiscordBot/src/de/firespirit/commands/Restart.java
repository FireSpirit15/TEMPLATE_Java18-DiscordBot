package de.firespirit.commands;

import java.awt.Color;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.security.auth.login.LoginException;

import de.firespirit.main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed.Thumbnail;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Restart extends ListenerAdapter {
	
	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent e) {
		
		if (e.getName().equals("restart")) {
			
			TextChannel logs = e.getGuild().getTextChannelById("INSERT CHANNEL ID HERE");
			DateTimeFormatter date = DateTimeFormatter.ofPattern("dd.MM.YYYY - HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			EmbedBuilder eb = new EmbedBuilder();
			eb.setTitle("Restarting...");
			eb.setDescription("» Bot is restarting...");
			eb.setColor(Color.decode("#b39919"));
			eb.setFooter("-------------------------------------------------------" + "\n" + "Moderator • " + e.getMember().getEffectiveName() + " • " + date.format(now));
			logs.sendMessageEmbeds(eb.build()).queue();
			eb.clear();
			
			//Required reply to original message for fix "application not responding" error
			Thumbnail thumb = new Thumbnail("https://cdn.pixabay.com/photo/2016/05/30/17/26/hook-1425312__340.png", "https://cdn.pixabay.com/photo/2016/05/30/17/26/hook-1425312__340.png", 356, 340);
			MessageEmbed me = new MessageEmbed("", "Success!", "The command has been executed!", EmbedType.AUTO_MODERATION, null, Color.GREEN.getRGB(), thumb, null, null, null, null, null, null);
			e.replyEmbeds(me).setEphemeral(true).queue();
			
			try {
				Thread.sleep(1000);
				EmbedBuilder eb1 = new EmbedBuilder();
				eb1.setTitle("Bot offline");
				eb1.setDescription("» Bot shutdown successful!");
				eb1.setColor(Color.decode("#911917"));
				eb1.setFooter("-------------------------------------------------------" + "\n" + "System • " + " • " + date.format(now));
				logs.sendMessageEmbeds(eb1.build()).queue();
				eb1.clear();
				e.getGuild().getJDA().shutdown();
				Main.main(null);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			} catch (LoginException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

}
