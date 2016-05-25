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
import java.util.HashMap;
import java.util.List;

import org.drools.workbench.models.datamodel.oracle.DataType;
import org.drools.workbench.models.datamodel.oracle.FieldAccessorsAndMutators;
import org.drools.workbench.models.datamodel.oracle.ModelField;
import org.drools.workbench.models.guided.dtable.shared.model.ActionInsertFactCol52;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.BaseUiSingletonColumn;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.IntegerUiColumn;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.StringUiColumn;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.converters.column.BaseColumnConverter;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.converters.column.impl.ActionInsertFactColumnConverter;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.synchronizers.ModelSynchronizer;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.synchronizers.Synchronizer;
import org.junit.Test;
import org.kie.workbench.common.widgets.client.datamodel.AsyncPackageDataModelOracle;
import org.kie.workbench.common.widgets.client.datamodel.AsyncPackageDataModelOracleImpl;
import org.uberfire.ext.wires.core.grids.client.model.BaseGridCellValue;
import org.uberfire.ext.wires.core.grids.client.model.IGridColumn;

import static org.drools.workbench.screens.guided.dtable.client.widget.table.model.synchronizers.Synchronizer.*;
import static org.junit.Assert.*;

public class ActionInsertFactColumnSynchronizerTest extends BaseSynchronizerTest {

    @Override
    protected AsyncPackageDataModelOracle getOracle() {
        final AsyncPackageDataModelOracle oracle = new AsyncPackageDataModelOracleImpl();
        oracle.addModelFields( new HashMap<String, ModelField[]>() {
                                   {
                                       put( "Applicant",
                                            new ModelField[]{
                                                    new ModelField( "this",
                                                                    "Applicant",
                                                                    ModelField.FIELD_CLASS_TYPE.REGULAR_CLASS,
                                                                    ModelField.FIELD_ORIGIN.SELF,
                                                                    FieldAccessorsAndMutators.ACCESSOR,
                                                                    "Applicant" ),
                                                    new ModelField( "age",
                                                                    Integer.class.getName(),
                                                                    ModelField.FIELD_CLASS_TYPE.REGULAR_CLASS,
                                                                    ModelField.FIELD_ORIGIN.SELF,
                                                                    FieldAccessorsAndMutators.ACCESSOR,
                                                                    DataType.TYPE_NUMERIC_INTEGER ),
                                                    new ModelField( "name",
                                                                    String.class.getName(),
                                                                    ModelField.FIELD_CLASS_TYPE.REGULAR_CLASS,
                                                                    ModelField.FIELD_ORIGIN.SELF,
                                                                    FieldAccessorsAndMutators.ACCESSOR,
                                                                    DataType.TYPE_STRING ) } );
                                   }
                               }

                             );
        return oracle;
    }

    @Override
    protected List<BaseColumnConverter> getConverters() {
        final List<BaseColumnConverter> converters = new ArrayList<BaseColumnConverter>();
        converters.add( new ActionInsertFactColumnConverter() );
        return converters;
    }

    @Override
    protected List<Synchronizer<? extends MetaData, ? extends MetaData, ? extends MetaData, ? extends MetaData, ? extends MetaData>> getSynchronizers() {
        final List<Synchronizer<? extends MetaData, ? extends MetaData, ? extends MetaData, ? extends MetaData, ? extends MetaData>> synchronizers = new ArrayList<>();
        synchronizers.add( new ActionInsertFactColumnSynchronizer() );
        synchronizers.add( new RowSynchronizer() );
        return synchronizers;
    }

    @Test
    public void testAppend() throws ModelSynchronizer.MoveColumnVetoException {
        final ActionInsertFactCol52 column = new ActionInsertFactCol52();
        column.setHeader( "col1" );
        column.setBoundName( "$a" );
        column.setFactType( "Applicant" );
        column.setFactField( "age" );

        modelSynchronizer.appendColumn( column );

        assertEquals( 1,
                      model.getActionCols().size() );

        assertEquals( 3,
                      uiModel.getColumns().size() );
        assertTrue( uiModel.getColumns().get( 2 ) instanceof IntegerUiColumn );
        assertEquals( true,
                      ( (BaseUiSingletonColumn) uiModel.getColumns().get( 2 ) ).isEditable() );
    }

