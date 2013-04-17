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

package org.sdnplatform.threadpool;

import java.util.concurrent.ScheduledExecutorService;

import org.sdnplatform.core.module.IPlatformService;


public interface IThreadPoolService extends IPlatformService {
    /**
     * Get the master scheduled thread pool executor maintained by the
     * ThreadPool provider.  This can be used by other modules as a centralized
     * way to schedule tasks.
     * @return
     */
    public ScheduledExecutorService getScheduledExecutor();
}
