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
package org.drools.workbench.screens.guided.dtable.client.widget.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Command;
import org.drools.workbench.models.datamodel.oracle.DataType;
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
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.models.guided.dtable.shared.model.LimitedEntryBRLActionColumn;
import org.drools.workbench.models.guided.dtable.shared.model.LimitedEntryBRLConditionColumn;
import org.drools.workbench.models.guided.dtable.shared.model.MetadataCol52;
import org.drools.workbench.models.guided.dtable.shared.model.Pattern52;
import org.drools.workbench.models.guided.dtable.shared.model.RowNumberCol52;
import org.drools.workbench.screens.guided.dtable.client.editor.clipboard.Clipboard;
import org.drools.workbench.screens.guided.dtable.client.editor.clipboard.impl.DefaultClipboard;
import org.drools.workbench.screens.guided.dtable.client.type.GuidedDTableResourceType;
import org.drools.workbench.screens.guided.dtable.client.widget.analysis.DecisionTableAnalyzer;
import org.drools.workbench.screens.guided.dtable.client.widget.auditlog.AuditLog;
import org.drools.workbench.screens.guided.dtable.client.widget.table.events.cdi.DecisionTableColumnSelectedEvent;
import org.drools.workbench.screens.guided.dtable.client.widget.table.events.cdi.DecisionTableSelectedEvent;
import org.drools.workbench.screens.guided.dtable.client.widget.table.events.cdi.DecisionTableSelectionsChangedEvent;
import org.drools.workbench.screens.guided.dtable.client.widget.table.events.cdi.RefreshActionsPanelEvent;
import org.drools.workbench.screens.guided.dtable.client.widget.table.events.cdi.RefreshAttributesPanelEvent;
import org.drools.workbench.screens.guided.dtable.client.widget.table.events.cdi.RefreshConditionsPanelEvent;
import org.drools.workbench.screens.guided.dtable.client.widget.table.events.cdi.RefreshMetaDataPanelEvent;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.GuidedDecisionTableUiModel;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.converters.cell.GridWidgetCellFactory;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.converters.column.BaseColumnConverter;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.converters.column.GridWidgetColumnFactory;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.synchronizers.ModelSynchronizer;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.synchronizers.Synchronizer;
import org.drools.workbench.screens.guided.dtable.client.widget.table.themes.GuidedDecisionTableTheme;
import org.drools.workbench.screens.guided.dtable.client.widget.table.utilities.CellUtilities;
import org.drools.workbench.screens.guided.dtable.client.widget.table.utilities.ColumnUtilities;
import org.drools.workbench.screens.guided.dtable.client.widget.table.utilities.DependentEnumsUtilities;
import org.drools.workbench.screens.guided.dtable.model.GuidedDecisionTableEditorContent;
import org.drools.workbench.screens.guided.rule.client.util.GWTDateConverter;
import org.guvnor.common.services.shared.metadata.model.Overview;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.ioc.client.container.SyncBeanDef;
import org.jboss.errai.ioc.client.container.SyncBeanManager;
import org.jboss.errai.security.shared.api.identity.User;
import org.kie.workbench.common.services.datamodel.model.PackageDataModelOracleBaselinePayload;
import org.kie.workbench.common.services.shared.enums.EnumDropdownService;
import org.kie.workbench.common.services.shared.rulename.RuleNamesService;
import org.kie.workbench.common.widgets.client.datamodel.AsyncPackageDataModelOracle;
import org.kie.workbench.common.widgets.client.datamodel.AsyncPackageDataModelOracleFactory;
import org.kie.workbench.common.widgets.client.resources.i18n.CommonConstants;
import org.kie.workbench.common.widgets.client.util.ConstraintValueHelper;
import org.uberfire.backend.vfs.ObservablePath;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.client.mvp.LockManager;
import org.uberfire.client.mvp.LockRequiredEvent;
import org.uberfire.client.mvp.LockTarget;
import org.uberfire.ext.widgets.common.client.callbacks.HasBusyIndicatorDefaultErrorCallback;
import org.uberfire.ext.wires.core.grids.client.model.BaseGridRow;
import org.uberfire.ext.wires.core.grids.client.model.IGridColumn;
import org.uberfire.ext.wires.core.grids.client.model.IGridData;
import org.uberfire.ext.wires.core.grids.client.model.IGridRow;
import org.uberfire.ext.wires.core.grids.client.widget.IBaseGridWidget;
import org.uberfire.ext.wires.core.grids.client.widget.dom.IHasDOMElementResources;
import org.uberfire.ext.wires.core.grids.client.widget.pinning.IRestriction;
import org.uberfire.ext.wires.core.grids.client.widget.renderers.grids.BaseGridRenderer;
import org.uberfire.ext.wires.core.grids.client.widget.selections.RowSelectionManager;
import org.uberfire.mvp.ParameterizedCommand;
import org.uberfire.mvp.PlaceRequest;
import org.uberfire.workbench.events.NotificationEvent;

