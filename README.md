# Minecraft Hypixel Skyblock Plugin

This project is a personal endeavor to recreate and extend core gameplay systems inspired by the popular Hypixel Skyblock Minecraft server, developed as a plugin for the Bukkit/Paper API. It transforms the vanilla Minecraft experience into a rich RPG-like environment with custom mechanics, entities, and progression systems.

## Key Features

*   **Custom Player Attributes:** Dynamic health, defense, strength, speed, and other RPG stats.
*   **Intricate Combat System:** Custom damage calculations, mob interactions, and combat abilities.
*   **Skill Progression:** Farming, Mining, Combat skills with custom experience and rewards.
*   **Advanced Item System:** Custom items with unique attributes, crafting recipes, enchantments, and rarity tiers.
*   **Player Economy & Persistent Data:** In-game currency, bank accounts, and saving/loading player data.
*   **Minion System:** Automated resource generation entities with upgrades and dialogues.
*   **Interactive Menus & Locations:** Custom GUI-driven interfaces and distinct in-game areas.

## Technical Highlights & Architecture

This plugin was meticulously engineered using modern Kotlin principles to manage significant complexity, demonstrating robust software architecture and design patterns:

*   **Modular & Layered Architecture:** Implemented a clear separation of concerns, dividing the system into distinct layers (e.g., Plugin, Manager, Service, Registry, Entity) for enhanced maintainability and extensibility.
*   **Composition over Inheritance & Delegation:** Heavily utilized composition and Kotlin's `by` keyword for interface delegation (e.g., for `MinionHolder`, `Crafter` within `SkyblockPlayer`), effectively mitigating 'God Class' complexity and adhering to the **Single Responsibility Principle**.
*   **Event-Driven Architecture (EDA):** Leveraged the Bukkit API as a central event broker, with various listener and manager components acting as event consumers. This fostered **loose coupling** and responsive system interactions across diverse game domains.
*   **Pattern-Driven Design:** Employed a range of design patterns including:
    *   **Strategy Pattern** for flexible attribute effects and item behaviors.
    *   **Factory Pattern with Reflection** for auto-discovery and registration of new items and attributes, ensuring **zero-configuration extensibility**.
    *   **Repository Pattern** with asynchronous operations (`JsonPlayerDataService`) for thread-safe and non-blocking data persistence.
*   **Robustness & Type Safety:** Focused on writing clean, idiomatic Kotlin code, utilizing its strong type system and null safety features to prevent runtime errors and enhance overall system reliability.

## Technologies Used

*   **Kotlin** (JVM)
*   **Minecraft Bukkit/Paper API** (Game Modding Framework)
*   **Gson** (for JSON serialization/deserialization)
*   **Reflection** (for dynamic class discovery)
