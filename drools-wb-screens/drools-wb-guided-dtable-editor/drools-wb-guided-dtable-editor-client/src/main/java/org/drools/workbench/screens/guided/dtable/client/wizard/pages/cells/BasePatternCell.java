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
package org.drools.workbench.screens.guided.dtable.client.wizard.pages.cells;

import java.util.Set;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import org.drools.workbench.models.guided.dtable.shared.model.Pattern52;
import org.drools.workbench.screens.guided.dtable.client.widget.Validator;
import org.drools.workbench.screens.guided.dtable.client.wizard.pages.RequiresValidator;
import org.uberfire.ext.widgets.core.client.resources.WizardResources;

/**
 * An abstract base class containing shared logic between {@link DefaultPatternCell}, {@link ActionSetFieldPatternCell}, and {@link ConditionPatternCell}.
 * <p/>
 * Important: Having this abstract base class as the common ancestor of the three previous classes avoids ambgious dependency exceptions.
 */
public abstract class BasePatternCell extends AbstractCell<Pattern52> implements RequiresValidator {

    protected Validator validator;

    protected interface FactPatternCellTemplate
            extends
            SafeHtmlTemplates {

        @Template("<div class=\"{0}\" >{1}</div>")
        SafeHtml text( String cssStyleName,
                       String message );
    }

    private static final FactPatternCellTemplate TEMPLATE = GWT.create( FactPatternCellTemplate.class );

    public BasePatternCell( String... consumedEvents ) {
        super( consumedEvents );
    }

    public BasePatternCell( Set consumedEvents ) {
        super( consumedEvents );
    }

    @Override
    public void setValidator( final Validator validator ) {
        this.validator = validator;
    }

    @Override
    public void render( final Context context,
                        final Pattern52 pattern,
                        final SafeHtmlBuilder sb ) {
        final String binding = pattern.getBoundName();
        final StringBuilder b = new StringBuilder();
        if ( binding == null || "".equals( binding ) ) {
            b.append( pattern.getFactType() );
        } else {
            b.append( pattern.getBoundName() );
            b.append( " : " );
            b.append( pattern.getFactType() );
        }
        sb.append( TEMPLATE.text( getCssStyleName( pattern ),
                                  b.toString() ) );
    }

    protected String getCssStyleName( final Pattern52 p ) {
        if ( !validator.isPatternBindingUnique( p ) ) {
            return WizardResources.INSTANCE.css().wizardDTableValidationError();
        }
        return "";
    }

}