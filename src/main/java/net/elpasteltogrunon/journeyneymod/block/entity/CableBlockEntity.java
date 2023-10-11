package net.elpasteltogrunon.journeyneymod.block.entity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CableBlockEntity extends EnergyBlockEntity
{
    public CableBlockEntity master;
    public boolean isMaster;
    List<EnergyBlockEntity> connectedEnergyBLocks;
    List<Direction> connections;

    public CableBlockEntity(BlockPos pos, BlockState state) 
    {
        super(ModBlockEntities.CABLE.get(), pos, state);

        this.maxEnergy = 100;
        this.master = null;
    }
    
    public static void tick(Level level, BlockPos pos, BlockState state, CableBlockEntity pCableEntity) 
    {
        if(level.isClientSide()) return;

        if(pCableEntity.master == null)
        {
            pCableEntity.master = pCableEntity.findMasterAndConnections(pos);
            pCableEntity.addNearEnergyBlocksToNetwork(pos);
        }

        if(pCableEntity.isMaster)
        {
            for (EnergyBlockEntity connection : pCableEntity.connectedEnergyBLocks) 
            {
                if(connection.isReceiver)
                {
                    pCableEntity.transferEnergy(pCableEntity.getMaxTransfer(), connection);
                }
                else
                {
                    pCableEntity.suckEnergy(connection.getMaxTransfer(), connection);
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
            BlockPos newPos = pos.offset(dir.getNormal());
            CableBlockEntity posCable = getPosMaster(newPos);
            //The master can only be null if there is no cable, since all cables must have a master
            if(posCable!=null)
            {
                connections.add(dir);

                if(masterCandidate == null)
                    masterCandidate = posCable;
                else
                    ((CableBlockEntity) level.getBlockEntity(newPos)).updateMaster(masterCandidate, newPos);
            }
        }

        if(masterCandidate == null)
        {
            this.connectedEnergyBLocks = new ArrayList<>();
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
        if(this.isMaster)
        {
            for(EnergyBlockEntity energyBlock : this.connectedEnergyBLocks)
            {
                if(!newMaster.connectedEnergyBLocks.contains(energyBlock))
                newMaster.connectedEnergyBLocks.add(energyBlock);
            }
            this.connectedEnergyBLocks.clear();
        }
        
        this.master = newMaster;
        this.isMaster = newMaster == this;
        
        //Update neighbours
        for(Direction dir : connections)
        {
            BlockPos newPos = pos.offset(dir.getNormal());
            if(getPosMaster(newPos)!= newMaster)
            {
                ((CableBlockEntity) level.getBlockEntity(newPos)).updateMaster(newMaster, newPos);
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
                if(!master.connectedEnergyBLocks.contains((EnergyBlockEntity) blockEntity))
                    master.connectedEnergyBLocks.add((EnergyBlockEntity) blockEntity);
            }
            
        }
    }

    public void removeFromNetwork(BlockPos pos)
    {
        if(this.isMaster)
        {
            for (Direction dir : connections) 
            {
                BlockPos newPos = pos.offset(dir.getNormal());
                BlockEntity blockEntity = level.getBlockEntity(newPos);
                if(getPosMaster(newPos)==this)
                {
                    ((CableBlockEntity)blockEntity).updateMaster((CableBlockEntity)blockEntity, pos);
                }
            }
        }
        else
        {
            
        }
    }

    public void sendMasterChatMessage()
    {
        if(master != null)
            level.players().get(0).sendSystemMessage(Component.literal("Master position: " + master.getBlockPos().getX() + ", " + master.getBlockPos().getY() + ", " + master.getBlockPos().getZ()));
        else
            level.players().get(0).sendSystemMessage(Component.literal("No master found..."));
    }
}
