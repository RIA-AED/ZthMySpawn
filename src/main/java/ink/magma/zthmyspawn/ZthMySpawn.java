package ink.magma.zthmyspawn;

import ink.magma.zthmyspawn.command.MainCommand;
import ink.magma.zthmyspawn.command.SetMySpawn;
import ink.magma.zthmyspawn.command.SpawnCommand;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.william278.huskhomes.api.HuskHomesAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ZthMySpawn extends JavaPlugin implements Listener {

    public static HuskHomesAPI huskHomesAPI;
    public static LuckPerms luckPermsAPI;
    public static JavaPlugin instance;
    public static List<String> spawnList = new ArrayList<String>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        loadPlugin();
    }

    public void loadPlugin() {
        saveDefaultConfig();
        loadSpawnList();

        try {
            huskHomesAPI = HuskHomesAPI.getInstance();
            luckPermsAPI = LuckPermsProvider.get();
        } catch (Exception e) {
            getLogger().warning(e.getLocalizedMessage());
            getLogger().warning("载入 API 依赖时发生错误, 请检查 HuskHomes、LuckPerms 是否正常!");
            return;
        }


        try {
            Bukkit.getPluginCommand("myspawn").setExecutor(new SpawnCommand());
            Bukkit.getPluginCommand("setmyspawn").setExecutor(new SetMySpawn());
            Bukkit.getPluginCommand("zthmyspawn").setExecutor(new MainCommand());
            getLogger().info("成功注册指令!");
        } catch (Exception e) {
            getLogger().warning(e.getLocalizedMessage());
            getLogger().warning("注册指令时发生错误!");
        }
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void loadSpawnList() {
        // 加载所有主城 Label (含别名)
        ConfigurationSection tempSpawnList = getConfig().getConfigurationSection("spawn-list");
        if (tempSpawnList == null) return;
        spawnList.clear();
        for (Map.Entry<String, Object> spawn : tempSpawnList.getValues(false).entrySet()) {
            spawnList.add(spawn.getKey());
        }
    }

    @EventHandler
    public void spawnCmdTabFix(TabCompleteEvent event) {
        if (event.getBuffer().startsWith("/spawn ")) {
            String[] args = event.getBuffer().split(" ");
            if (args.length < 3) {
                event.setCompletions(spawnList);
            }
        }
    }
}
