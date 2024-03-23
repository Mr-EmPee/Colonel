package io.github.empee.colonel.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.empee.colonel.BrigadierExceptions;
import io.github.empee.colonel.BrigadierExceptions.Type;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class EnumArgumentType<T extends Enum<T>> implements CustomArgumentType<T> {

  private final Class<T> enumClass;

  public static <T extends Enum<T>> EnumArgumentType<T> enumArg(Class<T> enumClass) {
    return new EnumArgumentType<>(enumClass);
  }

  @Override
  public T parse(StringReader reader) throws CommandSyntaxException {
    int start = reader.getCursor();
    String literal = reader.readString();

    try {
      return Enum.valueOf(enumClass, literal.toUpperCase());
    } catch (IllegalArgumentException e) {
      reader.setCursor(start);
      throw BrigadierExceptions.createWithContext(Type.LITERAL_INCORRECT, reader, literal);
    }
  }

  @Override
  public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
    for (T value : enumClass.getEnumConstants()) {
      builder.suggest(value.name());
    }

    return builder.buildFuture();
  }

  @Override
  public ArgumentType<?> getNmsType() {
    return StringArgumentType.string();
  }
}
