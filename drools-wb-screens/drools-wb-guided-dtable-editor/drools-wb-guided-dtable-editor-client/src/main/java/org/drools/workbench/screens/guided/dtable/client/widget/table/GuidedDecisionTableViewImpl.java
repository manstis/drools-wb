/*
 * Copyright 2015 JBoss Inc
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

import java.util.Set;
import javax.enterprise.event.Event;

import com.ait.lienzo.client.core.types.Point2D;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import org.drools.workbench.models.datamodel.workitems.PortableWorkDefinition;
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
import org.drools.workbench.models.guided.dtable.shared.model.ConditionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.DTCellValue52;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.models.guided.dtable.shared.model.LimitedEntryActionInsertFactCol52;
import org.drools.workbench.models.guided.dtable.shared.model.LimitedEntryActionRetractFactCol52;
import org.drools.workbench.models.guided.dtable.shared.model.LimitedEntryActionSetFieldCol52;
import org.drools.workbench.models.guided.dtable.shared.model.LimitedEntryBRLActionColumn;
import org.drools.workbench.models.guided.dtable.shared.model.LimitedEntryBRLConditionColumn;
import org.drools.workbench.models.guided.dtable.shared.model.LimitedEntryConditionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.MetadataCol52;
import org.drools.workbench.models.guided.dtable.shared.model.Pattern52;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableConstants;
import org.drools.workbench.screens.guided.dtable.client.resources.images.GuidedDecisionTableImageResources508;
import org.drools.workbench.screens.guided.dtable.client.widget.ActionColumnCommand;
import org.drools.workbench.screens.guided.dtable.client.widget.ActionInsertFactPopup;
import org.drools.workbench.screens.guided.dtable.client.widget.ActionRetractFactPopup;
import org.drools.workbench.screens.guided.dtable.client.widget.ActionSetFieldPopup;
import org.drools.workbench.screens.guided.dtable.client.widget.ActionWorkItemInsertFactPopup;
import org.drools.workbench.screens.guided.dtable.client.widget.ActionWorkItemPopup;
import org.drools.workbench.screens.guided.dtable.client.widget.ActionWorkItemSetFieldPopup;
import org.drools.workbench.screens.guided.dtable.client.widget.BRLActionColumnViewImpl;
import org.drools.workbench.screens.guided.dtable.client.widget.BRLConditionColumnViewImpl;
import org.drools.workbench.screens.guided.dtable.client.widget.ConditionColumnCommand;
import org.drools.workbench.screens.guided.dtable.client.widget.ConditionPopup;
import org.drools.workbench.screens.guided.dtable.client.widget.LimitedEntryBRLActionColumnViewImpl;
import org.drools.workbench.screens.guided.dtable.client.widget.LimitedEntryBRLConditionColumnViewImpl;
import org.drools.workbench.screens.guided.rule.client.editor.RuleAttributeWidget;
import org.kie.workbench.common.widgets.client.datamodel.AsyncPackageDataModelOracle;
import org.uberfire.ext.widgets.common.client.common.BusyPopup;
import org.uberfire.ext.widgets.common.client.common.DirtyableHorizontalPane;
import org.uberfire.ext.widgets.common.client.common.popups.FormStylePopup;
import org.uberfire.ext.wires.core.grids.client.model.IGridData;
import org.uberfire.ext.wires.core.grids.client.widget.BaseGridWidget;
import org.uberfire.ext.wires.core.grids.client.widget.renderers.grids.IGridRenderer;
import org.uberfire.workbench.events.NotificationEvent;

public class GuidedDecisionTableViewImpl extends BaseGridWidget implements GuidedDecisionTableView {

    private final GuidedDecisionTableView.Presenter presenter;
    private final GuidedDecisionTable52 model;
    private final AsyncPackageDataModelOracle oracle;
    private final Set<PortableWorkDefinition> workItemDefinitions;
    private final Event<NotificationEvent> notificationEvent;
    private final EventBus eventBus;
    private final GuidedDecisionTablePresenter.Access access;

    public GuidedDecisionTableViewImpl( final IGridData uiModel,
                                        final IGridRenderer renderer,
                                        final GuidedDecisionTableView.Presenter presenter,
                                        final GuidedDecisionTable52 model,
                                        final AsyncPackageDataModelOracle oracle,
                                        final Set<PortableWorkDefinition> workItemDefinitions,
                                        final Event<NotificationEvent> notificationEvent,
                                        final EventBus eventBus,
                                        final GuidedDecisionTablePresenter.Access access ) {
        super( uiModel,
               presenter,
               presenter,
               renderer );
        this.presenter = presenter;
        this.model = model;
        this.oracle = oracle;
        this.workItemDefinitions = workItemDefinitions;
        this.notificationEvent = notificationEvent;
        this.eventBus = eventBus;
        this.access = access;
    }

    @Override
    public void setLocation( final double x,
                             final double y ) {
        setLocation( new Point2D( x,
                                  y ) );
    }

    @Override
    public void newAttributeOrMetaDataColumn() {
        // show choice of attributes
        final Image image = GuidedDecisionTableImageResources508.INSTANCE.Config();
        final FormStylePopup pop = new FormStylePopup( image,
                                                       GuidedDecisionTableConstants.INSTANCE.AddAnOptionToTheRule() );
        final ListBox list = RuleAttributeWidget.getAttributeList();

        //This attribute is only used for Decision Tables
        list.addItem( GuidedDecisionTable52.NEGATE_RULE_ATTR );

        // Remove any attributes already added
        final Set<String> existingAttributeNames = presenter.getExistingAttributeNames();
        for ( String existingAttributeName : existingAttributeNames ) {
            for ( int iItem = 0; iItem < list.getItemCount(); iItem++ ) {
                if ( list.getItemText( iItem ).equals( existingAttributeName ) ) {
                    list.removeItem( iItem );
                    break;
                }
            }
        }

        //Selection of an Attribute adds it
        list.setSelectedIndex( 0 );
        list.addChangeHandler( new ChangeHandler() {
            public void onChange( ChangeEvent event ) {
                final String attributeName = list.getItemText( list.getSelectedIndex() );
                final AttributeCol52 column = new AttributeCol52();
                column.setAttribute( attributeName );
                presenter.appendColumn( column );
                pop.hide();
            }
        } );

        //You have to click "add" to add MetaData.. inconsistent for sure!
        final TextBox box = new TextBox();
        box.setVisibleLength( 15 );

        final Image addButton = GuidedDecisionTableImageResources508.INSTANCE.NewItem();
        addButton.setTitle( GuidedDecisionTableConstants.INSTANCE.AddMetadataToTheRule() );
        addButton.addClickHandler( new ClickHandler() {
            public void onClick( ClickEvent w ) {

                final String metaDataName = box.getText();
                if ( !presenter.isMetaDataUnique( metaDataName ) ) {
                    Window.alert( GuidedDecisionTableConstants.INSTANCE.ThatColumnNameIsAlreadyInUsePleasePickAnother() );
                    return;
                }
                final MetadataCol52 column = new MetadataCol52();
                column.setMetadata( metaDataName );
                column.setHideColumn( true );
                presenter.appendColumn( column );
                pop.hide();
            }

        } );
        DirtyableHorizontalPane horiz = new DirtyableHorizontalPane();
        horiz.add( box );
        horiz.add( addButton );

        pop.addAttribute( new StringBuilder( GuidedDecisionTableConstants.INSTANCE.Metadata1() )
                                  .append( GuidedDecisionTableConstants.COLON ).toString(), horiz );
        pop.addAttribute( GuidedDecisionTableConstants.INSTANCE.Attribute(),
                          list );
        pop.show();
    }

    @Override
    public void newExtendedEntryConditionColumn() {
        final ConditionCol52 column = new ConditionCol52();
        final ConditionPopup popup = new ConditionPopup( model,
                                                         oracle,
                                                         presenter,
                                                         new ConditionColumnCommand() {
                                                             public void execute( final Pattern52 pattern,
                                                                                  final ConditionCol52 column ) {
                                                                 presenter.appendColumn( pattern,
                                                                                         column );
                                                             }
                                                         },
                                                         column,
                                                         true,
                                                         !access.isEditable() );
        popup.show();
    }

    @Override
    public void newLimitedEntryConditionColumn() {
        final ConditionCol52 column = new LimitedEntryConditionCol52();
        final ConditionPopup popup = new ConditionPopup( model,
                                                         oracle,
                                                         presenter,
                                                         new ConditionColumnCommand() {
                                                             public void execute( final Pattern52 pattern,
                                                                                  final ConditionCol52 column ) {
                                                                 presenter.appendColumn( pattern,
                                                                                         column );
                                                             }
                                                         },
                                                         column,
                                                         true,
                                                         !access.isEditable() );
        popup.show();
    }

    @Override
    public void newExtendedEntryConditionBRLFragment() {
        final BRLConditionColumn column = new BRLConditionColumn();
        final BRLConditionColumnViewImpl popup = new BRLConditionColumnViewImpl( model,
                                                                                 oracle,
                                                                                 presenter,
                                                                                 eventBus,
                                                                                 column,
                                                                                 true,
                                                                                 !access.isEditable() );
        popup.show();
    }

    @Override
    public void newLimitedEntryConditionBRLFragment() {
        final LimitedEntryBRLConditionColumn column = new LimitedEntryBRLConditionColumn();
        final LimitedEntryBRLConditionColumnViewImpl popup = new LimitedEntryBRLConditionColumnViewImpl( model,
                                                                                                         oracle,
                                                                                                         presenter,
                                                                                                         eventBus,
                                                                                                         column,
                                                                                                         true,
                                                                                                         !access.isEditable() );
        popup.show();
    }

    @Override
    public void newExtendedEntryActionInsertColumn() {
        final ActionInsertFactCol52 column = new ActionInsertFactCol52();
        final ActionInsertFactPopup popup = new ActionInsertFactPopup( model,
                                                                       oracle,
                                                                       presenter,
                                                                       new ActionColumnCommand() {
                                                                           public void execute( final ActionCol52 column ) {
                                                                               presenter.appendColumn( column );
                                                                           }
                                                                       },
                                                                       column,
                                                                       true,
                                                                       !access.isEditable() );
        popup.show();
    }

    @Override
    public void newLimitedEntryActionInsertColumn() {
        final LimitedEntryActionInsertFactCol52 column = new LimitedEntryActionInsertFactCol52();
        final ActionInsertFactPopup popup = new ActionInsertFactPopup( model,
                                                                       oracle,
                                                                       presenter,
                                                                       new ActionColumnCommand() {
                                                                           public void execute( final ActionCol52 column ) {
                                                                               presenter.appendColumn( column );
                                                                           }
                                                                       },
                                                                       column,
                                                                       true,
                                                                       !access.isEditable() );
        popup.show();
    }

    @Override
    public void newExtendedEntryActionSetColumn() {
        final ActionSetFieldCol52 column = new ActionSetFieldCol52();
        final ActionSetFieldPopup popup = new ActionSetFieldPopup( model,
                                                                   oracle,
                                                                   presenter,
                                                                   new ActionColumnCommand() {
                                                                       public void execute( final ActionCol52 column ) {
                                                                           presenter.appendColumn( column );
                                                                       }
                                                                   },
                                                                   column,
                                                                   true,
                                                                   !access.isEditable() );
        popup.show();
    }

    @Override
    public void newLimitedEntryActionSetColumn() {
        final LimitedEntryActionSetFieldCol52 column = new LimitedEntryActionSetFieldCol52();
        final ActionSetFieldPopup popup = new ActionSetFieldPopup( model,
                                                                   oracle,
                                                                   presenter,
                                                                   new ActionColumnCommand() {
                                                                       public void execute( final ActionCol52 column ) {
                                                                           presenter.appendColumn( column );
                                                                       }
                                                                   },
                                                                   column,
                                                                   true,
                                                                   !access.isEditable() );
        popup.show();
    }

    @Override
    public void newExtendedEntryActionRetractFact() {
        final ActionRetractFactCol52 column = new ActionRetractFactCol52();
        final ActionRetractFactPopup popup = new ActionRetractFactPopup( model,
                                                                         presenter,
                                                                         new ActionColumnCommand() {
                                                                             public void execute( final ActionCol52 column ) {
                                                                                 presenter.appendColumn( column );
                                                                             }
                                                                         },
                                                                         column,
                                                                         true,
                                                                         !access.isEditable() );
        popup.show();
    }

    @Override
    public void newLimitedEntryActionRetractFact() {
        final LimitedEntryActionRetractFactCol52 column = new LimitedEntryActionRetractFactCol52();
        column.setValue( new DTCellValue52( "" ) );
        final ActionRetractFactPopup popup = new ActionRetractFactPopup( model,
                                                                         presenter,
                                                                         new ActionColumnCommand() {
                                                                             public void execute( final ActionCol52 column ) {
                                                                                 presenter.appendColumn( column );
                                                                             }
                                                                         },
                                                                         column,
                                                                         true,
                                                                         !access.isEditable() );
        popup.show();
    }

    @Override
    public void newActionWorkItem() {
        //WorkItems are defined within the column and always boolean (i.e. Limited Entry) in the table
        final ActionWorkItemCol52 column = new ActionWorkItemCol52();
        final ActionWorkItemPopup popup = new ActionWorkItemPopup( model,
                                                                   presenter,
                                                                   new ActionColumnCommand() {
                                                                       public void execute( final ActionCol52 column ) {
                                                                           presenter.appendColumn( column );
                                                                       }
                                                                   },
                                                                   column,
                                                                   workItemDefinitions,
                                                                   true,
                                                                   !access.isEditable() );
        popup.show();
    }

    @Override
    public void newActionWorkItemSetField() {
        //Actions setting Field Values from Work Item Result Parameters are always boolean (i.e. Limited Entry) in the table
        final ActionWorkItemSetFieldCol52 column = new ActionWorkItemSetFieldCol52();
        final ActionWorkItemSetFieldPopup popup = new ActionWorkItemSetFieldPopup( model,
                                                                                   oracle,
                                                                                   presenter,
                                                                                   new ActionColumnCommand() {
                                                                                       public void execute( final ActionCol52 column ) {
                                                                                           presenter.appendColumn( column );
                                                                                       }
                                                                                   },
                                                                                   column,
                                                                                   true,
                                                                                   !access.isEditable() );
        popup.show();
    }

    @Override
    public void newActionWorkItemInsertFact() {
        //Actions setting Field Values from Work Item Result Parameters are always boolean (i.e. Limited Entry) in the table
        final ActionWorkItemInsertFactCol52 column = new ActionWorkItemInsertFactCol52();
        final ActionWorkItemInsertFactPopup popup = new ActionWorkItemInsertFactPopup( model,
                                                                                       oracle,
                                                                                       presenter,
                                                                                       new ActionColumnCommand() {
                                                                                           public void execute( final ActionCol52 column ) {
                                                                                               presenter.appendColumn( column );
                                                                                           }
                                                                                       },
                                                                                       column,
                                                                                       true,
                                                                                       !access.isEditable() );
        popup.show();
    }

    @Override
    public void newExtendedEntryActionBRLFragment() {
        final BRLActionColumn column = new BRLActionColumn();
        final BRLActionColumnViewImpl popup = new BRLActionColumnViewImpl( model,
                                                                           oracle,
                                                                           presenter,
                                                                           eventBus,
                                                                           column,
                                                                           true,
                                                                           !access.isEditable() );
        popup.show();
    }

    @Override
    public void newLimitedEntryActionBRLFragment() {
        final LimitedEntryBRLActionColumn column = new LimitedEntryBRLActionColumn();
        final LimitedEntryBRLActionColumnViewImpl popup = new LimitedEntryBRLActionColumnViewImpl( model,
                                                                                                   oracle,
                                                                                                   presenter,
                                                                                                   eventBus,
                                                                                                   column,
                                                                                                   true,
                                                                                                   !access.isEditable() );
        popup.show();
    }

    public void editCondition( final Pattern52 pattern,
                               final ConditionCol52 column ) {
        final ConditionPopup popup = new ConditionPopup( model,
                                                         oracle,
                                                         presenter,
                                                         new ConditionColumnCommand() {
                                                             public void execute( final Pattern52 newPattern,
                                                                                  final ConditionCol52 newColumn ) {
                                                                 if ( !access.isEditable() ) {
                                                                     return;
                                                                 }
                                                                 presenter.updateColumn( pattern,
                                                                                         column,
                                                                                         newPattern,
                                                                                         newColumn );
                                                             }
                                                         },
                                                         pattern,
                                                         column,
                                                         false,
                                                         !access.isEditable() );
        popup.show();
    }

    @Override
    public void editExtendedEntryConditionBRLFragment( final BRLConditionColumn column ) {
        final BRLConditionColumnViewImpl popup = new BRLConditionColumnViewImpl( model,
                                                                                 oracle,
                                                                                 presenter,
                                                                                 eventBus,
                                                                                 column,
                                                                                 false,
                                                                                 !access.isEditable() );
        popup.show();
    }

    @Override
    public void editLimitedEntryConditionBRLFragment( final LimitedEntryBRLConditionColumn column ) {
        final LimitedEntryBRLConditionColumnViewImpl popup = new LimitedEntryBRLConditionColumnViewImpl( model,
                                                                                                         oracle,
                                                                                                         presenter,
                                                                                                         eventBus,
                                                                                                         column,
                                                                                                         false,
                                                                                                         !access.isEditable() );
        popup.show();
    }

    @Override
    public void editActionInsertFact( final ActionInsertFactCol52 column ) {
        final ActionInsertFactPopup ed = new ActionInsertFactPopup( model,
                                                                    oracle,
                                                                    presenter,
                                                                    new ActionColumnCommand() {
                                                                        public void execute( final ActionCol52 newColumn ) {
                                                                            if ( !access.isEditable() ) {
                                                                                return;
                                                                            }
                                                                            presenter.updateColumn( column,
                                                                                                    (ActionInsertFactCol52) newColumn );
                                                                        }
                                                                    },
                                                                    column,
                                                                    false,
                                                                    !access.isEditable() );
        ed.show();
    }

    @Override
    public void editActionSetField( final ActionSetFieldCol52 column ) {
        final ActionSetFieldPopup ed = new ActionSetFieldPopup( model,
                                                                oracle,
                                                                presenter,
                                                                new ActionColumnCommand() {
                                                                    public void execute( final ActionCol52 newColumn ) {
                                                                        if ( !access.isEditable() ) {
                                                                            return;
                                                                        }
                                                                        presenter.updateColumn( column,
                                                                                                (ActionSetFieldCol52) newColumn );
                                                                    }
                                                                },
                                                                column,
                                                                false,
                                                                !access.isEditable() );
        ed.show();
    }

    @Override
    public void editActionRetractFact( final ActionRetractFactCol52 column ) {
        final ActionRetractFactPopup ed = new ActionRetractFactPopup( model,
                                                                      presenter,
                                                                      new ActionColumnCommand() {
                                                                          public void execute( final ActionCol52 newColumn ) {
                                                                              if ( !access.isEditable() ) {
                                                                                  return;
                                                                              }
                                                                              presenter.updateColumn( column,
                                                                                                      (ActionRetractFactCol52) newColumn );
                                                                          }
                                                                      },
                                                                      column,
                                                                      false,
                                                                      !access.isEditable() );
        ed.show();
    }

    @Override
    public void editActionWorkItemInsertFact( final ActionWorkItemInsertFactCol52 column ) {
        final ActionWorkItemInsertFactPopup ed = new ActionWorkItemInsertFactPopup( model,
                                                                                    oracle,
                                                                                    presenter,
                                                                                    new ActionColumnCommand() {
                                                                                        public void execute( final ActionCol52 newColumn ) {
                                                                                            if ( !access.isEditable() ) {
                                                                                                return;
                                                                                            }
                                                                                            presenter.updateColumn( column,
                                                                                                                    (ActionWorkItemInsertFactCol52) newColumn );
                                                                                        }
                                                                                    },
                                                                                    column,
                                                                                    false,
                                                                                    !access.isEditable() );
        ed.show();
    }

    @Override
    public void editActionWorkItemSetField( final ActionWorkItemSetFieldCol52 column ) {
        final ActionWorkItemSetFieldPopup ed = new ActionWorkItemSetFieldPopup( model,
                                                                                oracle,
                                                                                presenter,
                                                                                new ActionColumnCommand() {
                                                                                    public void execute( final ActionCol52 newColumn ) {
                                                                                        if ( !access.isEditable() ) {
                                                                                            return;
                                                                                        }
                                                                                        presenter.updateColumn( column,
                                                                                                                (ActionWorkItemSetFieldCol52) newColumn );
                                                                                    }
                                                                                },
                                                                                column,
                                                                                false,
                                                                                !access.isEditable() );
        ed.show();
    }

    @Override
    public void editActionWorkItem( final ActionWorkItemCol52 column ) {
        final ActionWorkItemPopup popup = new ActionWorkItemPopup( model,
                                                                   presenter,
                                                                   new ActionColumnCommand() {
                                                                       public void execute( final ActionCol52 newColumn ) {
                                                                           if ( !access.isEditable() ) {
                                                                               return;
                                                                           }
                                                                           presenter.updateColumn( column,
                                                                                                   (ActionWorkItemCol52) newColumn );
                                                                       }
                                                                   },
                                                                   column,
                                                                   workItemDefinitions,
                                                                   false,
                                                                   !access.isEditable() );
        popup.show();
    }

    @Override
    public void editExtendedEntryActionBRLFragment( final BRLActionColumn column ) {
        final BRLActionColumnViewImpl popup = new BRLActionColumnViewImpl( model,
                                                                           oracle,
                                                                           presenter,
                                                                           eventBus,
                                                                           column,
                                                                           false,
                                                                           !access.isEditable() );
        popup.show();
    }

    @Override
    public void editLimitedEntryActionBRLFragment( final LimitedEntryBRLActionColumn column ) {
        final LimitedEntryBRLActionColumnViewImpl popup = new LimitedEntryBRLActionColumnViewImpl( model,
                                                                                                   oracle,
                                                                                                   presenter,
                                                                                                   eventBus,
                                                                                                   column,
                                                                                                   false,
                                                                                                   !access.isEditable() );
        popup.show();
    }

    @Override
    public void showDataCutNotificationEvent() {
        notificationEvent.fire( new NotificationEvent( GuidedDecisionTableConstants.INSTANCE.DataCutToClipboardMessage() ) );
    }

    @Override
    public void showDataCopiedNotificationEvent() {
        notificationEvent.fire( new NotificationEvent( GuidedDecisionTableConstants.INSTANCE.DataCopiedToClipboardMessage() ) );
    }

    @Override
    public void showBusyIndicator( final String message ) {
        BusyPopup.showMessage( message );
    }

    @Override
    public void hideBusyIndicator() {
        BusyPopup.close();
    }

}
