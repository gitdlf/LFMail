package cn.lfsenior.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import cn.lfsenior.entrys.EmailInfo;
import cn.lfsenior.entrys.EmailServerHostAndPort;
import cn.lfsenior.entrys.EmailServerInfo;
import cn.lfsenior.util.FetchingEmailUtil;


/**
 * 
 * @author lfsenior
 *
 * 下午1:50:35
 */
public class EmailServerService {
	
	/**
	 * 获取配置的邮箱服务器的信息
	 * @return
	 */
	public EmailServerInfo getConfigEmailServerInfo() {
		// 读取配置文件
		Properties properties = new Properties();
		InputStream inStream = null;
		try {
			// 获取类路径(/)下的配置文件
			inStream = getClass().getResourceAsStream("/emailServerConfig.properties");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("配置文件加载失败");
			return null;
		}
		try {
			properties.load(inStream);
			String mailServer_POP3Host = properties.getProperty("mailServer_POP3Host");
			String mailServer_SMTPHost = properties.getProperty("mailServer_SMTPHost");
			String myEmailAddress = properties.getProperty("myEmailAddress");
			String userName = properties.getProperty("userName");
			String password = properties.getProperty("password");
			String validate = properties.getProperty("validate");
			
			EmailServerInfo emailServerInfo = new EmailServerInfo();
			
			if (mailServer_POP3Host != null && !("".equals(mailServer_POP3Host.trim()))) {
				emailServerInfo.setMailServerPOP3Host(mailServer_POP3Host.trim());
			}
			if (mailServer_SMTPHost != null && !("".equals(mailServer_SMTPHost.trim()))) {
				emailServerInfo.setMailServerSMTPHost(mailServer_SMTPHost.trim());
			}
			if (userName != null && !("".equals(userName.trim()))) {
				emailServerInfo.setUserName(userName.trim());
			}
			if (password != null && !("".equals(password.trim()))) {
				emailServerInfo.setPassword(password.trim());
			}
			if (myEmailAddress != null && !("".equals(myEmailAddress.trim()))) {
				emailServerInfo.setMyEmailAddress(myEmailAddress.trim());
			}
			if (validate != null && !("".equals(validate.trim()))) {
				boolean isValidate = "true".equals(validate.trim()) ? true : false;
				emailServerInfo.setValidate(isValidate);
			}
			
			System.out.println("--------邮件服务器配置信息--------");
			System.out.println(emailServerInfo.toString());
			return emailServerInfo;
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
	

	/**
	 * 根据 EmailServerInfo 信息登陆邮件服务器，返回mail回话对象
	 * 
	 * @param emailServerInfo
	 * @return
	 */
	public Session loginEmailServer(EmailServerInfo emailServerInfo, boolean useReadProtocol) {
		Session sendMailSession = null;
		Authenticator authentication = null;
		
		try {
			Properties properties = getProperties(emailServerInfo, useReadProtocol);
			// 如果需要身份认证，则创建一个密码验证器
			if (emailServerInfo.isValidate()) {
				authentication = new Authenticator() {
					
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(emailServerInfo.getUserName(), emailServerInfo.getPassword());
					}
				};
			}
			// 获取回话对象
			sendMailSession = Session.getDefaultInstance(properties, useReadProtocol ? null : authentication);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return sendMailSession;
	}

	
	
	/**
	 * 获取最近的一份邮件，并保存附件
	 * @param sendMailSession
	 * @param emailServerInfo
	 * @return
	 */
	public EmailInfo getLatestOneEmailFromStore(Session sendMailSession, EmailServerInfo emailServerInfo) {
		EmailInfo emailInfo = null;
		Store store = null;
        try {
            store = sendMailSession.getStore("pop3");
            store.connect(emailServerInfo.getUserName(), emailServerInfo.getPassword());
 
            FetchingEmailUtil fetchingEmailUtil = new FetchingEmailUtil();
            
            emailInfo = fetchingEmailUtil.fetchingLatestEmailFromStore(store, true);
            
            // close the store
	        return emailInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (store != null) {
                    store.close();
                }
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
	}
	
	/**
	 * 读取所有邮件
	 * @param sendMailSession
	 * @param emailServerInfo
	 * @return
	 */
	public List<EmailInfo> readAllEmailInfos(Session sendMailSession, EmailServerInfo emailServerInfo) {
		
		List<EmailInfo> allEmailInfos = null;
		Store store = null;
        try {
            store = sendMailSession.getStore("pop3");
            store.connect(emailServerInfo.getUserName(), emailServerInfo.getPassword());
 
            FetchingEmailUtil fetchingEmailUtil = new FetchingEmailUtil();
            
            allEmailInfos = fetchingEmailUtil.fetchingAllEmailInfos(store, true);
            
            // close the store
	        return allEmailInfos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (store != null) {
                    store.close();
                }
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
	}
	
	/**
	 * 获得邮件会话属性
	 * <b>注：此处需要适配 SMTP、POP3、IMAP</>
	 * 
	 */
	private Properties getProperties(EmailServerInfo emailServerInfo, boolean useReadProtocol) {
		Properties p = new Properties();
		if (useReadProtocol) {
	        p.put("mail.pop3.host", emailServerInfo.getMailServerPOP3Host());
	        p.put("mail.pop3.port", EmailServerHostAndPort.POP3_PORT);
	        p.put("mail.pop3.auth", emailServerInfo.isValidate() ? "true" : "false");
	        p.put("mail.pop3s.starttls.enable", "true");
		} else {
			p.put("mail.smtp.host", emailServerInfo.getMailServerSMTPHost());
			p.put("mail.smtp.port", EmailServerHostAndPort.SMTP_PORT);
			p.put("mail.smtp.auth", emailServerInfo.isValidate() ? "true" : "false");
			p.put("mail.smtp.starttls.enable", "true");
		}
		return p;
	}

}
