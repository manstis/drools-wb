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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.enterprise.event.Event;

import org.drools.workbench.screens.guided.dtable.service.GuidedDecisionTableEditorService;
import org.drools.workbench.screens.guided.dtable.service.GuidedDecisionTableGraphEditorService;
import org.drools.workbench.screens.guided.dtable.service.GuidedDecisionTableLinkManager;
import org.drools.workbench.screens.guided.dtable.type.GuidedDTableResourceTypeDefinition;
import org.drools.workbench.screens.workitems.service.WorkItemsEditorService;
import org.guvnor.common.services.backend.util.CommentedOptionFactory;
import org.guvnor.common.services.backend.validation.GenericValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.services.datamodel.backend.server.service.DataModelService;
import org.kie.workbench.common.services.shared.project.KieProjectService;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.uberfire.backend.vfs.Path;
import org.uberfire.backend.vfs.PathFactory;
import org.uberfire.ext.editor.commons.backend.version.VersionRecordService;
import org.uberfire.ext.editor.commons.service.CopyService;
import org.uberfire.ext.editor.commons.service.DeleteService;
import org.uberfire.ext.editor.commons.service.RenameService;
import org.uberfire.io.IOService;
import org.uberfire.java.nio.IOException;
import org.uberfire.java.nio.file.DirectoryStream;
import org.uberfire.java.nio.file.Paths;
import org.uberfire.mocks.EventSourceMock;
import org.uberfire.rpc.SessionInfo;
import org.uberfire.workbench.events.ResourceOpenedEvent;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GuidedDecisionTableGraphEditorServiceImplTest {

    @Mock
    private IOService ioService;

    @Mock
    private CopyService copyService;

    @Mock
    private DeleteService deleteService;

    @Mock
    private RenameService renameService;

    @Mock
    private DataModelService dataModelService;

    @Mock
    private WorkItemsEditorService workItemsService;

    @Mock
    private KieProjectService projectService;

    @Mock
    private VersionRecordService versionRecordService;

    @Mock
    private GuidedDecisionTableEditorService dtableService;

    @Mock
    private GuidedDecisionTableLinkManager dtableLinkManager;

    @Mock
    private Event<ResourceOpenedEvent> resourceOpenedEvent = new EventSourceMock<>();

    @Mock
    private GenericValidator genericValidator;

    @Mock
    private CommentedOptionFactory commentedOptionFactory;

    @Mock
    private SessionInfo sessionInfo;

    @Mock
    private org.guvnor.common.services.project.model.Package pkg;

    private GuidedDecisionTableGraphEditorService service;

    private GuidedDTableResourceTypeDefinition resourceType = new GuidedDTableResourceTypeDefinition();

    private final List<org.uberfire.java.nio.file.Path> resolvedPaths = new ArrayList<>();

    @Before
    public void setup() {
        service = new GuidedDecisionTableGraphEditorServiceImpl( ioService,
                                                                 copyService,
                                                                 deleteService,
                                                                 renameService,
                                                                 projectService,
                                                                 versionRecordService,
                                                                 dtableService,
                                                                 dtableLinkManager,
                                                                 resourceOpenedEvent,
                                                                 commentedOptionFactory,
                                                                 resourceType,
                                                                 sessionInfo );

        when( projectService.resolvePackage( any( Path.class ) ) ).thenReturn( pkg );
        when( pkg.getPackageMainResourcesPath() ).thenReturn( PathFactory.newPath( "project",
                                                                                   "default://project/src/main/resources" ) );

        resolvedPaths.clear();
        when( ioService.newDirectoryStream( any( org.uberfire.java.nio.file.Path.class ) ) ).thenReturn( new DirectoryStream<org.uberfire.java.nio.file.Path>() {
            @Override
            public void close() throws IOException {
                //This is al mocked!
            }

            @Override
            public Iterator<org.uberfire.java.nio.file.Path> iterator() {
                return resolvedPaths.iterator();
            }
        } );
    }

    @Test
    public void testListDecisionTablesInPackage() {
        final Path path = mock( Path.class );
        when( path.toURI() ).thenReturn( "default://project/src/main/resources/dtable1.gdst" );

        resolvedPaths.add( makeNioPath( "default://project/src/main/resources/dtable1.gdst" ) );
        resolvedPaths.add( makeNioPath( "default://project/src/main/resources/dtable2.gdst" ) );
        resolvedPaths.add( makeNioPath( "default://project/src/main/resources/dtable3.gdst" ) );
        resolvedPaths.add( makeNioPath( "default://project/src/main/resources/pupa.smurf" ) );

        final List<Path> paths = service.listDecisionTablesInPackage( path );

        assertNotNull( paths );
        assertEquals( 3,
                      paths.size() );
        final Set<String> fileNames = new HashSet<>();
        for ( Path p : paths ) {
            fileNames.add( p.getFileName() );
        }
        assertTrue( fileNames.contains( "dtable1.gdst" ) );
        assertTrue( fileNames.contains( "dtable2.gdst" ) );
        assertTrue( fileNames.contains( "dtable3.gdst" ) );
    }

    private org.uberfire.java.nio.file.Path makeNioPath( final String uri ) {
        return Paths.get( uri );
    }

}
