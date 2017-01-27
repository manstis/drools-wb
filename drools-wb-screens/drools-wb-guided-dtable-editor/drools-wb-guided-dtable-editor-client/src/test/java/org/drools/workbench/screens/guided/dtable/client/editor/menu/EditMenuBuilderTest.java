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
import java.util.HashSet;

import org.drools.workbench.models.datamodel.oracle.DataType;
import org.drools.workbench.models.guided.dtable.shared.model.ConditionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.DTCellValue52;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.models.guided.dtable.shared.model.MetadataCol52;
import org.drools.workbench.models.guided.dtable.shared.model.Pattern52;
import org.drools.workbench.screens.guided.dtable.client.editor.clipboard.Clipboard;
import org.drools.workbench.screens.guided.dtable.client.editor.clipboard.impl.DefaultClipboard;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTablePresenter;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.dtable.client.widget.table.events.cdi.DecisionTableSelectedEvent;
import org.drools.workbench.screens.guided.dtable.client.widget.table.events.cdi.DecisionTableSelectionsChangedEvent;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.GuidedDecisionTableUiModel;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.synchronizers.ModelSynchronizer;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.uberfire.ext.wires.core.grids.client.model.GridColumn;
import org.uberfire.ext.wires.core.grids.client.model.impl.BaseGridColumn;
import org.uberfire.ext.wires.core.grids.client.model.impl.BaseGridRow;
import org.uberfire.ext.wires.core.grids.client.widget.grid.renderers.columns.GridColumnRenderer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EditMenuBuilderTest {

    private EditMenuBuilder builder;
    private GuidedDecisionTable52 model;
    private GuidedDecisionTableUiModel uiModel;
    private Clipboard clipboard;

    @Mock
    private TranslationService ts;

    @Mock
    private GuidedDecisionTableView.Presenter dtPresenter;
    private GuidedDecisionTablePresenter.Access access = new GuidedDecisionTablePresenter.Access();

    @Mock
    private GuidedDecisionTableView dtPresenterView;

    @Before
    @SuppressWarnings("unchecked")
    public void setup() {
        model = new GuidedDecisionTable52();
        uiModel = new GuidedDecisionTableUiModel(mock(ModelSynchronizer.class));
        clipboard = new DefaultClipboard();

        when(dtPresenter.getView()).thenReturn(dtPresenterView);
        when(dtPresenter.getModel()).thenReturn(model);
        when(dtPresenter.getAccess()).thenReturn(access);
        when(dtPresenterView.getModel()).thenReturn(uiModel);
        when(ts.getTranslation(any(String.class))).thenReturn("i18n");

        uiModel.appendColumn(new BaseGridColumn<String>(mock(GridColumn.HeaderMetaData.class),
                                                        mock(GridColumnRenderer.class),
                                                        100));
        uiModel.appendColumn(new BaseGridColumn<String>(mock(GridColumn.HeaderMetaData.class),
                                                        mock(GridColumnRenderer.class),
                                                        100));
        uiModel.appendColumn(new BaseGridColumn<String>(mock(GridColumn.HeaderMetaData.class),
                                                        mock(GridColumnRenderer.class),
                                                        100));
        uiModel.appendRow(new BaseGridRow());

        builder = new EditMenuBuilder(clipboard,
                                      ts);
        builder.setup();
    }

    @Test
    public void testOnDecisionTableSelectedEventWithNoSelections() {
        builder.onDecisionTableSelectedEvent(new DecisionTableSelectedEvent(dtPresenter));

        assertFalse(builder.miCut.isEnabled());
        assertFalse(builder.miCopy.isEnabled());
        assertFalse(builder.miPaste.isEnabled());
        assertFalse(builder.miDeleteSelectedCells.isEnabled());
        assertFalse(builder.miDeleteSelectedColumns.isEnabled());
        assertFalse(builder.miDeleteSelectedRows.isEnabled());
        assertFalse(builder.miOtherwiseCell.isEnabled());
    }

    @Test
    public void testOnDecisionTableSelectedEventWithNonOtherwiseColumnSelected() {
        model.getMetadataCols().add(new MetadataCol52());
        model.getData().add(new ArrayList<DTCellValue52>() {{
            add(new DTCellValue52(1));
            add(new DTCellValue52("descr"));
            add(new DTCellValue52("md"));
        }});

        uiModel.selectCell(0,
                           2);

        builder.onDecisionTableSelectedEvent(new DecisionTableSelectedEvent(dtPresenter));

        assertTrue(builder.miCut.isEnabled());
        assertTrue(builder.miCopy.isEnabled());
        assertFalse(builder.miPaste.isEnabled());
        assertTrue(builder.miDeleteSelectedCells.isEnabled());
        assertTrue(builder.miDeleteSelectedColumns.isEnabled());
        assertTrue(builder.miDeleteSelectedRows.isEnabled());
        assertFalse(builder.miOtherwiseCell.isEnabled());
// TODO {manstis} miOtherwiseCell needs to be a Custom Widget to handle a check-mark
//        verify( builder.miOtherwiseCell,
//                times( 1 ) ).setOtherwise( eq( false ) );
    }

    @Test
    public void testOnDecisionTableSelectedEventWithOtherwiseColumnSelected() {
        model.getConditions().add(new Pattern52() {{
            setFactType("Fact");
            getChildColumns().add(new ConditionCol52() {{
                setFactType("Fact");
                setFactField("field1");
                setFieldType(DataType.TYPE_STRING);
                setOperator("==");
            }});
        }});
        model.getData().add(new ArrayList<DTCellValue52>() {{
            add(new DTCellValue52(1));
            add(new DTCellValue52("descr"));
            add(new DTCellValue52("md"));
        }});

        uiModel.selectCell(0,
                           2);

        builder.onDecisionTableSelectedEvent(new DecisionTableSelectedEvent(dtPresenter));

        assertTrue(builder.miCut.isEnabled());
        assertTrue(builder.miCopy.isEnabled());
        assertFalse(builder.miPaste.isEnabled());
        assertTrue(builder.miDeleteSelectedCells.isEnabled());
        assertTrue(builder.miDeleteSelectedColumns.isEnabled());
        assertTrue(builder.miDeleteSelectedRows.isEnabled());
        assertTrue(builder.miOtherwiseCell.isEnabled());
// TODO {manstis} miOtherwiseCell needs to be a Custom Widget to handle a check-mark
//        verify( builder.miOtherwiseCell,
//                times( 1 ) ).setOtherwiseCell( eq( false ) );
    }

    @Test
    public void testOnDecisionTableSelectedEventWithOtherwiseCellSelected() {
        model.getConditions().add(new Pattern52() {{
            setFactType("Fact");
            getChildColumns().add(new ConditionCol52() {{
                setFactType("Fact");
                setFactField("field1");
                setFieldType(DataType.TYPE_STRING);
                setOperator("==");
            }});
        }});
        model.getData().add(new ArrayList<DTCellValue52>() {{
            add(new DTCellValue52(1));
            add(new DTCellValue52("descr"));
            add(new DTCellValue52() {{
                setOtherwise(true);
            }});
        }});

        uiModel.selectCell(0,
                           2);

        builder.onDecisionTableSelectedEvent(new DecisionTableSelectedEvent(dtPresenter));

        assertTrue(builder.miCut.isEnabled());
        assertTrue(builder.miCopy.isEnabled());
        assertFalse(builder.miPaste.isEnabled());
        assertTrue(builder.miDeleteSelectedCells.isEnabled());
        assertTrue(builder.miDeleteSelectedColumns.isEnabled());
        assertTrue(builder.miDeleteSelectedRows.isEnabled());
        assertTrue(builder.miOtherwiseCell.isEnabled());
// TODO {manstis} miOtherwiseCell needs to be a Custom Widget to handle a check-mark
//        verify( builder.miOtherwiseCell,
//                times( 1 ) ).setOtherwiseCell( eq( true ) );
    }

    @Test
    public void testOnDecisionTableSelectedEventWithSelectionsWithClipboardPopulated() {
        model.getMetadataCols().add(new MetadataCol52());
        model.getData().add(new ArrayList<DTCellValue52>() {{
            add(new DTCellValue52(1));
            add(new DTCellValue52("descr"));
            add(new DTCellValue52("md"));
        }});

        uiModel.selectCell(0,
                           2);
        clipboard.setData(new HashSet<Clipboard.ClipboardData>() {{
            add(new DefaultClipboard.ClipboardDataImpl(0,
                                                       2,
                                                       model.getData().get(0).get(2)));
        }});

        builder.onDecisionTableSelectedEvent(new DecisionTableSelectedEvent(dtPresenter));

        assertTrue(builder.miCut.isEnabled());
        assertTrue(builder.miCopy.isEnabled());
        assertTrue(builder.miPaste.isEnabled());
        assertTrue(builder.miDeleteSelectedCells.isEnabled());
        assertTrue(builder.miDeleteSelectedColumns.isEnabled());
        assertTrue(builder.miDeleteSelectedRows.isEnabled());
        assertFalse(builder.miOtherwiseCell.isEnabled());
// TODO {manstis} miOtherwiseCell needs to be a Custom Widget to handle a check-mark
//        verify( builder.miOtherwiseCell,
//                times( 1 ) ).setOtherwiseCell( eq( false ) );
    }

    @Test
    public void testOnDecisionTableSelectionsChangedEventWithNoSelections() {
        builder.onDecisionTableSelectionsChangedEvent(new DecisionTableSelectionsChangedEvent(dtPresenter));

        assertFalse(builder.miCut.isEnabled());
        assertFalse(builder.miCopy.isEnabled());
        assertFalse(builder.miPaste.isEnabled());
        assertFalse(builder.miDeleteSelectedCells.isEnabled());
        assertFalse(builder.miDeleteSelectedColumns.isEnabled());
        assertFalse(builder.miDeleteSelectedRows.isEnabled());
        assertFalse(builder.miOtherwiseCell.isEnabled());
    }

    @Test
    public void testOnDecisionTableSelectionsChangedEventWithNonOtherwiseColumnSelected() {
        model.getMetadataCols().add(new MetadataCol52());
        model.getData().add(new ArrayList<DTCellValue52>() {{
            add(new DTCellValue52(1));
            add(new DTCellValue52("descr"));
            add(new DTCellValue52("md"));
        }});

        uiModel.selectCell(0,
                           2);

        builder.onDecisionTableSelectionsChangedEvent(new DecisionTableSelectionsChangedEvent(dtPresenter));

        assertTrue(builder.miCut.isEnabled());
        assertTrue(builder.miCopy.isEnabled());
        assertFalse(builder.miPaste.isEnabled());
        assertTrue(builder.miDeleteSelectedCells.isEnabled());
        assertTrue(builder.miDeleteSelectedColumns.isEnabled());
        assertTrue(builder.miDeleteSelectedRows.isEnabled());
        assertFalse(builder.miOtherwiseCell.isEnabled());
// TODO {manstis} miOtherwiseCell needs to be a Custom Widget to handle a check-mark
//        verify( builder.miOtherwiseCell,
//                times( 1 ) ).setOtherwiseCell( eq( false ) );
    }

    @Test
    public void testOnDecisionTableSelectionsChangedEventWithOtherwiseColumnSelected() {
        model.getConditions().add(new Pattern52() {{
            setFactType("Fact");
            getChildColumns().add(new ConditionCol52() {{
                setFactType("Fact");
                setFactField("field1");
                setFieldType(DataType.TYPE_STRING);
                setOperator("==");
            }});
        }});
        model.getData().add(new ArrayList<DTCellValue52>() {{
            add(new DTCellValue52(1));
            add(new DTCellValue52("descr"));
            add(new DTCellValue52("md"));
        }});

        uiModel.selectCell(0,
                           2);

        builder.onDecisionTableSelectionsChangedEvent(new DecisionTableSelectionsChangedEvent(dtPresenter));

        assertTrue(builder.miCut.isEnabled());
        assertTrue(builder.miCopy.isEnabled());
        assertFalse(builder.miPaste.isEnabled());
        assertTrue(builder.miDeleteSelectedCells.isEnabled());
        assertTrue(builder.miDeleteSelectedColumns.isEnabled());
        assertTrue(builder.miDeleteSelectedRows.isEnabled());
        assertTrue(builder.miOtherwiseCell.isEnabled());
// TODO {manstis} miOtherwiseCell needs to be a Custom Widget to handle a check-mark
//        verify( builder.miOtherwiseCell,
//                times( 1 ) ).setOtherwiseCell( eq( false ) );
    }

    @Test
    public void testOnDecisionTableSelectionsChangedEventWithOtherwiseCellSelected() {
        model.getConditions().add(new Pattern52() {{
            setFactType("Fact");
            getChildColumns().add(new ConditionCol52() {{
                setFactType("Fact");
                setFactField("field1");
                setFieldType(DataType.TYPE_STRING);
                setOperator("==");
            }});
        }});
        model.getData().add(new ArrayList<DTCellValue52>() {{
            add(new DTCellValue52(1));
            add(new DTCellValue52("descr"));
            add(new DTCellValue52() {{
                setOtherwise(true);
            }});
        }});

        uiModel.selectCell(0,
                           2);

        builder.onDecisionTableSelectionsChangedEvent(new DecisionTableSelectionsChangedEvent(dtPresenter));

        assertTrue(builder.miCut.isEnabled());
        assertTrue(builder.miCopy.isEnabled());
        assertFalse(builder.miPaste.isEnabled());
        assertTrue(builder.miDeleteSelectedCells.isEnabled());
        assertTrue(builder.miDeleteSelectedColumns.isEnabled());
        assertTrue(builder.miDeleteSelectedRows.isEnabled());
        assertTrue(builder.miOtherwiseCell.isEnabled());
// TODO {manstis} miOtherwiseCell needs to be a Custom Widget to handle a check-mark
//        verify( builder.miOtherwiseCell,
//                times( 1 ) ).setOtherwiseCell( eq( true ) );
    }

    @Test
    public void testOnDecisionTableSelectionsChangedEventWithSelectionsWithClipboardPopulated() {
        model.getMetadataCols().add(new MetadataCol52());
        model.getData().add(new ArrayList<DTCellValue52>() {{
            add(new DTCellValue52(1));
            add(new DTCellValue52("descr"));
            add(new DTCellValue52("md"));
        }});

        uiModel.selectCell(0,
                           2);
        clipboard.setData(new HashSet<Clipboard.ClipboardData>() {{
            add(new DefaultClipboard.ClipboardDataImpl(0,
                                                       2,
                                                       model.getData().get(0).get(2)));
        }});

        builder.onDecisionTableSelectionsChangedEvent(new DecisionTableSelectionsChangedEvent(dtPresenter));

        assertTrue(builder.miCut.isEnabled());
        assertTrue(builder.miCopy.isEnabled());
        assertTrue(builder.miPaste.isEnabled());
        assertTrue(builder.miDeleteSelectedCells.isEnabled());
        assertTrue(builder.miDeleteSelectedColumns.isEnabled());
        assertTrue(builder.miDeleteSelectedRows.isEnabled());
        assertFalse(builder.miOtherwiseCell.isEnabled());
// TODO {manstis} miOtherwiseCell needs to be a Custom Widget to handle a check-mark
//        verify( builder.miOtherwiseCell,
//                times( 1 ) ).setOtherwiseCell( eq( false ) );
    }

    @Test
    public void testOnDecisionTableSelectedEventReadOnly() {
        dtPresenter.getAccess().setReadOnly(true);
        builder.onDecisionTableSelectedEvent(new DecisionTableSelectedEvent(dtPresenter));

        assertFalse(builder.miCut.isEnabled());
        assertFalse(builder.miCopy.isEnabled());
        assertFalse(builder.miPaste.isEnabled());
        assertFalse(builder.miDeleteSelectedCells.isEnabled());
        assertFalse(builder.miDeleteSelectedColumns.isEnabled());
        assertFalse(builder.miDeleteSelectedRows.isEnabled());
        assertFalse(builder.miOtherwiseCell.isEnabled());
    }
}
