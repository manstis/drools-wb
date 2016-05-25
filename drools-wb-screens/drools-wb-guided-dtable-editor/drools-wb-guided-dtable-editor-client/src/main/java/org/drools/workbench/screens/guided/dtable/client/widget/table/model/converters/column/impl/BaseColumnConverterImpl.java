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
package org.drools.workbench.screens.guided.dtable.client.widget.table.model.converters.column.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.ListBox;
import org.drools.workbench.models.datamodel.oracle.DataType;
import org.drools.workbench.models.datamodel.oracle.OperatorsOracle;
import org.drools.workbench.models.guided.dtable.shared.model.ActionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.BaseColumn;
import org.drools.workbench.models.guided.dtable.shared.model.ConditionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTablePresenter;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.BigDecimalUiColumn;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.BigIntegerUiColumn;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.BooleanUiColumn;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.ByteUiColumn;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.DateUiColumn;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.DoubleUiColumn;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.EnumMultiSelectUiColumn;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.EnumSingleSelectBigDecimalUiColumn;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.EnumSingleSelectBigIntegerUiColumn;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.EnumSingleSelectByteUiColumn;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.EnumSingleSelectDateUiColumn;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.EnumSingleSelectDoubleUiColumn;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.EnumSingleSelectFloatUiColumn;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.EnumSingleSelectIntegerUiColumn;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.EnumSingleSelectLongUiColumn;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.EnumSingleSelectNumericUiColumn;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.EnumSingleSelectShortUiColumn;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.EnumSingleSelectStringUiColumn;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.FloatUiColumn;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.IntegerUiColumn;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.LongUiColumn;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.SalienceUiColumn;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.ShortUiColumn;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.StringUiColumn;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.ValueListUiColumn;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.datepicker.DatePickerSingletonDOMElementFactory;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.listbox.ListBoxBigDecimalSingletonDOMElementFactory;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.listbox.ListBoxBigIntegerSingletonDOMElementFactory;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.listbox.ListBoxByteSingletonDOMElementFactory;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.listbox.ListBoxDOMElement;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.listbox.ListBoxDateSingletonDOMElementFactory;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.listbox.ListBoxDoubleSingletonDOMElementFactory;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.listbox.ListBoxFloatSingletonDOMElementFactory;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.listbox.ListBoxIntegerSingletonDOMElementFactory;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.listbox.ListBoxLongSingletonDOMElementFactory;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.listbox.ListBoxNumericSingletonDOMElementFactory;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.listbox.ListBoxShortSingletonDOMElementFactory;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.listbox.ListBoxSingletonDOMElementFactory;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.listbox.ListBoxStringSingletonDOMElementFactory;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.textbox.TextBoxBigDecimalSingletonDOMElementFactory;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.textbox.TextBoxBigIntegerSingletonDOMElementFactory;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.textbox.TextBoxByteSingletonDOMElementFactory;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.textbox.TextBoxDoubleSingletonDOMElementFactory;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.textbox.TextBoxFloatSingletonDOMElementFactory;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.textbox.TextBoxIntegerSingletonDOMElementFactory;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.textbox.TextBoxLongSingletonDOMElementFactory;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.textbox.TextBoxNumericSingletonDOMElementFactory;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.textbox.TextBoxShortSingletonDOMElementFactory;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.textbox.TextBoxStringSingletonDOMElementFactory;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.converters.column.BaseColumnConverter;
import org.drools.workbench.screens.guided.dtable.client.widget.table.utilities.ColumnUtilities;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.kie.workbench.common.widgets.client.datamodel.AsyncPackageDataModelOracle;
import org.uberfire.commons.validation.PortablePreconditions;
import org.uberfire.ext.wires.core.grids.client.model.IGridCell;
import org.uberfire.ext.wires.core.grids.client.model.IGridColumn;
import org.uberfire.ext.wires.core.grids.client.widget.IBaseGridWidget;
import org.uberfire.ext.wires.core.grids.client.widget.IGridLayer;
import org.uberfire.ext.wires.core.grids.client.widget.context.GridBodyCellRenderContext;
import org.uberfire.ext.wires.core.grids.client.widget.dom.multiple.CheckBoxDOMElementFactory;

