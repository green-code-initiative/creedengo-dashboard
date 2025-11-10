/*
 * Creedengo Dashboard plugin - Provides dashboard to monitor and help reduce the environmental footprint of your programs
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
package org.greencodeinitiative.creedengo.confluence.plugin.sonar;

/**
 * Constants used across SonarQube API calls.
 *
 * <p>Sustainability tags used to filter issues or rules:</p>
 * <ul>
 *   <li>{@code sustainability} — generic tag used on some sonar rules</li>
 *   <li>{@code greensight} — tag used by the Capgemini Greensight plugin</li>
 *   <li>{@code ecocode} — tag occasionally used when creedengo was called ecocode</li>
 *   <li>{@code creedengo} — tag identifying rules provided by Creedengo plugins</li>
 * </ul>
 */
public final class SonarConstants {

    private SonarConstants() {
        // utility class
    }

    /**
     * Comma-separated sustainability tags for filtering issues and rules.
     */
    public static final String SUSTAINABILITY_TAGS = "sustainability,greensight,ecocode,creedengo";

    /**
     * Default issue statuses filter (current SonarQube API).
     */
    public static final String ISSUE_STATUSES = "OPEN,CONFIRMED";

    /**
     * Legacy issue statuses filter (for older SonarQube versions).
     */
    public static final String LEGACY_STATUSES = "OPEN,CONFIRMED,REOPENED";
}
