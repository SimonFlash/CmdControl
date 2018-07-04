package com.mcsimonflash.sponge.cmdcontrol.core;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mcsimonflash.sponge.teslalibs.command.CommandService;
import com.mcsimonflash.sponge.teslalibs.message.MessageService;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

public abstract class CmdPlugin {

    public static final Optional<URL> DISCORD = CmdUtils.parseURL("https://discord.gg/4wayq37");
    private static final Map<String, CmdPlugin> REGISTRY = Maps.newHashMap();

    private final PluginContainer container;
    private final Text prefix;
    private final Path directory;
    private final Logger logger;
    private final CommandService commands;
    private final MessageService messages;

    public CmdPlugin(PluginContainer container) {
        this.container = container;
        prefix = CmdUtils.toText("&c[&6" + container.getName() + "&c]&7 ");
        directory = Sponge.getConfigManager().getPluginConfig(container).getDirectory();
        logger = container.getLogger();
        commands = CommandService.of(container);
        messages = getMessageService();
        REGISTRY.put(container.getId(), this);
    }

    private MessageService getMessageService() {
        Path translations = directory.resolve("translations");
        try {
            container.getAsset("messages.properties").get().copyToDirectory(translations);
            return MessageService.of(translations, "messages");
        } catch (IOException e) {
            logger.error("An error occurred initializing message translations. Using internal copies.");
            return MessageService.of(container, "messages");
        }
    }

    public PluginContainer getContainer() {
        return container;
    }

    public Text getPrefix() {
        return prefix;
    }

    public Path getDirectory() {
        return directory;
    }

    public Logger getLogger() {
        return logger;
    }

    public MessageService getMessages() {
        return messages;
    }

    public CommandService getCommands() {
        return commands;
    }

    public static ImmutableMap<String, CmdPlugin> getRegistry() {
        return ImmutableMap.copyOf(REGISTRY);
    }

}