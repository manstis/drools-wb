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

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.drools.workbench.models.datamodel.rule.BaseSingleFieldConstraint;
import org.drools.workbench.models.guided.dtable.shared.model.BaseColumn;
import org.drools.workbench.models.guided.dtable.shared.model.ConditionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.DescriptionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.RowNumberCol52;
import org.drools.workbench.screens.guided.dtable.client.editor.clipboard.Clipboard;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableErraiConstants;
import org.drools.workbench.screens.guided.dtable.client.widget.table.events.cdi.DecisionTableSelectedEvent;
import org.drools.workbench.screens.guided.dtable.client.widget.table.events.cdi.DecisionTableSelectionsChangedEvent;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.uberfire.ext.wires.core.grids.client.model.GridColumn;
import org.uberfire.ext.wires.core.grids.client.model.GridData;
import org.uberfire.mvp.Command;
import org.uberfire.workbench.model.menu.MenuFactory;
import org.uberfire.workbench.model.menu.MenuItem;

@Dependent
public class EditMenuBuilder extends BaseMenu implements MenuFactory.CustomMenuBuilder {

    public interface SupportsEditMenu {

        void onCut();

        void onCopy();

        void onPaste();

        void onDeleteSelectedCells();

        void onDeleteSelectedColumns();

        void onDeleteSelectedRows();

        void onOtherwiseCell();
    }

    private Clipboard clipboard;
    private TranslationService ts;

    MenuItem miCut;
    MenuItem miCopy;
    MenuItem miPaste;
    MenuItem miDeleteSelectedCells;
    MenuItem miDeleteSelectedColumns;
    MenuItem miDeleteSelectedRows;
    MenuItem miOtherwiseCell;

    @Inject
    public EditMenuBuilder(final Clipboard clipboard,
                           final TranslationService ts) {
        this.clipboard = clipboard;
        this.ts = ts;
    }

    @PostConstruct
    public void setup() {
        miCut = makeMenuItem(ts.getTranslation(GuidedDecisionTableErraiConstants.EditMenuViewImpl_cut),
                             this::onCut);
        miCopy = makeMenuItem(ts.getTranslation(GuidedDecisionTableErraiConstants.EditMenuViewImpl_copy),
                              this::onCopy);
        miPaste = makeMenuItem(ts.getTranslation(GuidedDecisionTableErraiConstants.EditMenuViewImpl_paste),
                               this::onPaste);
        miDeleteSelectedCells = makeMenuItem(ts.getTranslation(GuidedDecisionTableErraiConstants.EditMenuViewImpl_deleteCells),
                                             this::onDeleteSelectedCells);
        miDeleteSelectedColumns = makeMenuItem(ts.getTranslation(GuidedDecisionTableErraiConstants.EditMenuViewImpl_deleteColumns),
                                               this::onDeleteSelectedColumns);
        miDeleteSelectedRows = makeMenuItem(ts.getTranslation(GuidedDecisionTableErraiConstants.EditMenuViewImpl_deleteRows),
                                            this::onDeleteSelectedRows);
        miOtherwiseCell = makeMenuItem(ts.getTranslation(GuidedDecisionTableErraiConstants.EditMenuViewImpl_otherwise),
                                       this::onOtherwiseCell);
    }

    @Override
    public void push(final MenuFactory.CustomMenuBuilder element) {
    }

    @Override
    public MenuItem build() {
        return MenuFactory.newTopLevelMenu(ts.getTranslation(GuidedDecisionTableErraiConstants.EditMenuViewImpl_title))
                .withItems(getEditMenuItems())
                .endMenu()
                .build()
                .getItems()
                .get(0);
    }

    List<MenuItem> getEditMenuItems() {
        ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
        menuItems.add(miCut);
        menuItems.add(miCopy);
        menuItems.add(miPaste);
        menuItems.add(miDeleteSelectedCells);
        menuItems.add(miDeleteSelectedColumns);
        menuItems.add(miDeleteSelectedRows);
        menuItems.add(miOtherwiseCell);
        return menuItems;
    }

    MenuItem makeMenuItem(final String caption,
                          final Command cmd) {
        return MenuFactory.newSimpleItem(caption)
                .respondsWith(cmd)
                .endMenu()
                .build()
                .getItems()
                .get(0);
    }

    @Override
    public void onDecisionTableSelectedEvent(final @Observes DecisionTableSelectedEvent event) {
        super.onDecisionTableSelectedEvent(event);
    }

    @Override
    public void onDecisionTableSelectionsChangedEvent(final @Observes DecisionTableSelectionsChangedEvent event) {
        super.onDecisionTableSelectionsChangedEvent(event);
    }

    @Override
    public void initialise() {
        if (activeDecisionTable == null || !activeDecisionTable.getAccess().isEditable()) {
            disableMenuItems();
            return;
        }
        final List<GridData.SelectedCell> selections = activeDecisionTable.getView().getModel().getSelectedCells();
        if (selections == null || selections.isEmpty()) {
            disableMenuItems();
            return;
        }
        enableMenuItems(selections);
        setupOtherwiseCellEntry(selections);
    }

    void onCut() {
        if (activeDecisionTable != null) {
            activeDecisionTable.onCut();
        }
    }

    void onCopy() {
        if (activeDecisionTable != null) {
            activeDecisionTable.onCopy();
        }
    }

