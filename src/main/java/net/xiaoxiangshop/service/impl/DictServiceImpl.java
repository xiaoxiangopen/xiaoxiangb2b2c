package net.xiaoxiangshop.service.impl;
import net.xiaoxiangshop.dao.DictDao;
import net.xiaoxiangshop.entity.Dict;
import net.xiaoxiangshop.service.DictService;
import org.springframework.stereotype.Service;
import javax.inject.Inject;
import java.util.List;

@Service
public class DictServiceImpl  extends BaseServiceImpl<Dict> implements DictService {

    @Inject
    private DictDao dictDao;

    @Override
    public List<Dict> find(Dict dict) {
        return dictDao.find(dict);
    }
}
