package com.wen.email.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.wen.base.exception.BusinessExcetion;
import com.wen.base.exception.MessageCode;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public class EmailService {

	@Autowired
	private Configuration freemarkerConfig;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private Environment environment;

	public void sendMail(String from, String personal, String to, String subject, String content)
			throws MessagingException, IOException, TemplateException {

		// CHECK PARAMETERS
		if (from.isEmpty() || to.isEmpty() || subject.isEmpty()) {
			throw new BusinessExcetion(MessageCode.CODE1001);
		}

		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

		// SET 寄件人 & 收信人 & 主旨
		mimeMessageHelper.setFrom(from, personal);
		mimeMessageHelper.setTo(to);
		mimeMessageHelper.setSubject(subject);

		// SET LOGO IMAGE & CONTENT
		Resource logoClassPathResource = new ClassPathResource(
				"static" + File.separator + environment.getProperty("EMAIL_LOGO_NAME"));
		ByteArrayResource logo = new ByteArrayResource(logoClassPathResource.getInputStream().readAllBytes());
		Map<String, Object> parameters = Map.of("logo", "logo.png", "content", content);

		Template template = freemarkerConfig.getTemplate(environment.getProperty("EMAIL_TEMPLATE"));
		String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, parameters);
		mimeMessageHelper.setText(text, true);

		// This line must be after helper.setText(), otherwise the logo won't be
		// displayed
		mimeMessageHelper.addInline("logo.png", logo, "image/png");
		// 寄出
		mailSender.send(mimeMessage);
	}
}
