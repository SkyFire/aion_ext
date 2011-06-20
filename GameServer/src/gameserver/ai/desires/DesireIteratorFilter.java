/*
 * This file is part of alpha team <alpha-team.com>.
 *
 * alpha team is pryvate software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * alpha team is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with alpha team.  If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.ai.desires;

/**
 * This class represents simple filter for desire iterations.
 *
 * @author SoulKeeper
 */
public interface DesireIteratorFilter {
    /**
     * This method is called each time for every desire that is in the queue.<br>
     * <br> {@link java.util.ConcurrentModificationException} will be thrown by
     * {@link gameserver.ai.desires.DesireQueue#iterateDesires(DesireIteratorHandler, DesireIteratorFilter[])}
     * if any of the following methods will be called from here:
     * <ul>
     * <li>{@link gameserver.ai.desires.DesireQueue#addDesire(Desire)}</li>
     * <li>{@link gameserver.ai.desires.DesireQueue#poll()}</li>
     * <li>{@link gameserver.ai.desires.DesireQueue#removeDesire(Desire)}</li>
     * </ul>
     * <p/>
     * However {@link gameserver.ai.desires.DesireQueue#clear()} can be called.
     *
     * @param desire current element of iteration that is being filtered
     * @return true if this filter accepted desire, false otherwise
     */
	public boolean isOk(Desire desire);
}
