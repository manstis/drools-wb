/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
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

package org.drools.workbench.screens.guided.dtable.client.editor.menu;

import java.util.ArrayList;

import org.drools.workbench.models.datamodel.oracle.DataType;
import org.drools.workbench.models.guided.dtable.shared.model.ConditionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.DTCellValue52;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.models.guided.dtable.shared.model.Pattern52;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableModellerView;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTablePresenter;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.dtable.client.widget.table.events.cdi.DecisionTableSelectedEvent;
import org.drools.workbench.screens.guided.dtable.client.widget.table.events.cdi.DecisionTableSelectionsChangedEvent;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.GuidedDecisionTableUiModel;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.synchronizers.ModelSynchronizer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.uberfire.ext.wires.core.grids.client.model.BaseGridColumn;
import org.uberfire.ext.wires.core.grids.client.model.BaseGridRow;
import org.uberfire.ext.wires.core.grids.client.model.IGridColumn;
import org.uberfire.ext.wires.core.grids.client.widget.renderers.columns.IGridColumnRenderer;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class InsertMenuBuilderTest {

    private InsertMenuBuilder builder;
    private GuidedDecisionTable52 model;
    private GuidedDecisionTableUiModel uiModel;

    @Mock
    private InsertMenuView view;

    @Mock
    private GuidedDecisionTableView.Presenter dtPresenter;
    private GuidedDecisionTablePresenter.Access access = new GuidedDecisionTablePresenter.Access();

    @Mock
    private GuidedDecisionTableView dtPresenterView;

    @Mock
    private GuidedDecisionTableModellerView.Presenter modeller;

    @Before
    @SuppressWarnings("unchecked")
    public void setup() {
        model = new GuidedDecisionTable52();
        uiModel = new GuidedDecisionTableUiModel( mock( ModelSynchronizer.class ) );

        when( dtPresenter.getView() ).thenReturn( dtPresenterView );
        when( dtPresenter.getModel() ).thenReturn( model );
        when( dtPresenter.getAccess() ).thenReturn( access );
        when( dtPresenterView.getModel() ).thenReturn( uiModel );

        uiModel.appendColumn( new BaseGridColumn<String>( mock( IGridColumn.HeaderMetaData.class ),
                                                          mock( IGridColumnRenderer.class ),
                                                          100 ) );
        uiModel.appendColumn( new BaseGridColumn<String>( mock( IGridColumn.HeaderMetaData.class ),
                                                          mock( IGridColumnRenderer.class ),
                                                          100 ) );
        uiModel.appendColumn( new BaseGridColumn<String>( mock( IGridColumn.HeaderMetaData.class ),
                                                          mock( IGridColumnRenderer.class ),
                                                          100 ) );
        uiModel.appendRow( new BaseGridRow() );
        uiModel.appendRow( new BaseGridRow() );

        builder = new InsertMenuBuilder( view );
        builder.setup();
        builder.setModeller( modeller );
    }

    @Test
    public void testOnDecisionTableSelectedEventWithNoSelections() {
        builder.onDecisionTableSelectedEvent( new DecisionTableSelectedEvent( dtPresenter ) );

        verify( view,
                times( 1 ) ).enableAppendRowMenuItem( eq( true ) );
        verify( view,
                times( 1 ) ).enableInsertRowAboveMenuItem( eq( false ) );
        verify( view,
                times( 1 ) ).enableInsertRowBelowMenuItem( eq( false ) );
        verify( view,
                times( 1 ) ).enableAppendColumnMenuItem( eq( true ) );
    }

    @Test
    public void testOnDecisionTableSelectedEventWithSingleRowSelected() {
        model.getConditions().add( new Pattern52() {{
            setFactType( "Fact" );
            getChildColumns().add( new ConditionCol52() {{
                setFactType( "Fact" );
                setFactField( "field1" );
                setFieldType( DataType.TYPE_STRING );
                setOperator( "==" );
            }} );
        }} );
        model.getData().add( new ArrayList<DTCellValue52>() {{
            add( new DTCellValue52( 1 ) );
            add( new DTCellValue52( "descr" ) );
            add( new DTCellValue52( "md" ) );
        }} );

        uiModel.selectCell( 0,
                            2 );

        builder.onDecisionTableSelectedEvent( new DecisionTableSelectedEvent( dtPresenter ) );

        verify( view,
                times( 1 ) ).enableAppendRowMenuItem( eq( true ) );
        verify( view,
                times( 1 ) ).enableInsertRowAboveMenuItem( eq( true ) );
        verify( view,
                times( 1 ) ).enableInsertRowBelowMenuItem( eq( true ) );
        verify( view,
                times( 1 ) ).enableAppendColumnMenuItem( eq( true ) );
    }

    @Test
    public void testOnDecisionTableSelectedEventWithMultipleRowsSelected() {
        model.getConditions().add( new Pattern52() {{
            setFactType( "Fact" );
            getChildColumns().add( new ConditionCol52() {{
                setFactType( "Fact" );
                setFactField( "field1" );
                setFieldType( DataType.TYPE_STRING );
                setOperator( "==" );
            }} );
        }} );
        model.getData().add( new ArrayList<DTCellValue52>() {{
            add( new DTCellValue52( 1 ) );
            add( new DTCellValue52( "descr" ) );
            add( new DTCellValue52( "md" ) );
        }} );
        model.getData().add( new ArrayList<DTCellValue52>() {{
            add( new DTCellValue52( 1 ) );
            add( new DTCellValue52( "descr" ) );
            add( new DTCellValue52( "md" ) );
        }} );

        uiModel.selectCells( 0,
                             2,
                             1,
                             2 );

        builder.onDecisionTableSelectedEvent( new DecisionTableSelectedEvent( dtPresenter ) );

        verify( view,
                times( 1 ) ).enableAppendRowMenuItem( eq( true ) );
        verify( view,
                times( 1 ) ).enableInsertRowAboveMenuItem( eq( false ) );
        verify( view,
                times( 1 ) ).enableInsertRowBelowMenuItem( eq( false ) );
        verify( view,
                times( 1 ) ).enableAppendColumnMenuItem( eq( true ) );
    }

    @Test
    public void testOnDecisionTableSelectionsChangedEventWithNoSelections() {
        builder.onDecisionTableSelectionsChangedEvent( new DecisionTableSelectionsChangedEvent( dtPresenter ) );

        verify( view,
                times( 1 ) ).enableAppendRowMenuItem( eq( true ) );
        verify( view,
                times( 1 ) ).enableInsertRowAboveMenuItem( eq( false ) );
        verify( view,
                times( 1 ) ).enableInsertRowBelowMenuItem( eq( false ) );
        verify( view,
                times( 1 ) ).enableAppendColumnMenuItem( eq( true ) );
    }

    @Test
    public void testOnDecisionTableSelectionsChangedEventWithSingleRowSelected() {
        model.getConditions().add( new Pattern52() {{
            setFactType( "Fact" );
            getChildColumns().add( new ConditionCol52() {{
                setFactType( "Fact" );
                setFactField( "field1" );
                setFieldType( DataType.TYPE_STRING );
                setOperator( "==" );
            }} );
        }} );
        model.getData().add( new ArrayList<DTCellValue52>() {{
            add( new DTCellValue52( 1 ) );
            add( new DTCellValue52( "descr" ) );
            add( new DTCellValue52( "md" ) );
        }} );

        uiModel.selectCell( 0,
                            2 );

        builder.onDecisionTableSelectionsChangedEvent( new DecisionTableSelectionsChangedEvent( dtPresenter ) );

        verify( view,
                times( 1 ) ).enableAppendRowMenuItem( eq( true ) );
        verify( view,
                times( 1 ) ).enableInsertRowAboveMenuItem( eq( true ) );
        verify( view,
                times( 1 ) ).enableInsertRowBelowMenuItem( eq( true ) );
        verify( view,
                times( 1 ) ).enableAppendColumnMenuItem( eq( true ) );
    }

    @Test
    public void testOnDecisionTableSelectionsChangedEventWithMultipleRowsSelected() {
        model.getConditions().add( new Pattern52() {{
            setFactType( "Fact" );
            getChildColumns().add( new ConditionCol52() {{
                setFactType( "Fact" );
                setFactField( "field1" );
                setFieldType( DataType.TYPE_STRING );
                setOperator( "==" );
            }} );
        }} );
        model.getData().add( new ArrayList<DTCellValue52>() {{
            add( new DTCellValue52( 1 ) );
            add( new DTCellValue52( "descr" ) );
            add( new DTCellValue52( "md" ) );
        }} );
        model.getData().add( new ArrayList<DTCellValue52>() {{
            add( new DTCellValue52( 1 ) );
            add( new DTCellValue52( "descr" ) );
            add( new DTCellValue52( "md" ) );
        }} );

        uiModel.selectCells( 0,
                             2,
                             1,
                             2 );

        builder.onDecisionTableSelectionsChangedEvent( new DecisionTableSelectionsChangedEvent( dtPresenter ) );

        verify( view,
                times( 1 ) ).enableAppendRowMenuItem( eq( true ) );
        verify( view,
                times( 1 ) ).enableInsertRowAboveMenuItem( eq( false ) );
        verify( view,
                times( 1 ) ).enableInsertRowBelowMenuItem( eq( false ) );
        verify( view,
                times( 1 ) ).enableAppendColumnMenuItem( eq( true ) );
    }

    @Test
    public void testOnDecisionTableSelectedEventReadOnly() {
        dtPresenter.getAccess().setReadOnly( true );
        builder.onDecisionTableSelectedEvent( new DecisionTableSelectedEvent( dtPresenter ) );

        verify( view,
                times( 1 ) ).enableAppendRowMenuItem( eq( false ) );
        verify( view,
                times( 1 ) ).enableInsertRowAboveMenuItem( eq( false ) );
        verify( view,
                times( 1 ) ).enableInsertRowBelowMenuItem( eq( false ) );
        verify( view,
                times( 1 ) ).enableAppendColumnMenuItem( eq( false ) );
    }

}
