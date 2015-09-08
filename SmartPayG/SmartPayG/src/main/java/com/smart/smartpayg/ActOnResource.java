/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smart.smartpayg;

import com.smart.common.FormationResult;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;

/**
 * REST Web Service
 *
 * @author Administrator
 */
@Path("ActOn")
public class ActOnResource {

    @Context
    private UriInfo context;
SmartPayAnalyzeParam smartPayAnalyzeParam = new SmartPayAnalyzeParam();
    FormationResult formationResult = new FormationResult();
    /**
     * Creates a new instance of ActOnResource
     */
    public ActOnResource() {
    }

    
}
