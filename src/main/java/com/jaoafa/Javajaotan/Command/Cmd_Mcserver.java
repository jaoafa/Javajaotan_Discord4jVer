package com.jaoafa.Javajaotan.Command;

import com.jaoafa.Javajaotan.CommandPremise;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class Cmd_Mcserver implements CommandPremise {
	@Override
	public void onCommand(IDiscordClient client, IGuild guild, IChannel channel, IUser author, IMessage message,
			String[] args) {
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("start")) {

			}
		}
	}

	@Override
	public String getDescription() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public String getUsage() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public boolean isjMSOnly() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

}