/**
 * Generic Handler for different BaseUiColumn types
 */
public abstract class BaseColumnConverterImpl implements BaseColumnConverter {

    protected static final int DEFAULT_COLUMN_WIDTH = 100;

    protected GuidedDecisionTable52 model;
    protected AsyncPackageDataModelOracle oracle;
    protected ColumnUtilities columnUtilities;
    protected GuidedDecisionTableView.Presenter presenter;
    protected IGridLayer gridLayer;

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public void initialise( final GuidedDecisionTable52 model,
                            final AsyncPackageDataModelOracle oracle,
                            final ColumnUtilities columnUtilities,
                            final GuidedDecisionTableView.Presenter presenter ) {
        this.model = PortablePreconditions.checkNotNull( "model",
                                                         model );
        this.oracle = PortablePreconditions.checkNotNull( "oracle",
                                                          oracle );
        this.columnUtilities = PortablePreconditions.checkNotNull( "columnUtilities",
                                                                   columnUtilities );
        this.presenter = PortablePreconditions.checkNotNull( "presenter",
                                                             presenter );
        this.gridLayer = presenter.getModellerPresenter().getView().getGridLayerView();
    }

    protected IGridColumn<?> newColumn( final BaseColumn column,
                                        final GuidedDecisionTablePresenter.Access access,
                                        final GuidedDecisionTableView gridWidget ) {
        //Get a column based upon the data-type
        final String type = columnUtilities.getType( column );

        if ( type.equals( DataType.TYPE_NUMERIC ) ) {
            return newNumericColumn( makeHeaderMetaData( column ),
                                     Math.max( column.getWidth(),
                                               DEFAULT_COLUMN_WIDTH ),
                                     true,
                                     !column.isHideColumn(),
                                     access,
                                     gridWidget );

        } else if ( type.equals( DataType.TYPE_NUMERIC_BIGDECIMAL ) ) {
            return newBigDecimalColumn( makeHeaderMetaData( column ),
                                        Math.max( column.getWidth(),
                                                  DEFAULT_COLUMN_WIDTH ),
                                        true,
                                        !column.isHideColumn(),
                                        access,
                                        gridWidget );

        } else if ( type.equals( DataType.TYPE_NUMERIC_BIGINTEGER ) ) {
            return newBigIntegerColumn( makeHeaderMetaData( column ),
                                        Math.max( column.getWidth(),
                                                  DEFAULT_COLUMN_WIDTH ),
                                        true,
                                        !column.isHideColumn(),
                                        access,
                                        gridWidget );

        } else if ( type.equals( DataType.TYPE_NUMERIC_BYTE ) ) {
            return newByteColumn( makeHeaderMetaData( column ),
                                  Math.max( column.getWidth(),
                                            DEFAULT_COLUMN_WIDTH ),
                                  true,
                                  !column.isHideColumn(),
                                  access,
                                  gridWidget );

        } else if ( type.equals( DataType.TYPE_NUMERIC_DOUBLE ) ) {
            return newDoubleColumn( makeHeaderMetaData( column ),
                                    Math.max( column.getWidth(),
                                              DEFAULT_COLUMN_WIDTH ),
                                    true,
                                    !column.isHideColumn(),
                                    access,
                                    gridWidget );

        } else if ( type.equals( DataType.TYPE_NUMERIC_FLOAT ) ) {
            return newFloatColumn( makeHeaderMetaData( column ),
                                   Math.max( column.getWidth(),
                                             DEFAULT_COLUMN_WIDTH ),
                                   true,
                                   !column.isHideColumn(),
                                   access,
                                   gridWidget );

        } else if ( type.equals( DataType.TYPE_NUMERIC_INTEGER ) ) {
            return newIntegerColumn( makeHeaderMetaData( column ),
                                     Math.max( column.getWidth(),
                                               DEFAULT_COLUMN_WIDTH ),
                                     true,
                                     !column.isHideColumn(),
                                     access,
                                     gridWidget );

        } else if ( type.equals( DataType.TYPE_NUMERIC_LONG ) ) {
            return newLongColumn( makeHeaderMetaData( column ),
                                  Math.max( column.getWidth(),
                                            DEFAULT_COLUMN_WIDTH ),
                                  true,
                                  !column.isHideColumn(),
                                  access,
                                  gridWidget );

        } else if ( type.equals( DataType.TYPE_NUMERIC_SHORT ) ) {
            return newShortColumn( makeHeaderMetaData( column ),
                                   Math.max( column.getWidth(),
                                             DEFAULT_COLUMN_WIDTH ),
                                   true,
                                   !column.isHideColumn(),
                                   access,
                                   gridWidget );

        } else if ( type.equals( DataType.TYPE_BOOLEAN ) ) {
            return newBooleanColumn( makeHeaderMetaData( column ),
                                     Math.max( column.getWidth(),
                                               DEFAULT_COLUMN_WIDTH ),
                                     true,
                                     !column.isHideColumn(),
                                     access,
                                     gridWidget );

        } else if ( type.equals( DataType.TYPE_DATE ) ) {
            return newDateColumn( makeHeaderMetaData( column ),
                                  Math.max( column.getWidth(),
                                            DEFAULT_COLUMN_WIDTH ),
                                  true,
                                  !column.isHideColumn(),
                                  access,
                                  gridWidget );

        } else {
            return newStringColumn( makeHeaderMetaData( column ),
                                    Math.max( column.getWidth(),
                                              DEFAULT_COLUMN_WIDTH ),
                                    true,
                                    !column.isHideColumn(),
                                    access,
                                    gridWidget );
        }
    }

