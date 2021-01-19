package net.xiaoxiangshop.service.impl;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.core.metadata.IPage;

import net.xiaoxiangshop.Page;
import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.dao.SocialUserDao;
import net.xiaoxiangshop.entity.SocialUser;
import net.xiaoxiangshop.entity.User;
import net.xiaoxiangshop.service.SocialUserService;

import java.util.Set;

/**
 * Service - 社会化用户
 * 
 */
@Service
public class SocialUserServiceImpl extends BaseServiceImpl<SocialUser> implements SocialUserService {

	@Inject
	private SocialUserDao socialUserDao;

	@Override
	@Transactional(readOnly = true)
	public SocialUser find(String loginPluginId, String uniqueId) {
		return socialUserDao.findByAttribute(loginPluginId, uniqueId);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<SocialUser> findPage(User user, Pageable pageable) {
		IPage<SocialUser> iPage = getPluginsPage(pageable);
		iPage.setRecords(socialUserDao.findPage(iPage, getPageable(pageable), user));
		return super.findPage(iPage, pageable);
	}

	@Override
	public void bindUser(User user, SocialUser socialUser, String uniqueId) {
		Assert.notNull(socialUser, "[Assertion failed] - socialUser is required; it must not be null");
		Assert.hasText(uniqueId, "[Assertion failed] - uniqueId must have text; it must not be null, empty, or blank");

		if (!StringUtils.equals(socialUser.getUniqueId(), uniqueId) || socialUser.getUser() != null) {
			return;
		}

		socialUser.setUser(user);
		socialUserDao.update(socialUser);
	}

	@Override
	public Set<SocialUser> finByUserId(Long userId) {
		return socialUserDao.findSet("user_id",userId);
	}
}