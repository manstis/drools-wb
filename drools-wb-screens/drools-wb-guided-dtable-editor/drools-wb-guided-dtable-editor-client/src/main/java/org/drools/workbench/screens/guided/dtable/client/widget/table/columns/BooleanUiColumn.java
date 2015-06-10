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

import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTablePresenter;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.uberfire.ext.wires.core.grids.client.widget.dom.CheckBoxDOMElement;
import org.uberfire.ext.wires.core.grids.client.widget.dom.multiple.CheckBoxDOMElementFactory;
import org.uberfire.ext.wires.core.grids.client.widget.renderers.columns.multiple.BooleanColumnDOMElementRenderer;

public class BooleanUiColumn extends BaseUiColumn<Boolean, CheckBox, CheckBoxDOMElement, CheckBoxDOMElementFactory> {

    public BooleanUiColumn( final List<HeaderMetaData> headerMetaData,
                            final double width,
                            final boolean isResizable,
                            final boolean isVisible,
                            final GuidedDecisionTablePresenter.Access access,
                            final CheckBoxDOMElementFactory factory ) {
        super( headerMetaData,
               new BooleanColumnDOMElementRenderer( factory ),
               width,
               isResizable,
               isVisible,
               access,
               factory );
    }

}