    protected IGridColumn<?> newValueListColumn( final ConditionCol52 column,
                                                 final GuidedDecisionTablePresenter.Access access,
                                                 final GuidedDecisionTableView gridWidget ) {
        final boolean isMultipleSelect = OperatorsOracle.operatorRequiresList( column.getOperator() );
        return new ValueListUiColumn( makeHeaderMetaData( column ),
                                      Math.max( column.getWidth(),
                                                DEFAULT_COLUMN_WIDTH ),
                                      true,
                                      !column.isHideColumn(),
                                      access,
                                      new ListBoxSingletonDOMElementFactory<String, ListBox>( gridLayer,
                                                                                              gridWidget ) {

                                          @Override
                                          public ListBox createWidget() {
                                              final ListBox listBox = new ListBox();
                                              listBox.setMultipleSelect( isMultipleSelect );
                                              return listBox;
                                          }

                                          @Override
                                          public ListBoxDOMElement<String, ListBox> createDomElement( final IGridLayer gridLayer,
                                                                                                      final IBaseGridWidget gridWidget,
                                                                                                      final GridBodyCellRenderContext context ) {
                                              this.widget = createWidget();
                                              this.e = new ListBoxDOMElement<String, ListBox>( widget,
                                                                                               gridLayer,
                                                                                               gridWidget );

                                              widget.addBlurHandler( new BlurHandler() {
                                                  @Override
                                                  public void onBlur( final BlurEvent event ) {
                                                      e.flush( fromWidget( widget ) );
                                                      e.detach();
                                                      gridLayer.batch();
                                                  }
                                              } );
                                              widget.addChangeHandler( new ChangeHandler() {
                                                  @Override
                                                  public void onChange( final ChangeEvent event ) {
                                                      e.flush( fromWidget( widget ) );
                                                      e.detach();
                                                      gridLayer.batch();
                                                  }
                                              } );

                                              return e;
                                          }

                                          @Override
                                          public void toWidget( final IGridCell<String> cell,
                                                                final ListBox widget ) {
                                              if ( cell == null || cell.getValue() == null || cell.getValue().getValue() == null ) {
                                                  if ( widget.getItemCount() > 0 ) {
                                                      widget.setSelectedIndex( 0 );
                                                  }

                                              } else {
                                                  final String value = cell.getValue().getValue();
                                                  if ( isMultipleSelect ) {
                                                      final List<String> values = Arrays.asList( value.split( "," ) );
                                                      for ( int i = 0; i < widget.getItemCount(); i++ ) {
                                                          widget.setItemSelected( i,
                                                                                  values.contains( widget.getValue( i ) ) );
                                                      }

                                                  } else {
                                                      for ( int i = 0; i < widget.getItemCount(); i++ ) {
                                                          if ( widget.getValue( i ).equals( value ) ) {
                                                              widget.setSelectedIndex( i );
                                                              break;
                                                          }
                                                      }
                                                  }
                                              }
                                          }

                                          @Override
                                          public String fromWidget( final ListBox widget ) {
                                              final StringBuilder sb = new StringBuilder();
                                              if ( isMultipleSelect ) {
                                                  for ( int i = 0; i < widget.getItemCount(); i++ ) {
                                                      if ( widget.isItemSelected( i ) ) {
                                                          if ( i == 0 ) {
                                                              sb.append( widget.getValue( i ) );
                                                          } else {
                                                              sb.append( "," ).append( widget.getValue( i ) );
                                                          }
                                                      }
                                                  }

                                              } else {
                                                  int selectedIndex = widget.getSelectedIndex();
                                                  if ( selectedIndex >= 0 ) {
                                                      sb.append( widget.getValue( selectedIndex ) );
                                                  }
                                              }

                                              return sb.toString();
                                          }

                                          @Override
                                          public String convert( final String value ) {
                                              return value;
                                          }

                                      },
                                      presenter.getValueListLookups( column ),
                                      isMultipleSelect );
    }

