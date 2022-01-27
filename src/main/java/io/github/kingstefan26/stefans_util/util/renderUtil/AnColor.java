/*
 *     Dungeons Guide - The most intelligent Hypixel Skyblock Dungeons Mod
 *     Copyright (C) 2021  cyoung06
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.kingstefan26.stefans_util.util.renderUtil;


import java.awt.*;
import java.util.Objects;


public class AnColor extends Color {
    private boolean chroma;
    private float chromaSpeed;

    public AnColor(int r, int g, int b, int a) {
        super(r, g, b, a);
    }

    public AnColor(int rgba, boolean hasalpha) {
        super(rgba, hasalpha);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AnColor anColor = (AnColor) o;
        return chroma == anColor.chroma && Float.compare(anColor.chromaSpeed, chromaSpeed) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), chroma, chromaSpeed);
    }

    //Integer.parseInt("hex code", 16)


    public boolean isChroma() {
        return chroma;
    }

    public float getChromaSpeed() {
        return chromaSpeed;
    }

    @Override
    public String toString() {
        return "AColor{" +
                ", r=" + getRed() +
                ", g=" + getGreen() +
                ", b=" + getBlue() +
                ", a=" + getAlpha() +
                ", chromaSpeed=" + chromaSpeed +
                '}';
    }
}
