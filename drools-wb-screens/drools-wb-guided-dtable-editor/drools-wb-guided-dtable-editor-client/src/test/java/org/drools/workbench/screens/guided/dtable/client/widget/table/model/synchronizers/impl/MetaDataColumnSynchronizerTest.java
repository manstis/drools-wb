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

import org.drools.workbench.models.guided.dtable.shared.model.MetadataCol52;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.StringUiColumn;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.converters.column.BaseColumnConverter;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.converters.column.impl.MetaDataColumnConverter;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.synchronizers.ModelSynchronizer;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.synchronizers.Synchronizer;
import org.junit.Test;
import org.kie.workbench.common.widgets.client.datamodel.AsyncPackageDataModelOracle;
import org.kie.workbench.common.widgets.client.datamodel.AsyncPackageDataModelOracleImpl;
import org.uberfire.ext.wires.core.grids.client.model.BaseGridCellValue;
import org.uberfire.ext.wires.core.grids.client.model.IGridColumn;

import static org.drools.workbench.screens.guided.dtable.client.widget.table.model.synchronizers.Synchronizer.*;
import static org.junit.Assert.*;

public class MetaDataColumnSynchronizerTest extends BaseSynchronizerTest {

    @Override
    protected AsyncPackageDataModelOracle getOracle() {
        final AsyncPackageDataModelOracle oracle = new AsyncPackageDataModelOracleImpl();
        return oracle;
    }

    @Override
    protected List<BaseColumnConverter> getConverters() {
        final List<BaseColumnConverter> converters = new ArrayList<BaseColumnConverter>();
        converters.add( new MetaDataColumnConverter() );
        return converters;
    }

    @Override
    protected List<Synchronizer<? extends MetaData, ? extends MetaData, ? extends MetaData, ? extends MetaData, ? extends MetaData>> getSynchronizers() {
        final List<Synchronizer<? extends MetaData, ? extends MetaData, ? extends MetaData, ? extends MetaData, ? extends MetaData>> synchronizers = new ArrayList<>();
        synchronizers.add( new MetaDataColumnSynchronizer() );
        synchronizers.add( new RowSynchronizer() );
        return synchronizers;
    }

    @Test
    public void testAppend() throws ModelSynchronizer.MoveColumnVetoException {
        final MetadataCol52 column = new MetadataCol52();
        column.setMetadata( "smurf" );

        modelSynchronizer.appendColumn( column );

        assertEquals( 1,
                      model.getMetadataCols().size() );
        assertEquals( "smurf",
                      model.getMetadataCols().get( 0 ).getMetadata() );

        assertEquals( 3,
                      uiModel.getColumns().size() );
        assertEquals( "smurf",
                      uiModel.getColumns().get( 2 ).getHeaderMetaData().get( 0 ).getTitle() );
    }

    @Test
    public void testUpdate1() throws ModelSynchronizer.MoveColumnVetoException {
        final MetadataCol52 column = new MetadataCol52();
        column.setMetadata( "smurf" );

        modelSynchronizer.appendColumn( column );

        final MetadataCol52 edited = new MetadataCol52();
        edited.setMetadata( "changed" );

        modelSynchronizer.updateColumn( column,
                                        edited );

        assertEquals( 1,
                      model.getMetadataCols().size() );
        assertEquals( "changed",
                      model.getMetadataCols().get( 0 ).getMetadata() );

        assertEquals( 3,
                      uiModel.getColumns().size() );
        assertEquals( "changed",
                      uiModel.getColumns().get( 2 ).getHeaderMetaData().get( 0 ).getTitle() );
    }

    @Test
    public void testUpdate2() throws ModelSynchronizer.MoveColumnVetoException {
        final MetadataCol52 column = new MetadataCol52();
        column.setMetadata( "smurf" );

        modelSynchronizer.appendColumn( column );

        final MetadataCol52 edited = new MetadataCol52();
        edited.setMetadata( "smurf" );
        edited.setHideColumn( true );

        modelSynchronizer.updateColumn( column,
                                        edited );

        assertEquals( 1,
                      model.getMetadataCols().size() );
        assertEquals( "smurf",
                      model.getMetadataCols().get( 0 ).getMetadata() );

        assertEquals( 3,
                      uiModel.getColumns().size() );
        assertEquals( "smurf",
                      uiModel.getColumns().get( 2 ).getHeaderMetaData().get( 0 ).getTitle() );
        assertEquals( false,
                      uiModel.getColumns().get( 2 ).isVisible() );
    }

