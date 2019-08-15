package com.jaoafa.Javajaotan.ALLChat;

import com.jaoafa.Javajaotan.ALLChatPremise;
import com.jaoafa.Javajaotan.Javajaotan;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;

public class ALL_MessagePin implements ALLChatPremise {
	@Override
	public void run(IDiscordClient client, IGuild guild, IChannel channel, IUser author, IMessage message,
			boolean edited) {
		String text = message.getContent();

		if (text.startsWith("📌")) {
			try {
				if (edited && message.isPinned()) {
					RequestBuffer.request(() -> {
						try {
							message.addReaction(ReactionEmoji.of("📌"));
						} catch (DiscordException discordexception) {
							Javajaotan.DiscordExceptionError(getClass(), channel, discordexception);
						}
					});
					return;
				}
				channel.pin(message);
			} catch (DiscordException e) {
				RequestBuffer.request(() -> {
					try {
						message.addReaction(ReactionEmoji.of("❌")); // :x:
						message.reply("メッセージをピン止めするのに失敗しました…。```" + e.getErrorMessage() + "```");
					} catch (DiscordException discordexception) {
						Javajaotan.DiscordExceptionError(getClass(), channel, discordexception);
					}
				});
			}
			RequestBuffer.request(() -> {
				try {
					message.addReaction(ReactionEmoji.of("📌")); // :pushpin:
				} catch (DiscordException discordexception) {
					Javajaotan.DiscordExceptionError(getClass(), channel, discordexception);
				}
			});
		}
	}

	@Override
	public boolean isAlsoTargetEdited() {
		return true;
	}
}
