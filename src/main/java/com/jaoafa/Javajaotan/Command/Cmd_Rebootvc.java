package com.jaoafa.Javajaotan.Command;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.jaoafa.Javajaotan.CommandPremise;
import com.jaoafa.Javajaotan.Main;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;

public class Cmd_Rebootvc implements CommandPremise {

	@Override
	public void onCommand(IDiscordClient client, IGuild guild, IChannel channel, IUser author, IMessage message,
			String[] args) {
		String[] command = new String[] {
				"systemctl",
				"restart",
				"VCChannelSpeaker"
		};
		try {
			Process p = Runtime.getRuntime().exec(command);
			p.waitFor(30, TimeUnit.SECONDS);
			RequestBuffer.request(() -> {
				try {
					message.reply("VCChannelSpeakerを再起動しました。必要に応じて`/changechannel`を実行してください。");
				} catch (DiscordException discordexception) {
					Main.DiscordExceptionError(getClass(), channel, discordexception);
				}
			});
		} catch (IOException e) {
			Main.ExceptionReporter(channel, e);
		} catch (InterruptedException e) {
			Main.ExceptionReporter(channel, e);
		}
	}

	@Override
	public String getDescription() {
		return "VCChannelSpeakerを再起動します。";
	}

	@Override
	public String getUsage() {
		return "/rebootvc";
	}

	@Override
	public boolean isjMSOnly() {
		return true;
	}

}
