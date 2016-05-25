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

import org.drools.workbench.models.guided.dtable.shared.model.AttributeCol52;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.BaseUiSingletonColumn;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.BooleanUiColumn;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.IntegerUiColumn;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.SalienceUiColumn;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.StringUiColumn;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.converters.column.BaseColumnConverter;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.converters.column.impl.AttributeColumnConverter;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.synchronizers.ModelSynchronizer;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.synchronizers.Synchronizer;
import org.drools.workbench.screens.guided.rule.client.editor.RuleAttributeWidget;
import org.junit.Test;
import org.kie.workbench.common.widgets.client.datamodel.AsyncPackageDataModelOracle;
import org.kie.workbench.common.widgets.client.datamodel.AsyncPackageDataModelOracleImpl;
import org.uberfire.ext.wires.core.grids.client.model.BaseGridCellValue;
import org.uberfire.ext.wires.core.grids.client.model.IGridColumn;

import static org.drools.workbench.screens.guided.dtable.client.widget.table.model.synchronizers.Synchronizer.*;
import static org.junit.Assert.*;

public class AttributeColumnSynchronizerTest extends BaseSynchronizerTest {

    @Override
    protected AsyncPackageDataModelOracle getOracle() {
        final AsyncPackageDataModelOracle oracle = new AsyncPackageDataModelOracleImpl();
        return oracle;
    }

    @Override
    protected List<BaseColumnConverter> getConverters() {
        final List<BaseColumnConverter> converters = new ArrayList<BaseColumnConverter>();
        converters.add( new AttributeColumnConverter() );
        return converters;
    }

    @Override
    protected List<Synchronizer<? extends MetaData, ? extends MetaData, ? extends MetaData, ? extends MetaData, ? extends MetaData>> getSynchronizers() {
        final List<Synchronizer<? extends MetaData, ? extends MetaData, ? extends MetaData, ? extends MetaData, ? extends MetaData>> synchronizers = new ArrayList<>();
        synchronizers.add( new AttributeColumnSynchronizer() );
        synchronizers.add( new RowSynchronizer() );
        return synchronizers;
    }

    @Test
    public void testAppend() throws ModelSynchronizer.MoveColumnVetoException {
        final AttributeCol52 column = new AttributeCol52();
        column.setAttribute( RuleAttributeWidget.SALIENCE_ATTR );

        modelSynchronizer.appendColumn( column );

        assertEquals( 1,
                      model.getAttributeCols().size() );
        assertEquals( RuleAttributeWidget.SALIENCE_ATTR,
                      model.getAttributeCols().get( 0 ).getAttribute() );

        assertEquals( 3,
                      uiModel.getColumns().size() );
        assertTrue( uiModel.getColumns().get( 2 ) instanceof IntegerUiColumn );
        assertEquals( RuleAttributeWidget.SALIENCE_ATTR,
                      uiModel.getColumns().get( 2 ).getHeaderMetaData().get( 0 ).getTitle() );
        assertEquals( true,
                      ( (BaseUiSingletonColumn) uiModel.getColumns().get( 2 ) ).isEditable() );
    }

    @Test
    public void testUpdate1() throws ModelSynchronizer.MoveColumnVetoException {
        final AttributeCol52 column = new AttributeCol52();
        column.setAttribute( RuleAttributeWidget.SALIENCE_ATTR );

        modelSynchronizer.appendColumn( column );

        final AttributeCol52 edited = new AttributeCol52();
        edited.setAttribute( RuleAttributeWidget.ENABLED_ATTR );

        modelSynchronizer.updateColumn( column,
                                        edited );

        assertEquals( 1,
                      model.getAttributeCols().size() );
        assertEquals( RuleAttributeWidget.ENABLED_ATTR,
                      model.getAttributeCols().get( 0 ).getAttribute() );

        assertEquals( 3,
                      uiModel.getColumns().size() );
        assertTrue( uiModel.getColumns().get( 2 ) instanceof BooleanUiColumn );
        assertEquals( RuleAttributeWidget.ENABLED_ATTR,
                      uiModel.getColumns().get( 2 ).getHeaderMetaData().get( 0 ).getTitle() );
    }

