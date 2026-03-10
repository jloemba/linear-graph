package com.lineage.infrastructure.web.controller;

import com.lineage.application.port.in.graph.CreateGraphUseCase;
import com.lineage.application.port.in.graph.CreateGraphUseCase.CreateGraphCommand;
import com.lineage.application.port.in.graph.CreateGraphUseCase.GraphId;
import com.lineage.application.port.in.query.FindNodesByTypeUseCase;
import com.lineage.application.port.in.query.FindNodesByTypeUseCase.Query;
import com.lineage.application.port.in.query.FindAllGraphsUseCase;
import com.lineage.application.port.in.query.FindAllGraphsUseCase.Result;
import com.lineage.application.port.in.query.TracePathUseCase;
import com.lineage.application.port.in.query.FindAncestorsUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/graphs")
public class GraphController {

    private final CreateGraphUseCase createGraphUseCase;
    private final FindNodesByTypeUseCase findNodesByTypeUseCase;
    private final TracePathUseCase tracePathUseCase;
    private final FindAncestorsUseCase findAncestorsUseCase;
    private final FindAllGraphsUseCase findAllGraphsUseCase;

    public GraphController(
            CreateGraphUseCase createGraphUseCase,
            FindNodesByTypeUseCase findNodesByTypeUseCase,
            TracePathUseCase tracePathUseCase,
            FindAncestorsUseCase findAncestorsUseCase,
        FindAllGraphsUseCase findAllGraphsUseCase) {
        this.createGraphUseCase = createGraphUseCase;
        this.findNodesByTypeUseCase = findNodesByTypeUseCase;
        this.tracePathUseCase = tracePathUseCase;
        this.findAncestorsUseCase = findAncestorsUseCase;
        this.findAllGraphsUseCase = findAllGraphsUseCase;
        
    }

    @GetMapping
    public ResponseEntity<Result> findAllGraphs() {
            return ResponseEntity.ok(findAllGraphsUseCase.execute());
    }

    // -------------------------
    // POST /api/graphs
    // -------------------------
    @PostMapping
    public ResponseEntity<GraphIdResponse> createGraph(@RequestBody CreateGraphRequest request) {
        GraphId graphId = createGraphUseCase.execute(
                new CreateGraphCommand(request.name(), request.type()));

        return ResponseEntity.ok(new GraphIdResponse(graphId.value()));
    }

    // -------------------------
    // GET /api/graphs/{graphId}/nodes?type=GENRE
    // -------------------------
    @GetMapping("/{graphId}/nodes")
    public ResponseEntity<FindNodesByTypeUseCase.Result> findNodesByType(
            @PathVariable UUID graphId,
            @RequestParam String type) {
        FindNodesByTypeUseCase.Result result = findNodesByTypeUseCase.execute(
                new Query(graphId, type));

        return ResponseEntity.ok(result);
    }

    // -------------------------
    // GET /api/graphs/{graphId}/path?fromId=...&toId=...
    // -------------------------
    @GetMapping("/{graphId}/path")
    public ResponseEntity<TracePathUseCase.PathResult> tracePath(
            @PathVariable UUID graphId,
            @RequestParam UUID fromId,
            @RequestParam UUID toId) {
        TracePathUseCase.PathResult result = tracePathUseCase.execute(
                new TracePathUseCase.TracePathQuery(graphId, fromId, toId));

        return ResponseEntity.ok(result);
    }

    // -------------------------
    // GET /api/graphs/{graphId}/ancestors?nodeId=...&type=...&maxDepth=...
    // -------------------------
    @GetMapping("/{graphId}/ancestors")
    public ResponseEntity<FindAncestorsUseCase.Result> findAncestors(
            @PathVariable UUID graphId,
            @RequestParam UUID nodeId,
            @RequestParam String relationshipType,
            @RequestParam(defaultValue = "10") int maxDepth) {
        FindAncestorsUseCase.Result result = findAncestorsUseCase.execute(
                new FindAncestorsUseCase.Query(graphId, nodeId, relationshipType, maxDepth));

        return ResponseEntity.ok(result);
    }

    // -------------------------
    // Records de requête / réponse
    // -------------------------
    record CreateGraphRequest(String name, String type) {
    }

    record GraphIdResponse(UUID id) {
    }
}