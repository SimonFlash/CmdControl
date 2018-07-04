package com.mcsimonflash.sponge.cmdcontrol.command;

import com.google.common.collect.BoundType;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mcsimonflash.sponge.cmdcontrol.CmdControl;
import com.mcsimonflash.sponge.cmdcontrol.core.CmdUtils;
import com.mcsimonflash.sponge.teslalibs.argument.Arguments;
import com.mcsimonflash.sponge.teslalibs.command.Aliases;
import com.mcsimonflash.sponge.teslalibs.command.Command;
import com.mcsimonflash.sponge.teslalibs.command.CommandService;
import com.mcsimonflash.sponge.teslalibs.command.Permission;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
@Aliases({"random", "rnd"})
@Permission("cmdcontrol.command.random.base")
public class Random extends Command {

    private static final java.util.Random RANDOM = new java.util.Random();
    private static final Pattern FORMAT = Pattern.compile("<@Random(?:([(\\[])(\\*|[-+]?[0-9]+),(\\*|[-+]?[0-9]+)([)\\]])|\\{((?:.+)(?:,.+)*)})>");

    @Inject
    private Random(Command.Settings settings) {
        super(settings.elements(Arguments.command().toElement("command"))
                .usage(CmdUtils.usage("/cmdcontrol random ", CmdUtils.info("Random", "Inserts random numbers or selections into a command.\n", "", "random, rnd\n", "cmdcontrol.command.random.base"),
                        CmdUtils.arg(true, "command", CmdUtils.info("Command", "The command to be executed\n", "Command (no forward slash)\n", "", "")))));
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String command = args.<String>getOne("command").get();
        Matcher matcher = FORMAT.matcher(command);
        try {
            while (matcher.find()) {
                if (matcher.group(5) != null) {
                    String[] split = matcher.group(5).split(",", -1);
                    command = command.replaceFirst(Pattern.quote(matcher.group(0)), split[RANDOM.nextInt(split.length)]);
                } else {
                    Range<Integer> range = Range.range(
                            matcher.group(2).equals("*") ? Integer.MIN_VALUE : Integer.parseInt(matcher.group(2)),
                            matcher.group(1).equals("(") ? BoundType.OPEN : BoundType.CLOSED,
                            matcher.group(3).equals("*") ? Integer.MAX_VALUE : Integer.parseInt(matcher.group(3)),
                            matcher.group(4).equals(")") ? BoundType.OPEN : BoundType.CLOSED)
                            .canonical(DiscreteDomain.integers());
                    command = command.replaceFirst(Pattern.quote(matcher.group(0)), String.valueOf(RANDOM.nextInt(range.upperEndpoint() - range.lowerEndpoint()) + range.lowerEndpoint()));
                }
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new CommandException(CmdControl.getMessage(src, "cmdcontrol.command.random.range.invalid-format", "range", matcher.group(), "error", e.getMessage()));
        }
        return CmdUtils.execute(src, command, 0);
    }

}