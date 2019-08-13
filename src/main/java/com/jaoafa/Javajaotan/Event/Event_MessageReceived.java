package com.jaoafa.Javajaotan.Event;

import com.jaoafa.Javajaotan.Javajaotan;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;

public class Event_MessageReceived {
	@EventSubscriber
	public void onMessageReceivedEvent(MessageReceivedEvent event) {
		IDiscordClient client = event.getClient();
		IGuild guild = event.getGuild();
		IChannel channel = event.getChannel();
		IUser author = event.getAuthor();
		IMessage message = event.getMessage();
		String text = event.getMessage().getContent();

		// ピン
		if (text.startsWith("📌")) {
			try {
				channel.pin(message);
			} catch (DiscordException e) {
				message.addReaction(ReactionEmoji.of("❌")); // :x:
				RequestBuffer.request(() -> {
					try {
						message.reply("メッセージをピン止めするのに失敗しました…。```" + e.getErrorMessage() + "```");
					} catch (DiscordException discordexception) {
						Javajaotan.DiscordExceptionError(getClass(), channel, discordexception);
					}
				});
			}
			message.addReaction(ReactionEmoji.of("📌")); // :pushpin:
		}

		// jaotan
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
		if (!text.equalsIgnoreCase("jaotan") && text.contains("jaotan")) {
			RequestBuffer.request(() -> {
				try {
					message.reply("はいっ！あっ、呼んだわけではないんですね…");
				} catch (DiscordException discordexception) {
					Javajaotan.DiscordExceptionError(getClass(), channel, discordexception);
				}
			});
		}
	}
}
