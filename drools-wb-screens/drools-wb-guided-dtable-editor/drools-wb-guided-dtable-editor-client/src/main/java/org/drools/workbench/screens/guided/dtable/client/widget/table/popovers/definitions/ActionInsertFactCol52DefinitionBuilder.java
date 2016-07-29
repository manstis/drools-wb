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

package org.drools.workbench.screens.guided.dtable.client.widget.table.popovers.definitions;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.drools.workbench.models.guided.dtable.shared.model.ActionInsertFactCol52;
import org.drools.workbench.models.guided.dtable.shared.model.BaseColumn;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.dtable.client.widget.table.utilities.ColumnUtilities;
import org.drools.workbench.screens.guided.dtable.service.GuidedDecisionTableEditorService;
import org.jboss.errai.common.client.api.Caller;
import org.uberfire.client.callbacks.Callback;

@Dependent
public class ActionInsertFactCol52DefinitionBuilder extends BaseColumnDefinitionBuilder {

    @Inject
    public ActionInsertFactCol52DefinitionBuilder( final Caller<GuidedDecisionTableEditorService> service ) {
        super( service );
    }

    @Override
    public Class getSupportedColumnType() {
        return ActionInsertFactCol52.class;
    }

    @Override
    public void generateDefinition( final GuidedDecisionTableView.Presenter dtPresenter,
                                    final BaseColumn column,
                                    final Callback<String> afterGenerationCallback ) {
        if ( !( column instanceof ActionInsertFactCol52 ) ) {
            return;
        }

        final GuidedDecisionTable52 existingModel = dtPresenter.getModel();
        final GuidedDecisionTable52 partialModel = new GuidedDecisionTable52();
        final ColumnUtilities columnUtilities = new ColumnUtilities( existingModel,
                                                                     dtPresenter.getDataModelOracle() );

        final ActionInsertFactCol52 aif = (ActionInsertFactCol52) column;
        partialModel.getActionCols().add( aif );
        partialModel.getData().add( makeRowData( columnUtilities,
                                                 aif ) );

        generateDefinitionOnServer( partialModel,
                                    dtPresenter.getCurrentPath(),
                                    ( String drl ) -> afterGenerationCallback.callback( getRHS( drl ) ) );
    }

}