    @Test
    public void testDelete() throws ModelSynchronizer.MoveColumnVetoException {
        final MetadataCol52 column = new MetadataCol52();
        column.setMetadata( "smurf" );

        modelSynchronizer.appendColumn( column );

        assertEquals( 1,
                      model.getMetadataCols().size() );
        assertEquals( 3,
                      uiModel.getColumns().size() );

        modelSynchronizer.deleteColumn( column );
        assertEquals( 0,
                      model.getMetadataCols().size() );
        assertEquals( 2,
                      uiModel.getColumns().size() );
    }

    @Test
    public void testMoveColumnTo_MoveLeft() throws ModelSynchronizer.MoveColumnVetoException {
        final MetadataCol52 column1 = new MetadataCol52();
        column1.setMetadata( "metadata1" );
        final MetadataCol52 column2 = new MetadataCol52();
        column2.setMetadata( "metadata2" );

        modelSynchronizer.appendColumn( column1 );
        modelSynchronizer.appendColumn( column2 );

        modelSynchronizer.appendRow();
        uiModel.setCell( 0,
                         2,
                         new BaseGridCellValue<String>( "metadata1" ) );
        uiModel.setCell( 0,
                         3,
                         new BaseGridCellValue<String>( "metadata2" ) );

        assertEquals( 2,
                      model.getMetadataCols().size() );
        assertEquals( column1,
                      model.getMetadataCols().get( 0 ) );
        assertEquals( column2,
                      model.getMetadataCols().get( 1 ) );
        assertEquals( "metadata1",
                      model.getData().get( 0 ).get( 2 ).getStringValue() );
        assertEquals( "metadata2",
                      model.getData().get( 0 ).get( 3 ).getStringValue() );

        assertEquals( 4,
                      uiModel.getColumns().size() );
        final IGridColumn<?> uiModelColumn1_1 = uiModel.getColumns().get( 2 );
        final IGridColumn<?> uiModelColumn2_1 = uiModel.getColumns().get( 3 );
        assertEquals( "metadata1",
                      uiModelColumn1_1.getHeaderMetaData().get( 0 ).getTitle() );
        assertEquals( "metadata2",
                      uiModelColumn2_1.getHeaderMetaData().get( 0 ).getTitle() );
        assertTrue( uiModelColumn1_1 instanceof StringUiColumn );
        assertTrue( uiModelColumn2_1 instanceof StringUiColumn );
        assertEquals( 2,
                      uiModelColumn1_1.getIndex() );
        assertEquals( 3,
                      uiModelColumn2_1.getIndex() );
        assertEquals( "metadata1",
                      uiModel.getRow( 0 ).getCells().get( uiModelColumn1_1.getIndex() ).getValue().getValue() );
        assertEquals( "metadata2",
                      uiModel.getRow( 0 ).getCells().get( uiModelColumn2_1.getIndex() ).getValue().getValue() );

        uiModel.moveColumnTo( 2,
                              uiModelColumn2_1 );

        assertEquals( 2,
                      model.getMetadataCols().size() );
        assertEquals( column2,
                      model.getMetadataCols().get( 0 ) );
        assertEquals( column1,
                      model.getMetadataCols().get( 1 ) );
        assertEquals( "metadata2",
                      model.getData().get( 0 ).get( 2 ).getStringValue() );
        assertEquals( "metadata1",
                      model.getData().get( 0 ).get( 3 ).getStringValue() );

        assertEquals( 4,
                      uiModel.getColumns().size() );
        final IGridColumn<?> uiModelColumn1_2 = uiModel.getColumns().get( 2 );
        final IGridColumn<?> uiModelColumn2_2 = uiModel.getColumns().get( 3 );
        assertEquals( "metadata2",
                      uiModelColumn1_2.getHeaderMetaData().get( 0 ).getTitle() );
        assertEquals( "metadata1",
                      uiModelColumn2_2.getHeaderMetaData().get( 0 ).getTitle() );
        assertTrue( uiModelColumn1_2 instanceof StringUiColumn );
        assertTrue( uiModelColumn2_2 instanceof StringUiColumn );
        assertEquals( 3,
                      uiModelColumn1_2.getIndex() );
        assertEquals( 2,
                      uiModelColumn2_2.getIndex() );
        assertEquals( "metadata2",
                      uiModel.getRow( 0 ).getCells().get( uiModelColumn1_2.getIndex() ).getValue().getValue() );
        assertEquals( "metadata1",
                      uiModel.getRow( 0 ).getCells().get( uiModelColumn2_2.getIndex() ).getValue().getValue() );
    }

