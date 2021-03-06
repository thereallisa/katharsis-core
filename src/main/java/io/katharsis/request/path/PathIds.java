package io.katharsis.request.path;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Represent an id or ids passed in the path from a client.
 */
public class PathIds {
    public static final String ID_SEPERATOR = ",";

    private final List<String> ids = new LinkedList<>();

    public PathIds(@SuppressWarnings("SameParameterValue") String id) {
        ids.add(id);
    }

    public PathIds(Collection<String> id) {
        ids.addAll(id);
    }

    public List<String> getIds() {
        return ids;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PathIds pathIds = (PathIds) o;

        return !(ids != null ? !ids.equals(pathIds.ids) : pathIds.ids != null);

    }

    @Override
    public int hashCode() {
        return ids != null ? ids.hashCode() : 0;
    }
}
