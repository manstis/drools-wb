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

package org.drools.workbench.screens.guided.dtable.client.widget.table.popovers;

import org.uberfire.client.callbacks.Callback;

/**
 * The View for generic Popovers.
 */
public interface PopOverView {

    /**
     * Shows the Popover with content from the given {@link ContentProvider}
     * @param provider
     *         Provider of content to be shown.
     */
    void show( final PopOverView.ContentProvider provider );

    /**
     * Hides the Popover.
     */
    void hide();

    interface ContentProvider {

        void getContent( final Callback<Content> setter );

    }

    interface Content {

        String getContent();

        int getScreenX();

        int getScreenY();

    }

}
