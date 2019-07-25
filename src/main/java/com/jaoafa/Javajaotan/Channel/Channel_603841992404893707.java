package com.jaoafa.Javajaotan.Channel;

import com.jaoafa.Javajaotan.ChannelPremise;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

public class Channel_603841992404893707 implements ChannelPremise {
	// #greeting 603841992404893707
	@Override
	public void run(IDiscordClient client, IGuild guild, IChannel channel, IUser author, IMessage message) {
		if(message.isSystemMessage()) {
			return;
		}
		if(!message.getContent().equals("jao") && !message.getContent().equals("afa")) {
			message.delete();
			return;
		}
		IRole role = client.getRoleByID(597421078817669121L);
		if(role == null) {
			channel.sendMessage("<@221991565567066112> ROLE IS NOT FOUND");
			return;
		}
		if(message.getContent().equals("jao")) {
			if(!author.hasRole(role)) {
				author.addRole(role);
				message.addReaction(ReactionEmoji.of("\u2B55")); // o
			}else{
				message.addReaction(ReactionEmoji.of("\u274C")); // x
			}
		}
	}
}