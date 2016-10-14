/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
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
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.GuidedDecisionTableUiCell;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.ext.wires.core.grids.client.model.GridCell;
import org.uberfire.ext.wires.core.grids.client.model.GridCellValue;
import org.uberfire.ext.wires.core.grids.client.widget.context.GridBodyCellRenderContext;
import org.uberfire.ext.wires.core.grids.client.widget.dom.impl.CheckBoxDOMElement;
import org.uberfire.ext.wires.core.grids.client.widget.dom.multiple.impl.CheckBoxDOMElementFactory;
import org.uberfire.ext.wires.core.grids.client.widget.grid.renderers.columns.multiple.impl.BooleanColumnDOMElementRenderer;

public class BooleanUiColumn extends BaseMultipleDOMElementUiColumn<Boolean, CheckBox, CheckBoxDOMElement, CheckBoxDOMElementFactory> {

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

    @Override
    public void edit( final GridCell<Boolean> cell,
                      final GridBodyCellRenderContext context,
                      final Callback<GridCellValue<Boolean>> callback ) {
        if ( !isEditable() ) {
            return;
        }
        callback.callback( new GuidedDecisionTableUiCell<>( !cell.getValue().getValue() ) );
    }

}
