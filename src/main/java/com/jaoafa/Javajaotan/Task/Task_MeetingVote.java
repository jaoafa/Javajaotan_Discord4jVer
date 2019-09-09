package com.jaoafa.Javajaotan.Task;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jaoafa.Javajaotan.Main;
import com.jaoafa.Javajaotan.Lib.Library;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IReaction;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

public class Task_MeetingVote extends TimerTask {
	int TeamJaoCount = 9; // Admin + Moderator
	Pattern p = Pattern.compile("\\[Border:([0-9]+)\\]");
	boolean debugMode = false;

	public Task_MeetingVote() {

	}

	public Task_MeetingVote(boolean debugMode) {
		this.debugMode = debugMode;
	}

	@Override
	public void run() {
		IDiscordClient client = Main.getClient();

		double divided = TeamJaoCount / 2;
		int VoteBorder = (int) Math.ceil(divided);
		if (TeamJaoCount % 2 == 0) {
			VoteBorder++;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		IChannel channel = client.getChannelByID(597423974816808970L);
		List<IMessage> messages = channel.getPinnedMessages();
		for (IMessage message : messages) {
			String content = message.getFormattedContent();
			LocalDateTime timestamp = LocalDateTime.ofInstant(message.getTimestamp(), ZoneId.of("Asia/Tokyo"));

			message = channel.fetchMessage(message.getLongID());
			List<IReaction> reacts = message.getReactions();
			if (debugMode) {
				for (IReaction react : reacts) {
					System.out.println(react.getEmoji().getName() + ": " + react.getCount());
					System.out.println("Good: " + Boolean.toString(react.getEmoji().getName().equals("\uD83D\uDC4D")));
					System.out.println("White: " + Boolean.toString(react.getEmoji().getName().equals("🏳️")));
				}
			}

			IReaction good = message.getReactionByUnicode("\uD83D\uDC4D");
			int good_count = good != null ? good.getCount() : 0;
			IReaction bad = message.getReactionByUnicode("\uD83D\uDC4E");
			int bad_count = bad != null ? bad.getCount() : 0;
			IReaction white = message.getReactionByUnicode("\uD83C\uDFF3");
			int white_count = white != null ? white.getCount() : 0;

			int _VoteBorder = VoteBorder;
			if (content.contains("\n")) {
				String[] contents = content.split("\n");
				Matcher m = p.matcher(contents[0]);
				if (m.find()) {
					if (!Library.isInt(m.group(1))) {
						continue;
					}
					_VoteBorder = Integer.valueOf(m.group(1));
				}
			}
			int _white = white_count;
			if (_white != 0) {
				if (_VoteBorder % 2 == 0) {
					_VoteBorder--;
					_white--;
				}
				_VoteBorder -= _white / 2;
			}
			if (debugMode) {
				System.out.println("MeetingVote[debugMode] " + message.getStringID() + " by "
						+ message.getAuthor().getName() + "#" + message.getAuthor().getDiscriminator());
				System.out.println("MeetingVote[debugMode] " + "VoteBorder: " + VoteBorder + " / Good: " + good_count
						+ " / Bad: " + bad_count + " / White: " + white_count + " / _VoteBorder: " + _VoteBorder);
			}

			if (good_count >= _VoteBorder) {
				EmbedBuilder builder = new EmbedBuilder();
				builder.withTitle("VOTE RESULT");
				builder.appendDesc("@here :thumbsup:投票が承認されたことをお知らせします。");
				builder.appendField("賛成 / 反対 / 白票", good_count + " / " + bad_count + " / " + white_count, false);
				builder.appendField("決議ボーダー", String.valueOf(_VoteBorder), false);
				builder.appendField("内容", content, false);
				builder.appendField("対象メッセージURL", "https://discordapp.com/channels/" + message.getGuild().getStringID()
						+ "/" + message.getChannel().getStringID() + "/" + message.getStringID(), false);
				builder.appendField("投票開始日時",
						sdf.format(new Date(timestamp.toEpochSecond(ZoneOffset.ofHours(9)) * 1000)), false);
				builder.withColor(Color.GREEN);
				RequestBuffer.request(() -> {
					try {
						channel.sendMessage(builder.build());
					} catch (DiscordException discordexception) {
						Main.DiscordExceptionError(getClass(), channel, discordexception);
					}
				});
				IMessage FINALMESSAGE = message;
				RequestBuffer.request(() -> {
					try {
						channel.unpin(FINALMESSAGE);
					} catch (DiscordException discordexception) {
						Main.DiscordExceptionError(getClass(), channel, discordexception);
					}
				});
			} else if (bad_count >= _VoteBorder) {
				EmbedBuilder builder = new EmbedBuilder();
				builder.withTitle("VOTE RESULT");
				builder.appendDesc("@here :thumbsup:投票が否認されたことをお知らせします。");
				builder.appendField("賛成 / 反対 / 白票", good_count + " / " + bad_count + " / " + white_count, false);
				builder.appendField("決議ボーダー", String.valueOf(_VoteBorder), false);
				builder.appendField("内容", content, false);
				builder.appendField("対象メッセージURL", "https://discordapp.com/channels/" + message.getGuild().getStringID()
						+ "/" + message.getChannel().getStringID() + "/" + message.getStringID(), false);
				builder.appendField("投票開始日時",
						sdf.format(new Date(timestamp.toEpochSecond(ZoneOffset.ofHours(9)) * 1000)), false);
				builder.withColor(Color.RED);
				RequestBuffer.request(() -> {
					try {
						channel.sendMessage(builder.build());
					} catch (DiscordException discordexception) {
						Main.DiscordExceptionError(getClass(), channel, discordexception);
					}
				});
				IMessage FINALMESSAGE = message;
				RequestBuffer.request(() -> {
					try {
						channel.unpin(FINALMESSAGE);
					} catch (DiscordException discordexception) {
						Main.DiscordExceptionError(getClass(), channel, discordexception);
					}
				});
			}

			long start = timestamp.toEpochSecond(ZoneOffset.ofHours(9));
			long now = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());

			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(TimeUnit.SECONDS.toMillis(start));
			cal.add(Calendar.WEEK_OF_YEAR, 2);

			if ((start + 1209600) <= now) {
				// 2週間 1209600秒
				EmbedBuilder builder = new EmbedBuilder();
				builder.withTitle("VOTE RESULT");
				builder.appendDesc("@here :wave:有効会議期限を過ぎたため、投票が否認されたことをお知らせします。");
				builder.appendField("賛成 / 反対 / 白票", good_count + " / " + bad_count + " / " + white_count, false);
				builder.appendField("決議ボーダー", String.valueOf(_VoteBorder), false);
				builder.appendField("内容", content, false);
				builder.appendField("対象メッセージURL", "https://discordapp.com/channels/" + message.getGuild().getStringID()
						+ "/" + message.getChannel().getStringID() + "/" + message.getStringID(), false);
				builder.appendField("投票開始日時",
						sdf.format(timestamp.toEpochSecond(ZoneOffset.ofHours(9))) + " ("
								+ timestamp.toEpochSecond(ZoneOffset.ofHours(9)) + ")",
						false);
				builder.appendField("有効会議期限",
						sdf.format(cal.getTime()) + " (" + TimeUnit.MILLISECONDS.toSeconds(cal.getTimeInMillis()) + ")",
						false);
				builder.appendField("現在時刻",
						sdf.format(new Date(TimeUnit.SECONDS.toMillis(now))) + " (" + now + ")",
						false);
				builder.withColor(Color.ORANGE);
				RequestBuffer.request(() -> {
					try {
						channel.sendMessage(builder.build());
					} catch (DiscordException discordexception) {
						Main.DiscordExceptionError(getClass(), channel, discordexception);
					}
				});
				IMessage FINALMESSAGE = message;
				RequestBuffer.request(() -> {
					try {
						channel.unpin(FINALMESSAGE);
					} catch (DiscordException discordexception) {
						Main.DiscordExceptionError(getClass(), channel, discordexception);
					}
				});
			}
		}
	}
}
