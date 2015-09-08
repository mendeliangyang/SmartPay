/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smart.smartpayg;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author Administrator
 */
@javax.ws.rs.ApplicationPath("/sp")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method. It is automatically
     * populated with all resources defined in the project. If required, comment
     * out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.smart.common.webService.RESTCorsDemoRequestFilter.class);
        resources.add(com.smart.common.webService.RESTCorsDemoResponseFilter.class);
        resources.add(com.smart.smartpayg.AccountBindResource.class);
        resources.add(com.smart.smartpayg.ActOnResource.class);
        resources.add(com.smart.smartpayg.AuthResource.class);
        resources.add(com.smart.smartpayg.MyAccountResource.class);
    }

}
