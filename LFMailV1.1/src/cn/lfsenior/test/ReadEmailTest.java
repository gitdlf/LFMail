package cn.lfsenior.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import cn.lfsenior.entrys.EmailInfo;
import cn.lfsenior.entrys.EmailServerInfo;
import cn.lfsenior.util.EmailTemplateUtil;
import cn.lfsenior.space.BDSpaceFlow;


/**
 * 
 * @author lfsenior
 *
 * 下午1:59:08
 */
public class ReadEmailTest {

	@Test
	public void testReadAllEmails() throws Exception {
		
		// 获取配置的登陆邮件服务器的信息
		EmailServerInfo emailServerInfo = EmailTemplateUtil.getConfigEmailServerInfo();
		
		List<EmailInfo> emailInfos = EmailTemplateUtil.getAllEmailInfos(emailServerInfo);
		for (EmailInfo emailInfo : emailInfos) {
			Date sentDate = emailInfo.getSentDate();
			if(sentDate==null){
				continue;
			}
			String path="./resource/"+sentDate.getTime()+".ls";
			ObjectOutputStream outstream=new ObjectOutputStream(new FileOutputStream(path));
			outstream.writeObject(emailInfo);
			outstream.close();
		}
	}
	
	@Test
	public void testReadEmailTemplate() throws FileNotFoundException {
		
		// 获取配置的登陆邮件服务器的信息
		EmailServerInfo emailServerInfo = EmailTemplateUtil.getConfigEmailServerInfo();
		
		EmailInfo emailInfo = EmailTemplateUtil.getLatestOneEmailInfo(emailServerInfo);
		System.out.println(emailInfo.toString());
	}
}
