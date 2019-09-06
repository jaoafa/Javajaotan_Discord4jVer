package com.jaoafa.Javajaotan.Command;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.jaoafa.Javajaotan.CommandPremise;
import com.jaoafa.Javajaotan.Javajaotan;
import com.jaoafa.Javajaotan.Lib.MySQLDBManager;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;

public class Cmd_Dislink implements CommandPremise {
	@Override
	public void onCommand(IDiscordClient client, IGuild guild, IChannel channel, IUser author, IMessage message,
			String[] args) {
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
			PreparedStatement statement = conn.prepareStatement(
					"SELECT * FROM discordlink WHERE disid = ? AND disabled = ?");
			statement.setString(1, author.getStringID());
			statement.setInt(2, 0);
			ResultSet res = statement.executeQuery();
			if (!res.next()) {
				RequestBuffer.request(() -> {
					try {
						message.reply("あなたのDiscordアカウントにリンクされているMinecraftアカウントが見つかりませんでした。");
					} catch (DiscordException discordexception) {
						Javajaotan.DiscordExceptionError(getClass(), channel, discordexception);
					}
				});
				return;
			}
			String name = res.getString("player");
			PreparedStatement statement_update = conn.prepareStatement(
					"UPDATE discordlink SET disabled = ? WHERE disid = ? AND disabled = ?");
			statement_update.setInt(1, 1);
			statement_update.setString(2, author.getStringID());
			statement_update.setInt(3, 0);
			int ret = statement_update.executeUpdate();
			if (ret == 1) {
				RequestBuffer.request(() -> {
					try {
						message.reply("あなたのアカウントに連携されているMinecraftアカウント「" + name + "」の連携を削除しました。");
					} catch (DiscordException discordexception) {
						Javajaotan.DiscordExceptionError(getClass(), channel, discordexception);
					}
				});
			} else {
				RequestBuffer.request(() -> {
					try {
						message.reply("連携を解除出来なかった可能性があります。(ReturnCode: `" + ret + "`)");
					} catch (DiscordException discordexception) {
						Javajaotan.DiscordExceptionError(getClass(), channel, discordexception);
					}
				});
			}

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
		return "「Minecraft-Discord Connect」を解除します。";
	}

	@Override
	public String getUsage() {
		return "/dislink";
	}

	@Override
	public boolean isjMSOnly() {
		return true;
	}

}
