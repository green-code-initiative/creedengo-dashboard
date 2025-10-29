/*
 * Creedengo Dashboard - Provides a Creedengo Sustainability Dashboard to have an overview of the status and the main sustainability issue of the projects
 * Copyright © 2025 Green Code Initiative (https://green-code-initiative.org)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.greencodeinitiative.creedengo.dashboard.web;

import org.sonar.api.web.page.Context;
import org.sonar.api.web.page.Page;
import org.sonar.api.web.page.PageDefinition;

import static org.sonar.api.web.page.Page.Scope.COMPONENT;

public class DashboardPageDefinition implements PageDefinition {

  @Override
  public void define(Context context) {
    context
            .addPage(Page.builder("creedengodashboard/view")
                .setName("Creedengo")
                .setScope(COMPONENT)
                .build())
    ;
  }
}
