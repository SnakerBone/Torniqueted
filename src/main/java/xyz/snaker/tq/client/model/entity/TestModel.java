package xyz.snaker.tq.client.model.entity;

import xyz.snaker.snakerlib.math.Maths;
import xyz.snaker.snakerlib.utility.ResourcePath;
import xyz.snaker.tq.level.entity.mob.Test;

import org.jetbrains.annotations.NotNull;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

/**
 * Created by SnakerBone on 30/07/2023
 **/
public class TestModel extends EntityModel<Test>
{
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourcePath("test"), "main");
    private final ModelPart base;


    public TestModel(ModelPart root)
    {
        this.base = root.getChild("base");
    }

    public static LayerDefinition createBodyLayer()
    {
        MeshDefinition mesh = new MeshDefinition();

        PartDefinition root = mesh.getRoot();
        PartDefinition base = root.addOrReplaceChild("base", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        circleModel(base);

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(@NotNull Test test, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {

    }

    @Override
    public void renderToBuffer(@NotNull PoseStack stack, @NotNull VertexConsumer consumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
    {
        base.render(stack, consumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public static void ringModel(PartDefinition base)
    {
        CubeListBuilder inner = CubeListBuilder.create();
        CubeListBuilder outer = CubeListBuilder.create();
        CubeDeformation innerDef = CubeDeformation.NONE.extend(0.02F, 2F, 0.02F);
        CubeDeformation outerDef = CubeDeformation.NONE.extend(1F);

        for (int i = 0; i < 360; i++) {
            float x = Maths.cos(i * 360) * 30;
            float z = Maths.sin(i * 360) * 30;

            outer.addBox(x, -3, z, 1, 1, 1, outerDef);
            outer.addBox(x, 3, z, 1, 1, 1, outerDef);

            if (i % 8 == 0) {
                inner.addBox(x, 0, z, 1, 1, 1, innerDef);
            }
        }

        base.addOrReplaceChild("outerRing", outer, PartPose.ZERO);
        base.addOrReplaceChild("innerRing", inner, PartPose.ZERO);
    }

    public static void sphereModel(PartDefinition base, int radius, byte cubes)
    {
        CubeListBuilder sphere = CubeListBuilder.create();

        for (int i = 0; i < cubes; i++) {
            for (int j = 0; j < cubes; j++) {
                float theta = Maths.PI * i / (cubes - 1);
                float phi = 2 * Maths.PI * j / cubes;

                float x = radius * Maths.sin(theta) * Maths.cos(phi);
                float y = radius * Maths.sin(theta) * Maths.sin(phi);
                float z = radius * Maths.cos(theta);

                sphere.addBox(x, y, z, 1, 1, 1, CubeDeformation.NONE.extend(3));
            }
        }

        base.addOrReplaceChild("sphere", sphere, PartPose.ZERO);
    }

    public static void circleModel(PartDefinition base)
    {
        CubeListBuilder body = CubeListBuilder.create();
        CubeListBuilder outer = CubeListBuilder.create();

        for (int i = 0; i < 360; i++) {
            float bodyX = Maths.sin(i) * (i / 4F);
            float bodyZ = Maths.cos(i) * (i / 4F);

            float outerX = Maths.sin(i) * (i / 4F);
            float outerZ = Maths.cos(i) * (i / 4F);

            body.addBox(bodyX, 0, bodyZ, 1, 1, 1, CubeDeformation.NONE.extend(1));

            if (i > 270) {
                outer.addBox(outerX, 0, outerZ, 1, 1, 1, CubeDeformation.NONE.extend(3));
            }
        }

        base.addOrReplaceChild("body", body, PartPose.ZERO);
        base.addOrReplaceChild("outer", outer, PartPose.ZERO);
    }
}