import static org.drools.workbench.screens.guided.dtable.client.widget.table.model.synchronizers.Synchronizer.*;

@Dependent
public class GuidedDecisionTablePresenter implements GuidedDecisionTableView.Presenter {

    private final User identity;
    private final GuidedDTableResourceType resourceType;
    private final Caller<RuleNamesService> ruleNameService;
    private final Caller<EnumDropdownService> enumDropdownService;
    private final Event<DecisionTableSelectedEvent> decisionTableSelectedEvent;
    private final Event<DecisionTableColumnSelectedEvent> decisionTableColumnSelectedEvent;
    private final Event<DecisionTableSelectionsChangedEvent> decisionTableSelectionsChangedEvent;
    private final Event<RefreshAttributesPanelEvent> refreshAttributesPanelEvent;
    private final Event<RefreshMetaDataPanelEvent> refreshMetaDataPanelEvent;
    private final Event<RefreshConditionsPanelEvent> refreshConditionsPanelEvent;
    private final Event<RefreshActionsPanelEvent> refreshActionsPanelEvent;
    private final Event<NotificationEvent> notificationEvent;
    private final Event<LockRequiredEvent> lockRequiredEvent;
    private final GridWidgetCellFactory gridWidgetCellFactory;
    private final GridWidgetColumnFactory gridWidgetColumnFactory;
    private final AsyncPackageDataModelOracleFactory oracleFactory;
    private final ModelSynchronizer synchronizer;
    private final SyncBeanManager beanManager;
    private final Clipboard clipboard;

    private final Access access = new Access();

    private GuidedDecisionTable52 model;
    private Overview overview;
    private AsyncPackageDataModelOracle oracle;
    private GuidedDecisionTableModellerView.Presenter parent;
    private LockManager lockManager;
    private BRLRuleModel rm;

    private GuidedDecisionTableUiModel uiModel;
    private GuidedDecisionTableView view;

    private AuditLog auditLog;

    protected CellUtilities cellUtilities;
    protected ColumnUtilities columnUtilities;
    protected DependentEnumsUtilities dependentEnumsUtilities;

    protected DecisionTableAnalyzer decisionTableAnalyzer;

    private String version = null;
    private ObservablePath latestPath = null;
    private ObservablePath currentPath = null;
    private PlaceRequest placeRequest = null;

    private boolean isReadOnly = false;
    private Integer originalHashCode = null;
    private ObservablePath.OnConcurrentUpdateEvent concurrentUpdateSessionInfo = null;

    //This EventBus is local to the screen and should be used for local operations, set data, add rows etc
    private EventBus eventBus = new SimpleEventBus();

    public static class Access {

        private boolean isLocked = false;
        private boolean isReadOnly = false;

        public boolean isLocked() {
            return isLocked;
        }

        public void setLocked( final boolean isLocked ) {
            this.isLocked = isLocked;
        }

        public boolean isReadOnly() {
            return isReadOnly;
        }

        public void setReadOnly( final boolean isReadOnly ) {
            this.isReadOnly = isReadOnly;
        }

        public boolean isEditable() {
            return !( isLocked || isReadOnly );
        }

    }

