package com.jaoafa.Javajaotan.Command;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jaoafa.Javajaotan.CommandPremise;
import com.jaoafa.Javajaotan.Javajaotan;
import com.jaoafa.Javajaotan.Lib.ErrorReporter;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;

public class Cmd_Toen implements CommandPremise {
	@Override
	public void onCommand(IDiscordClient client, IGuild guild, IChannel channel, IUser author, IMessage message,
			String[] args) {
		List<String> froms = Arrays.stream(args).filter(
				arg -> arg != null && arg.startsWith("from:")).collect(Collectors.toList());
		String from = "auto";
		if (froms.size() != 0) {
			from = froms.get(0).substring("from:".length() - 1);
		}
		String to = "en";
		List<String> texts = Arrays.stream(args).filter(
				arg -> arg != null && !arg.startsWith("from:") && !arg.startsWith("to:")).collect(Collectors.toList());
		if (texts.size() == 0) {
			RequestBuffer.request(() -> {
				try {
					message.reply("引数が適切ではありません。");
				} catch (DiscordException discordexception) {
					Javajaotan.DiscordExceptionError(getClass(), channel, discordexception);
				}
			});
			return;
		}

		String res = GoogleTranslateWeb(String.join(" ", texts), from, to);
		String source = "GoogleTranslateWeb";
		if (res == null) {
			res = GoogleTranslateGAS(String.join(" ", texts), from, to);
			source = "GoogleTranslateGAS";
		}
		if (res == null) {
			RequestBuffer.request(() -> {
				try {
					message.reply("翻訳に失敗しました。");
				} catch (DiscordException discordexception) {
					Javajaotan.DiscordExceptionError(getClass(), channel, discordexception);
				}
			});
			return;
		}
		final String _res = res;
		final String _from = from;
		final String _to = to;
		final String _source = source;
		RequestBuffer.request(() -> {
			try {
				message.reply("```" + String.join(" ", texts) + "```↓```" + _res + "```(`" + _from + "` -> `" + _to
						+ "` | SOURCE: `" + _source + "`)");
			} catch (DiscordException discordexception) {
				Javajaotan.DiscordExceptionError(getClass(), channel, discordexception);
			}
		});
	}

	private String GoogleTranslateWeb(String text, String from, String to) {
		try {
			String encodeText = URLEncoder.encode(text, "UTF-8");
			String url = "http://translate.google.com/translate_a/t?client=z&sl=" + from + "&tl=" + to + "&text="
					+ encodeText;
			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder().url(url).build();
			Response response = client.newCall(request).execute();
			if (response.code() == 429) {
				return null;
			}
			String res = response.body().string();
			if (res.substring(0, 1).equals("[") && res.substring(res.length() - 1).equals("]")) {
				// json | auto?
				JSONArray json = new JSONArray(res);
				if (json.length() == 2) {
					to = json.getString(1);
					return json.getString(0);
				}
				return res;
			} else if (res.substring(0, 1).equals("\"") && res.substring(res.length() - 1).equals("\"")) {
				return res.substring(1, res.length() - 1);
			} else {
				return res;
			}
			// auto ["こんにちは","en"]
			// other "こんにちは"
		} catch (UnsupportedEncodingException e) {
			ErrorReporter.report(e);
			return null;
		} catch (IOException e) {
			ErrorReporter.report(e);
			return null;
		} catch (JSONException e) {
			ErrorReporter.report(e);
			return null;
		}
	}

	private String GoogleTranslateGAS(String text, String from, String to) {
		if (Javajaotan.translateGAS == null) {
			return null;
		}
		try {
			String url = Javajaotan.translateGAS;
			FormBody.Builder formBuilder = new FormBody.Builder();
			formBuilder.add("text", text);
			formBuilder.add("before", from);
			formBuilder.add("after", to);
			RequestBody body = formBuilder.build();

			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder().url(url).post(body).build();
			Response response = client.newCall(request).execute();
			if (response.code() != 200 && response.code() != 302) {
				return null;
			}
			JSONObject json = new JSONObject(response.body().string());
			String res = json.getJSONObject("params").getJSONObject("parameter").getString("result");
			if (res.equalsIgnoreCase("undefined")) {
				return null;
			}
			return res;
		} catch (UnsupportedEncodingException e) {
			ErrorReporter.report(e);
			return null;
		} catch (IOException e) {
			ErrorReporter.report(e);
			return null;
		}
	}

	@Override
	public String getDescription() {
		return "指定されたテキストを英語(en)に翻訳します。「from:<LANG>」を指定すると元言語を設定できます。指定しないと自動で判定します。";
	}

	@Override
	public String getUsage() {
		return "/toen <Text> [from:LANG]";
	}

	@Override
	public boolean isjMSOnly() {
		return false;
	}
}
