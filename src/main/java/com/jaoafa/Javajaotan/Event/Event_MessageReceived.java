package com.jaoafa.Javajaotan.Event;

import com.jaoafa.Javajaotan.Javajaotan;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;

public class Event_MessageReceived {
	@EventSubscriber
	public void onMessageReceivedEvent(MessageReceivedEvent event) {
		IDiscordClient client = event.getClient();
		IGuild guild = event.getGuild();
		IChannel channel = event.getChannel();
		IUser author = event.getAuthor();
		IMessage message = event.getMessage();
		String text = event.getMessage().getContent();

		// ピン
		if (text.startsWith("📌")) {
			try {
				channel.pin(message);
			} catch (DiscordException e) {
				message.addReaction(ReactionEmoji.of("\u274C")); // :x:
				RequestBuffer.request(() -> {
					try {
						message.reply("メッセージをピン止めするのに失敗しました…。```" + e.getErrorMessage() + "```");
					} catch (DiscordException discordexception) {
						Javajaotan.DiscordExceptionError(getClass(), channel, discordexception);
					}
				});
			}
			message.addReaction(ReactionEmoji.of("\u1F4CC")); // :pushpin:
		}
	}
}
