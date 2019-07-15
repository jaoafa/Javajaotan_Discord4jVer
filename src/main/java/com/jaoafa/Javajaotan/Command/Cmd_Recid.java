package com.jaoafa.Javajaotan.Command;

import com.jaoafa.Javajaotan.CommandPremise;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class Cmd_Recid implements CommandPremise {
	@Override
	public void onCommand(IDiscordClient client, IGuild guild, IChannel channel, IUser author, IMessage message, String[] args){
		channel.sendMessage("はい、調子乗って俺のこと叩いてくるやろ お前ほんまに覚えとけよ ガチで仕返ししたるからな ほんまにキレタ 絶対許さん お前のID控えたからな\n"
				+ "\n"
				+ "`" + author.getStringID() + "` \\_φ(･\\_･");
	}

    @Override
    public String getDescription() {
        return "IDをメモするメッセージを発言されたチャンネルに投稿します。";
    }

    @Override
    public String getUsage() {
        return "/recid";
    }
}
