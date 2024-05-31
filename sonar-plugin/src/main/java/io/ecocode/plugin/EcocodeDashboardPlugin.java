// Package

package io.ecocode.plugin;

import org.sonar.api.Plugin;

import io.ecocode.plugin.web.EcocodeDashboardPageDefinition;

/**
 * Main plugin class
 */
public class EcocodeDashboardPlugin implements Plugin {
    @Override
    public void define(Context context) {
        context.addExtension(EcocodeDashboardPageDefinition.class);
    }
}
