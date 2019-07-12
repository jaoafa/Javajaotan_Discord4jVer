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
		IChannel channel = event.getGuild().getChannelByID(597419057251090443L);
		channel.sendMessage(":man_dancing:<@" + user.getStringID() + ">(#" + user.getDiscriminator() + ")さん、jao Minecraft Server Discordにようこそ。\n"
			+ "<#597418966335356928>を見ると少しハッピーになれるかも？\n※**ご質問がありますか？お問い合わせ等は<#597423370589700098>まで。**\n"
			+ "\n"
			+ "``/link``コマンドをDiscord内で使用することで、MinecraftアカウントとDiscordアカウントを連携することができます。他の皆さんにあなたのMinecraftアカウントを同時に告知できますから、ぜひ連携してみましょう！");


	}
}
