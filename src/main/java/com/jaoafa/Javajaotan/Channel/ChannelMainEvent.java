package com.jaoafa.Javajaotan.Channel;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.jaoafa.Javajaotan.ChannelPremise;
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

public class ChannelMainEvent {
	@EventSubscriber
	public void onMessageReceivedEvent(MessageReceivedEvent event) {
		IDiscordClient client = event.getClient();
		IGuild guild = event.getGuild();
		IChannel channel = event.getChannel();
		IUser author = event.getAuthor();
		IMessage message = event.getMessage();

		if (channel.getLongID() != 603841992404893707L || MuteManager.isMuted(author.getStringID())) {
			return; // #greetingではない or Muted
		}

		try {
			String className = channel.getStringID();
			//channel.sendMessage("com.jaoafa.Javajaotan.Command.Cmd_" + className);

			Class.forName("com.jaoafa.Javajaotan.Channel.Channel_" + className);
			// クラスがない場合これ以降進まない
			Constructor<?> construct = (Constructor<?>) Class
					.forName("com.jaoafa.Javajaotan.Channel.Channel_" + className).getConstructor();
			ChannelPremise cmd = (ChannelPremise) construct.newInstance();

			cmd.run(client, guild, channel, author, message);
		} catch (ClassNotFoundException e) {
			// not found
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// error
			ErrorReporter.report(e);
		}
	}

	@EventSubscriber
	public void onMessageEditEvent(MessageEditEvent event) {
		IDiscordClient client = event.getClient();
		IGuild guild = event.getGuild();
		IChannel channel = event.getChannel();
		IUser author = event.getAuthor();
		IMessage message = event.getMessage();

		if (channel.getLongID() != 603841992404893707L || MuteManager.isMuted(author.getStringID())) {
			return; // #greetingではない or Muted
		}

		try {
			String className = channel.getStringID();
			//channel.sendMessage("com.jaoafa.Javajaotan.Command.Cmd_" + className);

			Class.forName("com.jaoafa.Javajaotan.Channel.Channel_" + className);
			// クラスがない場合これ以降進まない
			Constructor<?> construct = (Constructor<?>) Class
					.forName("com.jaoafa.Javajaotan.Channel.Channel_" + className).getConstructor();
			ChannelPremise cmd = (ChannelPremise) construct.newInstance();

			if (!cmd.isAlsoTargetEdited()) {
				return;
			}

			cmd.run(client, guild, channel, author, message);
		} catch (ClassNotFoundException e) {
			// not found
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// error
			ErrorReporter.report(e);
		}
	}
}
