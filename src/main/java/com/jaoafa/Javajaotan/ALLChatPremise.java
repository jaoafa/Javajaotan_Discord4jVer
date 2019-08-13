package com.jaoafa.Javajaotan;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public interface ALLChatPremise {
	/**
	 * チャンネルでメッセージが送信されたときに呼び出します。
	 * @param client IDiscordClient
	 * @param guild 送信元のGuild(Discord Server)
	 * @param channel 送信元のチャンネル
	 * @param author 送信者(実行者)
	 * @param message メッセージに関するデータ
	 * @param edited 編集による呼び出しかどうか
	 */
	public void run(IDiscordClient client, IGuild guild, IChannel channel, IUser author, IMessage message,
			boolean edited);

	/**
	 * 編集された場合でも呼び出すかを指定・確認します。
	 * @return 編集された場合でも呼び出すか
	 */
	public boolean isAlsoTargetEdited();
}
