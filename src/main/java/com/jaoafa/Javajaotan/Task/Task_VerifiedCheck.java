package com.jaoafa.Javajaotan.Task;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.TimerTask;

import com.jaoafa.Javajaotan.Main;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;

public class Task_VerifiedCheck extends TimerTask {
	@Override
	public void run() {
		// 参加から10分以内に発言がなかったら蹴る
		IDiscordClient client = Main.getClient();
		IGuild guild = client.getGuildByID(597378876556967936L); // new jMS Gamers Club
		IRole role = guild.getRoleByID(597421078817669121L);
		IChannel channel = client.getChannelByID(597419057251090443L); // new general
		if (channel == null) {
			System.out.println("[VerifiedError] general(597419057251090443) channel is not found.");
			return;
		}
		for (IUser user : guild.getUsers()) {
			if (user.hasRole(role)) {
				// role exists : ok
				continue;
			}
			if (user.isBot()) {
				// bot
				continue;
			}
			LocalDateTime joinTime = LocalDateTime.ofInstant(guild.getJoinTimeForUser(user), ZoneId.of("Asia/Tokyo"));
			LocalDateTime now = LocalDateTime.now();
			long diffmin = ChronoUnit.MINUTES.between(joinTime, now);
			if (diffmin <= 10) {
				// 10分以内
				continue;
			}
			try {
				guild.kickUser(user);
			} catch (DiscordException e) {
				RequestBuffer.request(() -> {
					try {
						Main.ReportChannel
								.sendMessage("Task_VerifiedCheckにてチャットがないまま10分を経過したためユーザー「" + user.getName() + "#"
										+ user.getDiscriminator() + "」をキックしようとしましたが正常に実行できませんでした！\n**Message**: `"
										+ e.getErrorMessage() + "`");
					} catch (DiscordException discordexception) {
						Main.DiscordExceptionError(getClass(), Main.ReportChannel, discordexception);
					}
				});
			}
			RequestBuffer.request(() -> {
				try {
					channel.sendMessage("__**[VerifiedCheck]**__ チャットがないまま10分を経過したため、ユーザー「" + user.getName() + "#"
							+ user.getDiscriminator() + "」をキックしました。");
				} catch (DiscordException discordexception) {
					Main.DiscordExceptionError(getClass(), channel, discordexception);
				}
			});
		}
	}
}
