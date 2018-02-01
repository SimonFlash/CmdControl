package com.mcsimonflash.sponge.cmdcontrol.command;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mcsimonflash.sponge.cmdcontrol.CmdControl;
import com.mcsimonflash.sponge.cmdcontrol.core.CmdPlugin;
import com.mcsimonflash.sponge.teslalibs.command.*;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.List;

@Singleton
@Children({Execute.class, SendMessage.class})
@Aliases({"cmdcontrol", "cc"})
@Permission("cmdcontrol.command.base")
public class Base extends Command {

    @Inject
    private Base(CommandService service) {
        super(service, Settings.create());
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        List<Text> commands = Lists.newArrayList(CMDCONTROL);
        if (src.hasPermission("cmdcontrol.command.execute.base")) {
            commands.add(EXECUTE);
        }
        if (src.hasPermission("cmdcontrol.command.sendmessage.base")) {
            commands.add(SEND_MESSAGE);
        }
        Text wikiDisc = null;
        if (CmdControl.getInstance().URL.isPresent() && CmdPlugin.Discord.isPresent()) {
            wikiDisc = Text.builder("| ")
                    .color(TextColors.WHITE)
                    .append(Text.builder("ActiveTime Wiki")
                            .color(TextColors.AQUA).style(TextStyles.UNDERLINE)
                            .onClick(TextActions.openUrl(CmdControl.getInstance().URL.get()))
                            .onHover(TextActions.showText(Text.of("Click to open the ActiveTime Wiki")))
                            .build())
                    .append(Text.of(TextColors.WHITE, " | "))
                    .append(Text.builder("Support Discord")
                            .color(TextColors.AQUA).style(TextStyles.UNDERLINE)
                            .onClick(TextActions.openUrl(CmdPlugin.Discord.get()))
                            .onHover(TextActions.showText(Text.of("Click to open the Support Discord")))
                            .build())
                    .append(Text.of(TextColors.WHITE, " |"))
                    .build();
        }
        PaginationList.builder()
                .padding(Text.of(TextColors.AQUA, "="))
                .title(CmdControl.getInstance().Prefix)
                .contents(commands)
                .footer(wikiDisc)
                .sendTo(src);
        return CommandResult.success();
    }

    private static final Text CMDCONTROL = Text.builder("/cmdcontrol ")
            .color(TextColors.GRAY)
            .onClick(TextActions.suggestCommand("/cmdcontrol "))
            .onHover(TextActions.showText(Text.of(
                    TextColors.GRAY, "CmdControl: ", TextColors.WHITE, "Opens command reference menu\n",
                    TextColors.GRAY, "Aliases: ", TextColors.WHITE, "cmdcontrol, cc\n",
                    TextColors.GRAY, "Permission: ", TextColors.WHITE, "cmdcontrol.command.base")))
            .build();
    private static final Text EXECUTE = Text.builder("/cmdcontrol execute ")
            .color(TextColors.GRAY)
            .onClick(TextActions.suggestCommand("/cmdcontrol execute "))
            .onHover(TextActions.showText(Text.of(
                    TextColors.GRAY, "Execute: ", TextColors.WHITE, "Opens command reference menu\n",
                    TextColors.GRAY, "Aliases: ", TextColors.WHITE, "execute, ex\n",
                    TextColors.GRAY, "Permission: ", TextColors.WHITE, "cmdcontrol.command.execute.base")))
            .append(Text.builder("[-delay] ")
                    .color(TextColors.WHITE)
                    .onClick(TextActions.suggestCommand("/cmdcontrol execute -delay "))
                    .onHover(TextActions.showText(Text.of(
                            TextColors.GRAY, "Delay: ", TextColors.WHITE, "The delay for this execution\n",
                            TextColors.GRAY, "Type: ", TextColors.WHITE, "Duration (10s800ms = 10 seconds, 800 millisecons)\n",
                            TextColors.GRAY, "Aliases: ", TextColors.WHITE, "-delay, -d\n",
                            TextColors.GRAY, "Permission: ", TextColors.WHITE, "cmdcontrol.command.execute.delay\n")))
                    .build())
            .append(Text.builder("[-source] ")
                    .color(TextColors.WHITE)
                    .onClick(TextActions.suggestCommand("/cmdcontrol execute -delay "))
                    .onHover(TextActions.showText(Text.of(
                            TextColors.GRAY, "Source: ", TextColors.WHITE, "The source of the command\n",
                            TextColors.GRAY, "Type: ", TextColors.WHITE, "CommandSource (a player, #console, or #server)\n",
                            TextColors.GRAY, "Aliases: ", TextColors.WHITE, "-source, -s\n",
                            TextColors.GRAY, "Permission: ", TextColors.WHITE, "cmdcontrol.command.execute.other, cmdcontrol.command.execute.console\n")))
                    .build())
            .append(Text.builder("<command>")
                    .color(TextColors.WHITE)
                    .onClick(TextActions.suggestCommand("/cmdcontrol execute "))
                    .onHover(TextActions.showText(Text.of(
                            TextColors.GRAY, "Command: ", TextColors.WHITE, "The command to be executed\n",
                            TextColors.GRAY, "Type: ", TextColors.WHITE, "Command (without the forward slash)\n")))
                    .build())
            .build();
    private static final Text SEND_MESSAGE = Text.builder("/cmdcontrol sendmessage ")
            .color(TextColors.GRAY)
            .onClick(TextActions.suggestCommand("/cmdcontrol sendmessage "))
            .onHover(TextActions.showText(Text.of(
                    TextColors.GRAY, "SendMessage: ", TextColors.WHITE, "Sends an un-prefixed message.\n",
                    TextColors.GRAY, "Aliases: ", TextColors.WHITE, "sendmessage, sm\n",
                    TextColors.GRAY, "Permission: ", TextColors.WHITE, "cmdcontrol.command.sendmessage.base")))
            .append(Text.builder("<source> ")
                    .color(TextColors.WHITE)
                    .onClick(TextActions.suggestCommand("/cmdcontrol sendmessage "))
                    .onHover(TextActions.showText(Text.of(
                            TextColors.GRAY, "Source: ", TextColors.WHITE, "The source to receive the message\n",
                            TextColors.GRAY, "Type: ", TextColors.WHITE, "CommandSource (a player, #console, or #server)\n")))
                    .build())
            .append(Text.builder("<message>")
                    .color(TextColors.WHITE)
                    .onClick(TextActions.suggestCommand("/cmdcontrol sendmessage "))
                    .onHover(TextActions.showText(Text.of(
                            TextColors.GRAY, "Message: ", TextColors.WHITE, "The message to be sent\n",
                            TextColors.GRAY, "Type: ", TextColors.WHITE, "Text (supports colors/formatting)\n")))
                    .build())
            .build();

}