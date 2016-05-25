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

import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import org.drools.workbench.models.guided.dtable.shared.model.ActionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.ActionRetractFactCol52;
import org.drools.workbench.models.guided.dtable.shared.model.BRLRuleModel;
import org.drools.workbench.models.guided.dtable.shared.model.DTCellValue52;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52.TableFormat;
import org.drools.workbench.models.guided.dtable.shared.model.LimitedEntryActionRetractFactCol52;
import org.drools.workbench.models.guided.dtable.shared.model.LimitedEntryCol;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableConstants;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.client.ui.TextBox;
import org.uberfire.ext.widgets.common.client.common.popups.FormStylePopup;
import org.uberfire.ext.widgets.common.client.common.popups.footers.ModalFooterOKCancelButtons;

/**
 * A popup to define the parameters of an Action to retract a Fact
 */
public class ActionRetractFactPopup extends FormStylePopup {

    //TODO {manstis} Popups need to MVP'ed
    private final GuidedDecisionTable52 model;

    private final BRLRuleModel rm;
    private final GuidedDecisionTableView.Presenter presenter;
    private final ActionRetractFactCol52 editingCol;
    private final ActionColumnCommand refreshGrid;
    private final ActionRetractFactCol52 originalCol;
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

    public ActionRetractFactPopup( final GuidedDecisionTable52 model,
                                   final GuidedDecisionTableView.Presenter presenter,
                                   final ActionColumnCommand refreshGrid,
                                   final ActionRetractFactCol52 column,
                                   final boolean isNew,
                                   final boolean isReadOnly ) {
        super( GuidedDecisionTableConstants.INSTANCE.ColumnConfigurationDeleteAFact() );
        this.rm = new BRLRuleModel( model );
        this.model = model;
        this.presenter = presenter;
        this.editingCol = cloneActionRetractColumn( column );
        this.refreshGrid = refreshGrid;
        this.originalCol = column;
        this.isNew = isNew;
        this.isReadOnly = isReadOnly;

        //Show available pattern bindings, if Limited Entry
        if ( model.getTableFormat() == TableFormat.LIMITED_ENTRY ) {
            final LimitedEntryActionRetractFactCol52 ler = (LimitedEntryActionRetractFactCol52) editingCol;
            final ListBox patterns = loadBoundFacts( ler.getValue().getStringValue() );
            patterns.setEnabled( !isReadOnly );
            if ( !isReadOnly ) {
                patterns.addClickHandler( new ClickHandler() {

                    public void onClick( ClickEvent event ) {
                        int index = patterns.getSelectedIndex();
                        if ( index > -1 ) {
                            ler.getValue().setStringValue( patterns.getValue( index ) );
                        }
                    }

                } );
            }
            addAttribute( GuidedDecisionTableConstants.INSTANCE.FactToDeleteColon(),
                          patterns );
        }

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

    private ActionRetractFactCol52 cloneActionRetractColumn( ActionRetractFactCol52 col ) {
        ActionRetractFactCol52 clone = null;
        if ( col instanceof LimitedEntryCol ) {
            clone = new LimitedEntryActionRetractFactCol52();
            DTCellValue52 dcv = new DTCellValue52( ( (LimitedEntryCol) col ).getValue().getStringValue() );
            ( (LimitedEntryCol) clone ).setValue( dcv );
        } else {
            clone = new ActionRetractFactCol52();
        }
        clone.setHeader( col.getHeader() );
        clone.setHideColumn( col.isHideColumn() );
        return clone;
    }

    private ListBox loadBoundFacts( String binding ) {
        ListBox listBox = new ListBox();
        listBox.addItem( GuidedDecisionTableConstants.INSTANCE.Choose() );
        List<String> factBindings = rm.getLHSBoundFacts();

        for ( int index = 0; index < factBindings.size(); index++ ) {
            String boundName = factBindings.get( index );
            if ( !"".equals( boundName ) ) {
                listBox.addItem( boundName );
                if ( boundName.equals( binding ) ) {
                    listBox.setSelectedIndex( index + 1 );
                }
            }
        }

        listBox.setEnabled( listBox.getItemCount() > 1 );
        if ( listBox.getItemCount() == 1 ) {
            listBox.clear();
            listBox.addItem( GuidedDecisionTableConstants.INSTANCE.NoPatternBindingsAvailable() );
        }
        return listBox;
    }

}
