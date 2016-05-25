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
package org.drools.workbench.screens.guided.dtable.client.widget.table.model.converters.cell;

import org.drools.workbench.models.guided.dtable.shared.model.BaseColumn;
import org.drools.workbench.models.guided.dtable.shared.model.DTCellValue52;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.GuidedDecisionTableUiCell;
import org.drools.workbench.screens.guided.dtable.client.widget.table.utilities.CellUtilities;
import org.drools.workbench.screens.guided.dtable.client.widget.table.utilities.ColumnUtilities;

/**
 * Factory definition for GridWidget cells
 */
public interface GridWidgetCellFactory {

    /**
     * Instantiate a Cell for use within a GridWidget
     * @param cell The underlying GuidedDecisionTable52 model cell
     * @param column The underlying GuidedDecisionTable52 column for which the cell relates
     * @param cellUtilities Utilities to support identification of the column's data-type
     * @param columnUtilities Utilities to support identification of the column's data-type
     * @return
     */
    GuidedDecisionTableUiCell convertCell( final DTCellValue52 cell,
                                           final BaseColumn column,
                                           final CellUtilities cellUtilities,
                                           final ColumnUtilities columnUtilities );

}
