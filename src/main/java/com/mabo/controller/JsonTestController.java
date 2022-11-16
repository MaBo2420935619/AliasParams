package com.mabo.controller;

import com.alibaba.fastjson.JSONObject;
import com.mabo.utils.JSONUtil;
import com.mabo.entity.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JsonTestController {
    @RequestMapping("jsontest")
    public Object jsontest(@RequestBody JSONObject param) throws Exception {
        User user1 = param.toJavaObject(User.class);
        //使用自定义工具转实体类
        User user2 = JSONUtil.json2JavaObject(param, User.class);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user1",user1);
        jsonObject.put("user2",user2);
        return jsonObject;
    }
}
