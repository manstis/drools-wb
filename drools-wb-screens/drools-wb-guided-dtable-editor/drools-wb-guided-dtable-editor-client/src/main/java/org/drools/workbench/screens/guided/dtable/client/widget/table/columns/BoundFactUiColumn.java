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

import com.ait.lienzo.client.core.shape.Text;
import com.google.gwt.user.client.ui.ListBox;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableConstants;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTablePresenter;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.listbox.ListBoxDOMElement;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.listbox.ListBoxSingletonDOMElementFactory;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.ext.wires.core.grids.client.model.IGridCell;
import org.uberfire.ext.wires.core.grids.client.model.IGridCellValue;
import org.uberfire.ext.wires.core.grids.client.widget.context.GridBodyCellRenderContext;

public class BoundFactUiColumn extends BaseUiSingletonColumn<String, ListBox, ListBoxDOMElement<String, ListBox>, ListBoxSingletonDOMElementFactory<String, ListBox>> {

    private final GuidedDecisionTableView.Presenter presenter;

    public BoundFactUiColumn( final List<HeaderMetaData> headerMetaData,
                              final double width,
                              final boolean isResizable,
                              final boolean isVisible,
                              final GuidedDecisionTablePresenter.Access access,
                              final GuidedDecisionTableView.Presenter presenter,
                              final ListBoxSingletonDOMElementFactory<String, ListBox> factory ) {
        super( headerMetaData,
               new CellRenderer<String, ListBox, ListBoxDOMElement<String, ListBox>>( factory ) {
                   @Override
                   protected void doRenderCellContent( final Text t,
                                                       final String value,
                                                       final GridBodyCellRenderContext context ) {
                       t.setText( value );
                   }
               },
               width,
               isResizable,
               isVisible,
               access,
               factory );
        this.presenter = presenter;
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
                                          for ( String binding : presenter.getLHSBoundFacts() ) {
                                              widget.addItem( binding );
                                          }
                                          widget.setEnabled( widget.getItemCount() > 0 );
                                          if ( widget.getItemCount() == 0 ) {
                                              widget.addItem( GuidedDecisionTableConstants.INSTANCE.NoPatternBindingsAvailable() );
                                          } else {
                                              factory.toWidget( cell,
                                                                widget );
                                          }
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