    @Test
    public void testUpdate() throws ModelSynchronizer.MoveColumnVetoException {
        final ActionInsertFactCol52 column = new ActionInsertFactCol52();
        column.setHeader( "col1" );
        column.setBoundName( "$a" );
        column.setFactType( "Applicant" );
        column.setFactField( "age" );

        modelSynchronizer.appendColumn( column );

        final ActionInsertFactCol52 edited = new ActionInsertFactCol52();
        edited.setBoundName( "$a" );
        edited.setFactType( "Applicant" );
        edited.setFactField( "name" );
        edited.setHideColumn( true );
        edited.setHeader( "updated" );

        modelSynchronizer.updateColumn( column,
                                        edited );

        assertEquals( 1,
                      model.getActionCols().size() );

        assertEquals( 3,
                      uiModel.getColumns().size() );
        assertTrue( uiModel.getColumns().get( 2 ) instanceof StringUiColumn );
        assertEquals( "updated",
                      uiModel.getColumns().get( 2 ).getHeaderMetaData().get( 0 ).getTitle() );
        assertEquals( false,
                      uiModel.getColumns().get( 2 ).isVisible() );
    }

    @Test
    public void testDelete() throws ModelSynchronizer.MoveColumnVetoException {
        final ActionInsertFactCol52 column = new ActionInsertFactCol52();
        column.setHeader( "col1" );
        column.setBoundName( "$a" );
        column.setFactType( "Applicant" );
        column.setFactField( "age" );

        modelSynchronizer.appendColumn( column );

        assertEquals( 1,
                      model.getActionCols().size() );
        assertEquals( 3,
                      uiModel.getColumns().size() );

        modelSynchronizer.deleteColumn( column );
        assertEquals( 0,
                      model.getActionCols().size() );
        assertEquals( 2,
                      uiModel.getColumns().size() );
    }

    @Test
    public void testMoveColumnTo_MoveLeft() throws ModelSynchronizer.MoveColumnVetoException {
        final ActionInsertFactCol52 column1 = new ActionInsertFactCol52();
        column1.setBoundName( "$a" );
        column1.setFactType( "Applicant" );
        column1.setFactField( "age" );
        column1.setHeader( "age" );
        final ActionInsertFactCol52 column2 = new ActionInsertFactCol52();
        column2.setBoundName( "$a" );
        column2.setFactType( "Applicant" );
        column2.setFactField( "name" );
        column2.setHeader( "name" );

        modelSynchronizer.appendColumn( column1 );
        modelSynchronizer.appendColumn( column2 );

        modelSynchronizer.appendRow();
        uiModel.setCell( 0,
                         2,
                         new BaseGridCellValue<Integer>( 45 ) );
        uiModel.setCell( 0,
                         3,
                         new BaseGridCellValue<String>( "Smurf" ) );

        assertEquals( 2,
                      model.getActionCols().size() );
        assertEquals( column1,
                      model.getActionCols().get( 0 ) );
        assertEquals( column2,
                      model.getActionCols().get( 1 ) );
        assertEquals( 45,
                      model.getData().get( 0 ).get( 2 ).getNumericValue() );
        assertEquals( "Smurf",
                      model.getData().get( 0 ).get( 3 ).getStringValue() );

        assertEquals( 4,
                      uiModel.getColumns().size() );
        final IGridColumn<?> uiModelColumn1_1 = uiModel.getColumns().get( 2 );
        final IGridColumn<?> uiModelColumn2_1 = uiModel.getColumns().get( 3 );
        assertEquals( "age",
                      uiModelColumn1_1.getHeaderMetaData().get( 0 ).getTitle() );
        assertEquals( "name",
                      uiModelColumn2_1.getHeaderMetaData().get( 0 ).getTitle() );
        assertTrue( uiModelColumn1_1 instanceof IntegerUiColumn );
        assertTrue( uiModelColumn2_1 instanceof StringUiColumn );
        assertEquals( 2,
                      uiModelColumn1_1.getIndex() );
        assertEquals( 3,
                      uiModelColumn2_1.getIndex() );
        assertEquals( 45,
                      uiModel.getRow( 0 ).getCells().get( uiModelColumn1_1.getIndex() ).getValue().getValue() );
        assertEquals( "Smurf",
                      uiModel.getRow( 0 ).getCells().get( uiModelColumn2_1.getIndex() ).getValue().getValue() );

        uiModel.moveColumnTo( 2,
                              uiModelColumn2_1 );

        assertEquals( 2,
                      model.getActionCols().size() );
        assertEquals( column2,
                      model.getActionCols().get( 0 ) );
        assertEquals( column1,
                      model.getActionCols().get( 1 ) );
        assertEquals( "Smurf",
                      model.getData().get( 0 ).get( 2 ).getStringValue() );
        assertEquals( 45,
                      model.getData().get( 0 ).get( 3 ).getNumericValue() );

        assertEquals( 4,
                      uiModel.getColumns().size() );
        final IGridColumn<?> uiModelColumn1_2 = uiModel.getColumns().get( 2 );
        final IGridColumn<?> uiModelColumn2_2 = uiModel.getColumns().get( 3 );
        assertEquals( "name",
                      uiModelColumn1_2.getHeaderMetaData().get( 0 ).getTitle() );
        assertEquals( "age",
                      uiModelColumn2_2.getHeaderMetaData().get( 0 ).getTitle() );
        assertTrue( uiModelColumn1_2 instanceof StringUiColumn );
        assertTrue( uiModelColumn2_2 instanceof IntegerUiColumn );
        assertEquals( 3,
                      uiModelColumn1_2.getIndex() );
        assertEquals( 2,
                      uiModelColumn2_2.getIndex() );
        assertEquals( "Smurf",
                      uiModel.getRow( 0 ).getCells().get( uiModelColumn1_2.getIndex() ).getValue().getValue() );
        assertEquals( 45,
                      uiModel.getRow( 0 ).getCells().get( uiModelColumn2_2.getIndex() ).getValue().getValue() );
    }

