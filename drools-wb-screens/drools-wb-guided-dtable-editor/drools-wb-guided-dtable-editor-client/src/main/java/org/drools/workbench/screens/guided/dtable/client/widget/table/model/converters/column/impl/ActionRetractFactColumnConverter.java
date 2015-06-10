/*
 * Copyright 2011 JBoss Inc
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
package org.drools.workbench.screens.guided.dtable.client.widget.table.model.converters.column.impl;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.Dependent;

import org.drools.workbench.models.guided.dtable.shared.model.ActionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.ActionRetractFactCol52;
import org.drools.workbench.models.guided.dtable.shared.model.BaseColumn;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTablePresenter;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.BoundFactUiColumn;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.listbox.ListBoxStringSingletonDOMElementFactory;
import org.uberfire.ext.wires.core.grids.client.model.HeaderMetaDataImpl;
import org.uberfire.ext.wires.core.grids.client.model.IGridColumn;

@Dependent
public class ActionRetractFactColumnConverter extends BaseColumnConverterImpl {

    @Override
    public boolean handles( final BaseColumn column ) {
        return column instanceof ActionRetractFactCol52;
    }

    @Override
    public IGridColumn<?> convertColumn( final BaseColumn column,
                                         final GuidedDecisionTablePresenter.Access access,
                                         final GuidedDecisionTableView gridWidget ) {
        return convertColumn( (ActionRetractFactCol52) column,
                              access,
                              gridWidget );
    }

    private IGridColumn<?> convertColumn( final ActionRetractFactCol52 column,
                                          final GuidedDecisionTablePresenter.Access access,
                                          final GuidedDecisionTableView gridWidget ) {
        return newActionRetractFactColumn( makeHeaderMetaData( column ),
                                           Math.max( column.getWidth(),
                                                     DEFAULT_COLUMN_WIDTH ),
                                           true,
                                           !column.isHideColumn(),
                                           access,
                                           gridWidget );
    }

    protected IGridColumn<String> newActionRetractFactColumn( final List<IGridColumn.HeaderMetaData> headerMetaData,
                                                              final double width,
                                                              final boolean isResizable,
                                                              final boolean isVisible,
                                                              final GuidedDecisionTablePresenter.Access access,
                                                              final GuidedDecisionTableView gridWidget ) {
        return new BoundFactUiColumn( headerMetaData,
                                      width,
                                      isResizable,
                                      isVisible,
                                      access,
                                      presenter,
                                      new ListBoxStringSingletonDOMElementFactory( gridLayer,
                                                                                   gridWidget ) );
    }

    @Override
    public List<IGridColumn.HeaderMetaData> makeHeaderMetaData( final BaseColumn column ) {
        return new ArrayList<IGridColumn.HeaderMetaData>() {{
            add( new HeaderMetaDataImpl( column.getHeader(),
                                         ActionCol52.class.getName() ) );
        }};
    }

}
