package ru.homa.dinamit.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Explosive;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import ru.homa.dinamit.HomaDinamitPlugin;
import ru.homa.dinamit.managers.ExplosionManager;
import ru.homa.dinamit.types.DinamitType;

/**
 * Слушатель событий взрыва TNT.
 * Перехватывает стандартный взрыв и применяет специальные эффекты HomaDinamit.
 */
public class DinamitExplosionListener implements Listener {

    private final HomaDinamitPlugin plugin;
    private final ExplosionManager explosionManager;

    public DinamitExplosionListener(HomaDinamitPlugin plugin, ExplosionManager explosionManager) {
        this.plugin = plugin;
        this.explosionManager = explosionManager;
    }

    /**
     * Обрабатывает взрыв сущности.
     * Если взрыв произошёл на помеченном блоке — применяет эффект динамита.
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityExplode(EntityExplodeEvent event) {
        Location loc = event.getLocation();
        DinamitType type = plugin.getMarkedType(loc);
        if (type == null) return;

        // Удаляем метку и отменяем стандартный взрыв
        plugin.unmarkBlock(loc);
        event.setCancelled(true);

        // Применяем специальный взрыв динамита
        explosionManager.handleExplosion(type, loc, null);
    }
}
