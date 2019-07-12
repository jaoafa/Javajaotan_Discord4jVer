package com.jaoafa.Javajaotan.Event;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.member.UserBanEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public class Event_ServerBanned {
	@EventSubscriber
	public void onMemberBannedEvent(UserBanEvent event) {
		if(event.getGuild().getLongID() != 597378876556967936L){
			return; // jMS Gamers Clubのみ
		}
		IUser user = event.getUser();
		IChannel channel = event.getGuild().getChannelByID(597419057251090443L);
		channel.sendMessage(":no_pedestrians:" + user.getName() + "#" + user.getDiscriminator() + "がjMS Gamers ClubからBanされました。");
	}
}
