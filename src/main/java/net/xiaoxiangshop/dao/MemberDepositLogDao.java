package net.xiaoxiangshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import net.xiaoxiangshop.entity.Member;
import net.xiaoxiangshop.entity.MemberDepositLog;

/**
 * Dao - 会员预存款记录
 * 
 */
public interface MemberDepositLogDao extends BaseDao<MemberDepositLog> {

	/**
	 * 查找会员预存款记录分页
	 * 
	 * @param member
	 *            会员
	 * @return 会员预存款记录分页
	 */
	List<MemberDepositLog> findPage(IPage<MemberDepositLog> iPage, @Param("ew")QueryWrapper<MemberDepositLog> queryWrapper, @Param("member")Member member);

}