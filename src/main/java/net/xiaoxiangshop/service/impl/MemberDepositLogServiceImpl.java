package net.xiaoxiangshop.service.impl;

import javax.inject.Inject;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.xiaoxiangshop.Filter;
import net.xiaoxiangshop.Order;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;

import net.xiaoxiangshop.Page;
import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.dao.MemberDepositLogDao;
import net.xiaoxiangshop.entity.Member;
import net.xiaoxiangshop.entity.MemberDepositLog;
import net.xiaoxiangshop.service.MemberDepositLogService;

import java.util.List;
import java.util.Set;

/**
 * Service - 会员预存款记录
 * 
 */
@Service
public class MemberDepositLogServiceImpl extends BaseServiceImpl<MemberDepositLog> implements MemberDepositLogService {

	@Inject
	private MemberDepositLogDao memberDepositLogDao;

	@Override
	@Transactional(readOnly = true)
	public Page<MemberDepositLog> findPage(Member member, Pageable pageable) {

		IPage<MemberDepositLog> iPage = getPluginsPage(pageable);
		if (pageable.getOrderDirection() == null && pageable.getOrderProperty() == null) {
			pageable.setOrderDirection(net.xiaoxiangshop.Order.Direction.DESC);
			pageable.setOrderProperty("createdDate");
		}
		String  searchProperty=pageable.getSearchProperty();
		String  searchValue=pageable.getSearchValue();
		if("orders.sn".equals(searchProperty)){
			pageable.setSearchProperty(null);
			pageable.setSearchValue(null);
		}
		QueryWrapper<MemberDepositLog> queryWrapper=getPageable(pageable);
		if("orders.sn".equals(searchProperty)&&searchValue!=null&&!"".equals(searchValue)){
			queryWrapper.inSql(true,"orders","SELECT id FROM orders WHERE sn = '"+searchValue+"'");
		}
		List<Filter> filters=pageable.getFilters();
		if(filters!=null){
			for(Filter filter:filters){
				String property=filter.getProperty();
				Object value=filter.getValue();
				Filter.Operator operator=filter.getOperator();
				if(operator.equals(Filter.Operator.GE)){
					queryWrapper.ge(true,property,value);
				}
				if(operator.equals(Filter.Operator.LE)){
					queryWrapper.le(true,property,value);
				}
			}
			// 排序属性
			String orderProperty = net.xiaoxiangshop.util.StringUtils.camel2Underline(pageable.getOrderProperty());
			if (StringUtils.isNotEmpty(orderProperty)) {
				if (pageable.getOrderDirection().equals(Order.Direction.ASC)) {
					queryWrapper.orderByAsc(true, orderProperty);
				}
				if (pageable.getOrderDirection().equals(Order.Direction.DESC)) {
					queryWrapper.orderByDesc(true, orderProperty);
				}
			}
		}
		iPage.setRecords(memberDepositLogDao.findPage(iPage,queryWrapper, member));
		return super.findPage(iPage,pageable);
	}

	@Override
	public void insert(MemberDepositLog memberDepositLog) {
		memberDepositLogDao.save(memberDepositLog);
	}

	@Override
	public Set<MemberDepositLog> findListByUserId(Member member) {
		return memberDepositLogDao.findSet("member_id",member.getId());
	}

}