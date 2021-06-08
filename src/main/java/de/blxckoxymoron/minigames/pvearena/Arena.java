package de.blxckoxymoron.minigames.pvearena;

import de.blxckoxymoron.minigames.Minigames;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Arena {

    public static final String BASE_PATH = "pvearena.arenas";
    public static int currArenaId = 0;
    public static FileConfiguration defaultConfig = Minigames.getPlugin().getConfig();

    private Inventory mobInventory = Bukkit.createInventory(null, 54, ChatColor.GREEN + "Mob-Spawneggs");

    public String name;
    public int id; // Unique!
    public short maxHeight = 256;
    public short spawnHeight = 70;
    public Location spawnLocation;
    public HashMap<EntityType, Integer> mobs = new HashMap<>();
    public EntityType boss = EntityType.CHICKEN;
    public HashMap<Material, Integer> defaultLoot = new HashMap<>();
    public HashMap<Material, Integer> bossLoot = new HashMap<>();

    public Arena(int id) {
        this(id, "arena-" + id);
    }

    public Arena(int id, String name) {
        this.id = id;
        this.name = name;
        this.spawnLocation = new Location(Bukkit.getWorld(PvEArena.worldName), 16 + (id * 3 * 16),60,24);
    }

    public void setMob(EntityType type, int count) {
        this.mobs.put(type, count);
    }

    public void setToConfig() {
        setToConfig(defaultConfig);
    }

    /**
     * Config isn't saved automatically so it needs to be saved after!
     *
     * @param config any file Config
     */
    public void setToConfig(FileConfiguration config) {
        String path = BASE_PATH + "." + id + ".";
        config.set(path + "name", name);
        config.set(path + "maxHeight", maxHeight);
        config.set(path + "spawnHeight", spawnHeight);
        config.set(path + "mobs", enumMapToString(mobs));
        config.set(path + "boss", boss.name());
        config.set(path + "defaultLoot", enumMapToString(defaultLoot));
        config.set(path + "bossLoot", enumMapToString(bossLoot));
        config.set(path + "spawnLocation", locationToList(spawnLocation));
    }

    public void setFromConfig() {
        setFromConfig(defaultConfig);
    }

    public void setFromConfig(FileConfiguration config) {
        String path = BASE_PATH + "." + id + ".";
        this.name = config.getString(path + "name", name);
        this.maxHeight = (short) config.getInt(path + "maxHeight", maxHeight);
        this.spawnHeight = (short) config.getInt(path + "spawnHeight", spawnHeight);
        this.mobs = stringMapToEnum(
                convertValues(config.getConfigurationSection(path + "mobs").getValues(false)),
                EntityType.class
        );
/* OLD...
        this.mobs = stringMapToEnum(
                config.getObject(path + "mobs", HashMap.class, enumMapToString(mobs)),
                EntityType.class);
*/
        this.boss = EntityType.valueOf(config.getString(path + "boss", boss.name()));
        this.defaultLoot = stringMapToEnum(
                convertValues(config.getConfigurationSection(path + "defaultLoot").getValues(false)),
                Material.class
        );
        this.bossLoot = stringMapToEnum(
                convertValues(config.getConfigurationSection(path + "bossLoot").getValues(false)),
                Material.class
        );
        this.spawnLocation = listToLocation(config.getIntegerList(path + "spawnLocation"));
    }

    public static Map<String, Integer> convertValues(Map<String, Object> objectMap) {
        Map<String, Integer> result = new HashMap<>();
        for (String s : objectMap.keySet()) {
            result.put(s, (Integer) objectMap.get(s));
        }
        return result;
    }

    public static int[] locationToList(Location location) { return new int[] {location.getBlockX(), location.getBlockY(), location.getBlockZ()}; }

    public static Location listToLocation(List<Integer> ints) { return new Location(Bukkit.getWorld(PvEArena.worldName), ints.get(0), ints.get(1), ints.get(2)); }

    public static <E extends Enum<E>> HashMap<String, Integer> enumMapToString(Map<E, Integer> mobs) {
       HashMap<String, Integer> result = new HashMap<>();

       for (E type : mobs.keySet()) {
           result.put(type.name(), mobs.get(type));
       }

       return result;
    }

    public static <E extends Enum<E>> HashMap<E, Integer> stringMapToEnum(Map<String, Integer> mobs, Class<E> enumType) {
        HashMap<E, Integer> result = new HashMap<>();

        for (String s : mobs.keySet()) {
            try {
                result.put(E.valueOf(enumType, s), mobs.get(s));
            } catch (Exception ignored) {}

        }

        return result;
    }

    public static Integer getNewArenaId() {

        while (defaultConfig.contains(BASE_PATH + "." + currArenaId)) {
            currArenaId++;
        }

        return currArenaId;
    }


    public static Set<String> getArenaNames() {
        Set<String> result = new HashSet<>();
        for (String id : defaultConfig.getConfigurationSection(BASE_PATH).getKeys(false)) {
           result.add(defaultConfig.getString(BASE_PATH + "." + id + ".name"));
        }
        return result;
    }

    public static Set<String> getArenaIds() {
        return defaultConfig.getConfigurationSection(BASE_PATH).getKeys(false);
    }

    public static Set<String> getArenaIds(String arenaName) {
        Set<String> result = new HashSet<>();
        for (String id : defaultConfig.getConfigurationSection(BASE_PATH).getKeys(false)) {
            if (defaultConfig.getString(BASE_PATH + "." + id + ".name").matches(arenaName)) {
                result.add(id);
            }
        }
        return result;
    }

    public static Arena createNewAndSave(int id) {
        return createNewAndSave(id, "arena-"+id);
    }

    public static Arena createNewAndSave(int id, String name) {
        return createNewAndSave(id, name, defaultConfig);
    }

    public static Arena createNewAndSave(int id, String name, FileConfiguration configuration) {
        Arena newArena = new Arena(id, name);
        newArena.setToConfig(configuration);
        PvEArena.loadedArenas.add(newArena);
        return newArena;
    }

    public static void deleteArena(int id) {
        PvEArena.loadedArenas.removeIf(a -> a.id == id);
        defaultConfig.set(Arena.BASE_PATH + "." + id, null);
        Minigames.getPlugin().saveConfig();
    }


    public static Arena getArena(int id) {
        for (Arena a : PvEArena.loadedArenas) {
            if (a.id == id) return a;
        }
        if (Arena.getArenaIds().contains(Integer.toString(id))) {
            Arena newArena = new Arena(id);
            newArena.setFromConfig();
            PvEArena.loadedArenas.add(newArena);
            return newArena;
        }
        return createNewAndSave(id);
    }

    public Inventory getMobInventory() {

        setToConfig();
        Minigames.getPlugin().saveConfig();
        if (mobInventory.isEmpty()) {
            for (EntityType mob : mobs.keySet()) {
                mobInventory.addItem(new ItemStack(Material.valueOf(mob.name() + "_SPAWN_EGG"), mobs.get(mob)));
            }
        }

        return mobInventory;
    }

    public HashMap<EntityType, Integer> getMobs() {
        return mobs;
    }

    public String getName() {
        return name;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }
}
