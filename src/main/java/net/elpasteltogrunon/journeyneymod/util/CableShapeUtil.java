package net.elpasteltogrunon.journeyneymod.util;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import static net.elpasteltogrunon.journeyneymod.block.custom.CableBlock.propFromDir;

public class CableShapeUtil 
{

    private static final VoxelShape CORE = Shapes.box(.25, .25, .25, .75, .75, .75);

    private static final VoxelShape NORTH = Shapes.box(.25, .25, 0, .75, .75, .25);
    private static final VoxelShape SOUTH = Shapes.box(.25, .25, .75, .75, .75, 1);
    private static final VoxelShape WEST = Shapes.box(0, .25, .25, .25, .75, .75);
    private static final VoxelShape EAST = Shapes.box(.75, .25, .25, 1, .75, .75);
    private static final VoxelShape UP = Shapes.box(.25, .75, .25, .75, 1, .75);
    private static final VoxelShape DOWN = Shapes.box(.25, 0, .25, .75, .25, .75);

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

    public static VoxelShape getShape(BlockState state)
    {
        VoxelShape shape = CORE;
        for(Direction dir : Direction.values())
        {
            if(state.getValue(propFromDir.get(dir)))
                shape = Shapes.join(shape, shapesFromDir.get(dir), BooleanOp.OR);
        }
        return shape;
    }
}
