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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import org.drools.workbench.models.datamodel.rule.RuleModel;
import org.drools.workbench.models.guided.dtable.shared.model.BRLColumn;
import org.drools.workbench.models.guided.dtable.shared.model.BaseColumn;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableConstants;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.rule.client.editor.ModellerWidgetFactory;
import org.drools.workbench.screens.guided.rule.client.editor.RuleModelEditor;
import org.drools.workbench.screens.guided.rule.client.editor.RuleModeller;
import org.drools.workbench.screens.guided.rule.client.editor.RuleModellerConfiguration;
import org.drools.workbench.screens.guided.rule.client.editor.RuleModellerWidgetFactory;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.TextBox;
import org.kie.workbench.common.widgets.client.datamodel.AsyncPackageDataModelOracle;
import org.uberfire.ext.widgets.common.client.common.popups.BaseModal;
import org.uberfire.ext.widgets.common.client.common.popups.footers.ModalFooterOKCancelButtons;
import org.uberfire.mvp.ParameterizedCommand;

/**
 * An editor for Limited Entry BRL Column definitions
 */
public abstract class AbstractLimitedEntryBRLColumnViewImpl<T, C extends BaseColumn> extends BaseModal
        implements
        RuleModelEditor {

    protected int MIN_WIDTH = 500;

    @UiField(provided = true)
    RuleModeller ruleModeller;

    @UiField
    TextBox txtColumnHeader;

    @UiField
    CheckBox chkHideColumn;

    @UiField
    ScrollPanel brlEditorContainer;

    @SuppressWarnings("rawtypes")
    interface AbstractLimitedEntryBRLColumnEditorBinder
            extends
            UiBinder<Widget, AbstractLimitedEntryBRLColumnViewImpl> {

    }

    private static AbstractLimitedEntryBRLColumnEditorBinder uiBinder = GWT.create( AbstractLimitedEntryBRLColumnEditorBinder.class );

    //TODO {manstis} Popups need to MVP'ed
    protected final GuidedDecisionTable52 model;
    protected GuidedDecisionTableView.Presenter presenter;

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

    protected final BRLColumn<T, C> editingCol;
    protected final BRLColumn<T, C> originalCol;
    protected final RuleModel ruleModel;
    protected final EventBus eventBus;
    protected final boolean isNew;

    public AbstractLimitedEntryBRLColumnViewImpl( final GuidedDecisionTable52 model,
                                                  final AsyncPackageDataModelOracle oracle,
                                                  final GuidedDecisionTableView.Presenter presenter,
                                                  final EventBus eventBus,
                                                  final BRLColumn<T, C> column,
                                                  final boolean isNew,
                                                  final boolean isReadOnly ) {
        this.model = model;
        this.presenter = presenter;
        this.eventBus = eventBus;
        this.originalCol = column;
        this.isNew = isNew;
        this.editingCol = cloneBRLColumn( column );
        this.ruleModel = getRuleModel( editingCol );

        //Limited Entry decision tables do not permit field values to be defined with Template Keys
        final ModellerWidgetFactory widgetFactory = new RuleModellerWidgetFactory();

        this.ruleModeller = new RuleModeller( ruleModel,
                                              oracle,
                                              widgetFactory,
                                              getRuleModellerConfiguration(),
                                              eventBus,
                                              isReadOnly );

        setWidth( getPopupWidth() + "px" );
        setBody( uiBinder.createAndBindUi( this ) );
        add( footer );

        presenter.getPackageParentRuleNames( new ParameterizedCommand<Collection<String>>() {
            @Override
            public void execute( final Collection<String> ruleNames ) {
                ruleModeller.setRuleNamesForPackage( ruleNames );
            }
        } );

        this.brlEditorContainer.setHeight( "100%" );
        this.brlEditorContainer.setWidth( "100%" );
        this.txtColumnHeader.setText( editingCol.getHeader() );
        this.chkHideColumn.setValue( editingCol.isHideColumn() );
        this.footer.enableOkButton( !isReadOnly );
    }

    protected abstract boolean isHeaderUnique( String header );

    protected abstract RuleModel getRuleModel( BRLColumn<T, C> column );

    protected abstract RuleModellerConfiguration getRuleModellerConfiguration();

    protected abstract void doInsertColumn();

    protected abstract void doUpdateColumn();

    protected abstract BRLColumn<T, C> cloneBRLColumn( BRLColumn<T, C> col );

    protected abstract boolean isDefined();

    public RuleModeller getRuleModeller() {
        return this.ruleModeller;
    }

    /**
     * Width of pop-up, 50% of the client width or MIN_WIDTH
     * @return
     */
    private int getPopupWidth() {
        int w = (int) ( Window.getClientWidth() * 0.5 );
        if ( w < MIN_WIDTH ) {
            w = MIN_WIDTH;
        }
        return w;
    }

    @UiHandler("txtColumnHeader")
    void columnHanderChangeHandler( ChangeEvent event ) {
        editingCol.setHeader( txtColumnHeader.getText() );
    }

    @UiHandler("chkHideColumn")
    void hideColumnClickHandler( ClickEvent event ) {
        editingCol.setHideColumn( chkHideColumn.getValue() );
    }

    private void applyChanges() {

        //Validation
        if ( null == editingCol.getHeader() || "".equals( editingCol.getHeader() ) ) {
            Window.alert( GuidedDecisionTableConstants.INSTANCE.YouMustEnterAColumnHeaderValueDescription() );
            return;
        }
        if ( isNew ) {
            if ( !isHeaderUnique( editingCol.getHeader() ) ) {
                Window.alert( GuidedDecisionTableConstants.INSTANCE.ThatColumnNameIsAlreadyInUsePleasePickAnother() );
                return;
            }
            if ( isDefined() ) {
                doInsertColumn();
            } else {
                Window.alert( GuidedDecisionTableConstants.INSTANCE.DecisionTableBRLFragmentNothingDefined() );
                return;
            }

        } else {
            if ( !originalCol.getHeader().equals( editingCol.getHeader() ) ) {
                if ( !isHeaderUnique( editingCol.getHeader() ) ) {
                    Window.alert( GuidedDecisionTableConstants.INSTANCE.ThatColumnNameIsAlreadyInUsePleasePickAnother() );
                    return;
                }
            }
            if ( isDefined() ) {
                doUpdateColumn();
            } else {
                Window.alert( GuidedDecisionTableConstants.INSTANCE.DecisionTableBRLFragmentNothingDefined() );
                return;
            }

        }

        hide();
    }

}
