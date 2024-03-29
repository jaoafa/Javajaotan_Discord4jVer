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

public class Cmd_Blookup implements CommandPremise {
	@Override
	public void onCommand(IDiscordClient client, IGuild guild, IChannel channel, IUser author, IMessage message,
			String[] args) {
		if (args.length == 0) {
			message.reply("**Description**: `" + getDescription() + "`\n**Usage**: `" + getUsage() + "`");
			return;
		}
		RequestBuffer.request(() -> {
			try {
				message.reply("ブロック編集等のデータ: https://jaoafa.com/tomachi/co.php?player=" + args[0]);
			} catch (DiscordException discordexception) {
				Main.DiscordExceptionError(getClass(), channel, discordexception);
			}
		});
	}

	@Override
	public String getDescription() {
		return "ブロック編集等のデータのURLを発言されたチャンネルに投稿します。";
	}

	@Override
	public String getUsage() {
		return "/blookup <PlayerID>";
	}

	@Override
	public boolean isjMSOnly() {
		return true;
	}
}
