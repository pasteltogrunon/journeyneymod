package net.elpasteltogrunon.journeyneymod.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CableShapeUtil 
{

    private static final VoxelShape CORE = Shapes.box(.3, .3, .3, .7, .7, .7);

    private static final VoxelShape NORTH = Shapes.box(.3, .3, 0, .7, .7, .3);
    private static final VoxelShape SOUTH = Shapes.box(.3, .3, .7, .7, .7, 1);
    private static final VoxelShape WEST = Shapes.box(0, .3, .3, .3, .7, .7);
    private static final VoxelShape EAST = Shapes.box(.7, .3, .3, 1, .7, .7);
    private static final VoxelShape UP = Shapes.box(.3, .7, .3, .7, 1, .7);
    private static final VoxelShape DOWN = Shapes.box(.3, 0, .3, .7, .3, .7);

    private static final Map<Direction, VoxelShape> shapesFromDir = new HashMap<Direction, VoxelShape>()
    {
        {
            put(Direction.DOWN, DOWN);
            put(Direction.UP, UP);
            put(Direction.NORTH, NORTH);
            put(Direction.SOUTH, SOUTH);
            put(Direction.EAST, EAST);
            put(Direction.WEST, WEST);
        }
    };

    public static VoxelShape getShape(List<Direction> connections)
    {
        VoxelShape shape = CORE;
        for(Direction dir : connections)
        {
            shape = Shapes.join(shape, shapesFromDir.get(dir), BooleanOp.OR);
        }
        return shape;
    }
}
