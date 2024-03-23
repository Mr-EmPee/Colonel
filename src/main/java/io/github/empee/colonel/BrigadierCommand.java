package io.github.empee.colonel;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import io.github.empee.colonel.BrigadierExceptions.Type;
import lombok.SneakyThrows;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public abstract class BrigadierCommand<S> {

  public BrigadierCommand(BrigadierManager<S> manager) {
    manager.register(get());
  }

  public abstract LiteralArgumentBuilder<S> get();

  protected LiteralArgumentBuilder<S> literal(String name) {
    return LiteralArgumentBuilder.literal(name);
  }

  protected <T> RequiredArgumentBuilder<S, T> arg(String name, ArgumentType<T> type) {
    return RequiredArgumentBuilder.argument(name, type);
  }

  protected SuggestionProvider<S> suggestions(String... suggestions) {
    return (c, b) -> {
      for (String suggestion : suggestions) {
        b.suggest(suggestion);
      }

      return b.buildFuture();
    };
  }

  @SneakyThrows
  protected Player player(CommandContext<S> context) {
    if (context.getSource() instanceof Player) {
      return (Player) context.getSource();
    }

    throw BrigadierExceptions.create(Type.SENDER_NOT_ALLOWED);
  }

  @SneakyThrows
  protected ConsoleCommandSender console(CommandContext<S> context) {
    if (context.getSource() instanceof ConsoleCommandSender) {
      return (ConsoleCommandSender) context.getSource();
    }

    throw BrigadierExceptions.create(Type.SENDER_NOT_ALLOWED);
  }

  @SneakyThrows
  protected Entity entity(CommandContext<S> context) {
    if (context.getSource() instanceof Entity) {
      return (Entity) context.getSource();
    }

    throw BrigadierExceptions.create(Type.SENDER_NOT_ALLOWED);
  }

}