    @Test
    public void testMoveColumnTo_MoveRight() throws ModelSynchronizer.MoveColumnVetoException {
        final ActionInsertFactCol52 column1 = new ActionInsertFactCol52();
        column1.setBoundName( "$a" );
        column1.setFactType( "Applicant" );
        column1.setFactField( "age" );
        column1.setHeader( "age" );
        final ActionInsertFactCol52 column2 = new ActionInsertFactCol52();
        column2.setBoundName( "$a" );
        column2.setFactType( "Applicant" );
        column2.setFactField( "name" );
        column2.setHeader( "name" );

        modelSynchronizer.appendColumn( column1 );
        modelSynchronizer.appendColumn( column2 );

        modelSynchronizer.appendRow();
        uiModel.setCell( 0,
                         2,
                         new BaseGridCellValue<Integer>( 45 ) );
        uiModel.setCell( 0,
                         3,
                         new BaseGridCellValue<String>( "Smurf" ) );

        assertEquals( 2,
                      model.getActionCols().size() );
        assertEquals( column1,
                      model.getActionCols().get( 0 ) );
        assertEquals( column2,
                      model.getActionCols().get( 1 ) );
        assertEquals( 45,
                      model.getData().get( 0 ).get( 2 ).getNumericValue() );
        assertEquals( "Smurf",
                      model.getData().get( 0 ).get( 3 ).getStringValue() );

        assertEquals( 4,
                      uiModel.getColumns().size() );
        final IGridColumn<?> uiModelColumn1_1 = uiModel.getColumns().get( 2 );
        final IGridColumn<?> uiModelColumn2_1 = uiModel.getColumns().get( 3 );
        assertEquals( "age",
                      uiModelColumn1_1.getHeaderMetaData().get( 0 ).getTitle() );
        assertEquals( "name",
                      uiModelColumn2_1.getHeaderMetaData().get( 0 ).getTitle() );
        assertTrue( uiModelColumn1_1 instanceof IntegerUiColumn );
        assertTrue( uiModelColumn2_1 instanceof StringUiColumn );
        assertEquals( 2,
                      uiModelColumn1_1.getIndex() );
        assertEquals( 3,
                      uiModelColumn2_1.getIndex() );
        assertEquals( 45,
                      uiModel.getRow( 0 ).getCells().get( uiModelColumn1_1.getIndex() ).getValue().getValue() );
        assertEquals( "Smurf",
                      uiModel.getRow( 0 ).getCells().get( uiModelColumn2_1.getIndex() ).getValue().getValue() );

        uiModel.moveColumnTo( 3,
                              uiModelColumn1_1 );

        assertEquals( 2,
                      model.getActionCols().size() );
        assertEquals( column2,
                      model.getActionCols().get( 0 ) );
        assertEquals( column1,
                      model.getActionCols().get( 1 ) );
        assertEquals( "Smurf",
                      model.getData().get( 0 ).get( 2 ).getStringValue() );
        assertEquals( 45,
                      model.getData().get( 0 ).get( 3 ).getNumericValue() );

        assertEquals( 4,
                      uiModel.getColumns().size() );
        final IGridColumn<?> uiModelColumn1_2 = uiModel.getColumns().get( 2 );
        final IGridColumn<?> uiModelColumn2_2 = uiModel.getColumns().get( 3 );
        assertEquals( "name",
                      uiModelColumn1_2.getHeaderMetaData().get( 0 ).getTitle() );
        assertEquals( "age",
                      uiModelColumn2_2.getHeaderMetaData().get( 0 ).getTitle() );
        assertTrue( uiModelColumn1_2 instanceof StringUiColumn );
        assertTrue( uiModelColumn2_2 instanceof IntegerUiColumn );
        assertEquals( 3,
                      uiModelColumn1_2.getIndex() );
        assertEquals( 2,
                      uiModelColumn2_2.getIndex() );
        assertEquals( "Smurf",
                      uiModel.getRow( 0 ).getCells().get( uiModelColumn1_2.getIndex() ).getValue().getValue() );
        assertEquals( 45,
                      uiModel.getRow( 0 ).getCells().get( uiModelColumn2_2.getIndex() ).getValue().getValue() );
    }

