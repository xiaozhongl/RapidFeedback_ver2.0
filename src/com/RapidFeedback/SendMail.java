package com.RapidFeedback;

import java.util.*;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;  
import javax.mail.*;
import javax.mail.internet.*;

/**
 * @ClassName SendMail
 * @Description This class has all the functions of sending an email.
 *
 * @author Dinghao Yong, Jingxian Hu
 */
public class SendMail {
	private String host = ""; // smtp server
	private String from = ""; // send address
	private String to = ""; // receiver address
	private String affix = ""; // attachment local address
	private String affixName = ""; // attachment name in mail
	private String user = ""; // mail account
	private String pwd = ""; // mail password
	private String subject = ""; // mail subject

	public void setAddress(String from, String to, String subject) {
		this.from = from;
		this.to = to;
		this.subject = subject;
	}

	public void setAffix(String affix, String affixName) {
		this.affix = affix;
		this.affixName = affixName;
	}

	public boolean send(String host, String user, String pwd,
			String msg/* , DataSource source */) {
		this.host = host;
		this.user = user;
		this.pwd = pwd;

		boolean result = true;

		Properties props = new Properties();

		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.host", "smtp.gmail.com");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		props.put("mail.debug", "true");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");
		// create a session
		Session session = Session.getDefaultInstance(props);

		// to show the send process in console
		session.setDebug(true);

		// the message
		MimeMessage message = new MimeMessage(session);
		try {
			// load the sender's address
			message.setFrom(new InternetAddress(from));
			// load the receiver address
			message.addRecipient(Message.RecipientType.TO,
					new InternetAddress(to));
			// load the subject
			message.setSubject(subject);

			// add the text and the attachment to the multipart
			Multipart multipart = new MimeMultipart();

			// set the body part
			BodyPart contentPart = new MimeBodyPart();

			// contentPart.setText("This is a feedback for your COMP9000
			// Asignment1 Presentation.\r\n"
			// + "If you have any problems, please dont hesitate to contact the
			// lecturers/tutors.");
			contentPart.setText(msg);
			multipart.addBodyPart(contentPart);

			// load the attachment from the local machine
			BodyPart messageBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(affix);
			// add the content of the attachment
			messageBodyPart.setDataHandler(new DataHandler(source));
			// add the title of the attachment, ONLY ENGLISH!!
			messageBodyPart.setFileName(affixName);
			multipart.addBodyPart(messageBodyPart);

			// put the multipart into the mail content
			message.setContent(multipart);
			// save the message
			message.saveChanges();
			// send
			Transport transport = session.getTransport("smtp");
			// connect to the host mail box
			transport.connect(host, user, pwd);
			// send the mail
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	public boolean sendSimpleMail(String host, String user, String pwd,
			String msg) {

		boolean result = true;

		Properties props = new Properties();

		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.host", "smtp.gmail.com");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		props.put("mail.debug", "true");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");

		// create a session
		Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, pwd);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(to));
			message.setSubject(subject);

			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			mimeBodyPart.setText(msg);

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(mimeBodyPart);

			message.setContent(multipart);

			Transport.send(message);
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}
}
