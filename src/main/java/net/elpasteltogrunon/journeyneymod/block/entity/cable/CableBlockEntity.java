package net.elpasteltogrunon.journeyneymod.block.entity.cable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.elpasteltogrunon.journeyneymod.block.entity.EnergyBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CableBlockEntity extends EnergyBlockEntity
{
    public CableBlockEntity master;
    public boolean isMaster;
    List<EnergyBlockEntity> connectedEnergyBLocks;
    List<Direction> connections;

    public CableBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) 
    {
        super(blockEntityType, pos, state);

        this.maxEnergy = 5000;
        this.master = findMasterAndConnections(pos);
        addNearEnergyBlocksToNetwork(pos);
    }
    
    public static void tick(Level level, BlockPos pos, BlockState state, CableBlockEntity pCableEntity) 
    {
        if(level.isClientSide()) return;

        if(pCableEntity.isMaster)
        {
            for (EnergyBlockEntity connection : pCableEntity.connectedEnergyBLocks) 
            {
                if(connection.isReceiver)
                {
                    int transferredEnergy = Math.min(pCableEntity.getMaxTransfer(), pCableEntity.getEnergy());
                    pCableEntity.extractEnergy(connection.receiveEnergy(transferredEnergy));
                }
                else
                {
                    int transferredEnergy = Math.min(connection.getMaxTransfer(), connection.getEnergy());
                    pCableEntity.receiveEnergy(connection.receiveEnergy(transferredEnergy));
                }
            }
        }
    }

    //Finds the master of this cable and its connections
    public CableBlockEntity findMasterAndConnections(BlockPos pos)
    { 
        CableBlockEntity masterCandidate = null;

        this.connections = new ArrayList<>();
        for(Direction dir : Direction.values())
        {
            BlockPos position = pos.offset(dir.getNormal());
            CableBlockEntity posCable = getPosMaster(position);
            //The master can only be null if there is no cable, since all cables must have a master
            if(posCable!=null)
            {
                connections.add(dir);

                if(masterCandidate == null)
                    masterCandidate = posCable;
                else
                    ((CableBlockEntity) level.getBlockEntity(pos)).updateMaster(masterCandidate, position);
            }
        }

        if(masterCandidate == null)
        {
            this.isMaster = true;
            return this;
        }
        else
        {
            this.isMaster = false;
            return masterCandidate;
        }
    }

    //Gets the master at a certain location
    public CableBlockEntity getPosMaster(BlockPos pos)
    {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if(blockEntity instanceof CableBlockEntity)
        {
            this.isMaster = false;
            return ((CableBlockEntity) blockEntity).master;
        }

        return null;
    }

    //Updates master and calls it on connections
    public void updateMaster(CableBlockEntity newMaster, BlockPos pos)
    {
        this.master = newMaster;
        this.isMaster = false;
        for(Direction dir : connections)
        {
            BlockPos position = pos.offset(dir.getNormal());
            if(getPosMaster(position)!= newMaster)
            {
                ((CableBlockEntity) level.getBlockEntity(pos)).updateMaster(newMaster, position);
                if(this.isMaster)
                {
                    for(EnergyBlockEntity energyBlock : this.connectedEnergyBLocks)
                    {
                        newMaster.connectedEnergyBLocks.add(energyBlock);
                    }
                }
            }
        }
    }

    public void addNearEnergyBlocksToNetwork(BlockPos pos)
    {
        for(Direction dir : Direction.values())
        {
            BlockPos position = pos.offset(dir.getNormal());
            BlockEntity blockEntity = level.getBlockEntity(position);
            if(blockEntity instanceof EnergyBlockEntity && ! (blockEntity instanceof CableBlockEntity))
            {
                if(!connectedEnergyBLocks.contains((EnergyBlockEntity) blockEntity))
                    master.connectedEnergyBLocks.add((EnergyBlockEntity) blockEntity);
            }
            
        }
    }
}
