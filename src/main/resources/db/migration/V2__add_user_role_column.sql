-- Every table other than "users" in this project is created automatically
-- by Hibernate's ddl-auto=update (see application.properties) - that's fine
-- for pure schema DDL, but adding a NOT NULL column to a table that already
-- has rows needs a real backfill, which is what this migration is for.
--
-- Flyway migrations run BEFORE Hibernate's ddl-auto=update on every boot, so
-- on a brand new database "users" doesn't exist yet at this point - Hibernate
-- creates it later, already NOT NULL, with no rows to backfill. This is
-- guarded to a no-op in that case; it only does real work against a database
-- that already has a "users" table (and so may already have rows in it).
--
-- Every user created before roles existed effectively had unrestricted
-- access, so they all become SUPERADMIN here rather than being locked out
-- of their own system. Users created after this migration default to USER
-- and start with no permissions - see CreateUserUseCase.
DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables
        WHERE table_schema = 'public' AND table_name = 'users'
    ) THEN
        ALTER TABLE users ADD COLUMN IF NOT EXISTS role VARCHAR(20);
        UPDATE users SET role = 'SUPERADMIN' WHERE role IS NULL;
        ALTER TABLE users ALTER COLUMN role SET NOT NULL;
    END IF;
END $$;
