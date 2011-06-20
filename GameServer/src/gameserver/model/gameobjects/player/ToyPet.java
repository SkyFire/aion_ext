/**
 * This file is part of Aion X Emu <aionxemu.com>
 *
 *  This is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This software is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.model.gameobjects.player;

/**
 * @author xitanium
 */
public class ToyPet {
    private int databaseIndex;
    private Player master;
    private int decoration;
    private String name;
    private int petId;

    private float x1 = 0;
    private float y1 = 0;
    private float z1 = 0;

    private int h = 0;

    private float x2 = 0;
    private float y2 = 0;
    private float z2 = 0;

    public ToyPet() {

    }

    public Player getMaster() {
        return master;
    }

    public void setMaster(Player player) {
        this.master = player;
    }

    /**
     * @return the databaseIndex
     */
    public int getDatabaseIndex() {
        return databaseIndex;
    }

    /**
     * @param databaseIndex the databaseIndex to set
     */
    public void setDatabaseIndex(int databaseIndex) {
        this.databaseIndex = databaseIndex;
    }

    /**
     * @return the petId
     */
    public int getPetId() {
        return petId;
    }

    /**
     * @param petId the petId to set
     */
    public void setPetId(int petId) {
        this.petId = petId;
    }

    /**
     * @return the decoration
     */
    public int getDecoration() {
        return decoration;
    }

    /**
     * @param decoration the decoration to set
     */
    public void setDecoration(int decoration) {
        this.decoration = decoration;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the x1
     */
    public float getX1() {
        return x1;
    }

    /**
     * @param x1 the x1 to set
     */
    public void setX1(float x1) {
        this.x1 = x1;
    }

    /**
     * @return the y1
     */
    public float getY1() {
        return y1;
    }

    /**
     * @param y1 the y1 to set
     */
    public void setY1(float y1) {
        this.y1 = y1;
    }

    /**
     * @return the z1
     */
    public float getZ1() {
        return z1;
    }

    /**
     * @param z1 the z1 to set
     */
    public void setZ1(float z1) {
        this.z1 = z1;
    }

    /**
     * @return the h
     */
    public int getH() {
        return h;
    }

    /**
     * @param h the h to set
     */
    public void setH(int h) {
        this.h = h;
    }

    /**
     * @return the x2
     */
    public float getX2() {
        return x2;
    }

    /**
     * @param x2 the x2 to set
     */
    public void setX2(float x2) {
        this.x2 = x2;
    }

    /**
     * @return the y2
     */
    public float getY2() {
        return y2;
    }

    /**
     * @param y2 the y2 to set
     */
    public void setY2(float y2) {
        this.y2 = y2;
    }

    /**
     * @return the z2
     */
    public float getZ2() {
        return z2;
    }

    /**
     * @param z2 the z2 to set
     */
    public void setZ2(float z2) {
        this.z2 = z2;
    }
}
