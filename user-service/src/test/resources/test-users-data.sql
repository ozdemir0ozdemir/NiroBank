alter sequence users_id_sequence restart with 1;

insert into users (username, password, role)
values
    ('Starlight123', 'Twinkle@2024', 'USER'),
    ('NeonNinja', 'Shadow#123', 'USER'),
    ('PixelPioneer', 'Matrix$567', 'USER'),
    ('MysticMage', 'SpellCaster%2024', 'USER'),
    ('EchoPhoenix', 'Rebirth@Dawn', 'USER'),
    ('NebulaKnight', 'Space&Time', 'USER'),
    ('AetherArtist', 'Creative*Flow', 'USER'),
    ('CosmicDreamer', 'Gal@xyPass1', 'ADMIN'),
    ('QuantumLeap', 'Photon&Wave', 'ADMIN'),
    ('GalacticHero', 'StarWars!2024', 'ADMIN'),
    ('AuroraBorealis', 'Northern*Lights', 'ADMIN'),
    ('VelocityViper', 'Speed$Strike', 'ADMIN'),
    ('StormStriker', 'Thunder$Bolt', 'ADMIN'),
    ('MoonWalker', 'Apollo!11', 'MANAGER'),
    ('SolarFlare', 'Sunshine*99', 'MANAGER'),
    ('EclipseMaster', 'Lunar#Eclipse', 'MANAGER'),
    ('CyberSamurai', 'Blade&Shield', 'MANAGER'),
    ('ZenithZero', 'Peak#Horizon', 'MANAGER'),
    ('DigitalDragon', 'Cyber@Roar', 'MANAGER'),
    ('NimbusNavigator', 'Cloud9#Sky', 'MANAGER');

