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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import org.drools.workbench.models.datamodel.workitems.PortableBooleanParameterDefinition;
import org.drools.workbench.models.datamodel.workitems.PortableEnumParameterDefinition;
import org.drools.workbench.models.datamodel.workitems.PortableFloatParameterDefinition;
import org.drools.workbench.models.datamodel.workitems.PortableIntegerParameterDefinition;
import org.drools.workbench.models.datamodel.workitems.PortableListParameterDefinition;
import org.drools.workbench.models.datamodel.workitems.PortableObjectParameterDefinition;
import org.drools.workbench.models.datamodel.workitems.PortableParameterDefinition;
import org.drools.workbench.models.datamodel.workitems.PortableStringParameterDefinition;
import org.drools.workbench.models.datamodel.workitems.PortableWorkDefinition;
import org.drools.workbench.models.guided.dtable.shared.model.ActionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.ActionWorkItemCol52;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableConstants;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.client.ui.TextBox;
import org.kie.workbench.common.widgets.client.workitems.WorkItemParametersWidget;
import org.uberfire.ext.widgets.common.client.common.popups.FormStylePopup;
import org.uberfire.ext.widgets.common.client.common.popups.footers.ModalFooterOKCancelButtons;

/**
 * A popup to define an Action to execute a Work Item
 */
public class ActionWorkItemPopup extends FormStylePopup {

    //TODO {manstis} Popups need to MVP'ed
    private final GuidedDecisionTable52 model;

    private final WorkItemParametersWidget workItemInputParameters;
    private final GuidedDecisionTableView.Presenter presenter;
    private final ActionWorkItemCol52 editingCol;
    private final ActionColumnCommand refreshGrid;
    private final ActionWorkItemCol52 originalCol;
    private final boolean isNew;
    private final boolean isReadOnly;

    private final Command cmdOK = new Command() {
        @Override
        public void execute() {
            applyChanges();
        }
    };
    private final Command cmdCancel = new Command() {
        @Override
        public void execute() {
            hide();
        }
    };
    private final ModalFooterOKCancelButtons footer = new ModalFooterOKCancelButtons( cmdOK,
                                                                                      cmdCancel );

    private Map<String, PortableWorkDefinition> workItemDefinitionsMap;
    private int workItemInputParametersIndex;

    public ActionWorkItemPopup( final GuidedDecisionTable52 model,
                                final GuidedDecisionTableView.Presenter presenter,
                                final ActionColumnCommand refreshGrid,
                                final ActionWorkItemCol52 column,
                                final Set<PortableWorkDefinition> workItemDefinitions,
                                final boolean isNew,
                                final boolean isReadOnly ) {
        super( GuidedDecisionTableConstants.INSTANCE.ColumnConfigurationWorkItem() );
        this.model = model;
        this.presenter = presenter;
        this.editingCol = cloneActionWorkItemColumn( column );
        this.refreshGrid = refreshGrid;
        this.originalCol = column;
        this.isNew = isNew;
        this.isReadOnly = isReadOnly;

        this.workItemInputParameters = new WorkItemParametersWidget( presenter,
                                                                     isReadOnly );

        //Column header
        final TextBox header = new TextBox();
        header.setText( column.getHeader() );
        header.setEnabled( !isReadOnly );
        if ( !isReadOnly ) {
            header.addChangeHandler( new ChangeHandler() {
                public void onChange( ChangeEvent event ) {
                    editingCol.setHeader( header.getText() );
                }
            } );
        }
        addAttribute( GuidedDecisionTableConstants.INSTANCE.ColumnHeaderDescription(),
                      header );

        //Work Item Definitions
        final ListBox workItemsListBox = new ListBox();
        addAttribute( GuidedDecisionTableConstants.INSTANCE.WorkItemNameColon(),
                      workItemsListBox );
        workItemsListBox.setEnabled( !isReadOnly );
        if ( !isReadOnly ) {
            workItemsListBox.addChangeHandler( new ChangeHandler() {

                public void onChange( ChangeEvent event ) {
                    int index = workItemsListBox.getSelectedIndex();
                    if ( index >= 0 ) {
                        String selectedWorkItemName = workItemsListBox.getValue( index );
                        editingCol.setWorkItemDefinition( workItemDefinitionsMap.get( selectedWorkItemName ) );
                        showWorkItemParameters();
                    }
                }

            } );
        }

        //Work Item Input Parameters
        workItemInputParametersIndex = addAttribute( GuidedDecisionTableConstants.INSTANCE.WorkItemInputParameters(),
                                                     workItemInputParameters,
                                                     false ).getIndex();
        setupWorkItems( workItemsListBox,
                        workItemDefinitions );

        //Hide column tick-box
        addAttribute( new StringBuilder( GuidedDecisionTableConstants.INSTANCE.HideThisColumn() ).append( GuidedDecisionTableConstants.COLON ).toString(),
                      DTCellValueWidgetFactory.getHideColumnIndicator( editingCol ) );

        //Apply button
        footer.enableOkButton( !isReadOnly );
        add( footer );
    }

