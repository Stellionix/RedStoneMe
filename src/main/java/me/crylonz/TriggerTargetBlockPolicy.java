package me.crylonz;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public final class TriggerTargetBlockPolicy {

    private static final Set<Material> NEVER_ALLOWED_MATERIALS = EnumSet.of(
            Material.AIR,
            Material.CAVE_AIR,
            Material.VOID_AIR,
            Material.BEDROCK,
            Material.BARRIER,
            Material.COMMAND_BLOCK,
            Material.CHAIN_COMMAND_BLOCK,
            Material.REPEATING_COMMAND_BLOCK,
            Material.STRUCTURE_BLOCK,
            Material.STRUCTURE_VOID,
            Material.JIGSAW,
            Material.END_PORTAL_FRAME,
            Material.END_PORTAL,
            Material.NETHER_PORTAL
    );

    private final RedStoneMe plugin;

    public TriggerTargetBlockPolicy(RedStoneMe plugin) {
        this.plugin = plugin;
    }

    public ValidationResult validate(Player player, Block block) {
        if (block == null) {
            return ValidationResult.invalid("You need to target with your cursor the block you want to trigger");
        }

        Material material = block.getType();
        if (NEVER_ALLOWED_MATERIALS.contains(material)) {
            return ValidationResult.invalid("This block type cannot be used as a trigger");
        }

        if (!isAllowedByConfig(material)) {
            return ValidationResult.invalid("This block type is not allowed for triggers");
        }

        if (plugin.getConfig().getBoolean("selection.respect-break-protection", true)
                && !canBreakAccordingToServer(player, block)) {
            return ValidationResult.invalid("You cannot use this block as a trigger here");
        }

        return ValidationResult.valid();
    }

    private boolean isAllowedByConfig(Material material) {
        List<String> configured = plugin.getConfig().getStringList("selection.allowed-trigger-blocks");
        if (configured.isEmpty()) {
            return true;
        }

        List<String> normalized = new ArrayList<String>();
        for (String value : configured) {
            normalized.add(value.toUpperCase(Locale.ROOT));
        }
        return normalized.contains(material.name());
    }

    private boolean canBreakAccordingToServer(Player player, Block block) {
        BlockBreakEvent event = new BlockBreakEvent(block, player);
        Bukkit.getPluginManager().callEvent(event);
        return !event.isCancelled();
    }

    public static final class ValidationResult {

        private static final ValidationResult VALID = new ValidationResult(true, null);

        private final boolean valid;
        private final String message;

        private ValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }

        public static ValidationResult valid() {
            return VALID;
        }

        public static ValidationResult invalid(String message) {
            return new ValidationResult(false, message);
        }

        public boolean isValid() {
            return valid;
        }

        public String getMessage() {
            return message;
        }
    }
}
