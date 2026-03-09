CREATE TABLE graphs (
    id          TEXT PRIMARY KEY,
    name        TEXT NOT NULL,
    type        TEXT NOT NULL,
    created_at  TIMESTAMP NOT NULL,
    updated_at  TIMESTAMP NOT NULL
);

CREATE TABLE nodes (
    id          TEXT PRIMARY KEY,
    graph_id    TEXT NOT NULL REFERENCES graphs(id),
    label       TEXT NOT NULL,
    type        TEXT NOT NULL
);

CREATE TABLE relationships (
    id          TEXT PRIMARY KEY,
    graph_id    TEXT NOT NULL REFERENCES graphs(id),
    from_id     TEXT NOT NULL REFERENCES nodes(id),
    to_id       TEXT NOT NULL REFERENCES nodes(id),
    type        TEXT NOT NULL,
    start_date  DATE,
    end_date    DATE
);

CREATE TABLE node_properties (
    id            TEXT PRIMARY KEY,
    node_id       TEXT NOT NULL REFERENCES nodes(id),
    name          TEXT NOT NULL,
    value_type    TEXT NOT NULL,
    string_value  TEXT,
    date_value    DATE,
    ref_node_id   TEXT
);
