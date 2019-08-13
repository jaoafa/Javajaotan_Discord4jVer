package com.jaoafa.Javajaotan.Command;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.RandomStringUtils;

import com.jaoafa.Javajaotan.CommandPremise;
import com.jaoafa.Javajaotan.Javajaotan;
import com.jaoafa.Javajaotan.Lib.Library;
import com.jaoafa.Javajaotan.Lib.MySQLDBManager;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IPrivateChannel;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;

public class Cmd_Link implements CommandPremise {
	@Override
	public void onCommand(IDiscordClient client, IGuild guild, IChannel channel, IUser author, IMessage message,
			String[] args) {
		if (!Library.isNewjMSDiscordServer(guild)) {
			return; // 新しいjMSDiscordServer以外では動作させない
		}
		MySQLDBManager MySQLDBManager = Javajaotan.MySQLDBManager;
		if (MySQLDBManager == null) {
			RequestBuffer.request(() -> {
				try {
					message.reply("データベースサーバに接続できません。時間をおいて再度お試しください。(`MySQLDBManager null`)");
				} catch (DiscordException discordexception) {
					Javajaotan.DiscordExceptionError(getClass(), channel, discordexception);
				}
			});
			return;
		}
		try {
			Connection conn = MySQLDBManager.getConnection();
			String authkey = getAuthKey(conn);
			if (authkey == null) {
				RequestBuffer.request(() -> {
					try {
						message.reply("AuthKeyを生成できませんでした。時間をおいて再度お試しください。");
					} catch (DiscordException discordexception) {
						Javajaotan.DiscordExceptionError(getClass(), channel, discordexception);
					}
				});
				return;
			}
			String name = author.getName();
			String disid = author.getStringID();
			String discriminator = author.getDiscriminator();
			PreparedStatement statement = conn.prepareStatement(
					"INSERT INTO discordlink_waiting (authkey, name, disid, discriminator) VALUES (?, ?, ?, ?);");
			statement.setString(1, authkey);
			statement.setString(2, name);
			statement.setString(3, disid);
			statement.setString(4, discriminator);
			statement.executeUpdate();

			IPrivateChannel dm = author.getOrCreatePMChannel();
			RequestBuffer.request(() -> {
				try {
					dm.sendMessage("このメッセージはjao Minecraft Server Discordのアカウント認証メッセージです。\n"
							+ "**jao Minecraft Serverに入り**、以下コマンドを実行してください。");
					dm.sendMessage("```/discordlink " + authkey + "```");

					channel.sendMessage("<@" + author.getStringID() + "> 個人メッセージに送信されたメッセージを確認し、指定された行動を行って下さい。\n"
							+ "メッセージが送信されてきませんか？何度か実行し直して正常動作しなければ開発部にお問い合わせをお願いします！\n"
							+ "\n"
							+ "なお、原則**メールアドレスを登録したアカウントでのリンク**をお願いしています。それ以外のアカウントでリンクするとログイン出来なくなる可能性があります。");
				} catch (DiscordException discordexception) {
					Javajaotan.DiscordExceptionError(getClass(), channel, discordexception);
				}
			});
			return;
		} catch (SQLException e) {
			RequestBuffer.request(() -> {
				try {
					message.reply("データベースサーバに接続できません。時間をおいて再度お試しください。\n"
							+ "**Message**: `" + e.getMessage() + "`");
				} catch (DiscordException discordexception) {
					Javajaotan.DiscordExceptionError(getClass(), channel, discordexception);
				}
			});
			return;
		}
	}

	@Override
	public String getDescription() {
		return "「Minecraft-Discord Connect」を行うためのAuthKeyを発行します。";
	}

	@Override
	public String getUsage() {
		return "/link";
	}

	String getAuthKey(Connection conn) throws SQLException {
		String authkey = null;
		while (true) {
			authkey = RandomStringUtils.randomAlphabetic(10);
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM discordlink_waiting WHERE authkey = ?");
			statement.setString(1, authkey);
			ResultSet res = statement.executeQuery();
			if (!res.next()) {
				return authkey;
			}
		}
	}
}
