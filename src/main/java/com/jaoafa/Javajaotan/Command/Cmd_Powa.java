package com.jaoafa.Javajaotan.Command;

import com.jaoafa.Javajaotan.CommandPremise;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class Cmd_Powa implements CommandPremise {
	@Override
	public void onCommand(IDiscordClient client, IGuild guild, IChannel channel, IUser author, IMessage message, String[] args){
		// powa command
		channel.sendMessage("ポわ～～～～～～～ｗｗｗｗ！！！ｗ！ｗｗ！ｗ！ｗ");
	}

    @Override
    public String getDescription() {
        return "「ポわ～～～～～～～ｗｗｗｗ！！！ｗ！ｗｗ！ｗ！ｗ」を発言されたチャンネルに投稿します。";
    }

    @Override
    public String getUsage() {
        return "/powa";
    }
}
