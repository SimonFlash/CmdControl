package com.mcsimonflash.sponge.cmdcontrol.command;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mcsimonflash.sponge.cmdcontrol.CmdControl;
import com.mcsimonflash.sponge.cmdcontrol.command.parser.SourceParser;
import com.mcsimonflash.sponge.cmdcontrol.core.CmdUtils;
import com.mcsimonflash.sponge.teslalibs.argument.Arguments;
import com.mcsimonflash.sponge.teslalibs.command.Aliases;
import com.mcsimonflash.sponge.teslalibs.command.Command;
import com.mcsimonflash.sponge.teslalibs.command.Permission;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.text.Text;

@Singleton
@Aliases({"execute", "ex"})
@Permission("cmdcontrol.command.execute.base")
public class Execute extends Command {

    @Inject
    private Execute(Command.Settings settings) {
        super(settings
                .elements(Arguments.flags()
                        .flag(Arguments.duration().toElement("delay"), "delay", "d")
                        .flag(SourceParser.PARSER.toElement("source"), "source", "s")
                        .build(), Arguments.command().toElement("command"))
                .usage(CmdUtils.usage("/cmdcontrol execute ", CmdUtils.info("Execute", "Executes a command from a source.\n", "", "execute, ex\n", "cmdcontrol.command.execute.base"),
                        CmdUtils.arg(false, "-delay", CmdUtils.info("Delay", "The delay for this execution\n", "Duration (10s800ms = 10 seconds, 800 milliseconds)\n", "-delay, -d\n", "cmdcontrol.command.execute.delay")),
                        CmdUtils.arg(false, "-source", CmdUtils.info("Source", "The source of the command\n", "CommandSource (a player or #console)\n", "-source, -s\n", "cmdcontrol.command.execute.other, cmdcontrol.command.execute.console")),
                        CmdUtils.arg(true, "command", CmdUtils.info("Command", "The command to be executed\n", "Command (no forward slash)\n", "", "")))));
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        CommandSource source = args.<CommandSource>getOne("source").orElse(src);
        if (source != src) {
            if (source instanceof ConsoleSource && !src.hasPermission("cmdcontrol.command.execute.console")) {
                throw new CommandException(CmdControl.getMessage(src, "cmdcontrol.command.execute.console.no-permission"));
            } else if (!src.hasPermission("cmdcontrol.command.execute.other")) {
                throw new CommandException(CmdControl.getMessage(src, "cmdcontrol.command.execute.other.no-permission"));
            }
        }
        long delay = args.<Long>getOne("delay").orElse(0L);
        if (delay > 0 && !src.hasPermission("cmdcontrol.command.execute.delay")) {
            throw new CommandException(CmdControl.getMessage(src, "cmdcontrol.command.execute.delay.no-permission"));
        }
        return CmdUtils.execute(source, args.<String>getOne("command").get(), delay);
    }

}