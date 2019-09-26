package com.jaoafa.Javajaotan.Channel;

import com.jaoafa.Javajaotan.ChannelPremise;
import com.jaoafa.Javajaotan.Main;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;

public class Channel_626727474922913792 implements ChannelPremise {
	// #develop_todo
	@Override
	public void run(IDiscordClient client, IGuild guild, IChannel channel, IUser author, IMessage message,
			boolean edited) {
		if (message.isSystemMessage()) {
			return;
		}
		try {
			channel.pin(message);
		} catch (DiscordException e) {
			String pinerr = e.getErrorMessage();
			RequestBuffer.request(() -> {
				try {
					message.addReaction(ReactionEmoji.of("❌")); // :x:
					channel.sendMessage("ピンエラー: `" + pinerr + "`");
				} catch (DiscordException discordexception) {
					Main.DiscordExceptionError(getClass(), channel, discordexception);
				}
			});
		}
	}

	@Override
	public boolean isAlsoTargetEdited() {
		return false;
	}

}
