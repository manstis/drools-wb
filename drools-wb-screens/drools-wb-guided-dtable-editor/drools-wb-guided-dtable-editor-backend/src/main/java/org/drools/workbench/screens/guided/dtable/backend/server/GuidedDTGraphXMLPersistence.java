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

package org.drools.workbench.screens.guided.dtable.backend.server;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.drools.workbench.screens.guided.dtable.model.GuidedDecisionTableEditorGraphModel;
import org.uberfire.backend.vfs.Path;

public class GuidedDTGraphXMLPersistence {

    private static final GuidedDTGraphXMLPersistence INSTANCE = new GuidedDTGraphXMLPersistence();

    private XStream xt;

    private GuidedDTGraphXMLPersistence() {
        xt = new XStream( new DomDriver() );
        xt.alias( "graph",
                  GuidedDecisionTableEditorGraphModel.class );
        xt.alias( "entry",
                  GuidedDecisionTableEditorGraphModel.GuidedDecisionTableGraphEntry.class );
        xt.alias( "path",
                  Path.class );
    }

    public static GuidedDTGraphXMLPersistence getInstance() {
        return INSTANCE;
    }

    public String marshal( final GuidedDecisionTableEditorGraphModel content ) {
        return xt.toXML( content );
    }

    public GuidedDecisionTableEditorGraphModel unmarshal( final String xml ) {
        if ( xml == null || xml.trim().equals( "" ) ) {
            return new GuidedDecisionTableEditorGraphModel();
        }

        final Object o = xt.fromXML( xml );
        return (GuidedDecisionTableEditorGraphModel) o;
    }

}
