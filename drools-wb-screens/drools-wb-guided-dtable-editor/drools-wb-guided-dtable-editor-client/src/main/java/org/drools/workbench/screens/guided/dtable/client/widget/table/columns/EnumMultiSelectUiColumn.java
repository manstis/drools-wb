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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.ait.lienzo.client.core.shape.Text;
import com.google.gwt.user.client.ui.ListBox;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTablePresenter;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.listbox.ListBoxDOMElement;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.listbox.ListBoxSingletonDOMElementFactory;
import org.drools.workbench.screens.guided.dtable.client.widget.table.utilities.DependentEnumsUtilities;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.ext.wires.core.grids.client.model.IGridCell;
import org.uberfire.ext.wires.core.grids.client.model.IGridCellValue;
import org.uberfire.ext.wires.core.grids.client.widget.context.GridBodyCellRenderContext;

public class EnumMultiSelectUiColumn extends BaseUiSingletonColumn<String, ListBox, ListBoxDOMElement<String, ListBox>, ListBoxSingletonDOMElementFactory<String, ListBox>> {

    private final String factType;
    private final String factField;
    private final GuidedDecisionTableView.Presenter presenter;

    public EnumMultiSelectUiColumn( final List<HeaderMetaData> headerMetaData,
                                    final double width,
                                    final boolean isResizable,
                                    final boolean isVisible,
                                    final GuidedDecisionTablePresenter.Access access,
                                    final ListBoxSingletonDOMElementFactory<String, ListBox> factory,
                                    final GuidedDecisionTableView.Presenter presenter,
                                    final String factType,
                                    final String factField ) {
        super( headerMetaData,
               new CellRenderer<String, ListBox, ListBoxDOMElement<String, ListBox>>( factory ) {
                   @Override
                   protected void doRenderCellContent( final Text t,
                                                       final String value,
                                                       final GridBodyCellRenderContext context ) {
                       //We need to get the list of potential values to lookup the "Display" value from the "Stored" value.
                       //Since the content of the list may be different for each cell (dependent enumerations) the list
                       //has to be populated "on demand".
                       presenter.getEnumLookups( factType,
                                                 factField,
                                                 new DependentEnumsUtilities.Context( context.getRowIndex(),
                                                                                      context.getColumnIndex() ),
                                                 new Callback<Map<String, String>>() {

                                                     @Override
                                                     public void callback( final Map<String, String> enumLookups ) {
                                                         t.setText( makeLabel( value,
                                                                               enumLookups ) );
                                                     }

                                                     //Build String of display text for cell's component items
                                                     private String makeLabel( final String value,
                                                                               final Map<String, String> enumLookups ) {
                                                         final List<String> splitValues = Arrays.asList( value.split( "," ) );
                                                         final StringBuilder sb = new StringBuilder();
                                                         boolean first = true;
                                                         for ( int i = 0; i < splitValues.size(); i++ ) {
                                                             final String splitValue = splitValues.get( i );
                                                             if ( enumLookups.containsKey( splitValue ) ) {
                                                                 if ( first ) {
                                                                     sb.append( enumLookups.get( splitValue ) );
                                                                     first = false;
                                                                 } else {
                                                                     sb.append( "," ).append( enumLookups.get( splitValue ) );
                                                                 }
                                                             }
                                                         }
                                                         return sb.toString();
                                                     }

                                                 } );
                   }
               },
               width,
               isResizable,
               isVisible,
               access,
               factory );
        this.presenter = presenter;
        this.factType = factType;
        this.factField = factField;
    }

    @Override
    public void doEdit( final IGridCell<String> cell,
                        final GridBodyCellRenderContext context,
                        final Callback<IGridCellValue<String>> callback ) {
        final String value = extractValue( cell );

        //We need to get the list of potential values to lookup the "Display" value from the "Stored" value.
        //Since the content of the list may be different for each cell (dependent enumerations) the list
        //has to be populated "on demand".
        presenter.getEnumLookups( this.factType,
                                  this.factField,
                                  new DependentEnumsUtilities.Context( context.getRowIndex(),
                                                                       context.getColumnIndex() ),
                                  new Callback<Map<String, String>>() {

                                      @Override
                                      public void callback( final Map<String, String> enumLookups ) {
                                          factory.attachDomElement( context,
                                                                    new Callback<ListBoxDOMElement<String, ListBox>>() {
                                                                        @Override
                                                                        public void callback( final ListBoxDOMElement<String, ListBox> e ) {
                                                                            final ListBox widget = e.getWidget();
                                                                            for ( Map.Entry<String, String> lookup : enumLookups.entrySet() ) {
                                                                                widget.addItem( lookup.getValue(),
                                                                                                lookup.getKey() );
                                                                            }
                                                                            final List<String> values = Arrays.asList( value.split( "," ) );
                                                                            for ( int i = 0; i < widget.getItemCount(); i++ ) {
                                                                                widget.setItemSelected( i,
                                                                                                        values.contains( widget.getValue( i ) ) );
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
                                  } );

    }

    private String extractValue( final IGridCell<String> cell ) {
        if ( cell == null || cell.getValue() == null || cell.getValue().getValue() == null ) {
            return "";
        }
        return cell.getValue().getValue();
    }

}
