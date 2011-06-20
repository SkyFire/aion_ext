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
package com.aionemu.commons.versionning;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * @author lord_rex
 */
public class Version {
    private static final Logger log = Logger.getLogger(Version.class);
    private String revision;
    private String date;

    public Version() {
    }

    public Version(Class<?> c) {
        loadInformation(c);
    }

    public void loadInformation(Class<?> c) {
        File jarName = null;
        try {
            jarName = Locator.getClassSource(c);
            JarFile jarFile = new JarFile(jarName);

            Attributes attrs = jarFile.getManifest().getMainAttributes();

            setRevision(attrs);

            setDate(attrs);

        }
        catch (IOException e) {
            log.error("Unable to get Soft information\nFile name '"
                    + (jarName == null ? "null" : jarName.getAbsolutePath()) + "' isn't a valid jar", e);
        }

    }

    public void transferInfo(String jarName, String type, File fileToWrite) {
        try {
            if (!fileToWrite.exists()) {
                log.error("Unable to Find File :" + fileToWrite.getName() + " Please Update your " + type);
                return;
            }
            // Open the JAR file
            JarFile jarFile = new JarFile("./" + jarName);
            // Get the manifest
            Manifest manifest = jarFile.getManifest();
            // Write the manifest to a file
            OutputStream fos = new FileOutputStream(fileToWrite);
            manifest.write(fos);
            fos.close();
        }
        catch (IOException e) {
            log.error("Error, " + e);
        }
    }

    public void setRevision(Attributes attrs) {
        String revision = attrs.getValue("Revision");

        if (revision != null)
            this.revision = revision;
        else
            this.revision = "Unknown Revision Number.";
    }

    public String getRevision() {
        return revision;
    }

    public void setDate(Attributes attrs) {
        String date = attrs.getValue("Date");

        if (date != null)
            this.date = date;
        else
            this.date = "Unknown Date Time.";
    }

    public String getDate() {
        return date;
    }
}
