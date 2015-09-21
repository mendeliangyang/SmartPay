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
public class ShiftPay implements Processor {

    @Override
    public void process(Exchange exchng) throws Exception {
        System.out.println(exchng.getOut());

        exchng.getOut().setBody("0228V0016000051380009999999920150902090458000387021380706214621621000013279.07138544FFFF................................................................................0000000003850502....210......................13823191729FFFFFFFF");
    }

}