    @Test
    public void testUpdate2() throws ModelSynchronizer.MoveColumnVetoException {
        final AttributeCol52 column = new AttributeCol52();
        column.setAttribute( RuleAttributeWidget.SALIENCE_ATTR );

        modelSynchronizer.appendColumn( column );

        final AttributeCol52 edited = new AttributeCol52();
        edited.setAttribute( RuleAttributeWidget.SALIENCE_ATTR );
        edited.setHideColumn( true );

        modelSynchronizer.updateColumn( column,
                                        edited );

        assertEquals( 1,
                      model.getAttributeCols().size() );
        assertEquals( RuleAttributeWidget.SALIENCE_ATTR,
                      model.getAttributeCols().get( 0 ).getAttribute() );

        assertEquals( 3,
                      uiModel.getColumns().size() );
        assertTrue( uiModel.getColumns().get( 2 ) instanceof IntegerUiColumn );
        assertEquals( RuleAttributeWidget.SALIENCE_ATTR,
                      uiModel.getColumns().get( 2 ).getHeaderMetaData().get( 0 ).getTitle() );
        assertEquals( false,
                      uiModel.getColumns().get( 2 ).isVisible() );
    }

    @Test
    public void testUpdateSalienceRowNumber() throws ModelSynchronizer.MoveColumnVetoException {
        modelSynchronizer.appendRow();
        modelSynchronizer.appendRow();

        final AttributeCol52 column = new AttributeCol52();
        column.setAttribute( RuleAttributeWidget.SALIENCE_ATTR );

        modelSynchronizer.appendColumn( column );

        final AttributeCol52 edited1 = new AttributeCol52();
        edited1.setAttribute( RuleAttributeWidget.SALIENCE_ATTR );
        edited1.setUseRowNumber( true );

        modelSynchronizer.updateColumn( column,
                                        edited1 );

        assertEquals( 1,
                      model.getAttributeCols().size() );
        assertEquals( RuleAttributeWidget.SALIENCE_ATTR,
                      model.getAttributeCols().get( 0 ).getAttribute() );
        assertEquals( 1,
                      model.getData().get( 0 ).get( 2 ).getNumericValue() );
        assertEquals( 2,
                      model.getData().get( 1 ).get( 2 ).getNumericValue() );

        assertEquals( 3,
                      uiModel.getColumns().size() );
        assertTrue( uiModel.getColumns().get( 2 ) instanceof SalienceUiColumn );
        assertEquals( RuleAttributeWidget.SALIENCE_ATTR,
                      uiModel.getColumns().get( 2 ).getHeaderMetaData().get( 0 ).getTitle() );
        assertEquals( true,
                      ( (SalienceUiColumn) uiModel.getColumns().get( 2 ) ).isUseRowNumber() );
        assertEquals( 1,
                      uiModel.getRow( 0 ).getCells().get( 2 ).getValue().getValue() );
        assertEquals( 2,
                      uiModel.getRow( 1 ).getCells().get( 2 ).getValue().getValue() );

        final AttributeCol52 edited2 = new AttributeCol52();
        edited2.setAttribute( RuleAttributeWidget.SALIENCE_ATTR );
        edited2.setUseRowNumber( true );
        edited2.setReverseOrder( true );

        modelSynchronizer.updateColumn( column,
                                        edited2 );

        assertEquals( 1,
                      model.getAttributeCols().size() );
        assertEquals( RuleAttributeWidget.SALIENCE_ATTR,
                      model.getAttributeCols().get( 0 ).getAttribute() );
        assertEquals( 2,
                      model.getData().get( 0 ).get( 2 ).getNumericValue() );
        assertEquals( 1,
                      model.getData().get( 1 ).get( 2 ).getNumericValue() );

        assertEquals( 3,
                      uiModel.getColumns().size() );
        assertTrue( uiModel.getColumns().get( 2 ) instanceof SalienceUiColumn );
        assertEquals( RuleAttributeWidget.SALIENCE_ATTR,
                      uiModel.getColumns().get( 2 ).getHeaderMetaData().get( 0 ).getTitle() );
        assertEquals( true,
                      ( (SalienceUiColumn) uiModel.getColumns().get( 2 ) ).isUseRowNumber() );
        assertEquals( 2,
                      uiModel.getRow( 0 ).getCells().get( 2 ).getValue().getValue() );
        assertEquals( 1,
                      uiModel.getRow( 1 ).getCells().get( 2 ).getValue().getValue() );
    }

