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
import org.gwtbootstrap3.client.ui.TextBox;
import org.uberfire.ext.wires.core.grids.client.widget.IGridLayer;

public class TextBoxStringSingletonDOMElementFactory extends TextBoxSingletonDOMElementFactory<String, TextBox> {

    public TextBoxStringSingletonDOMElementFactory( final IGridLayer gridLayer,
                                                    final GuidedDecisionTableView gridWidget ) {
        super( gridLayer,
               gridWidget );
    }

    @Override
    public TextBox createWidget() {
        return new TextBox();
    }

    @Override
    public String convert( final String value ) {
        return value;
    }

}
