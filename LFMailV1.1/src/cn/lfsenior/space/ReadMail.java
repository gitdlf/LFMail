package cn.lfsenior.space;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import cn.lfsenior.entrys.EmailInfo;
import cn.lfsenior.entrys.EmailServerInfo;
import cn.lfsenior.util.EmailTemplateUtil;
import cn.lfsenior.util.LFEmailStaticInfo;

/**
 * 
 * @author lfsenior
 *
 *         下午2:52:43
 */
public class ReadMail {
	/**
	 * 获取所有邮件
	 * @throws Exception
	 */
	@Test
	public void ReadAllEmails() throws Exception {
		// 获取配置的登陆邮件服务器的信息
		EmailServerInfo emailServerInfo = EmailTemplateUtil.getConfigEmailServerInfo();
		
		List<EmailInfo> emailInfos = EmailTemplateUtil.getAllEmailInfos(emailServerInfo);
		ObjectOutputStream outstream=null;
		//邮件总数
		LFEmailStaticInfo.EMAILNUMBER= emailInfos.size();
		/**
		 * 根据seedDate命名没封邮件
		 * 如果存在则跳过
		 */
		for (EmailInfo emailInfo : emailInfos) {
			Date sentDate = emailInfo.getSentDate();
			if(sentDate==null){
				continue;
			}
			String path="./resource/"+sentDate.getTime()+".ls";
			File file=new File(path);
			if(file.exists()){
				continue;
			}
			outstream=new ObjectOutputStream(new FileOutputStream(path));
			outstream.writeObject(emailInfo);
			outstream.close();
		}
	}
}
