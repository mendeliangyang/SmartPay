/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smart.smartpayg.TreeWorldProcess;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 *
 * @author Administrator
 */
public class SearchOverageProcess implements Processor {

    @Override
    public void process(Exchange exchng) throws Exception {
        String str = exchng.getIn().getBody(String.class);//获取post ：raw
//        exchng.getOut().removeHeaders("CamelHttp*"); //在process中也可以去掉 camle路由头信息
        exchng.getOut().setHeader(Exchange.HTTP_METHOD, "POST"); //指定请求的方式
        exchng.getOut().setBody(str);//camle 在路由时去掉了body参数，所以需要手动添加
    }

}
