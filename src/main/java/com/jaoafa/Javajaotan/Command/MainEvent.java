package com.jaoafa.Javajaotan.Command;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class MainEvent {
	@EventSubscriber
	public void onReadyEvent(ReadyEvent event) {
		System.out.println("Ready: " + event.getClient().getOurUser().getName());
	}
	@EventSubscriber
	public void onMessageReceivedEvent(MessageReceivedEvent event) {

		IDiscordClient client = event.getClient();
		IGuild guild = event.getGuild();
		IChannel channel = event.getChannel();
		IUser author = event.getAuthor();
		IMessage message = event.getMessage();
		String text = event.getMessage().getContent();

		if(!text.startsWith("/")){
			return;
		}
		System.out.println("Msg: " + event.getAuthor().getName() + " " + event.getMessage().getContent());

		String[] args;
		if(text.contains("\n")){
			args = text.split("\n");
		}else if(text.contains(" ")){
			args = text.split(" ");
		}else{
			args = new String[]{text};
		}

	}
}
