package com.jaoafa.Javajaotan.Command;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.jaoafa.Javajaotan.CommandPremise;
import com.jaoafa.Javajaotan.Lib.ErrorReporter;
import com.jaoafa.Javajaotan.Lib.SQLiteDBManager;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class Cmd_Setnickatama implements CommandPremise {
	@Override
	public void onCommand(IDiscordClient client, IGuild guild, IChannel channel, IUser author, IMessage message, String[] args){
		File sqliteFile = new File("pp_dic_preset.sqlite");
		if(!sqliteFile.exists()){
			// nasa
			channel.sendMessage("動作に必要なファイルが見つかりません。開発部にお問い合わせください。\nReason: " + sqliteFile.getAbsolutePath() + " not found");
			return;
		}
		Connection conn;
		try {
			SQLiteDBManager sqlite = new SQLiteDBManager(sqliteFile);
			conn = sqlite.getConnection();
		} catch (ClassNotFoundException | IOException e) {
			channel.sendMessage("処理に失敗しました。時間を置いてもう一度お試しください。\nReason: " + e.getMessage());
			ErrorReporter.report(e);
			return;
		} catch (SQLException e) {
			channel.sendMessage("処理に失敗しました。時間を置いてもう一度お試しください。\nReason: " + e.getMessage());
			ErrorReporter.report(e);
			return;
		}
		StringBuilder builder = new StringBuilder();
		try {
			PreparedStatement statement_prefix = conn.prepareStatement("SELECT word FROM word_prefix ORDER BY RANDOM() LIMIT ?;");
			statement_prefix.setInt(1, 1);
			ResultSet res_prefix = statement_prefix.executeQuery();
			if(res_prefix.next()){
				builder.append(res_prefix.getString("word"));
			}

			PreparedStatement statement_middle = conn.prepareStatement("SELECT word FROM word_middle ORDER BY RANDOM() LIMIT ?;");
			statement_middle.setInt(1, 1);
			ResultSet res_middle = statement_middle.executeQuery();
			while(res_middle.next()){
				builder.append(res_middle.getString("word"));
			}

			PreparedStatement statement_suffix = conn.prepareStatement("SELECT word FROM word_suffix ORDER BY RANDOM() LIMIT ?;");
			statement_suffix.setInt(1, 1);
			ResultSet res_suffix = statement_suffix.executeQuery();
			while(res_suffix.next()){
				builder.append(res_suffix.getString("word"));
			}
			conn.close();
		} catch (SQLException e) {
			channel.sendMessage("処理に失敗しました。時間を置いてもう一度お試しください。\nReason: " + e.getMessage());
			ErrorReporter.report(e);
			return;
		}
		String oldnick = author.getNicknameForGuild(guild);
		if(oldnick == null) oldnick = "null";
		guild.setUserNickname(author, builder.toString());
		channel.sendMessage("`" + oldnick + "` -> `" + builder.toString() + "`");
	}

    @Override
    public String getDescription() {
        return "アタマな言葉を生成し、実行者のニックネームに設定します。";
    }

    @Override
    public String getUsage() {
        return "/setnickatama [Count]";
    }
}