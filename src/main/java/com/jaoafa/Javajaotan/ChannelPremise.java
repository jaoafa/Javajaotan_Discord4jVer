package com.jaoafa.Javajaotan;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public interface ChannelPremise {
	public void run(IDiscordClient client, IGuild guild, IChannel channel, IUser author, IMessage message);
}
