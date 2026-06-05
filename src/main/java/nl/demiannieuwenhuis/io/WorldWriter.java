package nl.demiannieuwenhuis.io;

import nl.demiannieuwenhuis.physics.World;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Path;

public class WorldWriter {

    private static final Path worldDataDir = Path.of("src/main/resources/worlds");

    public static void write(World world) throws IOException {
        String worldName = "earth";
        Path outputFile = worldDataDir.resolve(worldName + ".ser");

        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outputFile.toFile()))) {
            oos.writeObject(world.getBodies());
            oos.writeObject(world.getRopes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
