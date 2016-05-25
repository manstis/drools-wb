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

package org.drools.workbench.screens.guided.dtable.client.widget.table.model.synchronizers.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.enterprise.context.Dependent;

import org.drools.workbench.models.datamodel.oracle.DataType;
import org.drools.workbench.models.guided.dtable.shared.model.BaseColumn;
import org.drools.workbench.models.guided.dtable.shared.model.BaseColumnFieldDiff;
import org.drools.workbench.models.guided.dtable.shared.model.DTCellValue52;
import org.drools.workbench.models.guided.dtable.shared.model.LimitedEntryCol;
import org.drools.workbench.models.guided.dtable.shared.model.RowNumberCol52;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.synchronizers.ModelSynchronizer;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.synchronizers.Synchronizer;
import org.uberfire.ext.wires.core.grids.client.model.BaseGridRow;
import org.uberfire.ext.wires.core.grids.client.model.IGridData;
import org.uberfire.ext.wires.core.grids.client.model.IGridRow;
import org.uberfire.ext.wires.core.grids.client.widget.selections.RowSelectionManager;

import static org.drools.workbench.screens.guided.dtable.client.widget.table.model.synchronizers.impl.RowSynchronizer.*;

@Dependent
public class RowSynchronizer extends BaseSynchronizer<RowMetaData, RowMetaData, RowMetaData> {

    public interface RowMetaData extends Synchronizer.MetaData {

        int getRowIndex();

    }

    public static class RowMetaDataImpl implements RowMetaData {

        private final int rowIndex;

        public RowMetaDataImpl() {
            this( -1 );
        }

        public RowMetaDataImpl( final int rowIndex ) {
            this.rowIndex = rowIndex;
        }

        @Override
        public int getRowIndex() {
            return rowIndex;
        }

    }

    @Override
    public boolean handlesAppend( final MetaData metaData ) {
        return metaData instanceof RowMetaData;
    }

    @Override
    public void append( final RowMetaData metaData ) {
        if ( !handlesAppend( metaData ) ) {
            return;
        }
        final List<DTCellValue52> modelRow = new ArrayList<DTCellValue52>();
        model.getData().add( modelRow );

        final IGridRow uiModelRow = new BaseGridRow();
        uiModel.appendRow( uiModelRow );

        final int rowIndex = uiModel.getRowCount() - 1;
        initialiseRowData( rowIndex );
    }

    @Override
    public boolean handlesInsert( final MetaData metaData ) throws ModelSynchronizer.MoveColumnVetoException {
        return metaData instanceof RowMetaData;
    }

    @Override
    public void insert( final RowMetaData metaData ) throws ModelSynchronizer.MoveColumnVetoException {
        if ( !handlesAppend( metaData ) ) {
            return;
        }
        final int rowIndex = metaData.getRowIndex();
        final List<DTCellValue52> modelRow = new ArrayList<DTCellValue52>();
        model.getData().add( rowIndex,
                             modelRow );

        final IGridRow uiModelRow = new BaseGridRow();
        uiModel.insertRow( rowIndex,
                           uiModelRow );

        initialiseRowData( rowIndex );
    }

    private void initialiseRowData( final int rowIndex ) {
        final List<BaseColumn> modelColumns = model.getExpandedColumns();
        final List<DTCellValue52> modelRow = model.getData().get( rowIndex );
        for ( int columnIndex = 0; columnIndex < modelColumns.size(); columnIndex++ ) {
            final BaseColumn modelColumn = modelColumns.get( columnIndex );
            final DTCellValue52 modelCell = makeModelCellValue( modelColumn );
            modelRow.add( modelCell );

            //BaseGridData is sparsely populated; only add values if needed.
            if ( modelCell.hasValue() ) {
                uiModel.setCellInternal( rowIndex,
                                         columnIndex,
                                         gridWidgetCellFactory.convertCell( modelCell,
                                                                            modelColumn,
                                                                            cellUtilities,
                                                                            columnUtilities ) );
            }
            uiModel.indexColumn( columnIndex );

            //Set-up SelectionManager for Row Number column, to select entire row.
            if ( modelColumn instanceof RowNumberCol52 ) {
                uiModel.getCell( rowIndex,
                                 columnIndex ).setSelectionManager( RowSelectionManager.INSTANCE );
            }
        }
    }

    private DTCellValue52 makeModelCellValue( final BaseColumn modelColumn ) {
        DTCellValue52 dcv;
        if ( modelColumn instanceof LimitedEntryCol ) {
            dcv = new DTCellValue52( Boolean.FALSE );
        } else {
            dcv = new DTCellValue52( modelColumn.getDefaultValue() );
        }
        final DataType.DataTypes dataType = columnUtilities.getDataType( modelColumn );
        cellUtilities.assertDTCellValue( dataType,
                                         dcv );
        return dcv;
    }

    @Override
    public boolean handlesUpdate( final MetaData metaData ) {
        //We don't support updating a row at present, but we could; e.g. clear all values etc
        return false;
    }

    @Override
    public List<BaseColumnFieldDiff> update( final RowMetaData originalMetaData,
                                             final RowMetaData editedMetaData ) {
        //We don't support updating a row at present, but we could; e.g. clear all values etc
        return Collections.emptyList();
    }

    @Override
    public boolean handlesDelete( final MetaData metaData ) {
        return metaData instanceof RowMetaData;
    }

    @Override
    public void delete( final RowMetaData metaData ) {
        if ( !handlesDelete( metaData ) ) {
            return;
        }
        final int rowIndex = metaData.getRowIndex();
        final IGridData.Range rowRange = uiModel.deleteRow( rowIndex );
        final int minRowIndex = rowRange.getMinRowIndex();
        final int maxRowIndex = rowRange.getMaxRowIndex();
        for ( int ri = minRowIndex; ri <= maxRowIndex; ri++ ) {
            model.getData().remove( minRowIndex );
        }
    }

    @Override
    public boolean handlesMoveColumnsTo( final List<? extends MetaData> metaData ) throws ModelSynchronizer.MoveColumnVetoException {
        //Moving Row data is delegated to each respective Column Synchronizer
        return false;
    }

    @Override
    public void moveColumnsTo( final List<MoveColumnToMetaData> metaData ) throws ModelSynchronizer.MoveColumnVetoException {
        //Moving Row data is delegated to each respective Column Synchronizer
    }

    @Override
    public boolean handlesMoveRowsTo( final List<? extends MetaData> metaData ) throws ModelSynchronizer.MoveColumnVetoException {
        for ( MetaData md : metaData ) {
            if ( !( md instanceof MoveRowToMetaData ) ) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void moveRowsTo( final List<MoveRowToMetaData> metaData ) throws ModelSynchronizer.MoveColumnVetoException {
        //Check operation is supported
        if ( !handlesMoveRowsTo( metaData ) ) {
            return;
        }

        for ( int idx = 0; idx < metaData.size(); idx++ ) {
            final MoveRowToMetaData md = metaData.get( idx );
            final int sourceRowIndex = md.getSourceRowIndex();
            final int targetRowIndex = md.getTargetRowIndex();
            final List<DTCellValue52> row = md.getRow();

            if ( targetRowIndex < sourceRowIndex ) {
                model.getData().remove( sourceRowIndex );
                model.getData().add( targetRowIndex,
                                     row );

            } else if ( targetRowIndex > sourceRowIndex ) {
                model.getData().remove( sourceRowIndex - idx );
                model.getData().add( targetRowIndex - idx,
                                     row );
            }
        }
    }

}