    @Inject
    public GuidedDecisionTablePresenter( final User identity,
                                         final GuidedDTableResourceType resourceType,
                                         final Caller<RuleNamesService> ruleNameService,
                                         final Caller<EnumDropdownService> enumDropdownService,
                                         final Event<DecisionTableSelectedEvent> decisionTableSelectedEvent,
                                         final Event<DecisionTableColumnSelectedEvent> decisionTableColumnSelectedEvent,
                                         final Event<DecisionTableSelectionsChangedEvent> decisionTableSelectionsChangedEvent,
                                         final Event<RefreshAttributesPanelEvent> refreshAttributesPanelEvent,
                                         final Event<RefreshMetaDataPanelEvent> refreshMetaDataPanelEvent,
                                         final Event<RefreshConditionsPanelEvent> refreshConditionsPanelEvent,
                                         final Event<RefreshActionsPanelEvent> refreshActionsPanelEvent,
                                         final Event<NotificationEvent> notificationEvent,
                                         final Event<LockRequiredEvent> lockRequiredEvent,
                                         final GridWidgetCellFactory gridWidgetCellFactory,
                                         final GridWidgetColumnFactory gridWidgetColumnFactory,
                                         final AsyncPackageDataModelOracleFactory oracleFactory,
                                         final ModelSynchronizer synchronizer,
                                         final SyncBeanManager beanManager,
                                         final Clipboard clipboard ) {
        this.identity = identity;
        this.resourceType = resourceType;
        this.ruleNameService = ruleNameService;
        this.enumDropdownService = enumDropdownService;
        this.decisionTableSelectedEvent = decisionTableSelectedEvent;
        this.decisionTableColumnSelectedEvent = decisionTableColumnSelectedEvent;
        this.decisionTableSelectionsChangedEvent = decisionTableSelectionsChangedEvent;
        this.refreshAttributesPanelEvent = refreshAttributesPanelEvent;
        this.refreshMetaDataPanelEvent = refreshMetaDataPanelEvent;
        this.refreshConditionsPanelEvent = refreshConditionsPanelEvent;
        this.refreshActionsPanelEvent = refreshActionsPanelEvent;
        this.notificationEvent = notificationEvent;
        this.lockRequiredEvent = lockRequiredEvent;
        this.gridWidgetCellFactory = gridWidgetCellFactory;
        this.gridWidgetColumnFactory = gridWidgetColumnFactory;
        this.oracleFactory = oracleFactory;
        this.synchronizer = synchronizer;
        this.beanManager = beanManager;
        this.clipboard = clipboard;

        //Date converter is injected so a GWT compatible one can be used here and another in testing
        CellUtilities.injectDateConvertor( GWTDateConverter.getInstance() );
    }

