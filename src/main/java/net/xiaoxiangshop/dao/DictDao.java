package net.xiaoxiangshop.dao;

import net.xiaoxiangshop.entity.Dict;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface DictDao  extends BaseDao<Dict>{
    List<Dict> find(@Param("dict")Dict dict);
}
