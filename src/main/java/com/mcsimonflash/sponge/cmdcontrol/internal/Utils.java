package com.mcsimonflash.sponge.cmdcontrol.internal;

import com.mcsimonflash.sponge.cmdcontrol.CmdControl;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.scheduler.Task;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class Utils {

    public static CommandResult execute(CommandSource src, String command, long delay) {
        if (delay > 0) {
            Task.builder()
                    .execute(t -> Sponge.getCommandManager().process(src, command))
                    .delay(delay, TimeUnit.MILLISECONDS)
                    .submit(CmdControl.getInstance());
            return CommandResult.queryResult(1);
        }
        return Sponge.getCommandManager().process(src, command);
    }

    public static Optional<URL> parseUrl(String url) {
        try {
            return Optional.of(new URL(url));
        } catch (MalformedURLException ignored) {}
        return Optional.empty();
    }

}