    @Test
    public void testDelete() throws ModelSynchronizer.MoveColumnVetoException {
        final AttributeCol52 column = new AttributeCol52();
        column.setAttribute( RuleAttributeWidget.SALIENCE_ATTR );

        modelSynchronizer.appendColumn( column );

        assertEquals( 1,
                      model.getAttributeCols().size() );
        assertEquals( 3,
                      uiModel.getColumns().size() );

        modelSynchronizer.deleteColumn( column );
        assertEquals( 0,
                      model.getAttributeCols().size() );
        assertEquals( 2,
                      uiModel.getColumns().size() );
    }

    @Test
    public void testMoveColumnTo_MoveLeft() throws ModelSynchronizer.MoveColumnVetoException {
        final AttributeCol52 column1 = new AttributeCol52();
        column1.setAttribute( RuleAttributeWidget.SALIENCE_ATTR );
        final AttributeCol52 column2 = new AttributeCol52();
        column2.setAttribute( RuleAttributeWidget.AGENDA_GROUP_ATTR );

        modelSynchronizer.appendColumn( column1 );
        modelSynchronizer.appendColumn( column2 );

        modelSynchronizer.appendRow();
        uiModel.setCell( 0,
                         2,
                         new BaseGridCellValue<Integer>( 1 ) );
        uiModel.setCell( 0,
                         3,
                         new BaseGridCellValue<String>( "smurf" ) );

        assertEquals( 2,
                      model.getAttributeCols().size() );
        assertEquals( column1,
                      model.getAttributeCols().get( 0 ) );
        assertEquals( column2,
                      model.getAttributeCols().get( 1 ) );
        assertEquals( 1,
                      model.getData().get( 0 ).get( 2 ).getNumericValue() );
        assertEquals( "smurf",
                      model.getData().get( 0 ).get( 3 ).getStringValue() );

        assertEquals( 4,
                      uiModel.getColumns().size() );
        final IGridColumn<?> uiModelColumn1_1 = uiModel.getColumns().get( 2 );
        final IGridColumn<?> uiModelColumn2_1 = uiModel.getColumns().get( 3 );
        assertEquals( RuleAttributeWidget.SALIENCE_ATTR,
                      uiModelColumn1_1.getHeaderMetaData().get( 0 ).getTitle() );
        assertEquals( RuleAttributeWidget.AGENDA_GROUP_ATTR,
                      uiModelColumn2_1.getHeaderMetaData().get( 0 ).getTitle() );
        assertTrue( uiModelColumn1_1 instanceof IntegerUiColumn );
        assertTrue( uiModelColumn2_1 instanceof StringUiColumn );
        assertEquals( 2,
                      uiModelColumn1_1.getIndex() );
        assertEquals( 3,
                      uiModelColumn2_1.getIndex() );
        assertEquals( 1,
                      uiModel.getRow( 0 ).getCells().get( uiModelColumn1_1.getIndex() ).getValue().getValue() );
        assertEquals( "smurf",
                      uiModel.getRow( 0 ).getCells().get( uiModelColumn2_1.getIndex() ).getValue().getValue() );

        uiModel.moveColumnTo( 2,
                              uiModelColumn2_1 );

        assertEquals( 2,
                      model.getAttributeCols().size() );
        assertEquals( column2,
                      model.getAttributeCols().get( 0 ) );
        assertEquals( column1,
                      model.getAttributeCols().get( 1 ) );
        assertEquals( "smurf",
                      model.getData().get( 0 ).get( 2 ).getStringValue() );
        assertEquals( 1,
                      model.getData().get( 0 ).get( 3 ).getNumericValue() );

        assertEquals( 4,
                      uiModel.getColumns().size() );
        final IGridColumn<?> uiModelColumn1_2 = uiModel.getColumns().get( 2 );
        final IGridColumn<?> uiModelColumn2_2 = uiModel.getColumns().get( 3 );
        assertEquals( RuleAttributeWidget.AGENDA_GROUP_ATTR,
                      uiModelColumn1_2.getHeaderMetaData().get( 0 ).getTitle() );
        assertEquals( RuleAttributeWidget.SALIENCE_ATTR,
                      uiModelColumn2_2.getHeaderMetaData().get( 0 ).getTitle() );
        assertTrue( uiModelColumn1_2 instanceof StringUiColumn );
        assertTrue( uiModelColumn2_2 instanceof IntegerUiColumn );
        assertEquals( 3,
                      uiModelColumn1_2.getIndex() );
        assertEquals( 2,
                      uiModelColumn2_2.getIndex() );
        assertEquals( "smurf",
                      uiModel.getRow( 0 ).getCells().get( uiModelColumn1_2.getIndex() ).getValue().getValue() );
        assertEquals( 1,
                      uiModel.getRow( 0 ).getCells().get( uiModelColumn2_2.getIndex() ).getValue().getValue() );
    }

