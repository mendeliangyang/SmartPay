/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smart.smartpayg.cgbBank;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 *
 * @author Administrator
 */
public class ShiftPayed implements Processor {

    @Override
    public void process(Exchange exchng) throws Exception {
        exchng.getOut().setBody(exchng.getIn().getBody(String.class));
    }

}
