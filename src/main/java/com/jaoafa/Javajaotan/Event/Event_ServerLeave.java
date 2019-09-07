package com.jaoafa.Javajaotan.Event;

import com.jaoafa.Javajaotan.Main;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.member.UserLeaveEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;

public class Event_ServerLeave {
	@EventSubscriber
	public void onMemberLeaveEvent(UserLeaveEvent event) {
		if (event.getGuild().getLongID() != 597378876556967936L) {
			return; // jMS Gamers Clubのみ
		}
		IUser user = event.getUser();
		IChannel channel = event.getGuild().getChannelByID(597419057251090443L);
		RequestBuffer.request(() -> {
			try {
				channel.sendMessage(
						":wave:" + user.getName() + "#" + user.getDiscriminator() + "がjMS Gamers Clubから退出しました。");
			} catch (DiscordException discordexception) {
				Main.DiscordExceptionError(getClass(), channel, discordexception);
			}
		});
	}
}
