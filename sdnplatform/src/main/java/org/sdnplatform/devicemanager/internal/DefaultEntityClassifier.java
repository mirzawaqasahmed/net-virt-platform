/*
 * Copyright (c) 2011,2012,2013 Big Switch Networks, Inc.
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

package org.sdnplatform.devicemanager.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.sdnplatform.core.module.ModuleContext;
import org.sdnplatform.core.module.ModuleException;
import org.sdnplatform.core.module.IModule;
import org.sdnplatform.core.module.IPlatformService;
import org.sdnplatform.devicemanager.IDevice;
import org.sdnplatform.devicemanager.IDeviceService;
import org.sdnplatform.devicemanager.IEntityClass;
import org.sdnplatform.devicemanager.IEntityClassListener;
import org.sdnplatform.devicemanager.IEntityClassifierService;
import org.sdnplatform.devicemanager.IDeviceService.DeviceField;


/**
 * This is a default entity classifier that simply classifies all
 * entities into a fixed entity class, with key fields of MAC and VLAN.
 * @author readams
 */
public class DefaultEntityClassifier implements
        IEntityClassifierService,
        IModule 
{
    /**
     * A default fixed entity class
     */
    protected static class DefaultEntityClass implements IEntityClass {
        String name;

        public DefaultEntityClass(String name) {
            this.name = name;
        }

        @Override
        public EnumSet<IDeviceService.DeviceField> getKeyFields() {
            return keyFields;
        }

        @Override
        public String getName() {
            return name;
        }
    }
    
    protected static EnumSet<DeviceField> keyFields;
    static {
        keyFields = EnumSet.of(DeviceField.MAC, DeviceField.VLAN);
    }
    protected static DefaultEntityClass entityClass =
        new DefaultEntityClass("DefaultEntityClass");

    @Override
    public IEntityClass classifyEntity(Entity entity) {
        return entityClass;
    }

    @Override
    public IEntityClass reclassifyEntity(IDevice curDevice,
                                                     Entity entity) {
        return entityClass;
    }

    @Override
    public void deviceUpdate(IDevice oldDevice, 
                             Collection<? extends IDevice> newDevices) {
        // no-op
    }

    @Override
    public EnumSet<DeviceField> getKeyFields() {
        return keyFields;
    }

    @Override
    public Collection<Class<? extends IPlatformService>> getModuleServices() {
        Collection<Class<? extends IPlatformService>> l = 
                new ArrayList<Class<? extends IPlatformService>>();
        l.add(IEntityClassifierService.class);
        return l;
    }

    @Override
    public Map<Class<? extends IPlatformService>, IPlatformService> getServiceImpls() {
        Map<Class<? extends IPlatformService>,
        IPlatformService> m = 
        new HashMap<Class<? extends IPlatformService>,
                    IPlatformService>();
        // We are the class that implements the service
        m.put(IEntityClassifierService.class, this);
        return m;
    }

    @Override
    public Collection<Class<? extends IPlatformService>>
            getModuleDependencies() {
        // No dependencies
        return null;
    }

    @Override
    public void init(ModuleContext context)
                                                 throws ModuleException {
        // no-op
    }

    @Override
    public void startUp(ModuleContext context) {
        // no-op
    }

    @Override
    public void addListener(IEntityClassListener listener) {
        // no-op
        
    }
}
