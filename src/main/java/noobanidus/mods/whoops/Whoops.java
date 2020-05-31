package noobanidus.mods.whoops;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DirectionalPlaceContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vazkii.botania.common.block.ModSubtiles;

@Mod("whoops")
@SuppressWarnings("WeakerAccess")
public class Whoops {
  public static final String MODID = "whoops";

  @SuppressWarnings("unused")
  public final static Logger LOG = LogManager.getLogger(MODID);

  public Whoops() {
    MinecraftForge.EVENT_BUS.addListener(Whoops::onDaisyRightClick);
  }

  public static void onDaisyRightClick(PlayerInteractEvent.RightClickBlock event) {
    World world = event.getWorld();

    if (event.getHand() != Hand.MAIN_HAND) {
      return;
    }

    PlayerEntity player = event.getPlayer();
    if (player instanceof FakePlayer || player.isSneaking()) {
      return;
    }

    if (event.getUseBlock() == Event.Result.DENY || event.getUseItem() == Event.Result.DENY) {
      return;
    }

    BlockPos pos = event.getPos();

    Block flowerBlock = world.getBlockState(pos).getBlock();
    if (flowerBlock == ModSubtiles.pureDaisy || flowerBlock == ModSubtiles.pureDaisyFloating) {
      Hand curHand = event.getHand();

      ItemStack item = player.getHeldItem(curHand);

      if (item.isEmpty() || !(item.getItem() instanceof BlockItem)) {
        return;
      }

      if (world.isRemote) {
        event.setCanceled(true);
        event.setCancellationResult(ActionResultType.SUCCESS);
        return;
      }

      BlockItem iBlock = (BlockItem) item.getItem();

      Block block = iBlock.getBlock();

      for (int x = -1; x <= 1; x++) {
        for (int z = -1; z <= 1; z++) {
          BlockPos potential = pos.add(x, 0, z);
          if (potential.equals(pos)) {
            continue;
          }

          if (player.getDistanceSq((double) potential.getX(), (double) potential.getY(), (double) potential.getZ()) < 1.1 && player.getPosition().getY() == potential.getY()) {
            continue;
          }

          BlockState stateAt = world.getBlockState(potential);
          Block blockAt = stateAt.getBlock();
          BlockState state = block.getDefaultState();

          DirectionalPlaceContext context = new DirectionalPlaceContext(world, potential, Direction.DOWN, item, Direction.DOWN);

          if ((stateAt.isReplaceable(context) || blockAt.isAir(stateAt, world, potential)) && state.isValidPosition(world, potential)) {
            iBlock.tryPlace(context);
            event.setCanceled(true);
            event.setCancellationResult(ActionResultType.SUCCESS);
            return;
          }
        }
      }
    }
  }
}
