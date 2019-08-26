package com.jaoafa.Javajaotan;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public interface CommandPremise {
	/**
	 * コマンドが実行されたときに呼び出します。
	 * @param client IDiscordClient
	 * @param guild 送信元のGuild(Discord Server)
	 * @param channel 送信元のチャンネル
	 * @param author 送信者(実行者)
	 * @param message メッセージに関するデータ
	 * @param args 引数(コマンド自体を除く)
	 */
	public void onCommand(final IDiscordClient client, final IGuild guild, final IChannel channel, final IUser author,
			final IMessage message, final String[] args);

	/**
	 * コマンドを説明する文章を指定・返却します
	 * @return　コマンドを説明する文章
	 */
	public String getDescription();

	/**
	 * コマンドの使い方を指定・返却します。
	 * @return コマンドの使い方
	 */
	public String getUsage();

	/**
	 * jMS Gamers Clubのみで使用できるコマンドかどうかを返却します。
	 * @return jMS Gamers Clubのみであればtrue
	 */
	public boolean isjMSOnly();
}
