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
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTablePresenter;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.textbox.TextBoxDOMElement;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.textbox.TextBoxShortSingletonDOMElementFactory;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.ext.widgets.common.client.common.NumericShortTextBox;
import org.uberfire.ext.wires.core.grids.client.model.IGridCell;
import org.uberfire.ext.wires.core.grids.client.model.IGridCellValue;
import org.uberfire.ext.wires.core.grids.client.widget.context.GridBodyCellRenderContext;

public class ShortUiColumn extends BaseUiSingletonColumn<Short, NumericShortTextBox, TextBoxDOMElement<Short, NumericShortTextBox>, TextBoxShortSingletonDOMElementFactory> {

    public ShortUiColumn( final List<HeaderMetaData> headerMetaData,
                          final double width,
                          final boolean isResizable,
                          final boolean isVisible,
                          final GuidedDecisionTablePresenter.Access access,
                          final TextBoxShortSingletonDOMElementFactory factory ) {
        super( headerMetaData,
               new CellRenderer<Short, NumericShortTextBox, TextBoxDOMElement<Short, NumericShortTextBox>>( factory ) {
                   @Override
                   protected void doRenderCellContent( final Text t,
                                                       final Short value,
                                                       final GridBodyCellRenderContext context ) {
                       t.setText( value.toString() );
                   }
               },
               width,
               isResizable,
               isVisible,
               access,
               factory );
    }

    @Override
    public void doEdit( final IGridCell<Short> cell,
                        final GridBodyCellRenderContext context,
                        final Callback<IGridCellValue<Short>> callback ) {
        factory.attachDomElement( context,
                                  new Callback<TextBoxDOMElement<Short, NumericShortTextBox>>() {
                                      @Override
                                      public void callback( final TextBoxDOMElement<Short, NumericShortTextBox> e ) {
                                          if ( hasValue( cell ) ) {
                                              e.getWidget().setValue( factory.convert( cell.getValue().getValue() ) );
                                          }
                                      }
                                  },
                                  new Callback<TextBoxDOMElement<Short, NumericShortTextBox>>() {
                                      @Override
                                      public void callback( final TextBoxDOMElement<Short, NumericShortTextBox> e ) {
                                          e.getWidget().setFocus( true );
                                      }
                                  } );
    }

}
