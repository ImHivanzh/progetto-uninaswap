# UninaSwap

Piattaforma di scambio e compravendita per studenti universitari. UninaSwap permette agli utenti di pubblicare annunci per vendere, scambiare o regalare oggetti, gestire proposte e lasciare recensioni.

## Caratteristiche

- **Gestione Annunci**: Pubblica annunci di vendita, scambio o regalo con immagini
- **Sistema di Proposte**: Invia e ricevi proposte per gli annunci
- **Recensioni**: Sistema di valutazione tra utenti dopo transazioni completate
- **Ricerca Avanzata**: Filtra annunci per categoria, tipo e prezzo
- **Gestione Consegna**: Supporto per spedizione e ritiro in sede
- **Profilo Utente**: Visualizza storico annunci, proposte e recensioni

## Architettura

Il progetto segue il pattern **MVC (Model-View-Controller)**:

- **Model** (`src/model`): Entità del dominio (Annuncio, Utente, Proposta, Recensione, ecc.)
- **View** (`src/gui`): Interfacce grafiche Swing
- **Controller** (`src/controller`): Logica di business e coordinamento
- **DAO** (`src/dao`): Data Access Objects per persistenza database
- **DB** (`src/db`): Gestione connessione al database
- **Utils** (`src/utils`): Classi di utilità (Logger, SessionManager, DataCheck, ecc.)

### Vantaggi del Pattern MVC

Il pattern MVC offre numerosi vantaggi per lo sviluppo e la manutenzione del progetto:

- **Separazione delle Responsabilità**: Ogni componente ha un ruolo ben definito, rendendo il codice più organizzato e comprensibile
- **Manutenibilità**: Le modifiche a una componente (es. interfaccia grafica) non richiedono cambiamenti alle altre (es. logica di business)
- **Testabilità**: La logica di business nei Controller e nei DAO può essere testata indipendentemente dall'interfaccia grafica
- **Riusabilità**: I Model e i DAO possono essere riutilizzati in contesti diversi (es. API REST, applicazione mobile)
- **Sviluppo Parallelo**: Team diversi possono lavorare simultaneamente su View, Controller e Model senza conflitti
- **Scalabilità**: Facilita l'aggiunta di nuove funzionalità mantenendo la struttura esistente

## Requisiti

- **Java**: JDK 8 o superiore
- **Database**: PostgreSQL
- **Librerie**:
  - PostgreSQL JDBC Driver (42.7.8)
  - FlatLaf (3.6.2) - Look and Feel moderno per Swing
  - JFreeChart (1.5.6) - Grafici e visualizzazioni statistiche

## Interfaccia Grafica

Il progetto utilizza **FlatLaf** (Flat Look and Feel) per l'interfaccia grafica Swing.

### Perché FlatLaf?

FlatLaf è stato scelto per i seguenti motivi:

- **Design Moderno**: Offre un'interfaccia piatta e contemporanea, superando l'aspetto datato del Look and Feel predefinito di Swing
- **Leggibilità**: Migliora la leggibilità del testo e la chiarezza degli elementi UI con font e spaziature ottimizzate
- **Coerenza**: Garantisce un aspetto uniforme su diverse piattaforme (Windows, macOS, Linux)
- **Facilità d'uso**: Si integra perfettamente con Swing senza richiedere modifiche al codice esistente
- **Personalizzabile**: Permette di adattare colori e stili mantenendo un design professionale
- **Leggero**: Non appesantisce l'applicazione, mantenendo ottime performance

L'utilizzo di FlatLaf rende l'applicazione più gradevole visivamente e migliora l'esperienza utente complessiva, pur mantenendo la semplicità e l'affidabilità di Swing.

## Qualità del Codice

Il progetto utilizza **SonarQube** per l'analisi statica del codice e il mantenimento di elevati standard di qualità.

### Perché SonarQube?

SonarQube è stato integrato nel processo di sviluppo per:

- **Rilevamento Automatico di Bug**: Identifica potenziali bug e vulnerabilità di sicurezza prima che raggiungano la produzione
- **Code Smells**: Rileva problemi di manutenibilità come codice duplicato, complessità ciclomatica elevata e violazioni delle convenzioni
- **Standard di Codifica**: Garantisce l'aderenza alle best practice Java e alle convenzioni di naming
- **Metriche di Qualità**: Fornisce metriche dettagliate su copertura dei test, duplicazione del codice e debito tecnico
- **Miglioramento Continuo**: Permette di monitorare l'evoluzione della qualità del codice nel tempo

L'utilizzo di SonarQube ha contribuito a mantenere il codice pulito, leggibile e manutenibile, riducendo il debito tecnico e facilitando l'evoluzione futura del progetto.

## Struttura del Progetto

```
uninaswap/
├── src/
│   ├── controller/     # Controller MVC
│   ├── dao/           # Data Access Objects
│   ├── db/            # Gestione connessione database
│   ├── exception/     # Eccezioni custom
│   ├── gui/           # Interfacce grafiche Swing
│   ├── img/           # Risorse immagini
│   ├── model/         # Entità del dominio
│   │   └── enums/     # Enumerazioni
│   └── utils/         # Classi di utilità
├── test/              # Test unitari
├── lib/               # Dipendenze esterne (JAR)
├── bin/               # File compilati
├── out/               # Output compilazione
└── scripts/           # Script SQL e utilità
```

## Documentazione Classi

### Model

