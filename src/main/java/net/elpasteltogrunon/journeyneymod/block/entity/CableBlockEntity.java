package net.elpasteltogrunon.journeyneymod.block.entity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import static net.elpasteltogrunon.journeyneymod.block.custom.CableBlock.propFromDir;

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
        this.connections = new ArrayList<>();
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

        for(Direction dir : Direction.values())
        {
            BlockPos newPos = pos.relative(dir);
            CableBlockEntity posCableMaster = getPosMaster(newPos);
            //The master can only be null if there is no cable, since all cables must have a master
            if(posCableMaster!=null)
            {
                CableBlockEntity cableEntity = (CableBlockEntity) level.getBlockEntity(newPos);
                this.addConnection(dir, pos);
                cableEntity.addConnection(dir.getOpposite(), newPos);

                if(masterCandidate == null)
                    masterCandidate = posCableMaster;
                else
                    cableEntity.updateMaster(masterCandidate, newPos, dir.getOpposite(), false);
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
        if(level.getBlockEntity(pos) instanceof CableBlockEntity cableEntity)
            return cableEntity.master;

        return null;
    }

    //Updates master and calls it on connections
    public void updateMaster(CableBlockEntity newMaster, BlockPos pos, Direction commingFrom, boolean doUpdateEnergyBlocks)
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
        
        if(doUpdateEnergyBlocks)
            addNearEnergyBlocksToNetwork(pos);

        //Update neighbours
        for(Direction dir : connections)
        {
            if(!dir.equals(commingFrom))
            {
                BlockPos newPos = pos.relative(dir);
                if(getPosMaster(newPos) != null)
                    if(!getPosMaster(newPos).equals(newMaster))
                    {
                        ((CableBlockEntity) level.getBlockEntity(newPos)).updateMaster(newMaster, newPos, dir.getOpposite(), doUpdateEnergyBlocks);
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
                {
                    level.setBlockAndUpdate(pos, level.getBlockState(pos).setValue(propFromDir.get(dir), true));
                    master.connectedEnergyBLocks.add((EnergyBlockEntity) blockEntity);
                }
            }
            
        }
    }

    //Removes the master from the network
    public void removeFromNetwork(BlockPos pos)
    {
        for(Direction dir : connections)
        {
            BlockPos newPos = pos.relative(dir);
            if (level.getBlockEntity(newPos) instanceof CableBlockEntity cableEntity)
                cableEntity.removeConnection(dir.getOpposite(), newPos);
        }

        if(this.isMaster)
        {

            for (Direction dir : connections) 
            {
                BlockPos newPos = pos.relative(dir);
                if (level.getBlockEntity(newPos) instanceof CableBlockEntity cableEntity)
                {
                    //If after updating masters in one direction it keeps being the same in this direction, update it
                    if(getPosMaster(newPos).equals(this))
                    {
                        cableEntity.updateMaster(cableEntity, newPos, dir.getOpposite(), true);
                    }
                }
                else
                {
                    System.out.println("(Removefromnetwork) Cable (at " + pos.toShortString() + ") connection at " + dir.name() + " does not exist! (1)"); 
                }
            }
        }
        else
        {
            List<CableBlockEntity> cablesOnNewNetwork = new ArrayList<>();
            //Connections cannot be empty, since it is not master
            CableBlockEntity oldMaster = getPosMaster(pos.relative(this.connections.get(0)));
            if(oldMaster != null)
            {
                oldMaster.connectedEnergyBLocks.clear();
                oldMaster.addToNetworkList(cablesOnNewNetwork, oldMaster.getBlockPos(), null);
            }

            for(Direction dir : connections)
            {
                BlockPos newPos = pos.relative(dir);
                if(level.getBlockEntity(newPos) instanceof CableBlockEntity cableEntity)
                {
                    if(!cablesOnNewNetwork.contains(cableEntity))
                    {
                        cableEntity.updateMaster(cableEntity, newPos, dir.getOpposite(), true);
                    }
                }
            }

        }

        connections.clear();
    }

    public void addToNetworkList(List<CableBlockEntity> networkCables, BlockPos pos, Direction commingFrom)
    {
        if(!networkCables.contains(this))
        {
            networkCables.add(this);
            this.addNearEnergyBlocksToNetwork(pos);
            for(Direction dir : connections)
            {
                if(!dir.equals(commingFrom))
                {
                    BlockPos newPos = pos.relative(dir);
                    if(level.getBlockEntity(newPos) instanceof CableBlockEntity cableEntity)
                    {
                        cableEntity.addToNetworkList(networkCables, newPos, dir.getOpposite());
                    }
                }
            }
        }
    }

    public void newEnergyBlockNear(EnergyBlockEntity energyBlockEntity, BlockPos pos, Direction dir)
    {
        if(level.isClientSide()) return;
        if(!(energyBlockEntity instanceof CableBlockEntity))
        {
            if(this.master != null && !this.master.connectedEnergyBLocks.contains(energyBlockEntity))
            {
                this.master.connectedEnergyBLocks.add(energyBlockEntity);
                level.setBlockAndUpdate(pos, level.getBlockState(pos).setValue(propFromDir.get(dir), true));
            }
        }
    } 

    public void removeEnergyBlockNear(EnergyBlockEntity energyBlockEntity, BlockPos pos, Direction dir)
    {
        if(level.isClientSide()) return;
        if(!(energyBlockEntity instanceof CableBlockEntity))
        {
            if(this.master != null && this.master.connectedEnergyBLocks.contains(energyBlockEntity))
            {
                this.master.connectedEnergyBLocks.remove(energyBlockEntity);
                level.setBlockAndUpdate(pos, level.getBlockState(pos).setValue(propFromDir.get(dir), false));
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

    private void addConnection(Direction dir, BlockPos pos)
    {
        if(!this.connections.contains(dir))
            this.connections.add(dir);

        level.setBlockAndUpdate(pos, level.getBlockState(pos).setValue(propFromDir.get(dir), true));
    }

    private void removeConnection(Direction dir, BlockPos pos)
    {
        this.connections.remove(dir);

        level.setBlockAndUpdate(pos, level.getBlockState(pos).setValue(propFromDir.get(dir), false));
    }
}