    private void applyChanges() {
        if ( null == editingCol.getHeader()
                || "".equals( editingCol.getHeader() ) ) {
            Window.alert( GuidedDecisionTableConstants.INSTANCE.YouMustEnterAColumnHeaderValueDescription() );
            return;
        }
        if ( isNew ) {
            if ( !unique( editingCol.getHeader() ) ) {
                Window.alert( GuidedDecisionTableConstants.INSTANCE.ThatColumnNameIsAlreadyInUsePleasePickAnother() );
                return;
            }

        } else {
            if ( !originalCol.getHeader().equals( editingCol.getHeader() ) ) {
                if ( !unique( editingCol.getHeader() ) ) {
                    Window.alert( GuidedDecisionTableConstants.INSTANCE.ThatColumnNameIsAlreadyInUsePleasePickAnother() );
                    return;
                }
            }
        }

        // Pass new\modified column back for handling
        refreshGrid.execute( editingCol );
        hide();
    }

    private boolean unique( String header ) {
        for ( ActionCol52 o : model.getActionCols() ) {
            if ( o.getHeader().equals( header ) ) {
                return false;
            }
        }
        return true;
    }

    private ActionWorkItemCol52 cloneActionWorkItemColumn( ActionWorkItemCol52 col ) {
        ActionWorkItemCol52 clone = new ActionWorkItemCol52();
        clone.setHeader( col.getHeader() );
        clone.setHideColumn( col.isHideColumn() );
        clone.setWorkItemDefinition( cloneWorkItemDefinition( col.getWorkItemDefinition() ) );
        return clone;
    }

    private PortableWorkDefinition cloneWorkItemDefinition( PortableWorkDefinition pwd ) {
        if ( pwd == null ) {
            return null;
        }
        PortableWorkDefinition clone = new PortableWorkDefinition();
        clone.setName( pwd.getName() );
        clone.setDisplayName( pwd.getDisplayName() );
        clone.setParameters( cloneParameters( pwd.getParameters() ) );
        clone.setResults( cloneParameters( pwd.getResults() ) );
        return clone;
    }

    private Set<PortableParameterDefinition> cloneParameters( Collection<PortableParameterDefinition> parameters ) {
        Set<PortableParameterDefinition> clone = new HashSet<PortableParameterDefinition>();
        for ( PortableParameterDefinition ppd : parameters ) {
            clone.add( cloneParameter( ppd ) );
        }
        return clone;
    }

