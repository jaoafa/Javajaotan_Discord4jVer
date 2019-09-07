package com.jaoafa.Javajaotan.Command;

import java.util.Arrays;
import java.util.HashSet;

import com.jaoafa.Javajaotan.CommandPremise;
import com.jaoafa.Javajaotan.Main;
import com.jaoafa.Javajaotan.Lib.Library;
import com.jaoafa.Javajaotan.Lib.MuteManager;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;

public class Cmd_Mute implements CommandPremise {
	@Override
	public void onCommand(IDiscordClient client, IGuild guild, IChannel channel, IUser author, IMessage message,
			String[] args) {
		// Admin & Moderator ONLY Command
		if (!Library.hasAdminModeratorRole(guild, author)) {
			RequestBuffer.request(() -> {
				try {
					message.reply("あなたはこのコマンドを使用できません。");
				} catch (DiscordException discordexception) {
					Main.DiscordExceptionError(getClass(), channel, discordexception);
				}
			});
			return;
		}
		String[] allowChannel = new String[] {
				"222002864875110400", // admin
				"228771335499808769", // meeting
				"293856671799967744", // toma_lab

				"597423654451675137", // new admin
				"597423467796758529", // new meeting
		};
		if (!Arrays.asList(allowChannel).contains(channel.getStringID())) {
			RequestBuffer.request(() -> {
				try {
					message.reply("このチャンネルではこのコマンドを使用できません。");
				} catch (DiscordException discordexception) {
					Main.DiscordExceptionError(getClass(), channel, discordexception);
				}
			});
			return;
		}
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("add")) {
				// mute add
				String userid = args[1];
				if (MuteManager.isMuted(userid)) {
					// already
					RequestBuffer.request(() -> {
						try {
							message.reply("指定されたユーザーは既にミュートされています。");
						} catch (DiscordException discordexception) {
							Main.DiscordExceptionError(getClass(), channel, discordexception);
						}
					});
					return;
				}
				if (!Library.isLong(userid)) {
					RequestBuffer.request(() -> {
						try {
							message.reply("数値を指定してください。");
						} catch (DiscordException discordexception) {
							Main.DiscordExceptionError(getClass(), channel, discordexception);
						}
					});
					return;
				}
				MuteManager.addMuteList(userid);
				RequestBuffer.request(() -> {
					try {
						message.reply("ミュートリストに追加しました : <@" + userid + ">");
					} catch (DiscordException discordexception) {
						Main.DiscordExceptionError(getClass(), channel, discordexception);
					}
				});
				return;
			} else if (args[0].equalsIgnoreCase("remove")) {
				// mute remove
				String userid = args[1];
				if (!MuteManager.isMuted(userid)) {
					// already
					RequestBuffer.request(() -> {
						try {
							message.reply("指定されたユーザーはミュートされていません。");
						} catch (DiscordException discordexception) {
							Main.DiscordExceptionError(getClass(), channel, discordexception);
						}
					});
					return;
				}
				if (!Library.isLong(userid)) {
					RequestBuffer.request(() -> {
						try {
							message.reply("数値を指定してください。");
						} catch (DiscordException discordexception) {
							Main.DiscordExceptionError(getClass(), channel, discordexception);
						}
					});
					return;
				}
				MuteManager.removeMuteList(userid);
				RequestBuffer.request(() -> {
					try {
						message.reply("ミュートリストから解除しました : <@" + userid + ">");
					} catch (DiscordException discordexception) {
						Main.DiscordExceptionError(getClass(), channel, discordexception);
					}
				});
				return;
			}
		} else if (args.length == 1) {
			if (args[0].equalsIgnoreCase("list")) {
				HashSet<String> list = MuteManager.refreshMuteList();
				HashSet<String> replyList = new HashSet<>();
				for (String userid : list) {
					IUser user = client.fetchUser(Long.parseLong(userid));
					if (user != null) {
						replyList.add(user.getName() + "#" + user.getDiscriminator());
					} else {
						replyList.add(userid);
					}
				}
				RequestBuffer.request(() -> {
					try {
						message.reply("ミュートリスト```" + String.join(", ", replyList) + "```");
					} catch (DiscordException discordexception) {
						Main.DiscordExceptionError(getClass(), channel, discordexception);
					}
				});
				return;
			}
			RequestBuffer.request(() -> {
				try {
					message.reply(getUsage());
				} catch (DiscordException discordexception) {
					Main.DiscordExceptionError(getClass(), channel, discordexception);
				}
			});
			return;
		}
	}

	@Override
	public String getDescription() {
		return "ミュート機能の制御ができます。運営のみ利用可能で、特定のチャンネルでのみ使用できます。";
	}

	@Override
	public String getUsage() {
		return "/mute <add|remove|list> [UserID]";
	}

	@Override
	public boolean isjMSOnly() {
		return true;
	}
}