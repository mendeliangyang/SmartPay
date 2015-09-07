/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smart.smartpay.services;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * REST Web Service
 *
 * @author Administrator
 */
@Path("sprs")
public class SmartPayRS {

    @Context
    private UriInfo context;

    public SmartPayRS() {
    }

    @GET
    @Path("hello")
    public String hello() throws Exception {
         
        return "only test "; //NetHelper.test();
    }

}