#### Annuncio

Classe base astratta che rappresenta un annuncio generico sulla piattaforma. Contiene le informazioni comuni a tutti i tipi di annunci (vendita, scambio, regalo).

**Campi:**
- `idAnnuncio`: Identificativo univoco dell'annuncio
- `utente`: Utente proprietario dell'annuncio
- `titolo`: Titolo descrittivo dell'annuncio
- `descrizione`: Descrizione dettagliata dell'oggetto
- `categoria`: Categoria merceologica (es. ELETTRONICA, LIBRI)
- `tipoAnnuncio`: Tipo di annuncio (VENDITA, SCAMBIO, REGALO)
- `spedizione`: Flag che indica se è disponibile la spedizione (true) o solo ritiro (false)
- `stato`: Stato attivo/disattivo dell'annuncio
- `immagini`: Lista delle immagini associate all'annuncio

**Metodi:**
- `Annuncio()`: Costruttore vuoto
- `Annuncio(Utente, String, String, Categoria, TipoAnnuncio)`: Costruttore con parametri principali
- `Annuncio(int, String, String, Categoria, Utente, TipoAnnuncio)`: Costruttore completo con ID
- `getConsegnaLabel()`: Restituisce l'etichetta testuale per la modalità di consegna ("Spedizione" o "Ritiro in sede")

#### Utente

Rappresenta un utente registrato sulla piattaforma con le sue credenziali e informazioni di contatto.

**Campi:**
- `idUtente`: Identificativo univoco dell'utente
- `username`: Nome utente per il login
- `email`: Indirizzo email dell'utente
- `password`: Password hashata per l'autenticazione
- `numeroTelefono`: Numero di telefono per contatti

**Metodi:**
- `Utente()`: Costruttore vuoto
- `Utente(int, String, String, String, String)`: Costruttore con tutti i campi (id, username, password, email, numeroTelefono)

#### Vendita (extends Annuncio)

Specializzazione di Annuncio per oggetti in vendita. Aggiunge il campo prezzo.

**Campi:**
- `prezzo`: Prezzo richiesto per l'oggetto in euro

**Metodi:**
- `Vendita()`: Costruttore vuoto
- `Vendita(double)`: Costruttore con prezzo

#### Scambio (extends Annuncio)

Specializzazione di Annuncio per proposte di scambio. Specifica quale oggetto si desidera ricevere in cambio.

**Campi:**
- `oggettoRichiesto`: Descrizione dell'oggetto che si desidera ricevere in cambio

**Metodi:**
- `Scambio(int, String, String, Categoria, Utente, String)`: Costruttore completo con ID e oggetto richiesto
- `Scambio(String, String, Categoria, Utente, String)`: Costruttore senza ID (per nuovi annunci)

#### Regalo (extends Annuncio)

Specializzazione di Annuncio per oggetti offerti gratuitamente.

**Metodi:**
- `Regalo(int, String, String, Categoria, Utente)`: Costruttore completo con ID
- `Regalo(String, String, Categoria, Utente)`: Costruttore senza ID (per nuovi annunci)

#### Recensione

Rappresenta una recensione lasciata da un utente ad un altro dopo una transazione completata.

**Campi:**
- `voto`: Valutazione numerica (tipicamente da 1 a 5)
- `descrizione`: Commento testuale della recensione
- `utenteRecensore`: Utente che lascia la recensione
- `utenteRecensito`: Utente che riceve la recensione

**Metodi:**
- `Recensione()`: Costruttore vuoto
- `Recensione(int, String, Utente, Utente)`: Costruttore con voto, descrizione, recensore e recensito

#### Immagini

Rappresenta un'immagine associata ad un annuncio, memorizzata come array di byte.

**Campi:**
- `idImmagine`: Identificativo univoco dell'immagine
- `immagine`: Contenuto binario dell'immagine
- `annuncio`: Annuncio a cui l'immagine è associata

**Metodi:**
- `Immagini()`: Costruttore vuoto
- `Immagini(byte[], Annuncio)`: Costruttore con immagine e annuncio associato

#### Spedizione

Contiene i dettagli di una spedizione per la consegna di un oggetto.

**Campi:**
- `idSpedizione`: Identificativo univoco della spedizione
- `indirizzo`: Indirizzo di destinazione
- `numeroTelefono`: Numero di telefono per contatto
- `dataInvio`: Data prevista di invio
- `dataArrivo`: Data prevista di arrivo
- `spedito`: Flag che indica se l'oggetto è stato spedito
- `annuncio`: Annuncio associato alla spedizione

**Metodi:**
- `Spedizione()`: Costruttore vuoto
- `Spedizione(int, String, String, Date, Date, boolean, Annuncio)`: Costruttore completo

#### Ritiro

Contiene i dettagli per il ritiro in sede di un oggetto.

**Campi:**
- `idRitiro`: Identificativo univoco del ritiro
- `sede`: Luogo di ritiro (es. "Complesso Monte Sant'Angelo")
- `orario`: Fascia oraria per il ritiro
- `data`: Data prevista per il ritiro
- `numeroTelefono`: Numero di telefono per contatto
- `ritirato`: Flag che indica se l'oggetto è stato ritirato
- `annuncio`: Annuncio associato al ritiro

**Metodi:**
- `Ritiro()`: Costruttore vuoto
- `Ritiro(int, String, String, Date, String, boolean, Annuncio)`: Costruttore completo

