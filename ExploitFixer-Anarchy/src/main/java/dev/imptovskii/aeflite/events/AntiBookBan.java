package dev.imptovskii.aeflite.events;

import dev.imptovskii.aeflite.Main;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;

import java.nio.charset.Charset;

public class AntiBookBan implements Listener {
    private final Main plugin;

    public AntiBookBan(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBookEdit(PlayerEditBookEvent e) {
        for (String bookPage : e.getNewBookMeta().getPages()) {
            if (!Charset.forName("ISO-8859-5").newEncoder().canEncode(bookPage)) {
                e.setCancelled(true);
                String antiBookBan = plugin.getConfig().getString("message");
                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', antiBookBan != null ? antiBookBan : "&cВы используете Unicode символы!"));
            }
        }
    }
}