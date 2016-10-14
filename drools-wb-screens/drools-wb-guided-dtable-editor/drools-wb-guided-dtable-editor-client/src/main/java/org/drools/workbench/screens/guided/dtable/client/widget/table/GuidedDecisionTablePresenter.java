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
package org.drools.workbench.screens.guided.dtable.client.widget.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Command;
import org.drools.workbench.models.datamodel.oracle.DateConverter;
import org.drools.workbench.models.datamodel.oracle.DropDownData;
import org.drools.workbench.models.datamodel.rule.FactPattern;
import org.drools.workbench.models.datamodel.rule.IPattern;
import org.drools.workbench.models.datamodel.workitems.PortableWorkDefinition;
import org.drools.workbench.models.guided.dtable.shared.auditlog.DeleteColumnAuditLogEntry;
import org.drools.workbench.models.guided.dtable.shared.auditlog.DeleteRowAuditLogEntry;
import org.drools.workbench.models.guided.dtable.shared.auditlog.InsertColumnAuditLogEntry;
import org.drools.workbench.models.guided.dtable.shared.auditlog.InsertRowAuditLogEntry;
import org.drools.workbench.models.guided.dtable.shared.auditlog.UpdateColumnAuditLogEntry;
import org.drools.workbench.models.guided.dtable.shared.model.ActionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.ActionInsertFactCol52;
import org.drools.workbench.models.guided.dtable.shared.model.ActionRetractFactCol52;
import org.drools.workbench.models.guided.dtable.shared.model.ActionSetFieldCol52;
import org.drools.workbench.models.guided.dtable.shared.model.ActionWorkItemCol52;
import org.drools.workbench.models.guided.dtable.shared.model.ActionWorkItemInsertFactCol52;
import org.drools.workbench.models.guided.dtable.shared.model.ActionWorkItemSetFieldCol52;
import org.drools.workbench.models.guided.dtable.shared.model.AttributeCol52;
import org.drools.workbench.models.guided.dtable.shared.model.BRLActionColumn;
import org.drools.workbench.models.guided.dtable.shared.model.BRLConditionColumn;
import org.drools.workbench.models.guided.dtable.shared.model.BRLRuleModel;
import org.drools.workbench.models.guided.dtable.shared.model.BaseColumn;
import org.drools.workbench.models.guided.dtable.shared.model.BaseColumnFieldDiff;
import org.drools.workbench.models.guided.dtable.shared.model.CompositeColumn;
import org.drools.workbench.models.guided.dtable.shared.model.ConditionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.DTCellValue52;
import org.drools.workbench.models.guided.dtable.shared.model.DescriptionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.models.guided.dtable.shared.model.LimitedEntryBRLActionColumn;
import org.drools.workbench.models.guided.dtable.shared.model.LimitedEntryBRLConditionColumn;
import org.drools.workbench.models.guided.dtable.shared.model.MetadataCol52;
import org.drools.workbench.models.guided.dtable.shared.model.Pattern52;
import org.drools.workbench.models.guided.dtable.shared.model.RowNumberCol52;
import org.drools.workbench.screens.guided.dtable.client.GuidedDecisionTable;
import org.drools.workbench.screens.guided.dtable.client.editor.clipboard.Clipboard;
import org.drools.workbench.screens.guided.dtable.client.editor.clipboard.impl.DefaultClipboard;
import org.drools.workbench.screens.guided.dtable.client.type.GuidedDTableResourceType;
import org.drools.workbench.screens.guided.dtable.client.widget.analysis.DecisionTableAnalyzerProvider;
import org.drools.workbench.screens.guided.dtable.client.widget.analysis.controller.AnalyzerController;
import org.drools.workbench.screens.guided.dtable.client.widget.auditlog.AuditLog;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.BooleanUiColumn;
import org.drools.workbench.screens.guided.dtable.client.widget.table.events.cdi.DecisionTableColumnSelectedEvent;
import org.drools.workbench.screens.guided.dtable.client.widget.table.events.cdi.DecisionTableSelectedEvent;
import org.drools.workbench.screens.guided.dtable.client.widget.table.events.cdi.DecisionTableSelectionsChangedEvent;
import org.drools.workbench.screens.guided.dtable.client.widget.table.events.cdi.RefreshActionsPanelEvent;
import org.drools.workbench.screens.guided.dtable.client.widget.table.events.cdi.RefreshAttributesPanelEvent;
import org.drools.workbench.screens.guided.dtable.client.widget.table.events.cdi.RefreshConditionsPanelEvent;
import org.drools.workbench.screens.guided.dtable.client.widget.table.events.cdi.RefreshMetaDataPanelEvent;
import org.drools.workbench.screens.guided.dtable.client.widget.table.lockmanager.GuidedDecisionTableLockManager;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.GuidedDecisionTableUiCell;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.GuidedDecisionTableUiModel;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.converters.cell.GridWidgetCellFactory;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.converters.column.BaseColumnConverter;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.converters.column.GridWidgetColumnFactory;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.synchronizers.ModelSynchronizer;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.synchronizers.Synchronizer;
import org.drools.workbench.screens.guided.dtable.client.widget.table.themes.GuidedDecisionTableRenderer;
import org.drools.workbench.screens.guided.dtable.client.widget.table.utilities.CellUtilities;
import org.drools.workbench.screens.guided.dtable.client.widget.table.utilities.ColumnUtilities;
import org.drools.workbench.screens.guided.dtable.client.widget.table.utilities.DependentEnumsUtilities;
import org.drools.workbench.screens.guided.dtable.client.widget.table.utilities.EnumLoaderUtilities;
import org.drools.workbench.screens.guided.dtable.model.GuidedDecisionTableEditorContent;
import org.drools.workbench.screens.guided.dtable.service.GuidedDecisionTableLinkManager;
import org.drools.workbench.screens.guided.rule.client.util.GWTDateConverter;
import org.guvnor.common.services.shared.metadata.model.Overview;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.ioc.client.container.SyncBeanDef;
import org.jboss.errai.ioc.client.container.SyncBeanManager;
import org.jboss.errai.security.shared.api.identity.User;
import org.kie.workbench.common.services.datamodel.model.PackageDataModelOracleBaselinePayload;
import org.kie.workbench.common.services.shared.rulename.RuleNamesService;
import org.kie.workbench.common.widgets.client.datamodel.AsyncPackageDataModelOracle;
import org.kie.workbench.common.widgets.client.datamodel.AsyncPackageDataModelOracleFactory;
import org.kie.workbench.common.widgets.client.resources.i18n.CommonConstants;
import org.uberfire.backend.vfs.ObservablePath;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.client.mvp.LockTarget;
import org.uberfire.client.mvp.UpdatedLockStatusEvent;
import org.uberfire.ext.wires.core.grids.client.model.GridColumn;
import org.uberfire.ext.wires.core.grids.client.model.GridData;
import org.uberfire.ext.wires.core.grids.client.model.GridRow;
import org.uberfire.ext.wires.core.grids.client.model.impl.BaseGridRow;
import org.uberfire.ext.wires.core.grids.client.widget.dom.HasDOMElementResources;
import org.uberfire.ext.wires.core.grids.client.widget.grid.GridWidget;
import org.uberfire.ext.wires.core.grids.client.widget.grid.selections.impl.RowSelectionStrategy;
import org.uberfire.ext.wires.core.grids.client.widget.layer.pinning.TransformMediator;
import org.uberfire.mvp.ParameterizedCommand;
import org.uberfire.mvp.PlaceRequest;
import org.uberfire.workbench.events.NotificationEvent;

