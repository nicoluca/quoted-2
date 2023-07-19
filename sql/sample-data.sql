-- Truncate all tables and restart the IDENTITY sequence
TRUNCATE TABLE quotes RESTART IDENTITY CASCADE;
TRUNCATE TABLE sources RESTART IDENTITY CASCADE;
TRUNCATE TABLE users RESTART IDENTITY CASCADE;

-- Populate users table with 1 users
INSERT INTO users (email) VALUES ('peter@lustig.de');

-- Populate quote table with 4 sources
INSERT INTO sources (name, user_id) VALUES ('The Bible', 1);
INSERT INTO sources (name, user_id) VALUES ('The Quran', 1);
INSERT INTO sources (name, user_id) VALUES ('The Torah', 1);
INSERT INTO sources (name, user_id) VALUES ('The Tibetan Book of the Dead', 1);

-- Populate quote table with sample quotes from the 4 sources
INSERT INTO quotes (source_id, text, user_id) VALUES (1, 'For God so loved the world that he gave his one and only Son, that whoever believes in him shall not perish but have eternal life.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (1, 'For it is by grace you have been saved, through faith—and this is not from yourselves, it is the gift of God— not by works, so that no one can boast.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (1, 'For the wages of sin is death, but the gift of God is eternal life in Christ Jesus our Lord.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (2, 'And We have certainly created man from clay of altered black mud.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (2, 'And We have certainly created man in the best of stature.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (2, 'And We have certainly created man and We know what his soul whispers to him, and We are closer to him than [his] jugular vein.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (3, 'Hear, O Israel: The LORD our God, the LORD is one.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (3, 'You shall love the LORD your God with all your heart and with all your soul and with all your might.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (3, 'And these words that I command you today shall be on your heart.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (4, 'The nature of the mind is clear light; it is unproduced and unobstructed.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (4, 'When a person dies, the mind''s clarity, the luminosity of the mind, dawns.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (4, 'The essential nature of mind is without birth or cessation; it is empty and without identity.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (1, 'For God is not a God of confusion but of peace.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (1, 'Trust in the LORD with all your heart, and do not lean on your own understanding.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (1, 'Do not be anxious about anything, but in everything by prayer and supplication with thanksgiving let your requests be made known to God.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (1, 'In all your ways acknowledge him, and he will make straight your paths.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (1, 'Love is patient and kind; love does not envy or boast; it is not arrogant or rude.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (2, 'Say, "He is Allah, [who is] One, Allah, the Eternal Refuge.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (2, 'Allah does not burden a soul beyond that it can bear.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (2, 'And when My servants ask you concerning Me, indeed I am near. I respond to the invocation of the supplicant when he calls upon Me.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (2, 'And We have certainly created man in a best form.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (2, 'And Allah is the best of planners.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (3, 'Remember the Sabbath day, to keep it holy.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (3, 'You shall not murder.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (3, 'You shall not steal.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (3, 'You shall not take the name of the LORD your God in vain.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (3, 'Honor your father and your mother.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (4, 'Let the winds of the heavens now arise; let the winds blow away my sins.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (4, 'May I attain the transcendental wisdom of the Great Perfection, in which all doubts are clarified.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (4, 'May I recognize whatever appears as my projection, and may I understand the nature of the illusion-like phenomena of the six realms.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (4, 'May I remember the instructions of the lama and unite with the primordial wisdom of the path.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (4, 'May I attain the power to dissolve the body of delusion into the expanse of the ground.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (1, 'Rejoice in the Lord always. I will say it again: Rejoice!', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (1, 'The Lord is my shepherd; I shall not want.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (1, 'Let us not become weary in doing good, for at the proper time we will reap a harvest if we do not give up.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (1, 'Do not be overcome by evil, but overcome evil with good.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (1, 'Commit to the LORD whatever you do, and he will establish your plans.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (2, 'And We have certainly created man from a sperm-drop mixture so that We may test him.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (2, 'And the sun runs [on course] toward its stopping point. That is the determination of the Exalted in Might, the Knowing.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (2, 'And whoever is patient and forgives - indeed, that is of the matters [requiring] determination.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (2, 'Indeed, Allah is with the patient.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (2, 'So whoever does an atom''s weight of good will see it.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (3, 'You shall not commit adultery.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (3, 'You shall not covet your neighbor''s house; you shall not covet your neighbor''s wife, or his male servant, or his female servant, or his ox, or his donkey, or anything that is your neighbor''s.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (3, 'Remember the days of old; consider the years of many generations; ask your father, and he will show you, your elders, and they will tell you.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (3, 'The LORD bless you and keep you; the LORD make his face to shine upon you and be gracious to you; the LORD lift up his countenance upon you and give you peace.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (3, 'You shall walk in all the way that the LORD your God has commanded you, that you may live, and that it may go well with you, and that you may live long in the land that you shall possess.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (4, 'May I not wander in the samsara of confused perception.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (4, 'May I recognize all experiences as being the nature of mind.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (4, 'May I not be frightened by the Peaceful and Wrathful Ones.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (4, 'May I recognize my own projections and know them to be my mind.', 1);
INSERT INTO quotes (source_id, text, user_id) VALUES (4, 'May I not be confused by the pure realms of the peaceful and wrathful deities.', 1);
