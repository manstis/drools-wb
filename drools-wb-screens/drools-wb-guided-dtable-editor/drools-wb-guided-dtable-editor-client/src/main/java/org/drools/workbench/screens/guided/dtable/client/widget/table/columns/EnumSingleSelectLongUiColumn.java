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

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.ListBox;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTablePresenter;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.listbox.ListBoxDOMElement;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.listbox.MultiValueSingletonDOMElementFactory;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.textbox.SingleValueSingletonDOMElementFactory;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.textbox.TextBoxDOMElement;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.ext.widgets.common.client.common.NumericLongTextBox;
import org.uberfire.ext.wires.core.grids.client.model.IGridCell;
import org.uberfire.ext.wires.core.grids.client.widget.context.GridBodyCellRenderContext;

public class EnumSingleSelectLongUiColumn extends BaseEnumSingleSelectUiColumn<Long, ListBox, NumericLongTextBox, ListBoxDOMElement<Long, ListBox>, TextBoxDOMElement<Long, NumericLongTextBox>> {

    public EnumSingleSelectLongUiColumn( final List<HeaderMetaData> headerMetaData,
                                         final double width,
                                         final boolean isResizable,
                                         final boolean isVisible,
                                         final GuidedDecisionTablePresenter.Access access,
                                         final MultiValueSingletonDOMElementFactory<Long, ListBox, ListBoxDOMElement<Long, ListBox>> multiValueFactory,
                                         final SingleValueSingletonDOMElementFactory<Long, NumericLongTextBox, TextBoxDOMElement<Long, NumericLongTextBox>> singleValueFactory,
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
    protected void initialiseMultiValueDomElement( final IGridCell<Long> cell,
                                                   final GridBodyCellRenderContext context,
                                                   final Map<String, String> enumLookups ) {
        factory.attachDomElement( context,
                                  new Callback<ListBoxDOMElement<Long, ListBox>>() {
                                      @Override
                                      public void callback( final ListBoxDOMElement<Long, ListBox> e ) {
                                          final ListBox widget = e.getWidget();
                                          for ( Map.Entry<String, String> lookup : enumLookups.entrySet() ) {
                                              widget.addItem( lookup.getValue(),
                                                              lookup.getKey() );
                                          }
                                          factory.toWidget( cell,
                                                            widget );
                                      }
                                  },
                                  new Callback<ListBoxDOMElement<Long, ListBox>>() {
                                      @Override
                                      public void callback( final ListBoxDOMElement<Long, ListBox> e ) {
                                          e.getWidget().setFocus( true );
                                      }
                                  } );
    }

    @Override
    protected void initialiseSingleValueDomElement( final IGridCell<Long> cell,
                                                    final GridBodyCellRenderContext context ) {
        singleValueFactory.attachDomElement( context,
                                             new Callback<TextBoxDOMElement<Long, NumericLongTextBox>>() {
                                                 @Override
                                                 public void callback( final TextBoxDOMElement<Long, NumericLongTextBox> e ) {
                                                     if ( hasValue( cell ) ) {
                                                         e.getWidget().setValue( singleValueFactory.convert( cell.getValue().getValue() ) );
                                                     }
                                                 }
                                             },
                                             new Callback<TextBoxDOMElement<Long, NumericLongTextBox>>() {
                                                 @Override
                                                 public void callback( final TextBoxDOMElement<Long, NumericLongTextBox> e ) {
                                                     e.getWidget().setFocus( true );
                                                 }
                                             } );
    }

}
