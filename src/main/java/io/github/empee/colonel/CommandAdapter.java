package io.github.empee.colonel;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.tree.LiteralCommandNode;
import lombok.Builder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

class CommandAdapter<S> extends Command {
  private final CommandDispatcher<S> dispatcher;
  private final LiteralCommandNode<S> command;
  private final BrigadierManager<S> manager;

  @Builder
  CommandAdapter(LiteralCommandNode<S> command, BrigadierManager<S> manager) {
    super(command.getName());

    this.command = command;
    this.manager = manager;
    this.dispatcher = manager.getDispatcher();
  }

  @Override
  public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
    var source = manager.getSource(sender);

    try {
      var cmd = getRawInput(commandLabel, args);

      dispatcher.execute(cmd, source);
    } catch (Exception e) {
      manager.handleException(source, e);
    }

    return true;
  }

  private static String getRawInput(@NotNull String label, @NotNull String[] args) {
    var cmd = label;
    var joinedArgs = String.join(" ", args);
    if (!joinedArgs.isEmpty()) {
      cmd = String.join(" ", cmd, joinedArgs);
    }

    return cmd;
  }

  @Override
  public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
    return manager.getSuggestions(sender, getRawInput(alias, args))
        .join().getList().stream()
        .map(Suggestion::getText)
        .toList();
  }

}
