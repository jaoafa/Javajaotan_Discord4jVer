package com.jaoafa.Javajaotan.Command;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.math.NumberUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jaoafa.Javajaotan.CommandPremise;
import com.jaoafa.Javajaotan.Main;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;

public class Cmd_Origin implements CommandPremise {
	Pattern title_pattern = Pattern.compile("<TD nowarp><FONT.+>(.+?)</FONT>");
	Pattern text_pattern = Pattern.compile("<p.*?>([\\s\\S]+?)</p>");

	@Override
	public void onCommand(IDiscordClient client, IGuild guild, IChannel channel, IUser author, IMessage message,
			String[] args) {
		if (args.length != 1) {
			RequestBuffer.request(() -> {
				try {
					message.reply("このコマンドを実行するには、1つの引数が必要です。");
				} catch (DiscordException discordexception) {
					Main.DiscordExceptionError(getClass(), channel, discordexception);
				}
			});
			return;
		}
		String num = args[0];
		if (!NumberUtils.isDigits(num)) {
			RequestBuffer.request(() -> {
				try {
					message.reply("数値を指定してください。");
				} catch (DiscordException discordexception) {
					Main.DiscordExceptionError(getClass(), channel, discordexception);
				}
			});
			return;
		}
		try {
			Path path = Paths.get("/var/jaoafa/discord/kinenbi.json");
			List<String> datas = Files.readAllLines(path);
			JSONObject json = new JSONObject(String.join("\n", datas));
			if (!json.has(num)) {
				RequestBuffer.request(() -> {
					try {
						message.reply("指定された記念日ナンバーの記念日が見つかりませんでした。");
					} catch (DiscordException discordexception) {
						Main.DiscordExceptionError(getClass(), channel, discordexception);
					}
				});
				return;
			}
			String url = json.getString(num);
			OkHttpClient okclient = new OkHttpClient();
			Request request = new Request.Builder().url(url).build();
			Response response = okclient.newCall(request).execute();
			String res = response.body().string();
			Matcher title_matcher = title_pattern.matcher(res);
			if (!title_matcher.find()) {
				RequestBuffer.request(() -> {
					try {
						message.reply("指定された記念日ナンバーの記念日の情報を取得できませんでした。(title|`" + url + "`)");
					} catch (DiscordException discordexception) {
						Main.DiscordExceptionError(getClass(), channel, discordexception);
					}
				});
				return;
			}
			String title = title_matcher.group(1);
			Matcher text_matcher = text_pattern.matcher(res);
			if (!text_matcher.find()) {
				RequestBuffer.request(() -> {
					try {
						message.reply("指定された記念日ナンバーの記念日の情報を取得できませんでした。(text|`" + url + "`)");
					} catch (DiscordException discordexception) {
						Main.DiscordExceptionError(getClass(), channel, discordexception);
					}
				});
				return;
			}
			String text = text_matcher.group(1);
			RequestBuffer.request(() -> {
				try {
					message.reply(title + "```" + text + "```");
				} catch (DiscordException discordexception) {
					Main.DiscordExceptionError(getClass(), channel, discordexception);
				}
			});
		} catch (IOException e) {
			Main.ExceptionReporter(channel, e);
		} catch (JSONException e) {
			Main.ExceptionReporter(channel, e);
		}
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public String getUsage() {
		return null;
	}

	@Override
	public boolean isjMSOnly() {
		return false;
	}

}
