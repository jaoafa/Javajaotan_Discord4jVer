package com.jaoafa.Javajaotan.Command;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Date;

import com.jaoafa.Javajaotan.CommandPremise;
import com.jaoafa.Javajaotan.Javajaotan;
import com.jaoafa.Javajaotan.Lib.ErrorReporter;
import com.jaoafa.Javajaotan.Lib.Library;
import com.jaoafa.Javajaotan.Lib.MuteManager;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;

public class MessageMainEvent {
	@EventSubscriber
	public void onReadyEvent(ReadyEvent event) {
		System.out.println("Ready: " + event.getClient().getOurUser().getName());

		Javajaotan.setClient(event.getClient());

		Javajaotan.ReportChannel = event.getClient().getChannelByID(597766057117351937L);

		RequestBuffer.request(() -> {
			try {
				event.getClient().getChannelByID(597766057117351937L)
						.sendMessage("**[" + Javajaotan.sdf.format(new Date()) + " | " + Library.getHostName() + "]** "
								+ "Start Javajaotan");
			} catch (DiscordException discordexception) {
				Javajaotan.DiscordExceptionError(getClass(), event.getClient().getChannelByID(597766057117351937L),
						discordexception);
			}
		});
		Runtime.getRuntime().addShutdownHook(new Thread(
				() -> {
					RequestBuffer.request(() -> {
						try {
							event.getClient().getChannelByID(597766057117351937L)
									.sendMessage(
											"**[" + Javajaotan.sdf.format(new Date()) + " | " + Library.getHostName()
													+ "]** "
													+ "End Javajaotan");
						} catch (DiscordException discordexception) {
							Javajaotan.DiscordExceptionError(getClass(),
									event.getClient().getChannelByID(597766057117351937L), discordexception);
						}
					});
				}));
	}

	@EventSubscriber
	public void onMessageReceivedEvent(MessageReceivedEvent event) {
		IDiscordClient client = event.getClient();
		IGuild guild = event.getGuild();
		IChannel channel = event.getChannel();
		IUser author = event.getAuthor();
		IMessage message = event.getMessage();
		String text = event.getMessage().getContent();

		if (MuteManager.isMuted(author.getStringID())) {
			return;
		}
		if (channel.getLongID() == 603841992404893707L) {
			return;
		}

		if (!text.startsWith("/")) {
			return;
		}
		System.out.println("Msg: " + event.getAuthor().getName() + " " + event.getMessage().getContent());

		String[] args;
		String cmdname;
		if (text.contains(" ")) {
			cmdname = text.split(" ")[0].substring(1).trim();
			args = Arrays.copyOfRange(text.split(" "), 1, text.split(" ").length);
			args = Arrays.stream(args)
					.filter(s -> (s != null && s.length() > 0))
					.toArray(String[]::new);
		} else {
			args = new String[] {};
			cmdname = text.substring(1).trim();
		}
		try {
			String className = cmdname.substring(0, 1).toUpperCase() + cmdname.substring(1).toLowerCase(); // Help

			Class.forName("com.jaoafa.Javajaotan.Command.Cmd_" + className);
			// クラスがない場合これ以降進まない
			Constructor<?> construct = (Constructor<?>) Class.forName("com.jaoafa.Javajaotan.Command.Cmd_" + className)
					.getConstructor();
			CommandPremise cmd = (CommandPremise) construct.newInstance();

			if (cmd.isjMSOnly() && guild.getLongID() != 597378876556967936L) {
				return;
			}

			cmd.onCommand(client, guild, channel, author, message, args);
		} catch (ClassNotFoundException e) {
			// not found
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// error
			ErrorReporter.report(e);
		}
	}
}