    @Test
    public void testMoveColumnTo_MoveRight() throws ModelSynchronizer.MoveColumnVetoException {
        final AttributeCol52 column1 = new AttributeCol52();
        column1.setAttribute( RuleAttributeWidget.SALIENCE_ATTR );
        final AttributeCol52 column2 = new AttributeCol52();
        column2.setAttribute( RuleAttributeWidget.AGENDA_GROUP_ATTR );

        modelSynchronizer.appendColumn( column1 );
        modelSynchronizer.appendColumn( column2 );

        modelSynchronizer.appendRow();
        uiModel.setCell( 0,
                         2,
                         new BaseGridCellValue<Integer>( 1 ) );
        uiModel.setCell( 0,
                         3,
                         new BaseGridCellValue<String>( "smurf" ) );

        assertEquals( 2,
                      model.getAttributeCols().size() );
        assertEquals( column1,
                      model.getAttributeCols().get( 0 ) );
        assertEquals( column2,
                      model.getAttributeCols().get( 1 ) );
        assertEquals( 1,
                      model.getData().get( 0 ).get( 2 ).getNumericValue() );
        assertEquals( "smurf",
                      model.getData().get( 0 ).get( 3 ).getStringValue() );

        assertEquals( 4,
                      uiModel.getColumns().size() );
        final IGridColumn<?> uiModelColumn1_1 = uiModel.getColumns().get( 2 );
        final IGridColumn<?> uiModelColumn2_1 = uiModel.getColumns().get( 3 );
        assertEquals( RuleAttributeWidget.SALIENCE_ATTR,
                      uiModelColumn1_1.getHeaderMetaData().get( 0 ).getTitle() );
        assertEquals( RuleAttributeWidget.AGENDA_GROUP_ATTR,
                      uiModelColumn2_1.getHeaderMetaData().get( 0 ).getTitle() );
        assertTrue( uiModelColumn1_1 instanceof IntegerUiColumn );
        assertTrue( uiModelColumn2_1 instanceof StringUiColumn );
        assertEquals( 2,
                      uiModelColumn1_1.getIndex() );
        assertEquals( 3,
                      uiModelColumn2_1.getIndex() );
        assertEquals( 1,
                      uiModel.getRow( 0 ).getCells().get( uiModelColumn1_1.getIndex() ).getValue().getValue() );
        assertEquals( "smurf",
                      uiModel.getRow( 0 ).getCells().get( uiModelColumn2_1.getIndex() ).getValue().getValue() );

        uiModel.moveColumnTo( 3,
                              uiModelColumn1_1 );

        assertEquals( 2,
                      model.getAttributeCols().size() );
        assertEquals( column2,
                      model.getAttributeCols().get( 0 ) );
        assertEquals( column1,
                      model.getAttributeCols().get( 1 ) );
        assertEquals( "smurf",
                      model.getData().get( 0 ).get( 2 ).getStringValue() );
        assertEquals( 1,
                      model.getData().get( 0 ).get( 3 ).getNumericValue() );

        assertEquals( 4,
                      uiModel.getColumns().size() );
        final IGridColumn<?> uiModelColumn1_2 = uiModel.getColumns().get( 2 );
        final IGridColumn<?> uiModelColumn2_2 = uiModel.getColumns().get( 3 );
        assertEquals( RuleAttributeWidget.AGENDA_GROUP_ATTR,
                      uiModelColumn1_2.getHeaderMetaData().get( 0 ).getTitle() );
        assertEquals( RuleAttributeWidget.SALIENCE_ATTR,
                      uiModelColumn2_2.getHeaderMetaData().get( 0 ).getTitle() );
        assertTrue( uiModelColumn1_2 instanceof StringUiColumn );
        assertTrue( uiModelColumn2_2 instanceof IntegerUiColumn );
        assertEquals( 3,
                      uiModelColumn1_2.getIndex() );
        assertEquals( 2,
                      uiModelColumn2_2.getIndex() );
        assertEquals( "smurf",
                      uiModel.getRow( 0 ).getCells().get( uiModelColumn1_2.getIndex() ).getValue().getValue() );
        assertEquals( 1,
                      uiModel.getRow( 0 ).getCells().get( uiModelColumn2_2.getIndex() ).getValue().getValue() );
    }

