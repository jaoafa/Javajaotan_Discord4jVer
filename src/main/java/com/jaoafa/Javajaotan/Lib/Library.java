package com.jaoafa.Javajaotan.Lib;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

public class Library {
	/**
	 * 他Discordサーバのウィジェットから、そのサーバに指定した利用者がいるかを判定します
	 * @param guild_id 他DiscordサーバのGuildID
	 * @param user_id 判定する利用者のID
	 * @return 居ればtrue (オンラインのみ)
	 */
	public static Boolean checkOtherServerMember(String guild_id, String user_id){
		Map<String, String> headers = new HashMap<>();
		headers.put("User-Agent", "DiscordBot (https://jaoafacom, v0.1)");
		JSONObject obj = getHttpsJson("https://discordapp.com/api/guilds/" + guild_id + "/widget.json", headers);
		if(obj.has("code") && obj.getInt("code") == 50004){
			// disabled
			return false;
		}
		JSONArray members = obj.getJSONArray("members");
		for(Object o : members){
			if (!(o instanceof JSONObject)){
				continue;
			}
			JSONObject one = (JSONObject) o;
			if(user_id.equals(one.getString("id"))){
				return true;
			}
		}
		return false;
	}
	public static JSONObject getHttpsJson(String address, Map<String, String> headers){
		StringBuilder builder = new StringBuilder();
		try{
			URL url = new URL(address);

			HttpsURLConnection connect = (HttpsURLConnection) url.openConnection();
			connect.setRequestMethod("GET");
			if(headers != null){
				for(Map.Entry<String, String> header : headers.entrySet()) {
					connect.setRequestProperty(header.getKey(), header.getValue());
				}
			}

			connect.connect();

			if(connect.getResponseCode() != HttpURLConnection.HTTP_OK){
				InputStream in = connect.getErrorStream();

				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
				in.close();
				connect.disconnect();

				System.out.println("ConnectWARN: " + connect.getResponseMessage());
				return null;
			}

			InputStream in = connect.getInputStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			in.close();
			connect.disconnect();
			JSONObject json = new JSONObject(builder.toString());
			return json;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	public static String getCurrentpath(){
		String cp=System.getProperty("java.class.path");
		String fs=System.getProperty("file.separator");
		String acp=(new File(cp)).getAbsolutePath();
		int p,q;
		for(p=0;(q=acp.indexOf(fs,p))>=0;p=q+1);
		return acp.substring(0,p);
	}
	public static boolean isAllowRole(IUser author, IRole[] roles){
		for(IRole role : roles){
			if(author.hasRole(role)){
				return true;
			}
		}
		return false;
	}
	@Deprecated
	public static String implode(String delimiter, String elements){
		return String.join(delimiter, elements);
	}
}