import static org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTablePresenter.Access.LockedBy.*;
import static org.drools.workbench.screens.guided.dtable.client.widget.table.model.synchronizers.Synchronizer.*;

@Dependent
public class GuidedDecisionTablePresenter implements GuidedDecisionTableView.Presenter {

    private final User identity;
    private final GuidedDTableResourceType resourceType;
    private final Caller<RuleNamesService> ruleNameService;
    private final Event<DecisionTableSelectedEvent> decisionTableSelectedEvent;
    private final Event<DecisionTableColumnSelectedEvent> decisionTableColumnSelectedEvent;
    private final Event<DecisionTableSelectionsChangedEvent> decisionTableSelectionsChangedEvent;
    private final Event<RefreshAttributesPanelEvent> refreshAttributesPanelEvent;
    private final Event<RefreshMetaDataPanelEvent> refreshMetaDataPanelEvent;
    private final Event<RefreshConditionsPanelEvent> refreshConditionsPanelEvent;
    private final Event<RefreshActionsPanelEvent> refreshActionsPanelEvent;
    private final Event<NotificationEvent> notificationEvent;
    private final GridWidgetCellFactory gridWidgetCellFactory;
    private final GridWidgetColumnFactory gridWidgetColumnFactory;
    private final AsyncPackageDataModelOracleFactory oracleFactory;
    private final ModelSynchronizer synchronizer;
    private final SyncBeanManager beanManager;
    private final GuidedDecisionTableLockManager lockManager;
    private final GuidedDecisionTableLinkManager linkManager;
    private final Clipboard clipboard;
    private final DecisionTableAnalyzerProvider decisionTableAnalyzerProvider;
    private final EnumLoaderUtilities enumLoaderUtilities;

    private final Access access = new Access();

    private GuidedDecisionTable52 model;
    private Overview overview;
    private AsyncPackageDataModelOracle oracle;
    private GuidedDecisionTableModellerView.Presenter parent;
    private BRLRuleModel rm;

    private GuidedDecisionTableUiModel uiModel;
    private GuidedDecisionTableView view;

    private AuditLog auditLog;

    protected CellUtilities cellUtilities;
    protected ColumnUtilities columnUtilities;
    protected DependentEnumsUtilities dependentEnumsUtilities;

    protected AnalyzerController analyzerController;

    private String version = null;
    private ObservablePath latestPath = null;
    private ObservablePath currentPath = null;
    private PlaceRequest placeRequest = null;

    private Integer originalHashCode = null;
    private ObservablePath.OnConcurrentUpdateEvent concurrentUpdateSessionInfo = null;

    //This EventBus is local to the screen and should be used for local operations, set data, add rows etc
    private EventBus eventBus = new SimpleEventBus();

    public static class Access {

        public enum LockedBy {
            CURRENT_USER,
            OTHER_USER,
            NOBODY
        }

        private LockedBy lock = NOBODY;
        private boolean isReadOnly = false;

        public LockedBy getLock() {
            return lock;
        }

        public void setLock( final LockedBy lock ) {
            this.lock = lock;
        }

        public boolean isReadOnly() {
            return isReadOnly;
        }

        public void setReadOnly( final boolean isReadOnly ) {
            this.isReadOnly = isReadOnly;
        }

        public boolean isEditable() {
            return !( lock == OTHER_USER || isReadOnly );
        }

    }

    private interface VetoableCommand {

        void execute() throws ModelSynchronizer.MoveColumnVetoException;

    }

    private interface VetoableUpdateCommand {

        List<BaseColumnFieldDiff> execute() throws ModelSynchronizer.MoveColumnVetoException;

    }

    @Inject
    public GuidedDecisionTablePresenter( final User identity,
                                         final GuidedDTableResourceType resourceType,
                                         final Caller<RuleNamesService> ruleNameService,
                                         final Event<DecisionTableSelectedEvent> decisionTableSelectedEvent,
                                         final Event<DecisionTableColumnSelectedEvent> decisionTableColumnSelectedEvent,
                                         final Event<DecisionTableSelectionsChangedEvent> decisionTableSelectionsChangedEvent,
                                         final Event<RefreshAttributesPanelEvent> refreshAttributesPanelEvent,
                                         final Event<RefreshMetaDataPanelEvent> refreshMetaDataPanelEvent,
                                         final Event<RefreshConditionsPanelEvent> refreshConditionsPanelEvent,
                                         final Event<RefreshActionsPanelEvent> refreshActionsPanelEvent,
                                         final Event<NotificationEvent> notificationEvent,
                                         final GridWidgetCellFactory gridWidgetCellFactory,
                                         final GridWidgetColumnFactory gridWidgetColumnFactory,
                                         final AsyncPackageDataModelOracleFactory oracleFactory,
                                         final ModelSynchronizer synchronizer,
                                         final SyncBeanManager beanManager,
                                         final @GuidedDecisionTable GuidedDecisionTableLockManager lockManager,
                                         final GuidedDecisionTableLinkManager linkManager,
                                         final Clipboard clipboard,
                                         final DecisionTableAnalyzerProvider decisionTableAnalyzerProvider,
                                         final EnumLoaderUtilities enumLoaderUtilities ) {
        this.identity = identity;
        this.resourceType = resourceType;
        this.ruleNameService = ruleNameService;
        this.decisionTableSelectedEvent = decisionTableSelectedEvent;
        this.decisionTableColumnSelectedEvent = decisionTableColumnSelectedEvent;
        this.decisionTableSelectionsChangedEvent = decisionTableSelectionsChangedEvent;
        this.refreshAttributesPanelEvent = refreshAttributesPanelEvent;
        this.refreshMetaDataPanelEvent = refreshMetaDataPanelEvent;
        this.refreshConditionsPanelEvent = refreshConditionsPanelEvent;
        this.refreshActionsPanelEvent = refreshActionsPanelEvent;
        this.notificationEvent = notificationEvent;
        this.gridWidgetCellFactory = gridWidgetCellFactory;
        this.gridWidgetColumnFactory = gridWidgetColumnFactory;
        this.oracleFactory = oracleFactory;
        this.synchronizer = synchronizer;
        this.beanManager = beanManager;
        this.lockManager = lockManager;
        this.linkManager = linkManager;
        this.clipboard = clipboard;
        this.decisionTableAnalyzerProvider = decisionTableAnalyzerProvider;
        this.enumLoaderUtilities = enumLoaderUtilities;

        CellUtilities.injectDateConvertor( getDateConverter() );
    }

