package com.jaoafa.Javajaotan.Command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jaoafa.Javajaotan.CommandPremise;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class Cmd_Bassline implements CommandPremise {
	@Override
	public void onCommand(IDiscordClient client, IGuild guild, IChannel channel, IUser author, IMessage message, String[] args){
		String replyId = "<@222018383556771840>"; // jaotan
		Pattern all_id_pattern = Pattern.compile("^[0-9]+$");
		if(args.length == 0){
			// Hiratake Nanami
			replyId = "<@221498004505362433>"; // hiratake
		}else if(args.length == 1){
			Matcher all_id_matcher = all_id_pattern.matcher(args[0]);
			if(all_id_matcher.matches()){
				replyId = "<@" + args[0] + ">";
			}else{
				replyId = args[0];
			}
		}
		channel.sendMessage("ベースラインパーティーの途中ですが、ここで臨時ニュースをお伝えします。\n"
				+ "今日昼頃、わりとキモく女性にナンパをしたうえ、路上で爆睡をしたとして、\n"
				+ "道の上で寝たり、女の子に声をかけたりしたらいけないんだよ罪の容疑で、\n"
				+ "自称優良物件、" + replyId + "容疑者が逮捕されました。");
	}

    @Override
    public String getDescription() {
        return "ベースラインパーティーの途中の臨時ニュースを発言されたチャンネルに投稿します。";
    }

    @Override
    public String getUsage() {
        return "/bassline [UserID|Text]";
    }
}
