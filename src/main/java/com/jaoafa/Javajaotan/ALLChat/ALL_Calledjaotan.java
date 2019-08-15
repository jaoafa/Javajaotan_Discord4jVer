package com.jaoafa.Javajaotan.ALLChat;

import com.jaoafa.Javajaotan.ALLChatPremise;
import com.jaoafa.Javajaotan.Javajaotan;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;

public class ALL_Calledjaotan implements ALLChatPremise {
	@Override
	public void run(IDiscordClient client, IGuild guild, IChannel channel, IUser author, IMessage message,
			boolean edited) {
		String text = message.getContent();
		if ((channel.getName().contains("server") && channel.getName().contains("chat"))
				|| channel.getName().contains("console")) {
			// チャンネル名に「server」と「chat」が含まれるチャンネル
			// 「console」が含まれるチャンネル
			// は無効化
			return;
		}
		if (text.equals("jaotan")) {
			RequestBuffer.request(() -> {
				try {
					message.reply("はいっ！お呼びですか？");
				} catch (DiscordException discordexception) {
					Javajaotan.DiscordExceptionError(getClass(), channel, discordexception);
				}
			});
		}
		if (text.equals("Jaotan")) {
			RequestBuffer.request(() -> {
				try {
					message.reply("はいっ！お呼びで…はい？\njaotanは``jaotan``であって``Jaotan``じゃないです！人の名前を間違えるなんてひどい！人間のCrime！");
				} catch (DiscordException discordexception) {
					Javajaotan.DiscordExceptionError(getClass(), channel, discordexception);
				}
			});
		}
		if (!text.equals("jaotan") && !text.equals("Jaotan") && text.equalsIgnoreCase("jaotan")) {
			// 「jaotan」でもなく「Jaotan」でもないjaotan。つまりjAotanとかjaoTanとか。
			RequestBuffer.request(() -> {
				try {
					message.reply("はいっ！お呼びで…。ああ、論外です。御帰り願います。");
				} catch (DiscordException discordexception) {
					Javajaotan.DiscordExceptionError(getClass(), channel, discordexception);
				}
			});
		}
		/*
		if (!text.equalsIgnoreCase("jaotan") && text.contains("jaotan")) {
			RequestBuffer.request(() -> {
				try {
					message.reply("はいっ！あっ、呼んだわけではないんですね…");
				} catch (DiscordException discordexception) {
					Javajaotan.DiscordExceptionError(getClass(), channel, discordexception);
				}
			});
		}
		*/
	}

	@Override
	public boolean isAlsoTargetEdited() {
		return false;
	}
}
