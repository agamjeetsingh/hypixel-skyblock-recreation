# Minecraft Hypixel Skyblock Plugin **(Work In Progress)**

This project is a personal endeavor to recreate and extend core gameplay systems inspired by the popular Hypixel Skyblock Minecraft server, developed as a plugin for the Bukkit/Paper API. It transforms the vanilla Minecraft experience into a rich RPG-like environment with custom mechanics, entities, and progression systems.

**Current Status:** This project is a work-in-progress. The architectural foundations and many core systems are designed and implemented, but **not all features are fully functional or bug-free**. This README details the architectural vision and implemented structures, which formed the primary learning objectives.

**[Click here for comprehensive project documentation and design details on the Wiki.](https://github.com/agamjeetsingh/hypixel-skyblock-recreation/wiki)**

## Key Features (Implemented Architecture & Partial Functionality)

*   **Custom Player Attributes:** Dynamic health, defense, strength, speed, and other RPG stats. (Core system implemented; **health calculation and display are partially functional/may contain bugs**).
*   **Intricate Combat System:** Custom damage calculations, mob interactions, and combat abilities. (**Basic combat calculations are in place; more advanced features and stability are in progress**).
*   **Skill Progression:** Farming, Mining, Combat skills with custom experience and rewards. (**Core skill system is functional, allowing XP gain and level-ups**).
*   **Advanced Item System:** Custom items with unique attributes, crafting recipes, enchantments, and rarity tiers. (**Item definitions, attributes, and rarity tiers are largely implemented; crafting system is under development and currently non-functional**).
*   **Player Economy & Persistent Data:** In-game currency, bank accounts, and saving/loading player data. (**Economy systems (coins, bank accounts) are implemented; player data loading/saving is partially functional and may exhibit bugs**).
*   **Minion System:** Automated resource generation entities with upgrades and dialogues. (**Core Minion logic and definitions are in place**).
*   **Interactive Menus & Locations:** Custom GUI-driven interfaces and distinct in-game areas. (**Menu structures and location definitions are implemented**).

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