    @Test
    public void testMoveColumnTo_OutOfBounds() throws ModelSynchronizer.MoveColumnVetoException {
        final AttributeCol52 column1 = new AttributeCol52();
        column1.setAttribute( RuleAttributeWidget.SALIENCE_ATTR );
        final AttributeCol52 column2 = new AttributeCol52();
        column2.setAttribute( RuleAttributeWidget.AGENDA_GROUP_ATTR );

        modelSynchronizer.appendColumn( column1 );
        modelSynchronizer.appendColumn( column2 );

        modelSynchronizer.appendRow();
        uiModel.setCell( 0,
                         2,
                         new BaseGridCellValue<Integer>( 1 ) );
        uiModel.setCell( 0,
                         3,
                         new BaseGridCellValue<String>( "smurf" ) );

        assertEquals( 2,
                      model.getAttributeCols().size() );
        assertEquals( column1,
                      model.getAttributeCols().get( 0 ) );
        assertEquals( column2,
                      model.getAttributeCols().get( 1 ) );
        assertEquals( 1,
                      model.getData().get( 0 ).get( 2 ).getNumericValue() );
        assertEquals( "smurf",
                      model.getData().get( 0 ).get( 3 ).getStringValue() );

        assertEquals( 4,
                      uiModel.getColumns().size() );
        final IGridColumn<?> uiModelColumn1_1 = uiModel.getColumns().get( 2 );
        final IGridColumn<?> uiModelColumn2_1 = uiModel.getColumns().get( 3 );
        assertEquals( RuleAttributeWidget.SALIENCE_ATTR,
                      uiModelColumn1_1.getHeaderMetaData().get( 0 ).getTitle() );
        assertEquals( RuleAttributeWidget.AGENDA_GROUP_ATTR,
                      uiModelColumn2_1.getHeaderMetaData().get( 0 ).getTitle() );
        assertTrue( uiModelColumn1_1 instanceof IntegerUiColumn );
        assertTrue( uiModelColumn2_1 instanceof StringUiColumn );
        assertEquals( 2,
                      uiModelColumn1_1.getIndex() );
        assertEquals( 3,
                      uiModelColumn2_1.getIndex() );
        assertEquals( 1,
                      uiModel.getRow( 0 ).getCells().get( uiModelColumn1_1.getIndex() ).getValue().getValue() );
        assertEquals( "smurf",
                      uiModel.getRow( 0 ).getCells().get( uiModelColumn2_1.getIndex() ).getValue().getValue() );

        uiModel.moveColumnTo( 0,
                              uiModelColumn1_1 );

        assertEquals( 2,
                      model.getAttributeCols().size() );
        assertEquals( column1,
                      model.getAttributeCols().get( 0 ) );
        assertEquals( column2,
                      model.getAttributeCols().get( 1 ) );
        assertEquals( 1,
                      model.getData().get( 0 ).get( 2 ).getNumericValue() );
        assertEquals( "smurf",
                      model.getData().get( 0 ).get( 3 ).getStringValue() );

        assertEquals( 4,
                      uiModel.getColumns().size() );
        final IGridColumn<?> uiModelColumn1_2 = uiModel.getColumns().get( 2 );
        final IGridColumn<?> uiModelColumn2_2 = uiModel.getColumns().get( 3 );
        assertEquals( RuleAttributeWidget.SALIENCE_ATTR,
                      uiModelColumn1_2.getHeaderMetaData().get( 0 ).getTitle() );
        assertEquals( RuleAttributeWidget.AGENDA_GROUP_ATTR,
                      uiModelColumn2_2.getHeaderMetaData().get( 0 ).getTitle() );
        assertTrue( uiModelColumn1_2 instanceof IntegerUiColumn );
        assertTrue( uiModelColumn2_2 instanceof StringUiColumn );
        assertEquals( 2,
                      uiModelColumn1_2.getIndex() );
        assertEquals( 3,
                      uiModelColumn2_2.getIndex() );
        assertEquals( 1,
                      uiModel.getRow( 0 ).getCells().get( uiModelColumn1_2.getIndex() ).getValue().getValue() );
        assertEquals( "smurf",
                      uiModel.getRow( 0 ).getCells().get( uiModelColumn2_2.getIndex() ).getValue().getValue() );
    }

