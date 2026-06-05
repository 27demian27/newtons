package nl.demiannieuwenhuis.io;

import nl.demiannieuwenhuis.physics.Rope;
import nl.demiannieuwenhuis.physics.World;
import nl.demiannieuwenhuis.physics.rigidbody.Body;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Path;
import java.util.List;

public class WorldLoader {

    private static final Path worldDataDir = Path.of("src/main/resources/worlds");

    public static World load() throws IOException {
        String worldName = "earth";
        Path inputFile = worldDataDir.resolve(worldName + ".ser");

        World world = new World();

        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(inputFile.toFile()))) {
            List<Body> bodies = (List<Body>) ois.readObject();
            List<Rope> ropes = (List<Rope>) ois.readObject();

            world.setBodies(bodies);
            world.setRopes(ropes);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return world;
    }
}
