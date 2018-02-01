package com.mcsimonflash.sponge.cmdcontrol.core;

import com.google.common.collect.Maps;
import com.mcsimonflash.sponge.cmdcontrol.internal.Utils;
import com.mcsimonflash.sponge.teslalibs.command.CommandService;
import com.mcsimonflash.sponge.teslalibs.message.MessageService;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

public abstract class CmdPlugin {

    private static final Map<String, CmdPlugin> REGISTRY = Maps.newHashMap();
    public static final Optional<URL> Discord = Utils.parseUrl("https://discord.gg/4wayq37");

    public final PluginContainer Container;
    public final String Id;
    public final String Name;
    public final String Version;
    public final String Description;
    public final Optional<URL> URL;
    public final Path Directory;
    public final Logger Logger;
    public final CommandService Commands;
    public final MessageService Messages;
    public final Text Prefix;

    public CmdPlugin(PluginContainer container) {
        Container = container;
        Id = Container.getId();
        Name = Container.getName();
        Version = Container.getVersion().orElse("undefined");
        Description = Container.getDescription().orElse("undefined");
        URL = Utils.parseUrl(Container.getUrl().orElse("https://github.com/SimonFlash"));
        Directory = Sponge.getConfigManager().getPluginConfig(Container).getDirectory();
        Logger = Container.getLogger();
        Commands = CommandService.of(container);
        MessageService ms;
        try {
            Path translations = Directory.resolve("translations");
            Container.getAsset("messages.properties").get().copyToDirectory(translations, false, true);
            ms = MessageService.of(translations, "messages");
        } catch (IOException e) {
            Logger.error("An error occurred initializing message translations. Using internal copies.");
            ms = MessageService.of(getClass().getClassLoader(), "assets/" + Id + "/messages");
        }
        Messages = ms;
        Prefix = Text.of(TextColors.DARK_GRAY, "[", TextColors.GRAY, Name, TextColors.DARK_GRAY, "]", TextColors.GRAY, " ");
        REGISTRY.put(Id, this);
    }

}