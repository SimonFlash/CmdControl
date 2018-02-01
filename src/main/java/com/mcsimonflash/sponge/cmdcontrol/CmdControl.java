package com.mcsimonflash.sponge.cmdcontrol;

import com.google.inject.Inject;
import com.mcsimonflash.sponge.cmdcontrol.command.Base;
import com.mcsimonflash.sponge.cmdcontrol.core.CmdPlugin;
import com.mcsimonflash.sponge.teslalibs.command.CommandService;
import org.slf4j.Logger;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.entity.Hotbar;
import org.spongepowered.api.item.inventory.entity.MainPlayerInventory;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.item.inventory.type.GridInventory;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import java.util.Optional;

@Plugin(id = "cmdcontrol", name = "CmdControl", version = "1.0.0", url = "https://github.com/SimonFlash/CmdControl/wiki", authors = "Simon_Flash")
public class CmdControl extends CmdPlugin {

    private static CmdControl instance;

    @Inject
    public CmdControl(PluginContainer container) {
        super(container);
        instance = this;
    }

    @Listener
    public void onInit(GameInitializationEvent event) {
        Logger.info("+=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=+");
        Logger.info("|     CmdControl -- Version 1.0.0     |");
        Logger.info("|      Developed By: Simon_Flash      |");
        Logger.info("+=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=+");
        Commands.register(Base.class);
    }

    public static CmdControl getInstance() {
        return instance;
    }

}