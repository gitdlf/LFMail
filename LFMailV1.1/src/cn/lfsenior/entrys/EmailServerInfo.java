package cn.lfsenior.entrys;


/**
 * 登陆的邮箱服务器的信息，包括服务器的 host 和 ip，用户名和密码等
 * @author lfsenior
 *
 * 下午1:56:15
 */
public class EmailServerInfo {
	/**
	 * 发送邮件的服务器的IP地址
	 */
	private String mailServerPOP3Host;
	private String mailServerSMTPHost;

	/**
	 * 登陆的邮箱
	 */
	private String myEmailAddress;

	/**
	 * 登陆邮件发送服务器的用户名和密码
	 */
	private String userName;
	private String password;

	/**
	 * 是否需要身份验证，默认为true
	 */
	private boolean validate = true;

	/**
	 * 是否支持ssl链接
	 */
	private boolean ssl = true;

	public String getMailServerPOP3Host() {
		return mailServerPOP3Host;
	}

	public void setMailServerPOP3Host(String mailServerPOP3Host) {
		this.mailServerPOP3Host = mailServerPOP3Host;
	}

	public String getMailServerSMTPHost() {
		return mailServerSMTPHost;
	}

	public void setMailServerSMTPHost(String mailServerSMTPHost) {
		this.mailServerSMTPHost = mailServerSMTPHost;
	}

	public String getMyEmailAddress() {
		return myEmailAddress;
	}

	public void setMyEmailAddress(String myEmailAddress) {
		this.myEmailAddress = myEmailAddress;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isValidate() {
		return validate;
	}

	public void setValidate(boolean validate) {
		this.validate = validate;
	}

	public boolean isSsl() {
		return ssl;
	}

	public void setSsl(boolean ssl) {
		this.ssl = ssl;
	}

}
