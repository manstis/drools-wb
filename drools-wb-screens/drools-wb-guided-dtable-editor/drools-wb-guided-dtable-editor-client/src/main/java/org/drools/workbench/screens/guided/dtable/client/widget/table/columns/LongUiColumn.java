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
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.textbox.TextBoxLongSingletonDOMElementFactory;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.ext.widgets.common.client.common.NumericLongTextBox;
import org.uberfire.ext.wires.core.grids.client.model.IGridCell;
import org.uberfire.ext.wires.core.grids.client.model.IGridCellValue;
import org.uberfire.ext.wires.core.grids.client.widget.context.GridBodyCellRenderContext;

public class LongUiColumn extends BaseUiSingletonColumn<Long, NumericLongTextBox, TextBoxDOMElement<Long, NumericLongTextBox>, TextBoxLongSingletonDOMElementFactory> {

    public LongUiColumn( final List<HeaderMetaData> headerMetaData,
                         final double width,
                         final boolean isResizable,
                         final boolean isVisible,
                         final GuidedDecisionTablePresenter.Access access,
                         final TextBoxLongSingletonDOMElementFactory factory ) {
        super( headerMetaData,
               new CellRenderer<Long, NumericLongTextBox, TextBoxDOMElement<Long, NumericLongTextBox>>( factory ) {
                   @Override
                   protected void doRenderCellContent( final Text t,
                                                       final Long value,
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
    public void doEdit( final IGridCell<Long> cell,
                        final GridBodyCellRenderContext context,
                        final Callback<IGridCellValue<Long>> callback ) {
        factory.attachDomElement( context,
                                  new Callback<TextBoxDOMElement<Long, NumericLongTextBox>>() {
                                      @Override
                                      public void callback( final TextBoxDOMElement<Long, NumericLongTextBox> e ) {
                                          if ( hasValue( cell ) ) {
                                              e.getWidget().setValue( factory.convert( cell.getValue().getValue() ) );
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
