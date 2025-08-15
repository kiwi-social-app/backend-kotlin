-- This file runs only on initial container setup

-- Enable pgvector extension
CREATE EXTENSION IF NOT EXISTS vector;

-- If your vector_store table already exists with a non-vector embedding column:
-- 1. Add a new vector column
ALTER TABLE vector_store
    ADD COLUMN IF NOT EXISTS embedding_vec vector;

-- 2. Optional: Migrate existing data (if embedding data was JSON/text)
-- UPDATE vector_store SET embedding_vec = embedding::vector;

-- 3. Replace the original embedding column
ALTER TABLE vector_store
DROP COLUMN IF EXISTS embedding;
ALTER TABLE vector_store
    RENAME COLUMN embedding_vec TO embedding;

-- 4. Create an index for faster similarity searches
CREATE INDEX IF NOT EXISTS ON vector_store USING ivfflat (embedding);
