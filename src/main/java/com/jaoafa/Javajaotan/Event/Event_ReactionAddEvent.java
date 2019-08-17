package com.jaoafa.Javajaotan.Event;

import com.jaoafa.Javajaotan.Task.Task_MeetingVote;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionAddEvent;

public class Event_ReactionAddEvent {
	@EventSubscriber
	public void onReactionAddEvent(ReactionAddEvent event) {
		if (event.getChannel().getLongID() == 597423974816808970L) {
			new Task_MeetingVote().run();
		}
	}
}
