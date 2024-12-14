truncate table users;
alter sequence users_id_sequence restart with 1;


insert into users (user_id, username, password, role)
values
    (nextVal('users_id_sequence'), 'Starlight123', 'Twinkle@2024', 'USER'),
    (nextVal('users_id_sequence'), 'NeonNinja', 'Shadow#123', 'USER'),
    (nextVal('users_id_sequence'), 'PixelPioneer', 'Matrix$567', 'USER'),
    (nextVal('users_id_sequence'), 'MysticMage', 'SpellCaster%2024', 'USER'),
    (nextVal('users_id_sequence'), 'EchoPhoenix', 'Rebirth@Dawn', 'USER'),
    (nextVal('users_id_sequence'), 'NebulaKnight', 'Space&Time', 'USER'),
    (nextVal('users_id_sequence'), 'AetherArtist', 'Creative*Flow', 'USER'),
    (nextVal('users_id_sequence'), 'CosmicDreamer', 'Gal@xyPass1', 'ADMIN'),
    (nextVal('users_id_sequence'), 'QuantumLeap', 'Photon&Wave', 'ADMIN'),
    (nextVal('users_id_sequence'), 'GalacticHero', 'StarWars!2024', 'ADMIN'),
    (nextVal('users_id_sequence'), 'AuroraBorealis', 'Northern*Lights', 'ADMIN'),
    (nextVal('users_id_sequence'), 'VelocityViper', 'Speed$Strike', 'ADMIN'),
    (nextVal('users_id_sequence'), 'StormStriker', 'Thunder$Bolt', 'ADMIN'),
    (nextVal('users_id_sequence'), 'MoonWalker', 'Apollo!11', 'MANAGER'),
    (nextVal('users_id_sequence'), 'SolarFlare', 'Sunshine*99', 'MANAGER'),
    (nextVal('users_id_sequence'), 'EclipseMaster', 'Lunar#Eclipse', 'MANAGER'),
    (nextVal('users_id_sequence'), 'CyberSamurai', 'Blade&Shield', 'MANAGER'),
    (nextVal('users_id_sequence'), 'ZenithZero', 'Peak#Horizon', 'MANAGER'),
    (nextVal('users_id_sequence'), 'DigitalDragon', 'Cyber@Roar', 'MANAGER'),
    (nextVal('users_id_sequence'), 'NimbusNavigator', 'Cloud9#Sky', 'MANAGER');

