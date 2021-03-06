package com.lancslot.morn.utils.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.Message.RecipientType;
import javax.mail.internet.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class EmailUtil {

    private static final Logger logger = LoggerFactory.getLogger(EmailUtil.class);

    public static String TEXT = "text";
    public static String HTML = "html";

    /**
     * 功能描述：
     *
     * @param title
     * @param content
     * @param properties
     * @param attachFiles
     * @param addresses
     * @throws Exception
     * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
     * @since 2017年7月19日
     */
    public static void sendEmail(String title, String content, Properties properties, List<File> attachFiles,
                                 List<String> addresses, String mimeType) throws Exception {
        Assert.isTrue(properties != null && !properties.isEmpty(), "email properties can't be null or empty");
        Assert.isTrue(StringUtils.hasText(title), "emial title can't be null or empty");
        Assert.isTrue(StringUtils.hasText(content), "emial content can't be null or empty");
        Assert.isTrue((addresses != null && addresses.size() != 0), "emial address can't be null or empty");
        parameterCheck(properties);
        Session session = getSession(properties);

        MimeMessage message = new MimeMessage(session);
        setEmailContent(message, content, attachFiles == null ? new ArrayList<File>() : attachFiles, mimeType);
        message.setSubject(title, "utf-8");
        message.setFrom(new InternetAddress(properties.get("mail.smtp.from").toString(),
                properties.get("mail.smtp.from.chName").toString()));
//		设置抄送人员
//		message.setRecipients(RecipientType.BCC, addresses);
//		设置暗抄送人员
//		message.setRecipient(RecipientType.CC, address);
        Address[] eamilAddress = new InternetAddress[addresses.size()];
        int index = 0;
        for (String address : addresses) {
            eamilAddress[index++] = new InternetAddress(address);
        }
//		设置收件人
        message.setRecipients(RecipientType.TO, eamilAddress);
        logger.info(">>>>>sendEmail ready {}",content);
        Transport.send(message);
    }

    private static Session getSession(final Properties properties) {
        Session session;
        if (!properties.containsKey("mail.smtp.auth")) {
            properties.setProperty("mail.smtp.auth", "true");
        }
        if ("true".equals(properties.get("mail.smtp.auth").toString())) {
            session = Session.getDefaultInstance(properties, new Authenticator() {
                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    String userName = properties.get("mail.smtp.user").toString();
                    String password = properties.get("mail.smtp.password").toString();
                    return new PasswordAuthentication(userName, password);
                }
            });
        } else {
            session = Session.getDefaultInstance(properties);
        }
        return session;
    }


    private static void parameterCheck(Properties properties) {
        Assert.notNull(properties.get("mail.smtp.host"), "mail.smtp.host property can't be null");
        Assert.notNull(properties.get("mail.smtp.from"), "mail.smtp.from property can't be null");
        Assert.notNull(properties.get("mail.smtp.user"), "mail.smtp.user property can't be null");
        Assert.notNull(properties.get("mail.smtp.password"), "mail.smtp.password property can't be null");
    }

    private static void setEmailContent(MimeMessage message, String content, List<File> fileList,
                                        String mimeType) throws Exception {
        Multipart multipart = new MimeMultipart("mixed");
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        if (HTML.equalsIgnoreCase(mimeType)) {
            mimeBodyPart.setContent(content, "text/html; charset=utf-8");
        } else {
            mimeBodyPart.setContent(content, "text/plain; charset=utf-8");
        }
        multipart.addBodyPart(mimeBodyPart);
        if (!fileList.isEmpty()) {
            for (File file : fileList) {
                MimeBodyPart temp = new MimeBodyPart();
                FileDataSource fds = new FileDataSource(file); //得到数据源
                temp.setDataHandler(new DataHandler(fds)); //得到附件本身并放入BodyPart
                temp.setFileName(MimeUtility.encodeText(fds.getName()));  //得到文件名并编码（防止中文文件名乱码）同样放入BodyPart
                multipart.addBodyPart(temp);
            }
        }
        message.setContent(multipart);
    }

}
