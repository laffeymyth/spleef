package net.laffeymyth.spleef.command;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.*;

@Command(name = "testschem")
public class TestSchematicCommand {
    private final WorldEdit worldEdit = WorldEditPlugin.getInstance().getWorldEdit();
    private final Plugin plugin;

    public TestSchematicCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Execute
    public void command(@Context Player player, @Arg("world") org.bukkit.World world) {
        World adaptedWorld = BukkitAdapter.adapt(world);

        File file = new File(plugin.getDataFolder(), "Christmas.schem");

        try {
            paste(file, adaptedWorld, player);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void paste(File file, World world, Player player) throws IOException {
        ClipboardFormat format = ClipboardFormats.findByFile(file);

        assert format != null;
        try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
            Clipboard clipboard = reader.read();

            EditSession editSession = worldEdit.newEditSessionBuilder().world(world).build();

            Operation operation = new ClipboardHolder(clipboard).createPaste(editSession)
                    .to(BlockVector3.at(player.getX(), player.getY(), player.getZ())).ignoreAirBlocks(true).build();
            Operations.complete(operation);
            editSession.close();
        }
    }
}
