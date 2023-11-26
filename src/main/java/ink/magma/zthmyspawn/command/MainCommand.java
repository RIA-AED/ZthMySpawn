package ink.magma.zthmyspawn.command;

import ink.magma.zthmyspawn.ZthMySpawn;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MainCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage("ZthMySpawn - Zth 多主城插件实现 - MagmaBlock");
            return true;
        }
        if (args.length == 1) {
            if (args[0].equals("reload")) {
                if (!sender.hasPermission("zth.myspawn.admin")) {
                    sender.sendMessage("缺少 zth.myspawn.admin");
                    return true;
                }
                try {
                    JavaPlugin.getPlugin(ZthMySpawn.class).loadPlugin();
                    sender.sendMessage("已重载!");
                } catch (Exception e) {
                    sender.sendMessage("发生意外错误，请见后台");
                    throw new RuntimeException(e);
                }
            }
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command
            command, @NotNull String label, @NotNull String[] args) {
        return List.of("reload");
    }
}