    void onPaste() {
        if (activeDecisionTable != null) {
            activeDecisionTable.onPaste();
        }
    }

    void onDeleteSelectedCells() {
        if (activeDecisionTable != null) {
            activeDecisionTable.onDeleteSelectedCells();
        }
    }

    void onDeleteSelectedColumns() {
        if (activeDecisionTable != null) {
            activeDecisionTable.onDeleteSelectedColumns();
        }
    }

    void onDeleteSelectedRows() {
        if (activeDecisionTable != null) {
            activeDecisionTable.onDeleteSelectedRows();
        }
    }

    void onOtherwiseCell() {
// TODO {manstis} miOtherwiseCell needs to be a Custom Widget to handle a check-mark
//        if (activeDecisionTable != null) {
//            view.setOtherwiseCell(true);
//            activeDecisionTable.onOtherwiseCell();
//        }
    }

    private void disableMenuItems() {
        miCut.setEnabled(false);
        miCopy.setEnabled(false);
        miPaste.setEnabled(false);
        miDeleteSelectedCells.setEnabled(false);
        miDeleteSelectedColumns.setEnabled(false);
        miDeleteSelectedRows.setEnabled(false);
        miOtherwiseCell.setEnabled(false);
    }

    private void enableMenuItems(final List<GridData.SelectedCell> selections) {
        final boolean enabled = selections.size() > 0;
        final boolean isOtherwiseEnabled = isOtherwiseEnabled(selections);
        final boolean isOnlyMandatoryColumnSelected = isOnlyMandatoryColumnSelected(selections);

        miCut.setEnabled(enabled);
        miCopy.setEnabled(enabled);
        miPaste.setEnabled(clipboard.hasData());
        miDeleteSelectedCells.setEnabled(enabled);
        miDeleteSelectedColumns.setEnabled(enabled && !isOnlyMandatoryColumnSelected);
        miDeleteSelectedRows.setEnabled(enabled);
        miOtherwiseCell.setEnabled(isOtherwiseEnabled);
    }

    private void setupOtherwiseCellEntry(final List<GridData.SelectedCell> selections) {
// TODO {manstis} miOtherwiseCell needs to be a Custom Widget to handle a check-mark
//        if (selections.size() != 1) {
//            view.setOtherwiseCell(false);
//            return;
//        }
//        final GridData.SelectedCell selection = selections.get(0);
//        final int rowIndex = selection.getRowIndex();
//        final int columnIndex = findUiColumnIndex(selection.getColumnIndex());
//        final boolean isOtherwiseCell = activeDecisionTable.getModel().getData().get(rowIndex).get(columnIndex).isOtherwise();
//        view.setOtherwiseCell(isOtherwiseCell);
    }

    //Check whether the "otherwise" menu item can be enabled
    private boolean isOtherwiseEnabled(final List<GridData.SelectedCell> selections) {
        if (selections.size() != 1) {
            return false;
        }
        boolean isOtherwiseEnabled = true;
        final GridData.SelectedCell selection = selections.get(0);
        final int columnIndex = findUiColumnIndex(selection.getColumnIndex());
        final BaseColumn column = activeDecisionTable.getModel().getExpandedColumns().get(columnIndex);
        isOtherwiseEnabled = isOtherwiseEnabled && canAcceptOtherwiseValues(column);
        return isOtherwiseEnabled;
    }

    //Check whether column selection is only RowNumberColumn or DescriptionColumn. These cannot be deleted.
    private boolean isOnlyMandatoryColumnSelected(final List<GridData.SelectedCell> selections) {
        boolean isOnlyMandatoryColumnSelected = true;
        for (GridData.SelectedCell sc : selections) {
            final int columnIndex = findUiColumnIndex(sc.getColumnIndex());
            final BaseColumn column = activeDecisionTable.getModel().getExpandedColumns().get(columnIndex);
            if (!((column instanceof RowNumberCol52) || (column instanceof DescriptionCol52))) {
                isOnlyMandatoryColumnSelected = false;
            }
        }
        return isOnlyMandatoryColumnSelected;
    }

    private int findUiColumnIndex(final int modelColumnIndex) {
        final List<GridColumn<?>> columns = activeDecisionTable.getView().getModel().getColumns();
        for (int uiColumnIndex = 0; uiColumnIndex < columns.size(); uiColumnIndex++) {
            final GridColumn<?> c = columns.get(uiColumnIndex);
            if (c.getIndex() == modelColumnIndex) {
                return uiColumnIndex;
            }
        }
        throw new IllegalStateException("Column was not found!");
    }

    // Check whether the given column can accept "otherwise" values
    private boolean canAcceptOtherwiseValues(final BaseColumn column) {
        if (!(column instanceof ConditionCol52)) {
            return false;
        }
        final ConditionCol52 cc = (ConditionCol52) column;

        //Check column contains literal values and uses the equals operator
        if (cc.getConstraintValueType() != BaseSingleFieldConstraint.TYPE_LITERAL) {
            return false;
        }

        //Check operator is supported
        if (cc.getOperator() == null) {
            return false;
        }
        if (cc.getOperator().equals("==")) {
            return true;
        }
        if (cc.getOperator().equals("!=")) {
            return true;
        }
        return false;
    }
}
