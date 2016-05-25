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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ait.lienzo.client.core.shape.Text;
import com.google.gwt.user.client.ui.ListBox;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTablePresenter;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.listbox.ListBoxDOMElement;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.listbox.ListBoxSingletonDOMElementFactory;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.ext.wires.core.grids.client.model.IGridCell;
import org.uberfire.ext.wires.core.grids.client.model.IGridCellValue;
import org.uberfire.ext.wires.core.grids.client.widget.context.GridBodyCellRenderContext;

public class ValueListUiColumn extends BaseUiSingletonColumn<String, ListBox, ListBoxDOMElement<String, ListBox>, ListBoxSingletonDOMElementFactory<String, ListBox>> {

    private final Map<String, String> valueListLookup = new HashMap<String, String>();

    public ValueListUiColumn( final List<HeaderMetaData> headerMetaData,
                              final double width,
                              final boolean isResizable,
                              final boolean isVisible,
                              final GuidedDecisionTablePresenter.Access access,
                              final ListBoxSingletonDOMElementFactory<String, ListBox> factory,
                              final Map<String, String> valueListLookup ) {
        this( headerMetaData,
              width,
              isResizable,
              isVisible,
              access,
              factory,
              valueListLookup,
              false );
    }

    public ValueListUiColumn( final List<HeaderMetaData> headerMetaData,
                              final double width,
                              final boolean isResizable,
                              final boolean isVisible,
                              final GuidedDecisionTablePresenter.Access access,
                              final ListBoxSingletonDOMElementFactory<String, ListBox> factory,
                              final Map<String, String> valueListLookup,
                              final boolean isMultipleSelect ) {
        super( headerMetaData,
               new CellRenderer<String, ListBox, ListBoxDOMElement<String, ListBox>>( factory ) {
                   @Override
                   protected void doRenderCellContent( final Text t,
                                                       final String value,
                                                       final GridBodyCellRenderContext context ) {
                       t.setText( getLabel( value.toString() ) );
                   }

                   private String getLabel( final String value ) {
                       if ( !isMultipleSelect ) {
                           return valueListLookup.get( value );
                       }
                       final StringBuilder sb = new StringBuilder();
                       final String[] values = value.split( "," );
                       for ( int i = 0; i < values.length; i++ ) {
                           if ( i == 0 ) {
                               sb.append( valueListLookup.get( values[ i ] ) );
                           } else {
                               sb.append( "," ).append( valueListLookup.get( values[ i ] ) );
                           }
                       }
                       return sb.toString();
                   }

               },
               width,
               isResizable,
               isVisible,
               access,
               factory );
        this.valueListLookup.putAll( valueListLookup );
    }

    @Override
    public void doEdit( final IGridCell<String> cell,
                        final GridBodyCellRenderContext context,
                        final Callback<IGridCellValue<String>> callback ) {
        factory.attachDomElement( context,
                                  new Callback<ListBoxDOMElement<String, ListBox>>() {
                                      @Override
                                      public void callback( final ListBoxDOMElement<String, ListBox> e ) {
                                          final ListBox widget = e.getWidget();
                                          for ( Map.Entry<String, String> lookup : valueListLookup.entrySet() ) {
                                              widget.addItem( lookup.getValue(),
                                                              lookup.getKey() );
                                          }
                                          factory.toWidget( cell,
                                                            widget );
                                      }
                                  },
                                  new Callback<ListBoxDOMElement<String, ListBox>>() {
                                      @Override
                                      public void callback( final ListBoxDOMElement<String, ListBox> e ) {
                                          e.getWidget().setFocus( true );
                                      }
                                  } );
    }

}
