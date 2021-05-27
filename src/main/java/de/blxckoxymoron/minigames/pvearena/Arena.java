package de.blxckoxymoron.minigames.pvearena;

import de.blxckoxymoron.minigames.Minigames;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

public class Arena {

    public static final String BASE_PATH = "pvearena.arenas";
    public static int currArenaId = 0;

    public String name;
    public int id; // Unique!
    public short maxHeight = 256;
    public short spawnHeight = 70;
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
    }

    public void setMob(EntityType type, int count) {
        this.mobs.put(type, count);
    }

    public void setToConfig() {
        setToConfig(Minigames.getPlugin().getConfig());
        Minigames.getPlugin().saveConfig();
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
    }

    public void setFromConfig() {
        setFromConfig(Minigames.getPlugin().getConfig());
    }

    public void setFromConfig(FileConfiguration config) {
        String path = BASE_PATH + "." + id + ".";
        this.name = config.getString(path + "name", name);
        this.maxHeight = (short) config.getInt(path + "maxHeight", maxHeight);
        this.spawnHeight = (short) config.getInt(path + "spawnHeight", spawnHeight);
        this.mobs = stringMapToEnum(
                config.getObject(path + "mobs", Map.class, enumMapToString(mobs)),
                EntityType.class);
        this.boss = EntityType.valueOf(config.getString(path + "boss", boss.name()));
        this.defaultLoot = stringMapToEnum(
                config.getObject(path + "defaultLoot", Map.class, enumMapToString(defaultLoot)),
                Material.class);
        this.bossLoot = stringMapToEnum(
                config.getObject(path + "bossLoot", Map.class, enumMapToString(bossLoot)),
                Material.class);
    }

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
        FileConfiguration config = Minigames.getPlugin().getConfig();

        while (config.contains(BASE_PATH + "." + currArenaId)) {
            currArenaId++;
        }

        return currArenaId;
    }



    public HashMap<EntityType, Integer> getMobs() {
        return mobs;
    }

    public String getName() {
        return name;
    }
}
