/*
 * Copyright 2012 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.workbench.screens.guided.dtable.client.widget.table.model.synchronizers;

import java.util.List;

import com.google.gwt.event.shared.EventBus;
import org.drools.workbench.models.guided.dtable.shared.model.BRLRuleModel;
import org.drools.workbench.models.guided.dtable.shared.model.BaseColumn;
import org.drools.workbench.models.guided.dtable.shared.model.BaseColumnFieldDiff;
import org.drools.workbench.models.guided.dtable.shared.model.ConditionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.models.guided.dtable.shared.model.Pattern52;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTablePresenter;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.GuidedDecisionTableUiModel;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.converters.cell.GridWidgetCellFactory;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.converters.column.GridWidgetColumnFactory;
import org.drools.workbench.screens.guided.dtable.client.widget.table.utilities.CellUtilities;
import org.drools.workbench.screens.guided.dtable.client.widget.table.utilities.ColumnUtilities;
import org.drools.workbench.screens.guided.dtable.client.widget.table.utilities.DependentEnumsUtilities;
import org.uberfire.ext.wires.core.grids.client.model.IGridCellValue;
import org.uberfire.ext.wires.core.grids.client.model.IGridColumn;
import org.uberfire.ext.wires.core.grids.client.model.IGridRow;

import static org.uberfire.ext.wires.core.grids.client.model.IGridData.*;

/**
 * Handles synchronization of Model and UI-Model
 */
public interface ModelSynchronizer {

    class MoveColumnVetoException extends Exception {

    }

    void setSynchronizers( final List<Synchronizer<? extends Synchronizer.MetaData, ? extends Synchronizer.MetaData, ? extends Synchronizer.MetaData, ? extends Synchronizer.MetaData, ? extends Synchronizer.MetaData>> synchronizers );

    void initialise( final GuidedDecisionTable52 model,
                     final GuidedDecisionTableUiModel uiModel,
                     final CellUtilities cellUtilities,
                     final ColumnUtilities columnUtilities,
                     final DependentEnumsUtilities dependentEnumsUtilities,
                     final GridWidgetCellFactory gridWidgetCellFactory,
                     final GridWidgetColumnFactory gridWidgetColumnFactory,
                     final GuidedDecisionTableView view,
                     final BRLRuleModel rm,
                     final EventBus eventBus,
                     final GuidedDecisionTablePresenter.Access access );

    void setCell( final Range rowRange,
                  final int columnIndex,
                  final IGridCellValue<?> value );

    void deleteCell( final Range rowRange,
                     final int columnIndex );

    void appendColumn( final BaseColumn column ) throws MoveColumnVetoException;

    void appendColumn( final Pattern52 pattern,
                       final ConditionCol52 column ) throws MoveColumnVetoException;

    void deleteColumn( final BaseColumn column ) throws MoveColumnVetoException;

    List<BaseColumnFieldDiff> updateColumn( final Pattern52 originalPattern,
                                            final ConditionCol52 originalColumn,
                                            final Pattern52 editedPattern,
                                            final ConditionCol52 editedColumn ) throws MoveColumnVetoException;

    List<BaseColumnFieldDiff> updateColumn( final BaseColumn originalColumn,
                                            final BaseColumn editedColumn ) throws MoveColumnVetoException;

    void moveColumnTo( final int targetColumnIndex,
                       final IGridColumn<?> column ) throws MoveColumnVetoException;

    void moveColumnsTo( final int targetColumnIndex,
                        final List<IGridColumn<?>> columns ) throws MoveColumnVetoException;

    void moveRowsTo( final int targetRowIndex,
                     final List<IGridRow> rows ) throws MoveColumnVetoException;

    void appendRow() throws MoveColumnVetoException;

    void insertRow( final int rowIndex ) throws MoveColumnVetoException;

    void deleteRow( final int rowIndex ) throws MoveColumnVetoException;

    void updateSystemControlledColumnValues();

    void setCellOtherwiseState( final int rowIndex,
                                final int columnIndex );

}
