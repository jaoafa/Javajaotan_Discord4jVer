package com.jaoafa.Javajaotan.Event;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public class Event_ServerJoin {
	@EventSubscriber
	public void onMemberJoinEvent(UserJoinEvent event) {
		if(event.getGuild().getLongID() != 597378876556967936L){
			return; // jMS Gamers Clubのみ
		}
		IUser user = event.getUser();

		IChannel general = event.getGuild().getChannelByID(597419057251090443L);
		general.sendMessage(":man_dancing:<@" + user.getStringID() + ">(#" + user.getDiscriminator() + ")さんがjMS Gamers Clubに参加しました。");
		IChannel greeting = event.getGuild().getChannelByID(603841992404893707L);
		greeting.sendMessage(":man_dancing:<@" + user.getStringID() + ">(#" + user.getDiscriminator() + ")さん、jao Minecraft Server Discordにようこそ。\n"
			+ "運営方針により、参加から10分以内に発言がない場合システムによって自動的にキックされます。<#603841992404893707>チャンネルで「jao」「afa」とあいさつしてみましょう。");
	}
}
