package io.github.empee.colonel;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.Suggestions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.lucko.commodore.CommodoreProvider;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.CompletableFuture;

@Getter
@RequiredArgsConstructor
public abstract class BrigadierManager<S> {

  private final JavaPlugin plugin;
  private final CommandDispatcher<S> dispatcher;

  protected boolean logging = true;

  public void register(LiteralArgumentBuilder<S> command) {
    var brigadierCmd = dispatcher.register(command);
    var bukkitCmd = new CommandAdapter<>(brigadierCmd, this);
    var status = getCommandMap().register(plugin.getName(), bukkitCmd);

    if (CommodoreProvider.isSupported()) {
      CommodoreProvider.getCommodore(plugin).register(CommodoreAdapter.mapToCompatibleNode(brigadierCmd));
    }

    if (status) {
      if (logging) {
        plugin.getLogger().info("The command '" + bukkitCmd.getName() + "' has been registered successfully");
      }
    } else {
      plugin.getLogger().warning("The command '" + bukkitCmd.getName() + "' has a conflict, use '" + plugin.getName() + ":" + bukkitCmd.getName() + "'");
    }
  }

  protected abstract S getSource(CommandSender source);

  protected void handleException(S source, Exception exception) {
    exception.printStackTrace();
  }

  public CompletableFuture<Suggestions> getSuggestions(CommandSender sender, String rawInput) {
    var input = dispatcher.parse(rawInput, getSource(sender));
    return dispatcher.getCompletionSuggestions(input);
  }

  private CommandMap getCommandMap() { //Must run paper
    return plugin.getServer().getCommandMap();
  }

}