    @Test
    public void testMoveColumnTo_MoveRight() throws ModelSynchronizer.MoveColumnVetoException {
        final MetadataCol52 column1 = new MetadataCol52();
        column1.setMetadata( "metadata1" );
        final MetadataCol52 column2 = new MetadataCol52();
        column2.setMetadata( "metadata2" );

        modelSynchronizer.appendColumn( column1 );
        modelSynchronizer.appendColumn( column2 );

        modelSynchronizer.appendRow();
        uiModel.setCell( 0,
                         2,
                         new BaseGridCellValue<String>( "metadata1" ) );
        uiModel.setCell( 0,
                         3,
                         new BaseGridCellValue<String>( "metadata2" ) );

        assertEquals( 2,
                      model.getMetadataCols().size() );
        assertEquals( column1,
                      model.getMetadataCols().get( 0 ) );
        assertEquals( column2,
                      model.getMetadataCols().get( 1 ) );
        assertEquals( "metadata1",
                      model.getData().get( 0 ).get( 2 ).getStringValue() );
        assertEquals( "metadata2",
                      model.getData().get( 0 ).get( 3 ).getStringValue() );

        assertEquals( 4,
                      uiModel.getColumns().size() );
        final IGridColumn<?> uiModelColumn1_1 = uiModel.getColumns().get( 2 );
        final IGridColumn<?> uiModelColumn2_1 = uiModel.getColumns().get( 3 );
        assertEquals( "metadata1",
                      uiModelColumn1_1.getHeaderMetaData().get( 0 ).getTitle() );
        assertEquals( "metadata2",
                      uiModelColumn2_1.getHeaderMetaData().get( 0 ).getTitle() );
        assertTrue( uiModelColumn1_1 instanceof StringUiColumn );
        assertTrue( uiModelColumn2_1 instanceof StringUiColumn );
        assertEquals( 2,
                      uiModelColumn1_1.getIndex() );
        assertEquals( 3,
                      uiModelColumn2_1.getIndex() );
        assertEquals( "metadata1",
                      uiModel.getRow( 0 ).getCells().get( uiModelColumn1_1.getIndex() ).getValue().getValue() );
        assertEquals( "metadata2",
                      uiModel.getRow( 0 ).getCells().get( uiModelColumn2_1.getIndex() ).getValue().getValue() );

        uiModel.moveColumnTo( 3,
                              uiModelColumn1_1 );

        assertEquals( 2,
                      model.getMetadataCols().size() );
        assertEquals( column2,
                      model.getMetadataCols().get( 0 ) );
        assertEquals( column1,
                      model.getMetadataCols().get( 1 ) );
        assertEquals( "metadata2",
                      model.getData().get( 0 ).get( 2 ).getStringValue() );
        assertEquals( "metadata1",
                      model.getData().get( 0 ).get( 3 ).getStringValue() );

        assertEquals( 4,
                      uiModel.getColumns().size() );
        final IGridColumn<?> uiModelColumn1_2 = uiModel.getColumns().get( 2 );
        final IGridColumn<?> uiModelColumn2_2 = uiModel.getColumns().get( 3 );
        assertEquals( "metadata2",
                      uiModelColumn1_2.getHeaderMetaData().get( 0 ).getTitle() );
        assertEquals( "metadata1",
                      uiModelColumn2_2.getHeaderMetaData().get( 0 ).getTitle() );
        assertTrue( uiModelColumn1_2 instanceof StringUiColumn );
        assertTrue( uiModelColumn2_2 instanceof StringUiColumn );
        assertEquals( 3,
                      uiModelColumn1_2.getIndex() );
        assertEquals( 2,
                      uiModelColumn2_2.getIndex() );
        assertEquals( "metadata2",
                      uiModel.getRow( 0 ).getCells().get( uiModelColumn1_2.getIndex() ).getValue().getValue() );
        assertEquals( "metadata1",
                      uiModel.getRow( 0 ).getCells().get( uiModelColumn2_2.getIndex() ).getValue().getValue() );
    }

