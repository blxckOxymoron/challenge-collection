package de.blxckoxymoron.minigames.pvearena;

import de.blxckoxymoron.minigames.Minigames;
import de.blxckoxymoron.minigames.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Arena {

    public static final String BASE_PATH = "pvearena.arenas";
    public static final String INVENTORY_PREFIX = ChatUtils.MessageColor.NAME + "ArenaInventory" + ChatColor.GRAY + " | " + ChatColor.WHITE;
    public static int currArenaId = 0;
    public static Map<Player, Integer> playerArenaIdMap = new HashMap<>();
    public static FileConfiguration defaultConfig = Minigames.getPlugin().getConfig();
    public static void saveDefaultConfig() {
        Minigames.getPlugin().saveConfig();
    }

    private Inventory mobInv = Bukkit.createInventory(null, 54, INVENTORY_PREFIX + "Mob-Spawneggs");
    private Inventory bossLootInv = Bukkit.createInventory(null, 54, INVENTORY_PREFIX + "Boss-Loot");
    private Inventory defaultLootInv = Bukkit.createInventory(null, 54, INVENTORY_PREFIX + "Default-Loot");

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

    public static void updateInventories() {
        for (Arena a : PvEArena.loadedArenas) {
            a.saveMobInventory(false);
            a.saveLootInventories(false);
        }
        saveDefaultConfig();
    }

    public static EntityType spawneggToEntity(String spawneggEntityName) throws IllegalArgumentException {
        spawneggEntityName = spawneggEntityName.toUpperCase();
        try {
            return EntityType.valueOf(spawneggEntityName);
        } catch (Exception e) {
            if (defaultConfig.contains("pvearena.spawnegg-mobs." + spawneggEntityName)) {
                return EntityType.valueOf(defaultConfig.getString("pvearena.spawnegg-mobs." + spawneggEntityName));
            }
            throw e;
        }
    }

    public static Material entityToSpawnegg(String entityName) throws IllegalArgumentException {
        entityName = entityName.toUpperCase();
        try {
            return Material.valueOf(entityName + "_SPAWN_EGG");
        } catch (Exception e) {
            Map<String, Object> spawneggMobs = defaultConfig.getConfigurationSection("pvearena.spawnegg-mobs").getValues(false);
            for (String k : spawneggMobs.keySet()) {
                if (spawneggMobs.get(k).equals(entityName)) {
                    return Material.valueOf(k + "_SPAWN_EGG");
                }
            }
            throw e;
        }
    }

    public static void playerToNextArena(Player player) {
        if (playerArenaIdMap.containsKey(player)) {
            Arena nextArena = Arena.getArena(playerArenaIdMap.get(player)).getNextArena();
            player.teleport(nextArena.getSpawnLocation());
            playerArenaIdMap.put(player, nextArena.getId());
        }
    }

    public Inventory getMobInv() {

        if (mobInv.isEmpty()) {
            for (EntityType mob : mobs.keySet()) {
                try {
                    mobInv.addItem(new ItemStack(entityToSpawnegg(mob.name()), mobs.get(mob)));
                } catch (Exception e) {
                    System.out.println("Error with Mob: " + mob.name());
                }
            }
        }

        return mobInv;
    }

    public void saveMobInventory() {
        saveMobInventory(true);
    }

    public void saveMobInventory(boolean save) {
        mobs.clear();
        for (ItemStack itemStack : mobInv.getContents()) {
            if (itemStack == null) continue;
            String type = itemStack.getType().name().replace("_SPAWN_EGG", "");
            try {
                EntityType entityType = spawneggToEntity(type);
                if (mobs.containsKey(entityType)) {
                    mobs.put(entityType, mobs.get(entityType) + itemStack.getAmount());
                } else {
                    mobs.put(entityType, itemStack.getAmount());
                }
            } catch (Exception e) {
                System.out.println("Error with Spawnegg: " + type);
            }
        }
        setToConfig();
        if (save) saveDefaultConfig();
    }

    public Inventory getLootInventory(LootInventory inventory) {
        switch (inventory) {
            case BOSS_LOOT: {
                if (bossLootInv.isEmpty()) {
                    bossLoot.keySet().forEach(m -> bossLootInv.addItem( new ItemStack(m, bossLoot.get(m) ) ) );
                }
                return bossLootInv;
            }
            case DEFAULT_LOOT: {
                if (defaultLootInv.isEmpty()) {
                    defaultLoot.keySet().forEach(m -> defaultLootInv.addItem( new ItemStack(m, bossLoot.get(m) ) ) );
                }
                return defaultLootInv;
            }
            default: {
                throw new IllegalArgumentException("Unsupported Inventory!");
            }
        }
    }

    public void saveLootInventories() {
        saveLootInventories(true);
    }

    public void saveLootInventories(boolean save) {
        bossLoot.clear();
        defaultLoot.clear();
        for (ItemStack stack : bossLootInv) {
            if (stack == null) continue;
            Material stackMaterial = stack.getType();
            if (bossLoot.containsKey(stackMaterial)) {
                bossLoot.put(stackMaterial, bossLoot.get(stackMaterial) + stack.getAmount());
            } else {
                bossLoot.put(stackMaterial, stack.getAmount());
            }
        }
        for (ItemStack stack : defaultLootInv) {
            if (stack == null) continue;
            Material stackMaterial = stack.getType();
            if (defaultLoot.containsKey(stackMaterial)) {
                defaultLoot.put(stackMaterial, defaultLoot.get(stackMaterial) + stack.getAmount());
            } else {
                defaultLoot.put(stackMaterial, stack.getAmount());
            }
        }
        setToConfig();
        if (save) saveDefaultConfig();
    }

    public Arena getNextArena() {
        int nextId = this.id;
        int minDiff = Integer.MAX_VALUE;
        for (String stringId : Arena.getArenaIds()) {
            try {
                int arenaId = Integer.parseInt(stringId);
                if (arenaId < nextId && arenaId > this.id) {
                    nextId = arenaId;
                }
            } catch (IllegalArgumentException ignored) {
            }
        }
        return Arena.getArena(nextId);
    }

    public HashMap<EntityType, Integer> getMobs() {
        return mobs;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setBoss(EntityType newType) {
        boss = newType;
        setToConfig();
        saveDefaultConfig();
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public enum LootInventory {
        BOSS_LOOT(),
        DEFAULT_LOOT()
    }
}
