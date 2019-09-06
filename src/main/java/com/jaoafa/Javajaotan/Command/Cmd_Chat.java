package com.jaoafa.Javajaotan.Command;

import java.util.Arrays;

import com.jaoafa.Javajaotan.CommandPremise;
import com.jaoafa.Javajaotan.Javajaotan;
import com.jaoafa.Javajaotan.Lib.Library;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;

public class Cmd_Chat implements CommandPremise {
	@Override
	public void onCommand(IDiscordClient client, IGuild guild, IChannel channel, IUser author, IMessage message,
			String[] args) {
		IRole[] allowRoles = {
				client.getRoleByID(189381504059572224L), // old jGC Admin
				client.getRoleByID(281699181410910230L), // old jGC Moderator
				client.getRoleByID(597405109290532864L), // new jGC Admin
				client.getRoleByID(597405110683041793L), // new jGC Moderator
		};
		IChannel _sendToChannel = channel;
		if (Library.isAllowRole(author, allowRoles)) {
			// チャンネル指定可
			if (args.length >= 2) {
				try {
					long channelID = Long.valueOf(args[0]);
					if (client.getChannelByID(channelID) == null) {
						RequestBuffer.request(() -> {
							try {
								message.reply("指定されたチャンネルが見つかりません。");
							} catch (DiscordException discordexception) {
								Javajaotan.DiscordExceptionError(getClass(), channel, discordexception);
							}
						});
						return;
					}
					_sendToChannel = client.getChannelByID(channelID);
				} catch (NumberFormatException e) {
					// チャンネル指定しているわけではない？
				}
			}
		}
		String content;
		if (_sendToChannel.getLongID() == channel.getLongID()) {
			// チャンネル指定なし
			content = String.join(" ", args);
		} else {
			// チャンネル指定あり
			content = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
		}
		final IChannel sendToChannel = _sendToChannel; // 実装としてどうなんだろう？
		RequestBuffer.request(() -> {
			try {
				sendToChannel.sendMessage(content);
			} catch (DiscordException discordexception) {
				Javajaotan.DiscordExceptionError(getClass(), channel, discordexception);
			}
		});
	}

	@Override
	public String getDescription() {
		return "指定されたチャンネルにチャットを送信します。運営未満の権限の利用者はチャンネルの指定ができません。";
	}

	@Override
	public String getUsage() {
		return "/chat [ChannelID] <Message...>";
	}

	@Override
	public boolean isjMSOnly() {
		return true;
	}
}
