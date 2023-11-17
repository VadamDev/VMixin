package net.vadamdev.vmixin.test;

import net.vadamdev.vmixin.api.VMixin;
import net.vadamdev.vmixin.api.injection.At;
import net.vadamdev.vmixin.api.injection.Inject;
import org.bukkit.Bukkit;

/**
 * @author VadamDev
 * @since 16/11/2023
 */
@VMixin(target = "net.minecraft.server.v1_8_R3.ChunkProviderServer")
public class TestMixin {
    @Inject(method = "loadChunk", at = At.TOP)
    public void loadChunk1() {
        System.out.println("Loading chunk...");
    }

    @Inject(method = "loadChunk", at = At.BOTTOM)
    public void loadChunk2() {
        Bukkit.broadcastMessage("Chunk loaded !");
    }
}
