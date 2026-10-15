
# Combat Clarity

Combat Clarity is an independent RuneLite plugin for Old School RuneScape that displays known NPC combat styles using pixel-art icons above NPCs.

The goal is to make combat styles easier to recognize at a glance without cluttering the game screen.

## Features

- Displays combat style icons above supported NPCs
- Melee: sword icon
- Ranged: bow and arrow icon
- Magic: wizard hat icon
- Displays multiple icons when an NPC has multiple known combat styles
- Adjustable icon size
- Option to enable or disable NPC combat indicators
- Works without requiring the player to attack an NPC

## Combat style data

Combat Clarity uses a bundled JSON database generated from Old School RuneScape Wiki data.

The database currently contains combat style information for 3,910 NPC IDs.

Combat styles are matched using NPC IDs rather than NPC names.

The database is included with the plugin, so combat style lookups do not require live Wiki requests during gameplay.

## Known limitations

- Not every NPC is included in the database.
- Some NPCs may use different attacks depending on their phase or other conditions.
- Icons represent known combat styles, not necessarily the next incoming attack.
- Combat style accuracy depends on the available Wiki data.

## Configuration

Combat Clarity provides the following settings:

- NPC combat indicators: Enable or disable combat style icons.
- Icon size: Adjust the size of the icons.

## Development

Build the project:

    ./gradlew build

Run the development client:

    ./gradlew run

On Windows, use gradlew.bat instead of ./gradlew.

## Credits

Combat style information is derived from the Old School RuneScape Wiki.

Combat Clarity is an independent community project and is not affiliated with or endorsed by Jagex, RuneLite, or the Old School RuneScape Wiki team.

## License

MIT License. See LICENSE.