#### PropostaRiepilogo (record)

Record immutabile che rappresenta un riepilogo di una proposta (ricevuta o inviata) con tutte le informazioni necessarie per visualizzarla nell'interfaccia.

**Campi:**
- `annuncio`: Annuncio a cui si riferisce la proposta
- `utenteCoinvolto`: Utente che ha fatto o ricevuto la proposta
- `dettaglio`: Dettagli specifici della proposta (prezzo offerto, oggetto proposto per scambio)
- `accettata`: Flag che indica se la proposta è stata accettata
- `inattesa`: Flag che indica se la proposta è ancora in attesa di risposta
- `immagine`: Immagine associata alla proposta (per scambi)

**Metodi:**
- `getStatoTestuale()`: Restituisce lo stato della proposta come stringa ("Accettata", "In attesa", "Rifiutata")

#### ReportProposte (record)

Record immutabile contenente statistiche aggregate sulle proposte di un utente, utilizzato per generare report e grafici.

**Campi:**
- `totaleVendita`: Numero totale di proposte per annunci di vendita
- `accettateVendita`: Numero di proposte accettate per vendite
- `totaleScambio`: Numero totale di proposte per scambi
- `accettateScambio`: Numero di proposte accettate per scambi
- `totaleRegalo`: Numero totale di proposte per regali
- `accettateRegalo`: Numero di proposte accettate per regali
- `valoreMinimoVendita`: Prezzo minimo offerto nelle proposte di vendita
- `valoreMassimoVendita`: Prezzo massimo offerto nelle proposte di vendita
- `valoreMedioVendita`: Prezzo medio delle proposte di vendita

### Enums

#### Categoria

Enumerazione delle categorie merceologiche disponibili per classificare gli annunci.

**Valori:**
- `CARTOLERIA`: Materiale per ufficio e scuola
- `ELETTRONICA`: Dispositivi elettronici e accessori
- `DISPENSE_E_APPUNTI`: Materiale didattico universitario
- `SPORT`: Attrezzatura sportiva
- `MUSICA`: Strumenti musicali e accessori
- `ABBIGLIAMENTO`: Vestiti e accessori moda
- `LIBRI`: Libri di testo e narrativa
- `ALTRO`: Categoria generica per oggetti non classificabili

**Campi:**
- `descrizione`: Descrizione testuale della categoria

#### TipoAnnuncio

Enumerazione dei tipi di annuncio disponibili sulla piattaforma.

**Valori:**
- `SCAMBIO`: Proposta di scambio tra oggetti
- `VENDITA`: Vendita a pagamento
- `REGALO`: Cessione gratuita

**Campi:**
- `descrizione`: Descrizione testuale del tipo

#### StatoProposta

Enumerazione degli stati possibili di una proposta.

**Valori:**
- `IN_ATTESA`: Proposta inviata ma non ancora valutata
- `ACCETTATA`: Proposta accettata dal destinatario
- `RIFIUTATA`: Proposta rifiutata dal destinatario

**Campi:**
- `descrizione`: Descrizione testuale dello stato

**Metodi:**
- `fromFlags(boolean accettata, boolean inattesa)`: Determina lo stato della proposta dai flag booleani del database

#### StatoConsegna

Enumerazione degli stati del processo di consegna di un oggetto.

**Valori:**
- `IN_ATTESA`: In attesa di definire i dettagli di consegna
- `DA_SPEDIRE`: Spedizione programmata ma non ancora effettuata
- `DA_RITIRARE`: Ritiro programmato ma non ancora effettuato
- `CONCLUSO`: Consegna completata con successo
- `RIFIUTATO`: Consegna annullata o rifiutata

**Campi:**
- `descrizione`: Descrizione testuale dello stato

### DAO

#### UtenteDAO

Data Access Object per la gestione degli utenti nel database. Gestisce registrazione, autenticazione e recupero informazioni utente.

**Campi:**
- `con`: Connessione al database PostgreSQL

**Metodi:**
- `UtenteDAO()`: Costruttore che inizializza la connessione al database
- `registraUtente(String username, String email, String password, String numeroTelefono)`: Registra un nuovo utente dopo validazione dei dati (email valida, password forte, telefono valido, username univoco)
- `registraUtente(Utente)`: Registra un utente usando un'istanza del modello
- `autenticaUtente(String username, String password)`: Verifica le credenziali e restituisce l'utente se valide, null altrimenti
- `getUserByID(int id)`: Recupera un utente dal suo ID
- `getUserByUsername(String username)`: Recupera un utente dal suo username
- `esisteUtente(String username)`: Verifica se un username è già registrato
- `aggiornaPassword(String username, String nuovaPassword)`: Aggiorna la password di un utente esistente

#### AnnuncioDAO

Data Access Object per la gestione degli annunci. Gestisce pubblicazione, ricerca e recupero annunci dal database.

**Campi:**
- `con`: Connessione al database PostgreSQL

**Metodi pubblici:**
- `AnnuncioDAO()`: Costruttore che inizializza la connessione al database
- `pubblicaAnnuncio(Annuncio)`: Inserisce un nuovo annuncio nel database e restituisce l'ID generato
- `findAll()`: Recupera tutti gli annunci attivi dal database
- `search(String testoRicerca, String categoria, String tipo, Double prezzoMax)`: Ricerca annunci con filtri opzionali (testo, categoria, tipo, prezzo massimo)
- `findAllByUtente(Utente)`: Recupera tutti gli annunci pubblicati da un utente specifico

