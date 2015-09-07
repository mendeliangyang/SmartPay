/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smart.smartpayg;

import com.smart.common.FormationResult;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;

/**
 * REST Web Service
 *
 * @author Administrator
 */
@Path("MyAccount")
public class MyAccountResource {

    @Context
    private UriInfo context;
    
    SmartPayAnalyzeParam smartPayAnalyzeParam = new SmartPayAnalyzeParam();
    FormationResult formationResult = new FormationResult();

    /**
     * Creates a new instance of MyAccountResource
     */
    public MyAccountResource() {
    }

}
