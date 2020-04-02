/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.util;

import org.gradle.internal.UncheckedException;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class RelativePathUtil {
    /**
     * Returns a relative path from 'from' to 'to'
     *
     * @param from where to calculate from
     * @param to where to calculate to
     * @return The relative path
     */
    public static String relativePath(File from, File to) {
        try {
            return getRelativePath(from, to);
        } catch (Exception e) {
            throw UncheckedException.throwAsUncheckedException(e);
        }
    }

    // The following methods are _copied_ from Ant FileUtils, in order not to depend on an external library
    // for this

    private static String getRelativePath(File fromFile, File toFile) throws Exception { //NOSONAR
        String fromPath = fromFile.getCanonicalPath();
        String toPath = toFile.getCanonicalPath();

        // build the path stack info to compare
        String[] fromPathStack = getPathStack(fromPath);
        String[] toPathStack = getPathStack(toPath);

        if (0 < toPathStack.length && 0 < fromPathStack.length) {
            if (!fromPathStack[0].equals(toPathStack[0])) {
                // not the same device (would be "" on Linux/Unix)

                return getPath(Arrays.asList(toPathStack));
            }
        } else {
            // no comparison possible
            return getPath(Arrays.asList(toPathStack));
        }

        int minLength = Math.min(fromPathStack.length, toPathStack.length);
        int same = 1; // Used outside the for loop

        // get index of parts which are equal
        for (;
             same < minLength && fromPathStack[same].equals(toPathStack[same]);
             same++) {
            // Do nothing
        }

        List relativePathStack = new ArrayList();

        // if "from" part is longer, fill it up with ".."
        // to reach path which is equal to both paths
        for (int i = same; i < fromPathStack.length; i++) {
            relativePathStack.add("..");
        }

        // fill it up path with parts which were not equal
        for (int i = same; i < toPathStack.length; i++) {
            relativePathStack.add(toPathStack[i]);
        }

        return getPath(relativePathStack);
    }

    private static String[] getPathStack(String path) {
        String normalizedPath = path.replace(File.separatorChar, '/');

        return normalizedPath.split("/");
    }

    public static String getPath(List pathStack) {
        // can safely use '/' because Windows understands '/' as separator
        return getPath(pathStack, '/');
    }
    
    private static String getPath(final List pathStack, final char separatorChar) {
        final StringBuffer buffer = new StringBuffer();

        final Iterator iter = pathStack.iterator();
        if (iter.hasNext()) {
            buffer.append(iter.next());
        }
        while (iter.hasNext()) {
            buffer.append(separatorChar);
            buffer.append(iter.next());
        }
        return buffer.toString();
    }
}