    private PortableParameterDefinition cloneParameter( PortableParameterDefinition ppd ) {
        PortableParameterDefinition clone = null;
        if ( ppd instanceof PortableBooleanParameterDefinition ) {
            clone = new PortableBooleanParameterDefinition();
            clone.setName( ppd.getName() );
            ( (PortableBooleanParameterDefinition) clone ).setBinding( ( (PortableBooleanParameterDefinition) ppd ).getBinding() );
            ( (PortableBooleanParameterDefinition) clone ).setValue( ( (PortableBooleanParameterDefinition) ppd ).getValue() );
            return clone;
        } else if ( ppd instanceof PortableEnumParameterDefinition ) {
            clone = new PortableEnumParameterDefinition();
            clone.setName( ppd.getName() );
            ( (PortableEnumParameterDefinition) clone ).setClassName( ppd.getClassName() );
            ( (PortableEnumParameterDefinition) clone ).setBinding( ( (PortableEnumParameterDefinition) ppd ).getBinding() );
            ( (PortableEnumParameterDefinition) clone ).setValues( ( (PortableEnumParameterDefinition) ppd ).getValues() );
            ( (PortableEnumParameterDefinition) clone ).setValue( ( (PortableEnumParameterDefinition) ppd ).getValue() );
            return clone;
        } else if ( ppd instanceof PortableFloatParameterDefinition ) {
            clone = new PortableFloatParameterDefinition();
            clone.setName( ppd.getName() );
            ( (PortableFloatParameterDefinition) clone ).setBinding( ( (PortableFloatParameterDefinition) ppd ).getBinding() );
            ( (PortableFloatParameterDefinition) clone ).setValue( ( (PortableFloatParameterDefinition) ppd ).getValue() );
            return clone;
        } else if ( ppd instanceof PortableIntegerParameterDefinition ) {
            clone = new PortableIntegerParameterDefinition();
            clone.setName( ppd.getName() );
            ( (PortableIntegerParameterDefinition) clone ).setBinding( ( (PortableIntegerParameterDefinition) ppd ).getBinding() );
            ( (PortableIntegerParameterDefinition) clone ).setValue( ( (PortableIntegerParameterDefinition) ppd ).getValue() );
            return clone;
        } else if ( ppd instanceof PortableListParameterDefinition ) {
            clone = new PortableListParameterDefinition();
            clone.setName( ppd.getName() );
            ( (PortableListParameterDefinition) clone ).setClassName( ppd.getClassName() );
            ( (PortableListParameterDefinition) clone ).setBinding( ( (PortableListParameterDefinition) ppd ).getBinding() );
            return clone;
        } else if ( ppd instanceof PortableObjectParameterDefinition ) {
            clone = new PortableObjectParameterDefinition();
            clone.setName( ppd.getName() );
            ( (PortableObjectParameterDefinition) clone ).setClassName( ppd.getClassName() );
            ( (PortableObjectParameterDefinition) clone ).setBinding( ( (PortableObjectParameterDefinition) ppd ).getBinding() );
            return clone;
        } else if ( ppd instanceof PortableStringParameterDefinition ) {
            clone = new PortableStringParameterDefinition();
            clone.setName( ppd.getName() );
            ( (PortableStringParameterDefinition) clone ).setBinding( ( (PortableStringParameterDefinition) ppd ).getBinding() );
            ( (PortableStringParameterDefinition) clone ).setValue( ( (PortableStringParameterDefinition) ppd ).getValue() );
            return clone;
        }
        throw new IllegalArgumentException( "Unrecognized PortableParameterDefinition" );
    }

    private void setupWorkItems( final ListBox workItemsListBox,
                                 final Set<PortableWorkDefinition> workItemDefinitions ) {
        workItemsListBox.clear();
        workItemsListBox.addItem( GuidedDecisionTableConstants.INSTANCE.NoWorkItemsAvailable() );
        workItemsListBox.setEnabled( false );

        //Add list of Work Item Definitions to list box
        if ( workItemDefinitions.size() > 0 ) {
            workItemsListBox.clear();
            workItemsListBox.setEnabled( true && !isReadOnly );
            workItemsListBox.addItem( GuidedDecisionTableConstants.INSTANCE.Choose(),
                                      "" );
            workItemDefinitionsMap = new HashMap<String, PortableWorkDefinition>();

            String selectedName = null;
            boolean isWorkItemSelected = false;
            if ( editingCol.getWorkItemDefinition() != null ) {
                selectedName = editingCol.getWorkItemDefinition().getName();
            }

            //Add items
            int i = 0;
            for ( PortableWorkDefinition wid : workItemDefinitions ) {
                workItemsListBox.addItem( wid.getDisplayName(),
                                          wid.getName() );
                workItemDefinitionsMap.put( wid.getName(),
                                            wid );
                if ( wid.getName().equals( selectedName ) ) {
                    workItemsListBox.setSelectedIndex( i + 1 );
                    isWorkItemSelected = true;
                }
                i++;
            }

            //Show parameters if a Work Item is pre-selected
            setAttributeVisibility( workItemInputParametersIndex,
                                    isWorkItemSelected );
            showWorkItemParameters();
        }
    }

    private void showWorkItemParameters() {

        //Hide parameter selections if a Work Item has not been selected
        PortableWorkDefinition wid = editingCol.getWorkItemDefinition();
        if ( wid == null ) {
            this.setAttributeVisibility( workItemInputParametersIndex,
                                         false );
            return;
        }

        //Show parameters
        this.setAttributeVisibility( workItemInputParametersIndex,
                                     true );

        //Input parameters
        workItemInputParameters.setParameters( wid.getParameters() );

    }

}
