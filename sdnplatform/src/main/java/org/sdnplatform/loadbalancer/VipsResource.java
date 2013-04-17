/*
 * Copyright (c) 2013 Big Switch Networks, Inc.
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the
 * "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/legal/epl-v10.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package org.sdnplatform.loadbalancer;

import java.io.IOException;
import java.util.Collection;


import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import org.sdnplatform.packet.IPv4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VipsResource extends ServerResource {
    protected static Logger log = LoggerFactory.getLogger(VipsResource.class);
    
    @Get("json")
    public Collection <LBVip> retrieve() {
        ILoadBalancerService lbs =
                (ILoadBalancerService)getContext().getAttributes().
                    get(ILoadBalancerService.class.getCanonicalName());
        
        String vipId = (String) getRequestAttributes().get("vip");
        if (vipId!=null)
            return lbs.listVip(vipId);
        else        
            return lbs.listVips();               
    }
    
    @Put
    @Post
    public LBVip createVip(String postData) {        

        LBVip vip=null;
        try {
            vip=jsonToVip(postData);
        } catch (IOException e) {
            log.error("Could not parse JSON {}", e.getMessage());
        }
        
        ILoadBalancerService lbs =
                (ILoadBalancerService)getContext().getAttributes().
                    get(ILoadBalancerService.class.getCanonicalName());
        
        String vipId = (String) getRequestAttributes().get("vip");
        if (vipId != null)
            return lbs.updateVip(vip);
        else        
            return lbs.createVip(vip);
    }
    
    @Delete
    public int removeVip() {
        
        String vipId = (String) getRequestAttributes().get("vip");
               
        ILoadBalancerService lbs =
                (ILoadBalancerService)getContext().getAttributes().
                    get(ILoadBalancerService.class.getCanonicalName());

        return lbs.removeVip(vipId);
    }

    protected LBVip jsonToVip(String json) throws IOException {
        
        if (json==null) return null;
        
        MappingJsonFactory f = new MappingJsonFactory();
        JsonParser jp;
        LBVip vip = new LBVip();
        
        try {
            jp = f.createJsonParser(json);
        } catch (JsonParseException e) {
            throw new IOException(e);
        }
        
        jp.nextToken();
        if (jp.getCurrentToken() != JsonToken.START_OBJECT) {
            throw new IOException("Expected START_OBJECT");
        }
        
        while (jp.nextToken() != JsonToken.END_OBJECT) {
            if (jp.getCurrentToken() != JsonToken.FIELD_NAME) {
                throw new IOException("Expected FIELD_NAME");
            }
            
            String n = jp.getCurrentName();
            jp.nextToken();
            if (jp.getText().equals("")) 
                continue;
 
            if (n.equals("id")) {
                vip.id = jp.getText();
                continue;
            } 
            if (n.equals("tenant_id")) {
                vip.tenantId = jp.getText();
                continue;
            } 
            if (n.equals("name")) {
                vip.name = jp.getText();
                continue;
            }
            if (n.equals("network_id")) {
                vip.netId = jp.getText();
                continue;
            }
            if (n.equals("protocol")) {
                String tmp = jp.getText();
                if (tmp.equalsIgnoreCase("TCP")) {
                    vip.protocol = IPv4.PROTOCOL_TCP;
                } else if (tmp.equalsIgnoreCase("UDP")) {
                    vip.protocol = IPv4.PROTOCOL_UDP;
                } else if (tmp.equalsIgnoreCase("ICMP")) {
                    vip.protocol = IPv4.PROTOCOL_ICMP;
                } 
                continue;
            }
            if (n.equals("address")) {
                vip.address = IPv4.toIPv4Address(jp.getText());
                continue;
            }
            if (n.equals("port")) {
                vip.port = Short.parseShort(jp.getText());
                continue;
            }
            if (n.equals("pool_id")) {
                vip.pools.add(jp.getText());                        
                continue;
            }                    
            
            log.warn("Unrecognized field {} in " +
                    "parsing Vips", 
                    jp.getText());
        }
        jp.close();
        
        return vip;
    }
    
}
