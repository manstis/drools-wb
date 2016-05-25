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
import java.util.List;

import com.google.gwt.event.shared.EventBus;
import org.drools.workbench.models.guided.dtable.shared.model.BRLRuleModel;
import org.drools.workbench.models.guided.dtable.shared.model.BaseColumn;
import org.drools.workbench.models.guided.dtable.shared.model.DTCellValue52;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTablePresenter;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.GuidedDecisionTableUiModel;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.converters.cell.GridWidgetCellFactory;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.converters.column.GridWidgetColumnFactory;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.synchronizers.ModelSynchronizer;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.synchronizers.Synchronizer;
import org.drools.workbench.screens.guided.dtable.client.widget.table.utilities.CellUtilities;
import org.drools.workbench.screens.guided.dtable.client.widget.table.utilities.ColumnUtilities;

import static org.uberfire.commons.validation.PortablePreconditions.*;

public abstract class BaseSynchronizer<A extends Synchronizer.MetaData, U extends Synchronizer.MetaData, D extends Synchronizer.MetaData> implements Synchronizer<A, U, D, BaseSynchronizer.MoveColumnToMetaData, BaseSynchronizer.MoveRowToMetaData> {

    protected GuidedDecisionTable52 model;
    protected GuidedDecisionTableUiModel uiModel;
    protected CellUtilities cellUtilities;
    protected ColumnUtilities columnUtilities;
    protected GridWidgetCellFactory gridWidgetCellFactory;
    protected GridWidgetColumnFactory gridWidgetColumnFactory;
    protected GuidedDecisionTableView view;
    protected BRLRuleModel rm;
    protected EventBus eventBus;
    protected GuidedDecisionTablePresenter.Access access;

    public interface MoveColumnToMetaData<C extends BaseColumn> extends Synchronizer.MetaData {

        int getSourceColumnIndex();

        int getTargetColumnIndex();

        C getColumn();

    }

    public static class MoveColumnToMetaDataImpl<C extends BaseColumn> implements MoveColumnToMetaData<C> {

        private final int sourceColumnIndex;
        private final int targetColumnIndex;
        private final C column;

        public MoveColumnToMetaDataImpl( final int sourceColumnIndex,
                                         final int targetColumnIndex,
                                         final C column ) {
            this.sourceColumnIndex = sourceColumnIndex;
            this.targetColumnIndex = targetColumnIndex;
            this.column = checkNotNull( "column",
                                        column );
        }

        @Override
        public int getSourceColumnIndex() {
            return sourceColumnIndex;
        }

        @Override
        public int getTargetColumnIndex() {
            return targetColumnIndex;
        }

        @Override
        public C getColumn() {
            return column;
        }

    }

    public interface MoveRowToMetaData extends Synchronizer.MetaData {

        int getSourceRowIndex();

        int getTargetRowIndex();

        List<DTCellValue52> getRow();

    }

    public static class MoveRowToMetaDataImpl implements MoveRowToMetaData {

        private final int sourceRowIndex;
        private final int targetRowIndex;
        private final List<DTCellValue52> row;

        public MoveRowToMetaDataImpl( final int sourceRowIndex,
                                      final int targetRowIndex,
                                      final List<DTCellValue52> row ) {
            this.sourceRowIndex = sourceRowIndex;
            this.targetRowIndex = targetRowIndex;
            this.row = checkNotNull( "row",
                                     row );
        }

        @Override
        public int getSourceRowIndex() {
            return sourceRowIndex;
        }

        @Override
        public int getTargetRowIndex() {
            return targetRowIndex;
        }

        @Override
        public List<DTCellValue52> getRow() {
            return row;
        }

    }

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public void initialise( final GuidedDecisionTable52 model,
                            final GuidedDecisionTableUiModel uiModel,
                            final CellUtilities cellUtilities,
                            final ColumnUtilities columnUtilities,
                            final GridWidgetCellFactory gridWidgetCellFactory,
                            final GridWidgetColumnFactory gridWidgetColumnFactory,
                            final GuidedDecisionTableView view,
                            final BRLRuleModel rm,
                            final EventBus eventBus,
                            final GuidedDecisionTablePresenter.Access access ) {
        this.model = checkNotNull( "model",
                                   model );
        this.uiModel = checkNotNull( "uiModel",
                                     uiModel );
        this.cellUtilities = checkNotNull( "cellUtilities",
                                           cellUtilities );
        this.columnUtilities = checkNotNull( "columnUtilities",
                                             columnUtilities );
        this.gridWidgetCellFactory = checkNotNull( "gridWidgetCellFactory",
                                                   gridWidgetCellFactory );
        this.gridWidgetColumnFactory = checkNotNull( "gridWidgetColumnFactory",
                                                     gridWidgetColumnFactory );
        this.view = checkNotNull( "view",
                                  view );
        this.rm = checkNotNull( "rm",
                                rm );
        this.eventBus = checkNotNull( "eventBus",
                                      eventBus );
        this.access = checkNotNull( "access",
                                    access );
    }

    @Override
    public boolean handlesMoveRowsTo( final List<? extends MetaData> metaData ) throws ModelSynchronizer.MoveColumnVetoException {
        return false;
    }

    @Override
    public void moveRowsTo( final List<MoveRowToMetaData> metaData ) throws ModelSynchronizer.MoveColumnVetoException {
        throw new ModelSynchronizer.MoveColumnVetoException();
    }

    protected void moveModelData( final int tgtColumnIndex,
                                  final int srcColumnFirstIndex,
                                  final int srcColumnLastIndex ) {
        if ( tgtColumnIndex == srcColumnFirstIndex ) {
            return;
        }
        if ( srcColumnFirstIndex > srcColumnLastIndex ) {
            return;
        }

        for ( List<DTCellValue52> modelRow : model.getData() ) {
            final List<DTCellValue52> dataToMove = new ArrayList<DTCellValue52>();
            dataToMove.addAll( modelRow.subList( srcColumnFirstIndex,
                                                 srcColumnLastIndex + 1 ) );

            final int srcColumnCount = srcColumnLastIndex - srcColumnFirstIndex + 1;
            for ( int iCol = 0; iCol < srcColumnCount; iCol++ ) {
                modelRow.remove( srcColumnFirstIndex );
            }

            //Moving left
            if ( tgtColumnIndex < srcColumnFirstIndex ) {
                modelRow.addAll( tgtColumnIndex,
                                 dataToMove );
            }

            //Moving right
            if ( tgtColumnIndex > srcColumnFirstIndex ) {
                modelRow.addAll( tgtColumnIndex - ( srcColumnLastIndex - srcColumnFirstIndex ),
                                 dataToMove );
            }
        }
    }

}
