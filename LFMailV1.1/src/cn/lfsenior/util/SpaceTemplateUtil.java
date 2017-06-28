package cn.lfsenior.util;

import cn.lfsenior.entrys.EmailServerInfo;
import cn.lfsenior.entrys.SpaceServerInfo;
import cn.lfsenior.server.EmailServerService;
import cn.lfsenior.server.SpaceServerService;

public class SpaceTemplateUtil {
private static SpaceServerService spaceServerService = new SpaceServerService();
	
	/**
	 * 获取配置的语音服务信息
	 * @return
	 */
	public static SpaceServerInfo getConfigSpaceServerInfo() {
		return  spaceServerService.getConfigSpaceServerInfo();
	}
}