    protected IGridColumn<?> newValueListColumn( final ActionCol52 column,
                                                 final GuidedDecisionTablePresenter.Access access,
                                                 final GuidedDecisionTableView gridWidget ) {
        return new ValueListUiColumn( makeHeaderMetaData( column ),
                                      Math.max( column.getWidth(),
                                                DEFAULT_COLUMN_WIDTH ),
                                      true,
                                      !column.isHideColumn(),
                                      access,
                                      new ListBoxStringSingletonDOMElementFactory( gridLayer,
                                                                                   gridWidget ),
                                      presenter.getValueListLookups( column ) );
    }

    protected IGridColumn<?> newMultipleSelectEnumColumn( final String factType,
                                                          final String factField,
                                                          final BaseColumn column,
                                                          final GuidedDecisionTablePresenter.Access access,
                                                          final GuidedDecisionTableView gridWidget ) {
        return new EnumMultiSelectUiColumn( makeHeaderMetaData( column ),
                                            Math.max( column.getWidth(),
                                                      DEFAULT_COLUMN_WIDTH ),
                                            true,
                                            !column.isHideColumn(),
                                            access,
                                            new ListBoxSingletonDOMElementFactory<String, ListBox>( gridLayer,
                                                                                                    gridWidget ) {

                                                @Override
                                                public ListBox createWidget() {
                                                    final ListBox listBox = new ListBox();
                                                    listBox.setMultipleSelect( true );
                                                    return listBox;
                                                }

                                                @Override
                                                public ListBoxDOMElement<String, ListBox> createDomElement( final IGridLayer gridLayer,
                                                                                                            final IBaseGridWidget gridWidget,
                                                                                                            final GridBodyCellRenderContext context ) {
                                                    this.widget = createWidget();
                                                    this.e = new ListBoxDOMElement<String, ListBox>( widget,
                                                                                                     gridLayer,
                                                                                                     gridWidget );

                                                    widget.addBlurHandler( new BlurHandler() {
                                                        @Override
                                                        public void onBlur( final BlurEvent event ) {
                                                            e.flush( fromWidget( widget ) );
                                                            e.detach();
                                                            gridLayer.batch();
                                                        }
                                                    } );
                                                    widget.addChangeHandler( new ChangeHandler() {
                                                        @Override
                                                        public void onChange( final ChangeEvent event ) {
                                                            e.flush( fromWidget( widget ) );
                                                            e.detach();
                                                            gridLayer.batch();
                                                        }
                                                    } );

                                                    return e;
                                                }

                                                @Override
                                                public String convert( final String value ) {
                                                    return value;
                                                }

                                                @Override
                                                public void toWidget( final IGridCell<String> cell,
                                                                      final ListBox widget ) {
                                                    final String value = cell.getValue().getValue();
                                                    if ( value == null ) {
                                                        return;
                                                    }
                                                    final List<String> values = Arrays.asList( value.split( "," ) );
                                                    for ( int i = 0; i < widget.getItemCount(); i++ ) {
                                                        widget.setItemSelected( i,
                                                                                values.contains( widget.getValue( i ) ) );
                                                    }
                                                }

                                                @Override
                                                public String fromWidget( final ListBox widget ) {
                                                    final StringBuilder sb = new StringBuilder();
                                                    for ( int i = 0; i < widget.getItemCount(); i++ ) {
                                                        if ( widget.isItemSelected( i ) ) {
                                                            if ( i == 0 ) {
                                                                sb.append( widget.getValue( i ) );
                                                            } else {
                                                                sb.append( "," ).append( widget.getValue( i ) );
                                                            }
                                                        }
                                                    }
                                                    return sb.toString();
                                                }
                                            },
                                            presenter,
                                            factType,
                                            factField );
    }

