package com.usuario.bibliasagrada.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Entidade "Jesus", invocada pela Espada de Miguel (clique direito).
 * É amigável ao jogador, persegue e ataca monstros próximos, e some
 * sozinha após um tempo, num brilho de luz.
 */
public class JesusEntity extends PathfinderMob {

    private static final int DURACAO_MAXIMA_TICKS = 20 * 45; // 45 segundos
    private int ticksVivo = 0;

    public JesusEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
        this.setPersistenceRequired();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.32D)
                .add(Attributes.ATTACK_DAMAGE, 12.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.ARMOR, 6.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Monster.class, true));
    }

    @Override
    public boolean fireImmune() {
        // Jesus não teme o fogo do inferno
        return true;
    }

    @Override
    public boolean isInvulnerableTo(net.minecraft.world.damagesource.DamageSource source) {
        return source.is(net.minecraft.world.damagesource.DamageTypes.IN_FIRE)
                || source.is(net.minecraft.world.damagesource.DamageTypes.ON_FIRE)
                || super.isInvulnerableTo(source);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.level().isClientSide) {
            ticksVivo++;

            if (ticksVivo % 30 == 0 && this.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.END_ROD,
                        this.getX(), this.getY() + 1.0, this.getZ(),
                        3, 0.3, 0.5, 0.3, 0.01);
            }

            if (ticksVivo >= DURACAO_MAXIMA_TICKS) {
                desaparecer();
            }
        }
    }

    private void desaparecer() {
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.END_ROD,
                    this.getX(), this.getY() + 1.0, this.getZ(),
                    40, 0.4, 0.8, 0.4, 0.08);
            serverLevel.playSound(null, this.blockPosition(), SoundEvents.BEACON_DEACTIVATE,
                    SoundSource.NEUTRAL, 1.0F, 1.6F);
        }
        this.discard();
    }

    @Override
    protected void dropCustomDeathLoot(net.minecraft.world.damagesource.DamageSource source, int looting, boolean recentlyHit) {
        // não deixa nenhum item ao "morrer" (ele apenas desaparece)
    }

    @Override
    public boolean removeWhenFarAway(double distanceSq) {
        return false;
    }
}
