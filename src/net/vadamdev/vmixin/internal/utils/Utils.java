package net.vadamdev.vmixin.internal.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javassist.ClassPool;
import javassist.NotFoundException;
import net.vadamdev.vmixin.internal.VMixinConfig;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author VadamDev
 * @since 17/11/2023
 */
public class Utils {
    public static final ClassPool CLASS_POOL = ClassPool.getDefault();

    public static void addJarToClassPool(File jarFile) {
        try {
            CLASS_POOL.appendClassPath(jarFile.getAbsolutePath());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    private static final Gson gson = new GsonBuilder().create();

    public static VMixinConfig findMixinConfigFile(File file) {
        VMixinConfig mixinConfig = null;

        try {
            final ZipFile zipFile = new ZipFile(file);
            final ZipEntry entry = zipFile.getEntry("vmixins.json");

            if(entry != null) {
                InputStream in = zipFile.getInputStream(entry);
                Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8);

                mixinConfig = gson.fromJson(reader, VMixinConfig.class);
            }

            zipFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mixinConfig;
    }
}
