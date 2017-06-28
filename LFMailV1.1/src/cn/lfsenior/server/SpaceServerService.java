package cn.lfsenior.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import cn.lfsenior.entrys.EmailServerInfo;
import cn.lfsenior.entrys.SpaceServerInfo;

/**
 * 
 * @author lfsenior
 *
 *         下午3:10:44
 */
public class SpaceServerService {
	/**
	 * 获取配置的语音服务器信息
	 * 
	 * @return
	 */
	public SpaceServerInfo getConfigSpaceServerInfo() {
		// 读取配置文件
		Properties properties = new Properties();
		InputStream inStream = null;
		try {
			// 获取类路径(/)下的配置文件
			inStream = getClass().getResourceAsStream("/spaceServerConfig.properties");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("配置文件加载失败");
			return null;
		}
		try {
			properties.load(inStream);
			String userName = properties.getProperty("userName");
			String apiKey = properties.getProperty("apiKey");
			String secretKey = properties.getProperty("secretKey");
			String cuid = properties.getProperty("cuid");
			String per = properties.getProperty("per");
			String spd = properties.getProperty("spd");
			String pit = properties.getProperty("pit");
			String vol = properties.getProperty("vol");

			SpaceServerInfo spaceServerInfo = new SpaceServerInfo();

			if (userName != null && !("".equals(userName.trim()))) {
				spaceServerInfo.setUserName(userName.trim());
			}
			if (apiKey != null && !("".equals(apiKey.trim()))) {
				spaceServerInfo.setApiKey(apiKey.trim());
			}
			if (secretKey != null && !("".equals(secretKey.trim()))) {
				spaceServerInfo.setSecretKey(secretKey);
			}
			if (cuid != null && !("".equals(cuid.trim()))) {
				spaceServerInfo.setCuid(cuid.trim());
			}
			if (per != null && !("".equals(per.trim()))) {
				spaceServerInfo.setPer(per.trim());
			}
			if (spd != null && !("".equals(spd.trim()))) {
				spaceServerInfo.setSpd(spd.trim());
			}
			if (pit != null && !("".equals(pit.trim()))) {
				spaceServerInfo.setPit(pit.trim());
			}
			if (vol != null && !("".equals(vol.trim()))) {
				spaceServerInfo.setVol(vol.trim());
			}
			return spaceServerInfo;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
