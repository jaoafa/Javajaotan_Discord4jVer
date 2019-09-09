package com.jaoafa.Javajaotan;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.jaoafa.Javajaotan.ALLChat.ALLChatMainEvent;
import com.jaoafa.Javajaotan.Channel.ChannelMainEvent;
import com.jaoafa.Javajaotan.Command.MessageMainEvent;
import com.jaoafa.Javajaotan.Event.Event_MessageReceived;
import com.jaoafa.Javajaotan.Event.Event_ReactionAddEvent;
import com.jaoafa.Javajaotan.Event.Event_ServerBanned;
import com.jaoafa.Javajaotan.Event.Event_ServerJoin;
import com.jaoafa.Javajaotan.Event.Event_ServerLeave;
import com.jaoafa.Javajaotan.Lib.Library;
import com.jaoafa.Javajaotan.Lib.MySQLDBManager;
import com.jaoafa.Javajaotan.Task.Task_MeetingVote;
import com.jaoafa.Javajaotan.Task.Task_VerifiedCheck;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

public class Main {
	public static IChannel ReportChannel = null;
	private static IDiscordClient client = null;
	//private static JDA jda = null;
	public static MySQLDBManager MySQLDBManager = null;
	public static String translateGAS = null;

	public static void main(String[] args) {
		File f = new File("conf.properties");
		Properties props;
		try {
			InputStream is = new FileInputStream(f);

			// プロパティファイルを読み込む
			props = new Properties();
			props.load(is);
		} catch (FileNotFoundException e) {
			// ファイル生成
			props = new Properties();
			props.setProperty("token", "PLEASETOKEN");
			props.setProperty("sqlserver", "PLEASE");
			props.setProperty("sqluser", "PLEASE");
			props.setProperty("sqlpassword", "PLEASE");
			props.setProperty("translateGAS", "PLEASE");
			try {
				props.store(new FileOutputStream("conf.properties"), "Comments");
				System.out.println("Please Config Token!");
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
				return;
			} catch (IOException e1) {
				e1.printStackTrace();
				return;
			}
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		// キーを指定して値を取得する
		String token = props.getProperty("token");
		if (token.equalsIgnoreCase("PLEASETOKEN")) {
			System.out.println("Please Token!");
			return;
		}
		String sqlserver = props.getProperty("sqlserver");
		if (sqlserver.equalsIgnoreCase("PLEASE")) {
			System.out.println("Please Token!");
			return;
		}
		String sqluser = props.getProperty("sqluser");
		if (sqluser.equalsIgnoreCase("PLEASE")) {
			System.out.println("Please Token!");
			return;
		}
		String sqlpassword = props.getProperty("sqlpassword");
		if (sqlpassword.equalsIgnoreCase("PLEASE")) {
			System.out.println("Please Token!");
			return;
		}
		translateGAS = props.getProperty("translateGAS");
		if (translateGAS.equalsIgnoreCase("PLEASE")) {
			translateGAS = null;
		}
		try {
			MySQLDBManager = new MySQLDBManager(sqlserver, "3306", "jaoafa", sqluser, sqlpassword);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		IDiscordClient client = createClient(token, true);
		EventDispatcher dispatcher = client.getDispatcher();
		dispatcher.registerListener(new MessageMainEvent());
		dispatcher.registerListener(new ChannelMainEvent());
		dispatcher.registerListener(new ALLChatMainEvent());
		dispatcher.registerListener(new Event_ServerJoin());
		dispatcher.registerListener(new Event_ServerLeave());
		dispatcher.registerListener(new Event_ServerBanned());
		dispatcher.registerListener(new Event_MessageReceived());
		dispatcher.registerListener(new Event_ReactionAddEvent());

		/*try {
			jda = new JDABuilder(AccountType.BOT)
					.setAudioEnabled(false)
					.setAutoReconnect(true)
					.setBulkDeleteSplittingEnabled(false)
					.setToken(token)
					.setContextEnabled(false)
					.build().awaitReady();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}*/

		Runtime.getRuntime().addShutdownHook(
				new Thread(
						() -> {
							System.out.println("Exit");
						}));

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new Task_VerifiedCheck(), 10000, 60000); // 1分
		timer.scheduleAtFixedRate(new Task_MeetingVote(), 10000, 600000); // 10分

		/*
		JavajaotanWatcher JavajaotanWatcher = new JavajaotanWatcher();
		Timer timer = new Timer();
		timer.schedule(JavajaotanWatcher, 60000);
		*/
	}

	public static IDiscordClient createClient(String token, boolean login) { // Returns a new instance of the Discord client
		ClientBuilder clientBuilder = new ClientBuilder(); // Creates the ClientBuilder instance
		clientBuilder.withToken(token); // Adds the login info to the builder
		try {
			if (login) {
				return clientBuilder.login(); // Creates the client instance and logs the client in
			} else {
				return clientBuilder.build(); // Creates the client instance but it doesn't log the client in yet, you would have to call client.login() yourself
			}
		} catch (DiscordException e) { // This is thrown if there was a problem building the client
			Main.ExceptionReporter(null, e);
			return null;
		}
	}

	public static void setClient(IDiscordClient client) {
		Main.client = client;
	}

	public static IDiscordClient getClient() {
		return client;
	}

	/*public static JDA getJDA() {
		return jda;
	}*/

	public static void DiscordExceptionError(@NotNull Class<?> clazz, @Nullable IChannel channel,
			@NotNull DiscordException exception) {
		if (channel == null && Main.ReportChannel != null) {
			channel = Main.ReportChannel;
		} else if (channel == null) {
			System.out.println("DiscordExceptionError: channel == null and Javajaotan.ReportChannel == null.");
			System.out.println("DiscordExceptionError did not work properly!");
			return;
		}
		if (clazz == null) {
			throw new NullPointerException("Class<?> clazz is null!");
		}
		if (exception == null) {
			throw new NullPointerException("DiscordException exception is null!");
		}
		final IChannel FINALCHANNEL = channel;
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		exception.printStackTrace(pw);
		pw.flush();
		try {
			InputStream is = new ByteArrayInputStream(sw.toString().getBytes("utf-8"));
			RequestBuffer.request(() -> {
				FINALCHANNEL.sendFile(
						":pencil:おっと！Javajaotanでなにか問題が発生したようです！ <@221991565567066112>\n**ErrorMsg**: `"
								+ exception.getErrorMessage()
								+ "`\n**Class**: `" + clazz.getName() + "`",
						is,
						"stacktrace.txt");
			});
		} catch (UnsupportedEncodingException ex) {
			RequestBuffer.request(() -> {
				FINALCHANNEL.sendMessage(":pencil:<@221991565567066112> おっと！メッセージ送信時に問題が発生したみたいです！\n**ErrorMsg**: `"
						+ exception.getErrorMessage() + "`\n**Class**: `" + clazz.getName()
						+ "`\nUnsupportedEncodingException: `" + ex.getMessage() + "`");
			});
		}
	}

	public static void ExceptionReporter(@Nullable IChannel channel, @NotNull Throwable exception) {
		if (channel != null) {
			RequestBuffer.request(() -> {
				try {
					channel.sendMessage(
							":pencil:おっと！Javajaotanでなにか問題が発生したようです！ <@221991565567066112>\n**Throwable Class**: `"
									+ exception.getClass().getName() + "`");
				} catch (DiscordException discordexception) {
					Main.DiscordExceptionError(Main.class, Main.ReportChannel, discordexception);
				}
			});
		}
		if (Main.ReportChannel == null) {
			System.out.println("ExceptionReporter: Javajaotan.ReportChannel == null.");
			System.out.println("ExceptionReporter did not work properly!");
			return;
		}
		if (exception == null) {
			throw new NullPointerException("Throwable exception is null!");
		}
		exception.printStackTrace();

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		exception.printStackTrace(pw);

		try {
			EmbedBuilder builder = new EmbedBuilder();
			builder.withTitle("javajaotan Error Reporter");
			builder.withColor(Color.RED);
			builder.appendField("StackTrace", "```" + sw.toString() + "```", false);
			builder.appendField("Message", "```" + exception.getMessage() + "```", false);
			builder.appendField("Cause", "```" + exception.getCause() + "```", false);
			builder.withTimestamp(System.currentTimeMillis());
			RequestBuffer.request(() -> {
				try {
					Main.ReportChannel.sendMessage(builder.build());
				} catch (DiscordException discordexception) {
					Main.DiscordExceptionError(Main.class, Main.ReportChannel, discordexception);
				}
			});
		} catch (Exception e) {
			try {
				String text = "javajaotan Error Reporter (" + Library.sdfFormat(new Date()) + ")\n"
						+ "---------- StackTrace ----------\n"
						+ sw.toString() + "\n"
						+ "---------- Message ----------\n"
						+ exception.getMessage() + "\n"
						+ "---------- Cause ----------\n"
						+ exception.getCause();
				InputStream stream = new ByteArrayInputStream(
						text.getBytes("utf-8"));
				RequestBuffer.request(() -> {
					try {
						Main.ReportChannel.sendFile("javajaotan Error Reporter", stream,
								"Javajaotanreport" + System.currentTimeMillis() + ".txt");
					} catch (DiscordException discordexception) {
						Main.DiscordExceptionError(Main.class, Main.ReportChannel,
								discordexception);
					}
				});
			} catch (UnsupportedEncodingException ex) {
				return;
			}
		}
	}
}
