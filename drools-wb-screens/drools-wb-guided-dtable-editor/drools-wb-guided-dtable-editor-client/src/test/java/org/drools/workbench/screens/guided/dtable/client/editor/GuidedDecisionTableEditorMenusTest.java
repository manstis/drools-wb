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

package org.drools.workbench.screens.guided.dtable.client.editor;

import java.lang.reflect.Field;
import javax.enterprise.event.Event;

import com.google.gwtmockito.GwtMockitoTestRunner;
import org.drools.workbench.screens.guided.dtable.client.editor.clipboard.Clipboard;
import org.drools.workbench.screens.guided.dtable.client.editor.menu.EditMenuBuilder;
import org.drools.workbench.screens.guided.dtable.client.editor.menu.EditMenuView;
import org.drools.workbench.screens.guided.dtable.client.editor.menu.InsertMenuBuilder;
import org.drools.workbench.screens.guided.dtable.client.editor.menu.InsertMenuView;
import org.drools.workbench.screens.guided.dtable.client.editor.menu.RadarMenuBuilder;
import org.drools.workbench.screens.guided.dtable.client.editor.menu.RadarMenuView;
import org.drools.workbench.screens.guided.dtable.client.editor.menu.ViewMenuBuilder;
import org.drools.workbench.screens.guided.dtable.client.editor.menu.ViewMenuView;
import org.drools.workbench.screens.guided.dtable.client.type.GuidedDTableResourceType;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableModellerView;
import org.drools.workbench.screens.guided.dtable.client.widget.table.events.cdi.DecisionTableSelectedEvent;
import org.drools.workbench.screens.guided.dtable.service.GuidedDecisionTableEditorService;
import org.guvnor.common.services.project.context.ProjectContext;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.jboss.errai.ioc.client.container.SyncBeanManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.widgets.client.menu.FileMenuBuilder;
import org.kie.workbench.common.widgets.client.menu.FileMenuBuilderImpl;
import org.kie.workbench.common.widgets.configresource.client.widget.bound.ImportsWidgetPresenter;
import org.kie.workbench.common.widgets.metadata.client.KieMultipleDocumentEditorWrapperView;
import org.kie.workbench.common.widgets.metadata.client.menu.RegisteredDocumentsMenuBuilder;
import org.kie.workbench.common.widgets.metadata.client.menu.RegisteredDocumentsMenuView;
import org.kie.workbench.common.widgets.metadata.client.menu.RegisteredDocumentsMenuView.DocumentMenuItem;
import org.kie.workbench.common.widgets.metadata.client.widget.OverviewWidgetPresenter;
import org.mockito.Mock;
import org.mockito.Spy;
import org.uberfire.client.mvp.PlaceManager;
import org.uberfire.client.workbench.events.ChangeTitleWidgetEvent;
import org.uberfire.ext.editor.commons.client.file.popups.CopyPopUpPresenter;
import org.uberfire.ext.editor.commons.client.file.popups.DeletePopUpPresenter;
import org.uberfire.ext.editor.commons.client.file.popups.RenamePopUpPresenter;
import org.uberfire.ext.editor.commons.client.file.popups.SavePopUpPresenter;
import org.uberfire.ext.editor.commons.client.history.VersionRecordManager;
import org.uberfire.ext.editor.commons.client.menu.BasicFileMenuBuilder;
import org.uberfire.ext.editor.commons.client.menu.BasicFileMenuBuilderImpl;
import org.uberfire.ext.editor.commons.client.menu.RestoreVersionCommandProvider;
import org.uberfire.ext.editor.commons.client.validation.DefaultFileNameValidator;
import org.uberfire.ext.editor.commons.version.VersionService;
import org.uberfire.ext.editor.commons.version.events.RestoreEvent;
import org.uberfire.ext.widgets.common.client.common.BusyIndicatorView;
import org.uberfire.mocks.CallerMock;
import org.uberfire.mocks.EventSourceMock;
import org.uberfire.mvp.Command;
import org.uberfire.workbench.events.NotificationEvent;
import org.uberfire.workbench.model.menu.MenuCustom;
import org.uberfire.workbench.model.menu.MenuGroup;
import org.uberfire.workbench.model.menu.MenuItem;
import org.uberfire.workbench.model.menu.MenuItemCommand;
import org.uberfire.workbench.model.menu.MenuItemPerspective;
import org.uberfire.workbench.model.menu.MenuItemPlain;
import org.uberfire.workbench.model.menu.MenuVisitor;
import org.uberfire.workbench.model.menu.Menus;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class GuidedDecisionTableEditorMenusTest {

    @Mock
    protected BaseGuidedDecisionTableEditorPresenter.View view;

    @Mock
    protected GuidedDecisionTableEditorService dtService;
    protected Caller<GuidedDecisionTableEditorService> dtServiceCaller;

    protected Event<NotificationEvent> notification = spy(new EventSourceMock<NotificationEvent>() {
        @Override
        public void fire(final NotificationEvent event) {
            //Do nothing
        }
    });

    protected Event<DecisionTableSelectedEvent> decisionTableSelectedEvent = spy(new EventSourceMock<DecisionTableSelectedEvent>() {
        @Override
        public void fire(final DecisionTableSelectedEvent event) {
            //Do nothing
        }
    });

    @Mock
    protected Clipboard clipboard;

    @Mock
    protected EditMenuView editMenuView;
    protected EditMenuBuilder editMenuBuilder = new EditMenuBuilder(editMenuView,
                                                                    clipboard);

    @Mock
    protected ViewMenuView viewMenuView;
    protected ViewMenuBuilder viewMenuBuilder = new ViewMenuBuilder(viewMenuView);

    @Mock
    protected InsertMenuView insertMenuView;
    protected InsertMenuBuilder insertMenuBuilder = new InsertMenuBuilder(insertMenuView);

    @Mock
    protected RadarMenuView radarMenuView;
    protected RadarMenuBuilder radarMenuBuilder = new RadarMenuBuilder(radarMenuView);

    @Mock
    protected RegisteredDocumentsMenuView registeredDocumentsMenuView;

    @Mock
    protected ManagedInstance<DocumentMenuItem> documentMenuItems;
    protected RegisteredDocumentsMenuBuilder registeredDocumentsMenuBuilder = new RegisteredDocumentsMenuBuilder(registeredDocumentsMenuView,
                                                                                                                 documentMenuItems);

    @Mock
    protected MenuItem saveMenuItem;

    @Mock
    protected MenuItem versionManagerMenuItem;

    @Mock
    protected GuidedDecisionTableModellerView.Presenter modeller;

    @Mock
    protected GuidedDecisionTableModellerView modellerView;

    @Mock
    protected KieMultipleDocumentEditorWrapperView kieEditorWrapperView;

    @Mock
    protected OverviewWidgetPresenter overviewWidget;

    @Mock
    protected SavePopUpPresenter savePopUpPresenter;

    @Mock
    protected ImportsWidgetPresenter importsWidget;

    @Mock
    protected EventSourceMock<NotificationEvent> notificationEvent;

    @Mock
    protected EventSourceMock<ChangeTitleWidgetEvent> changeTitleEvent;

    @Mock
    protected ProjectContext workbenchContext;

    @Mock
    protected VersionRecordManager versionRecordManager;

    @Mock
    protected VersionService versionService;
    protected CallerMock<VersionService> versionServiceCaller;

    @Mock
    protected EventSourceMock<RestoreEvent> restoreEvent;

    @Mock
    protected DeletePopUpPresenter deletePopUpPresenter;

    @Mock
    protected CopyPopUpPresenter copyPopUpPresenter;

    @Mock
    protected RenamePopUpPresenter renamePopUpPresenter;

    @Mock
    protected BusyIndicatorView busyIndicatorView;

    @Spy
    protected RestoreVersionCommandProvider restoreVersionCommandProvider = getRestoreVersionCommandProvider();

    @Spy
    protected BasicFileMenuBuilder basicFileMenuBuilder = getBasicFileMenuBuilder();

    @Spy
    protected FileMenuBuilder fileMenuBuilder = getFileMenuBuilder();

    @Mock
    protected DefaultFileNameValidator fileNameValidator;

    @Mock
    protected SyncBeanManager beanManager;

    @Mock
    protected PlaceManager placeManager;

    private GuidedDecisionTableEditorPresenter presenter;
    private GuidedDTableResourceType resourceType = new GuidedDTableResourceType();

    @Before
    public void setup() {
        this.dtServiceCaller = new CallerMock<>(dtService);
        this.versionServiceCaller = new CallerMock<>(versionService);

        final GuidedDecisionTableEditorPresenter wrapped = new GuidedDecisionTableEditorPresenter(view,
                                                                                                  dtServiceCaller,
                                                                                                  notification,
                                                                                                  decisionTableSelectedEvent,
                                                                                                  resourceType,
                                                                                                  editMenuBuilder,
                                                                                                  viewMenuBuilder,
                                                                                                  insertMenuBuilder,
                                                                                                  radarMenuBuilder,
                                                                                                  modeller,
                                                                                                  beanManager,
                                                                                                  placeManager);

        wrapped.setKieEditorWrapperView(kieEditorWrapperView);
        wrapped.setOverviewWidget(overviewWidget);
        wrapped.setSavePopUpPresenter(savePopUpPresenter);
        wrapped.setImportsWidget(importsWidget);
        wrapped.setNotificationEvent(notificationEvent);
        wrapped.setChangeTitleEvent(changeTitleEvent);
        wrapped.setWorkbenchContext(workbenchContext);
        wrapped.setVersionRecordManager(versionRecordManager);
        wrapped.setRegisteredDocumentsMenuBuilder(registeredDocumentsMenuBuilder);
        wrapped.setFileMenuBuilder(fileMenuBuilder);
        wrapped.setFileNameValidator(fileNameValidator);

        this.presenter = spy(wrapped);

        when(modeller.getView()).thenReturn(modellerView);
        when(versionRecordManager.newSaveMenuItem(any(Command.class))).thenReturn(saveMenuItem);
        when(versionRecordManager.buildMenu()).thenReturn(versionManagerMenuItem);

        presenter.init();
        presenter.setupMenuBar();
    }

    @Test
    public void checkMenuStructure() {
        final Menus menus = presenter.getMenus();
        final MenuVisitor visitor = new MenuVisitor() {
            @Override
            public boolean visitEnter(final Menus menus) {
                return true;
            }

            @Override
            public void visitLeave(final Menus menus) {
            }

            @Override
            public boolean visitEnter(final MenuGroup menuGroup) {
                System.out.println(menuGroup.getIdentifier() + " --> " + menuGroup.getCaption());
                return true;
            }

            @Override
            public void visitLeave(final MenuGroup menuGroup) {

            }

            @Override
            public void visit(final MenuItemPlain menuItemPlain) {
                System.out.println(menuItemPlain.getIdentifier() + " --> " + menuItemPlain.getCaption());
            }

            @Override
            public void visit(final MenuItemCommand menuItemCommand) {
                System.out.println(menuItemCommand.getIdentifier() + " --> " + menuItemCommand.getCaption());
            }

            @Override
            public void visit(final MenuItemPerspective menuItemPerspective) {
                System.out.println(menuItemPerspective.getIdentifier() + " --> " + menuItemPerspective.getCaption());
            }

            @Override
            public void visit(final MenuCustom<?> menuCustom) {
                System.out.println(menuCustom.getIdentifier() + " --> " + menuCustom.getCaption());
            }
        };
        menus.accept(visitor);
    }

    private RestoreVersionCommandProvider getRestoreVersionCommandProvider() {
        final RestoreVersionCommandProvider restoreVersionCommandProvider = new RestoreVersionCommandProvider();
        setField(restoreVersionCommandProvider,
                 "versionService",
                 versionServiceCaller);
        setField(restoreVersionCommandProvider,
                 "restoreEvent",
                 restoreEvent);
        setField(restoreVersionCommandProvider,
                 "busyIndicatorView",
                 view);
        return restoreVersionCommandProvider;
    }

    private BasicFileMenuBuilder getBasicFileMenuBuilder() {
        final BasicFileMenuBuilder basicFileMenuBuilder = new BasicFileMenuBuilderImpl(deletePopUpPresenter,
                                                                                       copyPopUpPresenter,
                                                                                       renamePopUpPresenter,
                                                                                       busyIndicatorView,
                                                                                       notification,
                                                                                       restoreVersionCommandProvider);
        setField(basicFileMenuBuilder,
                 "restoreVersionCommandProvider",
                 restoreVersionCommandProvider);
        setField(basicFileMenuBuilder,
                 "notification",
                 notificationEvent);
        setField(restoreVersionCommandProvider,
                 "busyIndicatorView",
                 view);
        return basicFileMenuBuilder;
    }

    private FileMenuBuilder getFileMenuBuilder() {
        final FileMenuBuilder fileMenuBuilder = new FileMenuBuilderImpl();
        setField(fileMenuBuilder,
                 "menuBuilder",
                 basicFileMenuBuilder);
        return fileMenuBuilder;
    }

    private void setField(final Object o,
                          final String fieldName,
                          final Object value) {
        try {
            final Field field = o.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(o,
                      value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail(e.getMessage());
        }
    }
}
