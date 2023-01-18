package sapphiregaze.amethystitems.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import sapphiregaze.amethystitems.init.EnchantInit;
import sapphiregaze.amethystitems.util.Utility;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    @ModifyVariable(method = "attack", at = @At(value = "STORE"), ordinal = 1)
    private float addTransposeDamage(float value) {
        PlayerEntity pe = (PlayerEntity) (Object) this;
        if (!pe.getEntityWorld().isClient()) {
            int transposeChanceBase = 20;
            float transposeHeathBase = 1F;
            int level = EnchantmentHelper.getLevel(EnchantInit.TRANSPOSE, pe.getMainHandStack());
            if (level > 0 && Utility.percentChance(transposeChanceBase * level)) {
                value += transposeHeathBase * level;
                pe.heal(transposeHeathBase * level * pe.getAttackCooldownProgress(0.5f));
                System.out.println("You've been healed");
                pe.playSound(SoundEvents.BLOCK_AMETHYST_CLUSTER_HIT, 1.0F, 1.0F);
            }
        }
        return value;
    }
}
