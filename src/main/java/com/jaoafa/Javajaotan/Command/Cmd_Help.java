package com.jaoafa.Javajaotan.Command;

import java.awt.Color;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import com.jaoafa.Javajaotan.CommandPremise;
import com.jaoafa.Javajaotan.Lib.ClassFinder;
import com.jaoafa.Javajaotan.Lib.EmbedField;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

public class Cmd_Help implements CommandPremise {
	@Override
	public void onCommand(IDiscordClient client, IGuild guild, IChannel channel, IUser author, IMessage message, String[] args){
		// help command
		if(args.length == 0) {
			// cmd list
			List<EmbedField> cmdList = new ArrayList<>();
			try {
				ClassFinder classFinder = new ClassFinder();
				for(Class<?> clazz : classFinder.findClasses("com.jaoafa.Javajaotan.Command")){
					if(!clazz.getName().startsWith("com.jaoafa.Javajaotan.Command.Cmd_")){
						continue;
					}
					String commandName = clazz.getName().substring("com.jaoafa.Javajaotan.Command.Cmd_".length());

					Constructor<?> construct = (Constructor<?>) clazz.getConstructor();
					CommandPremise cmd = (CommandPremise) construct.newInstance();
					EmbedField field = new EmbedField(commandName.toLowerCase(), cmd.getDescription());
					cmdList.add(field);
				}
			} catch (Exception e) {
				channel.sendMessage("ERROR\n" + e.getCause() + "\n" + e.getMessage());
				return;
			}
			// todo: /help 2など対応、文字数計算・自動的に切ってページネーションする
			// 10コマンドづつページ分割
			final int nowpage = 0; // 1ページ = 0
			final int allcmdcount = cmdList.size();
			int allpage = allcmdcount / 10;
			if(allcmdcount % 10 != 0) allpage++;
			final int pagestart = nowpage * 10;
			final int pageend = nowpage * 10 + 10;

			EmbedBuilder builder = new EmbedBuilder();
			builder.withTitle("jaotan Commands (" + (nowpage + 1) + " / " + allpage + ")");
			builder.withColor(Color.YELLOW);
			if(nowpage < allpage){
				builder.withFooterText("View more command by typing /help <Page>.");
			}
			for(int i = pagestart; i < pageend; i++){
				if(i < 0 || i >= cmdList.size()){
					break;
				}
				EmbedField field = cmdList.get(i);
				builder.appendField("/" + field.getTitle(), field.getContent(), true);
			}

			channel.sendMessage(builder.build());
			return;
		}else if(args.length == 1 && NumberUtils.isCreatable(args[0])){
			// /help 1,2,3...
			List<EmbedField> cmdList = new ArrayList<>();
			try {
				ClassFinder classFinder = new ClassFinder();
				//Set<Class<?>> classes = Library.getClasses("com.jaoafa.Javajaotan.Command");
				for(Class<?> clazz : classFinder.findClasses("com.jaoafa.Javajaotan.Command")){
					if(!clazz.getName().startsWith("com.jaoafa.Javajaotan.Command.Cmd_")){
						continue;
					}
					String commandName = clazz.getName().substring("com.jaoafa.Javajaotan.Command.Cmd_".length());

					Constructor<?> construct = (Constructor<?>) clazz.getConstructor();
					CommandPremise cmd = (CommandPremise) construct.newInstance();
					EmbedField field = new EmbedField(commandName.toLowerCase(), cmd.getDescription());
					cmdList.add(field);
				}
			} catch (Exception e) {
				channel.sendMessage("ERROR\n" + e.getCause() + "\n" + e.getMessage());
				return;
			}
			// todo: /help 2など対応、文字数計算・自動的に切ってページネーションする
			// 10コマンドづつページ分割
			final int nowpage = Integer.valueOf(args[0]) - 1; // 1ページ = 0
			final int allcmdcount = cmdList.size();
			int allpage = allcmdcount / 10;
			if(allcmdcount % 10 != 0) allpage++;
			final int pagestart = nowpage * 10;
			final int pageend = nowpage * 10 + 10;

			EmbedBuilder builder = new EmbedBuilder();
			builder.withTitle("jaotan Commands (" + (nowpage + 1) + " / " + allpage + ")");
			builder.withColor(Color.YELLOW);
			if(nowpage < allpage){
				builder.withFooterText("View more command by typing /help <Page>.");
			}

			for(int i = pagestart; i < pageend; i++){
				if(i < 0 || i >= cmdList.size()){
					break;
				}
				EmbedField field = cmdList.get(i);
				builder.appendField("/" + field.getTitle(), field.getContent(), true);
			}

			channel.sendMessage(builder.build());
			return;
		}
		try {
			String className = args[0].substring(0, 1).toUpperCase() + args[0].substring(1).toLowerCase(); // Help
			//channel.sendMessage("com.jaoafa.Javajaotan.Command.Cmd_" + className);

			Class.forName("com.jaoafa.Javajaotan.Command.Cmd_" + className);
			// クラスがない場合これ以降進まない
			Constructor<?> construct = (Constructor<?>) Class.forName("com.jaoafa.Javajaotan.Command.Cmd_" + className).getConstructor();
			CommandPremise cmd = (CommandPremise) construct.newInstance();

			EmbedBuilder builder = new EmbedBuilder();
			builder.withTitle("jaotan Command Help");
			builder.withColor(Color.YELLOW);
			builder.appendField("/" + args[0].toLowerCase(), "**Description**: `" + cmd.getDescription() + "`\n" + "**Usage**: `" + cmd.getUsage() + "`", false);

			channel.sendMessage(builder.build());
		} catch (ClassNotFoundException e) {
			EmbedBuilder builder = new EmbedBuilder();
			builder.withTitle("jaotan Command Help");
			builder.withColor(Color.YELLOW);
			builder.appendField("/" + args[0].toLowerCase(), "Command not found.", false);

			channel.sendMessage(builder.build());
		} catch (InstantiationException|IllegalAccessException|IllegalArgumentException|InvocationTargetException|NoSuchMethodException|SecurityException e) {
			channel.sendMessage("ERROR\n" + e.getCause() + "\n" + e.getMessage());
		}
	}

	@Override
	public String getDescription() {
		return "コマンドの説明と使用方法を表示します。引数を指定しない場合、コマンドの一覧を表示します。";
	}

	@Override
	public String getUsage() {
		return "/help [Command|Page]";
	}
}
