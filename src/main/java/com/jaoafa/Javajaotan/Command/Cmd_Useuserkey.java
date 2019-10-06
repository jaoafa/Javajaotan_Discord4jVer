package com.jaoafa.Javajaotan.Command;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.jaoafa.Javajaotan.CommandPremise;
import com.jaoafa.Javajaotan.Main;
import com.jaoafa.Javajaotan.Lib.Library;
import com.jaoafa.Javajaotan.Lib.MySQLDBManager;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;

public class Cmd_Useuserkey implements CommandPremise {
	@Override
	public void onCommand(IDiscordClient client, IGuild guild, IChannel channel, IUser author, IMessage message,
			String[] args) {
		IRole[] allowRoles = {
				client.getRoleByID(189381504059572224L), // old jGC Admin
				client.getRoleByID(281699181410910230L), // old jGC Moderator
				client.getRoleByID(597405109290532864L), // new jGC Admin
				client.getRoleByID(597405110683041793L), // new jGC Moderator
		};
		if (!Library.isAllowRole(author, allowRoles)) {
			RequestBuffer.request(() -> {
				try {
					message.reply("あなたはこのコマンドを利用できません。");
				} catch (DiscordException discordexception) {
					Main.DiscordExceptionError(getClass(), channel, discordexception);
				}
			});
			return;
		}
		if (args.length == 0) {
			RequestBuffer.request(() -> {
				try {
					message.reply("第一引数にUserKeyを指定してください。");
				} catch (DiscordException discordexception) {
					Main.DiscordExceptionError(getClass(), channel, discordexception);
				}
			});
			return;
		}
		String userkey = args[0];
		MySQLDBManager MySQLDBManager = Main.MySQLDBManager;
		if (MySQLDBManager == null) {
			RequestBuffer.request(() -> {
				try {
					message.reply("データベースサーバに接続できません。時間をおいて再度お試しください。(`MySQLDBManager null`)");
				} catch (DiscordException discordexception) {
					Main.DiscordExceptionError(getClass(), channel, discordexception);
				}
			});
			return;
		}
		try {
			Connection conn = MySQLDBManager.getConnection();
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM userkey WHERE userkey = ?");
			statement.setString(1, userkey);
			ResultSet res = statement.executeQuery();
			if (res.next()) {
				String name = res.getString("player");
				String uuid = res.getString("uuid");
				statement.close();
				RequestBuffer.request(() -> {
					try {
						message.reply("指定されたキー「`" + userkey + "`」を利用済みにしました。\n"
								+ "**MinecraftID**: `" + name + "`\n"
								+ "**MinecraftUUID**: `" + uuid + "`\n"
								+ "**UserPage**: https://jaoafa.com/user/" + uuid);
					} catch (DiscordException discordexception) {
						Main.DiscordExceptionError(getClass(), channel, discordexception);
					}
				});

				PreparedStatement statement_used = conn
						.prepareStatement("UPDATE userkey SET used = ? WHERE userkey = ?");
				statement_used.setBoolean(1, true);
				statement_used.setString(2, userkey);
				statement_used.executeUpdate();
				statement_used.close();
				return;
			} else {
				statement.close();
				RequestBuffer.request(() -> {
					try {
						message.reply("指定されたキー「`" + userkey + "`」は見つかりませんでした。");
					} catch (DiscordException discordexception) {
						Main.DiscordExceptionError(getClass(), channel, discordexception);
					}
				});
				return;
			}
		} catch (SQLException e) {
			RequestBuffer.request(() -> {
				try {
					message.reply("データベースサーバに接続できません。時間をおいて再度お試しください。\n"
							+ "**Message**: `" + e.getMessage() + "`");
				} catch (DiscordException discordexception) {
					Main.DiscordExceptionError(getClass(), channel, discordexception);
				}
			});
			return;
		}
	}

	@Override
	public String getDescription() {
		return "UserKeyを利用済みにします。特定の権限を持ったユーザーのみ使用可能です。";
	}

	@Override
	public String getUsage() {
		return "/useuserkey <UserKey>";
	}

	@Override
	public boolean isjMSOnly() {
		return true;
	}
}
