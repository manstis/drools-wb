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

import com.google.gwt.user.client.ui.HasEnabled;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableModellerView;
import org.uberfire.client.mvp.UberView;

public interface ViewMenuView extends UberView<ViewMenuBuilder>,
                                      HasEnabled {

    interface Presenter {

        void setModeller( final GuidedDecisionTableModellerView.Presenter modeller );

        void onZoom( final int zoom );

        void onToggleMergeState();

        void onViewAuditLog();

    }

    void setMerged( final boolean merged );

    void setZoom125( final boolean checked );

    void setZoom100( final boolean checked );

    void setZoom75( final boolean checked );

    void setZoom50( final boolean checked );

    void enableToggleMergedStateMenuItem( final boolean enabled );

    void enableViewAuditLogMenuItem( final boolean enabled );

    void enableZoom( final boolean enabled );

}
