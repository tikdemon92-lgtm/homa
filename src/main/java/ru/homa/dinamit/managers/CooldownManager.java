package ru.homa.dinamit.managers;

import org.bukkit.entity.Player;
import ru.homa.dinamit.types.DinamitType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Менеджер кулдаунов для видов динамита.
 * Следит за временем последнего использования каждого типа динамита каждым игроком.
 */
public class CooldownManager {

    private final Map<UUID, Map<DinamitType, Long>> cooldowns = new HashMap<>();

    /**
     * Проверяет, находится ли игрок на кулдауне для данного типа динамита.
     *
     * @param player   игрок
     * @param type     тип динамита
     * @param seconds  длительность кулдауна в секундах
     * @return {@code true}, если кулдаун ещё не истёк
     */
    public boolean isOnCooldown(Player player, DinamitType type, int seconds) {
        if (seconds <= 0) return false;
        Map<DinamitType, Long> map = cooldowns.get(player.getUniqueId());
        if (map == null) return false;
        Long last = map.get(type);
        if (last == null) return false;
        return (System.currentTimeMillis() - last) < (seconds * 1000L);
    }

    /**
     * Возвращает оставшееся время кулдауна в секундах для игрока и типа динамита.
     *
     * @param player   игрок
     * @param type     тип динамита
     * @param seconds  длительность кулдауна в секундах
     * @return оставшееся время кулдауна в секундах (0, если кулдаун истёк)
     */
    public long getRemainingSeconds(Player player, DinamitType type, int seconds) {
        if (seconds <= 0) return 0;
        Map<DinamitType, Long> map = cooldowns.get(player.getUniqueId());
        if (map == null) return 0;
        Long last = map.get(type);
        if (last == null) return 0;
        long elapsed = System.currentTimeMillis() - last;
        long remaining = (seconds * 1000L) - elapsed;
        return remaining > 0 ? (remaining / 1000) + 1 : 0;
    }

    /**
     * Устанавливает кулдаун для игрока и типа динамита (фиксирует текущее время).
     *
     * @param player игрок
     * @param type   тип динамита
     */
    public void setCooldown(Player player, DinamitType type) {
        cooldowns
                .computeIfAbsent(player.getUniqueId(), k -> new HashMap<>())
                .put(type, System.currentTimeMillis());
    }

    /**
     * Сбрасывает все кулдауны для данного игрока.
     *
     * @param player игрок
     */
    public void clearCooldowns(Player player) {
        cooldowns.remove(player.getUniqueId());
    }
}