**Metodi privati:**
- `mapResultSetToAnnuncio(ResultSet)`: Converte una riga del ResultSet in un oggetto Annuncio del tipo appropriato
- `parseCategoria(String)`: Converte una stringa in enum Categoria
- `parseTipoAnnuncio(String)`: Converte una stringa in enum TipoAnnuncio
- `creaAnnuncioPerTipo(TipoAnnuncio, String, String, Categoria, Utente, ResultSet)`: Factory method che crea l'istanza corretta di Annuncio (Vendita, Scambio o Regalo) in base al tipo
- `readSpedizione(ResultSet)`: Legge il flag spedizione dal ResultSet

#### PropostaDAO

Data Access Object per la gestione delle proposte. Gestisce l'inserimento, modifica, eliminazione e recupero delle proposte per annunci di vendita, scambio e regalo.

**Campi:**
- `con`: Connessione al database PostgreSQL
- `SQL_PROPOSTE_RICEVUTE`: Query SQL per recuperare le proposte ricevute
- `SQL_PROPOSTE_INVIATE`: Query SQL per recuperare le proposte inviate

**Metodi pubblici:**
- `PropostaDAO()`: Costruttore che inizializza la connessione al database
- `inserisciPropostaVendita(Utente, Annuncio, double prezzo)`: Inserisce una proposta di acquisto con un prezzo offerto
- `inserisciPropostaScambio(Utente, Annuncio, String oggettoOfferto)`: Inserisce una proposta di scambio con descrizione dell'oggetto offerto
- `inserisciPropostaScambio(Utente, Annuncio, String oggettoOfferto, byte[] immagine)`: Inserisce una proposta di scambio con immagine dell'oggetto offerto
- `inserisciPropostaRegalo(Utente, Annuncio)`: Inserisce una richiesta per un regalo (senza dettagli aggiuntivi)
- `getProposteRicevute(int idUtente)`: Recupera tutte le proposte ricevute da un utente per i suoi annunci
- `getProposteInviate(int idUtente)`: Recupera tutte le proposte inviate da un utente ad altri annunci
- `aggiornaEsitoProposta(int idAnnuncio, String tabella, String tipoAnnuncio, boolean accettata, boolean inattesa)`: Aggiorna lo stato di una proposta (accettata/rifiutata/in attesa)
- `eliminaProposta(int idAnnuncio, String tabella, String tipoAnnuncio)`: Elimina una proposta dal database
- `modificaPropostaVendita(int idUtente, int idAnnuncio, double nuovoPrezzo)`: Modifica il prezzo offerto in una proposta di vendita
- `modificaPropostaScambio(int idUtente, int idAnnuncio, String nuovoOggetto, byte[] nuovaImmagine)`: Modifica l'oggetto offerto in una proposta di scambio
- `getReportProposte(int idUtente)`: Genera statistiche aggregate sulle proposte di un utente

**Metodi privati:**
- `getProposte(int idUtente, String sqlQuery)`: Metodo generico per recuperare proposte con una query specifica
- `resolveTabellaProposta(String tipoAnnuncio)`: Determina il nome della tabella database in base al tipo di annuncio

#### RecensioneDAO

Data Access Object per la gestione delle recensioni tra utenti.

**Campi:**
- `con`: Connessione al database PostgreSQL

**Metodi:**
- `RecensioneDAO()`: Costruttore che inizializza la connessione al database
- `inserisciRecensione(Recensione)`: Inserisce una nuova recensione nel database
- `getRecensioniRicevute(Utente)`: Recupera tutte le recensioni ricevute da un utente
- `hannoTransazioneCompletata(Utente recensore, Utente recensito)`: Verifica se due utenti hanno completato almeno una transazione insieme (necessario per poter lasciare recensioni)

#### ImmaginiDAO

Data Access Object per la gestione delle immagini associate agli annunci.

**Campi:**
- `con`: Connessione al database PostgreSQL

**Metodi:**
- `ImmaginiDAO()`: Costruttore che inizializza la connessione al database
- `salvaImmagine(Immagini)`: Salva un'immagine nel database associandola ad un annuncio
- `getImmaginiByAnnuncio(Annuncio)`: Recupera tutte le immagini associate ad un annuncio
- `getPrimaImmagine(Annuncio)`: Recupera solo la prima immagine di un annuncio (usata per anteprime)

#### SpedizioneDAO

Data Access Object per la gestione dei dettagli di spedizione.

**Campi:**
- `con`: Connessione al database PostgreSQL

**Metodi:**
- `SpedizioneDAO()`: Costruttore che inizializza la connessione al database
- `inserisciSpedizione(Date dataInvio, Date dataArrivo, String indirizzo, String telefono, int idAnnuncio, int idUtente)`: Inserisce i dettagli di una spedizione programmata
- `getSpedizioneByAnnuncio(int idAnnuncio)`: Recupera i dettagli di spedizione per un annuncio
- `aggiornaStatoSpedizione(int idSpedizione, boolean spedito)`: Aggiorna lo stato della spedizione (spedito/non spedito)

#### RitiroDAO

Data Access Object per la gestione dei dettagli di ritiro in sede.

**Campi:**
- `con`: Connessione al database PostgreSQL

