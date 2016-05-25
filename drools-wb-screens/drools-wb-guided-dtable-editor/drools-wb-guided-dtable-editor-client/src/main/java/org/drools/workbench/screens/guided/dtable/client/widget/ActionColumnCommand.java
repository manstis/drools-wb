/*
 * Copyright 2011 JBoss Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.drools.workbench.screens.guided.dtable.client.widget;

import org.drools.workbench.models.guided.dtable.shared.model.ActionCol52;

/**
 * Interface defining commands relating to ActionInsert Column operations
 */
public interface ActionColumnCommand {

    /**
     * Causes the Command to perform its encapsulated behaviour.
     * @param column The column on which the command should operate
     */
    public void execute( ActionCol52 column );
}
