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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.ait.lienzo.client.core.types.Point2D;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.screens.guided.dtable.client.editor.menu.RadarMenuBuilder;
import org.drools.workbench.screens.guided.dtable.client.widget.table.events.cdi.DecisionTableColumnSelectedEvent;
import org.drools.workbench.screens.guided.dtable.client.widget.table.events.cdi.DecisionTablePinnedEvent;
import org.drools.workbench.screens.guided.dtable.client.widget.table.events.cdi.DecisionTableSelectedEvent;
import org.drools.workbench.screens.guided.dtable.client.widget.table.events.cdi.RefreshActionsPanelEvent;
import org.drools.workbench.screens.guided.dtable.client.widget.table.events.cdi.RefreshAttributesPanelEvent;
import org.drools.workbench.screens.guided.dtable.client.widget.table.events.cdi.RefreshConditionsPanelEvent;
import org.drools.workbench.screens.guided.dtable.client.widget.table.events.cdi.RefreshMetaDataPanelEvent;
import org.drools.workbench.screens.guided.dtable.client.widget.table.popovers.ColumnHeaderPopOver;
import org.drools.workbench.screens.guided.dtable.client.widget.table.popovers.ColumnHeaderPopOverHandler;
import org.drools.workbench.screens.guided.dtable.model.GuidedDecisionTableEditorContent;
import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.uberfire.backend.vfs.ObservablePath;
import org.uberfire.ext.wires.core.grids.client.model.Bounds;
import org.uberfire.ext.wires.core.grids.client.widget.grid.GridWidget;
import org.uberfire.ext.wires.core.grids.client.widget.grid.impl.BaseGridWidgetKeyboardHandler;
import org.uberfire.ext.wires.core.grids.client.widget.grid.impl.KeyboardOperationClearCell;
import org.uberfire.ext.wires.core.grids.client.widget.grid.impl.KeyboardOperationEditCell;
import org.uberfire.ext.wires.core.grids.client.widget.grid.impl.KeyboardOperationMoveDown;
import org.uberfire.ext.wires.core.grids.client.widget.grid.impl.KeyboardOperationMoveLeft;
import org.uberfire.ext.wires.core.grids.client.widget.grid.impl.KeyboardOperationMoveRight;
import org.uberfire.ext.wires.core.grids.client.widget.grid.impl.KeyboardOperationMoveUp;
import org.uberfire.ext.wires.core.grids.client.widget.grid.impl.KeyboardOperationSelectBottomRightCell;
import org.uberfire.ext.wires.core.grids.client.widget.grid.impl.KeyboardOperationSelectTopLeftCell;
import org.uberfire.ext.wires.core.grids.client.widget.layer.GridLayer;
import org.uberfire.ext.wires.core.grids.client.widget.layer.pinning.TransformMediator;
import org.uberfire.mvp.ParameterizedCommand;
import org.uberfire.mvp.PlaceRequest;

@Dependent
public class GuidedDecisionTableModellerPresenter implements GuidedDecisionTableModellerView.Presenter {

    private final GuidedDecisionTableModellerView view;
    private final ManagedInstance<GuidedDecisionTableView.Presenter> dtPresenterProvider;
    private final Event<RadarMenuBuilder.UpdateRadarEvent> updateRadarEvent;
    private final Event<DecisionTablePinnedEvent> pinnedEvent;
    private final ColumnHeaderPopOver columnHeaderPopOver;

    private GuidedDecisionTableView.Presenter activeDecisionTable = null;
    private Set<GuidedDecisionTableView.Presenter> availableDecisionTables = new HashSet<GuidedDecisionTableView.Presenter>();
    private Set<HandlerRegistration> handlerRegistrations = new HashSet<>();

