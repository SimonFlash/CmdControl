package com.mcsimonflash.sponge.cmdcontrol.command;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mcsimonflash.sponge.cmdcontrol.command.parser.CommandParser;
import com.mcsimonflash.sponge.cmdcontrol.command.parser.SourceParser;
import com.mcsimonflash.sponge.cmdcontrol.internal.Utils;
import com.mcsimonflash.sponge.teslalibs.command.*;
import com.mcsimonflash.sponge.teslalibs.command.arguments.Arguments;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.text.Text;

import java.util.List;

@Singleton
@Aliases({"execute", "ex"})
@Permission("cmdcontrol.command.execute.base")
public class Execute extends Command {

    @Inject
    private Execute(CommandService service) {
        super(service, Settings.create().arguments(Arguments.flags()
                .flag(Arguments.duration().toElement("delay"), "delay", "d")
                .flag(SourceParser.PARSER.toElement("source"), "source", "s")
                .build(), CommandParser.PARSER.toElement("command")));
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        CommandSource source = args.<CommandSource>getOne("source").orElse(src);
        if (source != src) {
            if (source instanceof ConsoleSource && !src.hasPermission("cmdcontrol.command.execute.console")) {
                throw new CommandException(Text.of("You do not have permission to execute a command from console."));
            } else if (!src.hasPermission("cmdcontrol.command.execute.other")) {
                throw new CommandException(Text.of("You do not have permission to execute a command from another player."));
            }
        }
        Long delay = args.<Long>getOne("delay").orElse(0L);
        if (delay > 0 && !src.hasPermission("cmdcontrol.command.execute.delay")) {
            throw new CommandException(Text.of("You do not have permission to execute a command with a delay."));
        }
        return Utils.execute(source, args.<String>getOne("command").get(), delay);
    }

}