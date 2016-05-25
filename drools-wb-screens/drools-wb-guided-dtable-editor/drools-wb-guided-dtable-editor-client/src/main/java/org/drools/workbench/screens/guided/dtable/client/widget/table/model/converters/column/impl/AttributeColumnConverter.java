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

import org.drools.workbench.models.guided.dtable.shared.model.AttributeCol52;
import org.drools.workbench.models.guided.dtable.shared.model.BaseColumn;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTablePresenter;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.DialectUiColumn;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.dom.listbox.ListBoxStringSingletonDOMElementFactory;
import org.drools.workbench.screens.guided.rule.client.editor.RuleAttributeWidget;
import org.uberfire.ext.wires.core.grids.client.model.HeaderMetaDataImpl;
import org.uberfire.ext.wires.core.grids.client.model.IGridColumn;

@Dependent
public class AttributeColumnConverter extends BaseColumnConverterImpl {

    @Override
    public boolean handles( final BaseColumn column ) {
        return column instanceof AttributeCol52;
    }

    @Override
    public IGridColumn<?> convertColumn( final BaseColumn column,
                                         final GuidedDecisionTablePresenter.Access access,
                                         final GuidedDecisionTableView gridWidget ) {
        final AttributeCol52 attributeColumn = (AttributeCol52) column;
        final String attributeName = attributeColumn.getAttribute();
        if ( attributeName.equals( RuleAttributeWidget.SALIENCE_ATTR ) ) {
            return newSalienceColumn( makeHeaderMetaData( column ),
                                      Math.max( column.getWidth(),
                                                DEFAULT_COLUMN_WIDTH ),
                                      true,
                                      !column.isHideColumn(),
                                      access,
                                      attributeColumn.isUseRowNumber(),
                                      gridWidget );

        } else if ( attributeName.equals( GuidedDecisionTable52.ENABLED_ATTR ) ) {
            return newBooleanColumn( makeHeaderMetaData( column ),
                                     Math.max( column.getWidth(),
                                               DEFAULT_COLUMN_WIDTH ),
                                     true,
                                     !column.isHideColumn(),
                                     access,
                                     gridWidget );

        } else if ( attributeName.equals( GuidedDecisionTable52.NO_LOOP_ATTR ) ) {
            return newBooleanColumn( makeHeaderMetaData( column ),
                                     Math.max( column.getWidth(),
                                               DEFAULT_COLUMN_WIDTH ),
                                     true,
                                     !column.isHideColumn(),
                                     access,
                                     gridWidget );

        } else if ( attributeName.equals( GuidedDecisionTable52.DURATION_ATTR ) ) {
            return newLongColumn( makeHeaderMetaData( column ),
                                  Math.max( column.getWidth(),
                                            DEFAULT_COLUMN_WIDTH ),
                                  true,
                                  !column.isHideColumn(),
                                  access,
                                  gridWidget );

        } else if ( attributeName.equals( GuidedDecisionTable52.TIMER_ATTR ) ) {
            return newStringColumn( makeHeaderMetaData( column ),
                                    Math.max( column.getWidth(),
                                              DEFAULT_COLUMN_WIDTH ),
                                    true,
                                    !column.isHideColumn(),
                                    access,
                                    gridWidget );

        } else if ( attributeName.equals( GuidedDecisionTable52.CALENDARS_ATTR ) ) {
            return newStringColumn( makeHeaderMetaData( column ),
                                    Math.max( column.getWidth(),
                                              DEFAULT_COLUMN_WIDTH ),
                                    true,
                                    !column.isHideColumn(),
                                    access,
                                    gridWidget );

        } else if ( attributeName.equals( GuidedDecisionTable52.AUTO_FOCUS_ATTR ) ) {
            return newBooleanColumn( makeHeaderMetaData( column ),
                                     Math.max( column.getWidth(),
                                               DEFAULT_COLUMN_WIDTH ),
                                     true,
                                     !column.isHideColumn(),
                                     access,
                                     gridWidget );

        } else if ( attributeName.equals( GuidedDecisionTable52.LOCK_ON_ACTIVE_ATTR ) ) {
            return newBooleanColumn( makeHeaderMetaData( column ),
                                     Math.max( column.getWidth(),
                                               DEFAULT_COLUMN_WIDTH ),
                                     true,
                                     !column.isHideColumn(),
                                     access,
                                     gridWidget );

        } else if ( attributeName.equals( GuidedDecisionTable52.DATE_EFFECTIVE_ATTR ) ) {
            return newDateColumn( makeHeaderMetaData( column ),
                                  Math.max( column.getWidth(),
                                            DEFAULT_COLUMN_WIDTH ),
                                  true,
                                  !column.isHideColumn(),
                                  access,
                                  gridWidget );

        } else if ( attributeName.equals( GuidedDecisionTable52.DATE_EXPIRES_ATTR ) ) {
            return newDateColumn( makeHeaderMetaData( column ),
                                  Math.max( column.getWidth(),
                                            DEFAULT_COLUMN_WIDTH ),
                                  true,
                                  !column.isHideColumn(),
                                  access,
                                  gridWidget );

        } else if ( attributeName.equals( GuidedDecisionTable52.DIALECT_ATTR ) ) {
            return newDialectColumn( makeHeaderMetaData( column ),
                                     Math.max( column.getWidth(),
                                               DEFAULT_COLUMN_WIDTH ),
                                     true,
                                     !column.isHideColumn(),
                                     access,
                                     gridWidget );

        } else if ( attributeName.equals( GuidedDecisionTable52.NEGATE_RULE_ATTR ) ) {
            return newBooleanColumn( makeHeaderMetaData( column ),
                                     Math.max( column.getWidth(),
                                               DEFAULT_COLUMN_WIDTH ),
                                     true,
                                     !column.isHideColumn(),
                                     access,
                                     gridWidget );
        }
        return newStringColumn( makeHeaderMetaData( column ),
                                Math.max( column.getWidth(),
                                          DEFAULT_COLUMN_WIDTH ),
                                true,
                                !column.isHideColumn(),
                                access,
                                gridWidget );
    }

    protected IGridColumn<String> newDialectColumn( final List<IGridColumn.HeaderMetaData> headerMetaData,
                                                    final double width,
                                                    final boolean isResizable,
                                                    final boolean isVisible,
                                                    final GuidedDecisionTablePresenter.Access access,
                                                    final GuidedDecisionTableView gridWidget ) {
        return new DialectUiColumn( headerMetaData,
                                    width,
                                    isResizable,
                                    isVisible,
                                    access,
                                    new ListBoxStringSingletonDOMElementFactory( gridLayer,
                                                                                 gridWidget ) );
    }

    @Override
    public List<IGridColumn.HeaderMetaData> makeHeaderMetaData( final BaseColumn column ) {
        return new ArrayList<IGridColumn.HeaderMetaData>() {{
            add( new HeaderMetaDataImpl( ( (AttributeCol52) column ).getAttribute(),
                                         AttributeCol52.class.getName() ) );
        }};
    }

}
