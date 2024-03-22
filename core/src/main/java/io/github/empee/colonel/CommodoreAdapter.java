package io.github.empee.colonel;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import lombok.experimental.UtilityClass;
import io.github.empee.colonel.arguments.CustomArgumentType;

@UtilityClass
public class CommodoreAdapter {

  @SuppressWarnings({"unchecked", "rawtypes"})
  public <T extends CommandNode<?>> T mapToCompatibleNode(T node) {
    var mappedNode = node.createBuilder();

    if (node instanceof ArgumentCommandNode) {
      var type = ((ArgumentCommandNode) node).getType();
      if (type instanceof CustomArgumentType) {
        mappedNode = mapToVanillaArgument(node, (CustomArgumentType<?>) type);
      }
    }

    for (CommandNode child : node.getChildren()) {
      mappedNode.then(mapToCompatibleNode(child));
    }

    return (T) mappedNode.build();
  }

  private static ArgumentBuilder<?, ?> mapToVanillaArgument(
      CommandNode<?> brigadierCmd, CustomArgumentType<?> type
  ) {
    var node = RequiredArgumentBuilder.argument(brigadierCmd.getName(), type.getNmsType());
    node.requires(node.getRequirement());
    node.forward(node.getRedirect(), node.getRedirectModifier(), node.isFork());

    //Not sure if needed, but it's better to be safe
    node.executes(o -> {
      throw new UnsupportedOperationException();
    });

    node.suggests((o, o2) -> {
      throw new UnsupportedOperationException();
    });

    return node;
  }

}
