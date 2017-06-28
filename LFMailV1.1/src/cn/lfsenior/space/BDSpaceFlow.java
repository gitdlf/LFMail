package cn.lfsenior.space;

import java.awt.image.BufferedImageFilter;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

import TTS.SpeechSynthesis;
import cn.lfsenior.entrys.EmailInfo;
import cn.lfsenior.entrys.SpaceServerInfo;
import cn.lfsenior.server.SpaceServerService;
import javazoom.jl.player.Player;

/**
 * 
 * @author lfsenior
 *
 *         下午3:03:01
 */
public class BDSpaceFlow {
	private EmailInfo emailInfo;
	private static SpaceServerInfo spaceServerInfo = new SpaceServerService().getConfigSpaceServerInfo();
	private static SpeechSynthesis TTS = new SpeechSynthesis(spaceServerInfo.getApiKey(),
			spaceServerInfo.getSecretKey(), spaceServerInfo.getCuid(), spaceServerInfo.getPer(),
			spaceServerInfo.getSpd(), spaceServerInfo.getPit(), spaceServerInfo.getVol());

	public BDSpaceFlow(EmailInfo emailInfo) {
		this.emailInfo = emailInfo;
	}

	public void spaceTypeGirl() throws Exception {
		/*
		 * 语音播报流程
		 * 
		 */
		// String
		// flow_start="主人，你有"+LFEmailStaticInfo.NOREDEMAILNUMBER+"封未读邮件。";
		String flow_send = "来自" + emailInfo.getFromAddress() + "的邮件";
		spaceStart(flow_send);
		String flow_sendDate = "接受时间：" + emailInfo.getSentDate();
		spaceStart(flow_sendDate);
		String flow_subject = "主题：" + emailInfo.getSubject();
		spaceStart(flow_subject);
		String flow_content = "内容：" + emailInfo.getContent();
		System.out.println(flow_content);
		BufferedReader br = new BufferedReader(
				new InputStreamReader(new ByteArrayInputStream(flow_content.getBytes()), "utf-8"));
		String line = "";
		while ((line = br.readLine()) != null) {
			if (line.equals("")) {
				continue;
			}
			spaceStart(line);
		}
	}

	public void spaceStart(String text) throws Exception {
		TTS.setSaveURL("test.mp3");
		System.out.println("内容：" + text);
		TTS.setText(text);
		try {
			TTS.methodByGET();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedInputStream buffer = new BufferedInputStream(new FileInputStream("test.mp3"));
		Player play = new Player(buffer);
		play.play();
	}

}
