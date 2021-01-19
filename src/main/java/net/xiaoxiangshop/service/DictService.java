package net.xiaoxiangshop.service;

import net.xiaoxiangshop.entity.Dict;
import net.xiaoxiangshop.entity.StoreProductCategory;

import java.util.List;

public interface DictService extends BaseService<Dict> {

    List<Dict> find(Dict  dict);

}
