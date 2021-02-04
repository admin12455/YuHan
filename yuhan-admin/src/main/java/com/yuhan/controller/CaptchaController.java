package com.yuhan.controller;

import com.google.code.kaptcha.Producer;
import com.yuhan.constant.Constants;
import com.yuhan.core.domain.AjaxResult;
import com.yuhan.core.redis.RedisCache;
import com.yuhan.utils.sign.Base64;
import com.yuhan.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @Author jzx
 * @Date 2021/2/2 14:01
 * @Version 1.0
 */
@RestController
public class CaptchaController {

    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    //验证码类型
    @Value("${yuhan.captchaType}")
    private String captchaType;

    @Autowired
    private RedisCache redisCache;

    @GetMapping("/captchaImage")
    public AjaxResult getCode(HttpServletResponse response) throws IOException {

        String uuid = IdUtils.simpleUUID();
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;
        String capStr = null, code = null;
        BufferedImage image = null;

        if ("math".equals(captchaType)) {
            String capText = captchaProducerMath.createText();// eg: 8*1=?@8
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        } else if ("char".equals(captchaType)) {
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }
        redisCache.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);

        //转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            return AjaxResult.error(e.getMessage());
        }

        AjaxResult ajax = AjaxResult.success();
        ajax.put("uuid",uuid);
        ajax.put("img", Base64.encode(os.toByteArray()));

        return ajax;
    }


}
