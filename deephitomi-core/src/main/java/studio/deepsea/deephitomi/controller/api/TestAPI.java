package studio.deepsea.deephitomi.controller.api;

import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Reyton on 2017/7/10.
 */
@RestController
public class TestAPI {

    @Scope("prototype")
    @RequestMapping(value = "/api/v1/test/{status}", method = RequestMethod.POST)
    public ResponseEntity<Void> test(@PathVariable("status") String status,UriComponentsBuilder ucBuilder) {
        System.out.println("测试API的POST可用性，状态： " + status);

        if ("test".equals(status)) {
            System.out.println("现在的状态为："+status);
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/user/{id}").buildAndExpand(status).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @Scope("prototype")
    @RequestMapping(value = "/api/v1/test/", method = RequestMethod.GET, produces="text/plain;charset=UTF-8")
    public ResponseEntity<String> test1(HttpServletResponse response, HttpServletRequest request) {
        System.out.println("成功收到GET请求！");
        return new ResponseEntity<String>("GET请求发送成功，响应OK", HttpStatus.OK);
    }

}