package com.lineage.application.port.in.graph;
import com.lineage.domain.model.Property;

import java.util.*;

public interface AddNodeUseCase {

    NodeId execute(AddNodeCommand command);

    record AddNodeCommand(
        UUID nodeId,
        String label,
        Set<Property> properties,
        String type
    ) {}

    record NodeId(UUID value) {}
}