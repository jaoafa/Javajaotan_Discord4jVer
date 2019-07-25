package com.jaoafa.Javajaotan;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.Timer;

import com.jaoafa.Javajaotan.Channel.ChannelMainEvent;
import com.jaoafa.Javajaotan.Command.MessageMainEvent;
import com.jaoafa.Javajaotan.Event.Event_ServerBanned;
import com.jaoafa.Javajaotan.Event.Event_ServerJoin;
import com.jaoafa.Javajaotan.Event.Event_ServerLeave;
import com.jaoafa.Javajaotan.Lib.ErrorReporter;
import com.jaoafa.Javajaotan.Task.Task_VerifiedCheck;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.DiscordException;

public class Javajaotan {
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	public static IChannel ReportChannel = null;
	private static IDiscordClient client = null;
	public static void main(String[] args) {
		File f = new File("conf.properties");
		Properties props;
		try{
			InputStream is = new FileInputStream(f);

			// プロパティファイルを読み込む
			props = new Properties();
			props.load(is);
		}catch(FileNotFoundException e){
			// ファイル生成
			props = new Properties();
			props.setProperty("token", "PLEASETOKEN");
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
		if(token.equalsIgnoreCase("PLEASETOKEN")){
			System.out.println("Please Token!");
			return;
		}

		IDiscordClient client = createClient(token, true);
		EventDispatcher dispatcher = client.getDispatcher();
		dispatcher.registerListener(new MessageMainEvent());
		dispatcher.registerListener(new ChannelMainEvent());
		dispatcher.registerListener(new Event_ServerJoin());
		dispatcher.registerListener(new Event_ServerLeave());
		dispatcher.registerListener(new Event_ServerBanned());

		 Runtime.getRuntime().addShutdownHook(
			 new Thread(
				 () -> {
					 System.out.println("Exit");
				 }
		 ));

		 Task_VerifiedCheck Task_VerifiedCheck = new Task_VerifiedCheck();
		 Timer timer = new Timer();
		 timer.scheduleAtFixedRate(Task_VerifiedCheck, 10000, 60000); // 1分

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
			ErrorReporter.report(e);
			return null;
		}
	}
	public static void setClient(IDiscordClient client){
		Javajaotan.client = client;
	}
	public static IDiscordClient getClient(){
		return client;
	}
}
