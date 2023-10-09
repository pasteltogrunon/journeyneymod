package net.elpasteltogrunon.journeyneymod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.elpasteltogrunon.journeyneymod.entity.animations.ModAnimationDefinitions;
import net.elpasteltogrunon.journeyneymod.entity.custom.TortoisimEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class TortoisimModel<T extends Entity> extends HierarchicalModel<T> 
{
	private final ModelPart tortoisim;
	private final ModelPart head;

	public TortoisimModel(ModelPart root) 
	{
		this.tortoisim = root.getChild("tortoisim");
		this.head = tortoisim.getChild("head");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition tortoisim = partdefinition.addOrReplaceChild("tortoisim", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition shell = tortoisim.addOrReplaceChild("shell", CubeListBuilder.create().texOffs(1, 0).addBox(-9.0F, -7.0F, -11.0F, 18.0F, 4.0F, 22.0F, new CubeDeformation(0.0F))
		.texOffs(0, 26).addBox(-7.0F, -12.0F, -10.0F, 14.0F, 5.0F, 18.0F, new CubeDeformation(0.0F))
		.texOffs(0, 16).addBox(-1.0F, -5.0F, -14.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition head = tortoisim.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -6.0F, 12.0F, 6.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(44, 34).addBox(-2.0F, -5.0F, 8.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition legs = tortoisim.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bone4 = legs.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(44, 26).addBox(6.0F, -4.0F, 5.0F, 5.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bone3 = legs.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(0, 26).addBox(-11.0F, -4.0F, 5.0F, 5.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bone2 = legs.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(0, 8).addBox(-11.0F, -4.0F, -12.0F, 5.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bone = legs.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 34).addBox(6.0F, -4.0F, -12.0F, 5.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}


	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) 
	{
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.applyHeadRotation(netHeadYaw, headPitch, ageInTicks);

		this.animateWalk(ModAnimationDefinitions.TORTOISIM_WALK, limbSwing, limbSwingAmount, 5f, 5f);
		this.animate(((TortoisimEntity) entity).idleAnimationState, ModAnimationDefinitions.TORTOISIM_IDLE, ageInTicks, 1f);
	}

	private void applyHeadRotation(float pNetHeadYaw, float pHeadPitch, float pAgeInTicks) {
		pNetHeadYaw = Mth.clamp(pNetHeadYaw, -20.0F, 20.0F);
		pHeadPitch = Mth.clamp(pHeadPitch, -10.0F, 15.0F);

		this.head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
		this.head.xRot = -pHeadPitch * ((float)Math.PI / 180F);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) 
	{
		tortoisim.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() 
	{
		return tortoisim;
	}


}