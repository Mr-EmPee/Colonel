package io.github.empee.colonel.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import lombok.RequiredArgsConstructor;
import io.github.empee.colonel.BrigadierExceptions;
import io.github.empee.colonel.BrigadierExceptions.Type;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor(staticName = "playerArg")
public class PlayerArgumentType implements CustomArgumentType<Player> {
  @Override
  public Player parse(StringReader reader) throws CommandSyntaxException {
    int start = reader.getCursor();

    String playerName = reader.readString();
    Player player = Bukkit.getPlayer(playerName);

    if (player == null) {
      reader.setCursor(start);
      throw BrigadierExceptions.createWithContext(Type.PLAYER_NOT_FOUND, reader, playerName);
    }

    return player;
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
