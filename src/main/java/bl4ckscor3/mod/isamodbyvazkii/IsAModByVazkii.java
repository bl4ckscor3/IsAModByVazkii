package bl4ckscor3.mod.isamodbyvazkii;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid=IsAModByVazkii.MOD_ID, name="...is a mod by Vazkii", version="v1.0", acceptedMinecraftVersions="[1.12]")
@EventBusSubscriber
public class IsAModByVazkii
{
	protected static final String MOD_ID = "isamodbyvazkii";
	private static final ArrayList<String> MODS = new ArrayList<String>();
	private static final HashMap<String,String> CACHE = new HashMap<String,String>();
	private static final String BLOCK = "isamodbyvazkii:block";
	private static final String ITEM = "isamodbyvazkii:item";
	private static final Logger LOGGER = Logger.getLogger(MOD_ID);

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("https://raw.githubusercontent.com/bl4ckscor3/IsAModByVazkii/master/modlist.txt").openStream())))
		{
			String line;

			while((line = reader.readLine()) != null)
			{
				MODS.add(line);
			}
		}
		catch(IOException e)
		{
			LOGGER.warning("Failed to download Vazkii's mod list. Check your internet connection.");
		}
	}

	@SubscribeEvent
	public static void onItemTooltip(ItemTooltipEvent event)
	{
		if(event.getToolTip().size() < 1)
			return;

		String name = event.getToolTip().get(0);
		ItemStack stack = event.getItemStack();

		if(!stack.isEmpty() && MODS.contains(stack.getItem().getRegistryName().getNamespace()))
		{
			String newName;

			if(!CACHE.containsKey(name))
			{
				String addition = "";
				String cachedName = name;

				if(Minecraft.getMinecraft().gameSettings.advancedItemTooltips && name.contains(" "))
				{
					addition = name.substring(name.lastIndexOf(" "), name.length());
					name = name.replace(addition, "");
				}

				newName = I18n.format(stack.getItem() instanceof ItemBlock ? BLOCK : ITEM, name) + addition;
				CACHE.put(cachedName, newName);
			}
			else
				newName = CACHE.get(name);

			event.getToolTip().set(0, newName);
		}
	}
}
