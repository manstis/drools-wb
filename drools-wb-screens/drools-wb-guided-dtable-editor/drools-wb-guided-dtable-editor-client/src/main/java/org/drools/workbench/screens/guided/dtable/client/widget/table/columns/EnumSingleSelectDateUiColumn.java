/*
 * Copyright 2015 JBoss Inc
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
package org.drools.workbench.screens.guided.dtable.client.widget.table.columns;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.ListBox;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTablePresenter;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.datepicker.DatePickerDOMElement;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.listbox.ListBoxDOMElement;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.listbox.MultiValueSingletonDOMElementFactory;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.textbox.SingleValueSingletonDOMElementFactory;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.ext.widgets.common.client.common.DatePicker;
import org.uberfire.ext.wires.core.grids.client.model.IGridCell;
import org.uberfire.ext.wires.core.grids.client.widget.context.GridBodyCellRenderContext;

public class EnumSingleSelectDateUiColumn extends BaseEnumSingleSelectUiColumn<Date, ListBox, DatePicker, ListBoxDOMElement<Date, ListBox>, DatePickerDOMElement> {

    public EnumSingleSelectDateUiColumn( final List<HeaderMetaData> headerMetaData,
                                         final double width,
                                         final boolean isResizable,
                                         final boolean isVisible,
                                         final GuidedDecisionTablePresenter.Access access,
                                         final MultiValueSingletonDOMElementFactory<Date, ListBox, ListBoxDOMElement<Date, ListBox>> multiValueFactory,
                                         final SingleValueSingletonDOMElementFactory<Date, DatePicker, DatePickerDOMElement> singleValueFactory,
                                         final GuidedDecisionTableView.Presenter presenter,
                                         final String factType,
                                         final String factField ) {
        super( headerMetaData,
               width,
               isResizable,
               isVisible,
               access,
               multiValueFactory,
               singleValueFactory,
               presenter,
               factType,
               factField );
    }

    @Override
    protected void initialiseMultiValueDomElement( final IGridCell<Date> cell,
                                                   final GridBodyCellRenderContext context,
                                                   final Map<String, String> enumLookups ) {
        factory.attachDomElement( context,
                                  new Callback<ListBoxDOMElement<Date, ListBox>>() {
                                      @Override
                                      public void callback( final ListBoxDOMElement<Date, ListBox> e ) {
                                          final ListBox widget = e.getWidget();
                                          for ( Map.Entry<String, String> lookup : enumLookups.entrySet() ) {
                                              widget.addItem( lookup.getValue(),
                                                              lookup.getKey() );
                                          }
                                          factory.toWidget( cell,
                                                            widget );
                                      }
                                  },
                                  new Callback<ListBoxDOMElement<Date, ListBox>>() {
                                      @Override
                                      public void callback( final ListBoxDOMElement<Date, ListBox> e ) {
                                          e.getWidget().setFocus( true );
                                      }
                                  } );
    }

    @Override
    protected void initialiseSingleValueDomElement( final IGridCell<Date> cell,
                                                    final GridBodyCellRenderContext context ) {
        singleValueFactory.attachDomElement( context,
                                             new Callback<DatePickerDOMElement>() {
                                                 @Override
                                                 public void callback( final DatePickerDOMElement e ) {
                                                     final DatePicker widget = e.getWidget();
                                                     if ( hasValue( cell ) ) {
                                                         widget.setValue( cell.getValue().getValue() );
                                                     } else {
                                                         widget.setValue( new Date() );
                                                     }
                                                 }
                                             },
                                             new Callback<DatePickerDOMElement>() {
                                                 @Override
                                                 public void callback( final DatePickerDOMElement e ) {
                                                     //Do nothing
                                                 }
                                             } );
    }

}
