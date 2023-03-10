package com.wen.email.controller;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wen.base.data.HeaderModel;
import com.wen.base.exception.BusinessExcetion;
import com.wen.base.exception.MessageCode;
import com.wen.email.data.EmailResponse;
import com.wen.email.service.EmailService;

@RestController
@RequestMapping("/api")
public class EmailController {

	private static final Logger log = LoggerFactory.getLogger(EmailController.class);

	@Autowired
	private EmailService emailService;

	@PostMapping(value = "/sendMailWithLogo")
	public ResponseEntity<?> sendMail(@RequestBody Map<String, Object> body) {
		EmailResponse response = new EmailResponse();
		HeaderModel header = response.getHeaderModel();
		try {
			emailService.sendMailWithLogo(
					MapUtils.getString(body, "FROM", ""),
					MapUtils.getString(body, "PERSONAL", ""),
					MapUtils.getString(body, "TO", ""), 
					MapUtils.getString(body, "SUBJECT", ""),
					MapUtils.getString(body, "CONTENT", ""));
			header.setReturnCode(MessageCode.CODE0000.getCode());
			header.setReturnMessage(MessageCode.CODE0000.getMessage());
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} catch (BusinessExcetion e1) {
			header.setReturnCode(e1.getCode());
			header.setReturnMessage(e1.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		} catch (Exception e) {
			log.error("系統錯誤", e);
			header.setReturnCode(MessageCode.CODE9999.getCode());
			header.setReturnMessage(MessageCode.CODE9999.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}

}