    DateConverter getDateConverter() {
        return GWTDateConverter.getInstance();
    }

    @Override
    public void activate() {
        lockManager.fireChangeTitleEvent();
    }

    @Override
    public GuidedDecisionTable52 getModel() {
        return this.model;
    }

    GridData getUiModel() {
        return this.uiModel;
    }

    @Override
    public AsyncPackageDataModelOracle getDataModelOracle() {
        return this.oracle;
    }

    @Override
    public Overview getOverview() {
        return this.overview;
    }

    @Override
    public GuidedDecisionTableView getView() {
        return view;
    }

    @Override
    public GuidedDecisionTableModellerView.Presenter getModellerPresenter() {
        return parent;
    }

    @Override
    public void setContent( final ObservablePath path,
                            final PlaceRequest placeRequest,
                            final GuidedDecisionTableEditorContent content,
                            final GuidedDecisionTableModellerView.Presenter parent,
                            final boolean isReadOnly ) {
        this.parent = parent;
        this.latestPath = path;

        initialiseContent( path,
                           placeRequest,
                           content,
                           isReadOnly );
    }

    @Override
    public void refreshContent( final ObservablePath path,
                                final PlaceRequest placeRequest,
                                final GuidedDecisionTableEditorContent content,
                                final boolean isReadOnly ) {
        onClose();

        initialiseContent( path,
                           placeRequest,
                           content,
                           isReadOnly );
    }

    void initialiseContent( final ObservablePath path,
                            final PlaceRequest placeRequest,
                            final GuidedDecisionTableEditorContent content,
                            final boolean isReadOnly ) {
        final GuidedDecisionTable52 model = content.getModel();
        final PackageDataModelOracleBaselinePayload dataModel = content.getDataModel();
        final Set<PortableWorkDefinition> workItemDefinitions = content.getWorkItemDefinitions();

        this.currentPath = path;
        this.placeRequest = placeRequest;
        this.model = model;
        this.overview = content.getOverview();
        this.oracle = oracleFactory.makeAsyncPackageDataModelOracle( path,
                                                                     model,
                                                                     dataModel );
        this.access.setReadOnly( isReadOnly );
        this.rm = new BRLRuleModel( model );

        this.uiModel = makeUiModel();
        this.view = makeView( workItemDefinitions );

        initialiseLockManager();
        initialiseUtilities();
        initialiseModels();
        initialiseValidationAndVerification();
        initialiseAuditLog();
    }

    //Setup LockManager
    void initialiseLockManager() {
        lockManager.init( new LockTarget( currentPath,
                                          parent.getView().asWidget(),
                                          placeRequest,
                                          () -> currentPath.getFileName() + " - " + resourceType.getDescription(),
                                          () -> {/*nothing*/} ),
                          parent );
    }

    //Instantiate UiModel overriding cell selection to inform MenuItems about changes to selected cells.
    GuidedDecisionTableUiModel makeUiModel() {
        return new GuidedDecisionTableUiModel( synchronizer ) {
            @Override
            public Range selectCell( final int rowIndex,
                                     final int columnIndex ) {
                final Range rows = super.selectCell( rowIndex,
                                                     columnIndex );
                decisionTableSelectionsChangedEvent.fire( new DecisionTableSelectionsChangedEvent( GuidedDecisionTablePresenter.this ) );
                return rows;
            }

            @Override
            public Range selectCells( final int rowIndex,
                                      final int columnIndex,
                                      final int width,
                                      final int height ) {
                final Range rows = super.selectCells( rowIndex,
                                                      columnIndex,
                                                      width,
                                                      height );
                decisionTableSelectionsChangedEvent.fire( new DecisionTableSelectionsChangedEvent( GuidedDecisionTablePresenter.this ) );
                return rows;
            }

            @Override
            public boolean isRowDraggingEnabled() {
                return access.isEditable();
            }

            @Override
            public boolean isColumnDraggingEnabled() {
                return access.isEditable();
            }
        };
    }

    GuidedDecisionTableView makeView( final Set<PortableWorkDefinition> workItemDefinitions ) {
        return new GuidedDecisionTableViewImpl( uiModel,
                                                new GuidedDecisionTableRenderer( uiModel,
                                                                                 model ),
                                                this,
                                                model,
                                                oracle,
                                                workItemDefinitions,
                                                notificationEvent,
                                                eventBus,
                                                access );
    }

    void initialiseUtilities() {
        this.cellUtilities = new CellUtilities();
        this.columnUtilities = new ColumnUtilities( model,
                                                    oracle );

        //Setup the DropDownManager that requires the Model and UI data to determine drop-down lists
        //for dependent enumerations. This needs to be called before the columns are created.
        this.dependentEnumsUtilities = new DependentEnumsUtilities( model,
                                                                    oracle );

        //Setup Factories for new Columns and Cells
        gridWidgetColumnFactory.setConverters( getConverters() );
        gridWidgetColumnFactory.initialise( model,
                                            oracle,
                                            columnUtilities,
                                            this );

        //Setup synchronizers to update the Model when the UiModel changes.
        synchronizer.setSynchronizers( getSynchronizers() );
        synchronizer.initialise( model,
                                 uiModel,
                                 cellUtilities,
                                 columnUtilities,
                                 dependentEnumsUtilities,
                                 gridWidgetCellFactory,
                                 gridWidgetColumnFactory,
                                 view,
                                 rm,
                                 eventBus,
                                 access );

    }

    //Copy Model data to UiModel.
    void initialiseModels() {
        initialiseLegacyColumnDataTypes();
        final List<BaseColumn> modelColumns = model.getExpandedColumns();
        for ( BaseColumn column : modelColumns ) {
            initialiseColumn( column );
        }
        for ( List<DTCellValue52> row : model.getData() ) {
            initialiseRow( modelColumns,
                           row );
        }
    }

    //Ensure field data-type is set (field did not exist before 5.2)
    void initialiseLegacyColumnDataTypes() {
        for ( CompositeColumn<?> column : model.getConditions() ) {
            if ( column instanceof Pattern52 ) {
                final Pattern52 pattern = (Pattern52) column;
                for ( ConditionCol52 condition : pattern.getChildColumns() ) {
                    condition.setFieldType( oracle.getFieldType( pattern.getFactType(),
                                                                 condition.getFactField() ) );
                }
            }
        }
    }

    //Setup the Validation & Verification analyzer
    void initialiseValidationAndVerification() {
        this.analyzerController = decisionTableAnalyzerProvider.newAnalyzer( placeRequest,
                                                                             oracle,
                                                                             model,
                                                                             eventBus );
    }

