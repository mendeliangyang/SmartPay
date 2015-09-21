/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smart.smartpayg.cgbBank;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.apache.camel.ProducerTemplate;

/**
 * REST Web Service
 *
 * @author Administrator
 */
@Path("ShiftPay")
public class ShiftPayResource extends CodeExecution {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ShiftPayResource
     */
    public ShiftPayResource() {
    }

    @POST
    @Path("tt")
    public String SearchOverage(String param) {
        try {
            ProducerTemplate template = getCamelContext().createProducerTemplate();
            Object answer = null;
            Object body = null;
            try {
                answer = template.requestBody("direct:shiftPayDirect", body);
            } finally {
                template.stop();
            }
            return answer == null ? "null" : answer.toString();
        } catch (Exception e) {
            return e.getLocalizedMessage();
        } finally {

        }
    }

}
