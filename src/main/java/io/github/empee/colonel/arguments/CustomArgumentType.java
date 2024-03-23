package io.github.empee.colonel.arguments;

import com.mojang.brigadier.arguments.ArgumentType;

/**
 * An argument type that can be translated to a vanilla default argument type.
 */

public interface CustomArgumentType<T> extends ArgumentType<T> {

  /**
   * Get the vanilla argument type that this argument type can be translated to.
   */
  ArgumentType<?> getNmsType();

}
