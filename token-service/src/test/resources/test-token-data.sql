truncate table refresh_tokens;
alter sequence refresh_tokens_id_seq restart with 1;

insert into refresh_tokens (ref_id, username, refresh_token, expires_at, refresh_token_status)
values ('ref_001', 'Starlight123', 'token12345', '2025-12-31T00:30:00.00Z', 'ACCEPTABLE'),
       ('ref_002', 'CosmicDreamer', 'token11121', '2025-12-31T00:30:00.00Z', 'ACCEPTABLE'),
       ('ref_003', 'SolarFlare', 'token16171', '2025-12-31T00:30:00.00Z', 'ACCEPTABLE'),
       ('ref_004', 'PixelPioneer', 'token21222', '2025-12-31T00:30:00.00Z', 'ACCEPTABLE'),
       ('ref_005', 'GalacticHero', 'token25262', '2025-12-31T00:30:00.00Z', 'ACCEPTABLE'),
       ('ref_006', 'CyberSamurai', 'token29292', '2025-12-31T00:30:00.00Z', 'ACCEPTABLE'),
       ('ref_007', 'EchoPhoenix', 'token31313', '2025-12-31T00:30:00.00Z', 'ACCEPTABLE'),
       ('ref_008', 'VelocityViper', 'token33333', '2025-12-31T00:30:00.00Z', 'ACCEPTABLE'),
       ('ref_009', 'DigitalDragon', 'token35353', '2025-12-31T00:30:00.00Z', 'ACCEPTABLE'),
       ('ref_010', 'AetherArtist', 'token37373', '2025-12-31T00:30:00.00Z', 'ACCEPTABLE'),

       ('ref_011', 'Starlight123', 'token12345_R', '2025-12-31T00:30:00.00Z', 'REVOKED'),
       ('ref_012', 'CosmicDreamer', 'token11121_R', '2025-12-31T00:30:00.00Z', 'REVOKED'),
       ('ref_013', 'SolarFlare', 'token16171_R', '2025-12-31T00:30:00.00Z', 'REVOKED'),
       ('ref_014', 'PixelPioneer', 'token21222_R', '2025-12-31T00:30:00.00Z', 'REVOKED'),
       ('ref_015', 'GalacticHero', 'token25262_R', '2025-12-31T00:30:00.00Z', 'REVOKED'),
       ('ref_016', 'CyberSamurai', 'token29292_R', '2025-12-31T00:30:00.00Z', 'REVOKED'),
       ('ref_017', 'EchoPhoenix', 'token31313_R', '2025-12-31T00:30:00.00Z', 'REVOKED'),
       ('ref_018', 'VelocityViper', 'token33333_R', '2025-12-31T00:30:00.00Z', 'REVOKED'),
       ('ref_019', 'DigitalDragon', 'token35353_R', '2025-12-31T00:30:00.00Z', 'REVOKED'),
       ('ref_020', 'AetherArtist', 'token37373_R', '2025-12-31T00:30:00.00Z', 'REVOKED'),

       ('ref_021', 'Starlight123', 'token12345_E', '2025-12-30T00:30:00.00Z', 'ACCEPTABLE'),
       ('ref_022', 'CosmicDreamer', 'token11121_E', '2025-12-30T00:30:00.00Z', 'ACCEPTABLE'),
       ('ref_023', 'SolarFlare', 'token16171_E', '2025-12-30T00:30:00.00Z', 'ACCEPTABLE'),
       ('ref_024', 'PixelPioneer', 'token21222_E', '2025-12-30T00:30:00.00Z', 'ACCEPTABLE'),
       ('ref_025', 'GalacticHero', 'token25262_E', '2025-12-30T00:30:00.00Z', 'ACCEPTABLE'),
       ('ref_026', 'CyberSamurai', 'token29292_E', '2025-12-30T00:30:00.00Z', 'REVOKED'),
       ('ref_027', 'EchoPhoenix', 'token31313_E', '2025-12-30T00:30:00.00Z', 'REVOKED'),
       ('ref_028', 'VelocityViper', 'token33333_E', '2025-12-30T00:30:00.00Z', 'REVOKED'),
       ('ref_029', 'DigitalDragon', 'token35353_E', '2025-12-30T00:30:00.00Z', 'REVOKED'),
       ('ref_030', 'AetherArtist', 'token37373_E', '2025-12-30T00:30:00.00Z', 'REVOKED');





