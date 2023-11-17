package net.vadamdev.vmixin.internal;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author VadamDev
 * @since 17/11/2023
 */
public class VMixinConfig {
    @SerializedName("package")
    private String mainPackage;

    @SerializedName("mixins")
    private List<String> mixins;

    public String getMainPackage() {
        return mainPackage;
    }

    public List<String> getMixins() {
        return mixins;
    }
}
