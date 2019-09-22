package com.jaoafa.Javajaotan.Channel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import com.jaoafa.Javajaotan.ChannelPremise;
import com.jaoafa.Javajaotan.Main;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;

public class Channel_621632815599190016 implements ChannelPremise {
	// #659
	@Override
	public void run(IDiscordClient client, IGuild guild, IChannel channel, IUser author, IMessage message,
			boolean edited) {
		if (message.isSystemMessage()) {
			return;
		}
		String text = message.getFormattedContent();
		long unixtime = message.getTimestamp().getEpochSecond();
		long id = message.getLongID();
		long timestamp = ((id >> 22) + 1420070400000L);
		Date date = new Date(timestamp);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.S");
		TimeZone tz = TimeZone.getTimeZone("Asia/Tokyo");
		sdf.setTimeZone(tz);
		String datestr = sdf.format(date);
		String time = "\\\"" + text + "\\\"";

		ProcessBuilder pb = new ProcessBuilder("php", "-r \"echo strtotime(" + time + ", " + unixtime / 1000 + ");\"");
		pb.redirectErrorStream(true);
		int ret;
		String line = null;
		try {
			Process p = pb.start();
			p.waitFor(10, TimeUnit.SECONDS);
			ret = p.exitValue();
			InputStream is = p.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			line = br.readLine();
		} catch (IOException | InterruptedException e) {
			Main.ExceptionReporter(channel, e);
			return;
		}
		String retmessage = "";
		if (ret == 0 && !line.equals("")) {
			Date textDate = new Date(Long.parseLong(line) * 1000);
			long now = date.getTime();
			long to = textDate.getTime();
			long diff = (to - now) / (1000 * 60 * 60);
			if (diff == 0) {
				retmessage = "`チャレンジ成功！" + sdf.format(textDate) + "ぴったりに投稿されました！おめでとうございます！`";
			} else if (diff > 0) {
				retmessage = "`チャレンジ失敗…" + sdf.format(textDate) + "ぴったりに投稿するには、あと" + diff + "秒後に投稿するべきでした…。`";
			} else if (diff < 0) {
				retmessage = "`チャレンジ失敗…" + sdf.format(textDate) + "ぴったりに投稿するには、あと" + Math.abs(diff) + "秒前に投稿するべきでした…。`";
			}
			retmessage += "\n(差: " + returnplus(diff) + Math.abs(diff) + ")";
		} else if (text.matches(".*6時59分.*")) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR, 6);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			cal.set(Calendar.MILLISECOND, 0);
			Date textDate = cal.getTime();
			long now = date.getTime();
			long to = textDate.getTime();
			long diff = (to - now) / (1000 * 60 * 60);
			if (diff == 0) {
				retmessage = "`チャレンジ成功！" + sdf.format(textDate) + "ぴったりに投稿されました！おめでとうございます！`";
			} else if (diff > 0) {
				retmessage = "`チャレンジ失敗…" + sdf.format(textDate) + "ぴったりに投稿するには、あと" + diff + "秒後に投稿するべきでした…。`";
			} else if (diff < 0) {
				retmessage = "`チャレンジ失敗…" + sdf.format(textDate) + "ぴったりに投稿するには、あと" + Math.abs(diff) + "秒前に投稿するべきでした…。`";
			}
			retmessage += "\n(差: " + returnplus(diff) + Math.abs(diff) + "秒)";
		}
		retmessage += "(`" + ret + ")\n```" + line + "```";
		// ななじとかは後日対応で。
		String replyMessage = datestr + "\n" + retmessage;
		RequestBuffer.request(() -> {
			try {
				message.reply(replyMessage);
			} catch (DiscordException discordexception) {
				Main.DiscordExceptionError(getClass(), channel, discordexception);
			}
		});
	}

	@Override
	public boolean isAlsoTargetEdited() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	String returnplus(long value) {
		if (value > 0)
			return "+";
		if (value < 0)
			return "-";
		return "";
	}
}
