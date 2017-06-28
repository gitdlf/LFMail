package cn.lfsenior.util;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Store;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import cn.lfsenior.entrys.EmailInfo;


/**
 * 
 * @author lfsenior
 *
 * 下午1:53:24
 */
public class FetchingEmailUtil {
	
	private final static String SAVE_ATTACHMENTS_PATH = "./resource";
	
	
	public List<EmailInfo> fetchingAllEmailInfos(Store store, boolean closeFolder) throws Exception {
		List<EmailInfo> emailInfos = new ArrayList<EmailInfo>();
		
		// create the folder object and open it
		Folder emailFolder = store.getFolder("INBOX");
		emailFolder.open(Folder.READ_ONLY);

		// retrieve all messages from the folder in an array 
		Message[] messages = emailFolder.getMessages();
		for (Message message : messages) {
			EmailInfo emailInfo = new EmailInfo();
			writePart(message, emailInfo);
			emailInfos.add(emailInfo);
		}
		if (closeFolder) {
			emailFolder.close(false);
		}
		return emailInfos;
	}
	/**
	 * 获取一份最新的邮件
	 * @return
	 * @throws Exception 
	 */
	public EmailInfo fetchingLatestEmailFromStore(Store store, boolean closeFolder) throws Exception {
		EmailInfo emailInfo = new EmailInfo();
		
		// create the folder object and open it
		Folder emailFolder = store.getFolder("INBOX");
		emailFolder.open(Folder.READ_ONLY);

		// retrieve the latest messages from the folder in an array 
		Message message = emailFolder.getMessage(emailFolder.getMessageCount());
		writePart(message, emailInfo);

		if (closeFolder) {
			emailFolder.close(false);
		}
		return emailInfo;
	}

