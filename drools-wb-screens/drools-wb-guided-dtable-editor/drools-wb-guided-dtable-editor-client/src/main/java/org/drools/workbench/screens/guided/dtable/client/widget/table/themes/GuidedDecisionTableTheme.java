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

package org.drools.workbench.screens.guided.dtable.client.widget.table.themes;

import com.ait.lienzo.client.core.shape.Text;
import com.ait.lienzo.shared.core.types.TextUnit;
import org.uberfire.ext.wires.core.grids.client.widget.renderers.themes.MultiColouredTheme;

public class GuidedDecisionTableTheme extends MultiColouredTheme {

    @Override
    public Text getHeaderText() {
        final Text t = super.getHeaderText();
        t.setTextUnit( TextUnit.PX );
        return t;
    }

    @Override
    public Text getBodyText() {
        final Text t = super.getBodyText();
        t.setTextUnit( TextUnit.PX );
        return t;
    }

}
