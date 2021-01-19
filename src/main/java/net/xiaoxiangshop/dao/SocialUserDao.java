package net.xiaoxiangshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import net.xiaoxiangshop.entity.SocialUser;
import net.xiaoxiangshop.entity.User;

/**
 * Dao - 社会化用户
 * 
 */
public interface SocialUserDao extends BaseDao<SocialUser> {

	/**
	 * 查找社会化用户
	 * 
	 * @param loginPluginId
	 *            登录插件ID
	 * @param uniqueId
	 *            唯一ID
	 * @return 社会化用户，若不存在则返回null
	 */
	SocialUser findByAttribute(@Param("loginPluginId")String loginPluginId, @Param("uniqueId")String uniqueId);

	/**
	 * 查找社会化用户分页
	 * 
	 * @param user
	 *            用户
	 * @param pageable
	 *            分页信息
	 * @return 社会化用户分页
	 */
	List<SocialUser> findPage(IPage<SocialUser> iPage, @Param("ew")QueryWrapper<SocialUser> queryWrapper, @Param("user")User user);

}