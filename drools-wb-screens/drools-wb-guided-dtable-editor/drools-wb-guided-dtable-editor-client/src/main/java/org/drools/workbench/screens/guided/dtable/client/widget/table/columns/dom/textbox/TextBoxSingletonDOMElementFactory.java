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
package org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.textbox;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.gwtbootstrap3.client.ui.TextBox;
import org.uberfire.ext.wires.core.grids.client.widget.IBaseGridWidget;
import org.uberfire.ext.wires.core.grids.client.widget.IGridLayer;
import org.uberfire.ext.wires.core.grids.client.widget.context.GridBodyCellRenderContext;

/**
 * A DOMElement Factory for single-instance TextBoxes.
 */
public abstract class TextBoxSingletonDOMElementFactory<T, W extends TextBox> extends SingleValueSingletonDOMElementFactory<T, W, TextBoxDOMElement<T, W>> {

    public TextBoxSingletonDOMElementFactory( final IGridLayer gridLayer,
                                              final GuidedDecisionTableView gridWidget ) {
        super( gridLayer,
               gridWidget );
    }

    @Override
    public TextBoxDOMElement<T, W> createDomElement( final IGridLayer gridLayer,
                                                     final IBaseGridWidget gridWidget,
                                                     final GridBodyCellRenderContext context ) {
        this.widget = createWidget();
        this.e = new TextBoxDOMElement<T, W>( widget,
                                              gridLayer,
                                              gridWidget );

        widget.addBlurHandler( new BlurHandler() {
            @Override
            public void onBlur( final BlurEvent event ) {
                destroyResources();
                gridLayer.batch();
            }
        } );

        return e;
    }

    @Override
    protected T getValue() {
        if ( widget != null ) {
            return convert( widget.getValue() );
        }
        return null;
    }
}
