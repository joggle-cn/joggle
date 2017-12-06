package com.wuweibi.bullet.controller;
/**
 * Created by marker on 2017/12/6.
 */

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;

/**
 * @author marker
 * @create 2017-12-06 下午9:19
 **/
@RestController
public class DeviceController {


    @RequestMapping(value = "/device", method = RequestMethod.GET)
    @ResponseBody
    public Map device(HttpServletRequest request ){



        String uuid = UUID.randomUUID().toString().replaceAll("-","");


        JSONObject result = new JSONObject();
        result.put("uuid", uuid);
        return result;
    }



}
