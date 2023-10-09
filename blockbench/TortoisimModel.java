// Made with Blockbench 4.8.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class tortoisim<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "tortoisim"), "main");
	private final ModelPart tortoisim;

	public tortoisim(ModelPart root) {
		this.tortoisim = root.getChild("tortoisim");
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
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		tortoisim.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}