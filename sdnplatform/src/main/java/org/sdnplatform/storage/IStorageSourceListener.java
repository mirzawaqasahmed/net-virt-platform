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

package org.sdnplatform.storage;

import java.util.Set;

public interface IStorageSourceListener {

    /**
     * Called when rows are inserted or updated in the table.
     * 
     * @param tableName The table where the rows were inserted
     * @param rowKeys The keys of the rows that were inserted
     */
    public void rowsModified(String tableName, Set<Object> rowKeys);
    
    /**
     * Called when a new row is deleted from the table.
     * 
     * @param tableName The table where the rows were deleted
     * @param rowKeys The keys of the rows that were deleted
     */
    public void rowsDeleted(String tableName, Set<Object> rowKeys);
}
