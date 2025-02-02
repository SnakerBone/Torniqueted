package xyz.snaker.tq.level.entity.projectile;

import java.util.Comparator;
import java.util.List;

import xyz.snaker.snakerlib.level.entity.Trajectile;
import xyz.snaker.tq.level.entity.utterfly.Utterfly;
import xyz.snaker.tq.rego.Entities;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.NotNull;

/**
 * Created by SnakerBone on 20/02/2023
 * <p>
 * Credit: <a href="https://github.com/sinkillerj/ProjectE/blob/mc1.16.x/src/main/java/moze_intel/projecte/gameObjs/entity/EntityHomingArrow.java">ProjectE</a>
 **/
public class HommingArrow extends Trajectile
{
    static final EntityDataAccessor<Integer> TARGET_ID = SynchedEntityData.defineId(HommingArrow.class, EntityDataSerializers.INT);
    static final int NULL_TARGET_ID = -1;

    int newTargetCooldown = 0;

    public HommingArrow(EntityType<HommingArrow> type, Level level)
    {
        super(type, level);
    }

    public HommingArrow(Level level, LivingEntity shooter, double damage)
    {
        super(Entities.HOMMING_ARROW.get(), shooter, level);
        setBaseDamage(damage);
    }

    @NotNull
    @Override
    public EntityType<?> getType()
    {
        return Entities.HOMMING_ARROW.get();
    }

    @Override
    public void defineSynchedData()
    {
        super.defineSynchedData();
        entityData.define(TARGET_ID, NULL_TARGET_ID);
    }

    @Override
    public void doPostHurtEffects(@NotNull LivingEntity entity)
    {
        super.doPostHurtEffects(entity);
        entity.invulnerableTime = 0;
    }

    @Override
    public void tick()
    {
        if (!level().isClientSide && this.tickCount > 3) {
            if (hasTarget() && (!getTarget().isAlive() || this.inGround)) {
                entityData.set(TARGET_ID, NULL_TARGET_ID);
            }
            if (!hasTarget() && !this.inGround && newTargetCooldown <= 0) {
                findNewTarget();
            } else {
                newTargetCooldown--;
            }
        }

        if (tickCount > 3 && hasTarget() && !this.inGround) {
            double mX = getDeltaMovement().x();
            double mY = getDeltaMovement().y();
            double mZ = getDeltaMovement().z();
            Entity target = getTarget();
            Vec3 arrowLoc = new Vec3(getX(), getY(), getZ());
            Vec3 targetLoc = new Vec3(target.getX(), target.getY() + target.getBbHeight() / 2, target.getZ());
            Vec3 lookVec = targetLoc.subtract(arrowLoc);
            Vec3 arrowMotion = new Vec3(mX, mY, mZ);
            double theta = wrap180Radian(angleBetween(arrowMotion, lookVec));
            theta = clampAbs(theta, Math.PI / 2);
            Vec3 crossProduct = arrowMotion.cross(lookVec).normalize();
            Vec3 adjustedLookVec = transform(crossProduct, theta, arrowMotion);
            shoot(adjustedLookVec.x, adjustedLookVec.y, adjustedLookVec.z, 1.0F, 0);
        }

        super.tick();
    }

    public Vec3 transform(Vec3 axis, double angle, Vec3 normal)
    {
        double m00 = 1;
        double m01 = 0;
        double m02 = 0;
        double m10 = 0;
        double m11 = 1;
        double m12 = 0;
        double m20 = 0;
        double m21 = 0;
        double m22 = 1;
        double mag = Math.sqrt(axis.x * axis.x + axis.y * axis.y + axis.z * axis.z);
        if (mag >= 1.0E-10) {
            mag = 1.0 / mag;
            double ax = axis.x * mag;
            double ay = axis.y * mag;
            double az = axis.z * mag;
            double sinTheta = Math.sin(angle);
            double cosTheta = Math.cos(angle);
            double t = 1.0 - cosTheta;
            double xz = ax * az;
            double xy = ax * ay;
            double yz = ay * az;
            m00 = t * ax * ax + cosTheta;
            m01 = t * xy - sinTheta * az;
            m02 = t * xz + sinTheta * ay;
            m10 = t * xy + sinTheta * az;
            m11 = t * ay * ay + cosTheta;
            m12 = t * yz - sinTheta * ax;
            m20 = t * xz - sinTheta * ay;
            m21 = t * yz + sinTheta * ax;
            m22 = t * az * az + cosTheta;
        }
        return new Vec3(m00 * normal.x + m01 * normal.y + m02 * normal.z, m10 * normal.x + m11 * normal.y + m12 * normal.z, m20 * normal.x + m21 * normal.y + m22 * normal.z);
    }

    public void findNewTarget()
    {
        List<Mob> candidates = level().getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(8, 8, 8));
        if (!candidates.isEmpty()) {
            candidates.sort(Comparator.comparing(this::distanceToSqr, Double::compare));
            entityData.set(TARGET_ID, candidates.get(0).getId());
        }
        newTargetCooldown = 5;
    }

    public Mob getTarget()
    {
        return (Mob) level().getEntity(entityData.get(TARGET_ID));
    }

    public boolean hasTarget()
    {
        return getTarget() != null && !(getTarget() instanceof Utterfly);
    }

    public double angleBetween(Vec3 a, Vec3 b)
    {
        double vDot = a.dot(b) / (a.length() * b.length());
        if (vDot < -1.0) {
            vDot = -1.0;
        }
        if (vDot > 1.0) {
            vDot = 1.0;
        }
        return Math.acos(vDot);
    }

    public double wrap180Radian(double radian)
    {
        radian %= 2 * Math.PI;
        while (radian >= Math.PI) {
            radian -= 2 * Math.PI;
        }
        while (radian < -Math.PI) {
            radian += 2 * Math.PI;
        }
        return radian;
    }

    public double clampAbs(double param, double maxMagnitude)
    {
        if (Math.abs(param) > maxMagnitude) {
            if (param < 0) {
                param = -Math.abs(maxMagnitude);
            } else {
                param = Math.abs(maxMagnitude);
            }
        }
        return param;
    }

    @Override
    public boolean ignoreExplosion()
    {
        return true;
    }
}
