package com.jaoafa.Javajaotan.Event;

import com.jaoafa.Javajaotan.Main;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionAddEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IReaction;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;

public class Event_TodoCheck {
	@EventSubscriber
	public void onReactionAddEvent(ReactionAddEvent event) {
		IChannel channel = event.getChannel();
		if (channel.getLongID() != 597424023621599232L) {
			return;
		}
		IMessage message = event.getMessage();
		if (!message.isPinned()) {
			return;
		}
		if (message.getReactions().size() == 0) {
			message = channel.fetchMessage(message.getLongID());
		}
		IReaction white_check_mark = message.getReactionByUnicode("\u2705");
		if (white_check_mark.getCount() == 0) {
			return;
		}
		RequestBuffer.request(() -> {
			try {
				event.getMessage().addReaction(white_check_mark);
				channel.unpin(event.getMessage());
			} catch (DiscordException discordexception) {
				Main.DiscordExceptionError(getClass(), channel, discordexception);
			}
		});
	}
}