    @Test
    public void testMoveColumnsTo_MoveLeft() throws ModelSynchronizer.MoveColumnVetoException {
        final AttributeCol52 column1 = new AttributeCol52();
        column1.setAttribute( RuleAttributeWidget.SALIENCE_ATTR );
        final AttributeCol52 column2 = new AttributeCol52();
        column2.setAttribute( RuleAttributeWidget.AGENDA_GROUP_ATTR );
        final AttributeCol52 column3 = new AttributeCol52();
        column3.setAttribute( RuleAttributeWidget.AUTO_FOCUS_ATTR );

        modelSynchronizer.appendColumn( column1 );
        modelSynchronizer.appendColumn( column2 );
        modelSynchronizer.appendColumn( column3 );

        modelSynchronizer.appendRow();
        uiModel.setCell( 0,
                         2,
                         new BaseGridCellValue<Integer>( 1 ) );
        uiModel.setCell( 0,
                         3,
                         new BaseGridCellValue<String>( "smurf" ) );
        uiModel.setCell( 0,
                         4,
                         new BaseGridCellValue<Boolean>( true ) );

        final IGridColumn<?> uiModelColumn1_1 = uiModel.getColumns().get( 2 );
        final IGridColumn<?> uiModelColumn2_1 = uiModel.getColumns().get( 3 );
        final IGridColumn<?> uiModelColumn3_1 = uiModel.getColumns().get( 4 );

        assertTestMoveColumnsTo( column1,
                                 column2,
                                 column3,
                                 uiModelColumn1_1,
                                 uiModelColumn2_1,
                                 uiModelColumn3_1 );

        //Moving multiple Attribute columns as an unsupported operation as it's impossible via the UI
        uiModel.moveColumnsTo( 2,
                               new ArrayList<IGridColumn<?>>() {{
                                   add( uiModelColumn2_1 );
                                   add( uiModelColumn3_1 );
                               }} );

        final IGridColumn<?> uiModelColumn1_2 = uiModel.getColumns().get( 2 );
        final IGridColumn<?> uiModelColumn2_2 = uiModel.getColumns().get( 3 );
        final IGridColumn<?> uiModelColumn3_2 = uiModel.getColumns().get( 4 );

        assertTestMoveColumnsTo( column1,
                                 column2,
                                 column3,
                                 uiModelColumn1_2,
                                 uiModelColumn2_2,
                                 uiModelColumn3_2 );
    }

