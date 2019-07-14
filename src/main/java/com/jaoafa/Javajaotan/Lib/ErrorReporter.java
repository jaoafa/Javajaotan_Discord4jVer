package com.jaoafa.Javajaotan.Lib;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import com.jaoafa.Javajaotan.Javajaotan;

import sx.blah.discord.util.EmbedBuilder;

public class ErrorReporter {
	public static void report(Throwable exception){
		if(Javajaotan.ReportChannel == null){
			System.out.println("Javajaotan.ReportChannel == null error.");
			exception.printStackTrace();
			return;
		}
		if(Javajaotan.getClient() == null){
			System.out.println("Javajaotan.getClient() == null error.");
			exception.printStackTrace();
			return;
		}

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
			Javajaotan.ReportChannel.sendMessage(builder.build());
		}catch(Exception e){
			try{
				String text = "javajaotan Error Reporter (" + Javajaotan.sdf.format(new Date()) + ")\n"
						+ "---------- StackTrace ----------\n"
						+ sw.toString() + "\n"
						+ "---------- Message ----------\n"
						+ exception.getMessage() + "\n"
						+ "---------- Cause ----------\n"
						+ exception.getCause();
				InputStream stream = new ByteArrayInputStream(
					text.getBytes("utf-8")
				);
				Javajaotan.ReportChannel.sendFile("javajaotan Error Reporter", stream, "Javajaotanreport" + System.currentTimeMillis() + ".txt");
			}catch(UnsupportedEncodingException ex){
				return;
			}
		}

	}
}
