package com.jaoafa.Javajaotan.Event;

import com.jaoafa.Javajaotan.Main;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;

public class Event_MessageReceived {
	/*
		IDiscordClient client = event.getClient();
		IGuild guild = event.getGuild();
		IChannel channel = event.getChannel();
		IUser author = event.getAuthor();
		IMessage message = event.getMessage();
		String text = event.getMessage().getContent();
	 */
	@EventSubscriber
	public void onMessageReceivedEvent(MessageReceivedEvent event) {
		//
		FirstEmojiPin(event);

		// jaotan

	}

	void FirstEmojiPin(MessageReceivedEvent event) {

	}

	void calledjaotan(MessageReceivedEvent event) {
		IChannel channel = event.getChannel();
		IMessage message = event.getMessage();
		String text = event.getMessage().getContent();
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
					Main.DiscordExceptionError(getClass(), channel, discordexception);
				}
			});
		}
		if (text.equals("Jaotan")) {
			RequestBuffer.request(() -> {
				try {
					message.reply("はいっ！お呼びで…はい？\njaotanは``jaotan``であって``Jaotan``じゃないです！人の名前を間違えるなんてひどい！人間のCrime！");
				} catch (DiscordException discordexception) {
					Main.DiscordExceptionError(getClass(), channel, discordexception);
				}
			});
		}
		if (!text.equals("jaotan") && !text.equals("Jaotan") && text.equalsIgnoreCase("jaotan")) {
			// 「jaotan」でもなく「Jaotan」でもないjaotan。つまりjAotanとかjaoTanとか。
			RequestBuffer.request(() -> {
				try {
					message.reply("はいっ！お呼びで…。ああ、論外です。御帰り願います。");
				} catch (DiscordException discordexception) {
					Main.DiscordExceptionError(getClass(), channel, discordexception);
				}
			});
		}
		if (!text.equalsIgnoreCase("jaotan") && text.contains("jaotan")) {
			RequestBuffer.request(() -> {
				try {
					message.reply("はいっ！あっ、呼んだわけではないんですね…");
				} catch (DiscordException discordexception) {
					Main.DiscordExceptionError(getClass(), channel, discordexception);
				}
			});
		}
	}
}
