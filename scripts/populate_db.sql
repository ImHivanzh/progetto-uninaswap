-- Inserimento di alcuni utenti
INSERT INTO utente (idutente, nomeutente, mail, password, numerotelefono) VALUES
(1, 'mario.rossi', 'mario.rossi@email.com', 'Password123!', '1234567890'),
(2, 'luigi.verdi', 'luigi.verdi@email.com', 'Password123!', '0987654321'),
(3, 'anna.bianchi', 'anna.bianchi@email.com', 'Password123!', '1122334455');

-- Inserimento di alcuni annunci
-- Utente 1 (mario.rossi)
INSERT INTO annuncio (idannuncio, titolo, descrizione, categoria, idutente, tipoannuncio, prezzo, stato) VALUES
(1, 'iPhone X', 'iPhone X 64GB in buone condizioni', 'ELETTRONICA', 1, 'VENDITA', 250.00, true),
(2, 'Libro di Analisi 1', 'Libro di analisi matematica 1, come nuovo', 'LIBRI', 1, 'SCAMBIO', NULL, true),
(3, 'Tastiera da Gaming', 'Tastiera meccanica retroilluminata', 'ELETTRONICA', 1, 'REGALO', NULL, true);

-- Utente 2 (luigi.verdi)
INSERT INTO annuncio (idannuncio, titolo, descrizione, categoria, idutente, tipoannuncio, prezzo, stato) VALUES
(4, 'MacBook Pro 2019', 'MacBook Pro 13" 256GB, ottimo stato', 'ELETTRONICA', 2, 'VENDITA', 800.00, true),
(5, 'Appunti di Fisica 1', 'Appunti completi del corso di Fisica 1', 'DISPENSE_E_APPUNTI', 2, 'REGALO', NULL, true);

-- Utente 3 (anna.bianchi)
INSERT INTO annuncio (idannuncio, titolo, descrizione, categoria, idutente, tipoannuncio, oggetto_richiesto, stato) VALUES
(6, 'Monitor 24" Full HD', 'Monitor Samsung 24" in perfette condizioni', 'ELETTRONICA', 3, 'SCAMBIO', 'Webcam di buona qualità', true);

-- Inserimento di alcune proposte
-- Proposte di vendita
-- Proposta di luigi.verdi per l'iPhone X di mario.rossi (accettata)
INSERT INTO vendita (idutente, idannuncio, controofferta, accettato, inattesa) VALUES (2, 1, 230.00, true, false);
-- Proposta di anna.bianchi per l'iPhone X di mario.rossi (rifiutata)
INSERT INTO vendita (idutente, idannuncio, controofferta, accettato, inattesa) VALUES (3, 1, 220.00, false, false);
-- Proposta di mario.rossi per il MacBook Pro di luigi.verdi (in attesa)
INSERT INTO vendita (idutente, idannuncio, controofferta, accettato, inattesa) VALUES (1, 4, 750.00, false, true);

-- Proposte di scambio
-- Proposta di anna.bianchi per il libro di Analisi 1 di mario.rossi (accettata)
INSERT INTO scambio (idutente, idannuncio, propscambio, accettato, inattesa) VALUES (3, 2, 'Scambio con il mio libro di Geometria', true, false);
-- Proposta di luigi.verdi per il monitor di anna.bianchi (in attesa)
INSERT INTO scambio (idutente, idannuncio, propscambio, accettato, inattesa) VALUES (2, 6, 'Ti offro una webcam Logitech C920', false, true);

-- Proposte di regalo
-- Proposta di anna.bianchi per la tastiera di mario.rossi (accettata)
INSERT INTO regalo (idutente, idannuncio, dataprenotazione, accettato, inattesa) VALUES (3, 3, NOW(), true, false);
-- Proposta di mario.rossi per gli appunti di Fisica 1 di luigi.verdi (rifiutata)
INSERT INTO regalo (idutente, idannuncio, dataprenotazione, accettato, inattesa) VALUES (1, 5, NOW(), false, false);

-- Inserimento di alcune recensioni
-- Recensione di luigi.verdi per mario.rossi
INSERT INTO recensione (idutente, idutenterecensito, voto, descrizione) VALUES (2, 1, 5, 'Venditore affidabile, prodotto come da descrizione.');
-- Recensione di anna.bianchi per mario.rossi
INSERT INTO recensione (idutente, idutenterecensito, voto, descrizione) VALUES (3, 1, 4, 'Scambio veloce, tutto ok.');

-- Inserimento di dati di spedizione e ritiro
-- Spedizione per l'iPhone X
INSERT INTO spedizione (datainvio, dataarrivo, indirizzo, numerotelefono, idannuncio, spedito) VALUES ('2024-01-20', '2024-01-23', 'Via Roma 1, 20121 Milano', '0987654321', 1, true);
-- Ritiro per il libro di Analisi 1
INSERT INTO ritiro (sede, orario, data, numerotelefono, ritirato, idannuncio) VALUES ('Biblioteca di Ingegneria', '15:30:00', '2024-01-22', '1122334455', true, 2);
