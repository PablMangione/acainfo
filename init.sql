-- Script de inicialización para PostgreSQL
-- Este se ejecuta como el usuario postgres (superusuario)

-- Crear el usuario acainfo_dev si no existe
DO $$
    BEGIN
        IF NOT EXISTS (SELECT FROM pg_user WHERE usename = 'acainfo_dev') THEN
            CREATE USER acainfo_dev WITH PASSWORD 'dev123';
        END IF;
    END
$$;

-- Dar todos los privilegios al usuario acainfo_dev sobre la base de datos
GRANT ALL PRIVILEGES ON DATABASE acainfo_dev TO acainfo_dev;

-- Hacer que acainfo_dev sea dueño de la base de datos
ALTER DATABASE acainfo_dev OWNER TO acainfo_dev;

-- Conectar a la base de datos acainfo_dev para crear el schema
\c acainfo_dev

-- Dar permisos en el schema public
GRANT ALL ON SCHEMA public TO acainfo_dev;

-- Permitir crear nuevos schemas
GRANT CREATE ON DATABASE acainfo_dev TO acainfo_dev;

-- Mensaje de confirmación
DO $$
    BEGIN
        RAISE NOTICE 'Base de datos inicializada correctamente';
        RAISE NOTICE 'Usuario postgres: contraseña = postgres';
        RAISE NOTICE 'Usuario acainfo_dev: contraseña = dev123';
    END
$$;h