**Metodi:**
- `RitiroDAO()`: Costruttore che inizializza la connessione al database
- `inserisciRitiro(String sede, Time orario, Date data, String telefono, int idAnnuncio)`: Inserisce i dettagli di un ritiro programmato
- `getRitiroByAnnuncio(int idAnnuncio)`: Recupera i dettagli di ritiro per un annuncio
- `aggiornaStatoRitiro(int idRitiro, boolean ritirato)`: Aggiorna lo stato del ritiro (ritirato/non ritirato)

### Controller

#### MainController

Controller principale della bacheca annunci. Gestisce la navigazione, la ricerca e la visualizzazione degli annunci in evidenza.

**Campi:**
- `MAX_ANNUNCI_EVIDENZA`: Numero massimo di annunci da mostrare in evidenza (6)
- `view`: Riferimento alla vista MainApp
- `annuncioDAO`: DAO per accesso agli annunci
- `immaginiDAO`: DAO per accesso alle immagini

**Metodi pubblici:**
- `MainController(MainApp)`: Costruttore che inizializza il controller e registra i listener
- `avvia()`: Avvia il flusso principale, mostrando il login se necessario o la bacheca se l'utente è già autenticato
- `actionPerformed(ActionEvent)`: Gestisce tutti gli eventi UI (click su pulsanti, ricerca, ecc.)

**Metodi privati:**
- `registraListener()`: Registra i listener per tutti i componenti UI
- `mostraLogin()`: Apre la finestra di login
- `apriProfilo()`: Apre il profilo dell'utente corrente
- `apriPubblicaAnnuncio()`: Apre la finestra per pubblicare un nuovo annuncio
- `eseguiRicerca()`: Esegue la ricerca annunci con i filtri selezionati
- `parsePrezzoMax(String)`: Converte il testo del prezzo massimo in Double, gestendo errori
- `resetRicerca()`: Ripristina tutti i filtri di ricerca ai valori predefiniti
- `eseguiLogout()`: Effettua il logout e torna alla schermata di login
- `caricaAnnunciInEvidenza()`: Carica e mostra gli annunci in evidenza (con foto)
- `estraiPrimaImmagine(Annuncio)`: Recupera la prima immagine di un annuncio
- `apriDettaglio(ActionEvent)`: Apre la finestra di dettaglio per un annuncio selezionato
- `aggiornaTitoloUtente()`: Aggiorna il titolo della finestra con l'username corrente

#### LoginController

Controller per la gestione del login utente.

**Campi:**
- `view`: Riferimento alla vista LoginForm
- `utenteDAO`: DAO per autenticazione utente
- `onLoginSuccess`: Callback opzionale da eseguire dopo login riuscito

**Metodi:**
- `LoginController(LoginForm)`: Costruttore base
- `LoginController(LoginForm, Runnable)`: Costruttore con callback post-login
- `initListeners()`: Inizializza i listener per i componenti UI
- `controllaLogin()`: Verifica le credenziali e autentica l'utente
- `avviaMainApp()`: Apre la finestra principale dopo login riuscito

#### RegistrazioneController

Controller per la gestione della registrazione di nuovi utenti.

**Campi:**
- `view`: Riferimento alla vista RegistrazioneForm
- `utenteDAO`: DAO per registrazione utente

**Metodi:**
- `RegistrazioneController(RegistrazioneForm)`: Costruttore che inizializza il controller
- `initListeners()`: Inizializza i listener per i componenti UI
- `registraUtente()`: Valida i dati inseriti e registra il nuovo utente

#### PubblicaAnnuncioController

Controller per la pubblicazione di nuovi annunci.

**Campi:**
- `view`: Riferimento alla vista PubblicaAnnuncio
- `annuncioDAO`: DAO per inserimento annunci
- `immaginiDAO`: DAO per salvataggio immagini

**Metodi:**
- `PubblicaAnnuncioController(PubblicaAnnuncio)`: Costruttore che inizializza il controller
- `pubblica()`: Valida i dati del form e pubblica l'annuncio con le immagini
- `salvaImmaginiPerAnnuncio(Annuncio, List<File>)`: Salva tutte le immagini selezionate associandole all'annuncio

#### ProfiloController

Controller principale per la gestione del profilo utente. Coordina la visualizzazione di annunci, proposte e recensioni, delegando la gestione delle proposte a PropostaHandler e il caricamento dati a ProfiloDataLoader.

**Campi:**
- `view`: Riferimento alla vista Profilo
- `utenteTarget`: Utente di cui si sta visualizzando il profilo
- `mostraDatiSensibili`: Flag che indica se mostrare dati sensibili (true per profilo proprio, false per profili altrui)
- `propostaHandler`: Gestore delle operazioni sulle proposte
- `dataLoader`: Gestore del caricamento dati dal database
- `listaAnnunci`: Lista degli annunci dell'utente
- `proposteRicevute`: Lista delle proposte ricevute
- `proposteInviate`: Lista delle proposte inviate

**Metodi:**
- `ProfiloController(Profilo)`: Costruttore per visualizzare il profilo dell'utente corrente
- `ProfiloController(Profilo, Utente)`: Costruttore per visualizzare il profilo di un altro utente
- `creaDAO(DAOSupplier, String)`: Factory method generico per creare DAO con gestione errori
- `setupInteraction()`: Configura i listener per l'interazione utente
- `caricaDati()`: Carica tutti i dati del profilo (annunci, proposte, recensioni)
- `handlePropostaRicevuta(int indice)`: Gestisce il click su una proposta ricevuta
- `handlePropostaInviata(int indice)`: Gestisce il click su una proposta inviata
- `validaSelezioneProposta(int indice, List<PropostaRiepilogo>)`: Valida la selezione di una proposta dalla lista
- `handleRecensioneDaProposta(boolean ricevuta)`: Apre la finestra per scrivere una recensione da una proposta
- `validaPropostaPerModifica()`: Valida che una proposta possa essere modificata

