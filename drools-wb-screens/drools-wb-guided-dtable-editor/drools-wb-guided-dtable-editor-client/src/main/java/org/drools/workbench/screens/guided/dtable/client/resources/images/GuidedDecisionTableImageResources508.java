/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package org.drools.workbench.screens.guided.dtable.client.resources.images;

import com.google.gwt.user.client.ui.Image;
import org.drools.workbench.screens.guided.dtable.client.resources.GuidedDecisionTableResources;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableConstants;

/**
 * Images that have alt-t4ext set for Section 508 compliance
 */
public class GuidedDecisionTableImageResources508 {

    public static final GuidedDecisionTableImageResources508 INSTANCE = new GuidedDecisionTableImageResources508();

    private GuidedDecisionTableImageResources508() {
    }

    public Image Edit() {
        Image image = new Image( GuidedDecisionTableResources.INSTANCE.images().edit() );
        image.setAltText( GuidedDecisionTableConstants.INSTANCE.Edit() );
        return image;
    }

    public Image EditDisabled() {
        Image image = new Image( GuidedDecisionTableResources.INSTANCE.images().editDisabled() );
        image.setAltText( GuidedDecisionTableConstants.INSTANCE.EditDisabled() );
        return image;
    }

    public Image Config() {
        Image image = new Image( GuidedDecisionTableResources.INSTANCE.images().config() );
        image.setAltText( GuidedDecisionTableConstants.INSTANCE.Config() );
        return image;
    }

    public Image DeleteItemSmall() {
        Image image = new Image( GuidedDecisionTableResources.INSTANCE.itemImages().deleteItemSmall() );
        image.setAltText( GuidedDecisionTableConstants.INSTANCE.DeleteItem() );
        return image;
    }

    public Image NewItem() {
        Image image = new Image( GuidedDecisionTableResources.INSTANCE.itemImages().newItem() );
        image.setAltText( GuidedDecisionTableConstants.INSTANCE.NewItem() );
        return image;
    }

}
