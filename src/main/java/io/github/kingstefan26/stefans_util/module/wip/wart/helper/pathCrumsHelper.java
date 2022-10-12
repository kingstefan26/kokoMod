/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.wip.wart.helper;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class pathCrumsHelper {
    private static pathCrumsHelper instance;
    @Getter
    public List<Vec3> pathPoints;

    public static pathCrumsHelper getInstance() {
        return instance == null ? instance = new pathCrumsHelper() : instance;
    }

    public void clear() {
        if (pathPoints != null) {
            pathPoints.clear();
        }
    }

    public void update() {
        if (pathPoints == null) pathPoints = new ArrayList<>();
//        pathPoints.add(util.getPlayerFeetVec().add(new Vec3(0, Minecraft.getMinecraft().thePlayer.height, 0)));
        pathPoints.add(Minecraft.getMinecraft().thePlayer.getPositionVector());
    }

    public void simplify() {
        if (pathPoints != null) {
            pathPoints = simplyfyPathLosless(pathPoints);
        }
    }


    private List<Vec3> simplyfyPathLosless(List<Vec3> og) {
        ArrayList<Vec3> result = new ArrayList<>();

        final Iterator<Vec3> itr = og.iterator();
        Vec3 previous = itr.next();
        // add the first
        result.add(previous);
        Vec3 current = itr.next();
        while (itr.hasNext()) {
            Vec3 next = itr.next();


            // remove duplicates, aka the preruis current and next xyz is the same
            boolean shouldSkip =
                    // remove duplicates
                    (previous.zCoord == current.zCoord && previous.yCoord == current.yCoord && current.zCoord == next.zCoord && current.yCoord == next.yCoord && previous.xCoord == current.xCoord && current.xCoord == next.xCoord)

                            // optimise x
                            || (previous.zCoord == current.zCoord && previous.yCoord == current.yCoord && current.zCoord == next.zCoord && current.yCoord == next.yCoord
                            && previous.xCoord <= current.xCoord && next.xCoord >= current.xCoord)

                            || (previous.zCoord == current.zCoord && previous.yCoord == current.yCoord && current.zCoord == next.zCoord && current.yCoord == next.yCoord
                            && previous.xCoord >= current.xCoord && next.xCoord <= current.xCoord)

                            // z
                            || (previous.xCoord == current.xCoord && previous.yCoord == current.yCoord && current.xCoord == next.xCoord && current.yCoord == next.yCoord
                            && previous.zCoord <= current.zCoord && next.zCoord >= current.zCoord)

                            || (previous.xCoord == current.xCoord && previous.yCoord == current.yCoord && current.xCoord == next.xCoord && current.yCoord == next.yCoord
                            && previous.zCoord >= current.zCoord && next.zCoord <= current.zCoord)

                    // y
//                    ||(previous.xCoord == current.xCoord && previous.zCoord == current.zCoord && current.xCoord == next.xCoord && current.zCoord == next.zCoord
//                            && previous.yCoord <= current.yCoord   && next.yCoord >= current.yCoord )
//
//                    ||(previous.xCoord == current.xCoord && previous.zCoord == current.zCoord && current.xCoord == next.xCoord && current.zCoord == next.zCoord
//                            && previous.yCoord >= current.yCoord  && next.yCoord <= current.yCoord )


                    ;
            // !itr.hasNext() is used to save the last element
            if (!shouldSkip || !itr.hasNext()) {
                result.add(current);
            }

            previous = current;
            current = next;
        }

        return result;
    }

}