#### PropostaHandler

Classe helper che gestisce tutta la logica complessa delle proposte (ricevute e inviate). Separata da ProfiloController per ridurre la complessità e migliorare la manutenibilità.

**Campi:**
- `view`: Riferimento alla vista Profilo
- `propostaDAO`: DAO per operazioni sulle proposte
- `spedizioneDAO`: DAO per gestione spedizioni
- `ritiroDAO`: DAO per gestione ritiri
- `consegnaHelper`: Helper per dialog di spedizione/ritiro
- `utenteTarget`: Utente di cui si sta gestendo il profilo

**Metodi pubblici:**
- `PropostaHandler(Profilo, PropostaDAO, SpedizioneDAO, RitiroDAO, Utente)`: Costruttore con tutte le dipendenze
- `handlePropostaRicevuta(PropostaRiepilogo)`: Gestisce il click su una proposta ricevuta, mostrando opzioni appropriate in base allo stato
- `handlePropostaInviata(PropostaRiepilogo)`: Gestisce il click su una proposta inviata, mostrando opzioni appropriate
- `aggiornaEsitoProposta(PropostaRiepilogo, Utente, boolean accettata, boolean inattesa, String tipoAnnuncio)`: Aggiorna lo stato di una proposta nel database
- `eliminaProposta(PropostaRiepilogo, Utente, String tipoAnnuncio)`: Elimina una proposta dal database
- `handleModificaProposta(PropostaRiepilogo)`: Apre il dialog per modificare una proposta inviata
- `handleAnnullaProposta(PropostaRiepilogo)`: Annulla (elimina) una proposta inviata
- `formatStato(PropostaRiepilogo)`: Determina lo stato di consegna di una proposta

**Metodi privati:**
- `mostraDialogProposta(PropostaRiepilogo, boolean ricevuta)`: Mostra il dialog con le opzioni appropriate per la proposta
- `handlePropostaAccettata(PropostaRiepilogo, boolean ricevuta, String tipoAnnuncio, boolean spedizione)`: Gestisce azioni su proposte accettate
- `handlePropostaRifiutata(PropostaRiepilogo, boolean ricevuta, String tipoAnnuncio)`: Gestisce azioni su proposte rifiutate
- `handlePropostaInAttesa(PropostaRiepilogo, boolean ricevuta, String tipoAnnuncio, boolean spedizione)`: Gestisce azioni su proposte in attesa
- `buildOpzioniAccettata(PropostaRiepilogo, StatoConsegna, boolean ricevuta, boolean spedizione, int scelta)`: Costruisce le opzioni del dialog per proposte accettate
- `handleAzionePropostaAccettata(String azione, PropostaRiepilogo, int idAnnuncio, boolean ricevuta)`: Esegue l'azione selezionata su una proposta accettata
- `buildOpzioniInAttesa(boolean ricevuta, boolean spedizione)`: Costruisce le opzioni del dialog per proposte in attesa
- `validaDatiProposta(PropostaRiepilogo, Utente)`: Valida che i dati della proposta siano completi
- `aggiornaStatoConsegna(int idAnnuncio, boolean spedizione, String tipoAnnuncio)`: Aggiorna lo stato di spedizione/ritiro
- `verificaDettagliConsegna(int idAnnuncio)`: Verifica se esistono dettagli di consegna per un annuncio
- `inserisciDettagliConsegna(PropostaRiepilogo)`: Apre il dialog per inserire dettagli di spedizione/ritiro
- `visualizzaDettagliConsegna(PropostaRiepilogo)`: Mostra i dettagli di spedizione/ritiro esistenti
- `buildDettaglioProposta(PropostaRiepilogo, String tipoAnnuncio)`: Costruisce la stringa di dettaglio della proposta
- `isPropostaScambio(PropostaRiepilogo)`: Verifica se una proposta è di tipo scambio
- `mostraImmagineProposta(PropostaRiepilogo)`: Mostra l'immagine associata ad una proposta di scambio
- `apriScriviRecensione(Utente)`: Apre la finestra per scrivere una recensione

#### ProfiloDataLoader

Classe helper che gestisce il caricamento di tutti i dati del profilo dal database. Separata da ProfiloController per migliorare la separazione delle responsabilità.

**Campi:**
- `view`: Riferimento alla vista Profilo
- `recensioneDAO`: DAO per caricamento recensioni
- `annuncioDAO`: DAO per caricamento annunci
- `propostaDAO`: DAO per caricamento proposte
- `propostaHandler`: Handler per formattazione proposte

**Metodi pubblici:**
- `ProfiloDataLoader(Profilo, RecensioneDAO, AnnuncioDAO, PropostaDAO, PropostaHandler)`: Costruttore con tutte le dipendenze
- `caricaDatiCompleti(Utente, boolean mostraDatiSensibili)`: Carica tutti i dati del profilo e restituisce un record DatiProfilo

