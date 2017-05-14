package com.fish.api.controller;

import com.fish.controller.CommonController;
import com.fish.service.IDemoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

/**
 * Created by fish on 2017/5/14.
 */
@Controller
@RequestMapping("demo")
public class DemoController extends CommonController {

    @Resource
    private IDemoService demoService;

    @RequestMapping(value = "sayHell", method = RequestMethod.POST)
    public void demo() {
        demoService.sayHell("zhasan");
    }


}
