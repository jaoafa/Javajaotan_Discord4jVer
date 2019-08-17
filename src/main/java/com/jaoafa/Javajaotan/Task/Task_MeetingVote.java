package com.jaoafa.Javajaotan.Task;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jaoafa.Javajaotan.Javajaotan;
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

	@Override
	public void run() {
		IDiscordClient client = Javajaotan.getClient();

		int VoteBorder = (int) Math.ceil(TeamJaoCount / 2);
		if (TeamJaoCount % 2 == 0) {
			VoteBorder++;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		IChannel channel = client.getChannelByID(597423974816808970L);
		List<IMessage> messages = channel.getPinnedMessages();
		for (IMessage message : messages) {
			String content = message.getFormattedContent();
			LocalDateTime timestamp = LocalDateTime.ofInstant(message.getTimestamp(), ZoneId.of("Asia/Tokyo"));

			IReaction good = message.getReactionByUnicode("👍");
			int good_count = good != null ? good.getCount() : 0;
			IReaction bad = message.getReactionByUnicode("👎");
			int bad_count = bad != null ? bad.getCount() : 0;
			IReaction white = message.getReactionByUnicode("🏳️");
			int white_count = white != null ? white.getCount() : 0;

			int _VoteBorder = VoteBorder;
			if (content.contains("\n")) {
				String[] contents = content.split("\n");
				Matcher m = p.matcher(contents[0]);
				if (m.find()) {
					if (!Library.isInt(m.group(0))) {
						continue;
					}
					_VoteBorder = Integer.valueOf(m.group(0));
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

			if (good_count >= _VoteBorder) {
				EmbedBuilder builder = new EmbedBuilder();
				builder.withTitle("VOTE RESULT");
				builder.appendDesc("@here :thumbsup:投票が承認されたことをお知らせします。");
				builder.appendField("賛成 / 反対　/　白票", good_count + " / " + bad_count + " / " + white_count, false);
				builder.appendField("決議ボーダー", String.valueOf(_VoteBorder), false);
				builder.appendField("内容", content, false);
				builder.appendField("投票開始日時", sdf.format(timestamp), false);
				RequestBuffer.request(() -> {
					try {
						channel.sendMessage(builder.build());
					} catch (DiscordException discordexception) {
						Javajaotan.DiscordExceptionError(getClass(), channel, discordexception);
					}
				});
				RequestBuffer.request(() -> {
					try {
						channel.unpin(message);
					} catch (DiscordException discordexception) {
						Javajaotan.DiscordExceptionError(getClass(), channel, discordexception);
					}
				});
			} else if (bad_count >= _VoteBorder) {
				EmbedBuilder builder = new EmbedBuilder();
				builder.withTitle("VOTE RESULT");
				builder.appendDesc("@here :thumbsup:投票が否認されたことをお知らせします。");
				builder.appendField("賛成 / 反対　/　白票", good_count + " / " + bad_count + " / " + white_count, false);
				builder.appendField("決議ボーダー", String.valueOf(_VoteBorder), false);
				builder.appendField("内容", content, false);
				builder.appendField("投票開始日時", sdf.format(timestamp), false);
				RequestBuffer.request(() -> {
					try {
						channel.sendMessage(builder.build());
					} catch (DiscordException discordexception) {
						Javajaotan.DiscordExceptionError(getClass(), channel, discordexception);
					}
				});
				RequestBuffer.request(() -> {
					try {
						channel.unpin(message);
					} catch (DiscordException discordexception) {
						Javajaotan.DiscordExceptionError(getClass(), channel, discordexception);
					}
				});
			}
		}
	}
}
