package net.xiaoxiangshop.controller.admin;


import net.xiaoxiangshop.service.DictService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;

@Controller("businessStoreDictController")
@RequestMapping("/business/dict")
public class DictController extends BaseController  {

    @Inject
    private DictService dictService;
}
