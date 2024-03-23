package io.github.empee.colonel.helpers;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;

public interface BrigadierExecutable<S> extends Command<S> {

  void execute(CommandContext<S> context);

  @Override
  default int run(CommandContext<S> context) {
    execute(context);
    return 1;
  }
}
