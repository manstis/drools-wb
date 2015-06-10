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

import java.math.BigDecimal;
import java.util.List;

import com.ait.lienzo.client.core.shape.Text;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTablePresenter;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.textbox.TextBoxBigDecimalSingletonDOMElementFactory;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.textbox.TextBoxDOMElement;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.ext.widgets.common.client.common.NumericBigDecimalTextBox;
import org.uberfire.ext.wires.core.grids.client.model.IGridCell;
import org.uberfire.ext.wires.core.grids.client.model.IGridCellValue;
import org.uberfire.ext.wires.core.grids.client.widget.context.GridBodyCellRenderContext;

public class BigDecimalUiColumn extends BaseUiSingletonColumn<BigDecimal, NumericBigDecimalTextBox, TextBoxDOMElement<BigDecimal, NumericBigDecimalTextBox>, TextBoxBigDecimalSingletonDOMElementFactory> {

    public BigDecimalUiColumn( final List<HeaderMetaData> headerMetaData,
                               final double width,
                               final boolean isResizable,
                               final boolean isVisible,
                               final GuidedDecisionTablePresenter.Access access,
                               final TextBoxBigDecimalSingletonDOMElementFactory factory ) {
        super( headerMetaData,
               new CellRenderer<BigDecimal, NumericBigDecimalTextBox, TextBoxDOMElement<BigDecimal, NumericBigDecimalTextBox>>( factory ) {
                   @Override
                   protected void doRenderCellContent( final Text t,
                                                       final BigDecimal value,
                                                       final GridBodyCellRenderContext context ) {
                       t.setText( value.toPlainString() );
                   }
               },
               width,
               isResizable,
               isVisible,
               access,
               factory );
    }

    @Override
    public void doEdit( final IGridCell<BigDecimal> cell,
                        final GridBodyCellRenderContext context,
                        final Callback<IGridCellValue<BigDecimal>> callback ) {
        factory.attachDomElement( context,
                                  new Callback<TextBoxDOMElement<BigDecimal, NumericBigDecimalTextBox>>() {
                                      @Override
                                      public void callback( final TextBoxDOMElement<BigDecimal, NumericBigDecimalTextBox> e ) {
                                          e.getWidget().setValue( factory.convert( cell.getValue().getValue() ) );
                                      }
                                  },
                                  new Callback<TextBoxDOMElement<BigDecimal, NumericBigDecimalTextBox>>() {
                                      @Override
                                      public void callback( final TextBoxDOMElement<BigDecimal, NumericBigDecimalTextBox> e ) {
                                          e.getWidget().setFocus( true );
                                      }
                                  } );
    }

}
