# Event Store with Spotify and Ticketmaster Integration

## Project Description  
This project implements a **modular system** for the collection, normalization, and storage of **music and event data** from the **Spotify** and **Ticketmaster** APIs.  

The main value proposition is to **unify multiple data sources into a single datamart**, enabling:  
- Richer analytics  
- Efficient queries  
- Cross-source correlation of artist and event data  

---

## Value Proposition  
- **Centralization** of data from multiple APIs  
- **Normalization** and **standardization** of artist and event information  
- **Foundation for analytics** (trend detection, prediction, dashboards)  
- **Extensible architecture** to support additional data providers  

---

##  Justification of API Choices and Datamart Structure  

- **Spotify API**: Provides detailed information about artists and their related data, essential for understanding digital popularity and influence.  
<img width="1538" height="1100" alt="image" src="https://github.com/user-attachments/assets/56def5d7-8bc5-44ab-9b5d-8b9c8a0e5610" />

- **Ticketmaster API**: Offers a large catalog of live events (concerts, shows, performances), providing geographic and availability insights.  
<img width="792" height="636" alt="image" src="https://github.com/user-attachments/assets/11cc1541-e343-4908-95db-68cb84a213ac" />

- **Datamart**: Implemented in the `event-store-builder` module, consolidates both sources into a unified schema for consistent queries and data integrity.  
<img width="410" height="352" alt="image" src="https://github.com/user-attachments/assets/f4e25592-b6dd-4db6-9dd9-a3b67865f172" />

---

##  System Architecture  

### System-Level Architecture  
- **spotify-feeder**: Retrieves and normalizes data from Spotify
- **ticketmaster-feeder**: Retrieves and normalizes data from Ticketmaster  
- **event-store-builder**: Integrates and stores data in the datamart  

---

##  Design Principles and Patterns Applied  

- **Modularity**: Each module operates independently (*Separation of Concerns*).  
- **DAO / Repository Pattern**: For database access and persistence logic.  
- **Factory Method**: To create event and artist objects from API responses.  
- **Observer / Publisher-Subscriber**: Feeders publish events that the event store consumes.  
- **Singleton**: Used for configuration managers and database connections.  

---

##  Main Classes  

### Event Store Builder  
- `Main.java`  
- `EventSubscriber.java`  

### Spotify Feeder  
- `Main.java`  
- `ConfigLoader.java`  
- `SpotifyController.java`  
- `DatabaseManager.java`  
- Models: `Artist`, `ArtistEvent`, `SpotifyResponse`  
- Utilities: `SpotifyApiClient`, `SpotifyParser`, `SpotifyTokenRefresher`, `SpotifyStore`  

### Ticketmaster Feeder  
- `Main.java`  
- `ConfigLoader.java`  
- `TicketmasterController.java`  
- Models: `ArtistEvent`, `TicketmasterEvent`, `TicketmasterResponse`  
- Utilities: `TicketmasterApiClient`, `TicketmasterParser`, `TicketmasterEventCreator`, `TicketmasterEventService`, `TicketmasterParamsBuilder`  

---

##  Compilation and Execution Instructions  
