package com.jaoafa.Javajaotan.Channel;

import java.util.ArrayList;
import java.util.List;

import com.jaoafa.Javajaotan.ChannelPremise;
import com.jaoafa.Javajaotan.Main;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;

public class Channel_603841992404893707 implements ChannelPremise {
	// #greeting 603841992404893707
	private static List<Long> jaoPlayers = new ArrayList<Long>();

	@Override
	public void run(IDiscordClient client, IGuild guild, IChannel channel, IUser author, IMessage message,
			boolean edited) {
		if (message.isSystemMessage()) {
			return;
		}
		if (!message.getContent().equals("jao") && !message.getContent().equals("afa")) {
			message.delete();
			return;
		}
		IRole role = client.getRoleByID(597421078817669121L);
		if (role == null) {
			RequestBuffer.request(() -> {
				try {
					message.reply("<@221991565567066112> ROLE IS NOT FOUND");
				} catch (DiscordException discordexception) {
					Main.DiscordExceptionError(getClass(), channel, discordexception);
				}
			});
			return;
		}
		if (message.getContent().equals("jao")) {
			if (!author.hasRole(role)) {
				RequestBuffer.request(() -> {
					try {
						author.addRole(role);
						message.addReaction(ReactionEmoji.of("\u2B55")); // o
					} catch (DiscordException discordexception) {
						Main.DiscordExceptionError(getClass(), channel, discordexception);
					}
				});
				jaoPlayers.add(author.getLongID());
			} else {
				RequestBuffer.request(() -> {
					try {
						message.addReaction(ReactionEmoji.of("\u274C")); // x
					} catch (DiscordException discordexception) {
						Main.DiscordExceptionError(getClass(), channel, discordexception);
					}
				});
			}
		} else if (message.getContent().equals("afa")) {
			if (!jaoPlayers.contains(author.getLongID())) {
				RequestBuffer.request(() -> {
					try {
						message.addReaction(ReactionEmoji.of("\u274C")); // x
					} catch (DiscordException discordexception) {
						Main.DiscordExceptionError(getClass(), channel, discordexception);
					}
				});
				return;
			}
			RequestBuffer.request(() -> {
				try {
					message.reply("あいさつしていただきありがとうございます！これにより、多くのチャンネルを閲覧できるようになりました。\n" +
							"このあとは<#597419057251090443>などで「`/link`」を実行(投稿)して、MinecraftアカウントとDiscordアカウントを連携しましょう！");
				} catch (DiscordException discordexception) {
					Main.DiscordExceptionError(getClass(), channel, discordexception);
				}
			});
			jaoPlayers.remove(author.getLongID());
		}
	}

	@Override
	public boolean isAlsoTargetEdited() {
		return true;
	}
}
