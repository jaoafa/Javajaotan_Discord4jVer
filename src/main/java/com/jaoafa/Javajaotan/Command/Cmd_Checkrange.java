package com.jaoafa.Javajaotan.Command;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jaoafa.Javajaotan.CommandPremise;
import com.jaoafa.Javajaotan.Main;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;

public class Cmd_Checkrange implements CommandPremise {
	@Override
	public void onCommand(IDiscordClient client, IGuild guild, IChannel channel, IUser author, IMessage message,
			String[] args) {
		String text = String.join(" ", args);
		Pattern p = Pattern.compile("#([0-9]+) \\D*?(\\-?[0-9]+) \\D*?(\\-?[0-9]+)");
		Matcher m = p.matcher(text);

		String debug = "";
		LinkedList<Integer> X = new LinkedList<>();
		LinkedList<Integer> Z = new LinkedList<>();
		while (m.find()) {
			String keynum = m.group(1);
			String X_str = m.group(2);
			String Z_str = m.group(3);

			X.add(Integer.valueOf(X_str));
			Z.add(Integer.valueOf(Z_str));

			debug += "Added #" + keynum + " : " + X_str + " " + Z_str + "\n";
		}

		double blocks = calcBlockNumber(X, Z);

		String system_msg = "特に問題はありません。";
		if (!checkBlocks(X, Z)) {
			system_msg = "範囲指定が不適切です。時計回りまたは反時計回りに指定してください。";
		} else if (blocks != Math.floor(blocks)) {
			system_msg = "ブロック数が不適切(整数でない)です。範囲が正確に指定されていない可能性があります。";
		} else if (blocks >= 2500000) {
			system_msg = "拡張最大制限ブロック数(2,500,000ブロック)以上です。「自治体関連方針」により原則的に認可できません。";
		} else if (blocks >= 250000) {
			system_msg = "初期規定ブロック数(250,000ブロック)以上です。新規登録の場合は「規定ブロック数を超える明確な理由」が必要です。";
			;
		} else if (Math.round(blocks) == 0) {
			system_msg = "範囲情報を入力してください。";
		}
		String LASTMSG = system_msg;
		String LASTDEBUG = debug;
		RequestBuffer.request(() -> {
			try {
				message.reply(blocks + " Blocks (" + X.size() + ")\n"
						+ "メッセージ: `" + LASTMSG + "`\n"
						+ "\n範囲指定に誤りがあり、修正を行う場合は申請のメッセージを削除するなど、__**明確に申請の取り消し**__を行ってください。\n"
						+ "また、申請後に未だ認可がされてない状態で申請内容を変更したい場合、__**申請のメッセージを編集するのではなく再度申請をし直して**__ください。\n"
						+ "\n"
						+ "```" + LASTDEBUG + "```");
			} catch (DiscordException discordexception) {
				Main.DiscordExceptionError(getClass(), channel, discordexception);
			}
		});
		//Main.ExceptionReporter(channel, new NullPointerException("NullPointerException"));

		//new Task_MeetingVote(true).run();
	}

	private double calcBlockNumber(List<Integer> X, List<Integer> Z) {
		double size = 0; // 面積
		double side = 0; // 辺の長さ
		double blocks = 0; // ブロック数

		double x1 = 0; // 1点目のX座標値
		double x2 = 0; // 2点目のX座標値
		double z1 = 0; // 1点目のZ座標値
		double z2 = 0; // 2点目のZ座標値

		/* 図形の面積を計算 */
		for (int i = 0; i < X.size(); i++) {
			if ((i + 1) >= X.size()) {
				x1 = X.get(i);
				x2 = X.get(0);
				z1 = Z.get(i);
				z2 = Z.get(0);
			} else {
				x1 = X.get(i);
				x2 = X.get(i + 1);
				z1 = Z.get(i);
				z2 = Z.get(i + 1);
			}
			// 外積を計算して加算
			size += (x1 * z2) - (x2 * z1);
		}
		size = size / 2;
		size = Math.abs(size);

		for (int i = 0; i < X.size(); i++) {
			if ((i + 1) >= X.size()) {
				side = side
						+ Math.abs(X.get(i) - X.get(0))
						+ Math.abs(Z.get(i) - Z.get(0));
			} else {
				side = side
						+ Math.abs(X.get(i) - X.get(i + 1))
						+ Math.abs(Z.get(i) - Z.get(i + 1));
			}
		}

		/* ブロック数を計算 */
		if (size > 0) {
			// ブロック数 = 面積 + (辺の長さ / 2) + 1
			blocks = size + (side / 2) + 1;
		}
		return blocks;
	}

	private boolean checkBlocks(List<Integer> X, List<Integer> Z) {
		int oldx = X.get(0);
		int oldz = Z.get(0);
		String changed = null; // 変化したのがXかZか。最初はnull、XまたはZを代入
		for (int i = 1; i <= X.size(); i++) {
			int x;
			int z;
			if (i == X.size()) {
				x = X.get(0);
				z = Z.get(0);
			} else {
				x = X.get(i);
				z = Z.get(i);
			}
			if (changed == null) {
				// 最初だけ動作
				if (oldx != x && oldz == z) {
					// Xが変わってZは変わっていない
					oldx = x;
					changed = "X";
				} else if (oldx == x && oldz != z) {
					// Xが変わっていなくてZは変わっている
					oldz = z;
					changed = "Z";
				} else {
					// XとZ両方変わっているもしくは両方変わっていない
					return false;
				}
			} else {
				// 最初以外動作
				if (changed.equals("Z") && oldx != x && oldz == z) {
					// 前回Zが変わっていて、Xが変わってZは変わっていない
					oldx = x;
					changed = "X";
				} else if (changed.equals("X") && oldx == x && oldz != z) {
					// 前回Xが変わっていて、Xが変わっていなくてZは変わっている
					oldz = z;
					changed = "Z";
				} else {
					// XとZ両方変わっているもしくは両方変わっていない
					// またはX,Zが連続して変わった
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public String getDescription() {
		return "申請範囲の情報をパースします。";
	}

	@Override
	public String getUsage() {
		return "/checkrange";
	}

	@Override
	public boolean isjMSOnly() {
		return true;
	}
}
