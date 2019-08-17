package com.jaoafa.Javajaotan.Channel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jaoafa.Javajaotan.ChannelPremise;
import com.jaoafa.Javajaotan.Javajaotan;
import com.jaoafa.Javajaotan.Lib.Library;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

public class Channel_597423974816808970 implements ChannelPremise {
	// #meeting_vote
	@Override
	public void run(IDiscordClient client, IGuild guild, IChannel channel, IUser author, IMessage message,
			boolean edited) {
		if (message.isSystemMessage()) {
			return;
		}
		String content = message.getFormattedContent();
		String border = null;
		if (content.contains("\n")) {
			String[] contents = content.split("\n");
			Pattern p = Pattern.compile("\\[Border:([0-9]+)\\]");
			Matcher m = p.matcher(contents[0]);
			if (m.find()) {
				border = m.group(1);
			}
		}
		String pinerr = null;
		try {
			channel.pin(message);
		} catch (DiscordException e) {
			pinerr = e.getErrorMessage();
			RequestBuffer.request(() -> {
				try {
					message.addReaction(ReactionEmoji.of("❌")); // :x:
				} catch (DiscordException discordexception) {
					Javajaotan.DiscordExceptionError(getClass(), channel, discordexception);
				}
			});
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.WEEK_OF_YEAR, 2);

		EmbedBuilder builder = new EmbedBuilder();
		builder.withTitle("NEW VOTE");
		builder.withDesc("@here " + author.getName() + "#" + author.getDiscriminator() + "から新しい投票です。");
		builder.appendField("賛成の場合", "投票メッセージに対して:thumbsup:を付けてください。", false);
		builder.appendField("反対の場合", "投票メッセージに対して:thumbsdown:を付けてください。\n"
				+ "**反対の場合は<#597423467796758529>に意見理由を必ず書いてください。**", false);
		builder.appendField("白票の場合", "投票メッセージに対して:flag_white:を付けてください。\n"
				+ "(白票の場合投票権利を放棄し他の人に投票を委ねます)", false);
		builder.appendField("この投票に対する話し合い", "<#597423467796758529>でどうぞ。", false);
		if (border != null && Library.isInt(border)) {
			builder.appendField("決議ボーダー", "この投票の決議ボーダーは" + border + "です。", false);
		}
		builder.appendField("その他", "投票の有効会議期限は2週間(" + sdf.format(cal.getTime()) + "まで)です。", false);
		if (pinerr != null) {
			builder.appendField("ピン留めエラーメッセージ", pinerr, false);
		}
		RequestBuffer.request(() -> {
			try {
				channel.sendMessage(builder.build());
			} catch (DiscordException discordexception) {
				Javajaotan.DiscordExceptionError(getClass(), channel, discordexception);
			}
		});
	}

	@Override
	public boolean isAlsoTargetEdited() {
		return false;
	}
}