    protected IGridColumn<?> newSingleSelectionEnumColumn( final String factType,
                                                           final String factField,
                                                           final DataType.DataTypes dataType,
                                                           final BaseColumn column,
                                                           final GuidedDecisionTablePresenter.Access access,
                                                           final GuidedDecisionTableView gridWidget ) {
        if ( dataType.equals( DataType.DataTypes.NUMERIC ) ) {
            return new EnumSingleSelectNumericUiColumn( makeHeaderMetaData( column ),
                                                        Math.max( column.getWidth(),
                                                                  DEFAULT_COLUMN_WIDTH ),
                                                        true,
                                                        !column.isHideColumn(),
                                                        access,
                                                        new ListBoxNumericSingletonDOMElementFactory( gridLayer,
                                                                                                      gridWidget ),
                                                        new TextBoxNumericSingletonDOMElementFactory( gridLayer,
                                                                                                      gridWidget ),
                                                        presenter,
                                                        factType,
                                                        factField );

        } else if ( dataType.equals( DataType.DataTypes.NUMERIC_BIGDECIMAL ) ) {
            return new EnumSingleSelectBigDecimalUiColumn( makeHeaderMetaData( column ),
                                                           Math.max( column.getWidth(),
                                                                     DEFAULT_COLUMN_WIDTH ),
                                                           true,
                                                           !column.isHideColumn(),
                                                           access,
                                                           new ListBoxBigDecimalSingletonDOMElementFactory( gridLayer,
                                                                                                            gridWidget ),
                                                           new TextBoxBigDecimalSingletonDOMElementFactory( gridLayer,
                                                                                                            gridWidget ),

                                                           presenter,
                                                           factType,
                                                           factField );

        } else if ( dataType.equals( DataType.DataTypes.NUMERIC_BIGINTEGER ) ) {
            return new EnumSingleSelectBigIntegerUiColumn( makeHeaderMetaData( column ),
                                                           Math.max( column.getWidth(),
                                                                     DEFAULT_COLUMN_WIDTH ),
                                                           true,
                                                           !column.isHideColumn(),
                                                           access,
                                                           new ListBoxBigIntegerSingletonDOMElementFactory( gridLayer,
                                                                                                            gridWidget ),
                                                           new TextBoxBigIntegerSingletonDOMElementFactory( gridLayer,
                                                                                                            gridWidget ),
                                                           presenter,
                                                           factType,
                                                           factField );

        } else if ( dataType.equals( DataType.DataTypes.NUMERIC_BYTE ) ) {
            return new EnumSingleSelectByteUiColumn( makeHeaderMetaData( column ),
                                                     Math.max( column.getWidth(),
                                                               DEFAULT_COLUMN_WIDTH ),
                                                     true,
                                                     !column.isHideColumn(),
                                                     access,
                                                     new ListBoxByteSingletonDOMElementFactory( gridLayer,
                                                                                                gridWidget ),
                                                     new TextBoxByteSingletonDOMElementFactory( gridLayer,
                                                                                                gridWidget ),
                                                     presenter,
                                                     factType,
                                                     factField );

        } else if ( dataType.equals( DataType.DataTypes.NUMERIC_DOUBLE ) ) {
            return new EnumSingleSelectDoubleUiColumn( makeHeaderMetaData( column ),
                                                       Math.max( column.getWidth(),
                                                                 DEFAULT_COLUMN_WIDTH ),
                                                       true,
                                                       !column.isHideColumn(),
                                                       access,
                                                       new ListBoxDoubleSingletonDOMElementFactory( gridLayer,
                                                                                                    gridWidget ),
                                                       new TextBoxDoubleSingletonDOMElementFactory( gridLayer,
                                                                                                    gridWidget ),
                                                       presenter,
                                                       factType,
                                                       factField );

        } else if ( dataType.equals( DataType.DataTypes.NUMERIC_FLOAT ) ) {
            return new EnumSingleSelectFloatUiColumn( makeHeaderMetaData( column ),
                                                      Math.max( column.getWidth(),
                                                                DEFAULT_COLUMN_WIDTH ),
                                                      true,
                                                      !column.isHideColumn(),
                                                      access,
                                                      new ListBoxFloatSingletonDOMElementFactory( gridLayer,
                                                                                                  gridWidget ),
                                                      new TextBoxFloatSingletonDOMElementFactory( gridLayer,
                                                                                                  gridWidget ),
                                                      presenter,
                                                      factType,
                                                      factField );

        } else if ( dataType.equals( DataType.DataTypes.NUMERIC_INTEGER ) ) {
            return new EnumSingleSelectIntegerUiColumn( makeHeaderMetaData( column ),
                                                        Math.max( column.getWidth(),
                                                                  DEFAULT_COLUMN_WIDTH ),
                                                        true,
                                                        !column.isHideColumn(),
                                                        access,
                                                        new ListBoxIntegerSingletonDOMElementFactory( gridLayer,
                                                                                                      gridWidget ),
                                                        new TextBoxIntegerSingletonDOMElementFactory( gridLayer,
                                                                                                      gridWidget ),
                                                        presenter,
                                                        factType,
                                                        factField );

        } else if ( dataType.equals( DataType.DataTypes.NUMERIC_LONG ) ) {
            return new EnumSingleSelectLongUiColumn( makeHeaderMetaData( column ),
                                                     Math.max( column.getWidth(),
                                                               DEFAULT_COLUMN_WIDTH ),
                                                     true,
                                                     !column.isHideColumn(),
                                                     access,
                                                     new ListBoxLongSingletonDOMElementFactory( gridLayer,
                                                                                                gridWidget ),
                                                     new TextBoxLongSingletonDOMElementFactory( gridLayer,
                                                                                                gridWidget ),
                                                     presenter,
                                                     factType,
                                                     factField );

        } else if ( dataType.equals( DataType.DataTypes.NUMERIC_SHORT ) ) {
            return new EnumSingleSelectShortUiColumn( makeHeaderMetaData( column ),
                                                      Math.max( column.getWidth(),
                                                                DEFAULT_COLUMN_WIDTH ),
                                                      true,
                                                      !column.isHideColumn(),
                                                      access,
                                                      new ListBoxShortSingletonDOMElementFactory( gridLayer,
                                                                                                  gridWidget ),
                                                      new TextBoxShortSingletonDOMElementFactory( gridLayer,
                                                                                                  gridWidget ),
                                                      presenter,
                                                      factType,
                                                      factField );

        } else if ( dataType.equals( DataType.DataTypes.BOOLEAN ) ) {
            return newBooleanColumn( makeHeaderMetaData( column ),
                                     Math.max( column.getWidth(),
                                               DEFAULT_COLUMN_WIDTH ),
                                     true,
                                     !column.isHideColumn(),
                                     access,
                                     gridWidget );

        } else if ( dataType.equals( DataType.DataTypes.DATE ) ) {
            return new EnumSingleSelectDateUiColumn( makeHeaderMetaData( column ),
                                                     Math.max( column.getWidth(),
                                                               DEFAULT_COLUMN_WIDTH ),
                                                     true,
                                                     !column.isHideColumn(),
                                                     access,
                                                     new ListBoxDateSingletonDOMElementFactory( gridLayer,
                                                                                                gridWidget ),
                                                     new DatePickerSingletonDOMElementFactory( gridLayer,
                                                                                               gridWidget ),
                                                     presenter,
                                                     factType,
                                                     factField );

        } else {
            return new EnumSingleSelectStringUiColumn( makeHeaderMetaData( column ),
                                                       Math.max( column.getWidth(),
                                                                 DEFAULT_COLUMN_WIDTH ),
                                                       true,
                                                       !column.isHideColumn(),
                                                       access,
                                                       new ListBoxStringSingletonDOMElementFactory( gridLayer,
                                                                                                    gridWidget ),
                                                       new TextBoxStringSingletonDOMElementFactory( gridLayer,
                                                                                                    gridWidget ),
                                                       presenter,
                                                       factType,
                                                       factField );
        }
    }

