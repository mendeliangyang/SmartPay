/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smart.smartpayg.cgbBank;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.springframework.stereotype.Service;
//import org.apache.camel.Service;

/**
 *
 * @author Administrator
 */
@Service
public class CodeExecution implements CamelContextAware {

    protected static CamelContext camelContext;

    @Override
    public void setCamelContext(CamelContext cc) {
        camelContext = cc;
    }

    @Override
    public CamelContext getCamelContext() {
        return camelContext;
    }

}
