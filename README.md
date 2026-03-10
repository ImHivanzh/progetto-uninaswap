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
- **Utils** (`src/utils`): Classi di utilità (Logger, SessionManager, DataCheck, ecc.)

## Requisiti

- **Java**: JDK 8 o superiore
- **Database**: PostgreSQL
- **Librerie**:
  - PostgreSQL JDBC Driver (42.7.8)
  - FlatLaf (Look and Feel moderno per Swing)

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

## Struttura del Progetto

```
uninaswap/
├── src/
│   ├── controller/     # Controller MVC
│   ├── dao/           # Data Access Objects
│   ├── exception/     # Eccezioni custom
│   ├── gui/           # Interfacce grafiche Swing
│   ├── model/         # Entità del dominio
│   │   └── enums/     # Enumerazioni
│   └── utils/         # Classi di utilità
├── lib/               # Dipendenze esterne
├── out/               # File compilati
└── scripts/           # Script di utilità
```

## Documentazione Classi

### Model

#### Annuncio
```
- idAnnuncio: int
- utente: Utente
- titolo: String
- descrizione: String
- categoria: Categoria
- tipoAnnuncio: TipoAnnuncio
- spedizione: Boolean
- stato: boolean
- immagini: List<Immagini>

+ Annuncio()
+ Annuncio(Utente, String, String, Categoria, TipoAnnuncio)
+ Annuncio(int, String, String, Categoria, Utente, TipoAnnuncio)
+ getConsegnaLabel(): String
```

#### Utente
```
- idUtente: int
- username: String
- email: String
- password: String
- numeroTelefono: String

+ Utente()
+ Utente(int, String, String, String, String)
```

#### Vendita (extends Annuncio)
```
- prezzo: double

+ Vendita()
+ Vendita(double)
```

#### Scambio (extends Annuncio)
```
- oggettoRichiesto: String

+ Scambio(int, String, String, Categoria, Utente, String)
+ Scambio(String, String, Categoria, Utente, String)
```

#### Regalo (extends Annuncio)
```
+ Regalo(int, String, String, Categoria, Utente)
+ Regalo(String, String, Categoria, Utente)
```

#### Recensione
```
- voto: int
- descrizione: String
- utenteRecensore: Utente
- utenteRecensito: Utente

+ Recensione()
+ Recensione(int, String, Utente, Utente)
```

#### Immagini
```
- idImmagine: int
- immagine: byte[]
- annuncio: Annuncio

+ Immagini()
+ Immagini(byte[], Annuncio)
```

#### Spedizione
```
- idSpedizione: int
- indirizzo: String
- numeroTelefono: String
- dataInvio: Date
- dataArrivo: Date
- spedito: boolean
- annuncio: Annuncio

+ Spedizione()
+ Spedizione(int, String, String, Date, Date, boolean, Annuncio)
```

#### Ritiro
```
- idRitiro: int
- sede: String
- orario: String
- data: Date
- numeroTelefono: String
- ritirato: boolean
- annuncio: Annuncio

+ Ritiro()
+ Ritiro(int, String, String, Date, String, boolean, Annuncio)
```

#### PropostaRiepilogo (record)
```
+ annuncio: Annuncio
+ utenteCoinvolto: Utente
+ dettaglio: String
+ accettata: boolean
+ inattesa: boolean
+ immagine: byte[]

+ getStatoTestuale(): String
```

#### ReportProposte (record)
```
+ totaleVendita: int
+ accettateVendita: int
+ totaleScambio: int
+ accettateScambio: int
+ totaleRegalo: int
+ accettateRegalo: int
+ valoreMinimoVendita: double
+ valoreMassimoVendita: double
+ valoreMedioVendita: double
```

### Enums

#### Categoria
```
CARTOLERIA, ELETTRONICA, DISPENSE_E_APPUNTI, SPORT, MUSICA, ABBIGLIAMENTO, LIBRI, ALTRO

- descrizione: String
```

#### TipoAnnuncio
```
SCAMBIO, VENDITA, REGALO

- descrizione: String
```

#### StatoProposta
```
IN_ATTESA, ACCETTATA, RIFIUTATA

- descrizione: String

+ fromFlags(boolean, boolean): StatoProposta
```

#### StatoConsegna
```
IN_ATTESA, DA_SPEDIRE, DA_RITIRARE, CONCLUSO, RIFIUTATO

- descrizione: String
```

### DAO

#### UtenteDAO
```
- con: Connection

+ UtenteDAO()
+ registraUtente(String, String, String, String): boolean
+ registraUtente(Utente): boolean
+ autenticaUtente(String, String): Utente
+ getUserByID(int): Utente
+ getUserByUsername(String): Utente
+ esisteUtente(String): boolean
+ aggiornaPassword(String, String): boolean
```

