package tk.miskyle.talkroomdemo.core.plugin.loader;

import lombok.extern.slf4j.Slf4j;
import tk.miskyle.talkroomdemo.api.ChatRoomPlugin;
import tk.miskyle.talkroomdemo.core.setting.Settings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Slf4j
public class PluginLoader extends URLClassLoader {
  private static final Method pluginInitializationMethod;

  static {
    Method pluginInitializationMethod1;
    try {
      pluginInitializationMethod1 = ChatRoomPlugin.class.getDeclaredMethod("initialization",
              String.class,
              String.class,
              String.class);
    } catch (NoSuchMethodException e) {
      pluginInitializationMethod1 = null;
      e.printStackTrace();
    }
    pluginInitializationMethod = pluginInitializationMethod1;
  }

  private final HashMap<Class<?>, ChatRoomPlugin> plugins = new HashMap<>();
  private final HashSet<Class<?>> enabledPlugins = new HashSet<>();

  public PluginLoader() {
    super(new URL[]{});
  }

  public synchronized void loadPlugin(File file) throws ClassNotFoundException,
          InstantiationException,
          IllegalAccessException,
          InvocationTargetException {
    if (file == null || !file.exists() || !file.isFile()
            || !file.getName().toLowerCase().endsWith(".jar")) {
      return;
    }

    try (JarFile jar = new JarFile(file)) {
      // check plugin.properties
      JarEntry propFile = jar.getJarEntry("plugin.properties");
      if (propFile == null) {
        throw new FileNotFoundException("Missing plugin.properties file in this jar: " + file);
      }
      Properties properties = new Properties();
      properties.load(jar.getInputStream(propFile));
      String mainClass = properties.getProperty("main", null);
      String name = properties.getProperty("name", null);
      String version = properties.getProperty("version", null);
      String author = properties.getProperty("author", "");
      if (mainClass == null) {
        throw new PluginLoadException("Can't find main property, did you add into plugin.properties file?");
      } if (name == null) {
        throw new PluginLoadException("Can't find name property, did you add into plugin.properties file?");
      } else if (version == null) {
        throw new PluginLoadException("Can't find version property, did you add into plugin.properties file?");
      }
      // load jar file and try to load and make instance;
      addURL(file.toURI().toURL());
      Class<?> clazz = loadClass(mainClass);
      if (clazz.getSuperclass() == null
              || clazz.getSuperclass() != ChatRoomPlugin.class) {
        throw new ClassCastException(clazz + " is not extend ChatRoomPlugin");
      }
      ChatRoomPlugin plugin = (ChatRoomPlugin) clazz.newInstance();
      pluginInitializationMethod.setAccessible(true);
      pluginInitializationMethod.invoke(plugin, name, version, author);
      pluginInitializationMethod.setAccessible(false);
      plugins.put(clazz, plugin);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void searchPlugin() {
    for (File file : Objects.requireNonNull(Settings.FOLDER_PLUGIN.listFiles())) {
      if (file.isFile()) {
        try {
          loadPlugin(file);
        } catch (ClassNotFoundException |
                InstantiationException |
                IllegalAccessException |
                InvocationTargetException e) {
          e.printStackTrace();
        }
      }
    }
    callLoad();
    new HashSet<>(plugins.keySet()).forEach(this::callEnable);
  }

  public synchronized void callLoad() {
    new HashSet<>(plugins.values()).forEach(plugin -> {
      plugin.onLoad();
      log.info("Loaded plugin: " + plugin.getName() + " " +
              plugin.getVersion() + " by " + plugin.getAuthor());
    });
  }

  public synchronized void callEnable(Class<?> clazz) {
    if (!enabledPlugins.contains(clazz)) {
      ChatRoomPlugin plugin = plugins.get(clazz);
      plugin.onEnable();
      enabledPlugins.add(clazz);
      log.info("Enabled plugin: " + plugin.getName() + " " +
              plugin.getVersion() + " by " + plugin.getAuthor());
    }
  }

  public synchronized void callDisable(Class<?> clazz) {
    if (enabledPlugins.contains(clazz)) {
      enabledPlugins.remove(clazz);
      ChatRoomPlugin plugin = plugins.get(clazz);
      plugin.onDisable();
      log.info("Disabled plugin: " + plugin.getName() + " " +
              plugin.getVersion() + " by " + plugin.getAuthor());
    }
  }

  public boolean isEnable(Class<? extends ChatRoomPlugin> clazz) {
    return enabledPlugins.contains(clazz);
  }
}
