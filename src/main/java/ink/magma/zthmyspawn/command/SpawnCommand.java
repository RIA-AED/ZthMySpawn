package ink.magma.zthmyspawn.command;

import ink.magma.zthmyspawn.ZthMySpawn;
import net.william278.huskhomes.user.OnlineUser;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;

public class SpawnCommand implements TabExecutor {
    Configuration config = ZthMySpawn.instance.getConfig();

    @Override
    @ParametersAreNonnullByDefault
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage("控制台 spawn 个啥");
            return true;
        }


        // 主城列表
        ConfigurationSection spawnList = config.getConfigurationSection("spawn-list");
        if (spawnList == null) {
            sender.sendMessage(ChatColor.GRAY + "发生内部错误: 主城 config 列表为 null");
            sender.sendMessage(ChatColor.GRAY + "如果您认为这是一个 BUG, 请反馈至技术组");
            return true;
        }

        // 未指定主城
        if (args.length == 0) {
            // 遍历主城列表 如果有权限就进行传送
            for (Map.Entry<String, Object> spawn : spawnList.getValues(false).entrySet()) {
                if (sender.hasPermission("multispawn.spawn." + spawn.getKey())) {
                    doWarp(sender, (String) spawn.getValue());
                    return true;
                }
            }
            // 所有主城都没权限
            sender.sendMessage(ChatColor.GRAY + "似乎您没有一个默认主城... 请尝试重新设置您的主城");
            sender.sendMessage(ChatColor.GRAY + "如果您认为这是一个 BUG, 请反馈至技术组");
            return true;
        }
        // 指定了主城的逻辑
        if (args.length == 1) {
            String spawnName = spawnList.getString(args[0], "参数异常");
            doWarp(sender, spawnName);
            return true;
        }

        return true;
    }

    @Override
    @ParametersAreNonnullByDefault
    public @Nullable List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return ZthMySpawn.spawnList;
    }

    @ParametersAreNonnullByDefault
    public void doWarp(CommandSender player, String warpName) {
        ZthMySpawn.huskHomesAPI.getWarp(warpName)
                .thenAcceptAsync(result -> {
                    if (result.isEmpty()) { // warp 不存在
                        player.sendMessage(ChatColor.GRAY + "找不到主城 \"" + warpName + "\"...");
                        player.sendMessage(ChatColor.GRAY + "如果您认为这是一个 BUG, 请反馈至技术组");
                    } else {
                        OnlineUser user = ZthMySpawn.huskHomesAPI.adaptUser((Player) player);
                        ZthMySpawn.huskHomesAPI.teleportBuilder(user)
                                .target(result.get())
                                .toTimedTeleport()
                                .execute();
                    }
                });
    }

}

