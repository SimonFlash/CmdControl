package com.mcsimonflash.sponge.cmdcontrol;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.mcsimonflash.sponge.cmdcontrol.command.Base;
import com.mcsimonflash.sponge.cmdcontrol.core.CmdPlugin;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

@Plugin(id = "cmdcontrol", name = "CmdControl", version = "1.2.0", url = "https://ore.spongepowered.org/Simon_Flash/CmdControl", authors = "Simon_Flash")
public class CmdControl extends CmdPlugin {

    private static CmdControl instance;

    @Inject
    public CmdControl(PluginContainer container) {
        super(container);
        instance = this;
    }

    @Listener(order = Order.FIRST)
    public void onInit(GameInitializationEvent event) {
        getLogger().info("+=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=+");
        getLogger().info("|      /Cmd Series - Simon_Flash      |");
        getLogger().info("|                                     |");
        CmdPlugin.getRegistry().values().forEach(p -> {
            String message = p.getContainer().getId() + ": v" + p.getContainer().getVersion().orElse("unknown");
            getLogger().info("|        - " + message + Strings.repeat(" ", 27 - message.length()) + "|");
        });
        getLogger().info("+=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=+");
        getCommands().register(Base.class);
    }

    public static CmdControl get() {
        return instance;
    }

    public static Text getMessage(CommandSource src, String key, Object... args) {
        return instance.getPrefix().concat(instance.getMessages().get(key, src.getLocale()).args(args).toText());
    }

}