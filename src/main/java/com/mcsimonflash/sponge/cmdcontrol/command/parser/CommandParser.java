package com.mcsimonflash.sponge.cmdcontrol.command.parser;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mcsimonflash.sponge.teslalibs.command.arguments.Arguments;
import com.mcsimonflash.sponge.teslalibs.command.arguments.parser.StandardParser;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.world.Locatable;

import java.util.List;

public class CommandParser extends StandardParser<String> {

    public static final CommandParser PARSER = new CommandParser(ImmutableMap.of());

    public CommandParser(ImmutableMap<String, String> unused) {
        super(unused);
    }

    @Override
    public String parseValue(CommandSource src, CommandArgs args) throws ArgumentParseException {
        return Arguments.remainingStrings().parseValue(src, args);
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext ctx) {
        try {
            String command = Arguments.remainingStrings().parseValue(src,args);
            return Sponge.getCommandManager().getSuggestions(src, command, src instanceof Locatable ? ((Locatable) src).getLocation() : null);
        } catch (ArgumentParseException ignored) {}
        return ImmutableList.of();
    }

}