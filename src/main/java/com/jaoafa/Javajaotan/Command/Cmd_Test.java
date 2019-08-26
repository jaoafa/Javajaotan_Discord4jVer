package com.jaoafa.Javajaotan.Command;

import com.jaoafa.Javajaotan.CommandPremise;
import com.jaoafa.Javajaotan.Lib.ErrorReporter;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class Cmd_Test implements CommandPremise {
	@Override
	public void onCommand(IDiscordClient client, IGuild guild, IChannel channel, IUser author, IMessage message,
			String[] args) {
		ErrorReporter.report(new NullPointerException("NullPointerException"));
	}

	@Override
	public String getDescription() {
		return "試験用コマンド";
	}

	@Override
	public String getUsage() {
		return "/test";
	}

	@Override
	public boolean isjMSOnly() {
		return true;
	}
}
