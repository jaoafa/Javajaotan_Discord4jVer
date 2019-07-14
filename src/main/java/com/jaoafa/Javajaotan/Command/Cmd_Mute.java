package com.jaoafa.Javajaotan.Command;

import java.util.Arrays;

import com.jaoafa.Javajaotan.CommandPremise;
import com.jaoafa.Javajaotan.Lib.Library;
import com.jaoafa.Javajaotan.Lib.MuteManager;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class Cmd_Mute implements CommandPremise {
	@Override
	public void onCommand(IDiscordClient client, IGuild guild, IChannel channel, IUser author, IMessage message, String[] args){
		// Admin & Moderator ONLY Command
		if(!Library.hasAdminModeratorRole(guild, author)){
			channel.sendMessage("あなたはこのコマンドを使用できません。");
			return;
		}
		String[] allowChannel = new String[]{
				"222002864875110400", // admin
				"228771335499808769", // meeting
				"293856671799967744", // toma_lab

				"597423654451675137", // new admin
				"597423467796758529", // new meeting
		};
		if(!Arrays.asList(allowChannel).contains(channel.getStringID())){
			channel.sendMessage("このチャンネルではこのコマンドを使用できません。");
			return;
		}
		if(args.length == 2){
			if(args[0].equalsIgnoreCase("add")){
				// mute add
				String userid = args[1];
				if(MuteManager.isMuted(userid)){
					// already
					channel.sendMessage("指定されたユーザーは既にミュートされています。");
					return;
				}
				MuteManager.addMuteList(userid);
				channel.sendMessage("ミュートリストに追加しました : <@" + userid + ">");
				return;
			}else if(args[0].equalsIgnoreCase("remove")){
				// mute remove
				String userid = args[1];
				if(!MuteManager.isMuted(userid)){
					// already
					channel.sendMessage("指定されたユーザーはミュートされていません。");
					return;
				}
				if(!Library.isLong(userid)){
					channel.sendMessage("数値を指定してください。");
				}
				MuteManager.removeMuteList(userid);
				channel.sendMessage("ミュートリストから解除しました : <@" + userid + ">");
				return;
			}
		}
		channel.sendMessage(getUsage());
		return;
	}
	@Override
	public String getDescription() {
		return "ミュート機能の制御ができます。運営のみ利用可能で、特定のチャンネルでのみ使用できます。";
	}

	@Override
	public String getUsage() {
		return "/mute <add|remove> <UserID>";
	}
}