    @Test
    public void testMoveColumnTo_OutOfBounds() throws ModelSynchronizer.MoveColumnVetoException {
        final ActionInsertFactCol52 column1 = new ActionInsertFactCol52();
        column1.setBoundName( "$a" );
        column1.setFactType( "Applicant" );
        column1.setFactField( "age" );
        column1.setHeader( "age" );
        final ActionInsertFactCol52 column2 = new ActionInsertFactCol52();
        column2.setBoundName( "$a" );
        column2.setFactType( "Applicant" );
        column2.setFactField( "name" );
        column2.setHeader( "name" );

        modelSynchronizer.appendColumn( column1 );
        modelSynchronizer.appendColumn( column2 );

        modelSynchronizer.appendRow();
        uiModel.setCell( 0,
                         2,
                         new BaseGridCellValue<Integer>( 45 ) );
        uiModel.setCell( 0,
                         3,
                         new BaseGridCellValue<String>( "Smurf" ) );

        assertEquals( 2,
                      model.getActionCols().size() );
        assertEquals( column1,
                      model.getActionCols().get( 0 ) );
        assertEquals( column2,
                      model.getActionCols().get( 1 ) );
        assertEquals( 45,
                      model.getData().get( 0 ).get( 2 ).getNumericValue() );
        assertEquals( "Smurf",
                      model.getData().get( 0 ).get( 3 ).getStringValue() );

        assertEquals( 4,
                      uiModel.getColumns().size() );
        final IGridColumn<?> uiModelColumn1_1 = uiModel.getColumns().get( 2 );
        final IGridColumn<?> uiModelColumn2_1 = uiModel.getColumns().get( 3 );
        assertEquals( "age",
                      uiModelColumn1_1.getHeaderMetaData().get( 0 ).getTitle() );
        assertEquals( "name",
                      uiModelColumn2_1.getHeaderMetaData().get( 0 ).getTitle() );
        assertTrue( uiModelColumn1_1 instanceof IntegerUiColumn );
        assertTrue( uiModelColumn2_1 instanceof StringUiColumn );
        assertEquals( 2,
                      uiModelColumn1_1.getIndex() );
        assertEquals( 3,
                      uiModelColumn2_1.getIndex() );
        assertEquals( 45,
                      uiModel.getRow( 0 ).getCells().get( uiModelColumn1_1.getIndex() ).getValue().getValue() );
        assertEquals( "Smurf",
                      uiModel.getRow( 0 ).getCells().get( uiModelColumn2_1.getIndex() ).getValue().getValue() );

        uiModel.moveColumnTo( 0,
                              uiModelColumn1_1 );

        assertEquals( 2,
                      model.getActionCols().size() );
        assertEquals( column1,
                      model.getActionCols().get( 0 ) );
        assertEquals( column2,
                      model.getActionCols().get( 1 ) );
        assertEquals( 45,
                      model.getData().get( 0 ).get( 2 ).getNumericValue() );
        assertEquals( "Smurf",
                      model.getData().get( 0 ).get( 3 ).getStringValue() );

        assertEquals( 4,
                      uiModel.getColumns().size() );
        final IGridColumn<?> uiModelColumn1_2 = uiModel.getColumns().get( 2 );
        final IGridColumn<?> uiModelColumn2_2 = uiModel.getColumns().get( 3 );
        assertEquals( "age",
                      uiModelColumn1_2.getHeaderMetaData().get( 0 ).getTitle() );
        assertEquals( "name",
                      uiModelColumn2_2.getHeaderMetaData().get( 0 ).getTitle() );
        assertTrue( uiModelColumn1_2 instanceof IntegerUiColumn );
        assertTrue( uiModelColumn2_2 instanceof StringUiColumn );
        assertEquals( 2,
                      uiModelColumn1_2.getIndex() );
        assertEquals( 3,
                      uiModelColumn2_2.getIndex() );
        assertEquals( 45,
                      uiModel.getRow( 0 ).getCells().get( uiModelColumn1_2.getIndex() ).getValue().getValue() );
        assertEquals( "Smurf",
                      uiModel.getRow( 0 ).getCells().get( uiModelColumn2_2.getIndex() ).getValue().getValue() );
    }

}
