/*
 * Copyright 2025 Leonardo Bernardes (@redddcyclone)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.redcyclone.quarkus.security.taglib;

import io.quarkus.arc.Arc;
import io.quarkus.arc.InstanceHandle;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.faces.component.UIComponent;
import jakarta.faces.view.facelets.FaceletContext;
import jakarta.faces.view.facelets.TagConfig;
import jakarta.faces.view.facelets.TagHandler;

import java.io.IOException;

/**
 * TagHandler for the &lt;authenticated&gt; tag
 *
 * @author Leonardo Bernardes
 */
public class AuthenticatedTagHandler extends TagHandler {

    /**
     * Constructor for the TagHandler
     * @param config TagConfig
     */
    public AuthenticatedTagHandler(TagConfig config) {
        super(config);
    }

    @Override
    public void apply(FaceletContext faceletContext, UIComponent parent) throws IOException {
        try (InstanceHandle<SecurityIdentity> handle = Arc.container().instance(SecurityIdentity.class)) {
            SecurityIdentity identity = handle.get();
            if (identity == null)
                return;

            if (!identity.isAnonymous()) {
                this.nextHandler.apply(faceletContext, parent);
            }
        }
    }

}