#### AnnuncioDAO
```
- con: Connection

+ AnnuncioDAO()
+ pubblicaAnnuncio(Annuncio): int
+ findAll(): List<Annuncio>
+ search(String, String, String, Double): List<Annuncio>
+ findAllByUtente(Utente): List<Annuncio>
- mapResultSetToAnnuncio(ResultSet): Annuncio
- parseCategoria(String): Categoria
- parseTipoAnnuncio(String): TipoAnnuncio
- creaAnnuncioPerTipo(TipoAnnuncio, String, String, Categoria, Utente, ResultSet): Annuncio
- readSpedizione(ResultSet): Boolean
```

#### PropostaDAO
```
- con: Connection
- SQL_PROPOSTE_RICEVUTE: String
- SQL_PROPOSTE_INVIATE: String

+ PropostaDAO()
+ inserisciPropostaVendita(Utente, Annuncio, double): boolean
+ inserisciPropostaScambio(Utente, Annuncio, String): boolean
+ inserisciPropostaScambio(Utente, Annuncio, String, byte[]): boolean
+ inserisciPropostaRegalo(Utente, Annuncio): boolean
+ getProposteRicevute(int): List<PropostaRiepilogo>
+ getProposteInviate(int): List<PropostaRiepilogo>
+ aggiornaEsitoProposta(int, String, String, boolean, boolean): boolean
+ eliminaProposta(int, String, String): boolean
+ modificaPropostaVendita(int, int, double): boolean
+ modificaPropostaScambio(int, int, String, byte[]): boolean
+ getReportProposte(int): ReportProposte
- getProposte(int, String): List<PropostaRiepilogo>
- resolveTabellaProposta(String): String
```

#### RecensioneDAO
```
- con: Connection

+ RecensioneDAO()
+ inserisciRecensione(Recensione): boolean
+ getRecensioniRicevute(Utente): List<Recensione>
+ hannoTransazioneCompletata(Utente, Utente): boolean
```

#### ImmaginiDAO
```
- con: Connection

+ ImmaginiDAO()
+ salvaImmagine(Immagini): boolean
+ getImmaginiByAnnuncio(Annuncio): List<Immagini>
+ getPrimaImmagine(Annuncio): byte[]
```

#### SpedizioneDAO
```
- con: Connection

+ SpedizioneDAO()
+ inserisciSpedizione(Date, Date, String, String, int, int): boolean
+ getSpedizioneByAnnuncio(int): Spedizione
+ aggiornaStatoSpedizione(int, boolean): boolean
```

#### RitiroDAO
```
- con: Connection

+ RitiroDAO()
+ inserisciRitiro(String, Time, Date, String, int): boolean
+ getRitiroByAnnuncio(int): Ritiro
+ aggiornaStatoRitiro(int, boolean): boolean
```

### Controller

#### MainController
```
- MAX_ANNUNCI_EVIDENZA: int
- view: MainApp
- annuncioDAO: AnnuncioDAO
- immaginiDAO: ImmaginiDAO

+ MainController(MainApp)
+ avvia(): void
+ actionPerformed(ActionEvent): void
- registraListener(): void
- mostraLogin(): void
- apriProfilo(): void
- apriPubblicaAnnuncio(): void
- eseguiRicerca(): void
- parsePrezzoMax(String): Double
- resetRicerca(): void
- eseguiLogout(): void
- caricaAnnunciInEvidenza(): void
- estraiPrimaImmagine(Annuncio): byte[]
- apriDettaglio(ActionEvent): void
- aggiornaTitoloUtente(): void
```

#### LoginController
```
- view: LoginForm
- utenteDAO: UtenteDAO
- onLoginSuccess: Runnable

+ LoginController(LoginForm)
+ LoginController(LoginForm, Runnable)
- initListeners(): void
- controllaLogin(): void
- avviaMainApp(): void
```

#### RegistrazioneController
```
- view: RegistrazioneForm
- utenteDAO: UtenteDAO

+ RegistrazioneController(RegistrazioneForm)
- initListeners(): void
- registraUtente(): void
```

#### PubblicaAnnuncioController
```
- view: PubblicaAnnuncio
- annuncioDAO: AnnuncioDAO
- immaginiDAO: ImmaginiDAO

+ PubblicaAnnuncioController(PubblicaAnnuncio)
+ pubblica(): void
- salvaImmaginiPerAnnuncio(Annuncio, List<File>): void
```

