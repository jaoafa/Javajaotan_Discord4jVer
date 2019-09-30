package com.jaoafa.Javajaotan.Command;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import com.jaoafa.Javajaotan.CommandPremise;
import com.jaoafa.Javajaotan.Main;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;

public class Cmd_Mcserver implements CommandPremise {
	@Override
	public void onCommand(IDiscordClient client, IGuild guild, IChannel channel, IUser author, IMessage message,
			String[] args) {
		if(channel.getLongID() != 597423444501463040L) {
			RequestBuffer.request(() -> {
				try {
					message.reply("実行しようとしたコマンドはこのチャンネルでは使用できません。");
				} catch (DiscordException discordexception) {
					Main.DiscordExceptionError(getClass(), channel, discordexception);
				}
			});
			return;
		}
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("start")) {
				runCommand(message, "systemctl", "start", "minecraft");
				return;
			}else if(args[0].equalsIgnoreCase("stop")) {
				runCommand(message, "systemctl", "stop", "minecraft");
				return;
			}else if(args[0].equalsIgnoreCase("restart")) {
				runCommand(message, "systemctl", "restart", "minecraft");
				return;
			}else if(args[0].equalsIgnoreCase("kill")) {
				runCommand(message, "systemctl", "kill", "minecraft");
				return;
			}else if(args[0].equalsIgnoreCase("status")) {
				runCommand(message, "systemctl", "status", "minecraft");
				return;
			}else if(args[0].equalsIgnoreCase("uptime")) {
				runCommand(message, "uptime");
				return;
			}else if(args[0].equalsIgnoreCase("tps")) {
				String command = "tps";
				runCommand(message, "/usr/bin/screen", "-p 0", "-S minecraft", "-X eval 'stuff \"" + command + "\"\\\\015'");
				return;
			}
		}else if(args.length == 2) {
			if(args[0].equalsIgnoreCase("say")) {
				String command = "say " + String.join(" ", Arrays.copyOfRange(args, 1, args.length));
				runCommand(message, "/usr/bin/screen", "-p 0", "-S minecraft", "-X eval 'stuff \"" + command + "\"\\\\015'");
				return;
			}else if(args[0].equalsIgnoreCase("chat")) {
				String command = "chat " + String.join(" ", Arrays.copyOfRange(args, 1, args.length));
				runCommand(message, "/usr/bin/screen", "-p 0", "-S minecraft", "-X eval 'stuff \"" + command + "\"\\\\015'");
				return;
			}else if(args[0].equalsIgnoreCase("tell")) {
				String command = "tell " + String.join(" ", Arrays.copyOfRange(args, 1, args.length));
				runCommand(message, "/usr/bin/screen", "-p 0", "-S minecraft", "-X eval 'stuff \"" + command + "\"\\\\015'");
				return;
			}
		}
		RequestBuffer.request(() -> {
			try {
				message.reply(getUsage());
			} catch (DiscordException discordexception) {
				Main.DiscordExceptionError(getClass(), channel, discordexception);
			}
		});
	}

	private void runCommand(IMessage message, String... command) {
		IChannel channel = message.getChannel();
		Process p;
		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(command);
			builder.directory(new File("/var/1.12.2_jaoafaS3/"));
			builder.redirectErrorStream(true);
			p = builder.start();
			p.waitFor(10, TimeUnit.MINUTES);
		} catch (IOException e) {
			Main.ExceptionReporter(channel, e);
			return;
		} catch (InterruptedException e) {
			Main.ExceptionReporter(channel, e);
			return;
		}
		int ret = p.exitValue();
		InputStream is = p.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		String text = "";
		try {
			while (true) {
				String line = br.readLine();
				if (line == null) {
					break;
				}
				text += line + "\n";
			}
			br.close();
			is.close();
		}catch(IOException e) {
			Main.ExceptionReporter(channel, e);
			return;
		}
		String last = text;
		RequestBuffer.request(() -> {
			try {
				message.reply("```" + last + "```(" + ret + ")");
			} catch (DiscordException discordexception) {
				Main.DiscordExceptionError(getClass(), channel, discordexception);
			}
		});
	}

	@Override
	public String getDescription() {
		return "Minecraftサーバに関する操作を行います。特定のチャンネルでのみ使用できます。";
	}

	@Override
	public String getUsage() {
		return "/mcserver <start,stop,restart,kill,status,uptime,tps,say,chat,tell> [Value]";
	}

	@Override
	public boolean isjMSOnly() {
		return true;
	}
}