    @Inject
    public GuidedDecisionTableModellerPresenter( final GuidedDecisionTableModellerView view,
                                                 final ManagedInstance<GuidedDecisionTableView.Presenter> dtPresenterProvider,
                                                 final GuidedDecisionTableModellerContextMenuSupport contextMenuSupport,
                                                 final Event<RadarMenuBuilder.UpdateRadarEvent> updateRadarEvent,
                                                 final Event<DecisionTablePinnedEvent> pinnedEvent,
                                                 final ColumnHeaderPopOver columnHeaderPopOver ) {
        this.view = view;
        this.dtPresenterProvider = dtPresenterProvider;
        this.updateRadarEvent = updateRadarEvent;
        this.pinnedEvent = pinnedEvent;
        this.columnHeaderPopOver = columnHeaderPopOver;

        this.view.init( this );

        //Add support for Column PopOver
        handlerRegistrations.add( view.getGridLayerView().addNodeMouseMoveHandler( new ColumnHeaderPopOverHandler( this,
                                                                                                                   columnHeaderPopOver ) ) );
        handlerRegistrations.add( view.getGridLayerView().addNodeMouseOutHandler( ( e ) -> columnHeaderPopOver.hide() ) );

        //Add support for keyboard operations
        final GridLayer layer = view.getGridLayerView();
        final BaseGridWidgetKeyboardHandler handler = new BaseGridWidgetKeyboardHandler( layer );
        handler.addOperation( new KeyboardOperationClearCell( layer ) {
                                  @Override
                                  protected void clearCells( final GridWidget gridWidget ) {
                                      getActiveDecisionTable().onDeleteSelectedCells();
                                  }
                              },
                              new KeyboardOperationEditCell( layer ),
                              new KeyboardOperationMoveLeft( layer ),
                              new KeyboardOperationMoveRight( layer ),
                              new KeyboardOperationMoveUp( layer ),
                              new KeyboardOperationMoveDown( layer ),
                              new KeyboardOperationSelectTopLeftCell( layer ),
                              new KeyboardOperationSelectBottomRightCell( layer ) );
        handlerRegistrations.add( view.addKeyDownHandler( handler ) );

        //Add support for context menus
        handlerRegistrations.add( view.addContextMenuHandler( contextMenuSupport.getContextMenuHandler( this ) ) );
        handlerRegistrations.add( view.addMouseDownHandler( contextMenuSupport.getContextMenuMouseDownHandler() ) );
    }

    @Override
    public void onClose() {
        view.clear();
        releaseDecisionTables();
        releaseHandlerRegistrations();
    }

    @Override
    public void releaseDecisionTables() {
        //Release objects created manually with BeanManager
        availableDecisionTables.stream().forEach( GuidedDecisionTableView.Presenter::onClose );
        availableDecisionTables.clear();
    }

    @Override
    public void releaseHandlerRegistrations() {
        //Release HandlerRegistrations
        handlerRegistrations.stream().forEach( HandlerRegistration::removeHandler );
        handlerRegistrations.clear();
    }

    @Override
    public GuidedDecisionTableView.Presenter addDecisionTable( final ObservablePath path,
                                                               final PlaceRequest placeRequest,
                                                               final GuidedDecisionTableEditorContent content,
                                                               final boolean isReadOnly,
                                                               final Double x,
                                                               final Double y ) {
        //Instantiate a Presenter for the Decision Table
        final GuidedDecisionTableView.Presenter dtPresenter = dtPresenterProvider.get();

        //Set content of new Presenter
        dtPresenter.setContent( path,
                                placeRequest,
                                content,
                                this,
                                isReadOnly );

        //Add new view to Modeller
        final double dtViewX = ( x == null ? getDecisionTableX( dtPresenter ) : x );
        final double dtViewY = ( y == null ? getDecisionTableY( dtPresenter ) : y );
        dtPresenter.getView().setLocation( dtViewX,
                                           dtViewY );

        availableDecisionTables.add( dtPresenter );

        updateLinks();

        view.addDecisionTable( dtPresenter.getView() );

        return dtPresenter;
    }

    private double getDecisionTableX( final GuidedDecisionTableView.Presenter dtPresenter ) {
        final Bounds bounds = getView().getBounds();
        final double x = bounds.getX() + ( bounds.getWidth() - dtPresenter.getView().getWidth() ) / 2;
        return x;
    }

    @SuppressWarnings("unused")
    private double getDecisionTableY( final GuidedDecisionTableView.Presenter dtPresenter ) {
        final Bounds bounds = getView().getBounds();
        double y = bounds.getY() + ( bounds.getHeight() * 0.25 );
        for ( GuidedDecisionTableView.Presenter p : availableDecisionTables ) {
            y = y + p.getView().getHeight() + 200;
        }
        return y;
    }