    protected IGridColumn<BigDecimal> newNumericColumn( final List<IGridColumn.HeaderMetaData> headerMetaData,
                                                        final double width,
                                                        final boolean isResizable,
                                                        final boolean isVisible,
                                                        final GuidedDecisionTablePresenter.Access access,
                                                        final GuidedDecisionTableView gridWidget ) {
        return new BigDecimalUiColumn( headerMetaData,
                                       width,
                                       isResizable,
                                       isVisible,
                                       access,
                                       new TextBoxBigDecimalSingletonDOMElementFactory( gridLayer,
                                                                                        gridWidget ) );
    }

    protected IGridColumn<BigDecimal> newBigDecimalColumn( final List<IGridColumn.HeaderMetaData> headerMetaData,
                                                           final double width,
                                                           final boolean isResizable,
                                                           final boolean isVisible,
                                                           final GuidedDecisionTablePresenter.Access access,
                                                           final GuidedDecisionTableView gridWidget ) {
        return new BigDecimalUiColumn( headerMetaData,
                                       width,
                                       isResizable,
                                       isVisible,
                                       access,
                                       new TextBoxBigDecimalSingletonDOMElementFactory( gridLayer,
                                                                                        gridWidget ) );
    }

    protected IGridColumn<BigInteger> newBigIntegerColumn( final List<IGridColumn.HeaderMetaData> headerMetaData,
                                                           final double width,
                                                           final boolean isResizable,
                                                           final boolean isVisible,
                                                           final GuidedDecisionTablePresenter.Access access,
                                                           final GuidedDecisionTableView gridWidget ) {
        return new BigIntegerUiColumn( headerMetaData,
                                       width,
                                       isResizable,
                                       isVisible,
                                       access,
                                       new TextBoxBigIntegerSingletonDOMElementFactory( gridLayer,
                                                                                        gridWidget ) );
    }

