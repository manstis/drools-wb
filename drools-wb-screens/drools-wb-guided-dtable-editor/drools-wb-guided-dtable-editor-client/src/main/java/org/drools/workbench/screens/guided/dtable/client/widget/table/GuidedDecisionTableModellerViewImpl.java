package org.drools.workbench.screens.guided.dtable.client.widget.table;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;

import com.ait.lienzo.client.core.event.NodeMouseMoveEvent;
import com.ait.lienzo.client.core.types.Transform;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.drools.workbench.models.guided.dtable.shared.model.ActionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.AttributeCol52;
import org.drools.workbench.models.guided.dtable.shared.model.BRLActionColumn;
import org.drools.workbench.models.guided.dtable.shared.model.BRLConditionColumn;
import org.drools.workbench.models.guided.dtable.shared.model.BaseColumn;
import org.drools.workbench.models.guided.dtable.shared.model.CompositeColumn;
import org.drools.workbench.models.guided.dtable.shared.model.ConditionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.LimitedEntryBRLActionColumn;
import org.drools.workbench.models.guided.dtable.shared.model.LimitedEntryBRLConditionColumn;
import org.drools.workbench.models.guided.dtable.shared.model.MetadataCol52;
import org.drools.workbench.models.guided.dtable.shared.model.Pattern52;
import org.drools.workbench.screens.guided.dtable.client.resources.GuidedDecisionTableResources;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableConstants;
import org.drools.workbench.screens.guided.dtable.client.resources.images.GuidedDecisionTableImageResources508;
import org.drools.workbench.screens.guided.dtable.client.widget.DefaultValueWidgetFactory;
import org.drools.workbench.screens.guided.rule.client.editor.RuleAttributeWidget;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.kie.workbench.common.widgets.client.ruleselector.RuleSelector;
import org.uberfire.ext.widgets.common.client.common.DecoratedDisclosurePanel;
import org.uberfire.ext.widgets.common.client.common.ImageButton;
import org.uberfire.ext.widgets.common.client.common.PrettyFormLayout;
import org.uberfire.ext.widgets.common.client.common.SmallLabel;
import org.uberfire.ext.widgets.common.client.common.popups.FormStylePopup;
import org.uberfire.ext.widgets.common.client.common.popups.footers.ModalFooterOKCancelButtons;
import org.uberfire.ext.wires.core.grids.client.model.Bounds;
import org.uberfire.ext.wires.core.grids.client.model.IBounds;
import org.uberfire.ext.wires.core.grids.client.model.IGridColumn;
import org.uberfire.ext.wires.core.grids.client.widget.GridLayer;
import org.uberfire.ext.wires.core.grids.client.widget.GridLienzoPanel;
import org.uberfire.ext.wires.core.grids.client.widget.IBaseGridWidget;
import org.uberfire.ext.wires.core.grids.client.widget.pinning.BoundaryRestriction;
import org.uberfire.ext.wires.core.grids.client.widget.pinning.IGridPinnedModeManager;
import org.uberfire.ext.wires.core.grids.client.widget.pinning.IRestriction;
import org.uberfire.ext.wires.core.grids.client.widget.pinning.RestrictedMousePanMediator;

public class GuidedDecisionTableModellerViewImpl extends Composite implements GuidedDecisionTableModellerView {

    private static final double VP_SCALE = 1.0;

    private final double BOUNDS_MIN_X = -2000;
    private final double BOUNDS_MAX_X = 2000;
    private final double BOUNDS_MIN_Y = -2000;
    private final double BOUNDS_MAX_Y = 2000;

    private final IBounds bounds = new Bounds( BOUNDS_MIN_X,
                                               BOUNDS_MIN_Y,
                                               BOUNDS_MAX_X - BOUNDS_MIN_X,
                                               BOUNDS_MAX_Y - BOUNDS_MIN_Y );

    interface GuidedDecisionTableModellerViewImplUiBinder extends UiBinder<Widget, GuidedDecisionTableModellerViewImpl> {

    }

    private static GuidedDecisionTableModellerViewImplUiBinder uiBinder = GWT.create( GuidedDecisionTableModellerViewImplUiBinder.class );

    private static String SECTION_SEPARATOR = "..................";

    private enum NewColumnTypes {
        METADATA_ATTRIBUTE,
        CONDITION_SIMPLE,
        CONDITION_BRL_FRAGMENT,
        ACTION_UPDATE_FACT_FIELD,
        ACTION_INSERT_FACT_FIELD,
        ACTION_RETRACT_FACT,
        ACTION_WORKITEM,
        ACTION_WORKITEM_UPDATE_FACT_FIELD,
        ACTION_WORKITEM_INSERT_FACT_FIELD,
        ACTION_BRL_FRAGMENT
    }

    private GuidedDecisionTableModellerView.Presenter presenter;

    private final GridLayer gridLayer = new GridLayer() {
        @Override
        public void enterPinnedMode( final IBaseGridWidget gridWidget,
                                     final Command onStartCommand ) {
            super.enterPinnedMode( gridWidget,
                                   new Command() {
                                       @Override
                                       public void execute() {
                                           onStartCommand.execute();
                                           presenter.onViewPinned( true );
                                       }
                                   } );
        }

        @Override
        public void exitPinnedMode( final Command onCompleteCommand ) {
            super.exitPinnedMode( new Command() {
                @Override
                public void execute() {
                    onCompleteCommand.execute();
                    presenter.onViewPinned( false );
                }
            } );
        }

        @Override
        public IRestriction getDefaultRestriction() {
            return new BoundaryRestriction( bounds );
        }

    };

