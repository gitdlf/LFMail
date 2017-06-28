package cn.lfsenior.util;

import java.util.List;

import javax.mail.Session;

import cn.lfsenior.entrys.EmailInfo;
import cn.lfsenior.entrys.EmailServerInfo;
import cn.lfsenior.server.EmailServerService;

/**
 * 
 * @author lfsenior
 *
 * 下午1:48:18
 */
public class EmailTemplateUtil {

	private static EmailServerService emailServerService = new EmailServerService();
	
	/**
	 * 获取配置的邮箱服务器的信息
	 * @return
	 */
	public static EmailServerInfo getConfigEmailServerInfo() {
		return  emailServerService.getConfigEmailServerInfo();
	}
	
	/**
	 * 获取所有邮件
	 * @param emailServerInfo
	 * @return
	 */
	public static List<EmailInfo> getAllEmailInfos(EmailServerInfo emailServerInfo) {
		Session sendMailSession = emailServerService.loginEmailServer(emailServerInfo, true);
		if (sendMailSession != null) {
			System.out.println(emailServerInfo.getMailServerPOP3Host() + " 登陆成功！");
			System.out.println("正在读取邮件...");
			List<EmailInfo> emailInfos = emailServerService.readAllEmailInfos(sendMailSession, emailServerInfo);
			return emailInfos;
		} else {
			System.out.println(emailServerInfo.getMailServerPOP3Host() + " 登陆失败！");
			return null;
		}
	}
	
	/**
	 * 获取最近的一份邮件，并保存附件
	 * 
	 * @param emailServerInfo
	 * @return
	 */
	public static EmailInfo getLatestOneEmailInfo(EmailServerInfo emailServerInfo) {
		Session sendMailSession = emailServerService.loginEmailServer(emailServerInfo, true);
		if (sendMailSession != null) {
			System.out.println(emailServerInfo.getMailServerPOP3Host() + " 登陆成功！");
			System.out.println("正在读取邮件...");
			EmailInfo emailInfo = emailServerService.getLatestOneEmailFromStore(sendMailSession, emailServerInfo);
			return emailInfo;
		} else {
			System.out.println(emailServerInfo.getMailServerPOP3Host() + " 登陆失败！");
			return null;
		}
	}
}