	/*
	 * This method checks for content-type based on which, it processes and
	 * fetches the content of the message
	 */
	private void writePart(Part p, EmailInfo emailInfo) throws Exception {
		if (p instanceof Message)
			// Call methos writeEnvelope
			writeEnvelope((Message) p, emailInfo);

//		System.out.println("-------------Body---------------");
//		System.out.println("CONTENT-TYPE: " + p.getContentType());

		// check if the content is plain text
		if (p.isMimeType("text/plain")) {
//			System.out.println("This is plain text");
			// 设置文本内容的正文
			emailInfo.setContent(MimeUtility.decodeText(p.getContent().toString()));
		}
		// check if the content has attachment
		else if (p.isMimeType("multipart/*")) {
			emailInfo.setContainsAttachments(true);
//			System.out.println("--------------包含附件-------------");
			Multipart mp = (Multipart) p.getContent();
			int count = mp.getCount();
			for (int i = 0; i < count; i++)
				writePart(mp.getBodyPart(i), emailInfo);
		}
		// check if the content is a nested message // 包含内嵌的内容
		else if (p.isMimeType("message/rfc822")) {
//			System.out.println("This is a Nested Message");
//			System.out.println("---------------------------");
			writePart((Part) p.getContent(), emailInfo);
		}
		// check if the content is an inline image
		else if (p.isMimeType("image/jpeg")) {		// emailInfo
			Object o = p.getContent();
			InputStream x = (InputStream) o;
			// Construct the required byte array
//			System.out.println("x.length = " + x.available());

			// 开启线程保存文件
			new SaveFileThread(x, "image.jpg").start();
			
		} else if (p.getContentType().contains("image/")) {
			System.out.println("content type" + p.getContentType());
			File f = new File("E:\\email_attachments\\temp\\image" + new Date().getTime() + ".jpg");
			DataOutputStream output = new DataOutputStream(
					new BufferedOutputStream(new FileOutputStream(f)));
			com.sun.mail.util.BASE64DecoderStream test = (com.sun.mail.util.BASE64DecoderStream) p
					.getContent();
			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = test.read(buffer)) != -1) {
				output.write(buffer, 0, bytesRead);
			}
			output.flush();
			output.close();
		} else {
			Object o = p.getContent();
			if (o instanceof String) {
				// 设置文本内容的正文
//				emailInfo.setContent(MimeUtility.decodeText(p.getContent().toString()));
//				System.out.println("====================");
//				System.out.println(emailInfo.getContent());
//				System.out.println("====================");
			} else if (o instanceof InputStream) {
//				System.out.println("--------------- 附件 InputStream ------------");
//				System.out.println("p.getContentType():\n" + p.getContentType());
//				System.out.println("-------------p.getContentType()--------------");
				
				String attachmentFileName = p.getDataHandler().getDataSource().getName();
				if (attachmentFileName != null) {
					attachmentFileName = MimeUtility.decodeText(attachmentFileName);
//					System.out.println("附件文件名：" + attachmentFileName);
					InputStream fileIn = p.getDataHandler().getDataSource().getInputStream();
					List<String> attachmentFiles = emailInfo.getAttachmentFiles();
					attachmentFiles.add(SAVE_ATTACHMENTS_PATH +attachmentFileName);
					
					// 保存附件路径及名称
					emailInfo.setAttachmentFiles(attachmentFiles);
					// 开启线程保存文件
					new SaveFileThread(fileIn, attachmentFileName).start();
				}
				
			} else {
//				System.out.println("This is an unknown type");
//				System.out.println("---------------------------");
//				System.out.println(o.toString());
			}
		}

	}

	/*
	 * This method would print FROM,TO and SUBJECT of the message
	 */
	private static void writeEnvelope(Message m, EmailInfo emailInfo) throws Exception {
//		System.out.println("------------HEADER---------------");
		Address[] a;

		// 设置发送时间
		emailInfo.setSentDate(m.getSentDate());
		// FROM
		if ((a = m.getFrom()) != null) {
			// 注意需要 decode
//			System.out.println("From address: " + MimeUtility.decodeText(a[0].toString()));
			emailInfo.setFromAddress(MimeUtility.decodeText(a[0].toString()));
		}

		// TO
		try {
			a = m.getRecipients(Message.RecipientType.TO);
		} catch (AddressException e) {
//			System.out.println("*********** TO Illegal semicolon *************");
//			System.out.println(e.getMessage());
		}
		if ( a != null) {
			String[] toes = new String[a.length];
			for (int j = 0; j < a.length; j++) {
//				System.out.println("TO address: " + MimeUtility.decodeText(a[j].toString()));
				toes[j] = MimeUtility.decodeText(a[j].toString());
			}
			emailInfo.setToAddress(toes);
		}
		
		// CC
		try {
			a = m.getRecipients(Message.RecipientType.CC);
		} catch (Exception e) {
//			System.out.println("*********** CC Illegal semicolon *************");
//			System.out.println(e.getMessage());
		}
		if (a != null) {
			String[] toes = new String[a.length];
			for (int j = 0; j < a.length; j++) {
//				System.out.println("TO CC: " + MimeUtility.decodeText(a[j].toString()));
				toes[j] = MimeUtility.decodeText(a[j].toString());
			}
			emailInfo.setCarbonCopy(toes);
		}
		
		// BCC
		try {
			a = m.getRecipients(Message.RecipientType.BCC);
		} catch (Exception e) {
//			System.out.println("*********** BCC Illegal semicolon *************");
//			System.out.println(e.getMessage());
		}
		if (a != null) {
			String[] toes = new String[a.length];
			for (int j = 0; j < a.length; j++) {
//				System.out.println("TO BCC: " + MimeUtility.decodeText(a[j].toString()));
				toes[j] = MimeUtility.decodeText(a[j].toString());
			}
			emailInfo.setDarkCopy(toes);
		}
		
		// SUBJECT
		if (m.getSubject() != null) {
//			System.out.println("SUBJECT: " + MimeUtility.decodeText(m.getSubject()));
			emailInfo.setSubject(MimeUtility.decodeText(m.getSubject()));
		}

		// 判断邮件是否已读
//		boolean isNew = false;
//		Flags flags = m.getFlags();
//		Flags.Flag[] flag = flags.getSystemFlags();
//		System.out.println("flags的长度:　" + flag.length);
//		for (int i = 0; i < flag.length; i++) {
//			if (flag[i] == Flags.Flag.SEEN) {
//				isNew = true;
//				System.out.println("seen email...");
//				// break;
//			}
//		}
//		emailInfo.setReaded(isNew);
		/*
		 * This message is seen. This flag is implicitly set by the
		 * implementation when the this Message's content is returned to the
		 * client in some form. The getInputStream and getContent methods on
		 * Message cause this flag to be set.
		 */
		emailInfo.setReaded(false);
		
		// 判断是否需要回执
		boolean needReply = m.getHeader("Disposition-Notification-To") != null ? true : false;
		emailInfo.setNeedReply(needReply);
		
		// 获取该邮件的Message-ID
		String messageID = ((MimeMessage)m).getMessageID();
		emailInfo.setMessageID(messageID);
	}
	
	/**
	 * 保存附件的线程
	 * @author dell
	 *
	 */
	private class SaveFileThread extends Thread {
		
		private String filename;
		private InputStream fileIn;

		public SaveFileThread(InputStream fileIn, String filename) {
			this.filename = filename;
			this.fileIn = fileIn;
		}
		
		@Override
		public void run() {
			FileOutputStream out = null;
			try {
				out = new FileOutputStream(SAVE_ATTACHMENTS_PATH + filename);
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = fileIn.read(buffer)) != -1) {
					out.write(buffer, 0, len);
				}
				out.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (out != null) {
						out.close();
						out = null;
					}
					if (fileIn != null) {
						fileIn.close();
						fileIn = null;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
