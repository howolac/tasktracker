//package com.tasktracker.controller;
//
//import com.aliyun.dysmsapi20170525.Client;
//import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
//import com.aliyun.teaopenapi.models.Config;
//import com.tasktracker.common.lang.Result;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/sms")
//public class SmsController {
//
//    @Value("${aliyun.accessKeyId}")
//    private String accessKeyId;
//
//    @Value("${aliyun.accessKeySecret}")
//    private String accessKeySecret;
//
//    @Value("${aliyun.sms.signName}")
//    private String signName;
//
//    @Value("${aliyun.sms.templateCode}")
//    private String templateCode;
//
//    @GetMapping("/send")
//    public Result sendSms() {
//        try {
//            // 初始化客户端
//            Config config = new Config()
//                    .setAccessKeyId(accessKeyId)
//                    .setAccessKeySecret(accessKeySecret);
//            config.endpoint = "dysmsapi.aliyuncs.com";
//            Client client = new Client(config);
//
//            // 配置请求参数
//            SendSmsRequest sendSmsRequest = new SendSmsRequest()
//                    .setSignName(signName)
//                    .setTemplateCode(templateCode)
//                    .setPhoneNumbers("xx")
//                    .setTemplateParam("{\"code\":\"" + "12345" + "\"}");
//
//            // 发送短信
//            client.sendSms(sendSmsRequest);
//            return Result.succ("短信发送成功"); // 可以将需要返回的数据放在这里
//
////            return "短信发送成功";
//        } catch (Exception e) {
//            e.printStackTrace();
////            return "短信发送失败: " + e.getMessage();
//            return Result.fail("短信发送失败: " + e.getMessage());
//        }
//    }
//}