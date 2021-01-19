package net.xiaoxiangshop.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.core.metadata.IPage;

import net.xiaoxiangshop.Page;
import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.dao.DeliveryTemplateDao;
import net.xiaoxiangshop.entity.DeliveryCenter;
import net.xiaoxiangshop.entity.DeliveryTemplate;
import net.xiaoxiangshop.entity.Order;
import net.xiaoxiangshop.entity.Store;
import net.xiaoxiangshop.service.DeliveryTemplateService;

/**
 * Service - 快递单模板
 * 
 */
@Service
public class DeliveryTemplateServiceImpl extends BaseServiceImpl<DeliveryTemplate> implements DeliveryTemplateService {

	@Inject
	private DeliveryTemplateDao deliveryTemplateDao;

	@Override
	@Transactional(readOnly = true)
	public DeliveryTemplate findDefault(Store store) {
		return deliveryTemplateDao.findDefault(store);
	}

	@Override
	@Transactional(readOnly = true)
	public List<DeliveryTemplate> findList(Store store) {
		return deliveryTemplateDao.findList(store);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<DeliveryTemplate> findPage(Store store, Pageable pageable) {
		IPage<DeliveryTemplate> iPage = getPluginsPage(pageable);
		iPage.setRecords(deliveryTemplateDao.findPage(iPage, getPageable(pageable), store));
		return super.findPage(iPage, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public String resolveContent(DeliveryTemplate deliveryTemplate, Store store, DeliveryCenter deliveryCenter, Order order) {
		Assert.notNull(deliveryTemplate, "[Assertion failed] - deliveryTemplate is required; it must not be null");

		List<String> tagNames = new ArrayList<>();
		List<String> values = new ArrayList<>();

		for (DeliveryTemplate.StoreAttribute storeAttribute : DeliveryTemplate.StoreAttribute.values()) {
			tagNames.add(storeAttribute.getTagName());
			values.add(storeAttribute.getValue(store));
		}
		for (DeliveryTemplate.DeliveryCenterAttribute deliveryCenterAttribute : DeliveryTemplate.DeliveryCenterAttribute.values()) {
			tagNames.add(deliveryCenterAttribute.getTagName());
			values.add(deliveryCenterAttribute.getValue(deliveryCenter));
		}
		for (DeliveryTemplate.OrderAttribute orderAttribute : DeliveryTemplate.OrderAttribute.values()) {
			tagNames.add(orderAttribute.getTagName());
			values.add(orderAttribute.getValue(order));
		}

		return StringUtils.replaceEachRepeatedly(deliveryTemplate.getContent(), tagNames.toArray(new String[tagNames.size()]), values.toArray(new String[values.size()]));
	}

	@Override
	@Transactional
	public boolean save(DeliveryTemplate deliveryTemplate) {
		Assert.notNull(deliveryTemplate, "[Assertion failed] - deliveryTemplate is required; it must not be null");

		if (BooleanUtils.isTrue(deliveryTemplate.getIsDefault())) {
			deliveryTemplateDao.clearDefault(deliveryTemplate.getStore());
		}
		return super.save(deliveryTemplate);
	}

	@Override
	@Transactional
	public DeliveryTemplate update(DeliveryTemplate deliveryTemplate) {
		Assert.notNull(deliveryTemplate, "[Assertion failed] - deliveryTemplate is required; it must not be null");

		DeliveryTemplate pDeliveryTemplate = super.update(deliveryTemplate);
		if (BooleanUtils.isTrue(pDeliveryTemplate.getIsDefault())) {
			deliveryTemplateDao.clearDefaultExclude(pDeliveryTemplate);
		}
		return pDeliveryTemplate;
	}

}