    @Override
    public GuidedDecisionTableView.Presenter refreshDecisionTable( final GuidedDecisionTableView.Presenter dtPresenter,
                                                                   final ObservablePath path,
                                                                   final PlaceRequest placeRequest,
                                                                   final GuidedDecisionTableEditorContent content,
                                                                   final boolean isReadOnly ) {
        //Remove old view from Modeller
        final Point2D oldLocation = dtPresenter.getView().getLocation();
        view.removeDecisionTable( dtPresenter.getView(),
                                  () -> {
                                      //Refresh existing Presenter with new content
                                      dtPresenter.refreshContent( path,
                                                                  placeRequest,
                                                                  content,
                                                                  isReadOnly );

                                      //Add new view to Modeller and ensure selection
                                      dtPresenter.getView().setLocation( oldLocation );
                                      view.addDecisionTable( dtPresenter.getView() );

//                                      doDecisionTableSelected( dtPresenter );
                                  } );

        return dtPresenter;
    }

    @Override
    public void activateDecisionTable( final GuidedDecisionTableView.Presenter dtPresenter ) {
        if ( !isDecisionTableAvailable( dtPresenter ) ) {
            return;
        }
        view.activateDecisionTable( dtPresenter.getView() );
        activeDecisionTable = dtPresenter;
    }

    @Override
    public void removeDecisionTable( final GuidedDecisionTableView.Presenter dtPresenter ) {
        final Command afterRemovalCommand = () -> {
            view.setEnableColumnCreation( false );
            view.refreshAttributeWidget( Collections.emptyList() );
            view.refreshMetaDataWidget( Collections.emptyList() );
            view.refreshConditionsWidget( Collections.emptyList() );
            view.refreshActionsWidget( Collections.emptyList() );
            view.refreshColumnsNote( false );

            availableDecisionTables.remove( dtPresenter );

            if ( dtPresenter.equals( activeDecisionTable ) ) {
                activeDecisionTable = null;
            }
            dtPresenter.onClose();
        };
        view.removeDecisionTable( dtPresenter.getView(),
                                  afterRemovalCommand );
    }

    @Override
    public void onLockStatusUpdated( final GuidedDecisionTableView.Presenter dtPresenter ) {
        if ( dtPresenter == null ) {
            return;
        }

        //Update Definitions Panel if active Decision Table's lock has changed
        if ( dtPresenter.equals( getActiveDecisionTable() ) ) {
            refreshDefinitionsPanel( dtPresenter );
        }
    }

    @Override
    public GuidedDecisionTableView.Presenter getActiveDecisionTable() {
        return activeDecisionTable;
    }

    @Override
    public Set<GuidedDecisionTableView.Presenter> getAvailableDecisionTables() {
        return Collections.unmodifiableSet( availableDecisionTables );
    }

    @Override
    public boolean isDecisionTableAvailable( final GuidedDecisionTableView.Presenter dtPresenter ) {
        return availableDecisionTables.contains( dtPresenter );
    }

    @Override
    public boolean isActiveDecisionTableEditable() {
        final GuidedDecisionTableView.Presenter dtPresenter = getActiveDecisionTable();
        if ( dtPresenter == null ) {
            return false;
        }
        return dtPresenter.getAccess().isEditable();
    }

    @Override
    public GuidedDecisionTableModellerView getView() {
        return view;
    }

    @Override
    public void setZoom( final int zoom ) {
        view.setZoom( zoom );
    }

    @Override
    public void onInsertColumn() {
        final GuidedDecisionTableView.Presenter dtPresenter = getActiveDecisionTable();
        if ( dtPresenter == null ) {
            return;
        }
        view.onInsertColumn();
    }

    @Override
    public void enterPinnedMode( final GridWidget gridWidget,
                                 final Command onStartCommand ) {
        columnHeaderPopOver.hide();
        view.getGridLayerView().enterPinnedMode( gridWidget,
                                                 onStartCommand );
    }

    @Override
    public void exitPinnedMode( final Command onCompleteCommand ) {
        columnHeaderPopOver.hide();
        view.getGridLayerView().exitPinnedMode( onCompleteCommand );
    }

    @Override
    public void updatePinnedContext( final GridWidget gridWidget ) throws IllegalStateException {
        columnHeaderPopOver.hide();
        view.getGridLayerView().updatePinnedContext( gridWidget );
    }

    @Override
    public PinnedContext getPinnedContext() {
        return view.getGridLayerView().getPinnedContext();
    }

    @Override
    public boolean isGridPinned() {
        return view.getGridLayerView().isGridPinned();
    }

    @Override
    public TransformMediator getDefaultTransformMediator() {
        return view.getGridLayerView().getDefaultTransformMediator();
    }

