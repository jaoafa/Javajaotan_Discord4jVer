package com.jaoafa.Javajaotan.Command;

import com.jaoafa.Javajaotan.CommandPremise;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class Cmd_Blookup implements CommandPremise {
	@Override
	public void onCommand(IDiscordClient client, IGuild guild, IChannel channel, IUser author, IMessage message, String[] args){
		if(args.length == 0){
			channel.sendMessage("**Description**: `" + getDescription() + "`\n**Usage**: `" + getUsage() + "`");
			return;
		}
		channel.sendMessage("ブロック編集等のデータ: https://jaoafa.com/tomachi/co.php?player=" + args[0]);
	}

    @Override
    public String getDescription() {
        return "ブロック編集等のデータのURLを発言されたチャンネルに投稿します。";
    }

    @Override
    public String getUsage() {
        return "/blookup <PlayerID>";
    }
}
