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
package gameserver.model.siege;

import gameserver.model.templates.siege.SiegeLocationTemplate;

/**
 * @author Sarynth
 */
public class SiegeLocation
{
    public static final int INVULNERABLE = 0;
    public static final int VULNERABLE = 1;

    /**
     * Unique id, defined by NCSoft
     */
    private int locationId;
    private int worldId;
    private SiegeType type;

    private SiegeLocationTemplate template;

    private SiegeRace siegeRace = SiegeRace.BALAUR;
    private int legionId = 0;

    private boolean isVulnerable = false;
    private boolean isCanTeleport = false;
    private int nextState = 1;

    private long lastArtifactActivation;

    public SiegeLocation()
    {
    } // <3 Fastmap

    public SiegeLocation(SiegeLocationTemplate template)
    {
        this.template = template;
        this.locationId = template.getId();
        this.worldId = template.getWorldId();
        this.type = template.getType();
    }

    /**
     * Returns unique LocationId of Siege Location
     * 
     * @return Integer LocationId
     */
    public int getLocationId()
    {
        return this.locationId;
    }

    public SiegeType getSiegeType()
    {
        return type;
    }

    public int getWorldId()
    {
        return this.worldId;
    }

    public SiegeLocationTemplate getLocationTemplate()
    {
        return this.template;
    }

    public SiegeRace getRace()
    {
        return this.siegeRace;
    }

    public void setRace(SiegeRace siegeRace)
    {
        this.siegeRace = siegeRace;
    }

    public int getLegionId()
    {
        return this.legionId;
    }

    public void setLegionId(int legionId)
    {
        this.legionId = legionId;
    }

    /**
     * Next State: 0 invulnerable 1 vulnerable
     * 
     * @return nextState
     */
    public int getNextState()
    {
        return this.nextState;
    }

    /**
     * @param nextState
     */
    public void setNextState(int nextState)
    {
        this.nextState = nextState;
    }

    /**
     * @return isVulnerable
     */
    public boolean isVulnerable()
    {
        return this.isVulnerable;
    }

    /**
     * @param new vulnerable value
     */
    public void setVulnerable(boolean value)
    {
        this.isVulnerable = value;
    }

    /**
     * @return isCanTeleport
     */
    public boolean isCanTeleport()
    {
        return this.isCanTeleport;
    }

    /**
     * @param new isCanTeleport value
     */
    public void setCanTeleport(boolean value)
    {
        this.isCanTeleport = value;
    }

    /**
     * @return
     */
    public int getInfluenceValue()
    {
        return 0;
    }

    /**
     * @return the lastArtifactActivation
     */
    public long getLastArtifactActivation()
    {
        return lastArtifactActivation;
    }

    /**
     * @param lastArtifactActivation
     *            the lastArtifactActivation to set
     */
    public void setLastArtifactActivation(long lastArtifactActivation)
    {
        this.lastArtifactActivation = lastArtifactActivation;
    }

    public int getRemainingEffectSeconds()
    {
        int duration = 0;
        switch(type)
        {
            case ARTIFACT:
                break;
            case FORTRESS:
                duration = 3600;
                break;
        }
        long diff = System.currentTimeMillis() - lastArtifactActivation;
        if(diff > duration)
            return 0;
        else
            return (int) (duration - diff);
    }

    public int getVulnerabilityDuration() {
        return template.getVulnerabilityDuration();
    }

    public String getVulnerabilityTime() {
        return template.getVulnerabilityTime();
    }

}