    private final RestrictedMousePanMediator mousePanMediator = new RestrictedMousePanMediator( gridLayer ) {
        @Override
        protected void onMouseMove( final NodeMouseMoveEvent event ) {
            super.onMouseMove( event );
            presenter.updateRadar();
        }
    };

    @UiField
    VerticalPanel configuration;

    @UiField(provided = true)
    GridLienzoPanel gridPanel = new GridLienzoPanel() {

        @Override
        public void onResize() {
            Scheduler.get().scheduleDeferred( new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {
                    final int width = getParent().getOffsetWidth();
                    final int height = getParent().getOffsetHeight();
                    if ( ( width != 0 ) && ( height != 0 ) ) {
                        domElementContainer.setPixelSize( width,
                                                          height );
                        lienzoPanel.setPixelSize( width,
                                                  height );
                    }

                    final IRestriction restriction = mousePanMediator.getRestriction();
                    final Transform transform = restriction.adjust( gridLayer.getViewport().getTransform(),
                                                                    gridLayer.getVisibleBounds() );
                    gridLayer.getViewport().setTransform( transform );
                    gridLayer.draw();
                }
            } );

        }
    };

    private Button addButton = new Button() {{
        setIcon( IconType.PLUS_SQUARE );
        setText( GuidedDecisionTableConstants.INSTANCE.NewColumn() );
        setTitle( GuidedDecisionTableConstants.INSTANCE.AddNewColumn() );
        setEnabled( false );
    }};
    private VerticalPanel config = new VerticalPanel();

    private DecoratedDisclosurePanel disclosurePanelConditions;
    private DecoratedDisclosurePanel disclosurePanelActions;
    private DecoratedDisclosurePanel disclosurePanelAttributes;
    private DecoratedDisclosurePanel disclosurePanelMetaData;
    private PrettyFormLayout configureColumnsNote;
    private VerticalPanel attributeConfigWidget;
    private VerticalPanel metaDataConfigWidget;
    private VerticalPanel conditionsConfigWidget;
    private VerticalPanel actionsConfigWidget;

    private final RuleSelector ruleSelector = new RuleSelector();

    public GuidedDecisionTableModellerViewImpl() {
        initWidget( uiBinder.createAndBindUi( this ) );
    }

    @Override
    public void init( final GuidedDecisionTableModellerView.Presenter presenter ) {
        this.presenter = presenter;
    }

    @PostConstruct
    public void setup() {
        configureColumnsNote = new PrettyFormLayout();
        configureColumnsNote.startSection();
        configureColumnsNote.addRow( new HTML( AbstractImagePrototype.create( GuidedDecisionTableResources.INSTANCE.images().information() ).getHTML()
                                                       + "&nbsp;"
                                                       + GuidedDecisionTableConstants.INSTANCE.ConfigureColumnsNote() ) );
        configureColumnsNote.endSection();

        final DecoratedDisclosurePanel disclosurePanelContainer = new DecoratedDisclosurePanel( GuidedDecisionTableConstants.INSTANCE.DecisionTable() );
        disclosurePanelContainer.setTitle( GuidedDecisionTableConstants.INSTANCE.DecisionTable() );
        disclosurePanelContainer.setWidth( "100%" );

        config.setWidth( "100%" );
        disclosurePanelContainer.add( config );

        config.add( newColumn() );

        disclosurePanelConditions = new DecoratedDisclosurePanel( GuidedDecisionTableConstants.INSTANCE.ConditionColumns() );
        disclosurePanelConditions.setWidth( "75%" );
        disclosurePanelConditions.setOpen( false );
        disclosurePanelConditions.add( getConditionsWidget() );
        config.add( disclosurePanelConditions );

        disclosurePanelActions = new DecoratedDisclosurePanel( GuidedDecisionTableConstants.INSTANCE.ActionColumns() );
        disclosurePanelActions.setWidth( "75%" );
        disclosurePanelActions.setOpen( false );
        disclosurePanelActions.add( getActionsWidget() );
        config.add( disclosurePanelActions );

        disclosurePanelAttributes = new DecoratedDisclosurePanel( GuidedDecisionTableConstants.INSTANCE.Options() );
        disclosurePanelAttributes.setWidth( "75%" );
        disclosurePanelAttributes.setOpen( false );
        disclosurePanelAttributes.add( getAttributesWidget() );
        config.add( disclosurePanelAttributes );

        disclosurePanelMetaData = new DecoratedDisclosurePanel( GuidedDecisionTableConstants.INSTANCE.Options() );
        disclosurePanelMetaData.setWidth( "75%" );
        disclosurePanelMetaData.setOpen( false );
        disclosurePanelMetaData.add( getMetaDataWidget() );
        config.add( disclosurePanelMetaData );

        configuration.add( disclosurePanelContainer );
        configuration.add( configureColumnsNote );
        configuration.add( getRuleInheritanceWidget() );

        //Lienzo stuff - Set default scale
        final Transform transform = new Transform().scale( VP_SCALE );
        gridPanel.getViewport().setTransform( transform );

        //Lienzo stuff - Add mouse pan support
        mousePanMediator.setRestriction( new BoundaryRestriction( bounds ) );
        gridPanel.getViewport().getMediators().push( mousePanMediator );
        mousePanMediator.setBatchDraw( true );

        //Wire-up widgets
        gridPanel.add( gridLayer );
    }

    @Override
    public void onResize() {
        gridPanel.onResize();
        presenter.updateRadar();
    }

    @Override
    public HandlerRegistration addKeyDownHandler( final KeyDownHandler handler ) {
        return RootPanel.get().addDomHandler( handler,
                                              KeyDownEvent.getType() );
    }

    @Override
    public HandlerRegistration addContextMenuHandler( final ContextMenuHandler handler ) {
        return gridPanel.addDomHandler( handler,
                                        ContextMenuEvent.getType() );
    }

    @Override
    public HandlerRegistration addMouseDownHandler( final MouseDownHandler handler ) {
        return RootPanel.get().addDomHandler( handler,
                                              MouseDownEvent.getType() );
    }

    private Widget getRuleInheritanceWidget() {
        final HorizontalPanel result = new HorizontalPanel();
        result.add( new Label( GuidedDecisionTableConstants.INSTANCE.AllTheRulesInherit() ) );
        ruleSelector.addValueChangeHandler( new ValueChangeHandler<String>() {
            @Override
            public void onValueChange( ValueChangeEvent<String> event ) {
                presenter.getActiveDecisionTable().setParentRuleName( event.getValue() );
            }
        } );
        result.add( ruleSelector );
        return result;
    }

    @Override
    public void clear() {
        gridLayer.removeAll();
    }

    @Override
    public void addDecisionTable( final IBaseGridWidget gridWidget ) {
        gridLayer.add( gridWidget );
        gridLayer.batch();
    }

    @Override
    public void removeDecisionTable( final IBaseGridWidget gridWidget,
                                     final Command afterRemovalCommand ) {
        if ( gridWidget == null ) {
            return;
        }
        final Command remove = () -> {
            gridLayer.remove( gridWidget );
            gridLayer.batch();
            afterRemovalCommand.execute();
        };
        if ( gridLayer.isGridPinned() ) {
            final IGridPinnedModeManager.PinnedContext context = gridLayer.getPinnedContext();
            if ( gridWidget.equals( context.getGridWidget() ) ) {
                gridLayer.exitPinnedMode( remove );
            }
        } else {
            remove.execute();
        }
    }

    @Override
    public void setEnableColumnCreation( final boolean enabled ) {
        addButton.setEnabled( enabled );
    }

    private Widget newColumn() {
        addButton.addClickHandler( new ClickHandler() {
            public void onClick( ClickEvent w ) {
                doNewColumn();
            }
        } );

        return addButton;
    }

    private void doNewColumn() {
        final FormStylePopup pop = new FormStylePopup( GuidedDecisionTableConstants.INSTANCE.AddNewColumn() );

        //List of basic column types
        final ListBox choice = new ListBox();
        choice.setVisibleItemCount( NewColumnTypes.values().length );
        choice.setWidth( "100%" );

        choice.addItem( GuidedDecisionTableConstants.INSTANCE.AddNewMetadataOrAttributeColumn(),
                        NewColumnTypes.METADATA_ATTRIBUTE.name() );
        choice.addItem( SECTION_SEPARATOR );
        choice.addItem( GuidedDecisionTableConstants.INSTANCE.AddNewConditionSimpleColumn(),
                        NewColumnTypes.CONDITION_SIMPLE.name() );
        choice.addItem( SECTION_SEPARATOR );
        choice.addItem( GuidedDecisionTableConstants.INSTANCE.SetTheValueOfAField(),
                        NewColumnTypes.ACTION_UPDATE_FACT_FIELD.name() );
        choice.addItem( GuidedDecisionTableConstants.INSTANCE.SetTheValueOfAFieldOnANewFact(),
                        NewColumnTypes.ACTION_INSERT_FACT_FIELD.name() );
        choice.addItem( GuidedDecisionTableConstants.INSTANCE.DeleteAnExistingFact(),
                        NewColumnTypes.ACTION_RETRACT_FACT.name() );

        //Checkbox to include Advanced Action types
        final CheckBox chkIncludeAdvancedOptions = new CheckBox( SafeHtmlUtils.fromString( GuidedDecisionTableConstants.INSTANCE.IncludeAdvancedOptions() ) );
        chkIncludeAdvancedOptions.setValue( false );
        chkIncludeAdvancedOptions.addClickHandler( new ClickHandler() {

            public void onClick( ClickEvent event ) {
                if ( chkIncludeAdvancedOptions.getValue() ) {
                    addItem( 3,
                             GuidedDecisionTableConstants.INSTANCE.AddNewConditionBRLFragment(),
                             NewColumnTypes.CONDITION_BRL_FRAGMENT.name() );
                    addItem( GuidedDecisionTableConstants.INSTANCE.WorkItemAction(),
                             NewColumnTypes.ACTION_WORKITEM.name() );
                    addItem( GuidedDecisionTableConstants.INSTANCE.WorkItemActionSetField(),
                             NewColumnTypes.ACTION_WORKITEM_UPDATE_FACT_FIELD.name() );
                    addItem( GuidedDecisionTableConstants.INSTANCE.WorkItemActionInsertFact(),
                             NewColumnTypes.ACTION_WORKITEM_INSERT_FACT_FIELD.name() );
                    addItem( GuidedDecisionTableConstants.INSTANCE.AddNewActionBRLFragment(),
                             NewColumnTypes.ACTION_BRL_FRAGMENT.name() );
                } else {
                    removeItem( NewColumnTypes.CONDITION_BRL_FRAGMENT.name() );
                    removeItem( NewColumnTypes.ACTION_WORKITEM.name() );
                    removeItem( NewColumnTypes.ACTION_WORKITEM_UPDATE_FACT_FIELD.name() );
                    removeItem( NewColumnTypes.ACTION_WORKITEM_INSERT_FACT_FIELD.name() );
                    removeItem( NewColumnTypes.ACTION_BRL_FRAGMENT.name() );
                }
            }

            private void addItem( int index,
                                  String item,
                                  String value ) {
                for ( int itemIndex = 0; itemIndex < choice.getItemCount(); itemIndex++ ) {
                    if ( choice.getValue( itemIndex ).equals( value ) ) {
                        return;
                    }
                }
                choice.insertItem( item,
                                   value,
                                   index );
            }

            private void addItem( String item,
                                  String value ) {
                for ( int itemIndex = 0; itemIndex < choice.getItemCount(); itemIndex++ ) {
                    if ( choice.getValue( itemIndex ).equals( value ) ) {
                        return;
                    }
                }
                choice.addItem( item,
                                value );
            }

            private void removeItem( String value ) {
                for ( int itemIndex = 0; itemIndex < choice.getItemCount(); itemIndex++ ) {
                    if ( choice.getValue( itemIndex ).equals( value ) ) {
                        choice.removeItem( itemIndex );
                        break;
                    }
                }
            }

        } );

        //OK button to create column
        final ModalFooterOKCancelButtons footer = new ModalFooterOKCancelButtons( new Command() {
            @Override
            public void execute() {
                String s = choice.getValue( choice.getSelectedIndex() );
                if ( s.equals( NewColumnTypes.METADATA_ATTRIBUTE.name() ) ) {
                    presenter.getActiveDecisionTable().newAttributeOrMetaDataColumn();

                } else if ( s.equals( NewColumnTypes.CONDITION_SIMPLE.name() ) ) {
                    presenter.getActiveDecisionTable().newConditionColumn();

                } else if ( s.equals( NewColumnTypes.CONDITION_BRL_FRAGMENT.name() ) ) {
                    presenter.getActiveDecisionTable().newConditionBRLFragment();

                } else if ( s.equals( NewColumnTypes.ACTION_INSERT_FACT_FIELD.name() ) ) {
                    presenter.getActiveDecisionTable().newActionInsertColumn();

                } else if ( s.equals( NewColumnTypes.ACTION_UPDATE_FACT_FIELD.name() ) ) {
                    presenter.getActiveDecisionTable().newActionSetColumn();

                } else if ( s.equals( NewColumnTypes.ACTION_RETRACT_FACT.name() ) ) {
                    presenter.getActiveDecisionTable().newActionRetractFact();

                } else if ( s.equals( NewColumnTypes.ACTION_WORKITEM.name() ) ) {
                    presenter.getActiveDecisionTable().newActionWorkItem();

                } else if ( s.equals( NewColumnTypes.ACTION_WORKITEM_UPDATE_FACT_FIELD.name() ) ) {
                    presenter.getActiveDecisionTable().newActionWorkItemSetField();

                } else if ( s.equals( NewColumnTypes.ACTION_WORKITEM_INSERT_FACT_FIELD.name() ) ) {
                    presenter.getActiveDecisionTable().newActionWorkItemInsertFact();

                } else if ( s.equals( NewColumnTypes.ACTION_BRL_FRAGMENT.name() ) ) {
                    presenter.getActiveDecisionTable().newActionBRLFragment();

                }
                pop.hide();
            }

        }, new Command() {
            @Override
            public void execute() {
                pop.hide();
            }
        } );

        //If a separator is clicked disable OK button
        choice.addClickHandler( new ClickHandler() {

            public void onClick( ClickEvent event ) {
                int itemIndex = choice.getSelectedIndex();
                if ( itemIndex < 0 ) {
                    return;
                }
                footer.enableOkButton( !choice.getValue( itemIndex ).equals( SECTION_SEPARATOR ) );
            }

        } );

        pop.setTitle( GuidedDecisionTableConstants.INSTANCE.AddNewColumn() );
        pop.addAttribute( GuidedDecisionTableConstants.INSTANCE.TypeOfColumn(),
                          choice );
        pop.addAttribute( "",
                          chkIncludeAdvancedOptions );
        pop.add( footer );
        pop.show();
    }

    @Override
    public void refreshRuleInheritance( final String selectedParentRuleName,
                                        final Collection<String> availableParentRuleNames ) {
        ruleSelector.setRuleName( selectedParentRuleName );
        ruleSelector.setRuleNames( availableParentRuleNames );
    }

    private Widget getAttributesWidget() {
        attributeConfigWidget = new VerticalPanel();
        return attributeConfigWidget;
    }

    @Override
    public void refreshAttributeWidget( final List<AttributeCol52> attributeColumns ) {
        this.attributeConfigWidget.clear();

        if ( attributeColumns == null || attributeColumns.size() == 0 ) {
            disclosurePanelAttributes.setOpen( false );
            return;
        }

        final boolean isEditable = presenter.isActiveDecisionTableEditable();
        for ( AttributeCol52 attributeColumn : attributeColumns ) {
            HorizontalPanel hp = new HorizontalPanel();
            hp.setVerticalAlignment( HasVerticalAlignment.ALIGN_MIDDLE );

            hp.add( new HTML( "&nbsp;&nbsp;&nbsp;&nbsp;" ) );
            if ( isEditable ) {
                hp.add( removeAttribute( attributeColumn ) );
            }

            final SmallLabel label = makeColumnLabel( attributeColumn );
            hp.add( label );

            final AttributeCol52 originalColumn = attributeColumn;
            final Widget defaultValue = DefaultValueWidgetFactory.getDefaultValueWidget( attributeColumn,
                                                                                         !isEditable,
                                                                                         new DefaultValueWidgetFactory.DefaultValueChangedEventHandler() {
                                                                                             @Override
                                                                                             public void onDefaultValueChanged( DefaultValueWidgetFactory.DefaultValueChangedEvent event ) {
                                                                                                 final AttributeCol52 editedColumn = originalColumn.cloneColumn();
                                                                                                 editedColumn.setDefaultValue( event.getEditedDefaultValue() );
                                                                                                 presenter.getActiveDecisionTable().updateColumn( originalColumn,
                                                                                                                                                  editedColumn );
                                                                                             }
                                                                                         } );

            if ( attributeColumn.getAttribute().equals( RuleAttributeWidget.SALIENCE_ATTR ) ) {
                hp.add( new HTML( "&nbsp;&nbsp;" ) );
                final CheckBox chkUseRowNumber = new CheckBox( GuidedDecisionTableConstants.INSTANCE.UseRowNumber() );
                chkUseRowNumber.setValue( attributeColumn.isUseRowNumber() );
                chkUseRowNumber.setEnabled( isEditable );
                hp.add( chkUseRowNumber );

                hp.add( new SmallLabel( "(" ) );
                final CheckBox chkReverseOrder = new CheckBox( GuidedDecisionTableConstants.INSTANCE.ReverseOrder() );
                chkReverseOrder.setValue( attributeColumn.isReverseOrder() );
                chkReverseOrder.setEnabled( attributeColumn.isUseRowNumber() && isEditable );

                chkUseRowNumber.addClickHandler( new ClickHandler() {

                    @Override
                    public void onClick( final ClickEvent event ) {
                        final AttributeCol52 editedColumn = originalColumn.cloneColumn();
                        editedColumn.setUseRowNumber( chkUseRowNumber.getValue() );
                        chkReverseOrder.setEnabled( chkUseRowNumber.getValue() );
                        presenter.getActiveDecisionTable().updateColumn( originalColumn,
                                                                         editedColumn );
                    }
                } );

                chkReverseOrder.addClickHandler( new ClickHandler() {

                    @Override
                    public void onClick( final ClickEvent event ) {
                        final AttributeCol52 editedColumn = originalColumn.cloneColumn();
                        editedColumn.setReverseOrder( chkReverseOrder.getValue() );
                        presenter.getActiveDecisionTable().updateColumn( originalColumn,
                                                                         editedColumn );
                    }
                } );
                hp.add( chkReverseOrder );
                hp.add( new SmallLabel( ")" ) );
            }
            hp.add( new HTML( "&nbsp;&nbsp;" ) );
            hp.add( new SmallLabel( new StringBuilder( GuidedDecisionTableConstants.INSTANCE.DefaultValue() ).append( GuidedDecisionTableConstants.COLON ).toString() ) );
            hp.add( defaultValue );

            final CheckBox chkHideColumn = new CheckBox( new StringBuilder( GuidedDecisionTableConstants.INSTANCE.HideThisColumn() ).append( GuidedDecisionTableConstants.COLON ).toString() );
            chkHideColumn.setValue( attributeColumn.isHideColumn() );
            chkHideColumn.addClickHandler( new ClickHandler() {

                @Override
                public void onClick( final ClickEvent event ) {
                    final AttributeCol52 editedColumn = originalColumn.cloneColumn();
                    editedColumn.setHideColumn( chkHideColumn.getValue() );
                    presenter.getActiveDecisionTable().updateColumn( originalColumn,
                                                                     editedColumn );
                }
            } );
            hp.add( new HTML( "&nbsp;&nbsp;" ) );
            hp.add( chkHideColumn );

            attributeConfigWidget.add( hp );
        }
    }

    private SmallLabel makeColumnLabel( final AttributeCol52 attributeColumn ) {
        SmallLabel label = new SmallLabel( attributeColumn.getAttribute() );
        setColumnLabelStyleWhenHidden( label,
                                       attributeColumn.isHideColumn() );
        return label;
    }

    private Widget removeAttribute( final AttributeCol52 at ) {
        Image image = GuidedDecisionTableImageResources508.INSTANCE.DeleteItemSmall();
        image.setAltText( GuidedDecisionTableConstants.INSTANCE.RemoveThisAttribute() );

        return new ImageButton( image,
                                GuidedDecisionTableConstants.INSTANCE.RemoveThisAttribute(),
                                new ClickHandler() {
                                    public void onClick( ClickEvent w ) {
                                        String ms = GuidedDecisionTableConstants.INSTANCE.DeleteActionColumnWarning( at.getAttribute() );
                                        if ( Window.confirm( ms ) ) {
                                            presenter.getActiveDecisionTable().deleteColumn( at );
                                        }
                                    }
                                } );
    }

    private Widget getMetaDataWidget() {
        metaDataConfigWidget = new VerticalPanel();
        return metaDataConfigWidget;
    }

    @Override
    public void refreshMetaDataWidget( final List<MetadataCol52> metaDataColumns ) {
        this.metaDataConfigWidget.clear();

        if ( metaDataColumns == null || metaDataColumns.size() == 0 ) {
            disclosurePanelMetaData.setOpen( false );
            return;
        }

        final boolean isEditable = presenter.isActiveDecisionTableEditable();
        for ( MetadataCol52 metaDataColumn : metaDataColumns ) {
            HorizontalPanel hp = new HorizontalPanel();
            hp.setVerticalAlignment( HasVerticalAlignment.ALIGN_MIDDLE );
            hp.add( new HTML( "&nbsp;&nbsp;&nbsp;&nbsp;" ) );

            if ( isEditable ) {
                hp.add( removeMetaData( metaDataColumn ) );
            }

            final SmallLabel label = makeColumnLabel( metaDataColumn );
            hp.add( label );

            final MetadataCol52 originalColumn = metaDataColumn;
            final CheckBox chkHideColumn = new CheckBox( new StringBuilder( GuidedDecisionTableConstants.INSTANCE.HideThisColumn() ).append( GuidedDecisionTableConstants.COLON ).toString() );
            chkHideColumn.setValue( metaDataColumn.isHideColumn() );
            chkHideColumn.addClickHandler( new ClickHandler() {

                @Override
                public void onClick( final ClickEvent event ) {
                    final MetadataCol52 editedColumn = originalColumn.cloneColumn();
                    editedColumn.setHideColumn( chkHideColumn.getValue() );
                    presenter.getActiveDecisionTable().updateColumn( originalColumn,
                                                                     editedColumn );
                }
            } );
            hp.add( new HTML( "&nbsp;&nbsp;" ) );
            hp.add( chkHideColumn );

            metaDataConfigWidget.add( hp );
        }
    }

    private SmallLabel makeColumnLabel( final MetadataCol52 metaDataColumn ) {
        SmallLabel label = new SmallLabel( metaDataColumn.getMetadata() );
        setColumnLabelStyleWhenHidden( label,
                                       metaDataColumn.isHideColumn() );
        return label;
    }

    private Widget removeMetaData( final MetadataCol52 md ) {
        Image image = GuidedDecisionTableImageResources508.INSTANCE.DeleteItemSmall();
        image.setAltText( GuidedDecisionTableConstants.INSTANCE.RemoveThisMetadata() );

        return new ImageButton( image,
                                GuidedDecisionTableConstants.INSTANCE.RemoveThisMetadata(),
                                new ClickHandler() {
                                    public void onClick( ClickEvent w ) {
                                        String ms = GuidedDecisionTableConstants.INSTANCE.DeleteActionColumnWarning( md.getMetadata() );
                                        if ( Window.confirm( ms ) ) {
                                            presenter.getActiveDecisionTable().deleteColumn( md );
                                        }
                                    }
                                } );
    }

    private Widget getConditionsWidget() {
        conditionsConfigWidget = new VerticalPanel();
        return conditionsConfigWidget;
    }

    @Override
    public void refreshConditionsWidget( final List<CompositeColumn<? extends BaseColumn>> conditionColumns ) {
        this.conditionsConfigWidget.clear();

        if ( conditionColumns == null || conditionColumns.size() == 0 ) {
            disclosurePanelConditions.setOpen( false );
            return;
        }

        //Each Pattern is a row in a vertical panel
        final VerticalPanel patternsPanel = new VerticalPanel();
        conditionsConfigWidget.add( patternsPanel );

        final boolean isEditable = presenter.isActiveDecisionTableEditable();
        for ( CompositeColumn<?> conditionColumn : conditionColumns ) {
            if ( conditionColumn instanceof Pattern52 ) {
                Pattern52 p = (Pattern52) conditionColumn;
                VerticalPanel patternPanel = new VerticalPanel();
                VerticalPanel conditionsPanel = new VerticalPanel();
                HorizontalPanel patternHeaderPanel = new HorizontalPanel();
                patternHeaderPanel.setStylePrimaryName( GuidedDecisionTableResources.INSTANCE.css().patternSectionHeader() );
                Label patternLabel = makePatternLabel( p );
                patternHeaderPanel.add( patternLabel );
                patternPanel.add( patternHeaderPanel );
                patternPanel.add( conditionsPanel );
                patternsPanel.add( patternPanel );

                List<ConditionCol52> conditions = p.getChildColumns();
                for ( ConditionCol52 c : conditions ) {
                    HorizontalPanel hp = new HorizontalPanel();
                    hp.setStylePrimaryName( GuidedDecisionTableResources.INSTANCE.css().patternConditionSectionHeader() );
                    if ( isEditable ) {
                        hp.add( removeCondition( c ) );
                    }
                    hp.add( editCondition( p,
                                           c ) );
                    SmallLabel conditionLabel = makeColumnLabel( c );
                    hp.add( conditionLabel );
                    conditionsPanel.add( hp );
                }

            } else if ( conditionColumn instanceof BRLConditionColumn ) {
                BRLConditionColumn brl = (BRLConditionColumn) conditionColumn;

                HorizontalPanel patternHeaderPanel = new HorizontalPanel();
                patternHeaderPanel.setStylePrimaryName( GuidedDecisionTableResources.INSTANCE.css().patternSectionHeader() );
                HorizontalPanel patternPanel = new HorizontalPanel();
                if ( isEditable ) {
                    patternPanel.add( removeCondition( brl ) );
                }
                patternPanel.add( editCondition( brl ) );
                Label patternLabel = makePatternLabel( brl );
                patternPanel.add( patternLabel );
                patternHeaderPanel.add( patternPanel );
                patternsPanel.add( patternHeaderPanel );
            }

        }
    }

    private Label makePatternLabel( final Pattern52 p ) {
        StringBuilder patternLabel = new StringBuilder();
        String factType = p.getFactType();
        String boundName = p.getBoundName();
        if ( factType != null && factType.length() > 0 ) {
            if ( p.isNegated() ) {
                patternLabel.append( GuidedDecisionTableConstants.INSTANCE.negatedPattern() ).append( " " ).append( factType );
            } else {
                patternLabel.append( factType ).append( " [" ).append( boundName ).append( "]" );
            }
        }
        return new Label( patternLabel.toString() );
    }

    private Label makePatternLabel( final BRLConditionColumn brl ) {
        StringBuilder sb = new StringBuilder();
        sb.append( brl.getHeader() );
        return new Label( sb.toString() );
    }

    private SmallLabel makeColumnLabel( final ConditionCol52 cc ) {
        StringBuilder sb = new StringBuilder();
        if ( cc.isBound() ) {
            sb.append( cc.getBinding() );
            sb.append( " : " );
        }
        sb.append( cc.getHeader() );
        SmallLabel label = new SmallLabel( sb.toString() );
        if ( cc.isHideColumn() ) {
            label.setStylePrimaryName( GuidedDecisionTableResources.INSTANCE.css().columnLabelHidden() );
        }
        return label;
    }

    private Widget editCondition( final Pattern52 origPattern,
                                  final ConditionCol52 origCol ) {
        Image edit = GuidedDecisionTableImageResources508.INSTANCE.Edit();
        edit.setAltText( GuidedDecisionTableConstants.INSTANCE.EditThisColumnsConfiguration() );
        return new ImageButton( edit,
                                GuidedDecisionTableConstants.INSTANCE.EditThisColumnsConfiguration(),
                                new ClickHandler() {
                                    public void onClick( ClickEvent w ) {
                                        presenter.getActiveDecisionTable().editCondition( origPattern,
                                                                                          origCol );
                                    }
                                } );
    }

    private Widget editCondition( final BRLConditionColumn origCol ) {
        Image edit = GuidedDecisionTableImageResources508.INSTANCE.Edit();
        edit.setAltText( GuidedDecisionTableConstants.INSTANCE.EditThisColumnsConfiguration() );
        return new ImageButton( edit,
                                GuidedDecisionTableConstants.INSTANCE.EditThisColumnsConfiguration(),
                                new ClickHandler() {
                                    public void onClick( ClickEvent w ) {
                                        presenter.getActiveDecisionTable().editCondition( origCol );
                                    }
                                } );
    }

    private Widget removeCondition( final ConditionCol52 c ) {
        Image image = GuidedDecisionTableImageResources508.INSTANCE.DeleteItemSmall();
        image.setAltText( GuidedDecisionTableConstants.INSTANCE.RemoveThisConditionColumn() );
        if ( c instanceof LimitedEntryBRLConditionColumn ) {
            return new ImageButton( image,
                                    GuidedDecisionTableConstants.INSTANCE.RemoveThisConditionColumn(),
                                    new ClickHandler() {
                                        public void onClick( ClickEvent w ) {
                                            if ( !presenter.getActiveDecisionTable().canConditionBeDeleted( (LimitedEntryBRLConditionColumn) c ) ) {
                                                Window.alert( GuidedDecisionTableConstants.INSTANCE.UnableToDeleteConditionColumn0( c.getHeader() ) );
                                                return;
                                            }
                                            String cm = GuidedDecisionTableConstants.INSTANCE.DeleteConditionColumnWarning0( c.getHeader() );
                                            if ( Window.confirm( cm ) ) {
                                                presenter.getActiveDecisionTable().deleteColumn( (LimitedEntryBRLConditionColumn) c );
                                            }
                                        }
                                    } );

        } else if ( c instanceof BRLConditionColumn ) {
            return new ImageButton( image,
                                    GuidedDecisionTableConstants.INSTANCE.RemoveThisConditionColumn(),
                                    new ClickHandler() {
                                        public void onClick( ClickEvent w ) {
                                            if ( !presenter.getActiveDecisionTable().canConditionBeDeleted( (BRLConditionColumn) c ) ) {
                                                Window.alert( GuidedDecisionTableConstants.INSTANCE.UnableToDeleteConditionColumn0( c.getHeader() ) );
                                                return;
                                            }
                                            String cm = GuidedDecisionTableConstants.INSTANCE.DeleteConditionColumnWarning0( c.getHeader() );
                                            if ( Window.confirm( cm ) ) {
                                                presenter.getActiveDecisionTable().deleteColumn( (BRLConditionColumn) c );
                                            }
                                        }
                                    } );

        }
        return new ImageButton( image,
                                GuidedDecisionTableConstants.INSTANCE.RemoveThisConditionColumn(),
                                new ClickHandler() {
                                    public void onClick( ClickEvent w ) {
                                        if ( !presenter.getActiveDecisionTable().canConditionBeDeleted( c ) ) {
                                            Window.alert( GuidedDecisionTableConstants.INSTANCE.UnableToDeleteConditionColumn0( c.getHeader() ) );
                                            return;
                                        }
                                        String cm = GuidedDecisionTableConstants.INSTANCE.DeleteConditionColumnWarning0( c.getHeader() );
                                        if ( Window.confirm( cm ) ) {
                                            presenter.getActiveDecisionTable().deleteColumn( c );
                                        }
                                    }
                                } );
    }

    private Widget getActionsWidget() {
        actionsConfigWidget = new VerticalPanel();
        return actionsConfigWidget;
    }

    @Override
    public void refreshActionsWidget( final List<ActionCol52> actionColumns ) {
        this.actionsConfigWidget.clear();

        if ( actionColumns == null || actionColumns.size() == 0 ) {
            disclosurePanelActions.setOpen( false );
            return;
        }

        //Each Action is a row in a vertical panel
        final VerticalPanel actionsPanel = new VerticalPanel();
        this.actionsConfigWidget.add( actionsPanel );

        //Add Actions to panel
        final boolean isEditable = presenter.isActiveDecisionTableEditable();
        for ( ActionCol52 actionColumn : actionColumns ) {
            HorizontalPanel hp = new HorizontalPanel();
            if ( isEditable ) {
                hp.add( removeAction( actionColumn ) );
            }
            hp.add( editAction( actionColumn ) );
            Label actionLabel = makeColumnLabel( actionColumn );
            hp.add( actionLabel );
            actionsPanel.add( hp );
        }
    }

    private SmallLabel makeColumnLabel( final ActionCol52 actionColumn ) {
        SmallLabel label = new SmallLabel( actionColumn.getHeader() );
        if ( actionColumn.isHideColumn() ) {
            label.setStylePrimaryName( GuidedDecisionTableResources.INSTANCE.css().columnLabelHidden() );
        }
        return label;
    }

    private Widget editAction( final ActionCol52 actionColumn ) {
        Image edit = GuidedDecisionTableImageResources508.INSTANCE.Edit();
        edit.setAltText( GuidedDecisionTableConstants.INSTANCE.EditThisActionColumnConfiguration() );
        return new ImageButton( edit,
                                GuidedDecisionTableConstants.INSTANCE.EditThisActionColumnConfiguration(),
                                new ClickHandler() {
                                    public void onClick( ClickEvent w ) {
                                        presenter.getActiveDecisionTable().editAction( actionColumn );
                                    }
                                } );
    }

    private Widget removeAction( final ActionCol52 actionColumn ) {
        if ( actionColumn instanceof LimitedEntryBRLActionColumn ) {
            Image image = GuidedDecisionTableImageResources508.INSTANCE.DeleteItemSmall();
            image.setAltText( GuidedDecisionTableConstants.INSTANCE.RemoveThisActionColumn() );
            return new ImageButton( image,
                                    GuidedDecisionTableConstants.INSTANCE.RemoveThisActionColumn(),
                                    new ClickHandler() {
                                        public void onClick( ClickEvent w ) {
                                            String cm = GuidedDecisionTableConstants.INSTANCE.DeleteActionColumnWarning( actionColumn.getHeader() );
                                            if ( Window.confirm( cm ) ) {
                                                presenter.getActiveDecisionTable().deleteColumn( (LimitedEntryBRLActionColumn) actionColumn );
                                            }
                                        }
                                    } );

        } else if ( actionColumn instanceof BRLActionColumn ) {
            Image image = GuidedDecisionTableImageResources508.INSTANCE.DeleteItemSmall();
            image.setAltText( GuidedDecisionTableConstants.INSTANCE.RemoveThisActionColumn() );
            return new ImageButton( image,
                                    GuidedDecisionTableConstants.INSTANCE.RemoveThisActionColumn(),
                                    new ClickHandler() {
                                        public void onClick( ClickEvent w ) {
                                            String cm = GuidedDecisionTableConstants.INSTANCE.DeleteActionColumnWarning( actionColumn.getHeader() );
                                            if ( Window.confirm( cm ) ) {
                                                presenter.getActiveDecisionTable().deleteColumn( (BRLActionColumn) actionColumn );
                                            }
                                        }
                                    } );

        }
        Image image = GuidedDecisionTableImageResources508.INSTANCE.DeleteItemSmall();
        image.setAltText( GuidedDecisionTableConstants.INSTANCE.RemoveThisActionColumn() );
        return new ImageButton( image,
                                GuidedDecisionTableConstants.INSTANCE.RemoveThisActionColumn(),
                                new ClickHandler() {
                                    public void onClick( ClickEvent w ) {
                                        String cm = GuidedDecisionTableConstants.INSTANCE.DeleteActionColumnWarning( actionColumn.getHeader() );
                                        if ( Window.confirm( cm ) ) {
                                            presenter.getActiveDecisionTable().deleteColumn( actionColumn );
                                        }
                                    }
                                } );
    }

    private void setColumnLabelStyleWhenHidden( final SmallLabel label,
                                                final boolean isHidden ) {
        if ( isHidden ) {
            label.addStyleName( GuidedDecisionTableResources.INSTANCE.css().columnLabelHidden() );
        } else {
            label.removeStyleName( GuidedDecisionTableResources.INSTANCE.css().columnLabelHidden() );
        }
    }

    @Override
    public void refreshColumnsNote( final boolean hasColumnDefinitions ) {
        configureColumnsNote.setVisible( !hasColumnDefinitions );
    }

    @Override
    public void setZoom( final int zoom ) {
        //Set zoom preserving translation
        final Transform transform = new Transform();
        final double tx = gridPanel.getViewport().getTransform().getTranslateX();
        final double ty = gridPanel.getViewport().getTransform().getTranslateY();
        transform.translate( tx, ty );
        transform.scale( zoom / 100.0 );

        //Ensure the change in zoom keeps the view in bounds. IGridLayer's visibleBounds depends
        //on the Viewport Transformation; so set it to the "proposed" transformation before checking.
        gridPanel.getViewport().setTransform( transform );
        final IRestriction restriction = mousePanMediator.getRestriction();
        final Transform newTransform = restriction.adjust( transform,
                                                           gridLayer.getVisibleBounds() );
        gridPanel.getViewport().setTransform( newTransform );
        gridPanel.getViewport().batch();
    }

    @Override
    public void onInsertColumn() {
        doNewColumn();
    }

    @Override
    public GridLayer getGridLayerView() {
        return gridLayer;
    }

    @Override
    public IBounds getBounds() {
        return bounds;
    }

    @Override
    public void select( final IBaseGridWidget selectedGridWidget ) {
        gridLayer.select( selectedGridWidget );
    }

    @Override
    public void selectLinkedColumn( final IGridColumn<?> link ) {
        gridLayer.selectLinkedColumn( link );
    }

    @Override
    public Set<IBaseGridWidget> getGridWidgets() {
        return gridLayer.getGridWidgets();
    }
}
