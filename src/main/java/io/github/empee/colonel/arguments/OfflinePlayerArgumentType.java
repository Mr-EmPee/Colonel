package io.github.empee.colonel.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor(staticName = "offlinePlayerArg")
public class OfflinePlayerArgumentType implements CustomArgumentType<OfflinePlayer> {

  @Override
  public OfflinePlayer parse(StringReader reader) throws CommandSyntaxException {
    return Bukkit.getOfflinePlayer(reader.readString());
  }

  @Override
  public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
    for (var value : Bukkit.getOnlinePlayers()) {
      builder.suggest(value.getName());
    }

    return builder.buildFuture();
  }

  @Override
  public ArgumentType<?> getNmsType() {
    return StringArgumentType.string();
  }
}
