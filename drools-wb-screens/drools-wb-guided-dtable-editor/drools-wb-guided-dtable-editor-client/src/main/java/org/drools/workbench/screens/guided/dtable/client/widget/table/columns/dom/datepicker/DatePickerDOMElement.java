/*
 * Copyright 2015 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.datepicker;

import java.util.Date;

import com.google.gwt.dom.client.Style;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.textbox.SingleValueDOMElement;
import org.uberfire.ext.widgets.common.client.common.DatePicker;
import org.uberfire.ext.wires.core.grids.client.widget.IBaseGridWidget;
import org.uberfire.ext.wires.core.grids.client.widget.IGridLayer;
import org.uberfire.ext.wires.core.grids.client.widget.context.GridBodyCellRenderContext;

/**
 * A DOMElement for TextBoxes.
 */
public class DatePickerDOMElement extends SingleValueDOMElement<Date, DatePicker> {

    private static final int HEIGHT = 16;

    public DatePickerDOMElement( final DatePicker widget,
                                 final IGridLayer gridLayer,
                                 final IBaseGridWidget gridWidget ) {
        super( widget,
               gridLayer,
               gridWidget );
        final Style style = widget.getElement().getStyle();
        style.setWidth( 100,
                        Style.Unit.PCT );
        style.setHeight( HEIGHT,
                         Style.Unit.PX );
        style.setPaddingLeft( 2,
                              Style.Unit.PX );
        style.setPaddingRight( 2,
                               Style.Unit.PX );
        style.setFontSize( 10,
                           Style.Unit.PX );

        // --- Workaround for BS2 ---
        style.setPosition( Style.Position.RELATIVE );
        style.setPaddingTop( 0,
                             Style.Unit.PX );
        style.setPaddingBottom( 0,
                                Style.Unit.PX );
        style.setProperty( "WebkitBoxSizing",
                           "border-box" );
        style.setProperty( "MozBoxSizing",
                           "border-box" );
        style.setProperty( "boxSizing",
                           "border-box" );
        style.setProperty( "lineHeight",
                           "normal" );
        // --- End workaround ---

        getContainer().getElement().getStyle().setPaddingLeft( 5,
                                                               Style.Unit.PX );
        getContainer().getElement().getStyle().setPaddingRight( 5,
                                                                Style.Unit.PX );
        getContainer().setWidget( widget );
    }

    @Override
    public void initialise( final GridBodyCellRenderContext context ) {
        final Style style = widget.getElement().getStyle();
        style.setMarginTop( ( context.getCellHeight() - HEIGHT ) / 2,
                            Style.Unit.PX );
        transform( context );
    }

}
