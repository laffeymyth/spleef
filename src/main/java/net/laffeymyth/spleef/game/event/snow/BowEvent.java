package net.laffeymyth.spleef.game.event.snow;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.title.TitlePart;
import net.laffeymyth.spleef.api.Game;
import net.laffeymyth.spleef.game.event.GameEvent;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class BowEvent implements GameEvent {
    private final Game game;

    public BowEvent(Game game) {
        this.game = game;
    }

    @Override
    public void start() {
        game.getGamers().forEach(player -> {
            ItemStack bow = new ItemStack(Material.BOW);
            bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);

            ItemStack arrow = new ItemStack(Material.ARROW);

            player.getInventory().addItem(bow, arrow);
        });

        game.getGamers().forEach(player -> player.sendTitlePart(TitlePart.TITLE, game.getLang().getMessage(
                "game_event_bow_title",
                "ru"
        )));
    }

    @Override
    public int timeToNextEvent() {
        return 60;
    }

    @Override
    public Component getName(String language, TagResolver... tagResolvers) {
        return game.getLang().getMessage("game_event_board_bow", language, tagResolvers);
    }
}