    //Setup Audit Log
    void initialiseAuditLog() {
        this.auditLog = new AuditLog( model,
                                      identity );
    }

    @Override
    public void link( final Set<GuidedDecisionTableView.Presenter> dtPresenters ) {
        final Set<GuidedDecisionTableView.Presenter> otherDecisionTables = new HashSet<>();
        otherDecisionTables.addAll( dtPresenters );
        otherDecisionTables.remove( this );
        otherDecisionTables.stream().forEach( ( e ) -> linkManager.link( this.getModel(),
                                                                         e.getModel(),
                                                                         ( final int sourceColumnIndex,
                                                                           final int targetColumnIndex ) -> {
                                                                             final GridData sourceUiModel = GuidedDecisionTablePresenter.this.getView().getModel();
                                                                             final GridData targetUiModel = e.getView().getModel();
                                                                             sourceUiModel.getColumns().get( sourceColumnIndex ).setLink( targetUiModel.getColumns().get( targetColumnIndex ) );
                                                                         } ) );
    }

    List<BaseColumnConverter> getConverters() {
        final List<BaseColumnConverter> converters = new ArrayList<BaseColumnConverter>();
        for ( SyncBeanDef<BaseColumnConverter> bean : beanManager.lookupBeans( BaseColumnConverter.class ) ) {
            converters.add( bean.getInstance() );
        }
        Collections.sort( converters,
                          new Comparator<BaseColumnConverter>() {
                              @Override
                              public int compare( final BaseColumnConverter o1,
                                                  final BaseColumnConverter o2 ) {
                                  return o2.priority() - o1.priority();
                              }
                          } );
        return converters;
    }

    @SuppressWarnings("unchecked")
    List<Synchronizer<? extends MetaData, ? extends MetaData, ? extends MetaData, ? extends MetaData, ? extends MetaData>> getSynchronizers() {
        final List<Synchronizer<? extends MetaData, ? extends MetaData, ? extends MetaData, ? extends MetaData, ? extends MetaData>> synchronizers = new ArrayList<>();
        for ( SyncBeanDef<Synchronizer> bean : beanManager.lookupBeans( Synchronizer.class ) ) {
            synchronizers.add( bean.getInstance() );
        }
        return synchronizers;
    }

    @Override
    public Access getAccess() {
        return this.access;
    }

    @Override
    public void onClose() {
        terminateAnalysis();

        if ( uiModel != null ) {
            for ( GridColumn<?> column : uiModel.getColumns() ) {
                if ( column.getColumnRenderer() instanceof HasDOMElementResources ) {
                    ( (HasDOMElementResources) column.getColumnRenderer() ).destroyResources();
                }
            }
        }

        lockManager.releaseLock();
        oracleFactory.destroy( oracle );
    }

    @Override
    public void initialiseAnalysis() {
        if ( analyzerController != null ) {
            analyzerController.initialiseAnalysis();
        }
    }

    @Override
    public void terminateAnalysis() {
        if ( analyzerController != null ) {
            analyzerController.terminateAnalysis();
        }
    }

    @Override
    @SuppressWarnings("unused")
    public void select( final GridWidget selectedGridWidget ) {
        decisionTableSelectedEvent.fire( new DecisionTableSelectedEvent( this ) );
        if ( !isReadOnly() ) {
            lockManager.acquireLock();
        }
    }

    void onUpdatedLockStatusEvent( final @Observes UpdatedLockStatusEvent event ) {
        if ( currentPath == null ) {
            return;
        }
        if ( currentPath.equals( event.getFile() ) ) {
            if ( event.isLocked() ) {
                access.setLock( event.isLockedByCurrentUser() ? CURRENT_USER : OTHER_USER );
            } else {
                access.setLock( NOBODY );
            }
            parent.onLockStatusUpdated( this );
        }
    }

    @Override
    public void selectLinkedColumn( final GridColumn<?> column ) {
        decisionTableColumnSelectedEvent.fire( new DecisionTableColumnSelectedEvent( column ) );
    }

    @Override
    public Set<GridWidget> getGridWidgets() {
        return parent.getView().getGridWidgets();
    }

    @Override
    public void enterPinnedMode( final GridWidget gridWidget,
                                 final Command onStartCommand ) {
        parent.enterPinnedMode( gridWidget,
                                onStartCommand );
    }

    @Override
    public void exitPinnedMode( final Command onCompleteCommand ) {
        parent.exitPinnedMode( onCompleteCommand );
    }

    @Override
    public void updatePinnedContext( final GridWidget gridWidget ) throws IllegalStateException {
        parent.updatePinnedContext( gridWidget );
    }

    @Override
    public PinnedContext getPinnedContext() {
        return parent.getPinnedContext();
    }

    @Override
    public boolean isGridPinned() {
        return parent.isGridPinned();
    }

    @Override
    public TransformMediator getDefaultTransformMediator() {
        return parent.getDefaultTransformMediator();
    }

    @Override
    public void getPackageParentRuleNames( final ParameterizedCommand<Collection<String>> command ) {
        ruleNameService.call( new RemoteCallback<Collection<String>>() {
            @Override
            public void callback( final Collection<String> ruleNames ) {
                command.execute( ruleNames );
            }
        } ).getRuleNames( getCurrentPath(),
                          model.getPackageName() );
    }

    @Override
    public void setParentRuleName( final String parentName ) {
        model.setParentName( parentName );
    }

    @Override
    public boolean hasColumnDefinitions() {
        return model.getAttributeCols().size() > 0
                || model.getConditionsCount() > 0
                || model.getActionCols().size() > 0;
    }

    @Override
    public Set<String> getBindings( final String className ) {
        //For some reason, Fact Pattern data-types use the leaf name of the fully qualified Class Name
        //whereas Fields use the fully qualified Class Name. We don't use the generic fieldType (see
        //SuggestionCompletionEngine.TYPE) as we can't distinguish between different numeric types
        String simpleClassName = className;
        if ( simpleClassName != null && simpleClassName.lastIndexOf( "." ) > 0 ) {
            simpleClassName = simpleClassName.substring( simpleClassName.lastIndexOf( "." ) + 1 );
        }
        Set<String> bindings = new HashSet<String>();
        for ( Pattern52 p : model.getPatterns() ) {
            if ( className == null || p.getFactType().equals( simpleClassName ) ) {
                String binding = p.getBoundName();
                if ( !( binding == null || "".equals( binding ) ) ) {
                    bindings.add( binding );
                }
            }
            for ( ConditionCol52 c : p.getChildColumns() ) {
                if ( c.isBound() ) {
                    String fieldDataType = oracle.getFieldClassName( p.getFactType(),
                                                                     c.getFactField() );
                    if ( fieldDataType.equals( className ) ) {
                        bindings.add( c.getBinding() );
                    }
                }
            }
        }
        return bindings;
    }

