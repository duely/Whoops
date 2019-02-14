package com.noobanidus.whoops.events;

import com.noobanidus.whoops.Whoops;
import com.noobanidus.whoops.WhoopsConfig;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.botania.common.block.BlockFloatingSpecialFlower;
import vazkii.botania.common.block.BlockSpecialFlower;
import vazkii.botania.common.block.tile.TileSpecialFlower;

public class WhoopsEvents {
    @SuppressWarnings("deprecation")
    @SubscribeEvent
    public static void onDaisyRightClick(PlayerInteractEvent.RightClickBlock event) {
        if (!WhoopsConfig.ENABLED) return;

        World world = event.getWorld();

        EntityPlayer player = event.getEntityPlayer();
        if (player instanceof FakePlayer || player.isSneaking() || event.getHand() != EnumHand.MAIN_HAND) {
            return;
        }

        BlockPos pos = event.getPos();

        IBlockState state = world.getBlockState(pos);
        if (!(state.getBlock() instanceof BlockSpecialFlower) && !(state.getBlock() instanceof BlockFloatingSpecialFlower)) return;

        if (world.getTileEntity(pos) instanceof TileSpecialFlower) {
            TileSpecialFlower te = (TileSpecialFlower) world.getTileEntity(pos);
            if (te != null && te.subTileName.equals("puredaisy")) {
                EnumHand curHand = EnumHand.MAIN_HAND;
                EnumFacing playerFacing = player.getHorizontalFacing().getOpposite();

                ItemStack item = player.getHeldItem(curHand);

                if (item.isEmpty() || !(item.getItem() instanceof ItemBlock)) {
                    curHand = EnumHand.OFF_HAND;
                    item = player.getHeldItem(curHand);
                }

                if (item.isEmpty()) return;

                if (!(item.getItem() instanceof ItemBlock)) return;

                if (world.isRemote) {
                    event.setCanceled(true);
                    event.setCancellationResult(EnumActionResult.SUCCESS);
                    return;
                }

                ItemBlock iBlock = (ItemBlock) item.getItem();

                float hitX = pos.getX();
                float hitY = pos.getY();
                float hitZ = pos.getZ();

                Block block = iBlock.getBlock();

                BlockPos start = pos.add(1, 0, 1);
                BlockPos stop = pos.add(-1, 0, -1);

                for (BlockPos potential : BlockPos.getAllInBox(stop, start)) {
                    if (potential.equals(pos)) continue;
                    if (player.getDistance((double) pos.getX(), (double) pos.getY(), (double) pos.getZ()) < 1.3 && player.getPosition().getY() == pos.getY())
                        continue;

                    IBlockState stateAt = world.getBlockState(potential);
                    Block blockAt = stateAt.getBlock();

                    if ((blockAt.isReplaceable(world, potential) || blockAt.isAir(stateAt, world, potential)) && block.canPlaceBlockAt(world, potential)) {
                        IBlockState placingState = block.getStateForPlacement(world, pos, playerFacing, hitX, hitY, hitZ, item.getMetadata(), player, curHand);
                        iBlock.placeBlockAt(item, player, world, potential, playerFacing, hitX, hitY, hitZ, placingState);
                        if (!player.capabilities.isCreativeMode) item.shrink(1);
                        event.setCanceled(true);
                        event.setCancellationResult(EnumActionResult.SUCCESS);
                        break;
                    }
                }
            }
        }
    }
}
