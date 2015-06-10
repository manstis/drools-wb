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
package org.drools.workbench.screens.guided.dtable.client.widget.table.columns;

import java.util.List;

import com.ait.lienzo.client.core.shape.Group;
import com.ait.lienzo.client.core.shape.Text;
import com.google.gwt.user.client.ui.Widget;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableConstants;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTablePresenter;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.GuidedDecisionTableUiCell;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.ext.wires.core.grids.client.model.BaseGridColumn;
import org.uberfire.ext.wires.core.grids.client.model.IGridCell;
import org.uberfire.ext.wires.core.grids.client.model.IGridCellValue;
import org.uberfire.ext.wires.core.grids.client.widget.context.GridBodyCellRenderContext;
import org.uberfire.ext.wires.core.grids.client.widget.dom.BaseDOMElement;
import org.uberfire.ext.wires.core.grids.client.widget.dom.single.IHasSingletonDOMElementResource;
import org.uberfire.ext.wires.core.grids.client.widget.dom.single.ISingletonDOMElementFactory;
import org.uberfire.ext.wires.core.grids.client.widget.renderers.columns.single.BaseGridColumnSingletonDOMElementRenderer;
import org.uberfire.ext.wires.core.grids.client.widget.renderers.themes.IGridRendererTheme;

/**
 * Base column for Decision Tables.
 * @param <T> The Type of value presented by this column
 * @param <F> The Factory to create DOMElements for this column
 */
public abstract class BaseUiSingletonColumn<T, W extends Widget, E extends BaseDOMElement, F extends ISingletonDOMElementFactory<W, E>> extends BaseGridColumn<T> implements IHasSingletonDOMElementResource {

    protected F factory;

    protected GuidedDecisionTablePresenter.Access access;

    public BaseUiSingletonColumn( final List<HeaderMetaData> headerMetaData,
                                  final BaseGridColumnSingletonDOMElementRenderer<T, W, E> columnRenderer,
                                  final double width,
                                  final boolean isResizable,
                                  final boolean isVisible,
                                  final GuidedDecisionTablePresenter.Access access,
                                  final F factory ) {
        super( headerMetaData,
               columnRenderer,
               width );
        setResizable( isResizable );
        setVisible( isVisible );
        this.access = access;
        this.factory = factory;
    }

    @Override
    public void edit( final IGridCell<T> cell,
                      final GridBodyCellRenderContext context,
                      final Callback<IGridCellValue<T>> callback ) {
        if ( !access.isEditable() ) {
            return;
        }
        doEdit( cell,
                context,
                callback );
    }

    @Override
    public void flush() {
        factory.flush();
    }

    @Override
    public void destroyResources() {
        factory.destroyResources();
    }

    public boolean isEditable() {
        return access.isEditable();
    }

    protected abstract void doEdit( final IGridCell<T> cell,
                                    final GridBodyCellRenderContext context,
                                    final Callback<IGridCellValue<T>> callback );

    protected abstract static class CellRenderer<T, W extends Widget, E extends BaseDOMElement> extends BaseGridColumnSingletonDOMElementRenderer<T, W, E> {

        public CellRenderer( final ISingletonDOMElementFactory<W, E> factory ) {
            super( factory );
        }

        @Override
        public Group renderCell( final IGridCell<T> cell,
                                 final GridBodyCellRenderContext context ) {
            if ( cell == null || cell.getValue() == null ) {
                return null;
            }

            final Group g = new Group();
            final IGridRendererTheme theme = context.getRenderer().getTheme();
            final Text t = theme.getBodyText()
                    .setListening( false )
                    .setX( context.getCellWidth() / 2 )
                    .setY( context.getCellHeight() / 2 );

            final GuidedDecisionTableUiCell<T> cellValue = (GuidedDecisionTableUiCell<T>) cell.getValue();
            if ( cellValue.isOtherwise() ) {
                t.setText( GuidedDecisionTableConstants.INSTANCE.OtherwiseCellLabel() );

            } else if ( cellValue.getValue() != null ) {
                doRenderCellContent( t,
                                     cellValue.getValue(),
                                     context );
            }
            g.add( t );
            return g;

        }

        protected abstract void doRenderCellContent( final Text t,
                                                     final T value,
                                                     final GridBodyCellRenderContext context );
    }

    protected boolean hasValue( final IGridCell<T> cell ) {
        if ( cell == null || cell.getValue() == null | cell.getValue().getValue() == null ) {
            return false;
        }
        return true;
    }

}
