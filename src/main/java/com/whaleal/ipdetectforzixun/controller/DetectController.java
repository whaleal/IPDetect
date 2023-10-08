package com.whaleal.ipdetectforzixun.controller;

import com.alibaba.fastjson.JSONObject;
import com.whaleal.ipdetectforzixun.service.IDetectService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lyz
 * @desc
 * @create: 2023-09-27 09:47
 **/

@RestController
@RequestMapping("/ip")
public class DetectController {

    private static final Logger log = LogManager.getLogger(DetectController.class);

    private static  final  int port = 22;

    @Value("${international.address}")
    private String outAddress;

    @Autowired
    IDetectService iDetectService;

    @GetMapping("/detect")
    public ResponseEntity ipDetectCN(@RequestParam("ip") String ipAddress) {
        Map<String,Object> map = new HashMap<>();
        //检测国内
        //ping icmp
        Boolean ping = iDetectService.ping(ipAddress);
        log.info("测试 {}国内ping通 ? {}:" , ipAddress,ping);
        map.put("国内ICMP",ping ? "可用" : "不可用");

        //telnet tcp
        Boolean status = iDetectService.telnet(ipAddress, port);
        log.info("测试 {}国内telnet通 ? {}:" , ipAddress,status);
        map.put("国内TCP",status ? "可用" : "不可用");

        String url  = outAddress + "?ip=" + ipAddress;
        //检测国外
        Connection connection = Jsoup.connect(url).ignoreHttpErrors(true).ignoreContentType(true);

        JSONObject jsonObject = null;
        try {
            Document document = connection.get();
            String s = document.body().ownText();
            jsonObject = JSONObject.parseObject(s);

            for (String key : jsonObject.keySet()){
                map.put(key, jsonObject.get(key));
            }
        } catch (IOException e) {
            map.put("errorMsg","调用国外API时发生了异常");
            log.error("调用国外API检测{}时发生了异常：{}",ipAddress,e.getMessage());
        }

        return new ResponseEntity(map,HttpStatus.OK);
    }


    @GetMapping("/detect/international")
    public ResponseEntity<Map<String,Object>> ipDetectInternational(@RequestParam("ip") String ipAddress){

        Map<String,Object> map = new HashMap<>();

        //检测国外的
        //ping icmp
        Boolean ping = iDetectService.ping(ipAddress);
        log.info("ping通{}:" , ping);
        map.put("国外ICMP",ping ? "可用" : "不可用");

        //telnet tcp
        Boolean status = iDetectService.telnet(ipAddress, port);
        System.out.println("telnet通:{}" + ping);
        map.put("国外TCP",status ? "可用" : "不可用");

        return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
    }



}