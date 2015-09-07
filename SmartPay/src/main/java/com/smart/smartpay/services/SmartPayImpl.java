/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smart.smartpay.services;

import javax.ws.rs.core.Response;

/**
 *
 * @author Administrator
 */
public class SmartPayImpl implements SmartPayInterface {

    @Override
    public Response getHello(String param) {
        return Response.ok("HW").build();
    }

}
