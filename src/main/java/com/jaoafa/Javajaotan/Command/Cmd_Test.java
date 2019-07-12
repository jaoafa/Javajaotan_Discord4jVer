package com.jaoafa.Javajaotan.Command;

import com.jaoafa.Javajaotan.CommandPremise;
import com.jaoafa.Javajaotan.Lib.Library;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class Cmd_Test implements CommandPremise {
	@Override
	public void onCommand(IDiscordClient client, IGuild guild, IChannel channel, IUser author, IMessage message, String[] args){
		// powa command
		channel.sendMessage(
				Boolean.toString(Library.checkOtherServerMember("239487519488475136", author.getStringID()))
		);
	}

    @Override
    public String getDescription() {
        return "試験用コマンド";
    }

    @Override
    public String getUsage() {
        return "/test";
    }
}