**Metodi privati:**
- `impostaDatiUtente(Utente, boolean mostraDatiSensibili)`: Imposta i dati anagrafici dell'utente nella vista
- `caricaRecensioni(Utente)`: Carica e visualizza le recensioni ricevute dall'utente
- `caricaAnnunci(Utente)`: Carica gli annunci pubblicati dall'utente
- `caricaProposteRicevute(Utente)`: Carica le proposte ricevute dall'utente
- `caricaProposteInviate(Utente)`: Carica le proposte inviate dall'utente
- `aggiungiPropostaRicevutaAllaVista(PropostaRiepilogo)`: Aggiunge una proposta ricevuta alla lista UI
- `aggiungiPropostaInviataAllaVista(PropostaRiepilogo)`: Aggiunge una proposta inviata alla lista UI

#### DettaglioAnnuncioController

Controller per la visualizzazione dei dettagli di un annuncio. Gestisce il caricamento delle immagini e l'apertura del dialog per fare proposte.

**Campi:**
- `view`: Riferimento alla vista DettaglioAnnuncio
- `annuncio`: Annuncio di cui mostrare i dettagli
- `immaginiDAO`: DAO per caricamento immagini

**Metodi:**
- `DettaglioAnnuncioController(DettaglioAnnuncio, Annuncio)`: Costruttore che inizializza il controller e carica i dati
- `caricaImmagini()`: Carica tutte le immagini dell'annuncio e le visualizza
- `setupInteraction()`: Configura i listener per i pulsanti (profilo utente, fai proposta)
- `apriProfilo()`: Apre il profilo del proprietario dell'annuncio
- `apriPropostaDialog()`: Apre il dialog per fare una proposta sull'annuncio

#### FaiPropostaController

Controller per l'invio di proposte su annunci. Gestisce la validazione e l'inserimento di proposte di vendita, scambio o regalo.

**Campi:**
- `view`: Riferimento alla vista FaiProposta
- `annuncio`: Annuncio per cui si sta facendo la proposta
- `propostaDAO`: DAO per inserimento proposte

**Metodi:**
- `FaiPropostaController(FaiProposta, Annuncio)`: Costruttore che inizializza il controller
- `setupInteraction()`: Configura i listener per i pulsanti
- `inviaProposta()`: Valida i dati inseriti e invia la proposta appropriata (vendita con prezzo, scambio con oggetto/immagine, regalo senza dettagli)

#### ModificaPropostaController

Controller per la modifica di proposte già inviate. Permette di cambiare il prezzo offerto o l'oggetto proposto per lo scambio.

**Campi:**
- `view`: Riferimento alla vista ModificaProposta
- `proposta`: Proposta da modificare
- `propostaDAO`: DAO per aggiornamento proposte

**Metodi:**
- `ModificaPropostaController(ModificaProposta, PropostaRiepilogo)`: Costruttore che inizializza il controller con la proposta esistente
- `setupInteraction()`: Configura i listener per i pulsanti
- `modificaProposta()`: Valida i nuovi dati e aggiorna la proposta nel database

#### PassDimenticataController

Controller per il recupero password. Gestisce la verifica dell'username e l'aggiornamento della password.

**Campi:**
- `view`: Riferimento alla vista PassDimenticata
- `utenteDAO`: DAO per aggiornamento password

**Metodi:**
- `PassDimenticataController(PassDimenticata)`: Costruttore che inizializza il controller
- `setupInteraction()`: Configura i listener per i pulsanti
- `recuperaPassword()`: Verifica che l'username esista e aggiorna la password dopo validazione

#### ReportProposteController

Controller per la visualizzazione di statistiche e grafici sulle proposte di un utente. Utilizza JFreeChart per generare grafici a barre.

**Campi:**
- `view`: Riferimento alla vista ReportProposte
- `propostaDAO`: DAO per recupero statistiche

**Metodi:**
- `ReportProposteController(ReportProposte, int idUtente)`: Costruttore che carica e visualizza il report per l'utente specificato
- `caricaReport()`: Recupera le statistiche dal database e genera il grafico
- `creaGrafico(ReportProposte)`: Crea un grafico a barre con le statistiche delle proposte (totali vs accettate per ogni tipo)

#### ScriviRecensioneController

Controller per la scrittura di recensioni. Verifica che gli utenti abbiano completato una transazione prima di permettere la recensione.

**Campi:**
- `view`: Riferimento alla vista ScriviRecensione
- `utenteRecensito`: Utente che riceverà la recensione
- `recensioneDAO`: DAO per inserimento recensioni

**Metodi:**
- `ScriviRecensioneController(ScriviRecensione, Utente)`: Costruttore che inizializza il controller
- `setupInteraction()`: Configura i listener per i pulsanti
- `inviaRecensione()`: Valida i dati (voto e descrizione) e inserisce la recensione dopo aver verificato che esista una transazione completata tra i due utenti

### Utils

#### SessionManager

Gestisce la sessione dell'utente corrente utilizzando il pattern Singleton. Mantiene un riferimento all'utente autenticato accessibile da tutta l'applicazione.

**Pattern:** Singleton (istanza unica condivisa)

**Campi:**
- `instance`: Istanza singleton statica
- `utenteCorrente`: Utente attualmente autenticato

**Metodi:**
- `getInstance()`: Restituisce l'istanza singleton (thread-safe con synchronized)
- `login(Utente)`: Memorizza l'utente autenticato nella sessione
- `logout()`: Cancella la sessione corrente (imposta utente a null)
- `getUtente()`: Restituisce l'utente corrente o null se non autenticato

