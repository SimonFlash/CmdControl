package com.mcsimonflash.sponge.cmdcontrol.command.parser;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mcsimonflash.sponge.teslalibs.argument.parser.StandardParser;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.User;

import java.util.List;
import java.util.stream.Stream;

public class SourceParser extends StandardParser<CommandSource> {

    public static final SourceParser PARSER = new SourceParser(ImmutableMap.of());

    private static final ImmutableList<String> MODIFIERS = ImmutableList.of("#me", "#self", "#console", "#server");

    public SourceParser(ImmutableMap<String, String> messages) {
        super(messages);
    }

    @Override
    public CommandSource parseValue(CommandSource src, CommandArgs args) throws ArgumentParseException {
        String arg = args.next();
        if (arg.startsWith("#")) {
            switch (arg.toLowerCase()) {
                case "#me": case "#self":
                    return src;
                case "#console": case "#server":
                    return Sponge.getServer().getConsole();
                default:
                    throw args.createError(getMessage("unknown-modifier", "No known modifier with the name <arg>.", "arg", arg));
            }
        }
        return Sponge.getServer().getPlayer(arg).orElseThrow(() -> args.createError(getMessage("no-player", "No player found with name <arg>.", "arg", arg)));
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext ctx) {
        return complete(args, Stream.concat(MODIFIERS.stream(), Sponge.getServer().getOnlinePlayers().stream().map(User::getName)));
    }

}