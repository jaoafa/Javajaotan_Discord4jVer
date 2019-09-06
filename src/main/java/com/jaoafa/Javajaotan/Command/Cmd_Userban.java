package com.jaoafa.Javajaotan.Command;

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

public class Cmd_Userban implements CommandPremise {
	@Override
	public void onCommand(IDiscordClient client, IGuild guild, IChannel channel, IUser author, IMessage message,
			String[] args) {
		IRole[] allowRoles = {
				client.getRoleByID(189381504059572224L), // old jGC Admin
				client.getRoleByID(281699181410910230L), // old jGC Moderator
				client.getRoleByID(597405109290532864L), // new jGC Admin
				client.getRoleByID(597405110683041793L), // new jGC Moderator
		};
		if (!Library.isAllowRole(author, allowRoles)) {
			RequestBuffer.request(() -> {
				try {
					message.reply("実行しようとしたコマンドはこのチャンネルでは使用できません。");
				} catch (DiscordException discordexception) {
					Javajaotan.DiscordExceptionError(getClass(), channel, discordexception);
				}
			});
			return;
		}
		if (args.length != 1) {
			RequestBuffer.request(() -> {
				try {
					message.reply("引数が足りません。");
				} catch (DiscordException discordexception) {
					Javajaotan.DiscordExceptionError(getClass(), channel, discordexception);
				}
			});
			return;
		}
		if (!Library.isLong(args[0])) {
			RequestBuffer.request(() -> {
				try {
					message.reply("指定されたユーザーIDは適切ではありません。");
				} catch (DiscordException discordexception) {
					Javajaotan.DiscordExceptionError(getClass(), channel, discordexception);
				}
			});
			return;
		}
		long userid = Long.valueOf(args[0]);
		IUser user = client.fetchUser(userid);
		if (user != null) {
			guild.banUser(user);
			String name = user.getName();
			String discriminator = user.getDiscriminator();
			RequestBuffer.request(() -> {
				try {
					message.reply("指定されたユーザー「" + name + "#" + discriminator + "」(" + userid + ")をBanしました。");
				} catch (DiscordException discordexception) {
					Javajaotan.DiscordExceptionError(getClass(), channel, discordexception);
				}
			});
			return;
		} else {
			guild.banUser(userid);
			RequestBuffer.request(() -> {
				try {
					message.reply("指定されたユーザー(" + userid + ")をBanしました。");
				} catch (DiscordException discordexception) {
					Javajaotan.DiscordExceptionError(getClass(), channel, discordexception);
				}
			});
		}
	}

	@Override
	public String getDescription() {
		return "指定されたユーザーをBanします。特定のチャンネルでのみ使用できます。";
	}

	@Override
	public String getUsage() {
		return "/userban <UserID>";
	}

	@Override
	public boolean isjMSOnly() {
		return true;
	}
}
