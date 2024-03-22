package io.github.empee.colonel;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;

import static com.mojang.brigadier.exceptions.CommandSyntaxException.BUILT_IN_EXCEPTIONS;

@UtilityClass
public class BrigadierExceptions {

  @RequiredArgsConstructor
  public enum Type {
    DOUBLE_TOO_LOW(BUILT_IN_EXCEPTIONS.doubleTooLow()),
    DOUBLE_TOO_HIGH(BUILT_IN_EXCEPTIONS.doubleTooHigh()),
    FLOAT_TOO_LOW(BUILT_IN_EXCEPTIONS.floatTooLow()),
    FLOAT_TOO_HIGH(BUILT_IN_EXCEPTIONS.floatTooHigh()),
    INTEGER_TOO_LOW(BUILT_IN_EXCEPTIONS.integerTooLow()),
    INTEGER_TOO_HIGH(BUILT_IN_EXCEPTIONS.integerTooHigh()),
    LONG_TOO_LOW(BUILT_IN_EXCEPTIONS.longTooLow()),
    LONG_TOO_HIGH(BUILT_IN_EXCEPTIONS.longTooHigh()),
    LITERAL_INCORRECT(BUILT_IN_EXCEPTIONS.literalIncorrect()),
    READER_EXPECTED_START_OF_QUOTE(BUILT_IN_EXCEPTIONS.readerExpectedStartOfQuote()),
    READER_EXPECTED_END_OF_QUOTE(BUILT_IN_EXCEPTIONS.readerExpectedEndOfQuote()),
    READER_INVALID_ESCAPE(BUILT_IN_EXCEPTIONS.readerInvalidEscape()),
    READER_INVALID_BOOL(BUILT_IN_EXCEPTIONS.readerInvalidBool()),
    READER_INVALID_INT(BUILT_IN_EXCEPTIONS.readerInvalidInt()),
    READER_INVALID_LONG(BUILT_IN_EXCEPTIONS.readerInvalidLong()),
    READER_INVALID_DOUBLE(BUILT_IN_EXCEPTIONS.readerInvalidDouble()),
    READER_EXPECTED_INT(BUILT_IN_EXCEPTIONS.readerExpectedInt()),
    READER_EXPECTED_LONG(BUILT_IN_EXCEPTIONS.readerExpectedLong()),
    READER_EXPECTED_DOUBLE(BUILT_IN_EXCEPTIONS.readerExpectedDouble()),
    READER_EXPECTED_BOOL(BUILT_IN_EXCEPTIONS.readerExpectedBool()),
    READER_EXPECTED_FLOAT(BUILT_IN_EXCEPTIONS.readerExpectedFloat()),
    READER_EXPECTED_SYMBOL(BUILT_IN_EXCEPTIONS.readerExpectedSymbol()),
    DISPATCHER_UNKNOWN_COMMAND(BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand()),
    DISPATCHER_UNKNOWN_ARGUMENT(BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument()),
    DISPATCHER_EXPECTED_ARGUMENT_SEPARATOR(BUILT_IN_EXCEPTIONS.dispatcherExpectedArgumentSeparator()),
    DISPATCHER_PARSE_EXCEPTION(BUILT_IN_EXCEPTIONS.dispatcherParseException()),
    PLAYER_NOT_FOUND(new DynamicCommandExceptionType(name -> new LiteralMessage("Player " + name + " not found"))),
    SENDER_NOT_ALLOWED(new SimpleCommandExceptionType(new LiteralMessage("You aren't allowed to use this command")));

    private final CommandExceptionType identifier;

    public static Type fromType(CommandExceptionType type) {
      for (var exception : values()) {
        if (exception.identifier == type) {
          return exception;
        }
      }

      throw new IllegalArgumentException("Unknown exception type: " + type);
    }
  }

  public static CommandSyntaxException createWithContext(Type type, StringReader reader) {
    var identifier = type.identifier;
    if (identifier instanceof SimpleCommandExceptionType) {
      return ((SimpleCommandExceptionType) identifier).createWithContext(reader);
    }

    return createWithContext(type, reader, "unknown");
  }

  public static CommandSyntaxException createWithContext(Type type, StringReader reader, Object arg1) {
    var identifier = type.identifier;
    if (identifier instanceof DynamicCommandExceptionType) {
      return ((DynamicCommandExceptionType) identifier).createWithContext(reader, arg1);
    }

    return createWithContext(type, reader, arg1, "unknown");
  }

  public static CommandSyntaxException createWithContext(Type type, StringReader reader, Object arg1, Object arg2) {
    var identifier = type.identifier;
    if (identifier instanceof Dynamic2CommandExceptionType) {
      return ((Dynamic2CommandExceptionType) identifier).createWithContext(reader, arg1, arg2);
    }

    return new CommandSyntaxException(type.identifier, new LiteralMessage("Unknown exception type: " + type), reader.getString(), reader.getCursor());
  }

  public static CommandSyntaxException create(Type type) {
    var identifier = type.identifier;
    if (identifier instanceof SimpleCommandExceptionType) {
      return ((SimpleCommandExceptionType) identifier).create();
    }

    return create(type, "unknown");
  }

  public static CommandSyntaxException create(Type type, Object arg1) {
    var identifier = type.identifier;
    if (identifier instanceof DynamicCommandExceptionType) {
      return ((DynamicCommandExceptionType) identifier).create(arg1);
    }

    return create(type, arg1, "unknown");
  }

  public static CommandSyntaxException create(Type type, Object arg1, Object arg2) {
    var identifier = type.identifier;
    if (identifier instanceof Dynamic2CommandExceptionType) {
      return ((Dynamic2CommandExceptionType) identifier).create(arg1, arg2);
    }

    return new CommandSyntaxException(type.identifier, new LiteralMessage("Unknown exception type: " + type));
  }

}
