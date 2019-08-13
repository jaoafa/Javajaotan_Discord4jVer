package com.jaoafa.Javajaotan.ALLChat;

import java.lang.reflect.Constructor;

import com.jaoafa.Javajaotan.ALLChatPremise;
import com.jaoafa.Javajaotan.Lib.ClassFinder;
import com.jaoafa.Javajaotan.Lib.ErrorReporter;
import com.jaoafa.Javajaotan.Lib.MuteManager;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageEditEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class ALLChatMainEvent {
	@EventSubscriber
	public void onMessageReceivedEvent(MessageReceivedEvent event) {
		IDiscordClient client = event.getClient();
		IGuild guild = event.getGuild();
		IChannel channel = event.getChannel();
		IUser author = event.getAuthor();
		IMessage message = event.getMessage();
		if (channel.getLongID() == 603841992404893707L) {
			return; // #greeting
		}
		if (MuteManager.isMuted(author.getStringID())) {
			return; // Muted
		}
		try {
			ClassFinder classFinder = new ClassFinder();
			for (Class<?> clazz : classFinder.findClasses("com.jaoafa.Javajaotan.ALLChat")) {
				if (!clazz.getName().startsWith("com.jaoafa.Javajaotan.ALLChat.ALL_")) {
					continue;
				}
				Constructor<?> construct = (Constructor<?>) clazz.getConstructor();
				ALLChatPremise allchat = (ALLChatPremise) construct.newInstance();
				allchat.run(client, guild, channel, author, message, false);
			}
		} catch (Exception e) {
			ErrorReporter.report(e);
			return;
		}
	}

	@EventSubscriber
	public void onMessageEditEvent(MessageEditEvent event) {
		IDiscordClient client = event.getClient();
		IGuild guild = event.getGuild();
		IChannel channel = event.getChannel();
		IUser author = event.getAuthor();
		IMessage message = event.getMessage();
		if (channel.getLongID() == 603841992404893707L) {
			return; // #greeting
		}
		if (MuteManager.isMuted(author.getStringID())) {
			return; // Muted
		}
		System.out.println("EDIT: " + author.getName() + " -> " + message.getContent());
		try {
			ClassFinder classFinder = new ClassFinder();
			for (Class<?> clazz : classFinder.findClasses("com.jaoafa.Javajaotan.ALLChat")) {
				System.out.println("EDIT A: " + clazz.getName());
				if (!clazz.getName().startsWith("com.jaoafa.Javajaotan.ALLChat.ALL_")) {
					continue;
				}
				Constructor<?> construct = (Constructor<?>) clazz.getConstructor();
				ALLChatPremise allchat = (ALLChatPremise) construct.newInstance();

				if (!allchat.isAlsoTargetEdited()) {
					continue;
				}

				allchat.run(client, guild, channel, author, message, true);
			}
		} catch (Exception e) {
			ErrorReporter.report(e);
			return;
		}
	}
}
