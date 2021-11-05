package tk.miskyle.talkroomdemo.core.plugin;

import tk.miskyle.talkroomdemo.core.plugin.loader.PluginLoader;

public class PluginManager {
  private final PluginLoader pluginLoader;

  public PluginManager() {
    pluginLoader = new PluginLoader();
    pluginLoader.searchPlugin();
  }
}
