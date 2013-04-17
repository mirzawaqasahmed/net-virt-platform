/*
 * Copyright (c) 2011,2013 Big Switch Networks, Inc.
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
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 *    Originally created by David Erickson, Stanford University 
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the
 *    License. You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing,
 *    software distributed under the License is distributed on an "AS
 *    IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 *    express or implied. See the License for the specific language
 *    governing permissions and limitations under the License. 
 */

package org.sdnplatform.core.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.openflow.util.HexString;
import org.restlet.resource.Get;
import org.sdnplatform.core.IControllerService;
import org.sdnplatform.counter.ICounter;
import org.sdnplatform.counter.ICounterStoreService;


/**
 * Get counters for a particular switch 
 * @author readams
 */
public class SwitchCounterResource extends CounterResourceBase {
    @Get("json")
    public Map<String, Object> retrieve() {
        IControllerService controllerProvider = 
                (IControllerService)getContext().getAttributes().
                    get(IControllerService.class.getCanonicalName());
        HashMap<String,Object> model = new HashMap<String,Object>();
        
        String switchID = (String) getRequestAttributes().get("switchId");
        String counterName = (String) getRequestAttributes().get("counterName");

        Long[] switchDpids;
        if (switchID.equalsIgnoreCase("all")) {
            switchDpids = controllerProvider.getSwitches().keySet().toArray(new Long[0]);
            getOneSwitchCounterJson(model, ICounterStoreService.CONTROLLER_NAME, counterName);
            for (Long dpid : switchDpids) {
                switchID = HexString.toHexString(dpid);

                getOneSwitchCounterJson(model, switchID, counterName);
            }
        } else {
            getOneSwitchCounterJson(model, switchID, counterName);
        }
        return model;
    }
    
    protected void getOneSwitchCounterJson(Map<String, Object> model, 
                                           String switchID, String counterName) {
        String fullCounterName = "";      
        
        try {
            counterName = URLDecoder.decode(counterName, "UTF-8");
            fullCounterName = 
                switchID + ICounterStoreService.TitleDelimitor + counterName;
        } catch (UnsupportedEncodingException e) {
            //Just leave counterTitle undecoded if there is an issue - fail silently
        }

        ICounter counter = this.counterStore.getCounter(fullCounterName);
        Map<String, Long> sample = new HashMap<String, Long> ();
        if (counter != null) {
            sample.put(counter.getCounterDate().toString(), 
                       counter.getCounterValue().getLong());
            model.put(switchID, sample);
        }
    }
    
}
