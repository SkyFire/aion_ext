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

import gameserver.model.gameobjects.Letter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author kosyachok
 * @modified Atracer
 */
public class Mailbox {
    private Map<Integer, Letter> mails = new ConcurrentHashMap<Integer, Letter>();

    /**
     * @param letter
     */
    public void putLetterToMailbox(Letter letter) {
        mails.put(letter.getObjectId(), letter);
    }

    /**
     * Get all letters in mailbox (sorted according to time received)
     *
     * @return
     */
    public Collection<Letter> getLetters() {
        SortedSet<Letter> letters = new TreeSet<Letter>(new Comparator<Letter>() {

            @Override
            public int compare(Letter o1, Letter o2) {
                if (o1.getTimeStamp().getTime() > o2.getTimeStamp().getTime()) {
                    return 1;
                }
                if (o1.getTimeStamp().getTime() < o2.getTimeStamp().getTime()) {
                    return -1;
                }

                return o1.getObjectId() > o2.getObjectId() ? 1 : -1;
            }

        });

        for (Letter letter : mails.values()) {
            letters.add(letter);
        }
        return letters;
    }

    /**
     * Get letter with specified letter id
     *
     * @param letterObjId
     * @return
     */
    public Letter getLetterFromMailbox(int letterObjId) {
        return mails.get(letterObjId);
    }

    /**
     * Check whether mailbox contains empty letters
     *
     * @return
     */
    public boolean haveUnread() {
        for (Letter letter : mails.values()) {
            if (letter.isUnread()) {
                return true;
            }
        }

        return false;
    }

    /**
     * @return
     */
    public int getFreeSlots() {
        return 65536 - mails.size();
    }

    /**
     * @return
     */
    public boolean haveFreeSlots() {
        return mails.size() < 100;
    }

    /**
     * @param letterId
     */
    public void removeLetter(int letterId) {
        mails.remove(letterId);
    }

    /**
     * Current size of mailbox
     *
     * @return
     */
    public int size() {
        return mails.size();
    }

    public boolean haveUnreadExpress() {
        for (Letter letter : this.mails.values()) {
            if ((letter.isUnread()) && (letter.isExpress())) {
                return true;
            }
        }
        return false;
    }
}
