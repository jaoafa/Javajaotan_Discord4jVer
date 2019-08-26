package com.jaoafa.Javajaotan.Command;

import com.jaoafa.Javajaotan.CommandPremise;
import com.jaoafa.Javajaotan.Javajaotan;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;

public class Cmd_Discordlink implements CommandPremise {
	@Override
	public void onCommand(IDiscordClient client, IGuild guild, IChannel channel, IUser author, IMessage message,
			String[] args) {
		RequestBuffer.request(() -> {
			try {
				message.reply(
						"discordlinkコマンドはDiscordではなくjao Minecraft ServerのMinecraftサーバ内で実行してね！そうしないとあなたのMinecraftアカウントがわからないからね！");
			} catch (DiscordException discordexception) {
				Javajaotan.DiscordExceptionError(getClass(), channel, discordexception);
			}
		});
	}

	@Override
	public String getDescription() {
		return "Discordサーバではdiscordlinkが使用できないことをリプライします。";
	}

	@Override
	public String getUsage() {
		return "/discordlink";
	}

	@Override
	public boolean isjMSOnly() {
		return true;
	}

}
