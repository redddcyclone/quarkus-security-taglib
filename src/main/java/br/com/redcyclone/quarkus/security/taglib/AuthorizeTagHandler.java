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
import jakarta.faces.view.facelets.*;

import java.io.IOException;
import java.util.Arrays;

/**
 * TagHandler for the &lt;authorize&gt; tag.
 *
 * @author Leonardo Bernardes
 */
public class AuthorizeTagHandler extends TagHandler {

    private final TagAttribute role, anyRole, allRoles, var;

    /**
     * Constructor for the TagHandler
     * @param config TagConfig
     */
    public AuthorizeTagHandler(TagConfig config) {
        super(config);
        role = getAttribute("role");
        anyRole = getAttribute("anyRole");
        allRoles = getAttribute("allRoles");
        var = getAttribute("var");
    }

    @Override
    public void apply(FaceletContext ctx, UIComponent parent) throws IOException {
        try (InstanceHandle<SecurityIdentity> handle = Arc.container().instance(SecurityIdentity.class)) {
            SecurityIdentity identity = handle.get();
            if (identity == null)
                return;

            int argCount = 0;
            if (role != null) argCount++;
            if (anyRole != null) argCount++;
            if (allRoles != null) argCount++;
            if (argCount == 0) {
                throw new TagAttributeException(null, "You must specify an attribute for authorization!");
            } else if (argCount > 1) {
                throw new TagAttributeException(null, "You can only specify one attribute for authorization!");
            }

            boolean authorized;

            if (role != null) {
                String roleStr = role.getValue();
                if (roleStr == null || roleStr.isEmpty()) {
                    return;
                }
                if (roleStr.contains(" ")) {
                    throw new TagAttributeException(role, "The \"role\" attribute may not contain spaces!");
                } else if (roleStr.contains(",")) {
                    throw new TagAttributeException(role, "The \"role\" attribute expects a single role!");
                }
                authorized = identity.hasRole(roleStr);
            } else if (anyRole != null) {
                String rolesStr = anyRole.getValue(ctx);
                if (rolesStr == null || rolesStr.trim().isEmpty()) {
                    return;
                }
                authorized = Arrays.stream(rolesStr.split(",")).map(String::trim).anyMatch(identity::hasRole);
            } else {
                String allRolesStr = allRoles.getValue(ctx);
                if (allRolesStr == null || allRolesStr.trim().isEmpty()) {
                    return;
                }
                authorized = Arrays.stream(allRolesStr.split(",")).map(String::trim).allMatch(identity::hasRole);
            }

            if (authorized) {
                this.nextHandler.apply(ctx, parent);
            }

            if(var != null) {
                String varStr = var.getValue();
                if(varStr != null && !varStr.isEmpty()) {
                    ctx.setAttribute(varStr, authorized);
                }
            }
        }
    }

}
