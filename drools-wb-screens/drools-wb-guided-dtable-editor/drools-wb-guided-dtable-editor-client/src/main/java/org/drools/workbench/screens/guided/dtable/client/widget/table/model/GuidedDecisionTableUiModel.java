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

package org.drools.workbench.screens.guided.dtable.client.widget.table.model;

import java.util.List;

import org.drools.workbench.screens.guided.dtable.client.widget.table.model.synchronizers.ModelSynchronizer;
import org.uberfire.commons.validation.PortablePreconditions;
import org.uberfire.ext.wires.core.grids.client.model.BaseGridData;
import org.uberfire.ext.wires.core.grids.client.model.IGridCellValue;
import org.uberfire.ext.wires.core.grids.client.model.IGridColumn;
import org.uberfire.ext.wires.core.grids.client.model.IGridRow;

public class GuidedDecisionTableUiModel extends BaseGridData {

    private final ModelSynchronizer synchronizer;

    public GuidedDecisionTableUiModel( final ModelSynchronizer synchronizer ) {
        this.synchronizer = PortablePreconditions.checkNotNull( "synchronizer",
                                                                synchronizer );
        setMerged( false );
    }

    @Override
    //Override to sync underlying Model with UiModel
    public Range setCell( final int rowIndex,
                          final int columnIndex,
                          final IGridCellValue<?> value ) {
        final Range range = super.setCell( rowIndex,
                                           columnIndex,
                                           value );
        synchronizer.setCell( range,
                              columnIndex,
                              value );
        return range;
    }

    @Override
    //Override to sync underlying Model with UiModel
    public Range deleteCell( final int rowIndex,
                             final int columnIndex ) {
        final Range range = super.deleteCell( rowIndex,
                                              columnIndex );
        synchronizer.deleteCell( range,
                                 columnIndex );
        return range;
    }

    @Override
    public void moveColumnsTo( final int index,
                               final List<IGridColumn<?>> columns ) {
        try {
            synchronizer.moveColumnsTo( index,
                                        columns );
            super.moveColumnsTo( index,
                                 columns );
        } catch ( ModelSynchronizer.MoveColumnVetoException ignore ) {
            //Do nothing. The move has been vetoed.
        }
    }

    @Override
    public void moveRowsTo( final int index,
                            final List<IGridRow> rows ) {
        try {
            synchronizer.moveRowsTo( index,
                                     rows );
            super.moveRowsTo( index,
                              rows );
            synchronizer.updateSystemControlledColumnValues();

        } catch ( ModelSynchronizer.MoveColumnVetoException ignore ) {
            //Do nothing. The move has been vetoed.
        }
    }

    public void indexColumn( final int columnIndex ) {
        if ( isMerged() ) {
            indexManager.indexColumn( columnIndex );
        }
    }

    public Range setCellInternal( final int rowIndex,
                                  final int columnIndex,
                                  final IGridCellValue<?> value ) {
        final boolean isMerged = isMerged();
        try {
            this.isMerged = false;
            return super.setCell( rowIndex,
                                  columnIndex,
                                  value );

        } finally {
            this.isMerged = isMerged;
        }
    }

    public Range deleteCellInternal( final int rowIndex,
                                     final int columnIndex ) {
        final boolean isMerged = isMerged();
        try {
            this.isMerged = false;
            return super.deleteCell( rowIndex,
                                     columnIndex );

        } finally {
            this.isMerged = isMerged;
        }
    }

}
