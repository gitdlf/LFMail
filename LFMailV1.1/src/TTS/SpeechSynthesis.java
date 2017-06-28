package TTS;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.bind.DatatypeConverter;

import org.json.JSONObject;

public class SpeechSynthesis {

	private final String serverURL = "http://tsn.baidu.com/text2audio";
	private String token = "";
	private String text = "";
	private String saveURL = "";
	private boolean isDetailed = false;
	// pit6最好啦
	private int per = 0; // 男1女0
	private int spd = 6; // 音速0-9
	private int pit = 6; // 音调0-9
	private int vol = 5; // 音量0-9
	// put your own params here
	private String apiKey = "";
	private String secretKey = "";
	private String cuid = "";

	public SpeechSynthesis(String cuid) {
		this.cuid = cuid;
		checkLocalToken();
	}

	public SpeechSynthesis(String apiKey, String secretKey, String cuid,String per,String spd,String pit,String vol) {

		this.apiKey = apiKey;
		this.secretKey = secretKey;
		this.cuid = cuid;
		this.per=Integer.valueOf(per);
		this.spd=Integer.valueOf(spd);
		this.pit=Integer.valueOf(pit);
		this.vol=Integer.valueOf(vol);
		checkLocalToken();
	}

	private void checkLocalToken() {
		File tokenFile = new File("TTS_token.dat");
		if (tokenFile.exists()) {
			try {
				token = new Scanner(tokenFile).nextLine();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			try {
				getToken();
				PrintWriter output = new PrintWriter(tokenFile);
				output.print(token);
				output.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("获取认证失败,请检查apiKey,secreKey,cuid位置或者内容是否设置正确");
				System.exit(1);
			}
		}
	}

	// 与验证服务器验证 获取token
	public void getToken() throws Exception {
		String getTokenURL = "https://openapi.baidu.com/oauth/2.0/token?grant_type=client_credentials" + "&client_id="
				+ apiKey + "&client_secret=" + secretKey;
		HttpURLConnection conn = (HttpURLConnection) new URL(getTokenURL).openConnection(); // 与验证服务器连接
		token = new JSONObject(printResponse(conn)).getString("access_token");// 获取access_token:...后的taken
	}

	private String printResponse(HttpURLConnection conn) throws Exception {
		if (conn.getResponseCode() != 200) {
			// request error
			System.err.println("请求失败,请检查网络");
			return "";
		}
		InputStream is = conn.getInputStream();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		String line;
		StringBuffer response = new StringBuffer();
		while ((line = rd.readLine()) != null) {
			response.append(line);
			response.append('\r');
		}
		rd.close();
		if (isDetailed) {
			System.out.println(new JSONObject(response.toString()).toString(4));
		}
		return response.toString();
	}
	// 把语音文件转换成byte数组存储

	private byte[] getData(HttpURLConnection conn) throws IOException {
		InputStream is = conn.getInputStream();
		ArrayList byteList = new ArrayList<>();
		int value;
		while ((value = is.read()) != -1) { // 读取流的技巧
			byteList.add(value);

		}
		// 这里是一个冗余的方式

		byte[] bytes = new byte[byteList.size()];
		int[] intbytes = new int[bytes.length];
		for (int i = 0; i < bytes.length; i++) {
			intbytes[i] = (int) byteList.get(i);
		}
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) intbytes[i];
		}

		is.close();
		System.out.println("语音成功合成");
		return bytes;
	}

	// 利用获得的token与语音服务器连接
	public void methodByGET() throws Exception {
		if (saveURL.isEmpty() || text.isEmpty()) {
			System.err.println("未识别语音保存位置 或者未设置发送内容");
			System.exit(1);
		}
		HttpURLConnection conn = (HttpURLConnection) new URL(
				serverURL + "?tex=" + new String((text).getBytes(), "UTF-8") + "&lan=zh" + "&cuid=" + cuid + "&ctp=1"
						+ "&tok=" + token + "&per=" + per + "&spd=" + spd + "&pit=" + pit + "&vol=" + vol)
								.openConnection();

		// add request header
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-Type", "audio/mp3");

		conn.setDoInput(true);
		conn.setDoOutput(true);

		// 输出到指定目录
		DataOutputStream out = new DataOutputStream(new FileOutputStream(saveURL)); // 能保存为音频格式说明传递的就是音频本体
																					// 而不是html
		out.write(getData(conn));
	}

	public void methodByGET(String text) {
		setText(text);

		try {
			methodByGET();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void setVoice(int speed, int pitch, int volume) {
		setSpeed(speed);
		setPitch(pitch);
		setVolume(volume);
	}

	public void setSpeed(int speed) {
		if (speed > -1 && speed < 10)
			this.spd = speed;
		else {
			System.err.println("大小范围0-9");
			System.exit(1);
		}
	}

	public void setPitch(int pitch) {
		if (pitch > -1 && pitch < 10)
			this.pit = pitch;
		else {
			System.err.println("大小范围0-9");
			System.exit(1);
		}
	}

	public void setVolume(int volume) {
		if (volume > -1 && volume < 10)
			this.vol = volume;
		else {
			System.err.println("大小范围0-9");
			System.exit(1);
		}
	}

	public void changeSex() {
		if (per == 0) {
			per = 1;
		} else {
			per = 0;
		}
	}

	public void setIsDetailed(boolean isDetailed) {
		this.isDetailed = isDetailed;
	}

	public void setSaveURL(String path) {
		this.saveURL = path;
	}

	public void setText(String text) {
		StringBuffer textBuffer = new StringBuffer(text);
		for (int i = 0; i < textBuffer.length(); i++) {
			if (textBuffer.charAt(i) == ' ') {
				textBuffer.replace(i, i + 1, ",");
			}
		}
		this.text = textBuffer.toString();
	}

	public void setToken(String token) {
		this.token = token;
	}
}
