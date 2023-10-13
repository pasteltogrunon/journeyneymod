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
            pCableEntity.findMasterAndConnections(pos);
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
    public void findMasterAndConnections(BlockPos pos)
    { 
        CableBlockEntity masterCandidate = null;

        this.connections = new ArrayList<>();
        for(Direction dir : Direction.values())
        {
            BlockPos newPos = pos.relative(dir);
            CableBlockEntity posCableMaster = getPosMaster(newPos);
            //The master can only be null if there is no cable, since all cables must have a master
            if(posCableMaster!=null)
            {
                CableBlockEntity posCable = (CableBlockEntity) level.getBlockEntity(newPos);
                if(!this.connections.contains(dir))
                    this.connections.add(dir);
                if(!posCable.connections.contains(dir.getOpposite()))
                    posCable.connections.add(dir.getOpposite());

                if(masterCandidate == null)
                    masterCandidate = posCableMaster;
                else
                    posCable.updateMaster(masterCandidate, newPos, dir.getOpposite());
            }
        }

        if(masterCandidate == null)
        {
            this.connectedEnergyBLocks = new ArrayList<>();
            this.isMaster = true;
            this.master = this;
        }
        else
        {
            this.isMaster = false;
            this.master = masterCandidate;
        }
    }

    //Gets the master at a certain location
    public CableBlockEntity getPosMaster(BlockPos pos)
    {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if(blockEntity instanceof CableBlockEntity)
            return ((CableBlockEntity) blockEntity).master;

        return null;
    }

    //Updates master and calls it on connections
    public void updateMaster(CableBlockEntity newMaster, BlockPos pos, Direction commingFrom)
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
        if (this.isMaster = newMaster.equals(this))
        {
            this.connectedEnergyBLocks = new ArrayList<>();
        }
        
        //Update neighbours
        for(Direction dir : connections)
        {
            if(!dir.equals(commingFrom))
            {
                BlockPos newPos = pos.relative(dir);
                if(getPosMaster(newPos) != null)
                    if(!getPosMaster(newPos).equals(newMaster))
                    {
                        ((CableBlockEntity) level.getBlockEntity(newPos)).updateMaster(newMaster, newPos, dir.getOpposite());
                    }
                else
                    System.out.println("(Updatemaster) Cable (at " + pos.toShortString() + ") connection at " + dir.name() + " does not exist!");
            }
        }
    }

    public void addNearEnergyBlocksToNetwork(BlockPos pos)
    {
        for(Direction dir : Direction.values())
        {
            BlockPos position = pos.relative(dir);
            BlockEntity blockEntity = level.getBlockEntity(position);
            if(blockEntity instanceof EnergyBlockEntity && ! (blockEntity instanceof CableBlockEntity))
            {
                if(!master.connectedEnergyBLocks.contains((EnergyBlockEntity) blockEntity))
                    master.connectedEnergyBLocks.add((EnergyBlockEntity) blockEntity);
            }
            
        }
    }

    //Removes the master from the network
    public void removeFromNetwork(BlockPos pos)
    {
        if(this.isMaster)
        {
            for (Direction dir : connections) 
            {
                BlockPos newPos = pos.relative(dir);
                BlockEntity blockEntity = level.getBlockEntity(newPos);
                if (blockEntity instanceof CableBlockEntity cableEntity)
                {
                    cableEntity.connections.remove(dir.getOpposite());
                    if(getPosMaster(newPos).equals(this))
                    {
                        cableEntity.updateMaster(cableEntity, newPos, dir.getOpposite());
                    }
                }
                else
                {
                    System.out.println("(Removefromnetwork) Cable (at " + pos.toShortString() + ") connection at " + dir.name() + " does not exist! (1)"); 
                }
            }
        }
        connections.clear();
    }

    public void newEnergyBlockNear(EnergyBlockEntity energyBlockEntity)
    {
        if(level.isClientSide()) return;
        if(!(energyBlockEntity instanceof CableBlockEntity))
        {
            if(this.master != null && !this.master.connectedEnergyBLocks.contains(energyBlockEntity))
            {
                this.master.connectedEnergyBLocks.add(energyBlockEntity);
            }
        }
    } 

    public void sendMasterChatMessage()
    {
        if(master != null)
            level.players().get(0).sendSystemMessage(Component.literal("Master position: " + master.getBlockPos().toShortString()));
        else
            level.players().get(0).sendSystemMessage(Component.literal("No master found..."));
    }
}
