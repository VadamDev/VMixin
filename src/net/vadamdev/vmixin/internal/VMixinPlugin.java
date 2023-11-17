package net.vadamdev.vmixin.internal;

import javassist.*;
import net.vadamdev.vmixin.internal.utils.Utils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author VadamDev
 * @since 16/11/2023
 */
public class VMixinPlugin extends JavaPlugin {
    @Override
    public void onLoad() {
        final Logger logger = getLogger();
        logger.info("Initializing VMixin...");

        final long before = System.currentTimeMillis();

        final File[] plugins = getDataFolder().getParentFile().listFiles((dir, name) -> name.endsWith(".jar"));

        final Set<VMixinConfig> mixinConfigs = new HashSet<>();
        for (File pluginJar : plugins) {
            final VMixinConfig mixinConfig = Utils.findMixinConfigFile(pluginJar);
            if(mixinConfig != null) {
                mixinConfigs.add(mixinConfig);
                Utils.addJarToClassPool(pluginJar);
            }
        }

        logger.info("-> Scanned " + plugins.length + " plugins jars | Found " + mixinConfigs.size() + " vmixins config");

        final Set<VMixinClass> mixinClasses = new HashSet<>();
        for(VMixinConfig mixinConfig : mixinConfigs) {
            final String mainPackage = mixinConfig.getMainPackage();

            for(String mixinClass : mixinConfig.getMixins()) {
                try {
                    mixinClasses.add(new VMixinClass(mainPackage + "." + mixinClass));
                } catch (NotFoundException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        logger.info("-> Found " + mixinClasses.size() + " mixins");

        int failed = 0;
        for (VMixinClass mixinClass : mixinClasses) {
            try {
                mixinClass.inject();
            }catch(Exception e) {
                failed++;
                e.printStackTrace();
            }
        }

        logger.info("Injection Complete ! (" + (mixinClasses.size() - failed) + "/" + mixinClasses.size() + " | Took: " + (System.currentTimeMillis() - before) + " ms)");

        logger.info("\n \n" +
                "Keep in mind that this plugin is experimental.\n" +
                "It may not work or even break stuff ! USE IT A YOUR OWN RISK\n" +
                " ");
    }
}