    @Override
    public void onDecisionTableSelected( final @Observes DecisionTableSelectedEvent event ) {
        final GuidedDecisionTableView.Presenter dtPresenter = event.getPresenter();
        if ( dtPresenter == null ) {
            return;
        }
        if ( !isDecisionTableAvailable( dtPresenter ) ) {
            return;
        }
        if ( dtPresenter.equals( getActiveDecisionTable() ) ) {
            return;
        }
        doDecisionTableSelected( dtPresenter );
    }

    private void doDecisionTableSelected( final GuidedDecisionTableView.Presenter dtPresenter ) {
        //Store selected decision table
        activeDecisionTable = dtPresenter;

        //Bootstrap Decision Table analysis
        for ( GuidedDecisionTableView.Presenter p : getAvailableDecisionTables() ) {
            if ( p.equals( dtPresenter ) ) {
                p.initialiseAnalysis();
            } else {
                p.terminateAnalysis();
            }
        }

        //Update view with selected decision table detail
        final GuidedDecisionTable52 model = dtPresenter.getModel();
        dtPresenter.getPackageParentRuleNames( new ParameterizedCommand<Collection<String>>() {
            @Override
            public void execute( Collection<String> availableParentRuleNames ) {
                view.refreshRuleInheritance( model.getParentName(),
                                             availableParentRuleNames );
            }
        } );
        refreshDefinitionsPanel( dtPresenter );

        //Delegate highlighting of selected decision table to ISelectionManager
        view.select( dtPresenter.getView() );

        //If the Layer is "pinned" flip to the selected Decision Table
        if ( isGridPinned() ) {
            view.getGridLayerView().flipToGridWidget( dtPresenter.getView() );
        }
    }

    void refreshDefinitionsPanel( final GuidedDecisionTableView.Presenter dtPresenter ) {
        final GuidedDecisionTable52 model = dtPresenter.getModel();
        view.setEnableColumnCreation( dtPresenter.getAccess().isEditable() );
        view.refreshAttributeWidget( model.getAttributeCols() );
        view.refreshMetaDataWidget( model.getMetadataCols() );
        view.refreshConditionsWidget( model.getConditions() );
        view.refreshActionsWidget( model.getActionCols() );
        view.refreshColumnsNote( dtPresenter.hasColumnDefinitions() );
    }

    @Override
    public void onDecisionTableLinkedColumnSelected( final @Observes DecisionTableColumnSelectedEvent event ) {
        if ( event.getColumn() == null ) {
            return;
        }
        view.selectLinkedColumn( event.getColumn() );
    }

    @Override
    public void onRefreshAttributesPanelEvent( final @Observes RefreshAttributesPanelEvent event ) {
        refreshPanel( event.getPresenter(),
                      event.getColumns(),
                      view::refreshAttributeWidget );
    }

    @Override
    public void onRefreshMetaDataPanelEvent( final @Observes RefreshMetaDataPanelEvent event ) {
        refreshPanel( event.getPresenter(),
                      event.getColumns(),
                      view::refreshMetaDataWidget );
    }

    @Override
    public void onRefreshConditionsPanelEvent( final @Observes RefreshConditionsPanelEvent event ) {
        refreshPanel( event.getPresenter(),
                      event.getColumns(),
                      view::refreshConditionsWidget );
    }

    @Override
    public void onRefreshActionsPanelEvent( final @Observes RefreshActionsPanelEvent event ) {
        refreshPanel( event.getPresenter(),
                      event.getColumns(),
                      view::refreshActionsWidget );
    }

    private <C> void refreshPanel( final GuidedDecisionTableView.Presenter dtPresenter,
                                   final List<C> columns,
                                   final ParameterizedCommand<List<C>> command ) {
        if ( dtPresenter == null ) {
            return;
        }
        if ( !isDecisionTableAvailable( dtPresenter ) ) {
            return;
        }
        command.execute( columns );
    }

    @Override
    public void updateRadar() {
        updateRadarEvent.fire( new RadarMenuBuilder.UpdateRadarEvent( this ) );
    }

    @Override
    public void onViewPinned( final boolean isPinned ) {
        pinnedEvent.fire( new DecisionTablePinnedEvent( this,
                                                        isPinned ) );
    }

    @Override
    public void updateLinks() {
        for ( GuidedDecisionTableView.Presenter dtPresenter : getAvailableDecisionTables() ) {
            dtPresenter.link( getAvailableDecisionTables() );
        }
        getView().getGridLayerView().refreshGridWidgetConnectors();
    }

}
