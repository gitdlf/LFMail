package cn.lfsenior.entrys;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author lfsenior
 *
 * 下午2:23:43
 */
public class EmailInfo implements Serializable {

	/**
	 * 接收方的邮箱地址
	 */
	private String[] toAddress;

	/**
	 * 邮件主题
	 */
	private String subject;

	/**
	 * 邮件内容
	 */
	private String content;

	/**
	 * 待上传附件的路径及名称、或下载附件的地址及名称
	 */
	private List<String> attachmentFiles = new ArrayList<String>();

	/**
	 * 发送方的地址
	 */
	private String fromAddress;

	/**
	 * 发送的时间
	 */
	private Date sentDate;

	/**
	 * 是否需要邮件回执
	 */
	private boolean needReply;

	/**
	 * 是否已读
	 */
	private boolean isReaded;

	/**
	 * 是否包含附件
	 */
	private boolean containsAttachments = false;

	/**
	 * 抄送
	 */
	private String[] carbonCopy;

	/**
	 * 暗抄送
	 */
	private String[] darkCopy;

	/**
	 * 此邮件的Message-ID
	 */
	private String messageID;

	public String[] getToAddress() {
		return toAddress;
	}

	public void setToAddress(String[] toAddress) {
		this.toAddress = toAddress;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<String> getAttachmentFiles() {
		return attachmentFiles;
	}

	public void setAttachmentFiles(List<String> attachmentFiles) {
		this.attachmentFiles = attachmentFiles;
	}

	public String getMessageID() {
		return messageID;
	}

	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}

	public String[] getDarkCopy() {
		return darkCopy;
	}

	public void setDarkCopy(String[] darkCopy) {
		this.darkCopy = darkCopy;
	}

	public String[] getCarbonCopy() {
		return carbonCopy;
	}

	public void setCarbonCopy(String[] carbonCopy) {
		this.carbonCopy = carbonCopy;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public Date getSentDate() {
		return sentDate;
	}

	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}

	public boolean isNeedReply() {
		return needReply;
	}

	public void setNeedReply(boolean needReply) {
		this.needReply = needReply;
	}

	public boolean isReaded() {
		return isReaded;
	}

	public void setReaded(boolean isReaded) {
		this.isReaded = isReaded;
	}

	public boolean isContainsAttachments() {
		return containsAttachments;
	}

	public void setContainsAttachments(boolean containsAttachments) {
		this.containsAttachments = containsAttachments;
	}

	@Override
	public String toString() {
		return super.toString() + "\n[ReadEmailInfo {\n\tfromAddress="
				+ fromAddress + ", \n\tsentDate=" + sentDate
				+ ", \n\tneedReply=" + needReply + ", \n\tisReaded=" + isReaded
				+ ", \n\tcontainsAttachments=" + containsAttachments
				+ ", \n\tcarbonCopy=" + Arrays.toString(carbonCopy)
				+ ", \n\tdarkCopy=" + Arrays.toString(darkCopy)
				+ ", \n\tmessageID=" + messageID + "\n]}";
	}

}
