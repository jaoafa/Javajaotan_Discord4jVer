package com.jaoafa.Javajaotan.Task;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.TimerTask;

import com.jaoafa.Javajaotan.Javajaotan;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

public class Task_VerifiedCheck extends TimerTask {
	public Task_VerifiedCheck(){

	}
	@Override
	public void run(){
		// 参加から10分以内に発言がなかったら蹴る
		IDiscordClient client = Javajaotan.getClient();
		IGuild guild = client.getGuildByID(597378876556967936L); // new jMS Gamers Club
		IRole role = guild.getRoleByID(597421078817669121L);
		IChannel channel = client.getChannelByID(597419057251090443L); // new general
		if(channel == null) {
			System.out.println("[VerifiedError] general(597419057251090443) channel is not found.");
			return;
		}
		for(IUser user : guild.getUsers()) {
			if(user.hasRole(role)) {
				// role exists : ok
				continue;
			}
			if(user.isBot()) {
				// bot
				continue;
			}
			LocalDateTime joinTime = LocalDateTime.ofInstant(guild.getJoinTimeForUser(user), ZoneId.of("Asia/Tokyo"));
			LocalDateTime now = LocalDateTime.now();
			long diffmin = ChronoUnit.MINUTES.between(joinTime, now);
			if(diffmin <= 10) {
				// 10分以内
				continue;
			}
			guild.kickUser(user);
			channel.sendMessage("__**[VerifiedCheck]**__ チャットがないまま10分を経過したため、ユーザー「" + user.getName() + "#" + user.getDiscriminator() + "」をキックしました。");
		}
	}
}
