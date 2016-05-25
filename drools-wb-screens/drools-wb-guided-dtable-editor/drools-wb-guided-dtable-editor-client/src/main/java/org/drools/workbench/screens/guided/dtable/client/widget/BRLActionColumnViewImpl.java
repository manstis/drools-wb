/*
 * Copyright 2011 Red Hat, Inc. and/or its affiliates.
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
package org.drools.workbench.screens.guided.dtable.client.widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.shared.EventBus;
import org.drools.workbench.models.datamodel.oracle.DataType;
import org.drools.workbench.models.datamodel.rule.IAction;
import org.drools.workbench.models.datamodel.rule.InterpolationVariable;
import org.drools.workbench.models.datamodel.rule.RuleModel;
import org.drools.workbench.models.guided.dtable.shared.model.ActionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.BRLActionColumn;
import org.drools.workbench.models.guided.dtable.shared.model.BRLActionVariableColumn;
import org.drools.workbench.models.guided.dtable.shared.model.BRLColumn;
import org.drools.workbench.models.guided.dtable.shared.model.BRLRuleModel;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableConstants;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.rule.client.editor.RuleModellerConfiguration;
import org.kie.workbench.common.widgets.client.datamodel.AsyncPackageDataModelOracle;

/**
 * An editor for a BRL Action Columns
 */
public class BRLActionColumnViewImpl extends AbstractBRLColumnViewImpl<IAction, BRLActionVariableColumn>
        implements
        BRLActionColumnView {

    public BRLActionColumnViewImpl( final GuidedDecisionTable52 model,
                                    final AsyncPackageDataModelOracle oracle,
                                    final GuidedDecisionTableView.Presenter presenter,
                                    final EventBus eventBus,
                                    final BRLActionColumn column,
                                    final boolean isNew,
                                    final boolean isReadOnly ) {
        super( model,
               oracle,
               presenter,
               eventBus,
               column,
               isNew,
               isReadOnly );
    }

    @Override
    public String getTitle() {
        return GuidedDecisionTableConstants.INSTANCE.ActionBRLFragmentConfiguration();
    }

    protected boolean isHeaderUnique( String header ) {
        for ( ActionCol52 o : model.getActionCols() ) {
            if ( o.getHeader().equals( header ) ) {
                return false;
            }
        }
        return true;
    }

    protected BRLRuleModel getRuleModel( BRLColumn<IAction, BRLActionVariableColumn> column ) {
        BRLRuleModel ruleModel = new BRLRuleModel( model );
        List<IAction> definition = column.getDefinition();
        ruleModel.rhs = definition.toArray( new IAction[ definition.size() ] );
        return ruleModel;
    }

    protected RuleModellerConfiguration getRuleModellerConfiguration() {
        return new RuleModellerConfiguration( true,
                                              false,
                                              true,
                                              true );
    }

    @Override
    protected void doInsertColumn() {
        this.editingCol.setDefinition( Arrays.asList( this.ruleModel.rhs ) );
        presenter.appendColumn( (BRLActionColumn) this.editingCol );
    }

    @Override
    protected void doUpdateColumn() {
        this.editingCol.setDefinition( Arrays.asList( this.ruleModel.rhs ) );
        presenter.updateColumn( (BRLActionColumn) this.originalCol,
                                (BRLActionColumn) this.editingCol );
    }

    @Override
    protected List<BRLActionVariableColumn> convertInterpolationVariables( Map<InterpolationVariable, Integer> ivs ) {

        //If there are no variables add a boolean column to specify whether the fragment should apply 
        if ( ivs.size() == 0 ) {
            BRLActionVariableColumn variable = new BRLActionVariableColumn( "",
                                                                            DataType.TYPE_BOOLEAN );
            variable.setHeader( editingCol.getHeader() );
            variable.setHideColumn( editingCol.isHideColumn() );
            List<BRLActionVariableColumn> variables = new ArrayList<BRLActionVariableColumn>();
            variables.add( variable );
            return variables;
        }

        //Convert to columns for use in the Decision Table
        BRLActionVariableColumn[] variables = new BRLActionVariableColumn[ ivs.size() ];
        for ( Map.Entry<InterpolationVariable, Integer> me : ivs.entrySet() ) {
            InterpolationVariable iv = me.getKey();
            int index = me.getValue();
            BRLActionVariableColumn variable = new BRLActionVariableColumn( iv.getVarName(),
                                                                            iv.getDataType(),
                                                                            iv.getFactType(),
                                                                            iv.getFactField() );
            variable.setHeader( editingCol.getHeader() );
            variable.setHideColumn( editingCol.isHideColumn() );
            variables[ index ] = variable;
        }

        //Convert the array into a mutable list (Arrays.toList provides an immutable list)
        List<BRLActionVariableColumn> variableList = new ArrayList<BRLActionVariableColumn>();
        for ( BRLActionVariableColumn variable : variables ) {
            variableList.add( variable );
        }
        return variableList;
    }

    @Override
    protected BRLColumn<IAction, BRLActionVariableColumn> cloneBRLColumn( BRLColumn<IAction, BRLActionVariableColumn> col ) {
        BRLActionColumn clone = new BRLActionColumn();
        clone.setHeader( col.getHeader() );
        clone.setHideColumn( col.isHideColumn() );
        clone.setChildColumns( cloneVariables( col.getChildColumns() ) );
        clone.setDefinition( cloneDefinition( col.getDefinition() ) );
        return clone;
    }

    private List<BRLActionVariableColumn> cloneVariables( List<BRLActionVariableColumn> variables ) {
        List<BRLActionVariableColumn> clone = new ArrayList<BRLActionVariableColumn>();
        for ( BRLActionVariableColumn variable : variables ) {
            clone.add( cloneVariable( variable ) );
        }
        return clone;
    }

    private BRLActionVariableColumn cloneVariable( BRLActionVariableColumn variable ) {
        BRLActionVariableColumn clone = new BRLActionVariableColumn( variable.getVarName(),
                                                                     variable.getFieldType(),
                                                                     variable.getFactType(),
                                                                     variable.getFactField() );
        clone.setHeader( variable.getHeader() );
        clone.setHideColumn( variable.isHideColumn() );
        clone.setWidth( variable.getWidth() );
        return clone;
    }

    private List<IAction> cloneDefinition( List<IAction> definition ) {
        RuleModelCloneVisitor visitor = new RuleModelCloneVisitor();
        RuleModel rm = new RuleModel();
        for ( IAction action : definition ) {
            rm.addRhsItem( action );
        }
        RuleModel rmClone = visitor.visitRuleModel( rm );
        List<IAction> clone = new ArrayList<IAction>();
        for ( IAction action : rmClone.rhs ) {
            clone.add( action );
        }
        return clone;
    }

}
