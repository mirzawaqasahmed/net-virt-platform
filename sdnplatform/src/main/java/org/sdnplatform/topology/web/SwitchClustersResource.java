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

package org.sdnplatform.topology.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


import org.openflow.util.HexString;
import org.restlet.data.Form;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.sdnplatform.core.IControllerService;
import org.sdnplatform.core.IOFSwitch;
import org.sdnplatform.topology.ITopologyService;

/**
 * Returns a JSON map of <ClusterId, List<SwitchDpids>>
 */
public class SwitchClustersResource extends ServerResource {
    @Get("json")
    public Map<String, List<String>> retrieve() {
        IControllerService controllerProvider = 
                (IControllerService)getContext().getAttributes().
                    get(IControllerService.class.getCanonicalName());
        ITopologyService topology = 
                (ITopologyService)getContext().getAttributes().
                    get(ITopologyService.class.getCanonicalName());

        Form form = getQuery();
        String queryType = form.getFirstValue("type", true);
        boolean openflowDomain = true;
        if (queryType != null && "l2".equals(queryType)) {
            openflowDomain = false;
        }
        
        Map<String, List<String>> switchClusterMap = new HashMap<String, List<String>>();
        for (Entry<Long, IOFSwitch> entry : controllerProvider.getSwitches().entrySet()) {
            Long clusterDpid = 
                    (openflowDomain
                     ? topology.getOpenflowDomainId(entry.getValue().getId())
                     :topology.getL2DomainId(entry.getValue().getId()));
            List<String> switchesInCluster = switchClusterMap.get(HexString.toHexString(clusterDpid));
            if (switchesInCluster != null) {
                switchesInCluster.add(HexString.toHexString(entry.getKey()));              
            } else {
                List<String> l = new ArrayList<String>();
                l.add(HexString.toHexString(entry.getKey()));
                switchClusterMap.put(HexString.toHexString(clusterDpid), l);
            }
        }
        return switchClusterMap;
    }
}
