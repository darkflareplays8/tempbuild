package dev.proflare.snowflakecannon;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class SnowflakeStrike {

    private static final int DROP_HEIGHT = 80;
    private static final double ARM_RADIUS = 12.0;
    private static final int ARMS = 6;
    private static final int POINTS_PER_ARM = 5;
    private static final int FUSE_TICKS = 60;
    private static final double DOWNWARD_VELOCITY = -1.5;

    public static void fire(Location target, JavaPlugin plugin) {
        World world = target.getWorld();
        List<Location> spawnPoints = buildSnowflakePoints(target);

        new BukkitRunnable() {
            int index = 0;

            @Override
            public void run() {
                if (index >= spawnPoints.size()) {
                    cancel();
                    return;
                }

                Location point = spawnPoints.get(index);
                Location spawnLoc = point.clone().add(0, DROP_HEIGHT, 0);

                TNTPrimed tnt = world.spawn(spawnLoc, TNTPrimed.class);
                tnt.setFuseTicks(FUSE_TICKS);
                tnt.setVelocity(new Vector(0, DOWNWARD_VELOCITY, 0));

                index++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    private static List<Location> buildSnowflakePoints(Location center) {
        List<Location> points = new ArrayList<>();
        double cx = center.getX();
        double cy = center.getY();
        double cz = center.getZ();
        World world = center.getWorld();

        points.add(new Location(world, cx, cy, cz));

        for (int arm = 0; arm < ARMS; arm++) {
            double baseAngle = Math.toRadians((360.0 / ARMS) * arm);

            for (int p = 1; p <= POINTS_PER_ARM; p++) {
                double dist = (ARM_RADIUS / POINTS_PER_ARM) * p;
                double x = cx + Math.cos(baseAngle) * dist;
                double z = cz + Math.sin(baseAngle) * dist;
                points.add(new Location(world, x, cy, z));
            }

            if (POINTS_PER_ARM >= 3) {
                double branchDist = (ARM_RADIUS / POINTS_PER_ARM) * (POINTS_PER_ARM - 2);
                double branchLength = ARM_RADIUS * 0.3;

                for (int side = -1; side <= 1; side += 2) {
                    double perpAngle = baseAngle + Math.toRadians(60.0 * side);
                    double bx = cx + Math.cos(baseAngle) * branchDist + Math.cos(perpAngle) * branchLength;
                    double bz = cz + Math.sin(baseAngle) * branchDist + Math.sin(perpAngle) * branchLength;
                    points.add(new Location(world, bx, cy, bz));
                }
            }
        }

        return points;
    }
}