    @Override
    public List<String> getLHSBoundFacts() {
        return rm.getLHSBoundFacts();
    }

    @Override
    public boolean canConditionBeDeleted( final ConditionCol52 col ) {
        Pattern52 pattern = model.getPattern( col );
        if ( pattern.getChildColumns().size() > 1 ) {
            return true;
        }
        if ( isBindingUsed( pattern.getBoundName() ) ) {
            return false;
        }
        return true;
    }

    @Override
    public boolean canConditionBeDeleted( final BRLConditionColumn col ) {
        for ( IPattern p : col.getDefinition() ) {
            if ( p instanceof FactPattern ) {
                FactPattern fp = (FactPattern) p;
                if ( fp.isBound() ) {
                    if ( isBindingUsed( fp.getBoundName() ) ) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean isBindingUsed( final String binding ) {
        return rm.isBoundFactUsed( binding );
    }

    @Override
    public Map<String, String> getValueListLookups( final BaseColumn column ) {
        final String[] dropDownItems = columnUtilities.getValueList( column );
        return enumLoaderUtilities.convertDropDownData( dropDownItems );
    }

    @Override
    public void getEnumLookups( final String factType,
                                final String factField,
                                final DependentEnumsUtilities.Context context,
                                final Callback<Map<String, String>> callback ) {
        final DropDownData enumDefinition = oracle.getEnums( factType,
                                                             factField,
                                                             this.dependentEnumsUtilities.getCurrentValueMap( context ) );
        enumLoaderUtilities.getEnums( enumDefinition,
                                      callback,
                                      this,
                                      () -> view.showBusyIndicator( CommonConstants.INSTANCE.RefreshingList() ),
                                      () -> view.hideBusyIndicator() );
    }

    @Override
    public void newAttributeOrMetaDataColumn() {
        if ( isReadOnly() ) {
            return;
        }
        view.newAttributeOrMetaDataColumn();
    }

    @Override
    public Set<String> getExistingAttributeNames() {
        final Set<String> existingAttributeNames = new HashSet<String>();
        for ( AttributeCol52 attributeCol : model.getAttributeCols() ) {
            existingAttributeNames.add( attributeCol.getAttribute() );
        }
        return existingAttributeNames;
    }

    @Override
    public boolean isMetaDataUnique( final String metaDataName ) {
        for ( MetadataCol52 mc : model.getMetadataCols() ) {
            if ( metaDataName.equals( mc.getMetadata() ) ) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void newConditionColumn() {
        if ( isReadOnly() ) {
            return;
        }
        switch ( model.getTableFormat() ) {
            case EXTENDED_ENTRY:
                view.newExtendedEntryConditionColumn();
                break;
            case LIMITED_ENTRY:
                view.newLimitedEntryConditionColumn();
                break;
        }
    }

    @Override
    public void newConditionBRLFragment() {
        if ( isReadOnly() ) {
            return;
        }
        switch ( model.getTableFormat() ) {
            case EXTENDED_ENTRY:
                view.newExtendedEntryConditionBRLFragment();
                break;
            case LIMITED_ENTRY:
                view.newLimitedEntryConditionBRLFragment();
                break;
        }
    }

    @Override
    public void newActionInsertColumn() {
        if ( isReadOnly() ) {
            return;
        }
        switch ( model.getTableFormat() ) {
            case EXTENDED_ENTRY:
                view.newExtendedEntryActionInsertColumn();
                break;
            case LIMITED_ENTRY:
                view.newLimitedEntryActionInsertColumn();
                break;
        }
    }

    @Override
    public void newActionSetColumn() {
        if ( isReadOnly() ) {
            return;
        }
        switch ( model.getTableFormat() ) {
            case EXTENDED_ENTRY:
                view.newExtendedEntryActionSetColumn();
                break;
            case LIMITED_ENTRY:
                view.newLimitedEntryActionSetColumn();
                break;
        }
    }

    @Override
    public void newActionRetractFact() {
        if ( isReadOnly() ) {
            return;
        }
        switch ( model.getTableFormat() ) {
            case EXTENDED_ENTRY:
                view.newExtendedEntryActionRetractFact();
                break;
            case LIMITED_ENTRY:
                view.newLimitedEntryActionRetractFact();
                break;
        }
    }

    @Override
    public void newActionWorkItem() {
        if ( isReadOnly() ) {
            return;
        }
        view.newActionWorkItem();
    }

    @Override
    public void newActionWorkItemSetField() {
        if ( isReadOnly() ) {
            return;
        }
        view.newActionWorkItemSetField();
    }

    @Override
    public void newActionWorkItemInsertFact() {
        if ( isReadOnly() ) {
            return;
        }
        view.newActionWorkItemInsertFact();
    }

    @Override
    public void newActionBRLFragment() {
        if ( isReadOnly() ) {
            return;
        }
        switch ( model.getTableFormat() ) {
            case EXTENDED_ENTRY:
                view.newExtendedEntryActionBRLFragment();
                break;
            case LIMITED_ENTRY:
                view.newLimitedEntryActionBRLFragment();
                break;
        }
    }

    @Override
    public void editCondition( final Pattern52 pattern,
                               final ConditionCol52 column ) {
        if ( isReadOnly() ) {
            return;
        }
        view.editCondition( pattern,
                            column );
    }

    @Override
    public void editCondition( final BRLConditionColumn column ) {
        if ( isReadOnly() ) {
            return;
        }
        if ( column instanceof LimitedEntryBRLConditionColumn ) {
            view.editLimitedEntryConditionBRLFragment( (LimitedEntryBRLConditionColumn) column );
        } else {
            view.editExtendedEntryConditionBRLFragment( column );
        }
    }

    @Override
    public void editAction( final ActionCol52 column ) {
        if ( isReadOnly() ) {
            return;
        }
        if ( column instanceof ActionWorkItemSetFieldCol52 ) {
            view.editActionWorkItemSetField( (ActionWorkItemSetFieldCol52) column );
        } else if ( column instanceof ActionSetFieldCol52 ) {
            view.editActionSetField( (ActionSetFieldCol52) column );
        } else if ( column instanceof ActionWorkItemInsertFactCol52 ) {
            view.editActionWorkItemInsertFact( (ActionWorkItemInsertFactCol52) column );
        } else if ( column instanceof ActionInsertFactCol52 ) {
            view.editActionInsertFact( (ActionInsertFactCol52) column );
        } else if ( column instanceof ActionRetractFactCol52 ) {
            view.editActionRetractFact( (ActionRetractFactCol52) column );
        } else if ( column instanceof ActionWorkItemCol52 ) {
            view.editActionWorkItem( (ActionWorkItemCol52) column );
        } else if ( column instanceof LimitedEntryBRLActionColumn ) {
            view.editLimitedEntryActionBRLFragment( (LimitedEntryBRLActionColumn) column );
        } else if ( column instanceof BRLActionColumn ) {
            view.editExtendedEntryActionBRLFragment( (BRLActionColumn) column );
        }
    }

    @Override
    public void appendColumn( final AttributeCol52 column ) {
        doAppendColumn( column,
                        () -> synchronizer.appendColumn( column ),
                        () -> refreshAttributesPanelEvent.fire( new RefreshAttributesPanelEvent( this,
                                                                                                 model.getAttributeCols() ) ) );
    }

    @Override
    public void appendColumn( final MetadataCol52 column ) {
        doAppendColumn( column,
                        () -> synchronizer.appendColumn( column ),
                        () -> refreshMetaDataPanelEvent.fire( new RefreshMetaDataPanelEvent( this,
                                                                                             model.getMetadataCols() ) ) );
    }

    @Override
    public void appendColumn( final Pattern52 pattern,
                              final ConditionCol52 column ) {
        doAppendColumn( column,
                        () -> synchronizer.appendColumn( pattern,
                                                         column ),
                        () -> refreshConditionsPanelEvent.fire( new RefreshConditionsPanelEvent( this,
                                                                                                 model.getConditions() ) ) );
    }

    @Override
    public void appendColumn( final ConditionCol52 column ) {
        doAppendColumn( column,
                        () -> synchronizer.appendColumn( column ),
                        () -> refreshConditionsPanelEvent.fire( new RefreshConditionsPanelEvent( this,
                                                                                                 model.getConditions() ) ) );
    }

    @Override
    public void appendColumn( final ActionCol52 column ) {
        doAppendColumn( column,
                        () -> synchronizer.appendColumn( column ),
                        () -> refreshActionsPanelEvent.fire( new RefreshActionsPanelEvent( this,
                                                                                           model.getActionCols() ) ) );
    }

    private void doAppendColumn( final BaseColumn column,
                                 final VetoableCommand append,
                                 final Command callback ) {
        if ( isReadOnly() ) {
            return;
        }
        try {
            append.execute();

            parent.updateLinks();

            view.getLayer().draw();

            //Log addition of column
            model.getAuditLog().add( new InsertColumnAuditLogEntry( identity.getIdentifier(),
                                                                    column ) );
            callback.execute();

        } catch ( ModelSynchronizer.MoveColumnVetoException e ) {
            //Swallow. The VetoException signals that the column could not be appended.
        }
    }

    @Override
    public void onAppendRow() {
        if ( isReadOnly() ) {
            return;
        }
        try {
            synchronizer.appendRow();

            parent.updateLinks();

            view.getLayer().draw();

            //Log insertion of row
            model.getAuditLog().add( new InsertRowAuditLogEntry( identity.getIdentifier(),
                                                                 model.getData().size() - 1 ) );

        } catch ( ModelSynchronizer.MoveColumnVetoException e ) {
            //Swallow
        }
    }

    @Override
    public void deleteColumn( final AttributeCol52 column ) {
        doDeleteColumn( column,
                        () -> refreshAttributesPanelEvent.fire( new RefreshAttributesPanelEvent( this,
                                                                                                 model.getAttributeCols() ) ) );
    }

    @Override
    public void deleteColumn( final MetadataCol52 column ) {
        doDeleteColumn( column,
                        () -> refreshMetaDataPanelEvent.fire( new RefreshMetaDataPanelEvent( this,
                                                                                             model.getMetadataCols() ) ) );
    }

    @Override
    public void deleteColumn( final ConditionCol52 column ) {
        doDeleteColumn( column,
                        () -> refreshConditionsPanelEvent.fire( new RefreshConditionsPanelEvent( this,
                                                                                                 model.getConditions() ) ) );
    }

    @Override
    public void deleteColumn( final ActionCol52 column ) {
        doDeleteColumn( column,
                        () -> refreshActionsPanelEvent.fire( new RefreshActionsPanelEvent( this,
                                                                                           model.getActionCols() ) ) );
    }

    private void doDeleteColumn( final BaseColumn column,
                                 final Command callback ) {
        if ( isReadOnly() ) {
            return;
        }
        try {
            synchronizer.deleteColumn( column );

            parent.updateLinks();

            view.getLayer().draw();

            //Log deletion of column
            model.getAuditLog().add( new DeleteColumnAuditLogEntry( identity.getIdentifier(),
                                                                    column ) );
            callback.execute();

        } catch ( ModelSynchronizer.MoveColumnVetoException e ) {
            //Swallow. The VetoException signals that the column could not be deleted.
        }
    }

    @Override
    public void updateColumn( final AttributeCol52 originalColumn,
                              final AttributeCol52 editedColumn ) {
        doUpdateColumn( originalColumn,
                        editedColumn,
                        () -> synchronizer.updateColumn( originalColumn,
                                                         editedColumn ),
                        () -> refreshAttributesPanelEvent.fire( new RefreshAttributesPanelEvent( this,
                                                                                                 model.getAttributeCols() ) ) );
    }

    @Override
    public void updateColumn( final MetadataCol52 originalColumn,
                              final MetadataCol52 editedColumn ) {
        doUpdateColumn( originalColumn,
                        editedColumn,
                        () -> synchronizer.updateColumn( originalColumn,
                                                         editedColumn ),
                        () -> refreshMetaDataPanelEvent.fire( new RefreshMetaDataPanelEvent( this,
                                                                                             model.getMetadataCols() ) ) );
    }

    @Override
    public void updateColumn( final Pattern52 originalPattern,
                              final ConditionCol52 originalColumn,
                              final Pattern52 editedPattern,
                              final ConditionCol52 editedColumn ) {
        doUpdateColumn( originalColumn,
                        editedColumn,
                        () -> synchronizer.updateColumn( originalPattern,
                                                         originalColumn,
                                                         editedPattern,
                                                         editedColumn ),
                        () -> refreshConditionsPanelEvent.fire( new RefreshConditionsPanelEvent( this,
                                                                                                 model.getConditions() ) ) );
    }

    @Override
    public void updateColumn( final ConditionCol52 originalColumn,
                              final ConditionCol52 editedColumn ) {
        doUpdateColumn( originalColumn,
                        editedColumn,
                        () -> synchronizer.updateColumn( originalColumn,
                                                         editedColumn ),
                        () -> refreshConditionsPanelEvent.fire( new RefreshConditionsPanelEvent( this,
                                                                                                 model.getConditions() ) ) );
    }

    @Override
    public void updateColumn( final ActionCol52 originalColumn,
                              final ActionCol52 editedColumn ) {
        doUpdateColumn( originalColumn,
                        editedColumn,
                        () -> synchronizer.updateColumn( originalColumn,
                                                         editedColumn ),
                        () -> refreshActionsPanelEvent.fire( new RefreshActionsPanelEvent( this,
                                                                                           model.getActionCols() ) ) );
    }

    private void doUpdateColumn( final BaseColumn originalColumn,
                                 final BaseColumn editedColumn,
                                 final VetoableUpdateCommand update,
                                 final Command callback ) {
        if ( isReadOnly() ) {
            return;
        }
        try {
            final List<BaseColumnFieldDiff> diffs = update.execute();

            parent.updateLinks();

            //Log change to column definition
            if ( !( diffs == null || diffs.isEmpty() ) ) {
                view.getLayer().draw();
                model.getAuditLog().add( new UpdateColumnAuditLogEntry( identity.getIdentifier(),
                                                                        originalColumn,
                                                                        editedColumn,
                                                                        diffs ) );
                callback.execute();

            }
        } catch ( ModelSynchronizer.MoveColumnVetoException e ) {
            //Swallow. The VetoException signals that the column could not be updated.
        }
    }

    private void initialiseColumn( final BaseColumn column ) {
        final GridColumn<?> gridColumn = gridWidgetColumnFactory.convertColumn( column,
                                                                                access,
                                                                                getView() );
        uiModel.appendColumn( gridColumn );
    }

    private void initialiseRow( final List<BaseColumn> columns,
                                final List<DTCellValue52> row ) {
        final GridRow uiModelRow = new BaseGridRow( 24 );
        final int rowIndex = uiModel.getRowCount();
        uiModel.appendRow( uiModelRow );

        for ( int iModelColumn = 0; iModelColumn < row.size(); iModelColumn++ ) {
            final DTCellValue52 modelCell = row.get( iModelColumn );
            final BaseColumn modelColumn = columns.get( iModelColumn );

            // We cannot rely upon the values in the existing data as legacy tables aren't guaranteed to be sorted
            if ( modelColumn instanceof RowNumberCol52 ) {
                modelCell.setNumericValue( uiModel.getRowCount() );
            }

            //BaseGridData is sparsely populated; only add values if needed.
            if ( modelCell.hasValue() ) {
                uiModel.setCellInternal( rowIndex,
                                         iModelColumn,
                                         gridWidgetCellFactory.convertCell( modelCell,
                                                                            modelColumn,
                                                                            cellUtilities,
                                                                            columnUtilities ) );

                //Set-up SelectionManager for Row Number column, to select entire row.
                if ( modelColumn instanceof RowNumberCol52 ) {
                    uiModel.getCell( rowIndex,
                                     iModelColumn ).setSelectionManager( RowSelectionStrategy.INSTANCE );
                }
            }
        }
    }

    @Override
    public void onCut() {
        if ( isSelectionEmpty() ) {
            return;
        }
        if ( isReadOnly() ) {
            return;
        }
        copyCellsToClipboard();
        onDeleteSelectedCells();
        view.showDataCutNotificationEvent();
    }

    @Override
    public void onCopy() {
        if ( isSelectionEmpty() ) {
            return;
        }
        if ( isReadOnly() ) {
            return;
        }
        copyCellsToClipboard();
        view.showDataCopiedNotificationEvent();
    }

    private void copyCellsToClipboard() {
        final List<GridData.SelectedCell> selections = uiModel.getSelectedCells();
        if ( selections == null || selections.isEmpty() ) {
            return;
        }
        int originRowIndex = Integer.MAX_VALUE;
        int originColumnIndex = Integer.MAX_VALUE;
        final Set<Clipboard.ClipboardData> data = new HashSet<>();

        for ( GridData.SelectedCell sc : selections ) {
            final int rowIndex = sc.getRowIndex();
            final int columnIndex = findUiColumnIndex( sc.getColumnIndex() );
            originRowIndex = Math.min( rowIndex,
                                       originRowIndex );
            originColumnIndex = Math.min( columnIndex,
                                          originColumnIndex );
        }
        for ( GridData.SelectedCell sc : selections ) {
            final int rowIndex = sc.getRowIndex();
            final int columnIndex = findUiColumnIndex( sc.getColumnIndex() );
            final DTCellValue52 value = model.getData().get( rowIndex ).get( columnIndex );
            data.add( new DefaultClipboard.ClipboardDataImpl( rowIndex - originRowIndex,
                                                              columnIndex - originColumnIndex,
                                                              new DTCellValue52( value ) ) );
        }
        clipboard.setData( data );
    }

    @Override
    public void onPaste() {
        if ( !clipboard.hasData() ) {
            return;
        }
        if ( isSelectionEmpty() ) {
            return;
        }
        if ( isReadOnly() ) {
            return;
        }
        final Set<Clipboard.ClipboardData> data = clipboard.getData();
        final int currentOriginRowIndex = uiModel.getSelectedCellsOrigin().getRowIndex();
        final int currentOriginColumnIndex = findUiColumnIndex( uiModel.getSelectedCellsOrigin().getColumnIndex() );

        boolean updateSystemControlledValues = false;
        for ( Clipboard.ClipboardData cd : data ) {
            final int targetRowIndex = currentOriginRowIndex + cd.getRowIndex();
            final int targetColumnIndex = currentOriginColumnIndex + cd.getColumnIndex();
            if ( targetRowIndex < 0 || targetRowIndex > uiModel.getRowCount() - 1 ) {
                continue;
            }
            if ( targetColumnIndex < 0 || targetColumnIndex > uiModel.getColumns().size() - 1 ) {
                continue;
            }

            final DTCellValue52 modelCell = cd.getValue();
            final BaseColumn modelColumn = model.getExpandedColumns().get( targetColumnIndex );
            if ( modelCell.hasValue() ) {
                uiModel.setCell( targetRowIndex,
                                 targetColumnIndex,
                                 gridWidgetCellFactory.convertCell( modelCell,
                                                                    modelColumn,
                                                                    cellUtilities,
                                                                    columnUtilities ) );
            } else {
                uiModel.deleteCell( targetRowIndex,
                                    targetColumnIndex );
            }

            if ( modelColumn instanceof RowNumberCol52 ) {
                updateSystemControlledValues = true;
            }
        }
        if ( updateSystemControlledValues ) {
            synchronizer.updateSystemControlledColumnValues();
        }
        view.batch();
    }

    boolean isSelectionEmpty() {
        return uiModel.getSelectedCells().isEmpty();
    }

    @Override
    public void onDeleteSelectedCells() {
        if ( isReadOnly() ) {
            return;
        }
        final List<GridData.SelectedCell> selections = uiModel.getSelectedCells();
        if ( selections == null || selections.isEmpty() ) {
            return;
        }
        for ( GridData.SelectedCell sc : selections ) {
            final int rowIndex = sc.getRowIndex();
            final int columnIndex = findUiColumnIndex( sc.getColumnIndex() );
            final BaseColumn column = model.getExpandedColumns().get( columnIndex );
            final GridColumn<?> uiColumn = uiModel.getColumns().get( columnIndex );
            if ( column instanceof RowNumberCol52 ) {
                continue;
            }
            if ( uiColumn instanceof BooleanUiColumn ) {
                uiModel.setCell( rowIndex,
                                 columnIndex,
                                 new GuidedDecisionTableUiCell<>( false ) );
            } else {
                uiModel.deleteCell( rowIndex,
                                    columnIndex );
            }
        }
        view.getLayer().draw();
    }

    @Override
    public void onDeleteSelectedColumns() {
        if ( isReadOnly() ) {
            return;
        }
        final Set<Integer> selectedColumnIndexes = getSelectedColumnIndexes();
        final Set<BaseColumn> columnsToDelete = new HashSet<>();
        for ( int selectedColumnIndex : selectedColumnIndexes ) {
            final int columnIndex = findUiColumnIndex( selectedColumnIndex );
            final BaseColumn column = model.getExpandedColumns().get( columnIndex );
            if ( !( column instanceof RowNumberCol52 || column instanceof DescriptionCol52 ) ) {
                columnsToDelete.add( column );
            }
        }
        for ( BaseColumn columnToDelete : columnsToDelete ) {
            if ( columnToDelete instanceof AttributeCol52 ) {
                deleteColumn( (AttributeCol52) columnToDelete );
            } else if ( columnToDelete instanceof MetadataCol52 ) {
                deleteColumn( (MetadataCol52) columnToDelete );
            } else if ( columnToDelete instanceof ConditionCol52 ) {
                deleteColumn( (ConditionCol52) columnToDelete );
            } else if ( columnToDelete instanceof ActionCol52 ) {
                deleteColumn( (ActionCol52) columnToDelete );
            }
        }
    }

    private Set<Integer> getSelectedColumnIndexes() {
        final Set<Integer> columnUsage = new HashSet<>();
        for ( GridData.SelectedCell sc : uiModel.getSelectedCells() ) {
            columnUsage.add( sc.getColumnIndex() );
        }
        return columnUsage;
    }

    private int findUiColumnIndex( final int modelColumnIndex ) {
        final List<GridColumn<?>> columns = uiModel.getColumns();
        for ( int uiColumnIndex = 0; uiColumnIndex < columns.size(); uiColumnIndex++ ) {
            final GridColumn<?> c = columns.get( uiColumnIndex );
            if ( c.getIndex() == modelColumnIndex ) {
                return uiColumnIndex;
            }
        }
        throw new IllegalStateException( "Column was not found!" );
    }

    @Override
    public void onDeleteSelectedRows() {
        if ( isReadOnly() ) {
            return;
        }
        Set<Integer> selectedRowIndexes;
        while ( !( selectedRowIndexes = getSelectedRowIndexes() ).isEmpty() ) {
            final int rowIndex = selectedRowIndexes.iterator().next();
            deleteRow( rowIndex );
        }
    }

    private void deleteRow( final int rowIndex ) {
        try {
            synchronizer.deleteRow( rowIndex );

            parent.updateLinks();

            view.getLayer().draw();

            //Log deletion of column
            model.getAuditLog().add( new DeleteRowAuditLogEntry( identity.getIdentifier(),
                                                                 rowIndex ) );
        } catch ( ModelSynchronizer.MoveColumnVetoException e ) {
            //Swallow
        }
    }

    @Override
    public void setMerged( final boolean merged ) {
        uiModel.setMerged( merged );
        view.getLayer().draw();
    }

    @Override
    public boolean isMerged() {
        return uiModel.isMerged();
    }

    @Override
    public void showAuditLog() {
        auditLog.show();
    }

    @Override
    public void onInsertRowAbove() {
        doInsertRow( this::insertRow );
    }

    @Override
    public void onInsertRowBelow() {
        doInsertRow( ( index ) -> insertRow( index + 1 ) );
    }

    private void doInsertRow( final ParameterizedCommand<Integer> callback ) {
        if ( isReadOnly() ) {
            return;
        }
        final Set<Integer> selectedRowIndexes = getSelectedRowIndexes();
        if ( selectedRowIndexes.size() != 1 ) {
            return;
        }
        callback.execute( selectedRowIndexes.iterator().next() );
    }

    private Set<Integer> getSelectedRowIndexes() {
        final Set<Integer> rowUsage = new HashSet<>();
        for ( GridData.SelectedCell sc : uiModel.getSelectedCells() ) {
            rowUsage.add( sc.getRowIndex() );
        }
        return rowUsage;
    }

    private void insertRow( final int rowIndex ) {
        try {
            synchronizer.insertRow( rowIndex );

            parent.updateLinks();

            view.getLayer().draw();

            //Log insertion of row
            model.getAuditLog().add( new InsertRowAuditLogEntry( identity.getIdentifier(),
                                                                 rowIndex ) );
        } catch ( ModelSynchronizer.MoveColumnVetoException e ) {
            //Swallow
        }
    }

    @Override
    public void onOtherwiseCell() {
        if ( isReadOnly() ) {
            return;
        }
        final List<GridData.SelectedCell> selections = uiModel.getSelectedCells();
        if ( selections.size() != 1 ) {
            return;
        }
        final GridData.SelectedCell selection = selections.get( 0 );
        final int columnIndex = findUiColumnIndex( selection.getColumnIndex() );
        synchronizer.setCellOtherwiseState( selection.getRowIndex(),
                                            columnIndex );
        view.getLayer().draw();
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public void setVersion( final String version ) {
        this.version = version;
    }

    @Override
    public ObservablePath getLatestPath() {
        return latestPath;
    }

    @Override
    public void setLatestPath( final ObservablePath latestPath ) {
        this.latestPath = latestPath;
    }

    @Override
    public ObservablePath getCurrentPath() {
        return currentPath;
    }

    @Override
    public void setCurrentPath( final ObservablePath currentPath ) {
        this.currentPath = currentPath;
    }

    @Override
    public PlaceRequest getPlaceRequest() {
        return placeRequest;
    }

    @Override
    public boolean isReadOnly() {
        return !this.access.isEditable();
    }

    @Override
    public void setReadOnly( final boolean isReadOnly ) {
        this.access.setReadOnly( isReadOnly );
    }

    @Override
    public Integer getOriginalHashCode() {
        return originalHashCode;
    }

    @Override
    public void setOriginalHashCode( final Integer originalHashCode ) {
        this.originalHashCode = originalHashCode;
    }

    @Override
    public ObservablePath.OnConcurrentUpdateEvent getConcurrentUpdateSessionInfo() {
        return concurrentUpdateSessionInfo;
    }

    @Override
    public void setConcurrentUpdateSessionInfo( final ObservablePath.OnConcurrentUpdateEvent concurrentUpdateSessionInfo ) {
        this.concurrentUpdateSessionInfo = concurrentUpdateSessionInfo;
    }

}