#### Logger

Classe di utilità per il logging di errori e informazioni. Stampa messaggi formattati con timestamp su console.

**Campi:**
- `FORMATTER`: Formattatore per timestamp (formato: yyyy-MM-dd HH:mm:ss)

**Metodi:**
- `error(String messaggio)`: Logga un messaggio di errore con timestamp
- `error(String messaggio, Throwable e)`: Logga un messaggio di errore con eccezione e stack trace
- `info(String messaggio)`: Logga un messaggio informativo con timestamp

#### DataCheck

Classe di utilità per la validazione dei dati utente. Contiene metodi statici per verificare email, numeri di telefono e password.

**Costanti:**
- `MIN_PASSWORD_LENGTH`: Lunghezza minima password (8 caratteri)

**Metodi:**
- `isValidEmail(String email)`: Verifica che l'email abbia un formato valido usando regex
- `isValidPhoneNumber(String telefono)`: Verifica che il numero di telefono contenga esattamente 10 cifre
- `isStrongPassword(String password)`: Verifica che la password sia forte (min 8 caratteri, almeno una maiuscola, un numero e un carattere speciale)

#### Validator

Classe di utilità per validazione generica con eccezioni. Lancia IllegalArgumentException se le condizioni non sono soddisfatte.

**Metodi:**
- `requireNonNull(Object obj, String nomeParametro)`: Verifica che un oggetto non sia null
- `requirePositive(int valore, String nomeParametro)`: Verifica che un intero sia positivo (> 0)
- `requirePositive(double valore, String nomeParametro)`: Verifica che un double sia positivo (> 0)
- `requireNonEmpty(String stringa, String nomeParametro)`: Verifica che una stringa non sia null o vuota

#### WindowManager

Classe di utilità per la gestione delle finestre Swing. Gestisce l'apertura di nuove finestre e la chiusura di quelle precedenti.

**Metodi:**
- `open(Window nuovaFinestra, Window vecchiaFinestra)`: Apre una nuova finestra, centra sullo schermo e chiude la finestra precedente

#### FormHelper

Classe di utilità per la creazione di componenti form comuni. Fornisce metodi factory per spinner di data/ora e gestione layout.

**Metodi:**
- `createDateSpinner()`: Crea uno spinner per la selezione di date
- `createTimeSpinner()`: Crea uno spinner per la selezione di orari
- `createFormConstraints()`: Crea GridBagConstraints predefiniti per layout form
- `addFormRow(JPanel, GridBagConstraints, int riga, String etichetta, JComponent componente)`: Aggiunge una riga al form con etichetta e componente

#### Constanti

Classe contenente costanti stringa utilizzate in tutta l'applicazione per evitare duplicazione di literal.

**Costanti:**
- `TIPO_VENDITA`: Stringa "Vendita"
- `TIPO_SCAMBIO`: Stringa "Scambio"
- `TIPO_REGALO`: Stringa "Regalo"
- `CATEGORIA_TUTTE`: Stringa "Tutte"
- `TIPO_TUTTI`: Stringa "Tutti"
- `TABELLA_VENDITA`: Nome tabella database "vendita"
- `TABELLA_SCAMBIO`: Nome tabella database "scambio"
- `TABELLA_REGALO`: Nome tabella database "regalo"

#### ConsegnaHelper

Classe helper per la gestione dei dialog di spedizione e ritiro. Raccoglie i dati necessari dall'utente e li salva nel database.

**Metodi:**
- `mostraDialogSpedizione(Annuncio)`: Mostra un dialog per inserire i dettagli di spedizione (indirizzo, telefono, date) e restituisce true se confermato
- `mostraDialogRitiro(Annuncio)`: Mostra un dialog per inserire i dettagli di ritiro (sede, orario, data, telefono) e restituisce true se confermato
- `inserisciSpedizione(SpedizioneDAO, int idAnnuncio, int idUtente)`: Salva i dettagli di spedizione nel database
- `inserisciRitiro(RitiroDAO, int idAnnuncio)`: Salva i dettagli di ritiro nel database

#### ImmaginePropostaHelper

Classe helper per la gestione delle immagini nelle proposte di scambio. Permette di selezionare e visualizzare immagini.

**Metodi:**
- `selezionaImmagine()`: Apre un file chooser per selezionare un'immagine e la restituisce come array di byte
- `mostraImmagine(byte[] immagine)`: Visualizza un'immagine in un dialog modale

### Database

#### DbConnection

Gestisce la connessione al database PostgreSQL utilizzando il pattern Singleton. Fornisce un'unica istanza di connessione condivisa da tutti i DAO.

**Pattern:** Singleton (istanza unica condivisa)

**Campi:**
- `instance`: Istanza singleton statica
- `connection`: Connessione JDBC al database PostgreSQL
- Credenziali database (hardcoded per progetto universitario):
  - `DB_USER`: "postgres.wzzmgxzgtpsvazdwdbqr"
  - `DB_PASS`: "UninaSwapDB"
  - `DB_URL`: URL di connessione al database Supabase PostgreSQL

**Metodi:**
- `getInstance()`: Restituisce l'istanza singleton della classe
- `getConnection()`: Restituisce la connessione al database, creandola se necessario

**Note:** Le credenziali sono hardcoded nel codice per permettere al professore di accedere al database durante la valutazione del progetto universitario.