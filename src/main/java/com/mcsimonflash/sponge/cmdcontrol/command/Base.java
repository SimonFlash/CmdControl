package com.mcsimonflash.sponge.cmdcontrol.command;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mcsimonflash.sponge.cmdcontrol.CmdControl;
import com.mcsimonflash.sponge.cmdcontrol.core.CmdPlugin;
import com.mcsimonflash.sponge.cmdcontrol.core.CmdUtils;
import com.mcsimonflash.sponge.teslalibs.command.*;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
@Aliases({"cmdcontrol", "cc"})
@Permission("cmdcontrol.command.base")
@Children({Execute.class, Message.class, Random.class})
public class Base extends Command {

    private static final Text LINKS = Text.of("                      ", CmdUtils.link("Ore Project", CmdControl.get().getContainer().getUrl().flatMap(CmdUtils::parseURL)), TextColors.GRAY, " | ", CmdUtils.link("Support Discord", CmdPlugin.DISCORD));

    @Inject
    private Base(Command.Settings settings) {
        super(settings.usage(CmdUtils.usage("/cmdcontrol ", CmdUtils.info("CmdControl", "Opens the command reference menu.\n", "", "cmdcontrol, cc\n", "cmdcontrol.command.base"),
                CmdUtils.arg(true, "...", CmdUtils.info("Subcommand", "One of CmdControl's subcommands.\n", "execute, random, sendmessage\n", "", "")))));
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        PaginationList.builder()
                .padding(Text.of(TextColors.GRAY, "="))
                .title(CmdControl.get().getPrefix())
                .contents(Stream.concat(Stream.of(this), getChildren().stream()).map(Command::getUsage).collect(Collectors.toList()))
                .footer(LINKS)
                .sendTo(src);
        return CommandResult.success();
    }

}