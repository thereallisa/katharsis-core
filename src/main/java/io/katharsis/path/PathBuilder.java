package io.katharsis.path;

import java.util.Arrays;
import java.util.List;

/**
 * Builder responsible for parsing URL path.
 */
public class PathBuilder {
    public static final String SEPARATOR = "/";
    public static final String RELATIONSHIP_MARK = "links";

    public JsonPath buildPath(String path) {
        String[] strings = splitPath(path);
        if (strings.length == 0 || (strings.length == 1 && "".equals(strings[0]))) {
            throw new IllegalArgumentException("Path is empty");
        }

        JsonPath previousJsonPath = null, currentJsonPath;

        int currentElementIdx = 0;
        while (true) {
            if (currentElementIdx >= strings.length) {
                throw new IllegalArgumentException("No type field defined after links marker");
            }

            currentJsonPath = new JsonPath(strings[currentElementIdx]);
            currentElementIdx++;

            if (previousJsonPath != null) {
                previousJsonPath.setChildResource(currentJsonPath);
                currentJsonPath.setParentResource(previousJsonPath);
            }
            previousJsonPath = currentJsonPath;

            if (currentElementIdx >= strings.length) {
                break;
            } else {
                PathIds pathIds = createPathIds(strings[currentElementIdx]);
                currentJsonPath.setIds(pathIds);
                currentElementIdx++;
            }

            if (currentElementIdx >= strings.length) {
                break;
            } else if (RELATIONSHIP_MARK.equals(strings[currentElementIdx])) {
                currentJsonPath.setHasRelationshipMark(true);
                currentElementIdx++;
            }
        }

        return currentJsonPath;
    }

    private PathIds createPathIds(String idsString) {
        List<String> pathIds = Arrays.asList(idsString.split(PathIds.ID_SEPERATOR));
        return new PathIds(pathIds);
    }

    private String[] splitPath(String path) {
        if (path.startsWith(SEPARATOR)) {
            path = path.substring(1);
        }
        if (path.endsWith(SEPARATOR)) {
            path = path.substring(0, path.length());
        }
        return path.split(SEPARATOR);
    }
}
