CREATE TABLE IF NOT EXISTS clients (
    client_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    email_address TEXT NOT NULL,
    phone_number TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS events (
    event_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    time TEXT NOT NULL,
    attendance INTEGER NOT NULL,
    event_type TEXT NOT NULL CHECK (event_type IN ('Wedding', 'Birthday', 'Seminar')),
    client_id INTEGER NOT NULL,
    FOREIGN KEY (client_id) REFERENCES clients(client_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS wedding_event (
    event_id INTEGER PRIMARY KEY,
    bride_name TEXT,
    groom_name TEXT,
    photographer_required INTEGER NOT NULL CHECK (photographer_required IN (0, 1)),
    FOREIGN KEY (event_id) REFERENCES events(event_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS birthday_event (
    event_id INTEGER PRIMARY KEY,
    age INTEGER NOT NULL,
    theme TEXT,
    number_of_kids INTEGER,
    FOREIGN KEY (event_id) REFERENCES events(event_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS seminar_event (
    event_id INTEGER PRIMARY KEY,
    chief_guest TEXT NOT NULL,
    speaker TEXT NOT NULL,
    topic TEXT NOT NULL,
    FOREIGN KEY (event_id) REFERENCES events(event_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS statistics (
    total_events INTEGER DEFAULT 0,
    total_wedding_events INTEGER DEFAULT 0,
    total_birthday_events INTEGER DEFAULT 0,
    total_seminar_events INTEGER DEFAULT 0
);

CREATE TABLE IF NOT EXISTS credentials (
    username TEXT NOT NULL DEFAULT "admin",
    password TEXT NOT NULL DEFAULT "7P7V9OGd0xAXsgt1bpkQFQ==:7z/Mub6Ez3GilGCgxW307hW3wR1Pt2fPesp5FkhH28Y="
);

INSERT INTO credentials (username, password)
    SELECT "admin", "7P7V9OGd0xAXsgt1bpkQFQ==:7z/Mub6Ez3GilGCgxW307hW3wR1Pt2fPesp5FkhH28Y="
WHERE NOT EXISTS (SELECT 1 FROM credentials WHERE username = 'admin')