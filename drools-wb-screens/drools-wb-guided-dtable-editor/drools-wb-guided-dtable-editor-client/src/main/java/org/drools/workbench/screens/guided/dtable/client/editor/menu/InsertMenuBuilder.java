/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
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

package org.drools.workbench.screens.guided.dtable.client.editor.menu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.IsWidget;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableModellerView;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.dtable.client.widget.table.events.cdi.DecisionTableSelectedEvent;
import org.drools.workbench.screens.guided.dtable.client.widget.table.events.cdi.DecisionTableSelectionsChangedEvent;
import org.uberfire.ext.wires.core.grids.client.model.IGridData;
import org.uberfire.workbench.model.menu.MenuFactory;
import org.uberfire.workbench.model.menu.MenuItem;
import org.uberfire.workbench.model.menu.impl.BaseMenuCustom;

@ApplicationScoped
public class InsertMenuBuilder implements MenuFactory.CustomMenuBuilder,
                                          InsertMenuView.Presenter {

    public interface SupportsAppendRow {

        void onAppendRow();

    }

    public interface SupportsInsertRowAbove {

        void onInsertRowAbove();

    }

    public interface SupportsInsertRowBelow {

        void onInsertRowBelow();

    }

    public interface SupportsInsertColumn {

        void onInsertColumn();

    }

    private InsertMenuView view;
    private GuidedDecisionTableModellerView.Presenter modeller;
    private GuidedDecisionTableView.Presenter activeDecisionTable;

    @Inject
    public InsertMenuBuilder( final InsertMenuView view ) {
        this.view = view;
    }

    @PostConstruct
    void setup() {
        view.init( this );
    }

    @Override
    public void setModeller( final GuidedDecisionTableModellerView.Presenter modeller ) {
        this.modeller = modeller;
    }

    @Override
    public void push( final MenuFactory.CustomMenuBuilder element ) {
    }

    @Override
    public MenuItem build() {
        return new BaseMenuCustom<IsWidget>() {
            @Override
            public IsWidget build() {
                return view;
            }

            @Override
            public boolean isEnabled() {
                return view.isEnabled();
            }

            @Override
            public void setEnabled( final boolean enabled ) {
                view.setEnabled( enabled );
            }

            @Override
            public String getSignatureId() {
                return "org.drools.workbench.screens.guided.dtable.client.editor.menu.InsertMenuBuilder";
            }

        };
    }

    public void onDecisionTableSelectedEvent( final @Observes DecisionTableSelectedEvent event ) {
        final GuidedDecisionTableView.Presenter dtPresenter = event.getPresenter();
        activeDecisionTable = dtPresenter;
        initialise();
    }

    public void onDecisionTableSelectionsChangedEvent( final @Observes DecisionTableSelectionsChangedEvent event ) {
        final GuidedDecisionTableView.Presenter dtPresenter = event.getPresenter();
        activeDecisionTable = dtPresenter;
        initialise();
    }

    @Override
    public void initialise() {
        if ( activeDecisionTable == null || !activeDecisionTable.getAccess().isEditable() ) {
            enableMenuItemsForAppending( false );
            enableMenuItemsForInsertingRows( false );
            return;
        }
        final List<IGridData.SelectedCell> selections = activeDecisionTable.getView().getModel().getSelectedCells();
        if ( selections == null || selections.isEmpty() ) {
            enableMenuItemsForAppending( true );
            enableMenuItemsForInsertingRows( false );
            return;
        }
        final Map<Integer, Boolean> rowUsage = new HashMap<>();
        for ( IGridData.SelectedCell sc : selections ) {
            rowUsage.put( sc.getRowIndex(),
                          true );
        }
        enableMenuItemsForAppending( true );
        enableMenuItemsForInsertingRows( rowUsage.keySet().size() == 1 );
    }

    @Override
    public void onAppendRow() {
        if ( activeDecisionTable != null ) {
            activeDecisionTable.onAppendRow();
        }

    }

    @Override
    public void onInsertRowAbove() {
        if ( activeDecisionTable != null ) {
            activeDecisionTable.onInsertRowAbove();
        }
    }

    @Override
    public void onInsertRowBelow() {
        if ( activeDecisionTable != null ) {
            activeDecisionTable.onInsertRowBelow();
        }
    }

    @Override
    public void onAppendColumn() {
        if ( modeller != null ) {
            modeller.onInsertColumn();
        }
    }

    private void enableMenuItemsForAppending( final boolean enabled ) {
        view.enableAppendRowMenuItem( enabled );
        view.enableAppendColumnMenuItem( enabled );
    }

    private void enableMenuItemsForInsertingRows( final boolean enabled ) {
        view.enableInsertRowAboveMenuItem( enabled );
        view.enableInsertRowBelowMenuItem( enabled );
    }

}