    @Test
    public void testMoveColumnTo_OutOfBounds() throws ModelSynchronizer.MoveColumnVetoException {
        final MetadataCol52 column1 = new MetadataCol52();
        column1.setMetadata( "metadata1" );
        final MetadataCol52 column2 = new MetadataCol52();
        column2.setMetadata( "metadata2" );

        modelSynchronizer.appendColumn( column1 );
        modelSynchronizer.appendColumn( column2 );

        modelSynchronizer.appendRow();
        uiModel.setCell( 0,
                         2,
                         new BaseGridCellValue<String>( "metadata1" ) );
        uiModel.setCell( 0,
                         3,
                         new BaseGridCellValue<String>( "metadata2" ) );

        assertEquals( 2,
                      model.getMetadataCols().size() );
        assertEquals( column1,
                      model.getMetadataCols().get( 0 ) );
        assertEquals( column2,
                      model.getMetadataCols().get( 1 ) );
        assertEquals( "metadata1",
                      model.getData().get( 0 ).get( 2 ).getStringValue() );
        assertEquals( "metadata2",
                      model.getData().get( 0 ).get( 3 ).getStringValue() );

        assertEquals( 4,
                      uiModel.getColumns().size() );
        final IGridColumn<?> uiModelColumn1_1 = uiModel.getColumns().get( 2 );
        final IGridColumn<?> uiModelColumn2_1 = uiModel.getColumns().get( 3 );
        assertEquals( "metadata1",
                      uiModelColumn1_1.getHeaderMetaData().get( 0 ).getTitle() );
        assertEquals( "metadata2",
                      uiModelColumn2_1.getHeaderMetaData().get( 0 ).getTitle() );
        assertTrue( uiModelColumn1_1 instanceof StringUiColumn );
        assertTrue( uiModelColumn2_1 instanceof StringUiColumn );
        assertEquals( 2,
                      uiModelColumn1_1.getIndex() );
        assertEquals( 3,
                      uiModelColumn2_1.getIndex() );
        assertEquals( "metadata1",
                      uiModel.getRow( 0 ).getCells().get( uiModelColumn1_1.getIndex() ).getValue().getValue() );
        assertEquals( "metadata2",
                      uiModel.getRow( 0 ).getCells().get( uiModelColumn2_1.getIndex() ).getValue().getValue() );

        uiModel.moveColumnTo( 0,
                              uiModelColumn1_1 );

        assertEquals( 2,
                      model.getMetadataCols().size() );
        assertEquals( column1,
                      model.getMetadataCols().get( 0 ) );
        assertEquals( column2,
                      model.getMetadataCols().get( 1 ) );
        assertEquals( "metadata1",
                      model.getData().get( 0 ).get( 2 ).getStringValue() );
        assertEquals( "metadata2",
                      model.getData().get( 0 ).get( 3 ).getStringValue() );

        assertEquals( 4,
                      uiModel.getColumns().size() );
        final IGridColumn<?> uiModelColumn1_2 = uiModel.getColumns().get( 2 );
        final IGridColumn<?> uiModelColumn2_2 = uiModel.getColumns().get( 3 );
        assertEquals( "metadata1",
                      uiModelColumn1_2.getHeaderMetaData().get( 0 ).getTitle() );
        assertEquals( "metadata2",
                      uiModelColumn2_2.getHeaderMetaData().get( 0 ).getTitle() );
        assertTrue( uiModelColumn1_2 instanceof StringUiColumn );
        assertTrue( uiModelColumn2_2 instanceof StringUiColumn );
        assertEquals( 2,
                      uiModelColumn1_2.getIndex() );
        assertEquals( 3,
                      uiModelColumn2_2.getIndex() );
        assertEquals( "metadata1",
                      uiModel.getRow( 0 ).getCells().get( uiModelColumn1_2.getIndex() ).getValue().getValue() );
        assertEquals( "metadata2",
                      uiModel.getRow( 0 ).getCells().get( uiModelColumn2_2.getIndex() ).getValue().getValue() );
    }

