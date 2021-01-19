package net.xiaoxiangshop.service.impl;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;

import net.xiaoxiangshop.dao.MessageDao;
import net.xiaoxiangshop.dao.MessageGroupDao;
import net.xiaoxiangshop.entity.Message;
import net.xiaoxiangshop.entity.MessageGroup;
import net.xiaoxiangshop.entity.MessageStatus;
import net.xiaoxiangshop.entity.User;
import net.xiaoxiangshop.service.MessageService;

/**
 * Service - 消息
 * 
 */
@Service
public class MessageServiceImpl extends BaseServiceImpl<Message> implements MessageService {

	@Inject
	private MessageDao messageDao;
	@Inject
	private MessageGroupDao messageGroupDao;

	@Override
	@Transactional(readOnly = true)
	public List<Message> findList(MessageGroup messageGroup, User user) {
		return messageDao.findList(messageGroup, user);
	}

	@Override
	@Transactional(readOnly = true)
	public Long unreadMessageCount(MessageGroup messageGroup, User user) {
		return messageDao.unreadMessageCount(messageGroup, user);
	}

	@Override
	public void consult(MessageGroup messageGroup, User currentUser) {
		Assert.notNull(messageGroup, "[Assertion failed] - messageGroup is required; it must not be null");
		Assert.notNull(currentUser, "[Assertion failed] - currentUser is required; it must not be null");

		if (currentUser.equals(messageGroup.getUser1())) {
			messageGroup.getUser1MessageStatus().setIsRead(true);
		} else {
			messageGroup.getUser2MessageStatus().setIsRead(true);
		}

		List<Message> messages = messageDao.findList(messageGroup, currentUser);
		for (Message message : messages) {
			if (currentUser.equals(message.getToUser())) {
				message.getToUserMessageStatus().setIsRead(true);
			}
		}
	}

	@Override
	public void send(User.Type type, User fromUser, User toUser, String content, String ip) {
		Assert.notNull(type, "[Assertion failed] - type is required; it must not be null");
		Assert.notNull(fromUser, "[Assertion failed] - fromUser is required; it must not be null");
		Assert.notNull(toUser, "[Assertion failed] - toUser is required; it must not be null");
		Assert.hasText(content, "[Assertion failed] - content must have text; it must not be null, empty, or blank");
		Assert.hasText(ip, "[Assertion failed] - ip must have text; it must not be null, empty, or blank");
		Assert.state(!fromUser.equals(toUser), "[Assertion failed] - fromUser must not be toUser");

		MessageGroup messageGroup = messageGroupDao.findByUser(fromUser, toUser);
		if (messageGroup != null) {
			if (fromUser.equals(messageGroup.getUser1())) {
				messageGroup.setUser1MessageStatus(new MessageStatus(true, false));
				messageGroup.setUser2MessageStatus(new MessageStatus(false, false));
			} else {
				messageGroup.setUser1MessageStatus(new MessageStatus(false, false));
				messageGroup.setUser2MessageStatus(new MessageStatus(true, false));
			}
		} else {
			messageGroup = new MessageGroup();
			messageGroup.setUser1(fromUser);
			messageGroup.setUser2(toUser);
			messageGroup.setUser1MessageStatus(new MessageStatus(true, false));
			messageGroup.setUser2MessageStatus(new MessageStatus(false, false));
			messageGroup.setId(IdWorker.getId());
			messageGroup.setVersion(0L);
			messageGroup.setCreatedDate(new Date());
			messageGroupDao.save(messageGroup);
		}

		Message message = new Message();
		message.setContent(content);
		message.setIp(ip);
		message.setFromUser(fromUser);
		message.setToUser(toUser);
		message.setFromUserMessageStatus(new MessageStatus(true, false));
		message.setToUserMessageStatus(new MessageStatus(false, false));
		message.setMessageGroup(messageGroup);
		super.save(message);
	}

	@Override
	public void reply(MessageGroup messageGroup, User fromUser, String content, String ip) {
		Assert.notNull(messageGroup, "[Assertion failed] - messageGroup is required; it must not be null");
		Assert.notNull(fromUser, "[Assertion failed] - fromUser is required; it must not be null");
		Assert.hasText(content, "[Assertion failed] - content must have text; it must not be null, empty, or blank");
		Assert.hasText(ip, "[Assertion failed] - ip must have text; it must not be null, empty, or blank");

		User toUser = null;
		if (fromUser.equals(messageGroup.getUser1())) {
			toUser = messageGroup.getUser2();
			messageGroup.getUser2MessageStatus().setIsRead(false);
			messageGroup.getUser2MessageStatus().setIsDeleted(false);
		} else {
			toUser = messageGroup.getUser1();
			messageGroup.getUser1MessageStatus().setIsRead(false);
			messageGroup.getUser1MessageStatus().setIsDeleted(false);
		}

		Message message = new Message();
		message.setContent(content);
		message.setIp(ip);
		message.setFromUser(fromUser);
		message.setToUser(toUser);
		message.setFromUserMessageStatus(new MessageStatus(true, false));
		message.setToUserMessageStatus(new MessageStatus(false, false));
		message.setMessageGroup(messageGroup);
		super.save(message);
	}

}