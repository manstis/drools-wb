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

package org.drools.workbench.screens.guided.dtable.client.widget.table.model.synchronizers.impl;

import org.drools.workbench.screens.guided.dtable.client.widget.table.model.synchronizers.ModelSynchronizer;
import org.junit.Test;
import org.uberfire.ext.wires.core.grids.client.model.impl.BaseGridCellValue;

import static org.junit.Assert.*;

public class ModelSynchronizerTest extends BaseSynchronizerTest {

    @Test
    public void testSetCells() throws ModelSynchronizer.MoveColumnVetoException {
        modelSynchronizer.appendRow();

        uiModel.setCell( 0,
                         1,
                         new BaseGridCellValue<String>( "value" ) );

        assertEquals( "value",
                      model.getData().get( 0 ).get( 1 ).getStringValue() );
        assertEquals( "value",
                      uiModel.getCell( 0,
                                       1 ).getValue().getValue() );
    }

    @Test
    public void testDeleteCells() throws ModelSynchronizer.MoveColumnVetoException {
        modelSynchronizer.appendRow();

        uiModel.setCell( 0,
                         1,
                         new BaseGridCellValue<String>( "value" ) );
        assertEquals( "value",
                      model.getData().get( 0 ).get( 1 ).getStringValue() );
        assertEquals( "value",
                      uiModel.getCell( 0,
                                       1 ).getValue().getValue() );

        uiModel.deleteCell( 0,
                            1 );

        assertNull( model.getData().get( 0 ).get( 1 ).getStringValue() );
        assertNull( uiModel.getCell( 0,
                                     1 ) );
    }

}