    @Test
    public void testMoveColumnsTo_MoveLeft() throws ModelSynchronizer.MoveColumnVetoException {
        final MetadataCol52 column1 = new MetadataCol52();
        column1.setMetadata( "metadata1" );
        final MetadataCol52 column2 = new MetadataCol52();
        column2.setMetadata( "metadata2" );
        final MetadataCol52 column3 = new MetadataCol52();
        column3.setMetadata( "metadata3" );

        modelSynchronizer.appendColumn( column1 );
        modelSynchronizer.appendColumn( column2 );
        modelSynchronizer.appendColumn( column3 );

        modelSynchronizer.appendRow();
        uiModel.setCell( 0,
                         2,
                         new BaseGridCellValue<String>( "metadata1" ) );
        uiModel.setCell( 0,
                         3,
                         new BaseGridCellValue<String>( "metadata2" ) );
        uiModel.setCell( 0,
                         4,
                         new BaseGridCellValue<String>( "metadata3" ) );

        final IGridColumn<?> uiModelColumn1_1 = uiModel.getColumns().get( 2 );
        final IGridColumn<?> uiModelColumn2_1 = uiModel.getColumns().get( 3 );
        final IGridColumn<?> uiModelColumn3_1 = uiModel.getColumns().get( 4 );

        assertTestMoveColumnsTo( column1,
                                 column2,
                                 column3,
                                 uiModelColumn1_1,
                                 uiModelColumn2_1,
                                 uiModelColumn3_1 );

        //Moving multiple MetaData columns as an unsupported operation as it's impossible via the UI
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
        final MetadataCol52 column1 = new MetadataCol52();
        column1.setMetadata( "metadata1" );
        final MetadataCol52 column2 = new MetadataCol52();
        column2.setMetadata( "metadata2" );
        final MetadataCol52 column3 = new MetadataCol52();
        column3.setMetadata( "metadata3" );

        modelSynchronizer.appendColumn( column1 );
        modelSynchronizer.appendColumn( column2 );
        modelSynchronizer.appendColumn( column3 );

        modelSynchronizer.appendRow();
        uiModel.setCell( 0,
                         2,
                         new BaseGridCellValue<String>( "metadata1" ) );
        uiModel.setCell( 0,
                         3,
                         new BaseGridCellValue<String>( "metadata2" ) );
        uiModel.setCell( 0,
                         4,
                         new BaseGridCellValue<String>( "metadata3" ) );

        final IGridColumn<?> uiModelColumn1_1 = uiModel.getColumns().get( 2 );
        final IGridColumn<?> uiModelColumn2_1 = uiModel.getColumns().get( 3 );
        final IGridColumn<?> uiModelColumn3_1 = uiModel.getColumns().get( 4 );

        assertTestMoveColumnsTo( column1,
                                 column2,
                                 column3,
                                 uiModelColumn1_1,
                                 uiModelColumn2_1,
                                 uiModelColumn3_1 );

        //Moving multiple MetaData columns as an unsupported operation as it's impossible via the UI
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

    private void assertTestMoveColumnsTo( final MetadataCol52 column1,
                                          final MetadataCol52 column2,
                                          final MetadataCol52 column3,
                                          final IGridColumn<?> uiModelColumn1,
                                          final IGridColumn<?> uiModelColumn2,
                                          final IGridColumn<?> uiModelColumn3 ) {
        assertEquals( 3,
                      model.getMetadataCols().size() );
        assertEquals( column1,
                      model.getMetadataCols().get( 0 ) );
        assertEquals( column2,
                      model.getMetadataCols().get( 1 ) );
        assertEquals( column3,
                      model.getMetadataCols().get( 2 ) );
        assertEquals( "metadata1",
                      model.getData().get( 0 ).get( 2 ).getStringValue() );
        assertEquals( "metadata2",
                      model.getData().get( 0 ).get( 3 ).getStringValue() );
        assertEquals( "metadata3",
                      model.getData().get( 0 ).get( 4 ).getStringValue() );

        assertEquals( 5,
                      uiModel.getColumns().size() );
        assertEquals( "metadata1",
                      uiModelColumn1.getHeaderMetaData().get( 0 ).getTitle() );
        assertEquals( "metadata2",
                      uiModelColumn2.getHeaderMetaData().get( 0 ).getTitle() );
        assertEquals( "metadata3",
                      uiModelColumn3.getHeaderMetaData().get( 0 ).getTitle() );
        assertTrue( uiModelColumn1 instanceof StringUiColumn );
        assertTrue( uiModelColumn2 instanceof StringUiColumn );
        assertTrue( uiModelColumn3 instanceof StringUiColumn );
        assertEquals( 2,
                      uiModelColumn1.getIndex() );
        assertEquals( 3,
                      uiModelColumn2.getIndex() );
        assertEquals( 4,
                      uiModelColumn3.getIndex() );
        assertEquals( "metadata1",
                      uiModel.getRow( 0 ).getCells().get( uiModelColumn1.getIndex() ).getValue().getValue() );
        assertEquals( "metadata2",
                      uiModel.getRow( 0 ).getCells().get( uiModelColumn2.getIndex() ).getValue().getValue() );
        assertEquals( "metadata3",
                      uiModel.getRow( 0 ).getCells().get( uiModelColumn3.getIndex() ).getValue().getValue() );
    }

}
