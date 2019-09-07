package com.jaoafa.Javajaotan.Channel;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;

import com.jaoafa.Javajaotan.ChannelPremise;
import com.jaoafa.Javajaotan.Main;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IMessage.Attachment;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;

public class Channel_597768445601382400 implements ChannelPremise {
	// #nsfw
	@Override
	public void run(IDiscordClient client, IGuild guild, IChannel channel, IUser author, IMessage message,
			boolean edited) {
		if (message.getAttachments().size() == 0) {
			return;
		}
		for (Attachment attachment : message.getAttachments()) {
			try {
				URL url = new URL(attachment.getUrl());
				String fileName = Paths.get(url.getPath()).getFileName().toString();
				if (fileName.startsWith("SPOILER_")) {
					continue;
				}
			} catch (MalformedURLException e) {
				continue;
			}
			RequestBuffer.request(() -> {
				try {
					message.reply("スポイラーの設定がされていないファイルは投稿できません。");
					message.delete();
				} catch (DiscordException discordexception) {
					Main.DiscordExceptionError(getClass(), channel, discordexception);
				}
			});
			return;
		}
	}

	@Override
	public boolean isAlsoTargetEdited() {
		return true;
	}

}
