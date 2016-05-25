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

import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.uberfire.ext.widgets.common.client.common.NumericFloatTextBox;
import org.uberfire.ext.wires.core.grids.client.widget.IGridLayer;

public class TextBoxFloatSingletonDOMElementFactory extends TextBoxSingletonDOMElementFactory<Float, NumericFloatTextBox> {

    public TextBoxFloatSingletonDOMElementFactory( final IGridLayer gridLayer,
                                                   final GuidedDecisionTableView gridWidget ) {
        super( gridLayer,
               gridWidget );
    }

    @Override
    public NumericFloatTextBox createWidget() {
        return new NumericFloatTextBox( true );
    }

    @Override
    public String convert( final Float value ) {
        if ( value == null ) {
            return "";
        }
        return value.toString();
    }

    @Override
    public Float convert( final String value ) {
        try {
            return new Float( value );
        } catch ( NumberFormatException nfe ) {
            return new Float( 0.0 );
        }
    }

}