#### ProfiloController
```
- view: Profilo
- utenteTarget: Utente
- mostraDatiSensibili: boolean
- propostaHandler: PropostaHandler
- dataLoader: ProfiloDataLoader
- listaAnnunci: List<Annuncio>
- proposteRicevute: List<PropostaRiepilogo>
- proposteInviate: List<PropostaRiepilogo>

+ ProfiloController(Profilo)
+ ProfiloController(Profilo, Utente)
- creaDAO(DAOSupplier, String): T
- setupInteraction(): void
- caricaDati(): void
- handlePropostaRicevuta(int): void
- handlePropostaInviata(int): void
- validaSelezioneProposta(int, List): PropostaRiepilogo
- handleRecensioneDaProposta(boolean): void
- validaPropostaPerModifica(): PropostaRiepilogo
```

#### PropostaHandler
```
- view: Profilo
- propostaDAO: PropostaDAO
- spedizioneDAO: SpedizioneDAO
- ritiroDAO: RitiroDAO
- consegnaHelper: ConsegnaHelper
- utenteTarget: Utente

+ PropostaHandler(Profilo, PropostaDAO, SpedizioneDAO, RitiroDAO, Utente)
+ handlePropostaRicevuta(PropostaRiepilogo): void
+ handlePropostaInviata(PropostaRiepilogo): void
+ aggiornaEsitoProposta(PropostaRiepilogo, Utente, boolean, boolean, String): boolean
+ eliminaProposta(PropostaRiepilogo, Utente, String): void
+ handleModificaProposta(PropostaRiepilogo): void
+ handleAnnullaProposta(PropostaRiepilogo): void
+ formatStato(PropostaRiepilogo): StatoConsegna
- mostraDialogProposta(PropostaRiepilogo, boolean): void
- handlePropostaAccettata(PropostaRiepilogo, boolean, String, boolean): void
- handlePropostaRifiutata(PropostaRiepilogo, boolean, String): void
- handlePropostaInAttesa(PropostaRiepilogo, boolean, String, boolean): void
- buildOpzioniAccettata(PropostaRiepilogo, StatoConsegna, boolean, boolean, int): List<String>
- handleAzionePropostaAccettata(String, PropostaRiepilogo, int, boolean): boolean
- buildOpzioniInAttesa(boolean, boolean): Object[]
- validaDatiProposta(PropostaRiepilogo, Utente): boolean
- aggiornaStatoConsegna(int, boolean, String): void
- verificaDettagliConsegna(int): boolean
- inserisciDettagliConsegna(PropostaRiepilogo): void
- visualizzaDettagliConsegna(PropostaRiepilogo): void
- buildDettaglioProposta(PropostaRiepilogo, String): String
- isPropostaScambio(PropostaRiepilogo): boolean
- mostraImmagineProposta(PropostaRiepilogo): void
- apriScriviRecensione(Utente): void
```

#### ProfiloDataLoader
```
- view: Profilo
- recensioneDAO: RecensioneDAO
- annuncioDAO: AnnuncioDAO
- propostaDAO: PropostaDAO
- propostaHandler: PropostaHandler

+ ProfiloDataLoader(Profilo, RecensioneDAO, AnnuncioDAO, PropostaDAO, PropostaHandler)
+ caricaDatiCompleti(Utente, boolean): DatiProfilo
- impostaDatiUtente(Utente, boolean): void
- caricaRecensioni(Utente): void
- caricaAnnunci(Utente): List<Annuncio>
- caricaProposteRicevute(Utente): List<PropostaRiepilogo>
- caricaProposteInviate(Utente): List<PropostaRiepilogo>
- aggiungiPropostaRicevutaAllaVista(PropostaRiepilogo): void
- aggiungiPropostaInviataAllaVista(PropostaRiepilogo): void
```

### Utils

#### SessionManager
```
- instance: SessionManager
- utenteCorrente: Utente

- SessionManager()
+ getInstance(): SessionManager
+ login(Utente): void
+ logout(): void
```

#### Logger
```
- FORMATTER: DateTimeFormatter

+ error(String): void
+ error(String, Throwable): void
+ info(String): void
```

#### DataCheck
```
+ MIN_PASSWORD_LENGTH: int

+ isValidEmail(String): boolean
+ isValidPhoneNumber(String): boolean
+ isStrongPassword(String): boolean
```

#### Validator
```
+ requireNonNull(Object, String): void
+ requirePositive(int, String): void
+ requirePositive(double, String): void
+ requireNonEmpty(String, String): void
```

#### WindowManager
```
+ open(Window, Window): void
```

#### FormHelper
```
+ createDateSpinner(): JSpinner
+ createTimeSpinner(): JSpinner
+ createFormConstraints(): GridBagConstraints
+ addFormRow(JPanel, GridBagConstraints, int, String, JComponent): void
```

#### Constanti
```
+ TIPO_VENDITA: String
+ TIPO_SCAMBIO: String
+ TIPO_REGALO: String
+ CATEGORIA_TUTTE: String
+ TIPO_TUTTI: String
+ TABELLA_VENDITA: String
+ TABELLA_SCAMBIO: String
+ TABELLA_REGALO: String
```