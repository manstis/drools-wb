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
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.textbox.TextBoxDoubleSingletonDOMElementFactory;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.ext.widgets.common.client.common.NumericDoubleTextBox;
import org.uberfire.ext.wires.core.grids.client.model.IGridCell;
import org.uberfire.ext.wires.core.grids.client.model.IGridCellValue;
import org.uberfire.ext.wires.core.grids.client.widget.context.GridBodyCellRenderContext;

public class DoubleUiColumn extends BaseUiSingletonColumn<Double, NumericDoubleTextBox, TextBoxDOMElement<Double, NumericDoubleTextBox>, TextBoxDoubleSingletonDOMElementFactory> {

    public DoubleUiColumn( final List<HeaderMetaData> headerMetaData,
                           final double width,
                           final boolean isResizable,
                           final boolean isVisible,
                           final GuidedDecisionTablePresenter.Access access,
                           final TextBoxDoubleSingletonDOMElementFactory factory ) {
        super( headerMetaData,
               new CellRenderer<Double, NumericDoubleTextBox, TextBoxDOMElement<Double, NumericDoubleTextBox>>( factory ) {
                   @Override
                   protected void doRenderCellContent( final Text t,
                                                       final Double value,
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
    public void doEdit( final IGridCell<Double> cell,
                        final GridBodyCellRenderContext context,
                        final Callback<IGridCellValue<Double>> callback ) {
        factory.attachDomElement( context,
                                  new Callback<TextBoxDOMElement<Double, NumericDoubleTextBox>>() {
                                      @Override
                                      public void callback( final TextBoxDOMElement<Double, NumericDoubleTextBox> e ) {
                                          if ( hasValue( cell ) ) {
                                              e.getWidget().setValue( factory.convert( cell.getValue().getValue() ) );
                                          }
                                      }
                                  },
                                  new Callback<TextBoxDOMElement<Double, NumericDoubleTextBox>>() {
                                      @Override
                                      public void callback( final TextBoxDOMElement<Double, NumericDoubleTextBox> e ) {
                                          e.getWidget().setFocus( true );
                                      }
                                  } );
    }

}
