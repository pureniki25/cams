package com.hongte.alms.service.test;


import com.alibaba.fastjson.JSON;
import com.hongte.alms.common.open.OpenRequestContent;
import com.hongte.alms.common.open.OpenSecureUtil;
import com.hongte.alms.open.config.OpenServiceConfig;
import com.hongte.alms.open.vo.User;
import feign.Feign;
import feign.gson.GsonDecoder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;


import java.net.URLEncoder;
import java.util.UUID;

/**
 * @author dengzhiming
 * @date 2018/3/3 17:01
 */


@RunWith(SpringJUnit4ClassRunner.class)
public class CarOrderControllerTest {

//    @Autowired
//    private OpenServiceConfig openServiceConfig;
    @Test
    public void syncUser()
    {
        User user=new User();
        user.setName("xxxx");
        user.setAge(12);
        try {
            String postData = encryptPostData(user);
            /* http://local-test-api.yinliancn.com是信贷接口域名，这里本机配置的，需要配置成自己的*/
            XindaiApiService xindaiService = Feign.builder()
                    .decoder(new GsonDecoder())
                    .target(XindaiApiService.class, "http://local-test-api.yinliancn.com");
            ApiResult result=xindaiService.syncCarUser(postData);
            System.out.print(result.getMessage()+" "+result.getStatus());
            Assert.assertEquals("1",result.getStatus());
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private String encryptPostData(Object obj) throws Exception
    {
        //这里secret暂时写固定
        String secret = "67902e2fd52504f3859065f8c58d654b";
        String objText = JSON.toJSONString(obj);
        String encryptedObjText = OpenSecureUtil.tripleDESEncrypt(secret,objText);
        String encodedText = URLEncoder.encode(encryptedObjText);
        OpenRequestContent requestPackage = new OpenRequestContent();
        requestPackage.setKey("YInLLGcrVnv");
        requestPackage.setStrJson(encodedText);
        return JSON.toJSONString(requestPackage);
    }


}