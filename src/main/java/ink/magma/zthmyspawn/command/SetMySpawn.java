package ink.magma.zthmyspawn.command;

import ink.magma.zthmyspawn.ZthMySpawn;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SetMySpawn implements TabExecutor {
    List<String> tabList;

    public SetMySpawn() {
        tabList = new ArrayList<>(ZthMySpawn.spawnList);
        tabList.add(0, "default");
        tabList.add(0, "默认");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof ConsoleCommandSender) return false;

        // LuckPerms User
        User thisUser = ZthMySpawn.luckPermsAPI.getUserManager().getUser(sender.getName());

        // 设置回默认
        if (args.length == 0 || args.length == 1 && args[0].equals("default") || args[0].equals("默认")) {
            clearAllSpawn(thisUser);
            ZthMySpawn.luckPermsAPI.getUserManager().saveUser(thisUser).thenAccept(result -> {
                sender.sendMessage(ChatColor.GRAY + "您的主城成功设置为默认");
            });
            return true;
        }

        // 自定义
        if (args.length == 1) {
            // 先根据用户提供的主城名去查询一下是否有这个主城
            String spawnID = ZthMySpawn.instance.getConfig().getString("spawn-list." + args[0]);
            if (spawnID == null) {
                sender.sendMessage(ChatColor.GRAY + "您输入的主城名称无法找到");
                return true;
            }
            clearAllSpawn(thisUser);
            for (String node : ZthMySpawn.instance.getConfig().getStringList("real-list.nodes")) {
                thisUser.data().add(Node.builder("multispawn.spawn." + node).value(false).build());
            }
            thisUser.data().add(Node.builder("multispawn.spawn." + spawnID).build());
            ZthMySpawn.luckPermsAPI.getUserManager().saveUser(thisUser).thenAccept(result -> {
                sender.sendMessage(ChatColor.GRAY + "您的主城成功设置为 " + spawnID);
            });
            return true;
        }
        return false;
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return tabList;
    }

    void clearAllSpawn(@NotNull User user) {
        // 要清理的列表
        List<String> delList = ZthMySpawn.instance.getConfig().getStringList("real-list.nodes");
        // 删除所有权限设定
        delList.forEach(spawn -> {
            user.data().remove(Node.builder("multispawn.spawn." + spawn).build());
        });
    }
}
