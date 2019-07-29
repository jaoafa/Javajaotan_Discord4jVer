package com.jaoafa.Javajaotan.Command;

import com.jaoafa.Javajaotan.CommandPremise;
import com.jaoafa.Javajaotan.Lib.Library;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

public class Cmd_Userban implements CommandPremise {
	@Override
	public void onCommand(IDiscordClient client, IGuild guild, IChannel channel, IUser author, IMessage message, String[] args){
		IRole[] allowRoles = {
				client.getRoleByID(189381504059572224L), // old jGC Admin
				client.getRoleByID(281699181410910230L), // old jGC Moderator
				client.getRoleByID(597405109290532864L), // new jGC Admin
				client.getRoleByID(597405110683041793L), // new jGC Moderator
		};
		if(!Library.isAllowRole(author, allowRoles)){
			channel.sendMessage("実行しようとしたコマンドはこのチャンネルでは使用できません。");
			return;
		}
		if(args.length != 1){
			channel.sendMessage("引数が足りません。");
			return;
		}
		if(!Library.isLong(args[0])){
			channel.sendMessage("指定されたユーザーIDは適切ではありません。");
			return;
		}
		long userid = Long.valueOf(args[0]);
		IUser user = client.fetchUser(userid);
		if(user != null){
			guild.banUser(user);
			String name = user.getName();
			String discriminator = user.getDiscriminator();
			channel.sendMessage("指定されたユーザー「" + name + "#" + discriminator + "」(" + userid + ")をBanしました。");
			return;
		}else{
			guild.banUser(userid);
			channel.sendMessage("指定されたユーザー(" + userid + ")をBanしました。");
		}
	}

    @Override
    public String getDescription() {
        return "指定されたユーザーをBanします。";
    }

    @Override
    public String getUsage() {
        return "/userban <UserID>";
    }
}
