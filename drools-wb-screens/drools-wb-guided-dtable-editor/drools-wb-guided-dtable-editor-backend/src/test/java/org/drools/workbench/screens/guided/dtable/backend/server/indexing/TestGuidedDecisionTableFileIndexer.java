/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
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
package org.drools.workbench.screens.guided.dtable.backend.server.indexing;

import java.util.HashMap;
import javax.enterprise.context.ApplicationScoped;

import org.drools.workbench.models.commons.backend.oracle.ProjectDataModelOracleImpl;
import org.drools.workbench.models.datamodel.oracle.DataType;
import org.drools.workbench.models.datamodel.oracle.FieldAccessorsAndMutators;
import org.drools.workbench.models.datamodel.oracle.ModelField;
import org.drools.workbench.models.datamodel.oracle.ProjectDataModelOracle;
import org.drools.workbench.screens.guided.dtable.backend.server.indexing.classes.Applicant;
import org.drools.workbench.screens.guided.dtable.backend.server.indexing.classes.Mortgage;
import org.drools.workbench.screens.guided.dtable.type.GuidedDTableResourceTypeDefinition;
import org.kie.workbench.common.services.refactoring.backend.server.TestIndexer;
import org.kie.workbench.common.services.shared.project.KieProjectService;
import org.uberfire.io.IOService;
import org.uberfire.java.nio.file.Path;

/**
 * Test indexer
 */
@ApplicationScoped
public class TestGuidedDecisionTableFileIndexer extends GuidedDecisionTableFileIndexer implements TestIndexer<GuidedDTableResourceTypeDefinition> {

    @Override
    public void setIOService(final IOService ioService) {
        this.ioService = ioService;
    }

    @Override
    public void setProjectService(final KieProjectService projectService) {
        this.projectService = projectService;
    }

    @Override
    public void setResourceTypeDefinition(final GuidedDTableResourceTypeDefinition type) {
        this.type = type;
    }

    @Override
    protected ProjectDataModelOracle getProjectDataModelOracle(final Path path) {
        final ProjectDataModelOracle dmo = new ProjectDataModelOracleImpl();
        dmo.addProjectModelFields(new HashMap<String, ModelField[]>() {{
            put(Applicant.class.getCanonicalName(),
                new ModelField[]{
                        new ModelField("age",
                                       Integer.class.getName(),
                                       ModelField.FIELD_CLASS_TYPE.REGULAR_CLASS,
                                       ModelField.FIELD_ORIGIN.DECLARED,
                                       FieldAccessorsAndMutators.ACCESSOR,
                                       DataType.TYPE_NUMERIC_INTEGER)});
            put(Mortgage.class.getCanonicalName(),
                new ModelField[]{
                        new ModelField("amount",
                                       Integer.class.getName(),
                                       ModelField.FIELD_CLASS_TYPE.REGULAR_CLASS,
                                       ModelField.FIELD_ORIGIN.DECLARED,
                                       FieldAccessorsAndMutators.ACCESSOR,
                                       DataType.TYPE_NUMERIC_INTEGER),
                        new ModelField("applicant",
                                       Applicant.class.getCanonicalName(),
                                       ModelField.FIELD_CLASS_TYPE.REGULAR_CLASS,
                                       ModelField.FIELD_ORIGIN.DECLARED,
                                       FieldAccessorsAndMutators.ACCESSOR,
                                       Applicant.class.getCanonicalName())});
        }});
        return dmo;
    }
}
