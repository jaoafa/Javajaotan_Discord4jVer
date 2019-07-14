package com.jaoafa.Javajaotan.Lib;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MuteManager {
	private static List<String> mutes = null;
	public static void refreshMuteList(){
		if(mutes != null) mutes.clear();
		mutes = new ArrayList<>();

		File sqliteFile = new File("mutes.db");
		if(!sqliteFile.exists()){
			// nasa
			return;
		}
		Connection conn;
		try {
			SQLiteDBManager sqlite = new SQLiteDBManager(sqliteFile);
			conn = sqlite.getConnection();
		} catch (ClassNotFoundException | IOException e) {
			ErrorReporter.report(e);
			return;
		} catch (SQLException e) {
			ErrorReporter.report(e);
			return;
		}
		try {
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM users;");
			ResultSet res = statement.executeQuery();
			while(res.next()){
				mutes.add(res.getString("userid"));
			}
			conn.close();
		} catch (SQLException e) {
			ErrorReporter.report(e);
			return;
		}
	}
	public static void saveMuteList(){
		if(mutes == null) return;
		File sqliteFile = new File("mutes.db");
		Connection conn;
		try {
			SQLiteDBManager sqlite = new SQLiteDBManager(sqliteFile);
			conn = sqlite.getConnection();
		} catch (ClassNotFoundException | IOException e) {
			ErrorReporter.report(e);
			return;
		} catch (SQLException e) {
			ErrorReporter.report(e);
			return;
		}
		try {
			for(String userid : mutes){
				PreparedStatement statement = conn.prepareStatement("insert into users values(?);");
				statement.setString(1, userid);
				statement.executeUpdate();
			}
			conn.close();
		} catch (SQLException e) {
			ErrorReporter.report(e);
			return;
		}
	}
	public static boolean isMuted(String userid){
		if(mutes == null) refreshMuteList();
		return mutes.contains(userid);
	}
	public static void addMuteList(String userid){
		if(mutes == null) refreshMuteList();
		mutes.add(userid);
		saveMuteList();
	}
}
