package io.github.empee.colonel.helpers;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class BrigadierCompositor<S> {

  @SafeVarargs
  public static <S> BrigadierCompositor<S> compositionOf(ArgumentBuilder<S, ?>... args) {
    return new BrigadierCompositor<>(args);
  }

  private List<ArgumentBuilder<S, ?>> args = new ArrayList<>();

  public BrigadierCompositor(ArgumentBuilder<S, ?>... args) {
    this.args.addAll(List.of(args));
  }

  public BrigadierCompositor<S> then(ArgumentBuilder<S, ?> arg) {
    args.add(arg);
    return this;
  }

  public ArgumentBuilder<S, ?> build() {
    var start = args.get(0);
    for (int i = 1; i < args.size(); i++) {
      start.then(args.get(i));
      start = args.get(i);
    }

    return args.get(0);
  }

  public BrigadierCompositor<S> executes(BrigadierExecutable<S> command) {
    lastNode().executes(command);
    return this;
  }

  public BrigadierCompositor<S> requires(Predicate<S> predicate) {
    lastNode().requires(predicate);
    return this;
  }

  public BrigadierCompositor<S> redirect(CommandNode<S> node) {
    lastNode().redirect(node);
    return this;
  }

  private ArgumentBuilder<S, ?> lastNode() {
    return args.get(args.size() - 1);
  }

}