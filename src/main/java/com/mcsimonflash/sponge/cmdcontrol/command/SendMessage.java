package com.mcsimonflash.sponge.cmdcontrol.command;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mcsimonflash.sponge.cmdcontrol.command.parser.SourceParser;
import com.mcsimonflash.sponge.teslalibs.command.*;
import com.mcsimonflash.sponge.teslalibs.command.arguments.Arguments;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

@Singleton
@Aliases({"sendmessage", "sm"})
@Permission("cmdcontrol.command.sendmessage.base")
public class SendMessage extends Command {

    @Inject
    private SendMessage(CommandService service) {
        super(service, Settings.create().arguments(
                SourceParser.PARSER.toElement("source"),
                Arguments.remainingStrings().map(TextSerializers.FORMATTING_CODE::deserialize).toElement("message")));
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        args.<CommandSource>getOne("source").get().sendMessage(args.<Text>getOne("message").get());
        return CommandResult.success();
    }

}