    @Test
    public void testMoveColumnsTo_MoveRight() throws ModelSynchronizer.MoveColumnVetoException {
        final AttributeCol52 column1 = new AttributeCol52();
        column1.setAttribute( RuleAttributeWidget.SALIENCE_ATTR );
        final AttributeCol52 column2 = new AttributeCol52();
        column2.setAttribute( RuleAttributeWidget.AGENDA_GROUP_ATTR );
        final AttributeCol52 column3 = new AttributeCol52();
        column3.setAttribute( RuleAttributeWidget.AUTO_FOCUS_ATTR );

        modelSynchronizer.appendColumn( column1 );
        modelSynchronizer.appendColumn( column2 );
        modelSynchronizer.appendColumn( column3 );

        modelSynchronizer.appendRow();
        uiModel.setCell( 0,
                         2,
                         new BaseGridCellValue<Integer>( 1 ) );
        uiModel.setCell( 0,
                         3,
                         new BaseGridCellValue<String>( "smurf" ) );
        uiModel.setCell( 0,
                         4,
                         new BaseGridCellValue<Boolean>( true ) );

        final IGridColumn<?> uiModelColumn1_1 = uiModel.getColumns().get( 2 );
        final IGridColumn<?> uiModelColumn2_1 = uiModel.getColumns().get( 3 );
        final IGridColumn<?> uiModelColumn3_1 = uiModel.getColumns().get( 4 );

        assertTestMoveColumnsTo( column1,
                                 column2,
                                 column3,
                                 uiModelColumn1_1,
                                 uiModelColumn2_1,
                                 uiModelColumn3_1 );

        uiModel.moveColumnsTo( 4,
                               new ArrayList<IGridColumn<?>>() {{
                                   add( uiModelColumn1_1 );
                                   add( uiModelColumn2_1 );
                               }} );

        final IGridColumn<?> uiModelColumn1_2 = uiModel.getColumns().get( 2 );
        final IGridColumn<?> uiModelColumn2_2 = uiModel.getColumns().get( 3 );
        final IGridColumn<?> uiModelColumn3_2 = uiModel.getColumns().get( 4 );

        assertTestMoveColumnsTo( column1,
                                 column2,
                                 column3,
                                 uiModelColumn1_2,
                                 uiModelColumn2_2,
                                 uiModelColumn3_2 );
    }

    private void assertTestMoveColumnsTo( final AttributeCol52 column1,
                                          final AttributeCol52 column2,
                                          final AttributeCol52 column3,
                                          final IGridColumn<?> uiModelColumn1,
                                          final IGridColumn<?> uiModelColumn2,
                                          final IGridColumn<?> uiModelColumn3 ) {
        assertEquals( 3,
                      model.getAttributeCols().size() );
        assertEquals( column1,
                      model.getAttributeCols().get( 0 ) );
        assertEquals( column2,
                      model.getAttributeCols().get( 1 ) );
        assertEquals( column3,
                      model.getAttributeCols().get( 2 ) );
        assertEquals( 1,
                      model.getData().get( 0 ).get( 2 ).getNumericValue() );
        assertEquals( "smurf",
                      model.getData().get( 0 ).get( 3 ).getStringValue() );
        assertEquals( true,
                      model.getData().get( 0 ).get( 4 ).getBooleanValue() );

        assertEquals( 5,
                      uiModel.getColumns().size() );
        assertEquals( RuleAttributeWidget.SALIENCE_ATTR,
                      uiModelColumn1.getHeaderMetaData().get( 0 ).getTitle() );
        assertEquals( RuleAttributeWidget.AGENDA_GROUP_ATTR,
                      uiModelColumn2.getHeaderMetaData().get( 0 ).getTitle() );
        assertEquals( RuleAttributeWidget.AUTO_FOCUS_ATTR,
                      uiModelColumn3.getHeaderMetaData().get( 0 ).getTitle() );
        assertTrue( uiModelColumn1 instanceof IntegerUiColumn );
        assertTrue( uiModelColumn2 instanceof StringUiColumn );
        assertTrue( uiModelColumn3 instanceof BooleanUiColumn );
        assertEquals( 2,
                      uiModelColumn1.getIndex() );
        assertEquals( 3,
                      uiModelColumn2.getIndex() );
        assertEquals( 4,
                      uiModelColumn3.getIndex() );
        assertEquals( 1,
                      uiModel.getRow( 0 ).getCells().get( uiModelColumn1.getIndex() ).getValue().getValue() );
        assertEquals( "smurf",
                      uiModel.getRow( 0 ).getCells().get( uiModelColumn2.getIndex() ).getValue().getValue() );
        assertEquals( true,
                      uiModel.getRow( 0 ).getCells().get( uiModelColumn3.getIndex() ).getValue().getValue() );

    }

}
