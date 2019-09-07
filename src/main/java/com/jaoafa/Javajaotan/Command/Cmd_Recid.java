package com.jaoafa.Javajaotan.Command;

import com.jaoafa.Javajaotan.CommandPremise;
import com.jaoafa.Javajaotan.Main;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;

public class Cmd_Recid implements CommandPremise {
	@Override
	public void onCommand(IDiscordClient client, IGuild guild, IChannel channel, IUser author, IMessage message,
			String[] args) {
		RequestBuffer.request(() -> {
			try {
				message.reply("はい、調子乗って俺のこと叩いてくるやろ お前ほんまに覚えとけよ ガチで仕返ししたるからな ほんまにキレタ 絶対許さん お前のID控えたからな\n"
						+ "\n"
						+ "`" + author.getStringID() + "` \\_φ(･\\_･");
			} catch (DiscordException discordexception) {
				Main.DiscordExceptionError(getClass(), channel, discordexception);
			}
		});
	}

	@Override
	public String getDescription() {
		return "IDをメモするメッセージを発言されたチャンネルに投稿します。";
	}

	@Override
	public String getUsage() {
		return "/recid";
	}

	@Override
	public boolean isjMSOnly() {
		return false;
	}
}