    protected IGridColumn<Byte> newByteColumn( final List<IGridColumn.HeaderMetaData> headerMetaData,
                                               final double width,
                                               final boolean isResizable,
                                               final boolean isVisible,
                                               final GuidedDecisionTablePresenter.Access access,
                                               final GuidedDecisionTableView gridWidget ) {
        return new ByteUiColumn( headerMetaData,
                                 width,
                                 isResizable,
                                 isVisible,
                                 access,
                                 new TextBoxByteSingletonDOMElementFactory( gridLayer,
                                                                            gridWidget ) );
    }

    protected IGridColumn<Double> newDoubleColumn( final List<IGridColumn.HeaderMetaData> headerMetaData,
                                                   final double width,
                                                   final boolean isResizable,
                                                   final boolean isVisible,
                                                   final GuidedDecisionTablePresenter.Access access,
                                                   final GuidedDecisionTableView gridWidget ) {
        return new DoubleUiColumn( headerMetaData,
                                   width,
                                   isResizable,
                                   isVisible,
                                   access,
                                   new TextBoxDoubleSingletonDOMElementFactory( gridLayer,
                                                                                gridWidget ) );
    }

    protected IGridColumn<Float> newFloatColumn( final List<IGridColumn.HeaderMetaData> headerMetaData,
                                                 final double width,
                                                 final boolean isResizable,
                                                 final boolean isVisible,
                                                 final GuidedDecisionTablePresenter.Access access,
                                                 final GuidedDecisionTableView gridWidget ) {
        return new FloatUiColumn( headerMetaData,
                                  width,
                                  isResizable,
                                  isVisible,
                                  access,
                                  new TextBoxFloatSingletonDOMElementFactory( gridLayer,
                                                                              gridWidget ) {

                                  } );
    }