    @Override
    public GuidedDecisionTable52 getModel() {
        return this.model;
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
        refreshContent( path,
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
        this.lockManager = getLockManager( path,
                                           placeRequest );
        this.access.setReadOnly( isReadOnly );
        this.rm = new BRLRuleModel( model );

        //Ensure field data-type is set (field did not exist before 5.2)
        for ( CompositeColumn<?> column : model.getConditions() ) {
            if ( column instanceof Pattern52 ) {
                final Pattern52 pattern = (Pattern52) column;
                for ( ConditionCol52 condition : pattern.getChildColumns() ) {
                    condition.setFieldType( oracle.getFieldType( pattern.getFactType(),
                                                                 condition.getFactField() ) );
                }
            }
        }

        //Setup UiModel overriding cell selection to inform MenuItems about changes to selected cells.
        this.uiModel = new GuidedDecisionTableUiModel( synchronizer ) {
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
        };
        uiModel.setRowDraggingEnabled( access.isEditable() );
        uiModel.setColumnDraggingEnabled( access.isEditable() );

        //Setup View
        this.view = new GuidedDecisionTableViewImpl( uiModel,
                                                     new BaseGridRenderer( new GuidedDecisionTableTheme() ),
                                                     this,
                                                     model,
                                                     oracle,
                                                     workItemDefinitions,
                                                     notificationEvent,
                                                     eventBus,
                                                     access );

        //Setup Utilities
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

        //Copy Model data to UiModel.
        final List<BaseColumn> modelColumns = model.getExpandedColumns();
        for ( BaseColumn column : modelColumns ) {
            initialiseColumn( column );
        }
        for ( List<DTCellValue52> row : model.getData() ) {
            initialiseRow( modelColumns,
                           row );
        }

        //Setup the Validation & Verification analyzer
        this.decisionTableAnalyzer = new DecisionTableAnalyzer( placeRequest,
                                                                oracle,
                                                                model,
                                                                eventBus );

        //Setup synchronizer to update the Model when the UiModel changes.
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

        //Setup Audit Log
        auditLog = new AuditLog( model,
                                 identity );

    }

    private LockManager getLockManager( final ObservablePath path,
                                        final PlaceRequest placeRequest ) {
        final LockManager lockManager = beanManager.lookupBean( LockManager.class ).getInstance();
        lockManager.init( new LockTarget( path,
                                          parent.getView().asWidget(),
                                          placeRequest,
                                          new LockTarget.TitleProvider() {
                                              @Override
                                              public String getTitle() {
                                                  return path.getFileName() + " - " + resourceType.getDescription();
                                              }
                                          },
                                          new Runnable() {
                                              @Override
                                              public void run() {
                                                  //Nothing to do
                                              }
                                          } ) );
        return lockManager;
    }

    private List<BaseColumnConverter> getConverters() {
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
    private List<Synchronizer<? extends MetaData, ? extends MetaData, ? extends MetaData, ? extends MetaData, ? extends MetaData>> getSynchronizers() {
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
            for ( IGridColumn<?> column : uiModel.getColumns() ) {
                if ( column.getColumnRenderer() instanceof IHasDOMElementResources ) {
                    ( (IHasDOMElementResources) column.getColumnRenderer() ).destroyResources();
                }
            }
        }

        if ( lockManager != null ) {
            lockManager.releaseLock();
            beanManager.destroyBean( lockManager );
        }
        if ( oracle != null ) {
            oracleFactory.destroy( oracle );
        }
    }

    @Override
    public void initialiseAnalysis() {
        if ( decisionTableAnalyzer != null ) {
            decisionTableAnalyzer.onFocus();
        }
    }

    @Override
    public void terminateAnalysis() {
        if ( decisionTableAnalyzer != null ) {
            decisionTableAnalyzer.onClose();
        }
    }

    @Override
    @SuppressWarnings("unused")
    public void select( final IBaseGridWidget selectedGridWidget ) {
        decisionTableSelectedEvent.fire( new DecisionTableSelectedEvent( this ) );
        lockRequiredEvent.fire( new LockRequiredEvent() );
        lockManager.onFocus();
    }

    @Override
    public void selectLinkedColumn( final IGridColumn<?> column ) {
        decisionTableColumnSelectedEvent.fire( new DecisionTableColumnSelectedEvent( column ) );
    }

    @Override
    public Set<IBaseGridWidget> getGridWidgets() {
        return Collections.emptySet();
    }

    @Override
    public void enterPinnedMode( final IBaseGridWidget gridWidget,
                                 final Command onStartCommand ) {
        parent.getView().getGridLayerView().enterPinnedMode( gridWidget,
                                                             onStartCommand );
    }

    @Override
    public void exitPinnedMode( final Command onCompleteCommand ) {
        parent.getView().getGridLayerView().exitPinnedMode( onCompleteCommand );
    }

    @Override
    public void updatePinnedContext( final IBaseGridWidget gridWidget ) throws IllegalStateException {
        parent.getView().getGridLayerView().updatePinnedContext( gridWidget );
    }

    @Override
    public PinnedContext getPinnedContext() {
        return parent.getView().getGridLayerView().getPinnedContext();
    }

    @Override
    public boolean isGridPinned() {
        return parent.getView().getGridLayerView().isGridPinned();
    }

    @Override
    public IRestriction getDefaultRestriction() {
        return parent.getView().getGridLayerView().getDefaultRestriction();
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
        return convertDropDownData( dropDownItems );
    }

    private Map<String, String> convertDropDownData( final String[] dropDownItems ) {
        final Map<String, String> convertedDropDownData = new TreeMap<String, String>();
        for ( int i = 0; i < dropDownItems.length; i++ ) {
            final String dropDownItem = dropDownItems[ i ];
            String display = dropDownItem;
            if ( dropDownItem.indexOf( '=' ) > 0 ) {
                final String[] split = ConstraintValueHelper.splitValue( dropDownItem );
                display = split[ 1 ];
            }
            convertedDropDownData.put( dropDownItem.trim(),
                                       display.trim() );
        }
        return convertedDropDownData;
    }

    @Override
    public void getEnumLookups( final String factType,
                                final String factField,
                                final DependentEnumsUtilities.Context context,
                                final Callback<Map<String, String>> callback ) {
        final DropDownData dropDownData = oracle.getEnums( factType,
                                                           factField,
                                                           this.dependentEnumsUtilities.getCurrentValueMap( context ) );
        if ( dropDownData == null ) {
            callback.callback( Collections.<String, String>emptyMap() );
            return;
        }

        if ( dropDownData.getFixedList() != null ) {
            final Map<String, String> convertedDropDownData = convertDropDownData( dropDownData.getFixedList() );
            callback.callback( convertedDropDownData );
            return;
        }

        //Lookup data from server if the list of enumerations comes from an external query
        if ( dropDownData.getQueryExpression() == null ) {
            callback.callback( Collections.<String, String>emptyMap() );
        }
        view.showBusyIndicator( CommonConstants.INSTANCE.RefreshingList() );
        enumDropdownService.call( new RemoteCallback<String[]>() {
                                      @Override
                                      public void callback( final String[] items ) {
                                          view.hideBusyIndicator();
                                          if ( items.length == 0 ) {
                                              callback.callback( Collections.<String, String>emptyMap() );
                                          } else {
                                              final Map<String, String> convertedDropDownData = convertDropDownData( items );
                                              callback.callback( convertedDropDownData );
                                          }
                                      }
                                  },
                                  new HasBusyIndicatorDefaultErrorCallback( view ) ).loadDropDownExpression( getCurrentPath(),
                                                                                                             dropDownData.getValuePairs(),
                                                                                                             dropDownData.getQueryExpression() );
    }

    @Override
    public void newAttributeOrMetaDataColumn() {
        assertNotReadOnly();
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
        assertNotReadOnly();
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
        assertNotReadOnly();
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
        assertNotReadOnly();
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
        assertNotReadOnly();
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
        assertNotReadOnly();
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
        assertNotReadOnly();
        view.newActionWorkItem();
    }

    @Override
    public void newActionWorkItemSetField() {
        assertNotReadOnly();
        view.newActionWorkItemSetField();
    }

    @Override
    public void newActionWorkItemInsertFact() {
        assertNotReadOnly();
        view.newActionWorkItemInsertFact();
    }

    @Override
    public void newActionBRLFragment() {
        assertNotReadOnly();
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
        view.editCondition( pattern,
                            column );
    }

    @Override
    public void editCondition( final BRLConditionColumn column ) {
        if ( column instanceof LimitedEntryBRLConditionColumn ) {
            view.editLimitedEntryConditionBRLFragment( (LimitedEntryBRLConditionColumn) column );
        } else {
            view.editExtendedEntryConditionBRLFragment( column );
        }
    }

    @Override
    public void editAction( final ActionCol52 column ) {
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
        assertNotReadOnly();
        try {
            synchronizer.appendColumn( column );
            view.getLayer().draw();

            //Log addition of column
            model.getAuditLog().add( new InsertColumnAuditLogEntry( identity.getIdentifier(),
                                                                    column ) );

            //Refresh Attributes panel
            refreshAttributesPanelEvent.fire( new RefreshAttributesPanelEvent( this,
                                                                               model.getAttributeCols() ) );
        } catch ( ModelSynchronizer.MoveColumnVetoException e ) {
            //Swallow
        }
    }

    @Override
    public void appendColumn( final MetadataCol52 column ) {
        assertNotReadOnly();
        try {
            synchronizer.appendColumn( column );
            view.getLayer().draw();

            //Log addition of column
            model.getAuditLog().add( new InsertColumnAuditLogEntry( identity.getIdentifier(),
                                                                    column ) );

            //Refresh MetaData panel
            refreshMetaDataPanelEvent.fire( new RefreshMetaDataPanelEvent( this,
                                                                           model.getMetadataCols() ) );
        } catch ( ModelSynchronizer.MoveColumnVetoException e ) {
            //Swallow
        }
    }

    @Override
    public void appendColumn( final Pattern52 pattern,
                              final ConditionCol52 column ) {
        assertNotReadOnly();
        try {
            synchronizer.appendColumn( pattern,
                                       column );
            view.getLayer().draw();

            //Log addition of column
            model.getAuditLog().add( new InsertColumnAuditLogEntry( identity.getIdentifier(),
                                                                    column ) );

            //Refresh Conditions panel
            refreshConditionsPanelEvent.fire( new RefreshConditionsPanelEvent( this,
                                                                               model.getConditions() ) );
        } catch ( ModelSynchronizer.MoveColumnVetoException e ) {
            //Swallow
        }
    }

    @Override
    public void appendColumn( final ConditionCol52 column ) {
        assertNotReadOnly();
        try {
            synchronizer.appendColumn( column );
            view.getLayer().draw();

            //Log addition of column
            model.getAuditLog().add( new InsertColumnAuditLogEntry( identity.getIdentifier(),
                                                                    column ) );

            //Refresh Conditions panel
            refreshConditionsPanelEvent.fire( new RefreshConditionsPanelEvent( this,
                                                                               model.getConditions() ) );
        } catch ( ModelSynchronizer.MoveColumnVetoException e ) {
            //Swallow
        }
    }

    @Override
    public void appendColumn( final ActionCol52 column ) {
        assertNotReadOnly();
        try {
            synchronizer.appendColumn( column );
            view.getLayer().draw();

            //Log addition of column
            model.getAuditLog().add( new InsertColumnAuditLogEntry( identity.getIdentifier(),
                                                                    column ) );

            //Refresh Actions panel
            refreshActionsPanelEvent.fire( new RefreshActionsPanelEvent( this,
                                                                         model.getActionCols() ) );
        } catch ( ModelSynchronizer.MoveColumnVetoException e ) {
            //Swallow
        }
    }

    @Override
    public void onAppendRow() {
        assertNotReadOnly();
        try {
            synchronizer.appendRow();
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
        assertNotReadOnly();
        try {
            synchronizer.deleteColumn( column );
            view.getLayer().draw();

            //Log deletion of column
            model.getAuditLog().add( new DeleteColumnAuditLogEntry( identity.getIdentifier(),
                                                                    column ) );

            //Refresh Attributes panel
            refreshAttributesPanelEvent.fire( new RefreshAttributesPanelEvent( this,
                                                                               model.getAttributeCols() ) );
        } catch ( ModelSynchronizer.MoveColumnVetoException e ) {
            //Swallow
        }
    }

    @Override
    public void deleteColumn( final MetadataCol52 column ) {
        assertNotReadOnly();
        try {
            synchronizer.deleteColumn( column );
            view.getLayer().draw();

            //Log deletion of column
            model.getAuditLog().add( new DeleteColumnAuditLogEntry( identity.getIdentifier(),
                                                                    column ) );

            //Refresh MetaData panel
            refreshMetaDataPanelEvent.fire( new RefreshMetaDataPanelEvent( this,
                                                                           model.getMetadataCols() ) );
        } catch ( ModelSynchronizer.MoveColumnVetoException e ) {
            //Swallow
        }
    }

    @Override
    public void deleteColumn( final ConditionCol52 column ) {
        assertNotReadOnly();
        try {
            synchronizer.deleteColumn( column );
            view.getLayer().draw();

            //Log deletion of column
            model.getAuditLog().add( new DeleteColumnAuditLogEntry( identity.getIdentifier(),
                                                                    column ) );

            //Refresh Conditions panel
            refreshConditionsPanelEvent.fire( new RefreshConditionsPanelEvent( this,
                                                                               model.getConditions() ) );
        } catch ( ModelSynchronizer.MoveColumnVetoException e ) {
            //Swallow
        }
    }

    @Override
    public void deleteColumn( final ActionCol52 column ) {
        assertNotReadOnly();
        try {
            synchronizer.deleteColumn( column );
            view.getLayer().draw();

            //Log deletion of column
            model.getAuditLog().add( new DeleteColumnAuditLogEntry( identity.getIdentifier(),
                                                                    column ) );

            //Refresh Actions panel
            refreshActionsPanelEvent.fire( new RefreshActionsPanelEvent( this,
                                                                         model.getActionCols() ) );
        } catch ( ModelSynchronizer.MoveColumnVetoException e ) {
            //Swallow
        }
    }

    @Override
    public void updateColumn( final AttributeCol52 originalColumn,
                              final AttributeCol52 editedColumn ) {
        assertNotReadOnly();
        try {
            final List<BaseColumnFieldDiff> diffs = synchronizer.updateColumn( originalColumn,
                                                                               editedColumn );

            //Log change to column definition
            if ( !( diffs == null || diffs.isEmpty() ) ) {
                view.getLayer().draw();
                model.getAuditLog().add( new UpdateColumnAuditLogEntry( identity.getIdentifier(),
                                                                        originalColumn,
                                                                        editedColumn,
                                                                        diffs ) );

                //Refresh Attributes panel
                refreshAttributesPanelEvent.fire( new RefreshAttributesPanelEvent( this,
                                                                                   model.getAttributeCols() ) );
            }
        } catch ( ModelSynchronizer.MoveColumnVetoException e ) {
            //Swallow
        }
    }

    @Override
    public void updateColumn( final MetadataCol52 originalColumn,
                              final MetadataCol52 editedColumn ) {
        assertNotReadOnly();
        try {
            final List<BaseColumnFieldDiff> diffs = synchronizer.updateColumn( originalColumn,
                                                                               editedColumn );

            //Log change to column definition
            if ( !( diffs == null || diffs.isEmpty() ) ) {
                view.getLayer().draw();
                model.getAuditLog().add( new UpdateColumnAuditLogEntry( identity.getIdentifier(),
                                                                        originalColumn,
                                                                        editedColumn,
                                                                        diffs ) );

                //Refresh MetaData panel
                refreshMetaDataPanelEvent.fire( new RefreshMetaDataPanelEvent( this,
                                                                               model.getMetadataCols() ) );
            }
        } catch ( ModelSynchronizer.MoveColumnVetoException e ) {
            //Swallow
        }
    }

    @Override
    public void updateColumn( final Pattern52 originalPattern,
                              final ConditionCol52 originalColumn,
                              final Pattern52 editedPattern,
                              final ConditionCol52 editedColumn ) {
        assertNotReadOnly();
        try {
            final List<BaseColumnFieldDiff> diffs = synchronizer.updateColumn( originalPattern,
                                                                               originalColumn,
                                                                               editedPattern,
                                                                               editedColumn );

            //Log change to column definition
            if ( !( diffs == null || diffs.isEmpty() ) ) {
                view.getLayer().draw();
                model.getAuditLog().add( new UpdateColumnAuditLogEntry( identity.getIdentifier(),
                                                                        originalColumn,
                                                                        editedColumn,
                                                                        diffs ) );

                //Refresh Conditions panel
                refreshConditionsPanelEvent.fire( new RefreshConditionsPanelEvent( this,
                                                                                   model.getConditions() ) );
            }
        } catch ( ModelSynchronizer.MoveColumnVetoException e ) {
            //Swallow
        }
    }

    @Override
    public void updateColumn( final ConditionCol52 originalColumn,
                              final ConditionCol52 editedColumn ) {
        assertNotReadOnly();
        try {
            final List<BaseColumnFieldDiff> diffs = synchronizer.updateColumn( originalColumn,
                                                                               editedColumn );

            //Log change to column definition
            if ( !( diffs == null || diffs.isEmpty() ) ) {
                view.getLayer().draw();
                model.getAuditLog().add( new UpdateColumnAuditLogEntry( identity.getIdentifier(),
                                                                        originalColumn,
                                                                        editedColumn,
                                                                        diffs ) );

                //Refresh Conditions panel
                refreshConditionsPanelEvent.fire( new RefreshConditionsPanelEvent( this,
                                                                                   model.getConditions() ) );
            }
        } catch ( ModelSynchronizer.MoveColumnVetoException e ) {
            //Swallow
        }
    }

    @Override
    public void updateColumn( final ActionCol52 originalColumn,
                              final ActionCol52 editedColumn ) {
        assertNotReadOnly();
        try {
            final List<BaseColumnFieldDiff> diffs = synchronizer.updateColumn( originalColumn,
                                                                               editedColumn );

            //Log change to column definition
            if ( !( diffs == null || diffs.isEmpty() ) ) {
                view.getLayer().draw();
                model.getAuditLog().add( new UpdateColumnAuditLogEntry( identity.getIdentifier(),
                                                                        originalColumn,
                                                                        editedColumn,
                                                                        diffs ) );

                //Refresh Actions panel
                refreshActionsPanelEvent.fire( new RefreshActionsPanelEvent( this,
                                                                             model.getActionCols() ) );
            }
        } catch ( ModelSynchronizer.MoveColumnVetoException e ) {
            //Swallow
        }
    }

    private void initialiseColumn( final BaseColumn column ) {
        final IGridColumn<?> gridColumn = gridWidgetColumnFactory.convertColumn( column,
                                                                                 access,
                                                                                 getView() );
        uiModel.appendColumn( gridColumn );
    }

    private void initialiseRow( final List<BaseColumn> columns,
                                final List<DTCellValue52> row ) {
        final IGridRow uiModelRow = new BaseGridRow();
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
                                     iModelColumn ).setSelectionManager( RowSelectionManager.INSTANCE );
                }
            }
        }
    }

    @Override
    public void onCut() {
        assertNotReadOnly();
        copyCellsToClipboard();
        onDeleteSelectedCells();
        view.showDataCutNotificationEvent();
    }

    @Override
    public void onCopy() {
        assertNotReadOnly();
        copyCellsToClipboard();
        view.showDataCopiedNotificationEvent();
    }

    private void copyCellsToClipboard() {
        final List<IGridData.SelectedCell> selections = uiModel.getSelectedCells();
        if ( selections.size() == 0 ) {
            return;
        }
        int originRowIndex = Integer.MAX_VALUE;
        int originColumnIndex = Integer.MAX_VALUE;
        final Set<Clipboard.ClipboardData> data = new HashSet<>();

        for ( IGridData.SelectedCell sc : selections ) {
            final int rowIndex = sc.getRowIndex();
            final int columnIndex = findUiColumnIndex( sc.getColumnIndex() );
            originRowIndex = Math.min( rowIndex,
                                       originRowIndex );
            originColumnIndex = Math.min( columnIndex,
                                          originColumnIndex );
        }
        for ( IGridData.SelectedCell sc : selections ) {
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
        assertNotReadOnly();
        final Set<Clipboard.ClipboardData> data = clipboard.getData();
        final int currentOriginRowIndex = uiModel.getSelectedCellsOrigin().getRowIndex();
        final int currentOriginColumnIndex = uiModel.getSelectedCellsOrigin().getColumnIndex();

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
            final DataType.DataTypes modelColumnDataType = columnUtilities.getDataType( modelColumn );
            if ( cd.getValue().getDataType().equals( modelColumnDataType ) ) {
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

    @Override
    public void onDeleteSelectedCells() {
        assertNotReadOnly();
        final List<IGridData.SelectedCell> selections = uiModel.getSelectedCells();
        if ( selections.size() == 0 ) {
            return;
        }
        for ( IGridData.SelectedCell sc : selections ) {
            final int rowIndex = sc.getRowIndex();
            final int columnIndex = findUiColumnIndex( sc.getColumnIndex() );
            final BaseColumn column = model.getExpandedColumns().get( columnIndex );
            if ( !( column instanceof RowNumberCol52 ) ) {
                uiModel.deleteCell( rowIndex,
                                    columnIndex );
            }
        }
        view.getLayer().draw();
    }

    @Override
    public void onDeleteSelectedColumns() {
        assertNotReadOnly();
        final Set<Integer> selectedColumnIndexes = getSelectedColumnIndexes();
        final Set<BaseColumn> columnsToDelete = new HashSet<>();
        for ( int selectedColumnIndex : selectedColumnIndexes ) {
            final int columnIndex = findUiColumnIndex( selectedColumnIndex );
            final BaseColumn column = model.getExpandedColumns().get( columnIndex );
            if ( column instanceof AttributeCol52 ) {
                columnsToDelete.add( column );
            } else if ( column instanceof MetadataCol52 ) {
                columnsToDelete.add( column );
            } else if ( column instanceof ConditionCol52 ) {
                columnsToDelete.add( column );
            } else if ( column instanceof ActionCol52 ) {
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
        for ( IGridData.SelectedCell sc : getView().getModel().getSelectedCells() ) {
            columnUsage.add( sc.getColumnIndex() );
        }
        return columnUsage;
    }

    private int findUiColumnIndex( final int modelColumnIndex ) {
        final List<IGridColumn<?>> columns = uiModel.getColumns();
        for ( int uiColumnIndex = 0; uiColumnIndex < columns.size(); uiColumnIndex++ ) {
            final IGridColumn<?> c = columns.get( uiColumnIndex );
            if ( c.getIndex() == modelColumnIndex ) {
                return uiColumnIndex;
            }
        }
        throw new IllegalStateException( "Column was not found!" );
    }

    @Override
    public void onDeleteSelectedRows() {
        assertNotReadOnly();
        Set<Integer> selectedRowIndexes;
        while ( !( selectedRowIndexes = getSelectedRowIndexes() ).isEmpty() ) {
            final int rowIndex = selectedRowIndexes.iterator().next();
            deleteRow( rowIndex );
        }
    }

    private void deleteRow( final int rowIndex ) {
        try {
            synchronizer.deleteRow( rowIndex );
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
        assertNotReadOnly();
        final Set<Integer> selectedRowIndexes = getSelectedRowIndexes();
        if ( selectedRowIndexes.size() != 1 ) {
            return;
        }
        final int rowIndex = selectedRowIndexes.iterator().next();
        insertRow( rowIndex );
    }

    @Override
    public void onInsertRowBelow() {
        assertNotReadOnly();
        final Set<Integer> selectedRowIndexes = getSelectedRowIndexes();
        if ( selectedRowIndexes.size() != 1 ) {
            return;
        }
        final int rowIndex = selectedRowIndexes.iterator().next();
        insertRow( rowIndex + 1 );
    }

    private Set<Integer> getSelectedRowIndexes() {
        final Set<Integer> rowUsage = new HashSet<>();
        for ( IGridData.SelectedCell sc : getView().getModel().getSelectedCells() ) {
            rowUsage.add( sc.getRowIndex() );
        }
        return rowUsage;
    }

    private void insertRow( final int rowIndex ) {
        try {
            synchronizer.insertRow( rowIndex );
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
        assertNotReadOnly();
        final List<IGridData.SelectedCell> selections = uiModel.getSelectedCells();
        if ( selections.size() != 1 ) {
            return;
        }
        final IGridData.SelectedCell selection = selections.get( 0 );
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
        return isReadOnly;
    }

    @Override
    public void setReadOnly( final boolean isReadOnly ) {
        this.isReadOnly = isReadOnly;
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

    private void assertNotReadOnly() {
        if ( !this.access.isEditable() ) {
            throw new IllegalStateException( "Decision Table is read-only and cannot be modified." );
        }
    }

}
