-- Fix dates in V2 music family
UPDATE relationships SET start_date = start_date || ' 00:00:00' WHERE graph_id = 'a1b2c3d4-0000-0000-0000-000000000001';

-- Fix dates in V3 rap family
UPDATE relationships SET start_date = start_date || ' 00:00:00' WHERE graph_id = 'b1b2c3d4-0000-0000-0000-000000000001';

-- Fix dates in V4 Tupac influences
UPDATE relationships SET start_date = start_date || ' 00:00:00' WHERE graph_id = 'c1b2c3d4-0000-0000-0000-000000000001';