    protected IGridColumn<Integer> newIntegerColumn( final List<IGridColumn.HeaderMetaData> headerMetaData,
                                                     final double width,
                                                     final boolean isResizable,
                                                     final boolean isVisible,
                                                     final GuidedDecisionTablePresenter.Access access,
                                                     final GuidedDecisionTableView gridWidget ) {
        return new IntegerUiColumn( headerMetaData,
                                    width,
                                    isResizable,
                                    isVisible,
                                    access,
                                    new TextBoxIntegerSingletonDOMElementFactory( gridLayer,
                                                                                  gridWidget ) );
    }

    protected IGridColumn<Integer> newSalienceColumn( final List<IGridColumn.HeaderMetaData> headerMetaData,
                                                      final double width,
                                                      final boolean isResizable,
                                                      final boolean isVisible,
                                                      final GuidedDecisionTablePresenter.Access access,
                                                      final boolean useRowNumber,
                                                      final GuidedDecisionTableView gridWidget ) {
        return new SalienceUiColumn( headerMetaData,
                                     width,
                                     isResizable,
                                     isVisible,
                                     access,
                                     useRowNumber,
                                     new TextBoxIntegerSingletonDOMElementFactory( gridLayer,
                                                                                   gridWidget ) );
    }

    protected IGridColumn<Long> newLongColumn( final List<IGridColumn.HeaderMetaData> headerMetaData,
                                               final double width,
                                               final boolean isResizable,
                                               final boolean isVisible,
                                               final GuidedDecisionTablePresenter.Access access,
                                               final GuidedDecisionTableView gridWidget ) {
        return new LongUiColumn( headerMetaData,
                                 width,
                                 isResizable,
                                 isVisible,
                                 access,
                                 new TextBoxLongSingletonDOMElementFactory( gridLayer,
                                                                            gridWidget ) );
    }

    protected IGridColumn<Short> newShortColumn( final List<IGridColumn.HeaderMetaData> headerMetaData,
                                                 final double width,
                                                 final boolean isResizable,
                                                 final boolean isVisible,
                                                 final GuidedDecisionTablePresenter.Access access,
                                                 final GuidedDecisionTableView gridWidget ) {
        return new ShortUiColumn( headerMetaData,
                                  width,
                                  isResizable,
                                  isVisible,
                                  access,
                                  new TextBoxShortSingletonDOMElementFactory( gridLayer,
                                                                              gridWidget ) );
    }

    protected IGridColumn<Date> newDateColumn( final List<IGridColumn.HeaderMetaData> headerMetaData,
                                               final double width,
                                               final boolean isResizable,
                                               final boolean isVisible,
                                               final GuidedDecisionTablePresenter.Access access,
                                               final GuidedDecisionTableView gridWidget ) {
        return new DateUiColumn( headerMetaData,
                                 width,
                                 isResizable,
                                 isVisible,
                                 access,
                                 new DatePickerSingletonDOMElementFactory( gridLayer,
                                                                           gridWidget ) );
    }

    protected IGridColumn<Boolean> newBooleanColumn( final List<IGridColumn.HeaderMetaData> headerMetaData,
                                                     final double width,
                                                     final boolean isResizable,
                                                     final boolean isVisible,
                                                     final GuidedDecisionTablePresenter.Access access,
                                                     final GuidedDecisionTableView gridWidget ) {
        return new BooleanUiColumn( headerMetaData,
                                    width,
                                    isResizable,
                                    isVisible,
                                    access,
                                    new CheckBoxDOMElementFactory( gridLayer,
                                                                   gridWidget ) {
                                        @Override
                                        public CheckBox createWidget() {
                                            final CheckBox checkBox = super.createWidget();
                                            checkBox.setEnabled( access.isEditable() );
                                            return checkBox;
                                        }
                                    } );
    }

    protected IGridColumn<String> newStringColumn( final List<IGridColumn.HeaderMetaData> headerMetaData,
                                                   final double width,
                                                   final boolean isResizable,
                                                   final boolean isVisible,
                                                   final GuidedDecisionTablePresenter.Access access,
                                                   final GuidedDecisionTableView gridWidget ) {
        return new StringUiColumn( headerMetaData,
                                   width,
                                   isResizable,
                                   isVisible,
                                   access,
                                   new TextBoxStringSingletonDOMElementFactory( gridLayer,
                                                                                gridWidget ) );
    }

}
