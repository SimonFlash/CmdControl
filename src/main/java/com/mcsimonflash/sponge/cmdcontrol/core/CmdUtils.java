package com.mcsimonflash.sponge.cmdcontrol.core;

import com.mcsimonflash.sponge.cmdcontrol.CmdControl;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class CmdUtils {

    public static Text toText(String msg) {
        return TextSerializers.FORMATTING_CODE.deserialize(msg);
    }

    public static CommandResult execute(CommandSource src, String command, long delay) {
        if (delay > 0) {
            Task.builder()
                    .execute(t -> Sponge.getCommandManager().process(src, command))
                    .delay(delay, TimeUnit.MILLISECONDS)
                    .submit(CmdControl.get());
            return CommandResult.queryResult(1);
        }
        return Sponge.getCommandManager().process(src, command);
    }

    public static Optional<URL> parseURL(String url) {
        try {
            return Optional.of(new URL(url));
        } catch (MalformedURLException ignored) {
            return Optional.empty();
        }
    }

    public static Text usage(String cmd, Text info, Text... args) {
        return Text.builder(cmd)
                .color(TextColors.GOLD)
                .onClick(TextActions.suggestCommand(cmd))
                .onHover(TextActions.showText(info))
                .append(Text.joinWith(Text.of(" "), args))
                .build();
    }

    public static Text info(String name, String desc, String type, String aliases, String permission) {
        return Text.of(desc(name, desc), desc("Type", type), desc("Aliases", aliases), desc("Permission", permission));
    }

    public static Text desc(String name, String desc) {
        return desc.isEmpty() ? Text.EMPTY : Text.of(name, TextColors.DARK_GRAY, ": ", TextColors.GRAY, desc);
    }

    public static Text arg(boolean req, String arg, Text info) {
        return Text.builder((req ? "<" : "[") + arg + (req ? ">" : "]"))
                .color(TextColors.RED)
                .onHover(TextActions.showText(info))
                .build();
    }

    public static Text link(String name, Optional<URL> optUrl) {
        return optUrl.map(u -> Text.builder(name)
                .color(TextColors.WHITE)
                .onClick(TextActions.openUrl(u))
                .onHover(TextActions.showText(Text.of(u)))
                .build()).orElseGet(() -> Text.builder(name)
                .color(TextColors.RED)
                .onHover(TextActions.showText(Text.of("Sorry! This URL is unavailable.")))
                .build());
    }

}