package com.mcsimonflash.sponge.cmdcontrol.command;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mcsimonflash.sponge.cmdcontrol.command.parser.SourceParser;
import com.mcsimonflash.sponge.cmdcontrol.core.CmdUtils;
import com.mcsimonflash.sponge.teslalibs.argument.Arguments;
import com.mcsimonflash.sponge.teslalibs.command.Aliases;
import com.mcsimonflash.sponge.teslalibs.command.Command;
import com.mcsimonflash.sponge.teslalibs.command.CommandService;
import com.mcsimonflash.sponge.teslalibs.command.Permission;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
@Aliases({"message", "msg"})
@Permission("cmdcontrol.command.message.base")
public class Message extends Command {

    private static final Pattern MODIFIERS = Pattern.compile("\\[(.+?)]\\((.+?)\\)");

    @Inject
    private Message(Command.Settings settings) {
        super(settings
                .elements(
                        SourceParser.PARSER.toElement("source"),
                        Arguments.remainingStrings().toElement("message"))
                .usage(CmdUtils.usage("/cmdcontrol message ", CmdUtils.info("Message", "Sends an un-prefixed message to a source.\n", "", "message, msg\n", "cmdcontrol.command.message.base"),
                        CmdUtils.arg(true, "source", CmdUtils.info("Source", "The source to receive the message\n", "CommandSource (player, #console, or #server)\n", "", "")),
                        CmdUtils.arg(true, "message", CmdUtils.info("Message", "The message to be sent\n", "Text (supports legacy (&) formatting)\n", "", "")))));
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        String message = args.<String>getOne("message").get();
        Matcher matcher = MODIFIERS.matcher(message);
        Text.Builder builder = Text.builder();
        int index = 0;
        while (matcher.find()) {
            if (matcher.start() > index) {
                builder.append(CmdUtils.toText(message.substring(index, matcher.start())));
            }
            Text.Builder subtext = CmdUtils.toText(matcher.group(1)).toBuilder();
            String group = matcher.group(2);
            try {
                subtext.onClick(group.startsWith("/") ? TextActions.runCommand(group) : TextActions.openUrl(new URL(group)));
                subtext.onHover(TextActions.showText(Text.of(group)));
            } catch (MalformedURLException e) {
                subtext.onHover(TextActions.showText(CmdUtils.toText(group)));
            }
            builder.append(subtext.build());
            index = matcher.end();
            if (matcher.hitEnd() && index < message.length()) {
                builder.append(CmdUtils.toText(message.substring(index)));
            }
        }
        if (index == 0) {
            builder.append(CmdUtils.toText(message));
        }
        args.<CommandSource>getOne("source").get().sendMessage(builder.build());
        return CommandResult.success();
    }

}